package com.hcmute.ltdd.ui;

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

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.hcmute.ltdd.R;
import com.hcmute.ltdd.data.remote.ApiService;
import com.hcmute.ltdd.data.remote.RetrofitClient;
import com.hcmute.ltdd.model.ApiResponse;
import com.hcmute.ltdd.model.request.EditProfileRequest;
import com.hcmute.ltdd.model.response.UserProfileResponse;
import com.hcmute.ltdd.utils.SharedPrefManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountActivity extends AppCompatActivity {

    private ImageView backButton;
    private TextView tvUsername, tvJoinDate;
    private ImageView iconBirth, iconGender, iconPhone, modifyButton, iconAddress;
    private TextView statusBirth, statusGender, statusPhone, statusAddress;

    private String valueBirth, valueGender, valuePhone, valueEmail, valueAddress;

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        backButton = findViewById(R.id.btn_back);
        backButton.setOnClickListener(v -> finish());

        tvUsername = findViewById(R.id.tv_username);
        tvJoinDate = findViewById(R.id.tv_join_date);

        iconBirth = findViewById(R.id.iconBirth);
        statusBirth = findViewById(R.id.statusBirth);

        iconGender = findViewById(R.id.icon_Gender);
        statusGender = findViewById(R.id.status_Gender);

        iconPhone = findViewById(R.id.iconPhone);
        statusPhone = findViewById(R.id.statusPhone);

        iconAddress = findViewById(R.id.iconAddress);
        statusAddress = findViewById(R.id.statusAddress);

        modifyButton = findViewById(R.id.modify);

        apiService = RetrofitClient.getRetrofit(this).create(ApiService.class);
        loadUserProfile();

        findViewById(R.id.account_Birth).setOnClickListener(v -> showDetailDialog(valueBirth, "Chưa liên kết ngày sinh"));
        findViewById(R.id.account_Gender).setOnClickListener(v -> showDetailDialog(valueGender, "Chưa liên kết giới tính"));
        findViewById(R.id.account_Phone).setOnClickListener(v -> showDetailDialog(valuePhone, "Chưa liên kết số điện thoại"));
        findViewById(R.id.account_Email).setOnClickListener(v -> showDetailDialog(valueEmail, "Email tạm thời bị lỗi"));
        findViewById(R.id.account_Address).setOnClickListener(v -> showDetailDialog(valueAddress, "Địa chi tạm thời bị lỗi"));
        findViewById(R.id.modify).setOnClickListener(v -> {showEditProfileDialog(tvUsername.getText().toString(), valuePhone, valueBirth, valueGender, valueAddress);});
    }

    private void loadUserProfile() {
        String token = "Bearer " + SharedPrefManager.getInstance(this).getToken();

        apiService.getUserProfile(token).enqueue(new Callback<ApiResponse<UserProfileResponse>>() {
            @Override

            public void onResponse(Call<ApiResponse<UserProfileResponse>> call, Response<ApiResponse<UserProfileResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {

                    ApiResponse<UserProfileResponse> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        UserProfileResponse user = apiResponse.getData();

                        tvUsername.setText(user.getName());
                        tvJoinDate.setText("Ngày tham gia: " + formatDate(user.getCreatedAt()));

                        valueBirth = user.getBirthdate();
                        valueGender = user.getGender();
                        valuePhone = user.getPhone();
                        valueEmail = user.getEmail();
                        valueAddress = user.getAddress();
                        updateVerification(iconBirth, statusBirth, valueBirth);
                        updateVerification(iconGender, statusGender, valueGender);
                        updateVerification(iconPhone, statusPhone, valuePhone);
                        updateVerification(iconAddress, statusAddress, valueAddress);

                    } else {
                        Toast.makeText(AccountActivity.this, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(AccountActivity.this, "Không tải được thông tin", Toast.LENGTH_SHORT).show();
                }
            }

            @Override

            public void onFailure(Call<ApiResponse<UserProfileResponse>> call, Throwable t) {
                Toast.makeText(AccountActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void updateVerification(ImageView iconView, TextView statusView, String fieldValue) {
        if (fieldValue != null && !fieldValue.trim().isEmpty()) {
            iconView.setImageResource(R.drawable.ic_verify);
            statusView.setText("Đã xác thực");
            statusView.setTextColor(getResources().getColor(R.color.green_700));
        } else {
            iconView.setImageResource(R.drawable.ic_warning);
            statusView.setText("Chưa xác thực");
            statusView.setTextColor(getResources().getColor(R.color.bright_orange));
        }
    }

    private String formatDate(String isoDateTime) {
        try {
            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS");
            Date date = isoFormat.parse(isoDateTime);
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "Không rõ";
        }
    }

    private void showDetailDialog(String value, String fallback) {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_detail_info, null);
        TextView tvInfo = view.findViewById(R.id.tv_info);
        tvInfo.setText((value != null && !value.isEmpty()) ? value : fallback);
        dialog.setContentView(view);
        dialog.show();
    }

    private void showEditProfileDialog(String name, String phone, String birthdate, String gender, String address) {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_edit_profile, null);

        EditText edtName = view.findViewById(R.id.edt_name);
        EditText edtPhone = view.findViewById(R.id.edt_phone);
        EditText edtBirthdate = view.findViewById(R.id.edt_birthdate);
        EditText edtGender = view.findViewById(R.id.edt_gender);
        EditText edtAddress = view.findViewById(R.id.edt_address);
        Button btnSave = view.findViewById(R.id.btn_save);

        edtName.setText(name);
        edtPhone.setText(phone);
        edtBirthdate.setText(birthdate);  // Dữ liệu dạng "yyyy-MM-dd"
        edtGender.setText(gender);
        edtAddress.setText(address);

        btnSave.setOnClickListener(v -> {
            String newName = edtName.getText().toString().trim();
            String newPhone = edtPhone.getText().toString().trim();
            String newBirthdate = edtBirthdate.getText().toString().trim();
            String newGender = edtGender.getText().toString().trim();
            String newAddress = edtAddress.getText().toString().trim();

            if (newName.isEmpty() || newPhone.isEmpty() || newBirthdate.isEmpty() || newAddress.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newBirthdate.matches("\\d{4}-\\d{2}-\\d{2}")) {
                Toast.makeText(this, "Ngày sinh phải có định dạng yyyy-MM-dd", Toast.LENGTH_SHORT).show();
                return;
            }

            EditProfileRequest request = new EditProfileRequest(newName, newPhone, newBirthdate, newGender, newAddress);
            updateProfile(request);
            dialog.dismiss();
        });

        dialog.setContentView(view);
        dialog.show();
    }

    private LocalDate convertStringToLocalDate(String dateStr) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse(dateStr);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // Tháng bắt đầu từ 0
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return LocalDate.of(year, month, day);
    }

    private void updateProfile(EditProfileRequest request) {
        String token = "Bearer " + SharedPrefManager.getInstance(this).getToken();
        Gson gson = new Gson();
        String jsonRequest = gson.toJson(request);
        Log.d("JSON Request", jsonRequest);

        apiService.editProfile(request, token).enqueue(new Callback<ApiResponse<String>>() {
            @Override
            public void onResponse(Call<ApiResponse<String>> call, Response<ApiResponse<String>> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AccountActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    loadUserProfile(); // Refresh dữ liệu sau khi cập nhật
                } else {
                    Toast.makeText(AccountActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<String>> call, Throwable t) {
                Toast.makeText(AccountActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
