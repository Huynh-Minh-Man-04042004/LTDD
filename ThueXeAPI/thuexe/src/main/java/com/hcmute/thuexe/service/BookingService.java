package com.hcmute.thuexe.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.stereotype.Service;

import com.hcmute.thuexe.dto.request.BookingRequest;
import com.hcmute.thuexe.dto.request.CancelBookingRequest;
import com.hcmute.thuexe.dto.response.BookingPreviewResponse;
import com.hcmute.thuexe.model.Booking;
import com.hcmute.thuexe.model.Car;
import com.hcmute.thuexe.model.User;
import com.hcmute.thuexe.model.Voucher;
import com.hcmute.thuexe.repository.BookingRepository;
import com.hcmute.thuexe.repository.CarRepository;
import com.hcmute.thuexe.repository.UserRepository;
import com.hcmute.thuexe.repository.VoucherRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class BookingService {

    private final CarRepository carRepository;
    private final VoucherRepository voucherRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;

    public BookingPreviewResponse previewBooking(BookingRequest request) {
        Car car = carRepository.findById(request.getCarId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy xe"));

        long days = ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate());
        if (days <= 0) {
            throw new RuntimeException("Phạm vi ngày không hợp lệ");
        }

        double pricePerDay = car.getPrice();
        double insuranceFee = request.isInsuranceSelected() ? 200_000 : 0;
        double deliveryFee = request.isDeliverySelected() ? 30_000 : 0;

        double totalBeforeDiscount = (pricePerDay + insuranceFee) * days + deliveryFee;

        AtomicReference<Double> discountRef = new AtomicReference<>(0.0);

        if (request.getVoucherCode() != null) {
            voucherRepository.findByCode(request.getVoucherCode()).ifPresent(v -> {
                if (v.isValid(request.getStartDate(), totalBeforeDiscount)) {
                    if (v.getDiscountAmount() != null) {
                        discountRef.set(v.getDiscountAmount());
                    } else if (v.getDiscountPercentage() != null) {
                        double rawDiscount = totalBeforeDiscount * v.getDiscountPercentage() / 100;
                        discountRef.set(
                            (v.getMaxDiscount() != null)
                                ? Math.min(rawDiscount, v.getMaxDiscount())
                                : rawDiscount
                        );
                    }
                }
            });
        }

        double discount = discountRef.get();
        double total = totalBeforeDiscount - discount;
        double deposit = total * 0.4;
        double payLater = total - deposit;

        return new BookingPreviewResponse(
            pricePerDay, insuranceFee, deliveryFee, (int) days,
            totalBeforeDiscount, discount, total,
            deposit, payLater,
            "Bạn cần thanh toán giữ chỗ và cung cấp giấy tờ hợp lệ khi nhận xe"
        );
    }

    @Transactional
    public Long confirmBooking(BookingRequest request, Long userId) {
        Car car = carRepository.findById(request.getCarId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy xe"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));

        long days = ChronoUnit.DAYS.between(request.getStartDate(), request.getEndDate());
        if (days <= 0) throw new RuntimeException("Ngày thuê không hợp lệ");

        double pricePerDay = car.getPrice();
        double insuranceFee = request.isInsuranceSelected() ? 200_000 : 0;
        double deliveryFee = request.isDeliverySelected() ? 30_000 : 0;
        double totalBeforeDiscount = (pricePerDay + insuranceFee) * days + deliveryFee;

        Voucher voucher = null;
        double discount = 0;

        if (request.getVoucherCode() != null) {
            voucher = voucherRepository.findByCode(request.getVoucherCode()).orElse(null);
            if (voucher != null && voucher.isValid(request.getStartDate(), totalBeforeDiscount)) {
                if (voucher.getDiscountAmount() != null) {
                    discount = voucher.getDiscountAmount();
                } else if (voucher.getDiscountPercentage() != null) {
                    double raw = totalBeforeDiscount * voucher.getDiscountPercentage() / 100;
                    discount = (voucher.getMaxDiscount() != null)
                            ? Math.min(raw, voucher.getMaxDiscount())
                            : raw;
                }
            }
        }

        double total = totalBeforeDiscount - discount;

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setCar(car);
        booking.setStartDate(request.getStartDate());
        booking.setEndDate(request.getEndDate());
        booking.setPickupLocation(request.getPickupLocation());
        booking.setDropoffLocation(request.getDropoffLocation());
        booking.setInsuranceSelected(request.isInsuranceSelected());
        booking.setDeliverySelected(request.isDeliverySelected());
        booking.setVoucher(voucher);
        booking.setTotalPrice(total);
        booking.setStatus("pending");
        booking.setCreatedAt(LocalDate.now());

        bookingRepository.save(booking);
        return booking.getBookingId();
    }


    @Transactional
    public void cancelBooking(CancelBookingRequest request, Long userId) {
        Booking booking = bookingRepository.findById(request.getBookingId())
            .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn"));

        // Kiểm tra quyền hủy (chỉ người đặt mới được hủy)
        if (!booking.getUser().getUserId().equals(userId)) {
            throw new RuntimeException("Bạn không có quyền hủy đơn này");
        }

        // Kiểm tra thời gian còn lại
        LocalDate now = LocalDate.now();
        long hoursUntilStart = ChronoUnit.HOURS.between(now, booking.getStartDate());

        if (hoursUntilStart < 24) {
            booking.setStatus("cancelled_with_fee");
            // có thể tính phí tại đây nếu cần
        } else {
            booking.setStatus("cancelled");
        }

        booking.setUpdatedAt(LocalDate.now());
        booking.setCancelReason(request.getReason());
        bookingRepository.save(booking);
    }

}
