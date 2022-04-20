package com.app.bookstylist;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
        String b = get.getStringExtra("id");

        Intent intent = new Intent(ShopActivity.this,BookActivity.class);
        intent.putExtra("name", a);
        intent.putExtra("id", b);
        intent.putExtra("service", get.getStringExtra("service"));
        intent.putExtra("address", get.getStringExtra("address"));
        intent.putExtra("img",get.getStringExtra("img"));

        binding.btnBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });
        binding.shop.setText(a);
        binding.idShop.setText(b);
    }
}
