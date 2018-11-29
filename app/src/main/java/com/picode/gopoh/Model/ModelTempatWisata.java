package com.picode.gopoh.Model;

import android.location.Location;

import java.io.Serializable;

public class ModelTempatWisata implements Serializable {
    String id, nama, idAdmin;
    Location lokasi;

    public ModelTempatWisata(String id, String nama, String idAdmin, Location lokasi) {
        this.id = id;
        this.nama = nama;
        this.idAdmin = idAdmin;
        this.lokasi = lokasi;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getIdAdmin() {
        return idAdmin;
    }

    public void setIdAdmin(String idAdmin) {
        this.idAdmin = idAdmin;
    }

    public Location getLokasi() {
        return lokasi;
    }

    public void setLokasi(Location lokasi) {
        this.lokasi = lokasi;
    }

    public boolean isLokasiIn(Location lokasiPengguna, int radiusInMeter) {
        if (lokasiPengguna.distanceTo(this.lokasi) <= radiusInMeter)
            return true;
        return false;
    }
}
