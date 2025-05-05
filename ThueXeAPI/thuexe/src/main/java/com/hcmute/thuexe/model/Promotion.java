package com.hcmute.thuexe.model;


import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long promotionId;

    private String code;
    private double discountAmount;
    private LocalDate startDate;
    private LocalDate endDate;
    private String status;

    public boolean isValid(LocalDate checkDate) {
        return (startDate == null || !checkDate.isBefore(startDate)) &&
               (endDate == null || !checkDate.isAfter(endDate)) &&
               "active".equalsIgnoreCase(status);
    }

    
}