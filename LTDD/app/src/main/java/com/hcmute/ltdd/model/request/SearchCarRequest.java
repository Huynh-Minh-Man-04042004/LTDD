package com.hcmute.ltdd.model.request;

public class SearchCarRequest {
    private String location;
    private Integer seats;
    private String brand;
    private Double priceFrom;
    private Double priceTo;
    private String gearType;
    private String fuelType;
    private Boolean driverRequired;

    public Boolean getDriverRequired() {
        return driverRequired;
    }

    public void setDriverRequired(Boolean driverRequired) {
        this.driverRequired = driverRequired;
    }

    public SearchCarRequest(String location, Integer seats, String brand, Double priceFrom, Double priceTo, String gearType, String fuelType, Boolean driverRequired) {
        this.location = location;
        this.seats = seats;
        this.brand = brand;
        this.priceFrom = priceFrom;
        this.priceTo = priceTo;
        this.gearType = gearType;
        this.fuelType = fuelType;
        this.driverRequired = driverRequired;
    }

    public SearchCarRequest() {

    }

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
