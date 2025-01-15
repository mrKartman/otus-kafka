package ru.cherkashin.trafficgenerator.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import ru.cherkashin.trafficgenerator.service.TrafficGenerator;

import java.time.Duration;
import java.util.Properties;

public class TrafficProducer {
    private static final String BOOTSTRAP_SERVERS = "localhost:9092";
    private static final String TOPIC_TRAFFIC_PARTICIPANT = "traffic-participant";
    private static final Long SLEEP_BETWEEN_MSG_MS = 5000L;


    public static void startGenerateMessage() throws InterruptedException {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
//		props.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "transaction");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        try (var producer = new KafkaProducer<String, Object>(props)) {
            while (true) {
                String participantJson = TrafficGenerator.generateParticipantJson();
                producer.send(new ProducerRecord<>(TOPIC_TRAFFIC_PARTICIPANT, participantJson));
                Thread.sleep(SLEEP_BETWEEN_MSG_MS);
            }
        }
    }
}