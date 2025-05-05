package com.hcmute.thuexe.repository;

import com.hcmute.thuexe.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {
    @Query("SELECT c FROM Car c WHERE " +
            "(:location IS NULL OR c.location LIKE %:location%) AND " +
            "(:seats IS NULL OR c.seats = :seats) AND " +
            "(:brand IS NULL OR c.brand LIKE %:brand%) AND " +
            "(:priceFrom IS NULL OR c.price >= :priceFrom) AND " +
            "(:priceTo IS NULL OR c.price <= :priceTo) AND " +
            "(:gearType IS NULL OR c.gearType = :gearType) AND " +
            "(:fuelType IS NULL OR c.fuelType = :fuelType)")
    List<Car> searchCars(@Param("location") String location,
                         @Param("seats") Integer seats,
                         @Param("brand") String brand,
                         @Param("priceFrom") Double priceFrom,
                         @Param("priceTo") Double priceTo,
                         @Param("gearType") String gearType,
                         @Param("fuelType") String fuelType);
}
