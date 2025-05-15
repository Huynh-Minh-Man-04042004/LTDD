package com.hcmute.ltdd.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.hcmute.ltdd.databinding.FragmentFilterBottomSheetBinding;
import com.hcmute.ltdd.model.request.SearchCarRequest;

public class FilterBottomSheet extends BottomSheetDialogFragment {

    private FragmentFilterBottomSheetBinding binding;
    private FilterListener filterListener;
    private String location;
    private Boolean driverRequired;

    public interface FilterListener {
        void onFilterApplied(SearchCarRequest request);
    }

    public void setFilterListener(FilterListener listener) {
        this.filterListener = listener;
    }

    public void setInitialValues(String location, Boolean driverRequired) {
        this.location = location;
        this.driverRequired = driverRequired;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentFilterBottomSheetBinding.inflate(inflater, container, false);
        setupSpinners();

        binding.btnApply.setOnClickListener(v -> {
            String brand = getSelectedSafe(binding.spinnerBrand.getSelectedItem());
            Integer seats = parseIntSafe(getSelectedSafe(binding.spinnerSeats.getSelectedItem()));

            String gearTypeVi = getSelectedSafe(binding.spinnerGearType.getSelectedItem());
            String fuelTypeVi = getSelectedSafe(binding.spinnerFuelType.getSelectedItem());
            String gearType = convertGearTypeToEnglish(gearTypeVi);
            String fuelType = convertFuelTypeToEnglish(fuelTypeVi);

            Double priceFrom = parseDoubleSafe(toStringSafe(binding.etPriceFrom.getText()));
            Double priceTo = parseDoubleSafe(toStringSafe(binding.etPriceTo.getText()));

            SearchCarRequest request = new SearchCarRequest(
                    location,
                    seats,
                    brand,
                    priceFrom,
                    priceTo,
                    gearType,
                    fuelType,
                    driverRequired
            );

            if (filterListener != null) {
                filterListener.onFilterApplied(request);
            }
            dismiss();
        });

        return binding.getRoot();
    }

    private Integer parseIntSafe(String s) {
        try { return Integer.parseInt(s); } catch (Exception e) { return null; }
    }

    private Double parseDoubleSafe(String s) {
        try { return Double.parseDouble(s); } catch (Exception e) { return null; }
    }

    private String toStringSafe(Object o) {
        if (o == null) return null;
        String s = o.toString().trim();
        return s.isEmpty() ? null : s;
    }

    private String getSelectedSafe(Object selectedItem) {
        if (selectedItem == null) return null;
        String value = selectedItem.toString().trim();
        return value.isEmpty() || value.startsWith("Chọn") ? null : value;
    }

    private void setupSpinners() {
        String[] brandOptions = {"Chọn hãng xe", "Toyota", "Honda", "Ford", "Hyundai", "Kia", "Mazda", "Mercedes-Benz", "BMW", "VinFast"};
        ArrayAdapter<String> brandAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, brandOptions);
        brandAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerBrand.setAdapter(brandAdapter);

        String[] seatOptions = {"Chọn số chỗ", "2", "4", "5", "7"};
        ArrayAdapter<String> seatAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, seatOptions);
        seatAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerSeats.setAdapter(seatAdapter);

        String[] gearOptions = {"Chọn hộp số", "Số sàn", "Số tự động"};
        ArrayAdapter<String> gearAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, gearOptions);
        gearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerGearType.setAdapter(gearAdapter);

        String[] fuelOptions = {"Chọn nhiên liệu", "Xăng", "Dầu Diesel", "Điện"};
        ArrayAdapter<String> fuelAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, fuelOptions);
        fuelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerFuelType.setAdapter(fuelAdapter);
    }
    private String convertGearTypeToEnglish(String vietnamese) {
        if (vietnamese == null) return null;
        switch (vietnamese) {
            case "Số sàn": return "Manual";
            case "Số tự động": return "Automatic";
            default: return null;
        }
    }

    private String convertFuelTypeToEnglish(String vietnamese) {
        if (vietnamese == null) return null;
        switch (vietnamese) {
            case "Xăng": return "Gasoline";
            case "Dầu Diesel": return "Diesel";
            case "Điện": return "Electric";
            default: return null;
        }
    }

}
