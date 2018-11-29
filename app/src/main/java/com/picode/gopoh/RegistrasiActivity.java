package com.picode.gopoh;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.picode.gopoh.Control.ControlRegistrasi;
import com.picode.gopoh.View.IViewRegistrasi;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RegistrasiActivity extends AppCompatActivity implements IViewRegistrasi {

    @BindView(R.id.tietNama)
    TextInputEditText tietNama;
    @BindView(R.id.tietNoTelp)
    TextInputEditText tietNoTelp;
    @BindView(R.id.btnRegistrasi)
    CircularProgressButton btnRegistrasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrasi);
        ButterKnife.bind(this);

        ControlRegistrasi controlRegistrasi = new ControlRegistrasi(this);
        btnRegistrasi.setOnClickListener(v -> {
            btnRegistrasi.startAnimation();
            new Handler().postDelayed(() -> {
                controlRegistrasi.registrasi(tietNama.getText().toString(), tietNoTelp.getText().toString());
            }, 1000);
        });
    }

    @Override
    public void onRegistrasiSuccess(String message) {
        btnRegistrasi.revertAnimation(() -> {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        });
    }

    @Override
    public void onRegistrasiFailed(String message) {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Perhatian")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, (dialog, which) -> {
                    dialog.dismiss();
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
        btnRegistrasi.revertAnimation();
    }
}
