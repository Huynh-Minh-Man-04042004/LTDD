package com.hcmute.thuexe.repository;

import com.hcmute.thuexe.model.Review;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.car.carId = :carId")
    Double findAverageRatingByCarId(Long carId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.car.owner.userId = :ownerId")
    Double findAverageRatingByOwnerId(@Param("ownerId") Long ownerId);

    List<Review> findByCar_CarId(Long carId);
}
