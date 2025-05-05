package com.hcmute.thuexe.service;

import com.hcmute.thuexe.dto.request.SearchCarRequest;
import com.hcmute.thuexe.dto.response.CarDetailResponse;
import com.hcmute.thuexe.dto.response.ReviewDTO;
import com.hcmute.thuexe.model.Car;
import com.hcmute.thuexe.repository.BookingRepository;
import com.hcmute.thuexe.repository.CarRepository;
import com.hcmute.thuexe.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;

import java.util.List;

@Service
public class CarService {
    @Autowired
    private CarRepository carRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    public List<Car> searchCars(SearchCarRequest searchCarRequest) {
        return carRepository.searchCars(
                searchCarRequest.getLocation(),
                searchCarRequest.getSeats(),
                searchCarRequest.getBrand(),
                searchCarRequest.getPriceFrom(),
                searchCarRequest.getPriceTo(),
                searchCarRequest.getGearType(),
                searchCarRequest.getFuelType()
        );
    }

    public Double getAvgRating(Long carId) {
        return reviewRepository.findAverageRatingByCarId(carId);
    }

    public Long getTripCount(Long carId) {
        return bookingRepository.countCompletedTripsByCarId(carId);
    }

    public CarDetailResponse getCarDetail(Long carId) {
        Car car = carRepository.findById(carId)
            .orElseThrow(() -> new RuntimeException("Car not found"));

        // Đánh giá & chuyến của xe
        Double avgRating = reviewRepository.findAverageRatingByCarId(carId);
        Long tripCount = bookingRepository.countCompletedTripsByCarId(carId);

        // Thống kê của chủ xe
        Long ownerId = car.getOwner().getUserId();
        Double ownerAvgRating = reviewRepository.findAverageRatingByOwnerId(ownerId);
        Long ownerTripCount = bookingRepository.countCompletedTripsByOwnerId(ownerId);

        // Danh sách đánh giá
        List<ReviewDTO> reviews = reviewRepository.findByCar_CarId(carId)
            .stream()
            .map(r -> new ReviewDTO(
                r.getRating(),
                r.getComment(),
                r.getUser().getUserId(),
                r.getCreatedAt()
            ))
            .collect(Collectors.toList());
    

        // Trả về DTO
        CarDetailResponse response = new CarDetailResponse();
        response.setId(car.getCarId());
        response.setName(car.getName());
        response.setBrand(car.getBrand());
        response.setDescription(car.getDescription());
        response.setGearType(car.getGearType());
        response.setSeats(car.getSeats());
        response.setFuelType(car.getFuelType());
        response.setEnergyConsumption(car.getEnergyConsumption());
        response.setHasEtc(car.getHasEtc());
        response.setLocation(car.getLocation());
        response.setPrice(car.getPrice());
        response.setImageUrl(car.getImageUrl());
        response.setStatus(car.getStatus());
        response.setDriverRequired(car.isDriverRequired());
        response.setCreatedAt(car.getCreatedAt());
        response.setAvgRating(avgRating != null ? avgRating : 0.0);
        response.setTripCount(tripCount != null ? tripCount : 0L);
        response.setOwnerName(car.getOwner().getName());
        response.setOwnerAvgRating(ownerAvgRating != null ? ownerAvgRating : 0.0);
        response.setOwnerTripCount(ownerTripCount != null ? ownerTripCount : 0L);
        response.setReviews(reviews);

        return response;
    }
}
