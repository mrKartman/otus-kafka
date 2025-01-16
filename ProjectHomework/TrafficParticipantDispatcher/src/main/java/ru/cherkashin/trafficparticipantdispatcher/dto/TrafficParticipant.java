package ru.cherkashin.trafficparticipantdispatcher.dto;

import lombok.*;

@Data
public class TrafficParticipant {
    private int cameraId;
    private String carNumber;
    private int speed;
    private String photoId;
}
