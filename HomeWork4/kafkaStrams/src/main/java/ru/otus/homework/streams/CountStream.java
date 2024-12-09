package ru.otus.homework.streams;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.TimeWindows;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

@RequiredArgsConstructor
public class CountStream {
    public static void main(String[] args) throws Exception {
        Serde<String> stringSerde = Serdes.String();
        var builder = new StreamsBuilder();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");


        builder.stream("events", Consumed.with(stringSerde, stringSerde))
                .groupByKey()
                .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofSeconds(30)))
                .count()
                .toStream()
                .foreach((key, val) -> System.out.println("--- By key " + key.key() + " was get " + val + "values at" + LocalDateTime.now().format(dateTimeFormatter)));

        Topology topology = builder.build();

        try (var stream = new KafkaStreams(topology, getKafkaProps())) {
            stream.start();
            while (true) {
                Thread.sleep(Duration.ofSeconds(30));
                System.out.println("-----------------------------------------------------");
            }
        }
    }

    private static Properties getKafkaProps() {
        Properties properties = new Properties();
        properties.put(StreamsConfig.APPLICATION_ID_CONFIG, "myApp");
        properties.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9091");
        return properties;
    }
}
