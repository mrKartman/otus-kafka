package ru.cherkashin.finehandler.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import ru.cherkashin.finehandler.dto.Fine;
import ru.cherkashin.finehandler.dto.TrafficParticipant;
import ru.cherkashin.finehandler.serde.JsonSerializer;
import ru.cherkashin.finehandler.serde.TrafficParticipantDeserializer;
import ru.cherkashin.finehandler.service.FineCreator;

import java.time.Duration;
import java.util.List;
import java.util.Map;

public class FineHandler {
    private static final String BOOTSTRAP_SERVERS = "localhost:9092,localhost:9093,localhost:9094";
    public static final Map<String, Object> PRODUCER_CONFIG = Map.of(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS,
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class
    );

    public static final Map<String, Object> CONSUMER_CONFIG = Map.of(
            ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS,
            ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, TrafficParticipantDeserializer.class,
            ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
            ConsumerConfig.GROUP_ID_CONFIG, "myGroup",
            ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"
    );

    private static final String FINE_TOPIC = "fine";
    private static final String TRAFFIC_VIOLATOR_TOPIC = "traffic-violator";

    public static void startHandle() {

        try (var consumer = new KafkaConsumer<String, TrafficParticipant>(CONSUMER_CONFIG);
             var producer = new KafkaProducer<String, Fine>(PRODUCER_CONFIG)) {
            consumer.subscribe(List.of(TRAFFIC_VIOLATOR_TOPIC));

            while (true) {
                ConsumerRecords<String, TrafficParticipant> poll = consumer.poll(Duration.ofSeconds(5));

                for (ConsumerRecord<String, TrafficParticipant> record : poll) {
                    TrafficParticipant participant = record.value();
                    Fine fine = FineCreator.createFine(participant);
                    producer.send(new ProducerRecord<>(FINE_TOPIC, fine));
                }
            }
        }
    }
}
