package com.picode.gopoh.Control;

import com.picode.gopoh.Model.ModelRegistrasi;
import com.picode.gopoh.View.IViewRegistrasi;

public class ControlRegistrasi {

    IViewRegistrasi viewRegistrasi;

    public ControlRegistrasi(IViewRegistrasi viewRegistrasi) {
        this.viewRegistrasi = viewRegistrasi;
    }

    public void registrasi(String nama, String noTelp) {
        ModelRegistrasi modelRegistrasi = new ModelRegistrasi();
        boolean isValid = !modelRegistrasi.isEmpty(nama, noTelp);
        if (isValid) {
            modelRegistrasi.saveUser(nama, noTelp);
            viewRegistrasi.onRegistrasiSuccess("Sukses");
        } else
            viewRegistrasi.onRegistrasiFailed("Form nama atau nomor telpon tidak boleh kosong");
    }
}
