package com.app.bookstylist;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.app.bookstylist.databinding.ActivityBookBinding;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.util.Date;

public class BookActivity extends AppCompatActivity {
    private Date date_1;
    private String date, time;
    private ActivityBookBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
        builder.setTitleText("Chọn Ngày");
        builder.setTheme(R.style.datepicker);

        final MaterialDatePicker<Long> materialDatePicker = builder.build();
        binding.btnDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                materialDatePicker.show(getSupportFragmentManager(),"Date Pick");
            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                binding.txtday.setText(materialDatePicker.getHeaderText());
                date_1 = new Date(materialDatePicker.getSelection());
                
            }
        });


    }

}

