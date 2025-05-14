package com.hcmute.ltdd.model.request;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class EditCarRequest {

    private String name;
    private String brand;
    private String description;
    private String gearType;
    private int seats;
    private String fuelType;
    private double energyConsumption;
    private String imageUrl;
    private boolean driverRequired;
    private String location;
    private double price;

    @SerializedName("hasEtc")
    private List<String> features;

    public EditCarRequest(List<String> features, double price, String location,
                          boolean driverRequired, String imageUrl, double energyConsumption,
                          String fuelType, int seats, String gearType, String description,
                          String brand, String name) {
        this.features = features;
        this.price = price;
        this.location = location;
        this.driverRequired = driverRequired;
        this.imageUrl = imageUrl;
        this.energyConsumption = energyConsumption;
        this.fuelType = fuelType;
        this.seats = seats;
        this.gearType = gearType;
        this.description = description;
        this.brand = brand;
        this.name = name;
    }

    public List<String> getFeatures() {
        return features;
    }

    public void setFeatures(List<String> features) {
        this.features = features;
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

    public boolean isDriverRequired() {
        return driverRequired;
    }

    public void setDriverRequired(boolean driverRequired) {
        this.driverRequired = driverRequired;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getEnergyConsumption() {
        return energyConsumption;
    }

    public void setEnergyConsumption(double energyConsumption) {
        this.energyConsumption = energyConsumption;
    }

    public String getFuelType() {
        return fuelType;
    }

    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }
}
