package com.example.project1;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project1.databinding.ActivityUserBinding;

public class UserActivity extends AppCompatActivity {

    ActivityUserBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = this.getIntent();

        if (intent != null){

            String restName = intent.getStringExtra("restName");
            String menu = intent.getStringExtra("menu");
            String price = intent.getStringExtra("price");
            int imageid = intent.getIntExtra("imageid",R.drawable.a);

            binding.nameProfile.setText(restName);
            binding.menuProfile.setText(menu);
            binding.priceProfile.setText(price);
            binding.profileImage.setImageResource(imageid);


        }

    }
}