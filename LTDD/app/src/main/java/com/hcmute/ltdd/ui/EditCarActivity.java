package com.hcmute.ltdd.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.hcmute.ltdd.R;
import com.hcmute.ltdd.adapter.FeatureAdapter;
import com.hcmute.ltdd.data.remote.ApiService;
import com.hcmute.ltdd.data.remote.RetrofitClient;
import com.hcmute.ltdd.data.repository.FeatureRepository;
import com.hcmute.ltdd.model.ApiResponse;
import com.hcmute.ltdd.model.Feature;
import com.hcmute.ltdd.model.request.EditCarRequest;
import com.hcmute.ltdd.model.response.CarDetailResponse;
import com.hcmute.ltdd.utils.CloudinaryManager;
import com.hcmute.ltdd.utils.SharedPrefManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditCarActivity extends AppCompatActivity {

    private AutoCompleteTextView spnSeats;
    private EditText edtCarName, edtBrand, edtEnergyConsumption, edtDescription, edtPrice, edtDetailLocation;
    private Spinner spnGearType, spnFuelType, spnLocation;
    private CheckBox cbDriverRequired;
    private Button btnUpdateCar;
    private ImageView ivSelectImage, ivPreviewImage, ivSelectFeatures;
    private TextView tvSelectedFeatures;

    private ApiService apiService;
    private Long carId;
    private Uri selectedImageUri;
    private String imageUrl;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private List<Feature> featureList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);

        ((TextView) findViewById(R.id.tvTitle)).setText("Chỉnh sửa xe");
        initViews();

        apiService = RetrofitClient.getRetrofit(this).create(ApiService.class);
        carId = getIntent().getLongExtra("carId", -1);
        Log.e("EditCarActivity", "Received carId: " + carId);

        CloudinaryManager.init(this);
        featureList = FeatureRepository.getFeatures();

        if (carId != -1) {
            loadCarDetails(carId);
        }

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        ivPreviewImage.setImageURI(selectedImageUri);
                    }
                }
        );

        ivSelectImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });

        btnUpdateCar.setOnClickListener(v -> updateCar());
    }

    private void initViews() {
        edtCarName = findViewById(R.id.edtCarName);
        edtBrand = findViewById(R.id.edtBrand);
        edtEnergyConsumption = findViewById(R.id.edtEnergyConsumption);
        edtDescription = findViewById(R.id.edtDescription);
        spnGearType = findViewById(R.id.spnGearType);
        spnFuelType = findViewById(R.id.spnFuelType);
        cbDriverRequired = findViewById(R.id.cbDriverRequired);
        btnUpdateCar = findViewById(R.id.btnRegisterCar);
        ivSelectImage = findViewById(R.id.ivSelectImage);
        ivPreviewImage = findViewById(R.id.ivPreviewImage);
        tvSelectedFeatures = findViewById(R.id.tvSelectedFeatures);
        edtPrice = findViewById(R.id.edtPrice);
        spnLocation = findViewById(R.id.spnLocation);
        spnSeats = findViewById(R.id.spnSeats);
        edtDetailLocation = findViewById(R.id.edtDetailLocation);
        ivSelectFeatures = findViewById(R.id.ivSelectFeatures);

        btnUpdateCar.setText("Cập nhật xe");

        // Dữ liệu cho Gear Type
        List<String> gearTypes = Arrays.asList("Manual", "Automatic");
        ArrayAdapter<String> gearAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, gearTypes);
        gearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnGearType.setAdapter(gearAdapter);

        // Dữ liệu cho Fuel Type
        List<String> fuelTypes = Arrays.asList("Gasoline", "Diesel", "Electric");
        ArrayAdapter<String> fuelAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, fuelTypes);
        fuelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnFuelType.setAdapter(fuelAdapter);

        List<String> districtList = new ArrayList<>(Arrays.asList(
                "Quận 1", "Quận 2", "Quận 3", "Quận 4", "Quận 5", "Quận 6",
                "Quận 7", "Quận 8", "Quận 9", "Quận 10", "Quận 11",
                "Quận 12", "Bình Thạnh", "Gò Vấp", "Phú Nhuận",
                "Tân Bình", "Tân Phú", "Thủ Đức"
        ));

        ArrayAdapter<String> locationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, districtList);
        locationAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnLocation.setAdapter(locationAdapter);

        ivSelectFeatures.setOnClickListener(v -> showSelectFeaturesDialog());
    }

    private void showSelectFeaturesDialog() {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_select_features, null);

        RecyclerView rvFeatures = view.findViewById(R.id.rvFeatures);
        Button btnSelectFeatures = view.findViewById(R.id.btnSelectFeatures);

        FeatureAdapter adapter = new FeatureAdapter(this, featureList);
        rvFeatures.setLayoutManager(new LinearLayoutManager(this));
        rvFeatures.setAdapter(adapter);

        btnSelectFeatures.setOnClickListener(v1 -> {
            StringBuilder selectedFeatures = new StringBuilder();
            for (Feature feature : featureList) {
                if (feature.isSelected()) {
                    selectedFeatures.append(feature.getName()).append(", ");
                }
            }

            String selectedText = selectedFeatures.length() > 0
                    ? selectedFeatures.substring(0, selectedFeatures.length() - 2)
                    : "Chưa chọn tính năng";

            tvSelectedFeatures.setText(selectedText);
            dialog.dismiss();
        });

        dialog.setContentView(view);
        dialog.show();
    }

    private void loadCarDetails(Long carId) {
        apiService.getCarDetail(carId).enqueue(new Callback<ApiResponse<CarDetailResponse>>() {
            @Override
            public void onResponse(Call<ApiResponse<CarDetailResponse>> call, Response<ApiResponse<CarDetailResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CarDetailResponse car = response.body().getData();

                    edtCarName.setText(car.getName());
                    edtBrand.setText(car.getBrand());
                    edtDescription.setText(car.getDescription());
                    edtPrice.setText(String.valueOf(car.getPrice()));
                    edtEnergyConsumption.setText(String.valueOf(car.getEnergyConsumption()));
                    cbDriverRequired.setChecked(car.isDriverRequired());
                    edtDetailLocation.setText(car.getLocation());
                    imageUrl = car.getImageUrl();

                    if (imageUrl != null) {
                        Glide.with(EditCarActivity.this).load(imageUrl).into(ivPreviewImage);
                    }

                    ArrayAdapter<String> gearAdapter = (ArrayAdapter<String>) spnGearType.getAdapter();
                    spnGearType.setSelection(gearAdapter.getPosition(car.getGearType()));

                    ArrayAdapter<String> fuelAdapter = (ArrayAdapter<String>) spnFuelType.getAdapter();
                    spnFuelType.setSelection(fuelAdapter.getPosition(car.getFuelType()));

                    spnSeats.setText(String.valueOf(car.getSeats()));

                    if (car.getHasEtc() != null) {
                        tvSelectedFeatures.setText(String.join(", ", car.getHasEtc()));
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<CarDetailResponse>> call, Throwable t) {
                Toast.makeText(EditCarActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateCar() {
        String name = edtCarName.getText().toString().trim();
        String brand = edtBrand.getText().toString().trim();
        String description = edtDescription.getText().toString().trim();
        double price = Double.parseDouble(edtPrice.getText().toString().trim());
        double energyConsumption = Double.parseDouble(edtEnergyConsumption.getText().toString().trim());
        boolean driverRequired = cbDriverRequired.isChecked();
        String seats = spnSeats.getText().toString().trim();
        String gearType = spnGearType.getSelectedItem().toString();
        String fuelType = spnFuelType.getSelectedItem().toString();
        String location = edtDetailLocation.getText().toString().trim() + ", " + spnLocation.getSelectedItem().toString();

        List<String> selectedFeatures = new ArrayList<>();
        for (Feature feature : featureList) {
            if (feature.isSelected()) {
                selectedFeatures.add(feature.getName());
            }
        }

        if (selectedImageUri != null) {
            CloudinaryManager.uploadImage(this, selectedImageUri, new UploadCallback() {
                @Override
                public void onStart(String requestId) {
                    Toast.makeText(EditCarActivity.this, "Bắt đầu upload ảnh...", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onProgress(String requestId, long bytes, long totalBytes) {
                    Log.d("EditCarActivity", "Uploading: " + bytes + "/" + totalBytes);
                }

                @Override
                public void onSuccess(String requestId, Map resultData) {
                    String newImageUrl = (String) resultData.get("secure_url");
                    sendUpdateRequest(selectedFeatures, name, brand, description, price, energyConsumption, driverRequired, newImageUrl, seats, gearType, fuelType, location);
                }

                @Override
                public void onError(String requestId, ErrorInfo error) {
                    Toast.makeText(EditCarActivity.this, "Upload ảnh thất bại: " + error.getDescription(), Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onReschedule(String requestId, ErrorInfo error) {
                    Toast.makeText(EditCarActivity.this, "Upload bị tạm hoãn: " + error.getDescription(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            sendUpdateRequest(selectedFeatures, name, brand, description, price, energyConsumption, driverRequired, imageUrl, seats, gearType, fuelType, location);
        }
    }

    private void sendUpdateRequest(List<String> features, String name, String brand, String description, double price, double energyConsumption, boolean driverRequired, String imageUrl, String seats, String gearType, String fuelType, String location) {
        String token = "Bearer " + SharedPrefManager.getInstance(this).getToken();

        EditCarRequest request = new EditCarRequest(
                features, price, location, driverRequired, imageUrl, energyConsumption, fuelType, Integer.parseInt(seats), gearType, description, brand, name
        );

        apiService.updateCar(carId, request).enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(EditCarActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EditCarActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                Toast.makeText(EditCarActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
