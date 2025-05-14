package com.hcmute.ltdd.viewmodel;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.hcmute.ltdd.data.remote.ApiService;
import com.hcmute.ltdd.data.remote.RetrofitClient;
import com.hcmute.ltdd.model.ApiResponse;
import com.hcmute.ltdd.model.request.ReviewRequest;
import com.hcmute.ltdd.model.request.UpdateStatusRequest;
import com.hcmute.ltdd.model.response.BookingDetailResponse;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingViewModel extends ViewModel {

    private MutableLiveData<BookingDetailResponse> bookingDetailLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public MutableLiveData<BookingDetailResponse> getBookingDetailLiveData() {
        return bookingDetailLiveData;
    }

    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void getBookingDetail(Context context, Long bookingId) {
        isLoading.setValue(true);

        ApiService apiService = RetrofitClient.getRetrofit(context).create(ApiService.class);
        apiService.getBookingDetail(bookingId).enqueue(new Callback<ApiResponse<BookingDetailResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<BookingDetailResponse>> call, Response<ApiResponse<BookingDetailResponse>> response) {
                isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<BookingDetailResponse> apiResponse = response.body();

                    Log.d("BookingViewModel", "API Response: " + new Gson().toJson(apiResponse));

                    if (apiResponse.isSuccess()) {
                        bookingDetailLiveData.setValue(apiResponse.getData());
                    } else {
                        errorMessage.setValue("API Error: " + apiResponse.getMessage());
                    }
                } else {
                    errorMessage.setValue("Lỗi khi tải dữ liệu");
                    Log.e("BookingViewModel", "Response Error: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<BookingDetailResponse>> call, Throwable t) {
                isLoading.setValue(false);
                errorMessage.setValue("Lỗi kết nối: " + t.getMessage());
                Log.e("BookingViewModel", "Connection Error: " + t.getMessage());
            }
        });
    }

    public void updateBookingStatus(Context context, Long bookingId, String status, String reason) {
        isLoading.setValue(true);

        UpdateStatusRequest request = new UpdateStatusRequest(bookingId, status, reason);
        ApiService apiService = RetrofitClient.getRetrofit(context).create(ApiService.class);

        apiService.updateBookingStatus(request).enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                isLoading.setValue(false);
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<String> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        Toast.makeText(context, "Cập nhật trạng thái thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        errorMessage.setValue("Lỗi: " + apiResponse.getMessage());
                    }
                } else {
                    errorMessage.setValue("Lỗi khi cập nhật trạng thái");
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                isLoading.setValue(false);
                errorMessage.setValue("Lỗi kết nối: " + t.getMessage());
            }
        });
    }
    public void submitReview(Context context, int carId, long bookingId, Integer rating, String comment) {
        ReviewRequest request = new ReviewRequest(carId, bookingId, rating, comment);

        ApiService apiService = RetrofitClient.getRetrofit(context).create(ApiService.class);
        apiService.submitReview(request).enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(context, "Đã gửi đánh giá", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                Toast.makeText(context, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
