package com.picode.gopoh.Model;

import com.google.firebase.firestore.FirebaseFirestore;
import com.picode.gopoh.View.IViewLogin;
import com.pixplicity.easyprefs.library.Prefs;

public class ModelLogin {

    public void saveDataInLocal(String id, String email, String nama, String noTelp) {
        Prefs.putBoolean("isFirstTime", false);
        Prefs.putString("id", id);
        Prefs.putString("email", email);
        Prefs.putString("nama", nama);
        Prefs.putString("noTelp", noTelp);
    }

    public void cekLogin(String email, String password, IViewLogin listener) {
        FirebaseFirestore.getInstance().collection("admin")
                .whereEqualTo("email", email)
                .whereEqualTo("password", password)
                .limit(1)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots != null) {
                        if (queryDocumentSnapshots.size() != 0) {
                            saveDataInLocal(
                                    queryDocumentSnapshots.getDocuments().get(0).getId(),
                                    queryDocumentSnapshots.getDocuments().get(0).getString("email"),
                                    queryDocumentSnapshots.getDocuments().get(0).getString("name"),
                                    queryDocumentSnapshots.getDocuments().get(0).getString("noTelp")
                            );
                            listener.onLoginResult(true, "Login berhasil");
                        } else
                            listener.onLoginResult(false, "Email atau password salah");
                    } else
                        listener.onLoginResult(false, "Terjadi kesalahan");
                });
    }

    public void logout() {
        Prefs.clear();
    }

    public boolean idAlreadyLogin() {
        return Prefs.contains("email");
    }

}