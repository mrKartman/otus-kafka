package ru.cherkashin.trafficgenerator.dto;

import lombok.Data;

@Data
public class TrafficParticipant {
    private final int cameraId;
    private final String carNumber;
    private final int speed;
    private final String photoId;
}
