package com.hcmute.ltdd.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.hcmute.ltdd.R;
import com.hcmute.ltdd.model.response.BookingDetailResponse;
import com.hcmute.ltdd.model.response.ReviewDTO;
import com.hcmute.ltdd.ui.fragments.ReviewFragment;
import com.hcmute.ltdd.viewmodel.BookingViewModel;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class BookingDetailActivity extends AppCompatActivity {

    private BookingViewModel bookingViewModel;

    // UI Components
    private TextView tvCarName, tvInfoName, txtPickUpTime, txtReturnTime,
            tvPickupLocation, tvDropoffLocation, tvTotalPrice, tvStatus, txtPhoneNumber,
            txtRentalPrice, txtInsuranceFee, txtDeliveryFee, txtTotalDays, txtDriverFee;
    private ImageView imgCar, imgInfo, btnClose;
    private Button btnAction, btnConfirm;
    private ProgressBar progressBar;
    private LinearLayout reviewContainer;

    private long bookingId;
    private String role;

    private final Map<String, String> statusMapping = new HashMap<String, String>() {{
        put("Pending", "Đang chờ");
        put("Confirmed", "Đã xác nhận");
        put("Cancelled_by_user", "Đã hủy bởi người thuê");
        put("Cancelled_by_owner", "Đã hủy bởi chủ xe");
        put("InProgress", "Đang diễn ra");
        put("Completed", "Đã hoàn thành");
        put("Reviewed", "Đã đánh giá");
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_detail);

        initViews();
        setupViewModel();

        bookingId = getIntent().getLongExtra("bookingId", -1);
        role = getIntent().getStringExtra("tab");

        if (bookingId != -1) {
            loadBookingDetail();
        }
    }

    private void initViews() {
        progressBar = findViewById(R.id.progressBar_bookingDetail);
        tvCarName = findViewById(R.id.tvCarName_bookingdetail);
        tvStatus = findViewById(R.id.tvStatus);
        imgCar = findViewById(R.id.imgCar_bookingdetail);
        imgInfo = findViewById(R.id.imgInfo);
        tvInfoName = findViewById(R.id.txtInfoName);
        txtPhoneNumber = findViewById(R.id.txtPhoneNumber);
        txtPickUpTime = findViewById(R.id.txtPickUpTime2);
        txtReturnTime = findViewById(R.id.txtReturnTime2);
        tvPickupLocation = findViewById(R.id.txtPickupLocation2);
        tvDropoffLocation = findViewById(R.id.txtDropoffLocation2);
        txtRentalPrice = findViewById(R.id.txtRentalPrice);
        txtInsuranceFee = findViewById(R.id.txtInsuranceFee);
        txtDeliveryFee = findViewById(R.id.txtDeliveryFee);
        txtTotalDays = findViewById(R.id.txtTotalDays);
        txtDriverFee = findViewById(R.id.txtDriverFee);
        tvTotalPrice = findViewById(R.id.txtTotalPrice);
        btnAction = findViewById(R.id.btnAction);
        btnConfirm = findViewById(R.id.btnConfirm);
        btnClose = findViewById(R.id.btnClose_bookingdetail);
        reviewContainer = findViewById(R.id.reviewContainer);

        btnClose.setOnClickListener(v -> finish());
    }

    private void setupViewModel() {
        bookingViewModel = new ViewModelProvider(this).get(BookingViewModel.class);

        bookingViewModel.getBookingDetailLiveData().observe(this, this::bindData);
        bookingViewModel.getIsLoading().observe(this, isLoading ->
                progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE));
        bookingViewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadBookingDetail() {
        bookingViewModel.getBookingDetail(this, bookingId);
    }

    private void bindData(BookingDetailResponse detail) {
        setRoleAndUserInfo(detail);
        setActionButtons(detail);
        setCarInfo(detail);
        setTimeAndLocation(detail);
        setReview(detail);
    }

    private void setRoleAndUserInfo(BookingDetailResponse detail) {
        boolean isRenter = "trips".equals(role);

        if (isRenter) {
            tvInfoName.setText(detail.getOwnerName());
            txtPhoneNumber.setText(detail.getOwnerPhone());
            Glide.with(this).load(detail.getOwnerImageUrl()).into(imgInfo);
        } else {
            tvInfoName.setText(detail.getRenterName());
            txtPhoneNumber.setText(detail.getRenterPhone());
            Glide.with(this).load(detail.getRenterImageUrl()).into(imgInfo);
        }
    }

    private void setActionButtons(BookingDetailResponse detail) {
        String status = detail.getStatus();

        tvStatus.setText(statusMapping.getOrDefault(status, status));

        btnAction.setVisibility(View.VISIBLE);
        btnConfirm.setVisibility(View.GONE);

        if ("Pending".equals(status)) {
            setupPendingActions(detail);
        } else if ("Completed".equals(status) && detail.getReview() == null) {
            btnAction.setText("Đánh giá");
            btnAction.setOnClickListener(v -> openReviewFragment(detail.getCarId(), detail.getBookingId()));
        } else {
            btnAction.setVisibility(View.GONE);
        }
    }

    private void setupPendingActions(BookingDetailResponse detail) {
        boolean isRenter = "trips".equals(role);

        if (isRenter) {
            btnAction.setText("Hủy");
            btnAction.setOnClickListener(v -> showCancelDialog("Cancelled_by_user"));
        } else {
            btnAction.setText("Từ chối");
            btnAction.setOnClickListener(v -> showCancelDialog("Cancelled_by_owner"));

            btnConfirm.setVisibility(View.VISIBLE);
            btnConfirm.setText("Xác nhận");
            btnConfirm.setOnClickListener(v -> {
                bookingViewModel.updateBookingStatus(this, detail.getBookingId(), "Confirmed", null);
                loadBookingDetail();  // Reload để cập nhật trạng thái mới
            });
        }
    }

    private void setCarInfo(BookingDetailResponse detail) {
        tvCarName.setText(detail.getCarName());
        Glide.with(this).load(detail.getCarImageUrl()).into(imgCar);
    }

    private void setTimeAndLocation(BookingDetailResponse detail) {
        txtPickUpTime.setText(formatDateTime(detail.getStartDate()));
        txtReturnTime.setText(formatDateTime(detail.getEndDate()));
        tvPickupLocation.setText(detail.getPickupLocation());
        tvDropoffLocation.setText(detail.getDropoffLocation());
    }

    private void setReview(BookingDetailResponse detail) {
        reviewContainer.removeAllViews();

        ReviewDTO review = detail.getReview();
        if (review != null) {
            View reviewView = LayoutInflater.from(this).inflate(R.layout.item_review, reviewContainer, false);

            TextView tvName = reviewView.findViewById(R.id.tvName);
            TextView tvDate = reviewView.findViewById(R.id.tvDate);
            TextView tvContent = reviewView.findViewById(R.id.tvContent);
            TextView tvStar = reviewView.findViewById(R.id.tvStarr);
            ImageView imgAvatar = reviewView.findViewById(R.id.imgAvatar);

            tvName.setText(review.getName());
            tvDate.setText(review.getCreatedAt());
            tvContent.setText(review.getComment());
            tvStar.setText("★ " + review.getRating());

            Glide.with(this).load(review.getImageUrl()).into(imgAvatar);

            reviewContainer.addView(reviewView);
        }
    }

    private void showCancelDialog(String status) {
        new AlertDialog.Builder(this)
                .setTitle("Nhập lý do hủy")
                .setView(R.layout.dialog_cancel_reason)
                .setPositiveButton("Gửi", (dialog, which) -> {
                    EditText edtReason = ((AlertDialog) dialog).findViewById(R.id.edtCancelReason);
                    String reason = edtReason.getText().toString().trim();
                    bookingViewModel.updateBookingStatus(this, bookingId, status, reason);
                    loadBookingDetail();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void openReviewFragment(int carId, long bookingId) {
        ReviewFragment reviewFragment = new ReviewFragment(carId, bookingId, new ReviewFragment.ReviewCallback() {
            @Override
            public void onReviewSubmitted() {
                loadBookingDetail();  // Load lại dữ liệu sau khi đánh giá thành công
            }
        });
        reviewFragment.show(getSupportFragmentManager(), "ReviewFragment");
    }

    private String formatDateTime(String dateTimeStr) {
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm, dd MMM yyyy");
        return dateTime.format(formatter);
    }
}
