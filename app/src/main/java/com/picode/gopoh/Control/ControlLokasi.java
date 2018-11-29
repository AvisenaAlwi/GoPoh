package com.picode.gopoh.Control;

import android.content.Context;
import android.content.Intent;
import android.location.Location;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.picode.gopoh.ChattingActivity;
import com.picode.gopoh.Interface.OnGetTempatWisataListener;
import com.picode.gopoh.Interface.OnMakeChatRoomSuccess;
import com.picode.gopoh.Model.ModelLokasi;
import com.picode.gopoh.Model.ModelTempatWisata;
import com.picode.gopoh.View.IViewLocation;

import java.util.ArrayList;

public class ControlLokasi implements OnGetTempatWisataListener {
    public final int MAX_RADIUS = 2000; // 2Km
    private Context context;
    private IViewLocation viewLocation;
    private ModelLokasi modelLokasi;
    private ArrayList<ModelTempatWisata> dataWisata = new ArrayList<>();
    private int radius = 500;


    public ControlLokasi(Context context, IViewLocation viewLocation) {
        this.context = context;
        this.viewLocation = viewLocation;
        modelLokasi = new ModelLokasi(context);
        FirebaseFirestore.getInstance().collection("wisata")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots == null)
                        return;
                    for (DocumentSnapshot item : queryDocumentSnapshots) {
                        GeoPoint geoPoint = item.getGeoPoint("lokasi");
                        Location lokasi = new Location("");
                        lokasi.setLatitude(geoPoint.getLatitude());
                        lokasi.setLongitude(geoPoint.getLongitude());
                        dataWisata.add(
                                new ModelTempatWisata(
                                        item.getId(),
                                        item.getString("name"),
                                        item.getString("idAdmin"),
                                        lokasi
                                )
                        );
                    }
                });
    }

    public void deteksiLokasi(ArrayList<ModelTempatWisata> dataWisata) {
        if (this.dataWisata.size() == 0) {
            viewLocation.onLokasiTerdeteksi(null, false);
            return;
        }
        if (radius <= MAX_RADIUS)
            modelLokasi.getTempatWisata(this.dataWisata, radius, this);
        else
            resetRadiusLevel();
    }

    public void increaseRadiusLevel() {
        this.radius *= 1.5f;
    }

    public void resetRadiusLevel() {
        this.radius = 500;
    }

    public void openChattingActivity(String roomId, String namaWisata) {
        Intent intent = new Intent(context, ChattingActivity.class);
        intent.putExtra("roomId", roomId);
        intent.putExtra("namaWisata", namaWisata);
        context.startActivity(intent);
    }

    public void makeChatRoom(ModelTempatWisata wisata, OnMakeChatRoomSuccess listener) {
        modelLokasi.makeChatRoom(wisata, listener);
    }

    @Override
    public void onGetTempatWisataListener(ModelTempatWisata wisata) {
        viewLocation.onLokasiTerdeteksi(wisata, true);
    }

    @Override
    public void onZeroTempatWisata() {
        increaseRadiusLevel();
        deteksiLokasi(null);
    }

}