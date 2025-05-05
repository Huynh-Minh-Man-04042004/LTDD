package com.hcmute.thuexe.model;

import java.util.List;

import jakarta.persistence.*;

@Entity
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long carId;
    
    @ManyToOne
    @JoinColumn(name = "ownerId", referencedColumnName = "userId")
    private User owner;

    private String name;
    private String brand; 
    private String description;
    private String gearType;
    private int seats;
    private String fuelType;
    private double energyConsumption;
    
    @ElementCollection
    private List<String> hasEtc;
    
    private String location;
    private double price;
    private String imageUrl;
    private String status;
    private boolean driverRequired;
    
    @Column(name = "created_at")
    private String createdAt;

    public Long getCarId() {
        return carId;
    }

    public void setCarId(Long carId) {
        this.carId = carId;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
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
    @Override
    public String toString() {
        return "Car{" +
                "carId=" + carId +
                ", owner=" + (owner != null ? owner.getUserId() : "null") +
                ", name='" + name + '\'' +
                ", brand='" + brand + '\'' +
                ", description='" + description + '\'' +
                ", gearType='" + gearType + '\'' +
                ", seats=" + seats +
                ", fuelType='" + fuelType + '\'' +
                ", energyConsumption=" + energyConsumption +
                ", hasEtc=" + hasEtc +
                ", location='" + location + '\'' +
                ", price=" + price +
                ", imageUrl='" + imageUrl + '\'' +
                ", status='" + status + '\'' +
                ", driverRequired=" + driverRequired +
                ", createdAt='" + createdAt + '\'' +
                '}';
    }
    
}
