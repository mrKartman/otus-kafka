package ru.cherkashin.trafficgenerator;

import ru.cherkashin.trafficgenerator.producer.TrafficProducer;

public class TrafficGeneratorApplication {

	public static void main(String[] args) throws InterruptedException {
		TrafficProducer.startGenerateMessage();
	}
}
