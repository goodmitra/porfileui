package com.namdev.namdevvivah;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

public class ProfileActivity extends AppCompatActivity {
    ImageView profileImage,backButton;
    TextView usernameTextView , emailTextView ;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        // Assuming you have a User object
        User user = getUser();
        // Populate views with user information
        profileImage = findViewById(R.id.profileImage);
        usernameTextView = findViewById(R.id.userName);
        emailTextView = findViewById(R.id.mobile);
        backButton = findViewById(R.id.back_btn);
        backButton.setOnClickListener(v -> {
            onBackPressed();
        });
        // Set user information to views
        usernameTextView.setText(user.getUsername());
        emailTextView.setText("9001480042");
        String imageUrl = "https://www.namdevvivah.com/namdevuser/userimg/userphoto.png";
        Glide.with(getApplicationContext())
                .load(imageUrl)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)) // Cache image
                .placeholder(R.drawable.ic_user) // Placeholder image while loading
                .error(R.drawable.ic_error)
                .into(profileImage);
    }
    // Dummy method to get user information
    private User getUser() {
        return new User("John Doe", "john@example.com", "profile_image_url");
    }

}