package com.picode.gopoh.Control;

import com.google.firebase.firestore.GeoPoint;
import com.picode.gopoh.Model.ModelAddAdmin;
import com.picode.gopoh.View.IViewAddAdmin;

import java.util.HashMap;

public class ControlAddAdmin {

    private ModelAddAdmin modelAddAdmin;

    public ControlAddAdmin(){
        modelAddAdmin = new ModelAddAdmin();
    }

    public void addAdmin(HashMap<String, String> input, GeoPoint location, IViewAddAdmin listener){
        String idWisata = "Wis-"+System.currentTimeMillis();
        String idAdmin = "Adm-"+System.currentTimeMillis();

        HashMap<String, Object> dataAdmin = new HashMap<>();
        dataAdmin.put("idWisata", idWisata);
        dataAdmin.put("email", input.get("email"));
        dataAdmin.put("name", input.get("namaAdmin"));
        dataAdmin.put("noTelp", input.get("noTelp"));
        dataAdmin.put("password", input.get("password"));

        HashMap<String, Object> dataWisata = new HashMap<>();
        dataWisata.put("idAdmin", idAdmin);
        dataWisata.put("lokasi", location);
        dataWisata.put("name", input.get("namaWisata"));

        modelAddAdmin.addAdmin(dataAdmin, dataWisata, listener);
    }
}
