package ru.cherkashin.trafficgenerator.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import ru.cherkashin.trafficgenerator.dto.TrafficParticipant;
import ru.cherkashin.trafficgenerator.serde.JsonSerializer;
import ru.cherkashin.trafficgenerator.service.TrafficGenerator;

import java.util.Map;
import java.util.UUID;

public class TrafficProducer {
    private static final String BOOTSTRAP_SERVERS = "localhost:9092,localhost:9093,localhost:9094";
    private static final String TOPIC_TRAFFIC_PARTICIPANT = "traffic-participant";
    private static final Long ONE_SECOND = 1000L;

    private static final Map<String, Object> PRODUCER_CONFIG = Map.of(
            ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS,
            ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
            ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class,
            ProducerConfig.TRANSACTIONAL_ID_CONFIG, "generatorTransactions",
            ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true,
            ProducerConfig.ACKS_CONFIG, "all");


    public static void startGenerateMessage() throws InterruptedException {
        try (var producer = new KafkaProducer<String, Object>(PRODUCER_CONFIG)) {
            producer.initTransactions();
            while (true) {
                TrafficParticipant participant = TrafficGenerator.generateParticipantJson();
                String key = UUID.randomUUID().toString();
                producer.beginTransaction();
                producer.send(new ProducerRecord<>(TOPIC_TRAFFIC_PARTICIPANT, key, participant));
                producer.commitTransaction();
                Thread.sleep(ONE_SECOND);
            }
        }
    }
}
