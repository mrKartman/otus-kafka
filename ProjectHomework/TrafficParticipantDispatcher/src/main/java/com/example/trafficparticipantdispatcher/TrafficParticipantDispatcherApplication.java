package com.example.trafficparticipantdispatcher;

import com.example.trafficparticipantdispatcher.dto.TrafficParticipant;
import com.example.trafficparticipantdispatcher.streams.TrafficViolatorStream;
import com.google.gson.Gson;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class TrafficParticipantDispatcherApplication {

	public static void main(String[] args) {
		new Thread(TrafficViolatorStream::startStream);
	}
}
