package com.hcmute.ltdd.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.hcmute.ltdd.R;
import com.hcmute.ltdd.viewmodel.BookingViewModel;

public class ReviewFragment extends DialogFragment {

    private RatingBar ratingBar;
    private EditText edtFeedback;
    private Button btnSubmit;
    private BookingViewModel bookingViewModel;
    private int carId;
    private long bookingId;
    private ReviewCallback callback;

    public interface ReviewCallback {
        void onReviewSubmitted();
    }

    public ReviewFragment(int carId, long bookingId, ReviewCallback callback) {
        this.carId = carId;
        this.bookingId = bookingId;
        this.callback = callback;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rating, container, false);
        ratingBar = view.findViewById(R.id.ratingBar);
        edtFeedback = view.findViewById(R.id.edtFeedback);
        btnSubmit = view.findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(v -> {
            int rating = (int) ratingBar.getRating();
            String feedback = edtFeedback.getText().toString().trim();

            if (rating == 0) {
                Toast.makeText(getContext(), "Vui lòng chọn đánh giá", Toast.LENGTH_SHORT).show();
                return;
            }

            bookingViewModel.submitReview(getContext(), carId, bookingId, rating, feedback);
            dismiss();
            if (callback != null) {
                callback.onReviewSubmitted();  // Gọi callback để load lại dữ liệu
            }
        });

        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        bookingViewModel = new BookingViewModel();
    }
}
