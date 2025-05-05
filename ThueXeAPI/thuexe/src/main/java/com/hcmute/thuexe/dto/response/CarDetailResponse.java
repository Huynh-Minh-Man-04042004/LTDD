package com.hcmute.thuexe.dto.response;

import java.util.List;

public class CarDetailResponse {
    private Long id;
    private String name;
    private String brand;
    private String description;
    private String gearType;
    private int seats;
    private String fuelType;
    private double energyConsumption;
    private List<String> hasEtc;
    private String location;
    private double price;
    private String imageUrl;
    private String status;
    private boolean driverRequired;
    private String createdAt;

    // Car stats
    private double avgRating;
    private long tripCount;

    // Owner stats
    private String ownerName;
    private double ownerAvgRating;
    private long ownerTripCount;

    // Reviews
    private List<ReviewDTO> reviews;

    public CarDetailResponse() {
    }

    public CarDetailResponse(Long id, String name, String brand, String description, String gearType, int seats,
            String fuelType, double energyConsumption, List<String> hasEtc, String location, double price,
            String imageUrl, String status, boolean driverRequired, String createdAt, double avgRating, long tripCount,
            String ownerName, double ownerAvgRating, long ownerTripCount, List<ReviewDTO> reviews) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.gearType = gearType;
        this.seats = seats;
        this.fuelType = fuelType;
        this.energyConsumption = energyConsumption;
        this.hasEtc = hasEtc;
        this.location = location;
        this.price = price;
        this.imageUrl = imageUrl;
        this.status = status;
        this.driverRequired = driverRequired;
        this.createdAt = createdAt;
        this.avgRating = avgRating;
        this.tripCount = tripCount;
        this.ownerName = ownerName;
        this.ownerAvgRating = ownerAvgRating;
        this.ownerTripCount = ownerTripCount;
        this.reviews = reviews;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public List<String> getHasEtc() {
        return hasEtc;
    }

    public void setHasEtc(List<String> hasEtc) {
        this.hasEtc = hasEtc;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isDriverRequired() {
        return driverRequired;
    }

    public void setDriverRequired(boolean driverRequired) {
        this.driverRequired = driverRequired;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(double avgRating) {
        this.avgRating = avgRating;
    }

    public long getTripCount() {
        return tripCount;
    }

    public void setTripCount(long tripCount) {
        this.tripCount = tripCount;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public double getOwnerAvgRating() {
        return ownerAvgRating;
    }

    public void setOwnerAvgRating(double ownerAvgRating) {
        this.ownerAvgRating = ownerAvgRating;
    }

    public long getOwnerTripCount() {
        return ownerTripCount;
    }

    public void setOwnerTripCount(long ownerTripCount) {
        this.ownerTripCount = ownerTripCount;
    }

    public List<ReviewDTO> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewDTO> reviews) {
        this.reviews = reviews;
    }

}
