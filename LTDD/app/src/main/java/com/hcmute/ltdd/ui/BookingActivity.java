package com.hcmute.ltdd.ui;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.hcmute.ltdd.R;
import com.hcmute.ltdd.data.remote.ApiService;
import com.hcmute.ltdd.data.remote.RetrofitClient;
import com.hcmute.ltdd.model.ApiResponse;
import com.hcmute.ltdd.model.request.BookingPreviewRequest;
import com.hcmute.ltdd.model.response.BookingPreviewResponse;
import com.hcmute.ltdd.model.response.CarDetailResponse;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingActivity extends AppCompatActivity {

    private TextView txtRentalPrice, txtInsuranceFee, txtDeliveryFee, txtTotalDays,
            txtTotalPrice, txtDriverFee,
            txtCarName, txtPickupLocation, txtDropoffLocation,
            txtOwnerName,
            txtCarRating, txtCarTrips, txtOwnerRating, txtOwnerTrips, txtBrand, txtPickUpTime, txtReturnTime;
    private String startDate, endDate, pickupLocation, dropoffLocation;
    private ApiService apiService;
    private boolean insuranceSelected, deliverySelected, driverRequired;
    private CarDetailResponse carDetail;
    private ImageView imageCar, imgOwnerImage;
    private Button btnRentCar;
    private final SimpleDateFormat inputFormat = new SimpleDateFormat("EEEE, dd/MM/yyyy, HH:mm", Locale.getDefault());
    private final SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        initViews();
        receiveIntentData();
        sendBookingPreviewRequest();
    }

    private void initViews() {
        txtRentalPrice = findViewById(R.id.txtRentalPrice);
        txtInsuranceFee = findViewById(R.id.txtInsuranceFee);
        txtDeliveryFee = findViewById(R.id.txtDeliveryFee);
        txtTotalDays = findViewById(R.id.txtTotalDays);
        txtTotalPrice = findViewById(R.id.txtTotalPrice);
        txtDriverFee = findViewById(R.id.txtDriverFee);

        txtCarName = findViewById(R.id.txtName_bookingcar);
        imageCar = findViewById(R.id.imgCar_booking);
        txtBrand = findViewById(R.id.txtBrand_bookingcar);
        txtCarRating = findViewById(R.id.txtRating_bookingcar);
        txtCarTrips = findViewById(R.id.txtTrips_bookingcar);

        txtOwnerName = findViewById(R.id.txtOwnerName);
        imgOwnerImage = findViewById(R.id.imgOwnerImage);
        txtOwnerRating = findViewById(R.id.txtOwnerRating);
        txtOwnerTrips = findViewById(R.id.txtOwnerTrips);

        txtPickupLocation = findViewById(R.id.txtPickupLocation2);
        txtDropoffLocation = findViewById(R.id.txtDropoffLocation2);
        txtPickUpTime = findViewById(R.id.txtPickUpTime2);
        txtReturnTime = findViewById(R.id.txtReturnTime2);

        btnRentCar = findViewById(R.id.btnRentCar_bookingcar);
        btnRentCar.setOnClickListener(v -> {
            sendBookingConfirmRequest();
        });

    }

    private void receiveIntentData() {
        Intent intent = getIntent();
        startDate = intent.getStringExtra("startDate");
        endDate = intent.getStringExtra("endDate");
        pickupLocation = intent.getStringExtra("pickupLocation");
        dropoffLocation = intent.getStringExtra("dropoffLocation");
        insuranceSelected = intent.getBooleanExtra("insuranceSelected", false);
        deliverySelected = intent.getBooleanExtra("deliverySelected", false);
        driverRequired = intent.getBooleanExtra("driverRequired", false);
        carDetail = (CarDetailResponse) intent.getSerializableExtra("carDetail");

        apiService = RetrofitClient.getRetrofit(this).create(ApiService.class);
    }
    private String convertToApiFormat(String dateString) {
        SimpleDateFormat inputFormatWithDay = new SimpleDateFormat("EEEE, dd/MM/yyyy, HH:mm", new Locale("vi", "VN"));
        SimpleDateFormat inputFormatWithoutDay = new SimpleDateFormat("dd/MM/yyyy, HH:mm", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());

        try {
            Log.d("DateConversion", "Input date string: " + dateString);

            Date date;
            if (dateString.contains(",")) {
                // Kiểm tra nếu chuỗi có chứa thứ
                if (dateString.split(", ").length == 3) {
                    date = inputFormatWithDay.parse(dateString);
                } else {
                    date = inputFormatWithoutDay.parse(dateString);
                }
            } else {
                Log.e("DateConversion", "Định dạng ngày không hợp lệ: " + dateString);
                Toast.makeText(this, "Lỗi định dạng ngày giờ", Toast.LENGTH_SHORT).show();
                return null;
            }

            String formattedDate = outputFormat.format(date);
            Log.d("DateConversion", "Formatted date: " + formattedDate);
            return formattedDate;

        } catch (ParseException e) {
            Log.e("DateConversion", "ParseException: " + e.getMessage());
            Toast.makeText(this, "Lỗi định dạng ngày giờ", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    private void updateUI(BookingPreviewResponse data) {
        if (carDetail != null) {
            txtCarName.setText(carDetail.getName());
            txtBrand.setText(carDetail.getBrand());
            Glide.with(this).load(carDetail.getImageUrl()).into(imageCar);
            txtCarRating.setText(String.format("%.1f", carDetail.getAvgRating()));
            txtCarTrips.setText(String.format("%d chuyến", carDetail.getTripCount()));

            // Thông tin chủ xe
            txtOwnerName.setText(carDetail.getOwnerName());
            Glide.with(this).load(carDetail.getOwnerImage()).into(imgOwnerImage);
            txtOwnerRating.setText(String.format("%.1f", carDetail.getOwnerAvgRating()));
            txtOwnerTrips.setText(String.format("%d chuyến", carDetail.getOwnerTripCount()));

            txtPickupLocation.setText(pickupLocation);
            txtDropoffLocation.setText(dropoffLocation);
            txtPickUpTime.setText(startDate);
            txtReturnTime.setText(endDate);

            txtRentalPrice.setText(String.format("%.0f VND/ngày", data.getRentalPricePerDay()));
            txtInsuranceFee.setText(String.format("%.0f VND/ngày", data.getInsuranceFeePerDay()));
            txtDeliveryFee.setText(String.format("%.0f VND", data.getDeliveryFee()));
            txtTotalDays.setText(String.format("%.1f", data.getTotalDays()));
            txtTotalPrice.setText(String.format("%.0f VND", data.getTotalPrice()));
            txtDriverFee.setText(String.format("%.0f VND", data.getDriverRequired()));
        }
    }

    private void sendBookingPreviewRequest() {
        String startDateFormatted = convertToApiFormat(startDate);
        String endDateFormatted = convertToApiFormat(endDate);

        if (startDateFormatted == null || endDateFormatted == null) {
            Log.e("BookingPreviewRequest", "Ngày nhận hoặc trả xe không hợp lệ.");
            Toast.makeText(this, "Lỗi định dạng thời gian", Toast.LENGTH_SHORT).show();
            return;
        }
        BookingPreviewRequest bookingPreviewRequest = new BookingPreviewRequest(
                carDetail.getId(),
                startDateFormatted,
                endDateFormatted,
                pickupLocation,
                dropoffLocation,
                insuranceSelected,
                deliverySelected,
                driverRequired
        );
        Gson gson = new Gson();
        String json = gson.toJson(bookingPreviewRequest);
        Log.d("BookingPreviewRequest", json);

        apiService.bookingPreview(bookingPreviewRequest).enqueue(new Callback<ApiResponse<BookingPreviewResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<BookingPreviewResponse>> call, Response<ApiResponse<BookingPreviewResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<BookingPreviewResponse> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        BookingPreviewResponse data = apiResponse.getData();
                        updateUI(data);
                    } else {
                        Toast.makeText(BookingActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(BookingActivity.this, "Không thể tải dữ liệu. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<BookingPreviewResponse>> call, Throwable t) {
                Toast.makeText(BookingActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    private void sendBookingConfirmRequest() {
        String startDateFormatted = convertToApiFormat(startDate);
        String endDateFormatted = convertToApiFormat(endDate);

        if (startDateFormatted == null || endDateFormatted == null) {
            Toast.makeText(this, "Lỗi định dạng thời gian", Toast.LENGTH_SHORT).show();
            return;
        }

        BookingPreviewRequest bookingRequest = new BookingPreviewRequest(
                carDetail.getId(),
                startDateFormatted,
                endDateFormatted,
                pickupLocation,
                dropoffLocation,
                insuranceSelected,
                deliverySelected,
                driverRequired
        );

        apiService.confirmBooking(bookingRequest).enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<String> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        Toast.makeText(BookingActivity.this, "Đặt xe thành công!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(BookingActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(BookingActivity.this, "Không thể xác nhận đặt xe. Vui lòng thử lại.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                Toast.makeText(BookingActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
