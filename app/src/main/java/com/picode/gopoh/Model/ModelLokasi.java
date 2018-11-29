package com.picode.gopoh.Model;

import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.picode.gopoh.Interface.OnGetTempatWisataListener;
import com.picode.gopoh.Interface.OnMakeChatRoomSuccess;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.HashMap;

import io.nlopez.smartlocation.SmartLocation;
import io.nlopez.smartlocation.location.config.LocationParams;
import io.nlopez.smartlocation.location.providers.LocationGooglePlayServicesProvider;

public class ModelLokasi {

    private Context context;

    public ModelLokasi(Context context) {
        this.context = context;
    }

    public void getTempatWisata(ArrayList<ModelTempatWisata> dataWisata, int radius, OnGetTempatWisataListener listener) {
        Location locationz;
        if (!SmartLocation.with(context).location().state().isAnyProviderAvailable()) {
            if ((locationz = SmartLocation.with(context).location().getLastLocation()) != null) {
                boolean ketemu = false;
                for (ModelTempatWisata wisata : dataWisata) {
                    if (wisata.isLokasiIn(locationz, radius)) {
                        ketemu = true;
                        listener.onGetTempatWisataListener(wisata);
                        break;
                    }
                }
                if (!ketemu)
                    listener.onZeroTempatWisata();
                return;
            }
        }else
            SmartLocation.with(context)
                    .location(new LocationGooglePlayServicesProvider())
                    .oneFix()
                    .config(LocationParams.BEST_EFFORT)
                    .start(location -> {
                        boolean ketemu = false;
                        for (ModelTempatWisata wisata : dataWisata) {
                            if (location != null) {
                                if (wisata.isLokasiIn(location, radius)) {
                                    ketemu = true;
                                    listener.onGetTempatWisataListener(wisata);
                                    break;
                                }
                            }
                        }
                        if (!ketemu)
                            listener.onZeroTempatWisata();
                    });
    }

    public void makeChatRoom(ModelTempatWisata wisata, OnMakeChatRoomSuccess listener) {
        String roomId = "" + System.currentTimeMillis();
        FirebaseFirestore.getInstance().collection("rooms")
                .whereEqualTo("idUser1", Prefs.getString("id", "unknow"))
                .whereEqualTo("idUser2", wisata.getIdAdmin())
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        listener.OnMakeChatRoomSuccess(
                                queryDocumentSnapshots.getDocuments().get(0).getId()
                        );
                    } else {
                        FirebaseFirestore.getInstance()
                                .document("admin/" + wisata.getIdAdmin())
                                .get()
                                .addOnSuccessListener(documentSnapshot -> {

                                    HashMap<String, Object> room = new HashMap<>();
                                    room.put("idUser1", Prefs.getString("id", "unknow"));
                                    room.put("idUser2", documentSnapshot.getId());
                                    room.put("nameUser1", Prefs.getString("nama", "unknow"));
                                    room.put("nameUser2", documentSnapshot.getString("name"));
                                    room.put("telponUser1", Prefs.getString("noTelp", "0"));
                                    room.put("telponUser2", documentSnapshot.getString("noTelp"));

                                    FirebaseFirestore.getInstance()
                                            .document("rooms/" + roomId)
                                            .set(room)
                                            .addOnSuccessListener(aVoid -> {
                                                ModelChatting modelChatting = new ModelChatting(context, roomId);
                                                modelChatting.sendMessage("Hallo Admin");
                                                listener.OnMakeChatRoomSuccess(roomId);
                                            });
                                });
                    }
                });
    }
}
