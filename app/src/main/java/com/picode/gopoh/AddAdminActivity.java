package com.picode.gopoh;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.firestore.GeoPoint;
import com.picode.gopoh.Control.ControlAddAdmin;
import com.picode.gopoh.View.IViewAddAdmin;

import java.util.HashMap;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddAdminActivity extends AppCompatActivity implements View.OnClickListener, IViewAddAdmin {
    public final static int CODE_ADD_PLACE = 123;
    @BindView(R.id.tietNamaWisata)
    TextInputEditText tietNamaWisata;
    @BindView(R.id.tietNamaAdmin)
    TextInputEditText tietNamaAdmin;
    @BindView(R.id.tietNoTelp)
    TextInputEditText tietNoTelp;
    @BindView(R.id.tietEmail)
    TextInputEditText tietEmail;
    @BindView(R.id.tietPassword)
    TextInputEditText tietPassword;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.btnAddPlace)
    ImageView btnAddPlace;
    @BindView(R.id.btnSubmit)
    CircularProgressButton btnSubmit;

    private GeoPoint location;
    private ControlAddAdmin controlAddAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_admin);
        ButterKnife.bind(this);
        controlAddAdmin = new ControlAddAdmin();

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Tambah Admin Tempat Wisata");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CODE_ADD_PLACE) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                location = new GeoPoint(
                        place.getLatLng().latitude,
                        place.getLatLng().longitude
                );
            }
        }
    }

    @OnClick({R.id.btnAddPlace, R.id.btnSubmit})
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAddPlace:
                try {
                    Intent i = new PlacePicker.IntentBuilder().build(this);
                    startActivityForResult(i, CODE_ADD_PLACE);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btnSubmit:
                btnSubmit.startAnimation();
                HashMap<String, String> input = new HashMap<>();
                input.put("email", tietEmail.getText().toString());
                input.put("namaAdmin", tietNamaAdmin.getText().toString());
                input.put("noTelp", tietNoTelp.getText().toString());
                input.put("password", tietPassword.getText().toString());
                input.put("namaWisata", tietNamaWisata.getText().toString());
                controlAddAdmin.addAdmin(input, location, this);
                break;
        }
    }

    @Override
    public void onAdminAddedSuccess() {
        location = null;
        btnSubmit.revertAnimation();
        Toast.makeText(this, "Admin wisata telah ditambahkan", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAdminAddesFailed(String message) {
        location = null;
        btnSubmit.revertAnimation();
        Toast.makeText(this, "Admin wisata gagal ditambahkan", Toast.LENGTH_LONG).show();
    }
}
