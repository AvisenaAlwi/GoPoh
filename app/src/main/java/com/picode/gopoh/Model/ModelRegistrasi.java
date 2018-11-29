package com.picode.gopoh.Model;

import android.text.TextUtils;

import com.pixplicity.easyprefs.library.Prefs;

public class ModelRegistrasi {
    public boolean isEmpty(String nama, String noTelp) {
        return TextUtils.isEmpty(nama) || TextUtils.isEmpty(noTelp);
    }

    public void saveUser(String nama, String noTelp) {
        Prefs.putBoolean("isFirstTime", false);
        Prefs.putString("id", System.currentTimeMillis() + "");
        Prefs.putString("nama", nama);
        Prefs.putString("noTelp", noTelp);
    }
}
