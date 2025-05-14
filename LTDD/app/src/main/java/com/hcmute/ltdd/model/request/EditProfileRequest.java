package com.hcmute.ltdd.model.request;

public class EditProfileRequest {
    private String name;
    private String phone;
    private String birthdate;  // Sử dụng String thay vì Object
    private String gender;
    private String address;   // Thêm trường address

    public EditProfileRequest(String name, String phone, String birthdate, String gender, String address) {
        this.name = name;
        this.phone = phone;
        this.birthdate = birthdate;  // birthdate phải là định dạng "yyyy-MM-dd"
        this.gender = gender;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public String getGender() {
        return gender;
    }

    public String getAddress() {
        return address;
    }
}
