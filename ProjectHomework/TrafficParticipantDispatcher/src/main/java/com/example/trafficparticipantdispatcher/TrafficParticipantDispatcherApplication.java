package com.example.trafficparticipantdispatcher;

import com.example.trafficparticipantdispatcher.streams.TrafficViolatorStream;

public class TrafficParticipantDispatcherApplication {

	public static void main(String[] args) {
		TrafficViolatorStream.startStream();
	}
}
