package com.picode.gopoh;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.afollestad.materialdialogs.MaterialDialog;
import com.picode.gopoh.Control.ControlLokasi;
import com.picode.gopoh.Model.ModelTempatWisata;
import com.picode.gopoh.View.IViewLocation;
import com.tbruyelle.rxpermissions2.RxPermissions;

import br.com.simplepass.loading_button_lib.customViews.CircularProgressButton;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements IViewLocation {

    @BindView(R.id.btnPanik)
    CircularProgressButton btnPanic;

    @BindView(R.id.btnLoginAdmin)
    Button btnLoginAdmin;
    @BindView(R.id.btnLoginSuperAdmin)
    Button btnLoginSuperAdmin;

    ControlLokasi controlLokasi;
    MaterialDialog dialogLoading;
    boolean isShowDialog = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        controlLokasi = new ControlLokasi(this, this);

        btnPanic.setOnClickListener(v -> {
            new RxPermissions(this)
                    .request(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                    .subscribe(granted -> {
                        if (granted) {
                            btnPanic.startAnimation();
                            btnPanic.setEnabled(false);
                            controlLokasi.deteksiLokasi(null);
                        } else {
                            new MaterialDialog.Builder(this)
                                    .title("Butuh izin lokasi Anda")
                                    .content("Aplikasi membutuhkan izin untuk mengakses lokasi Anda")
                                    .positiveText("Buka pengaturan aplikasi")
                                    .onPositive((dialog1, which1) -> {
                                        Intent intent = new Intent();
                                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                                        intent.setData(uri);
                                        startActivity(intent);
                                    }).show();
                        }
                    });
        });

        btnLoginAdmin.setOnClickListener(v -> {
            startActivity(new Intent(this, LoginActivity.class));
        });
        btnLoginSuperAdmin.setOnClickListener(v -> {
            Intent i = new Intent(this, LoginActivity.class);
            i.putExtra("loginAsSuperAdmin", true);
            startActivity(i);
        });
    }

    @Override
    synchronized public void onLokasiTerdeteksi(ModelTempatWisata wisata, boolean sukses) {
        if (wisata != null && sukses && !isShowDialog) {
            isShowDialog = true;
            new MaterialDialog.Builder(this)
                    .title("Konfirmasi")
                    .content("Apakah Anda berada di " + wisata.getNama() + " ?")
                    .positiveText("YA")
                    .negativeText("TIDAK")
                    .onPositive((dialog, which) -> {
                        btnPanic.revertAnimation();
                        showLoading();
                        controlLokasi.makeChatRoom(wisata, (roomId) -> {
                            if (dialogLoading.isShowing())
                                dialogLoading.dismiss();
                            controlLokasi.openChattingActivity(roomId, wisata.getNama());
                        });
                    })
                    .onNegative((dialog, which) -> {
                        btnPanic.setEnabled(false);
                        btnPanic.revertAnimation();
                        showNotFound();
                    })
                    .dismissListener(dialog -> {
                        isShowDialog = false;
                        btnPanic.setEnabled(true);
                    })
                    .show();
        } else {
            btnPanic.revertAnimation();
            isShowDialog = true;
            new MaterialDialog.Builder(this)
                    .title("Perhatian")
                    .content("Terjadi error, silahkan coba beberapa saat lagi")
                    .positiveText("OK")
                    .onPositive(((dialog, which) -> dialog.dismiss()))
                    .dismissListener(dialog -> {
                        btnPanic.setEnabled(true);
                        isShowDialog = false;
                    })
                    .show();
        }

        btnPanic.setEnabled(true);
    }

    synchronized private void showNotFound() {
        isShowDialog = true;
        new MaterialDialog.Builder(this)
                .title("Perhatian")
                .content("Tidak dapat menemukan lokasi yang sesuai")
                .positiveText("OK")
                .onPositive(((dialog, which) -> dialog.dismiss()))
                .dismissListener(dialog -> {
                    btnPanic.setEnabled(true);
                    isShowDialog = false;
                })
                .show();
    }

    private void showLoading() {
        dialogLoading = new MaterialDialog.Builder(this)
                .title("Loading")
                .content("Silahkan tunggu sebentar")
                .progress(true, 100)
                .progressIndeterminateStyle(true)
                .dismissListener(dialog -> {
                    btnPanic.setEnabled(true);
                    isShowDialog = false;
                })
                .show();
    }
}
