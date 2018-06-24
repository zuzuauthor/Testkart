package com.testkart.exam.testkart.notifications;

import java.io.Serializable;

/**
 * Created by elfemo on 27/7/17.
 */

public class DataNotification implements Serializable{

    private String notificationId;
    private String title;
    private String message;
    private String dateTime;
    private String imageUrl;
    private boolean includeImage = false;

    public DataNotification(String title, String message, String dateTime, String imageUrl, boolean includeImage){

        this.title = title;
        this.message = message;
        this.imageUrl = imageUrl;
        this.includeImage = includeImage;
        this.dateTime = dateTime;

    }

    public DataNotification(){}


    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public boolean isIncludeImage() {
        return includeImage;
    }

    public void setIncludeImage(boolean includeImage) {
        this.includeImage = includeImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
