package ru.cherkashin.trafficgenerator.dto;

import lombok.Data;

@Data
public class TrafficParticipant {
    private int cameraId;
    private String carNumber;
    private int speed;
    private String photoId;

    public TrafficParticipant(int cameraId, String carNumber, int speed, String photoId) {
        this.cameraId = cameraId;
        this.carNumber = carNumber;
        this.speed = speed;
        this.photoId = photoId;
    }

    public TrafficParticipant() {
    }
}
