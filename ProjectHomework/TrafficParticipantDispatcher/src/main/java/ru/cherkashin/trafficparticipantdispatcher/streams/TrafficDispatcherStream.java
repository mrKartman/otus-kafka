package ru.cherkashin.trafficparticipantdispatcher.streams;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.*;
import ru.cherkashin.trafficparticipantdispatcher.dto.TrafficParticipant;
import ru.cherkashin.trafficparticipantdispatcher.serde.AppMaterialized;
import ru.cherkashin.trafficparticipantdispatcher.serde.AppSerdes;

import java.text.DecimalFormat;
import java.time.Duration;
import java.util.*;

public class TrafficDispatcherStream {
    private static final String BOOTSTRAP_SERVERS = "localhost:9092,localhost:9093,localhost:9094";
    private static final String TRAFFIC_PARTICIPANT_TOPIC = "traffic-participant";
    private static final String TRAFFIC_VIOLATOR_TOPIC = "traffic-violator";
    private static final String TRAFFIC_AVERAGE_SPEED_TOPIC = "traffic-average-speed";
    private static final String AVG_SPEED_STORE = "avg-speed-store";
    private static final Integer ONE_MINUTE = 60 * 1000;
    private static final String AVG_SPEED_MSG_FORMAT = "Средняя скорость автомобилей у данной камеры составила %s км/ч !" +
            "Было зафикисировано %s автомобилей.";

    public static void runStream() {
        TrafficDispatcherStream dispatcherStream = new TrafficDispatcherStream();
        Topology topology = dispatcherStream.buildTopology();
        dispatcherStream.runTopology(topology);
    }

    private Topology buildTopology() {
        var builder = new StreamsBuilder();

        KStream<String, TrafficParticipant> participantKStream = builder
                .stream(TRAFFIC_PARTICIPANT_TOPIC, Consumed.with(Serdes.String(), AppSerdes.trafficParticipant()));

        participantKStream
                .filter((k, v) -> v.getSpeed() >= 80)
                .to(TRAFFIC_VIOLATOR_TOPIC, Produced.with(Serdes.String(), AppSerdes.trafficParticipant()));

        participantKStream
                .selectKey((k, v) -> v.getCameraId())
                .groupByKey(Grouped.with(Serdes.Integer(), AppSerdes.trafficParticipant()))
                .windowedBy(TimeWindows.ofSizeAndGrace(Duration.ofMinutes(1), Duration.ofSeconds(10)))
                .aggregate(ArrayList::new, this::addSpeedToList, AppMaterialized.listInteger(AVG_SPEED_STORE))
                .suppress(Suppressed.untilWindowCloses(Suppressed.BufferConfig.unbounded()))
                .mapValues(this::countIntegerAndFormat)
                .toStream()
                .map((k, v) -> new KeyValue<>(String.valueOf(k.key()), v))
                .to(TRAFFIC_AVERAGE_SPEED_TOPIC, Produced.with(Serdes.String(), Serdes.String()));

        return builder.build();
    }

    private void runTopology(Topology topology) {
        Properties properties = new Properties();
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "traffic-violator-qualifier");
        try (var stream = new KafkaStreams(topology, properties)) {
            stream.start();
            while (true) {
                try {
                    Thread.sleep(ONE_MINUTE);
                } catch (Exception ignored) {}
            }
        }
    }

    private List<Integer> addSpeedToList(Integer key, TrafficParticipant participant, List<Integer> acc) {
        acc.add(participant.getSpeed());
        return acc;
    }

    private String countIntegerAndFormat(List<Integer> carSpeed) {
        int countOfCars = carSpeed.size();
        double avgSpeed = carSpeed.stream().mapToInt(i -> i).average().orElse(0.0);
        String formattedAvgSpeed = new DecimalFormat("#0.00").format(avgSpeed);
        return String.format(AVG_SPEED_MSG_FORMAT, formattedAvgSpeed, countOfCars);
    }
}
