package com.hcmute.ltdd.model.response;

import java.io.Serializable;

public class BookingPreviewResponse implements Serializable {
    private double rentalPricePerDay;
    private double insuranceFeePerDay;
    private double deliveryFee;
    private double totalDays;

    private double totalBeforeDiscount;
    private double discountAmount;
    private double totalPrice;

    private double depositAmount;
    private double payLaterAmount;
    private double driverRequired;
    private String policyNote;

    public BookingPreviewResponse() {
    }

    public BookingPreviewResponse(double rentalPricePerDay, double insuranceFeePerDay, double deliveryFee, double totalDays,
                                  double totalBeforeDiscount, double discountAmount, double totalPrice, double depositAmount,
                                  double payLaterAmount, double driverRequired, String policyNote) {
        this.rentalPricePerDay = rentalPricePerDay;
        this.insuranceFeePerDay = insuranceFeePerDay;
        this.deliveryFee = deliveryFee;
        this.totalDays = totalDays;
        this.totalBeforeDiscount = totalBeforeDiscount;
        this.discountAmount = discountAmount;
        this.totalPrice = totalPrice;
        this.depositAmount = depositAmount;
        this.payLaterAmount = payLaterAmount;
        this.driverRequired = driverRequired;
        this.policyNote = policyNote;
    }

    public double getRentalPricePerDay() {
        return rentalPricePerDay;
    }

    public void setRentalPricePerDay(double rentalPricePerDay) {
        this.rentalPricePerDay = rentalPricePerDay;
    }

    public double getInsuranceFeePerDay() {
        return insuranceFeePerDay;
    }

    public void setInsuranceFeePerDay(double insuranceFeePerDay) {
        this.insuranceFeePerDay = insuranceFeePerDay;
    }

    public double getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(double deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public double getTotalDays() {
        return totalDays;
    }

    public void setTotalDays(double totalDays) {
        this.totalDays = totalDays;
    }

    public double getTotalBeforeDiscount() {
        return totalBeforeDiscount;
    }

    public void setTotalBeforeDiscount(double totalBeforeDiscount) {
        this.totalBeforeDiscount = totalBeforeDiscount;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public double getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(double depositAmount) {
        this.depositAmount = depositAmount;
    }

    public double getPayLaterAmount() {
        return payLaterAmount;
    }

    public void setPayLaterAmount(double payLaterAmount) {
        this.payLaterAmount = payLaterAmount;
    }

    public double getDriverRequired() {
        return driverRequired;
    }

    public void setDriverRequired(double driverRequired) {
        this.driverRequired = driverRequired;
    }

    public String getPolicyNote() {
        return policyNote;
    }

    public void setPolicyNote(String policyNote) {
        this.policyNote = policyNote;
    }
}
