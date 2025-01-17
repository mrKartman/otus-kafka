package ru.cherkashin.finehandler.service;

import ru.cherkashin.finehandler.dto.Fine;
import ru.cherkashin.finehandler.dto.TrafficParticipant;

public class FineCreator {

    private static final String MSG_FORMAT = "Превышен скоростной лимит. Скорость автобиля с номерами %s составила %s км/ч.";

    public static Fine createFine(TrafficParticipant participant) {
        String photoId = participant.getPhotoId();
        String msg = String.format(MSG_FORMAT, participant.getCarNumber(), participant.getSpeed());
        return new Fine(123, 5000, msg, photoId);
    }
}
