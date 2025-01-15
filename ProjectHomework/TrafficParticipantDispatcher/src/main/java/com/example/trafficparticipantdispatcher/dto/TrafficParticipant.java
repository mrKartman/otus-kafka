package com.example.trafficparticipantdispatcher.dto;

import lombok.Data;

@Data
public class TrafficParticipant {
    private int cameraId;
    private String carNumber;
    private int speed;
    private String photoId;
}
