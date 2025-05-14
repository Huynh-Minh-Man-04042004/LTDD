package com.hcmute.ltdd.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.hcmute.ltdd.R;
import com.hcmute.ltdd.model.response.BookingHistoryResponse;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class BookingHistoryAdapter extends RecyclerView.Adapter<BookingHistoryAdapter.BookingViewHolder> {

    private List<BookingHistoryResponse> bookingHistoryList = new ArrayList<>();

    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(Long bookingId);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void setBookingHistoryList(List<BookingHistoryResponse> list) {
        if (list != null) {
            bookingHistoryList = list;
            Collections.reverse(bookingHistoryList);
        } else {
            bookingHistoryList = new ArrayList<>();
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trip, parent, false);
        return new BookingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingViewHolder holder, int position) {
        BookingHistoryResponse booking = bookingHistoryList.get(position);
        holder.bindData(booking);
        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(booking.getBookingId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookingHistoryList.size();
    }

    public void clearData() {
        bookingHistoryList.clear();
        notifyDataSetChanged();
    }

    static class BookingViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imgCar;
        private final TextView tvCarName;
        private final TextView tvPrice;
        private final TextView tvTotalDays;
        private final TextView tvOrderTime;

        public BookingViewHolder(@NonNull View itemView) {
            super(itemView);
            imgCar = itemView.findViewById(R.id.imgCar_bookingItem);
            tvCarName = itemView.findViewById(R.id.tvCarName_bookingitem);
            tvPrice = itemView.findViewById(R.id.tvPrice_bookingitem);
            tvTotalDays = itemView.findViewById(R.id.tvTotalDays_bookingitem);
            tvOrderTime = itemView.findViewById(R.id.tvOrderTime_bookingitem);
        }

        public void bindData(BookingHistoryResponse booking) {
            tvCarName.setText(booking.getCarName());
            tvPrice.setText(String.format("%.0f đ", booking.getTotalPrice()));
            String orderTime = formatOrderTime(booking.getStartDate());
            tvOrderTime.setText(orderTime);
            String totalDays = calculateTotalDays(booking.getStartDate(), booking.getEndDate());
            tvTotalDays.setText(totalDays);
            if (booking.getCarImageUrl() != null && !booking.getCarImageUrl().isEmpty()) {
                Glide.with(itemView.getContext())
                        .load(booking.getCarImageUrl())
                        .placeholder(R.drawable.ic_car_placeholder)
                        .error(R.drawable.ic_car_placeholder)
                        .into(imgCar);
            } else {
                imgCar.setImageResource(R.drawable.ic_car_placeholder);
            }
        }
        private String calculateTotalDays(String startDate, String endDate) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            try {
                LocalDateTime start = LocalDateTime.parse(startDate, formatter);
                LocalDateTime end = LocalDateTime.parse(endDate, formatter);
                Duration duration = Duration.between(start, end);
                // Số ngày (bao gồm cả phần thập phân)
                double days = duration.toHours() / 24.0;
                if (days % 1 == 0) {
                    return String.format(Locale.getDefault(), "%.0f ngày", days);
                } else {
                    return String.format(Locale.getDefault(), "%.1f ngày", days);
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "Không xác định";
            }
        }
        private String formatOrderTime(String startDate) {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("HH:mm, d 'thg' M yyyy", Locale.getDefault());

            try {
                LocalDateTime start = LocalDateTime.parse(startDate, inputFormatter);
                return start.format(outputFormatter);
            } catch (Exception e) {
                e.printStackTrace();
                return "Không xác định";
            }
        }
    }
}