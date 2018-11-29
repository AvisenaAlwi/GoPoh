package com.picode.gopoh.Control;

import android.content.Context;

import com.picode.gopoh.Interface.OnAnnouncementSuccess;
import com.picode.gopoh.Interface.OnGetModelTempatWisata;
import com.picode.gopoh.Model.ModelAddAnnouncement;
import com.picode.gopoh.Model.ModelTempatWisata;
import com.pixplicity.easyprefs.library.Prefs;

public class ControlAnnouncement implements OnGetModelTempatWisata {

    private Context context;
    private ModelTempatWisata currentModelWisata;
    private ModelAddAnnouncement modelAddAnnouncement;

    public ControlAnnouncement() {
        modelAddAnnouncement = new ModelAddAnnouncement();

        modelAddAnnouncement.getModelTempatWisata(Prefs.getString("id", "unknow"), this);
    }

    public void announce(String message, OnAnnouncementSuccess listener) {
        modelAddAnnouncement.announce(message, currentModelWisata, listener);
    }

    @Override
    public void OnGetModelTempatWisata(ModelTempatWisata wisata) {
        currentModelWisata = wisata;
    }
}
