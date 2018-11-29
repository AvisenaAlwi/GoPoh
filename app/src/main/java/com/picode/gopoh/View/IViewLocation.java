package com.picode.gopoh.View;

import com.picode.gopoh.Model.ModelTempatWisata;

import javax.annotation.Nullable;

public interface IViewLocation {
    public void onLokasiTerdeteksi(@Nullable ModelTempatWisata wisata, boolean sukses);
}
