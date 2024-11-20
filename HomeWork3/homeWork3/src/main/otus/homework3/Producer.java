package main.otus.homework3;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class Producer {

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9091");
        props.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "geniusTransaction");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        try (var producer = new KafkaProducer<String, String>(props)) {
            producer.initTransactions();

            producer.beginTransaction();
            for (int i = 1; i <= 5; i++) {
                producer.send(new ProducerRecord<>("topic1", "Topic1 commit Transaction message - " + i));
                producer.send(new ProducerRecord<>("topic2", "Topic2 commit Transaction message - " + i));
            }
            producer.commitTransaction();

            producer.beginTransaction();
            for (int i = 1; i <= 2; i++) {
                producer.send(new ProducerRecord<>("topic1", "Topic1 abort Transaction message - " + i));
                producer.send(new ProducerRecord<>("topic2", "Topic2 abort Transaction message - " + i));
            }
            producer.abortTransaction();
        }

    }
}
