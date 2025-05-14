package com.hcmute.ltdd.ui;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.hcmute.ltdd.R;
import com.hcmute.ltdd.adapter.MyCarsAdapter;
import com.hcmute.ltdd.data.remote.ApiService;
import com.hcmute.ltdd.data.remote.RetrofitClient;
import com.hcmute.ltdd.model.ApiResponse;
import com.hcmute.ltdd.model.request.PostRequest;
import com.hcmute.ltdd.model.response.CarResponse;
import com.hcmute.ltdd.utils.SharedPrefManager;

import java.time.LocalTime;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostActivity extends AppCompatActivity {

    private TextView tvSelectedCar, tvPickupTime, tvDropoffTime;
    private EditText edtPricePerDay, edtDescription;
    private ImageView ivPickupTime, ivDropoffTime;
    private Button btnPost;
    private ApiService apiService;
    private Long selectedCarId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        tvSelectedCar = findViewById(R.id.tvSelectedCar);
        edtPricePerDay = findViewById(R.id.edtPricePerDay);
        tvPickupTime = findViewById(R.id.tvPickupTime);
        tvDropoffTime = findViewById(R.id.tvDropoffTime);
        edtDescription = findViewById(R.id.edtDescription);
        btnPost = findViewById(R.id.btnPost);
        ivPickupTime = findViewById(R.id.ivPickupTime);
        ivDropoffTime = findViewById(R.id.ivDropoffTime);

        apiService = RetrofitClient.getRetrofit(this).create(ApiService.class);

        tvSelectedCar.setOnClickListener(v -> showMyCarsDialog());

        ivPickupTime.setOnClickListener(v -> showTimePickerDialog(tvPickupTime));
        ivDropoffTime.setOnClickListener(v -> showTimePickerDialog(tvDropoffTime));

        btnPost.setOnClickListener(v -> {
            if (selectedCarId == -1) {
                Toast.makeText(this, "Vui lòng chọn xe", Toast.LENGTH_SHORT).show();
                return;
            }

            String pricePerDayStr = edtPricePerDay.getText().toString().trim();
            String pickupTime = formatTime(tvPickupTime.getText().toString().trim());
            String dropoffTime = formatTime(tvDropoffTime.getText().toString().trim());
            String description = edtDescription.getText().toString().trim();

            Log.d("PostActivity", "Price per Day: " + pricePerDayStr);
            Log.d("PostActivity", "Pickup Time: " + pickupTime);
            Log.d("PostActivity", "Dropoff Time: " + dropoffTime);
            Log.d("PostActivity", "Description: " + description);

            if (pricePerDayStr.isEmpty() || pickupTime.isEmpty() || dropoffTime.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            double pricePerDay;
            try {
                pricePerDay = Double.parseDouble(pricePerDayStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Giá thuê không hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                String formattedPickupTime = formatTime(tvPickupTime.getText().toString().trim());
                String formattedDropoffTime = formatTime(tvDropoffTime.getText().toString().trim());

                // Tạo đối tượng PostRequest với String thay vì LocalTime
                PostRequest request = new PostRequest(
                        selectedCarId,
                        pricePerDay,
                        formattedPickupTime,
                        formattedDropoffTime,
                        description
                );

                // Log JSON gửi lên server
                Log.d("PostActivity", "JSON gửi lên: " + new Gson().toJson(request));

                postNewCar(request);
            } catch (Exception e) {
                Log.e("PostActivity", "Lỗi khi chuyển đổi thời gian", e);
                Toast.makeText(PostActivity.this, "Lỗi khi chuyển đổi thời gian", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showTimePickerDialog(final TextView editText) {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                PostActivity.this,
                (view, selectedHour, selectedMinute) -> {
                    String time = String.format("%02d:%02d", selectedHour, selectedMinute);
                    editText.setText(time); // Chỉ set thời gian
                    Log.d("PostActivity", "Selected Time: " + time);
                }, hour, minute, true);
        timePickerDialog.show();
    }

    private String formatTime(String timeStr) {
        // Đảm bảo thời gian có định dạng "HH:mm:ss"
        if (timeStr.length() == 5) {
            timeStr = timeStr + ":00"; // Thêm giây mặc định "00"
        }
        Log.d("PostActivity", "Formatted Time: " + timeStr);
        return timeStr; // Trả về thời gian định dạng "HH:mm:ss"
    }

    private void showMyCarsDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_my_cars, null);

        RecyclerView rvMyCars = dialogView.findViewById(R.id.rv_my_cars);
        rvMyCars.setLayoutManager(new LinearLayoutManager(this));

        String token = "Bearer " + SharedPrefManager.getInstance(this).getToken();
        apiService.getMyCars(token).enqueue(new Callback<ApiResponse<List<CarResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<CarResponse>>> call, Response<ApiResponse<List<CarResponse>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<CarResponse>> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        List<CarResponse> carList = apiResponse.getData();

                        MyCarsAdapter adapter = new MyCarsAdapter(carList, car -> {
                            selectedCarId = car.getCarId();
                            tvSelectedCar.setText(car.getName());
                            dialog.dismiss();
                        });

                        rvMyCars.setAdapter(adapter);
                    } else {
                        Toast.makeText(PostActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(PostActivity.this, "Không tải được danh sách xe", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<CarResponse>>> call, Throwable t) {
                Toast.makeText(PostActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        dialog.setContentView(dialogView);
        dialog.show();
    }


    private void postNewCar(PostRequest request) {
        String token = "Bearer " + SharedPrefManager.getInstance(this).getToken();

        apiService.createPost(request, token).enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(PostActivity.this, "Đăng bài thành công", Toast.LENGTH_SHORT).show();
                    finish(); // Đóng Activity sau khi đăng bài thành công
                } else {
                    Toast.makeText(PostActivity.this, "Đăng bài thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                Toast.makeText(PostActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
