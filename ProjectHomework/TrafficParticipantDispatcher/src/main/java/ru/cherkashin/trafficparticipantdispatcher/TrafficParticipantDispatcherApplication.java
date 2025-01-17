package ru.cherkashin.trafficparticipantdispatcher;


import ru.cherkashin.trafficparticipantdispatcher.streams.TrafficDispatcherStream;

public class TrafficParticipantDispatcherApplication {

	public static void main(String[] args) {
		TrafficDispatcherStream.runStream();
	}
}