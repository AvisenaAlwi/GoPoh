package com.picode.gopoh;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.pixplicity.easyprefs.library.Prefs;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startService(new Intent(this, AnnouncementServices.class));
        if (isFirstTime()) {
            new Handler().postDelayed(() -> {
                startActivity(new Intent(this, RegistrasiActivity.class));
                finish();
            }, 1000);
        } else {
            if (Prefs.contains("email") && Prefs.contains("nama"))
                new Handler().postDelayed(() -> {
                    startActivity(new Intent(this, AdminPanelActivity.class));
                    finish();
                }, 1000);
            else
                new Handler().postDelayed(() -> {
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                }, 1000);
        }
    }

    private boolean isFirstTime() {
        return Prefs.getBoolean("isFirstTime", true);
    }
}
