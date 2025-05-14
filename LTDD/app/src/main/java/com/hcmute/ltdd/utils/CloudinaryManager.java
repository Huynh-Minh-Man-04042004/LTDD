package com.hcmute.ltdd.utils;

import com.cloudinary.Cloudinary;
import com.cloudinary.android.MediaManager;
import com.cloudinary.android.callback.ErrorInfo;
import com.cloudinary.android.callback.UploadCallback;
import com.hcmute.ltdd.R;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class CloudinaryManager {

    private static Cloudinary cloudinary;

    private static boolean isInitialized = false;

    public static void init(Context context) {
        if (!isInitialized) {
            Map<String, String> config = new HashMap<>();
            config.put("cloud_name", context.getString(R.string.cloud_name));
            config.put("api_key", context.getString(R.string.api_key));
            config.put("api_secret", context.getString(R.string.api_secret));

            try {
                MediaManager.init(context, config);
                isInitialized = true;
                Log.d("CloudinaryManager", "Cloudinary initialized successfully.");
            } catch (IllegalStateException e) {
                Log.w("CloudinaryManager", "Cloudinary is already initialized.");
            }
        }
    }

    public static void uploadImage(Context context, Uri fileUri, UploadCallback callback) {
        MediaManager.get().upload(fileUri)
                .unsigned("thuexeapplication")
                .callback(callback)
                .dispatch();
    }
}
