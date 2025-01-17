package ru.cherkashin.trafficgenerator.service;

import ru.cherkashin.trafficgenerator.dto.TrafficParticipant;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class TrafficGenerator {
    private static final List<String> RANDOM_CAR_NUMBERS = List.of("с123ду", "в543ау", "м113уу", "п452ду", "с623му",
            "а521оп", "и215кт", "ф541мп", "л578мс", "д521фи", "е751дп", "щ712в", "ф456пр", "т451тп", "ч175пи", "ф456от",
            "п185оп", "с421тп", "т951ир", "н746не", "к413мт", "и451ра", "л412ср", "т483яв", "я492см", "р997ри", "и913ап",
            "т453ив", "г543ар", "о460им", "р403мс", "а496сн", "н520пр", "н789яс", "я524ит", "р607мп", "и419фа", "т089нп");

    public static TrafficParticipant generateParticipantJson() {
        Random random = new Random();
        int randomNumber = random.nextInt(RANDOM_CAR_NUMBERS.size());
        String carNumber = RANDOM_CAR_NUMBERS.get(randomNumber);
        int speed = random.nextInt(40, 90);
        int cameraId = random.nextInt(1, 4);
        String photoId = UUID.randomUUID().toString();
        return new TrafficParticipant(cameraId, carNumber, speed, photoId);
    }
}
