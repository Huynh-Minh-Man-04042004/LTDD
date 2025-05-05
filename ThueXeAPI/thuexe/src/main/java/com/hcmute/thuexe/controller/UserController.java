package com.hcmute.thuexe.controller;

import java.nio.file.attribute.UserPrincipal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hcmute.thuexe.dto.request.BookingRequest;
import com.hcmute.thuexe.dto.request.CancelBookingRequest;
import com.hcmute.thuexe.dto.request.SearchCarRequest;
import com.hcmute.thuexe.dto.response.BookingPreviewResponse;
import com.hcmute.thuexe.dto.response.CarDetailResponse;
import com.hcmute.thuexe.dto.response.CarListResponse;
import com.hcmute.thuexe.model.Car;
import com.hcmute.thuexe.model.User;
import com.hcmute.thuexe.payload.ApiResponse;
import com.hcmute.thuexe.service.BookingService;
import com.hcmute.thuexe.service.CarService;
import com.hcmute.thuexe.service.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private CarService carService;
    @Autowired
    private BookingService bookingService ;

    @GetMapping("/profile")
    public String getProfile(Authentication authentication) {
        String username = authentication.getName(); // Lấy username từ token đã giải mã
        return userService.getProfileByUsername(username);
    }
    
    @PostMapping("/cars/list")
    public ApiResponse<List<CarListResponse>> searchCars(@RequestBody SearchCarRequest request, Authentication authentication) {
        List<Car> cars = carService.searchCars(request);
        if (cars.isEmpty()) {
            return new ApiResponse<>(false, "Không tìm thấy xe nào");
        }

        List<CarListResponse> response = cars.stream()
            .map(car -> {
                Double avgRating = carService.getAvgRating(car.getCarId());
                Long tripCount = carService.getTripCount(car.getCarId());
                return new CarListResponse(car, avgRating != null ? avgRating : 0.0, tripCount != null ? tripCount : 0L);
            })
            .toList();

        return new ApiResponse<>(true, "Tìm kiếm thành công", response);
    }
    
    @GetMapping("/car/{id}")
    public ResponseEntity<CarDetailResponse> getCarDetail(@PathVariable Long id) {
        return ResponseEntity.ok(carService.getCarDetail(id));
    }

    @PostMapping("/booking/preview")
    public ResponseEntity<BookingPreviewResponse> preview(@RequestBody BookingRequest request) {
        return ResponseEntity.ok(bookingService.previewBooking(request));
    }

    @PostMapping("/booking/cancel")
    public ResponseEntity<String> cancelBooking(
            @RequestBody CancelBookingRequest request,
            @AuthenticationPrincipal User user) {
        bookingService.cancelBooking(request, user.getUserId());
        return ResponseEntity.ok("Huỷ đơn thành công");
    }

    @PostMapping("/booking/confirm")
    public ResponseEntity<String> confirmBooking(
        @RequestBody BookingRequest request,
        @AuthenticationPrincipal User user
    ) {
        Long bookingId = bookingService.confirmBooking(request, user.getUserId());
        return ResponseEntity.ok("Đặt xe thành công. ID đơn: " + bookingId);
    }

}

