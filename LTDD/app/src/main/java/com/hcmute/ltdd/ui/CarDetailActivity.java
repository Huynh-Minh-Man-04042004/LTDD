package com.hcmute.ltdd.ui;

import static androidx.core.content.ContentProviderCompat.requireContext;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.hcmute.ltdd.R;
import com.hcmute.ltdd.model.response.CarDetailResponse;
import com.hcmute.ltdd.model.response.ReviewDTO;
import com.hcmute.ltdd.ui.fragments.DateRangePickerBottomSheet;
import com.hcmute.ltdd.ui.fragments.LocationBottomSheet;
import com.hcmute.ltdd.viewmodel.CarViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.core.graphics.ColorUtils;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CarDetailActivity extends AppCompatActivity {
    private CarViewModel carViewModel;
    private ImageView imgCar, imgOwnerImage;
    private TextView txtName, txtRating, txtOwnerName, tvCarName,
            txtOwnerTrips, txtOwnerRating, txtTrips, txtPickupLocation,
            txtLocation, txtPrice, txtDescription, txtDropoffLocation,
            txtGearType, txtSeats, txtFuelType, txtEnergyConsumption;
    private LinearLayout pickupLocationLinearLayout;
    private LinearLayout dropoffLocationLinearLayout;
    private LinearLayout startTimeLinearLayout, endTimeLinearLayout;
    private TextView txtPickupTime, txtReturnTime;
    private Long carId;
    private CheckBox cbPickup;
    private GridLayout gridLayout;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_detail);

        initViews();
        initViewModel();

        // Handle ScrollView background color change
        setupScrollViewBehavior();

        // Handle button clicks
        setupListeners();

        // Get Car ID from Intent
        carId = getIntent().getLongExtra("carId", -1);
        if (carId == -1) {
            Toast.makeText(this, "Không tìm thấy xe", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Fetch Car Detail
        carViewModel.getCarDetail(this, carId);
        observeViewModel();
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> finish());
        startTimeLinearLayout.setOnClickListener(v -> openDateRangePicker(true));
        endTimeLinearLayout.setOnClickListener(v -> openDateRangePicker(false));
        pickupLocationLinearLayout.setOnClickListener(v -> openLocationBottomSheet(true));
        dropoffLocationLinearLayout.setOnClickListener(v -> openLocationBottomSheet(false));
        findViewById(R.id.btnRentCar_cardetail).setOnClickListener(v -> {
            String pickupTime = txtPickupTime.getText().toString();
            String returnTime = txtReturnTime.getText().toString();
            String dropoffLocation = txtDropoffLocation.getText().toString();

            if (dropoffLocation.equals("Chọn địa chỉ trả") ||
                    (pickupTime.contains("10/04/2025") || returnTime.contains("10/04/2025"))) {

                Toast.makeText(this, "Vui lòng chọn địa chỉ và thời gian nhận, trả xe hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            if (carViewModel.getCarDetailLiveData().getValue() != null) {
                openBookingActivity(carViewModel.getCarDetailLiveData().getValue());
            } else {
                Toast.makeText(this, "Dữ liệu xe chưa sẵn sàng", Toast.LENGTH_SHORT).show();
            }
        });
        cbPickup.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                txtPickupLocation.setEnabled(true);
            } else {
                txtPickupLocation.setEnabled(false);
            }
        });
    }


    private void openDateRangePicker(boolean isPickup) {
        DateRangePickerBottomSheet dateRangePicker = new DateRangePickerBottomSheet();
        dateRangePicker.setDateRangePickerListener((selectedDate, selectedTime) -> {
            String dayOfWeek = new java.text.SimpleDateFormat("EEEE", new Locale("vi", "VN")).format(selectedDate.getTime());
            String formattedDate = new java.text.SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(selectedDate.getTime());
            String summary = String.format("%s, %s, %s", dayOfWeek, formattedDate, selectedTime);

            if (isPickup) {
                txtPickupTime.setText(summary);
            } else {
                txtReturnTime.setText(summary);
            }
        });

        dateRangePicker.show(getSupportFragmentManager(), dateRangePicker.getTag());
    }


    private void openBookingActivity(CarDetailResponse carDetail) {
        Intent intent = new Intent(CarDetailActivity.this, BookingActivity.class);
        intent.putExtra("startDate", txtPickupTime.getText().toString());
        intent.putExtra("endDate", txtReturnTime.getText().toString());
        intent.putExtra("pickupLocation", txtPickupLocation.getText().toString());
        intent.putExtra("dropoffLocation", txtDropoffLocation.getText().toString());
        intent.putExtra("insuranceSelected", cbPickup.isChecked());
        intent.putExtra("deliverySelected", cbPickup.isChecked());
        intent.putExtra("driverRequired", cbPickup.isChecked());
        carDetail.setReviews(null);
        // Truyền nguyên đối tượng CarDetailResponse
        intent.putExtra("carDetail", carDetail);

        startActivity(intent);
    }

    private void openLocationBottomSheet(boolean isPickup) {
        LocationBottomSheet locationBottomSheet = new LocationBottomSheet();
        locationBottomSheet.setLocationListener(location -> {
            if (isPickup) {
                txtPickupLocation.setText(location);
            } else {
                txtDropoffLocation.setText(location);
            }
        });
        locationBottomSheet.show(getSupportFragmentManager(), locationBottomSheet.getTag());
    }

    private void initViews() {
        imgCar = findViewById(R.id.imgCar_cardetail);
        txtName = findViewById(R.id.txtName_cardetail);
        txtRating = findViewById(R.id.txtRating_cardetail);
        txtTrips = findViewById(R.id.txtTrips_cardetail);
        txtLocation = findViewById(R.id.txtLocation_cardetail);
        txtPrice = findViewById(R.id.txtCarPriceDetail_cardetail);
        txtDescription = findViewById(R.id.txtDescription_cardetail);
        tvCarName = findViewById(R.id.tvCarName);
        txtOwnerName = findViewById(R.id.txtOwnerName);
        imgOwnerImage = findViewById(R.id.imgOwnerImage);
        txtOwnerRating = findViewById(R.id.txtOwnerRating);
        txtOwnerTrips = findViewById(R.id.txtOwnerTrips);
        txtPickupLocation = findViewById(R.id.txtPickupLocation);
        txtDropoffLocation = findViewById(R.id.txtDropoffLocation);
        pickupLocationLinearLayout = findViewById(R.id.pickupLocationLinearLayout);
        dropoffLocationLinearLayout = findViewById(R.id.dropoffLocationLinearLayout);
        startTimeLinearLayout = findViewById(R.id.startTimeLinearLayout);
        endTimeLinearLayout = findViewById(R.id.endTimeLinearLayout);
        txtPickupTime = findViewById(R.id.txtPickupTime);
        txtReturnTime = findViewById(R.id.txtReturnTime);
        txtGearType = findViewById(R.id.gearType_cardetail);
        txtSeats = findViewById(R.id.seats_cardetail);
        txtFuelType = findViewById(R.id.fuelType_cardetail);
        txtEnergyConsumption = findViewById(R.id.energyConsumption_cardetail);
        txtSeats = findViewById(R.id.seats_cardetail);
        txtFuelType = findViewById(R.id.fuelType_cardetail);
        txtEnergyConsumption = findViewById(R.id.energyConsumption_cardetail);
        cbPickup = findViewById(R.id.cbPickup);
        gridLayout = findViewById(R.id.gridAmenities);
        btnBack = findViewById(R.id.btnBack_cardetail);
    }

    private void initViewModel() {
        carViewModel = new ViewModelProvider(this).get(CarViewModel.class);
    }

    private void observeViewModel() {
        carViewModel.getCarDetailLiveData().observe(this, carDetail -> {
            if (carDetail != null) {
                updateUI(carDetail);
            }
        });

        carViewModel.getErrorMessage().observe(this, message -> {
            if (message != null) {
                Toast.makeText(CarDetailActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        carViewModel.getIsLoading().observe(this, isLoading -> {
            // Show/hide progress bar based on isLoading
        });
    }

    private void setupScrollViewBehavior() {
        ScrollView scrollView = findViewById(R.id.scrollView_cardetail);
        LinearLayout layoutTopBar = findViewById(R.id.layoutTopBar_cardetail);

        scrollView.getViewTreeObserver().addOnScrollChangedListener(() -> {
            int scrollY = scrollView.getScrollY();
            int imageHeight = imgCar.getHeight();

            if (scrollY >= imageHeight) {
                layoutTopBar.setBackgroundColor(Color.WHITE);
            } else {
                // Tạo hiệu ứng trong suốt mượt mà
                float alpha = ((float) scrollY / (float) imageHeight)*255;
                alpha = Math.max(0, Math.min(255, alpha));
                int colorWithAlpha = ColorUtils.setAlphaComponent(Color.WHITE, (int) alpha);
                layoutTopBar.setBackgroundColor(colorWithAlpha);
            }
        });
    }
    private void updateUI(CarDetailResponse carDetail) {
        Glide.with(this).load(carDetail.getImageUrl()).into(imgCar);
        txtName.setText(carDetail.getName());
        txtRating.setText(String.format("%.1f ", carDetail.getAvgRating()));
        txtTrips.setText(String.format("%d chuyến", carDetail.getTripCount()));
        txtLocation.setText(carDetail.getLocation());
        txtPrice.setText(String.format("%.0fK/ngày", carDetail.getPrice()));
        txtDescription.setText(carDetail.getDescription());
        tvCarName.setText(carDetail.getName());
        txtOwnerName.setText(carDetail.getOwnerName());
        Glide.with(this).load(carDetail.getOwnerImage()).into(imgOwnerImage);
        txtOwnerRating.setText(String.format("%.1f ", carDetail.getOwnerAvgRating()));
        txtOwnerTrips.setText(String.format("%d chuyến", carDetail.getOwnerTripCount()));
        txtGearType.setText(carDetail.getGearType());
        txtSeats.setText(String.format("%d chỗ", carDetail.getSeats()));
        txtFuelType.setText(carDetail.getFuelType());
        txtEnergyConsumption.setText(String.format("%.1fL/100km", carDetail.getEnergyConsumption()));
        txtPickupLocation.setText(carDetail.getLocation());
        txtPickupLocation.setEnabled(false);
        updateAmenitiesUI(carDetail);
        addReviews(carDetail.getReviews());
    }
    private void updateAmenitiesUI(CarDetailResponse carDetail) {
        gridLayout.removeAllViews();

        if (carDetail.getHasEtc() != null && !carDetail.getHasEtc().isEmpty()) {
            for (String amenity : carDetail.getHasEtc()) {
                LinearLayout linearLayout = new LinearLayout(this);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                linearLayout.setGravity(Gravity.CENTER);
                linearLayout.setPadding(8, 8, 8, 8);

                ImageView imageView = new ImageView(this);
                int sizeInDp = 40; // Kích thước bạn muốn theo dp
                float scale = getResources().getDisplayMetrics().density;
                int sizeInPx = (int) (sizeInDp * scale + 0.5f);

                LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(sizeInPx, sizeInPx);
                imageView.setLayoutParams(imageParams);
                imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

                TextView textView = new TextView(this);
                textView.setTextSize(15);
                textView.setText(amenity);
                textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                switch (amenity) {
                    case "Camera ô tô":
                        imageView.setImageResource(R.drawable.ic_camera);
                        break;
                    case "Lốp dự phòng":
                        imageView.setImageResource(R.drawable.ic_spare_tire);
                        break;
                    case "Bluetooth":
                        imageView.setImageResource(R.drawable.ic_bluetooth);
                        break;
                    case "Màn hình DVD":
                        imageView.setImageResource(R.drawable.ic_dvd);
                        break;
                    case "Cửa sổ trời":
                        imageView.setImageResource(R.drawable.ic_sunroof);
                        break;
                    case "ETC":
                        imageView.setImageResource(R.drawable.ic_etc);
                        break;
                    case "Túi khí an toàn":
                        imageView.setImageResource(R.drawable.ic_air_bag);
                        break;
                    case "Camera lùi":
                        imageView.setImageResource(R.drawable.ic_camera_rear);
                        break;
                    case "Bản đồ":
                        imageView.setImageResource(R.drawable.ic_gps);
                        break;
                    case "Khe cắm USB":
                        imageView.setImageResource(R.drawable.ic_usb);
                        break;
                    case "Camera cặp lề":
                        imageView.setImageResource(R.drawable.ic_camera_side);
                        break;
                    case "Camera hành trình":
                        imageView.setImageResource(R.drawable.ic_camera_dash);
                        break;
                    case "Cảm biến lốp":
                        imageView.setImageResource(R.drawable.ic_tire_sensor);
                        break;
                    case "Cảm biến va chạm":
                        imageView.setImageResource(R.drawable.ic_collision_sensor);
                        break;
                    case "Ghế trẻ em":
                        imageView.setImageResource(R.drawable.ic_child_seat);
                        break;
                }

                linearLayout.addView(imageView);
                linearLayout.addView(textView);

                int columnCount = 3;
                int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
                int itemWidth = screenWidth / columnCount;

                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = itemWidth;
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;

                linearLayout.setLayoutParams(params);
                gridLayout.addView(linearLayout);
            }
        } else {
            gridLayout.setVisibility(View.GONE);
        }
    }
    private void addReviews(List<ReviewDTO> reviews) {
        LinearLayout reviewContainer = findViewById(R.id.reviewContainer);
        LayoutInflater inflater = LayoutInflater.from(this);
        reviewContainer.removeAllViews();

        for (ReviewDTO review : reviews) {
            View reviewView = inflater.inflate(R.layout.item_review, reviewContainer, false);

            TextView tvName = reviewView.findViewById(R.id.tvName);
            TextView tvDate = reviewView.findViewById(R.id.tvDate);
            TextView tvContent = reviewView.findViewById(R.id.tvContent);
            TextView tvStar = reviewView.findViewById(R.id.tvStarr); // nếu có ID riêng
            ImageView imgAvatar = reviewView.findViewById(R.id.imgAvatar);

            tvName.setText(review.getName());
            tvDate.setText(review.getCreatedAt());
            tvContent.setText(review.getComment());
            tvStar.setText("★ " + review.getRating());

            String imageUrl = review.getImageUrl();
            Glide.with(this)
                    .load(imageUrl != null && !imageUrl.isEmpty() ? imageUrl : R.drawable.avatar)
                    .circleCrop()
                    .into(imgAvatar);

            reviewContainer.addView(reviewView);
        }
    }

}
