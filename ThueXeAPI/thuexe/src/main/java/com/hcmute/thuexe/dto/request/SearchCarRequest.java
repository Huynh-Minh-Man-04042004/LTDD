package com.hcmute.thuexe.dto.request;

public class SearchCarRequest {
    private String location;
    private Integer seats;
    private String brand;
    private Double priceFrom;
    private Double priceTo;
    private String gearType;
    private String fuelType;
    
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public Integer getSeats() {
        return seats;
    }
    public void setSeats(Integer seats) {
        this.seats = seats;
    }
    public String getBrand() {
        return brand;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }
    public Double getPriceFrom() {
        return priceFrom;
    }
    public void setPriceFrom(Double priceFrom) {
        this.priceFrom = priceFrom;
    }
    public Double getPriceTo() {
        return priceTo;
    }
    public void setPriceTo(Double priceTo) {
        this.priceTo = priceTo;
    }
    public String getGearType() {
        return gearType;
    }
    public void setGearType(String gearType) {
        this.gearType = gearType;
    }
    public String getFuelType() {
        return fuelType;
    }
    public void setFuelType(String fuelType) {
        this.fuelType = fuelType;
    }

    
}
