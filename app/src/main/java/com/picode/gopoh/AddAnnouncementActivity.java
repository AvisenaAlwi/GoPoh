package com.picode.gopoh;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.picode.gopoh.Control.ControlAnnouncement;
import com.picode.gopoh.Interface.OnAnnouncementSuccess;
import com.tapadoo.alerter.Alerter;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import butterknife.BindView;
import butterknife.ButterKnife;

public class AddAnnouncementActivity extends AppCompatActivity implements OnAnnouncementSuccess {

    ControlAnnouncement controlAnnouncement;

    @BindView(R.id.tietAnnouncement)
    TextInputEditText tietAnnouncement;
    @BindView(R.id.btnAnnounce)
    CircularProgressButton btnAnnounce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_announcement);
        ButterKnife.bind(this);

        controlAnnouncement = new ControlAnnouncement();

        btnAnnounce.setOnClickListener(view -> {
            if (TextUtils.isEmpty(tietAnnouncement.getText().toString()))
                showError("Form tidak boleh kosong");
            else {
                btnAnnounce.startAnimation();
                controlAnnouncement.announce(tietAnnouncement.getText().toString(), this);
            }
        });
    }

    private void showError(String message) {
        Alerter.create(this)
                .setTitle("Error")
                .setText(message)
                .setBackgroundColorInt(Color.RED)
                .setDuration(3000)
                .show();
    }

    @Override
    public void onAnnouncementSuccess() {
        btnAnnounce.revertAnimation();
        Alerter.create(this)
                .setTitle("Sukses")
                .setText(tietAnnouncement.getText().toString())
                .setDuration(3000)
                .setBackgroundColorRes(R.color.colorPrimary)
                .show();
        tietAnnouncement.setText("");
    }
}
