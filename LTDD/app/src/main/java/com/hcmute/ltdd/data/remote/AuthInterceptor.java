package com.hcmute.ltdd.data.remote;

import android.content.Context;
import android.util.Log;

import com.hcmute.ltdd.utils.SharedPrefManager;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {
    private final Context context;

    public AuthInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        String token = SharedPrefManager.getInstance(context).getToken();
        Request originalRequest = chain.request();
        Request.Builder builder = originalRequest.newBuilder();

        // Danh sách các endpoint KHÔNG CẦN token
        String[] noAuthEndpoints = {"/login", "/register", "/forgot-password", "/verify-otp", "/reset-password"};

        // Kiểm tra URL hiện tại
        String urlPath = originalRequest.url().encodedPath();
        Log.d("AuthInterceptor", "Request URL: " + originalRequest.url());

        // Nếu URL nằm trong danh sách không cần token, bỏ qua
        for (String endpoint : noAuthEndpoints) {
            if (urlPath.endsWith(endpoint)) {
                Log.d("AuthInterceptor", "No token required for: " + endpoint);
                return chain.proceed(originalRequest);
            }
        }

        // Nếu không thuộc endpoint miễn token, gắn token vào header
        if (token != null && !token.isEmpty()) {
            builder.addHeader("Authorization", "Bearer " + token);
        }

        Request newRequest = builder.build();
        return chain.proceed(newRequest);
    }

}
