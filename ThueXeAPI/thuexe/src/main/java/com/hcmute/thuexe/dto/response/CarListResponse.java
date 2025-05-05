package com.hcmute.thuexe.dto.response;

import com.hcmute.thuexe.model.Car;

public class CarListResponse {
    private Long carId;
    private String name;
    private String brand;
    private String gearType;
    private int seats;
    private String fuelType;
    private String location;
    private String imageUrl;
    private double price;
    private Long ownerId;
    private Double avgRating;
    private Long tripCount;

    public CarListResponse(Car car, Double avgRating, Long tripCount) {
        this.carId = car.getCarId();
        this.name = car.getName();
        this.brand = car.getBrand();
        this.gearType = car.getGearType();
        this.seats = car.getSeats();
        this.fuelType = car.getFuelType();
        this.location = car.getLocation();
        this.imageUrl = car.getImageUrl();
        this.price = car.getPrice();
        this.ownerId = car.getOwner() != null ? car.getOwner().getUserId() : null;
        this.avgRating = avgRating;
        this.tripCount = tripCount;
    }

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getGearType() {
        return gearType;
    }

    public void setGearType(String gearType) {
        this.gearType = gearType;
    }

    public int getSeats() {
        return seats;
    }

    public void setSeats(int seats) {
        this.seats = seats;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public Double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(Double avgRating) {
        this.avgRating = avgRating;
    }

    public Long getTripCount() {
        return tripCount;
    }

    public void setTripCount(Long tripCount) {
        this.tripCount = tripCount;
    }
    
}
