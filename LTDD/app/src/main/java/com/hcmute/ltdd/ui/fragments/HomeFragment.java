package com.hcmute.ltdd.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.hcmute.ltdd.R;
import com.hcmute.ltdd.data.remote.ApiService;
import com.hcmute.ltdd.data.remote.RetrofitClient;
import com.hcmute.ltdd.model.ApiResponse;
import com.hcmute.ltdd.model.response.UserProfileResponse;
import com.hcmute.ltdd.ui.CarListActivity;
import com.hcmute.ltdd.ui.FavoritesActivity;
import com.hcmute.ltdd.ui.NotifyActivity;
import com.hcmute.ltdd.utils.SharedPrefManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private Button btnSelfDrive, btnWithDriver;
    private ViewFlipper viewFlipper;
    private ImageView iconHeart, iconNotify;
    private TextView tvUsername, tvEmail, tvLocation;
    private ShapeableImageView imageAvatar;

    private boolean driverRequired = false;
    private ApiService apiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        setupListeners();
        setActiveTab(btnSelfDrive, btnWithDriver);
        loadUserProfile();
    }

    private void initViews(View view) {
        btnSelfDrive = view.findViewById(R.id.btnSelfDrive);
        btnWithDriver = view.findViewById(R.id.btnWithDriver);
        viewFlipper = view.findViewById(R.id.viewFlipper);
        iconHeart = view.findViewById(R.id.iconHeart);
        iconNotify = view.findViewById(R.id.iconNotify);
        tvUsername = view.findViewById(R.id.tvUsername);
        tvEmail = view.findViewById(R.id.tvEmail);
        imageAvatar = view.findViewById(R.id.imageAvatar);
        tvLocation = view.findViewById(R.id.tvLocation);
        apiService = RetrofitClient.getRetrofit(requireContext()).create(ApiService.class);
    }

    private void setupListeners() {
        btnSelfDrive.setOnClickListener(v -> {
            viewFlipper.setDisplayedChild(0);
            setActiveTab(btnSelfDrive, btnWithDriver);
            driverRequired = false;
        });

        btnWithDriver.setOnClickListener(v -> {
            viewFlipper.setDisplayedChild(1);
            setActiveTab(btnWithDriver, btnSelfDrive);
            driverRequired = true;
        });

        iconHeart.setOnClickListener(v -> startActivity(new Intent(getActivity(), FavoritesActivity.class)));
        iconNotify.setOnClickListener(v -> startActivity(new Intent(getActivity(), NotifyActivity.class)));

        requireView().findViewById(R.id.btnFindCar).setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CarListActivity.class);
            intent.putExtra("location", tvLocation.getText().toString());
            intent.putExtra("driverRequired", driverRequired);
            startActivity(intent);
        });
    }

    private void setActiveTab(Button active, Button inactive) {
        active.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.tab_selected));
        active.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));

        inactive.setBackground(ContextCompat.getDrawable(requireContext(), R.drawable.tab_unselected));
        inactive.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));
    }

    private void loadUserProfile() {
        String token = SharedPrefManager.getInstance(requireContext()).getToken();
        apiService.getUserProfile("Bearer " + token)
                .enqueue(new Callback<ApiResponse<UserProfileResponse>>() {
                    @Override
                    public void onResponse(Call<ApiResponse<UserProfileResponse>> call, Response<ApiResponse<UserProfileResponse>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            ApiResponse<UserProfileResponse> apiResponse = response.body();
                            if (apiResponse.isSuccess()) {
                                bindUserProfile(apiResponse.getData());
                            } else {
                                showToast(apiResponse.getMessage());
                            }
                        } else {
                            showToast("Lỗi tải thông tin người dùng");
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponse<UserProfileResponse>> call, Throwable t) {
                        showToast("Lỗi kết nối: " + t.getMessage());
                    }
                });
    }


    private void bindUserProfile(UserProfileResponse user) {
        String name = user.getName();
        if (name == null || name.trim().isEmpty()) {
            tvUsername.setText("Chào bạn");
        } else {
            tvUsername.setText(name);
        }
        tvEmail.setText(user.getEmail());
        String location = user.getAddress();
        if (location == null || location.trim().isEmpty()) {
            tvLocation.setText("Quận 1");
        } else {
            tvLocation.setText(location);
        }

        String imageUrl = user.getImageUrl();
        Glide.with(requireContext())
                .load(imageUrl != null && !imageUrl.isEmpty() ? imageUrl : R.drawable.avatar)
                .circleCrop()
                .into(imageAvatar);
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}