package com.hcmute.thuexe.repository;

import com.hcmute.thuexe.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT COUNT(b) FROM Booking b WHERE b.car.carId = :carId AND b.status = 'completed'")
    Long countCompletedTripsByCarId(Long carId);
}
