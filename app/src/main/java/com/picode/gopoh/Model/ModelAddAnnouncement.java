package com.picode.gopoh.Model;

import android.location.Location;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.picode.gopoh.Interface.OnAnnouncementSuccess;
import com.picode.gopoh.Interface.OnGetModelTempatWisata;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.Date;
import java.util.HashMap;

public class ModelAddAnnouncement {

    public void announce(String message, ModelTempatWisata wisata, OnAnnouncementSuccess listener) {
        GeoPoint geoPoint = new GeoPoint(wisata.getLokasi().getLatitude(), wisata.getLokasi().getLongitude());
        HashMap<String, Object> data = new HashMap<>();
        data.put("idAdmin", Prefs.getString("id", "unknow"));
        data.put("namaWisata", wisata.getNama());
        data.put("location", geoPoint);
        data.put("radius", 1000);
        data.put("message", message);
        data.put("time", new Date());
        FirebaseFirestore.getInstance().document("announcements/" + System.currentTimeMillis())
                .set(data)
                .addOnSuccessListener(aVoid -> {
                    listener.onAnnouncementSuccess();
                });
    }

    public void getModelTempatWisata(String idAdmin, OnGetModelTempatWisata listener) {
        FirebaseFirestore.getInstance()
                .collection("wisata")
                .whereEqualTo("idAdmin", idAdmin)
                .limit(1)
                .get()
                .addOnSuccessListener(q -> {
                    if (q != null && q.size() != 0) {
                        Location location = new Location("");
                        location.setLatitude(q.getDocuments().get(0).getGeoPoint("lokasi").getLatitude());
                        location.setLongitude(q.getDocuments().get(0).getGeoPoint("lokasi").getLongitude());
                        ModelTempatWisata wisata = new ModelTempatWisata(
                                q.getDocuments().get(0).getId(),
                                q.getDocuments().get(0).getString("name"),
                                idAdmin,
                                location
                        );
                        listener.OnGetModelTempatWisata(wisata);
                    }
                });
    }
}
