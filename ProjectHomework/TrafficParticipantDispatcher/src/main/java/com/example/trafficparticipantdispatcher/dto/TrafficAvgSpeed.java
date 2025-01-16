package com.example.trafficparticipantdispatcher.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrafficAvgSpeed {
    private int sumOfSpeed;
    private int countOfCars;
    private int avgSpeed;



}
