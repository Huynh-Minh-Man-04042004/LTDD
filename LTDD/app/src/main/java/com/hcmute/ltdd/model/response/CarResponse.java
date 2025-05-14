package com.hcmute.ltdd.model.response;

import java.util.List;

public class CarResponse {
    private Long carId;
    private String name;
    private String brand;
    private String description;
    private String gearType;
    private int seats;
    private String fuelType;
    private double energyConsumption;
    private String imageUrl;
    private boolean driverRequired;
    private int numberOfRentals;
    private List<String> features;

    public CarResponse() {}

    public CarResponse(Long carId, String name, String brand, String description, String gearType, int seats, String fuelType, double energyConsumption, String imageUrl, boolean driverRequired, int numberOfRentals, List<String> features) {
        this.carId = carId;
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.gearType = gearType;
        this.seats = seats;
        this.fuelType = fuelType;
        this.energyConsumption = energyConsumption;
        this.imageUrl = imageUrl;
        this.driverRequired = driverRequired;
        this.numberOfRentals = numberOfRentals;
        this.features = features;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public double getEnergyConsumption() {
        return energyConsumption;
    }

    public void setEnergyConsumption(double energyConsumption) {
        this.energyConsumption = energyConsumption;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isDriverRequired() {
        return driverRequired;
    }

    public void setDriverRequired(boolean driverRequired) {
        this.driverRequired = driverRequired;
    }

    public int getNumberOfRentals() {
        return numberOfRentals;
    }

    public void setNumberOfRentals(int numberOfRentals) {
        this.numberOfRentals = numberOfRentals;
    }

    public List<String> getFeatures() {
        return features;
    }

    public void setFeatures(List<String> features) {
        this.features = features;
    }
}
