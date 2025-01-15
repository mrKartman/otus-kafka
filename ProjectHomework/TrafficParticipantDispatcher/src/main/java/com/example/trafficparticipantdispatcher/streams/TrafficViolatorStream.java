package com.example.trafficparticipantdispatcher.streams;

import com.example.trafficparticipantdispatcher.dto.TrafficParticipant;
import com.google.gson.Gson;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;

import java.util.Properties;

public class TrafficViolatorStream {
    private static final Long SLEEP_BETWEEN_MSG_MS = 1000L;

    public static void startStream() {
        Serde<String> stringSerde = Serdes.String();
        var builder = new StreamsBuilder();
        Gson gson = new Gson();

        KStream<String, String> participantKStream = builder
                .stream("traffic-participant", Consumed.with(stringSerde, stringSerde))
                .mapValues(p -> gson.fromJson(p, TrafficParticipant.class))
                .filter((k, v) -> v.getSpeed() >= 80)
                .mapValues(p -> gson.toJson(p));

        participantKStream.to("traffic-violator", Produced.with(stringSerde, stringSerde));
        Topology topology = builder.build();

        try (var stream = new KafkaStreams(topology,  getKafkaProps())) {
            stream.start();
            while (true) {
                try {
                    Thread.sleep(SLEEP_BETWEEN_MSG_MS);
                } catch (Exception ignored) {}
                System.out.println("-----------------------------------------------------");
            }
        }
    }

    private static Properties getKafkaProps() {
        Properties properties = new Properties();
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "traffic-violator-qualifier");
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        return properties;
    }
}
