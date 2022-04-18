package com.app.bookstylist;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.app.bookstylist.databinding.ActivityShopBinding;

public class ShopActivity extends AppCompatActivity {
    private ActivityShopBinding binding;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShopBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent get = getIntent();

        String a = get.getStringExtra("name");
        binding.shop.setText(a);
    }
}
