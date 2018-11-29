package com.picode.gopoh.Model;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.picode.gopoh.View.IViewAddAdmin;

import java.util.HashMap;

public class ModelAddAdmin {

    public void addAdmin(HashMap<String, Object> dataAdmin, HashMap<String, Object> dataWisata, IViewAddAdmin listener){
        String idAdmin = dataWisata.get("idAdmin").toString();
        String idWisata = dataAdmin.get("idWisata").toString();
        FirebaseFirestore.getInstance()
                .document("admin/"+idAdmin)
                .set(dataAdmin)
                .addOnSuccessListener(aVoid -> {
                    FirebaseFirestore.getInstance()
                            .document("wisata/"+idWisata)
                            .set(dataWisata)
                            .addOnSuccessListener(aVoid1 -> {
                                listener.onAdminAddedSuccess();
                            })
                            .addOnFailureListener(e -> {
                                FirebaseFirestore.getInstance()
                                        .document("admin/"+idAdmin)
                                        .delete();
                                listener.onAdminAddesFailed("Terjadi kesalahan koneksi");
                            });
                }).addOnFailureListener(e -> {
                    listener.onAdminAddesFailed("Terjadi kesalahan koneksi");
                });
    }

}
