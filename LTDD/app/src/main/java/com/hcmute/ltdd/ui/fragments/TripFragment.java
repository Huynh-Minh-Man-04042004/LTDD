package com.hcmute.ltdd.ui.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hcmute.ltdd.R;
import com.hcmute.ltdd.adapter.BookingHistoryAdapter;
import com.hcmute.ltdd.data.remote.ApiService;
import com.hcmute.ltdd.data.remote.RetrofitClient;
import com.hcmute.ltdd.model.ApiResponse;
import com.hcmute.ltdd.model.response.BookingHistoryResponse;
import com.hcmute.ltdd.ui.BookingDetailActivity;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TripFragment extends Fragment {

    private TextView tabTrips, tabOrders;
    private View indicatorTrips, indicatorOrders;
    private RecyclerView rvContent;
    private BookingHistoryAdapter adapter;
    private ApiService apiService;
    private String currentTab = "trips";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_trip, container, false);
        initViews(view);
        setupRecyclerView();
        setActiveTab(true);
        setupListeners();
        loadBookingHistory(); // Mặc định load "Chuyến của tôi"
        return view;
    }

    private void initViews(View view) {
        tabTrips = view.findViewById(R.id.tabMyTrips);
        tabOrders = view.findViewById(R.id.tabMyOrders);
        indicatorTrips = view.findViewById(R.id.indicatorTrips);
        indicatorOrders = view.findViewById(R.id.indicatorOrders);
        rvContent = view.findViewById(R.id.rvContent);
        apiService = RetrofitClient.getRetrofit(requireContext()).create(ApiService.class);
    }

    private void setupRecyclerView() {
        adapter = new BookingHistoryAdapter();
        rvContent.setLayoutManager(new LinearLayoutManager(getContext()));
        rvContent.setAdapter(adapter);

        adapter.setOnItemClickListener(bookingId -> {
            Intent intent = new Intent(getContext(), BookingDetailActivity.class);
            intent.putExtra("bookingId", bookingId);
            intent.putExtra("tab", currentTab); // Truyền thêm tab hiện tại
            startActivity(intent);
        });
    }

    private void setupListeners() {
        tabTrips.setOnClickListener(v -> {
            setActiveTab(true);
            loadBookingHistory();
        });

        tabOrders.setOnClickListener(v -> {
            setActiveTab(false);
            loadMyBookings();
        });
    }


    private void setActiveTab(boolean isTrips) {
        currentTab = isTrips ? "trips" : "orders";
        tabTrips.setTextColor(getResources().getColor(isTrips ? android.R.color.holo_orange_dark : android.R.color.darker_gray));
        tabOrders.setTextColor(getResources().getColor(!isTrips ? android.R.color.holo_orange_dark : android.R.color.darker_gray));
        indicatorTrips.setVisibility(isTrips ? View.VISIBLE : View.INVISIBLE);
        indicatorOrders.setVisibility(!isTrips ? View.VISIBLE : View.INVISIBLE);
        adapter.clearData();
    }

    private void loadBookingHistory() {
        apiService.getBookingHistory().enqueue(new Callback<ApiResponse<List<BookingHistoryResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<BookingHistoryResponse>>> call, Response<ApiResponse<List<BookingHistoryResponse>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.setBookingHistoryList(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<BookingHistoryResponse>>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi tải dữ liệu chuyến của tôi", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void loadMyBookings() {
        apiService.getMyBookings().enqueue(new Callback<ApiResponse<List<BookingHistoryResponse>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<BookingHistoryResponse>>> call, Response<ApiResponse<List<BookingHistoryResponse>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter.setBookingHistoryList(response.body().getData());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<BookingHistoryResponse>>> call, Throwable t) {
                Toast.makeText(getContext(), "Lỗi tải dữ liệu đơn của tôi", Toast.LENGTH_SHORT).show();
            }
        });
    }
}