package com.picode.gopoh;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;

import com.picode.gopoh.Control.ControlLogin;
import com.picode.gopoh.View.IViewLogin;
import com.tapadoo.alerter.Alerter;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements IViewLogin {

    @BindView(R.id.tietEmail)
    TextInputEditText tietEmail;
    @BindView(R.id.tietPassword)
    TextInputEditText tietPassword;

    @BindView(R.id.btnLogin)
    CircularProgressButton btnLogin;

    private boolean loginAsSuperAdmin = false;

    private ControlLogin controlLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        controlLogin = new ControlLogin(this, this);

        if (getIntent() != null) {
            loginAsSuperAdmin = getIntent().getBooleanExtra("loginAsSuperAdmin", false);
        }

        btnLogin.setOnClickListener(v -> {
            btnLogin.startAnimation();
            if (!loginAsSuperAdmin) {
                // login as Admin
                controlLogin.loginAsAdmin(tietEmail.getText().toString(), tietPassword.getText().toString());
            } else {
                controlLogin.loginAsSuperAdmin(tietEmail.getText().toString(), tietPassword.getText().toString());
            }
        });


    }

    @Override
    public void onLoginResult(boolean sukses, String message) {
        if (sukses) {
            Alerter.create(this).setTitle("Sukses")
                    .setText(message + ". Tunggu beberapa saat, Anda akan dialihkan dalam 3 detik")
                    .setDuration(3000)
                    .setBackgroundColorInt(getResources().getColor(R.color.colorPrimaryDark))
                    .setOnHideListener(() -> controlLogin.openAdminPanel())
                    .show();
        } else {
            Alerter.create(this).setTitle("Gagal")
                    .setText(message)
                    .setDuration(3000)
                    .enableVibration(true)
                    .setBackgroundColorInt(Color.RED)
                    .show();
        }
        btnLogin.revertAnimation();
    }
}
