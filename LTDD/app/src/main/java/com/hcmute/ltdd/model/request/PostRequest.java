package com.hcmute.ltdd.model.request;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class PostRequest {
    private Long carId;
    private double pricePerDay;
    private String  pickupTime;
    private String  dropoffTime;
    private String description;

    public PostRequest(Long carId, double pricePerDay, String  pickupTime, String  dropoffTime, String description) {
        this.carId = carId;
        this.pricePerDay = pricePerDay;
        this.pickupTime = pickupTime;
        this.dropoffTime = dropoffTime;
        this.description = description;
    }

    public String  getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(String  pickupTime) {
        this.pickupTime = pickupTime;
    }

    public String  getDropoffTime() {
        return dropoffTime;
    }

    public void setDropoffTime(String  dropoffTime) {
        this.dropoffTime = dropoffTime;
    }

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

