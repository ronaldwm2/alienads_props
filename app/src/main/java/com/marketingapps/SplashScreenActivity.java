package com.marketingapps;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.view.View;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.marketingapps.databinding.ActivitySplashScreenBinding;
import com.props.adsmanager.PropsAdsManagement;

public class SplashScreenActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
private ActivitySplashScreenBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

         binding = ActivitySplashScreenBinding.inflate(getLayoutInflater());
         setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        getSupportActionBar().hide();
        PropsAdsManagement.initializeAdmob(this);
        PropsAdsManagement.initializeAdsMapping(this);
        startNextActivity();

    }

    private void startNextActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(i);
            }
        }, 2000);
    }
}