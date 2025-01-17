package ru.cherkashin.finehandler.dto;

import lombok.Data;

@Data
public class Fine {
    private int userId;
    private int fine;
    private String msg;
    private String photoId;

    public Fine(int userId, int fine, String msg, String photoId) {
        this.userId = userId;
        this.fine = fine;
        this.msg = msg;
        this.photoId = photoId;
    }
}
