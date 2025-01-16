package com.example.trafficparticipantdispatcher.streams;

import com.example.trafficparticipantdispatcher.dto.TrafficAvgSpeed;
import com.example.trafficparticipantdispatcher.dto.TrafficParticipant;
import com.example.trafficparticipantdispatcher.serde.MyDeserializer;
import com.example.trafficparticipantdispatcher.serde.MySerializer;
import com.google.gson.Gson;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.WindowStore;

import java.text.DecimalFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class TrafficViolatorStream {
    public static void startStream() {
        Serde<String> stringSerde = Serdes.String();
        var builder = new StreamsBuilder();
        Gson gson = new Gson();
        Serde<TrafficParticipant> mySerde = new Serdes.WrapperSerde<>(new MySerializer<>(), new MyDeserializer<>(TrafficParticipant.class));
        Serde<TrafficAvgSpeed> avgSpeedSerde = new Serdes.WrapperSerde<>(new MySerializer<>(), new MyDeserializer<>(TrafficAvgSpeed.class));

        Materialized<Integer, List<Integer>, WindowStore<Bytes, byte[]>> avgSpeedMaterialized = Materialized.<Integer, List<Integer>, WindowStore<Bytes, byte[]>>as("avg-speed-store")
                .withKeySerde(Serdes.Integer()).withValueSerde(Serdes.ListSerde(ArrayList.class, Serdes.Integer()));

        KStream<String, TrafficParticipant> participantKStream = builder
                .stream("traffic-participant", Consumed.with(stringSerde, stringSerde))
                .mapValues(p -> gson.fromJson(p, TrafficParticipant.class));

        participantKStream
                .filter((k, v) -> v.getSpeed() >= 80)
                .mapValues(p -> gson.toJson(p))
                .to("traffic-violator", Produced.with(stringSerde, stringSerde));

        KTable<Windowed<Integer>, String> kTable = participantKStream
                .selectKey((k, v) -> v.getCameraId())
                .groupByKey(Grouped.with(Serdes.Integer(), mySerde))
                .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofMinutes(1)))
                .aggregate(ArrayList::new,
                        (key, value, acc) -> {
                            acc.add(value.getSpeed());
                            return acc;
                        },
                        avgSpeedMaterialized)
                .suppress(Suppressed.untilWindowCloses(Suppressed.BufferConfig.unbounded()))
                .mapValues(v -> {
                    double avgSpeed = v.stream().mapToInt(i -> i).average().orElse(0.0);
                    String formattedSpeed = new DecimalFormat("#0.00").format(avgSpeed);
                    return "Average speed on currentCamera is " + formattedSpeed + "km/h !" + "Were " + v.size() + "cars.";
                });

        kTable.toStream().map((k, v) -> new KeyValue<>(k.key(), v)).to("traffic-average-speed", Produced.with(Serdes.Integer(), stringSerde));

        Topology topology = builder.build();

        try (var stream = new KafkaStreams(topology, getKafkaProps())) {
            stream.start();
            while (true) {
                try {
                    Thread.sleep(60 * 1000);
                } catch (Exception ignored) {}
                System.out.println("-----------------------------------------------------");
            }
        }
    }

    private static Properties getKafkaProps() {
        Properties properties = new Properties();
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "traffic-violator-qualifier");
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.IntegerSerde.class);
        properties.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.StringSerde.class);
        return properties;
    }
}
