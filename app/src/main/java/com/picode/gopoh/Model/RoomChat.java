package com.picode.gopoh.Model;

public class RoomChat {
    private String id, idUser1, idUser2, nameUser1, nameUser2, telponUser1, telponUser2;

    public RoomChat(String id, String idUser1, String idUser2, String nameUser1, String nameUser2, String telponUser1, String telponUser2) {
        this.id = id;
        this.idUser1 = idUser1;
        this.idUser2 = idUser2;
        this.nameUser1 = nameUser1;
        this.nameUser2 = nameUser2;
        this.telponUser1 = telponUser1;
        this.telponUser2 = telponUser2;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdUser1() {
        return idUser1;
    }

    public void setIdUser1(String idUser1) {
        this.idUser1 = idUser1;
    }

    public String getIdUser2() {
        return idUser2;
    }

    public void setIdUser2(String idUser2) {
        this.idUser2 = idUser2;
    }

    public String getNameUser1() {
        return nameUser1;
    }

    public void setNameUser1(String nameUser1) {
        this.nameUser1 = nameUser1;
    }

    public String getNameUser2() {
        return nameUser2;
    }

    public void setNameUser2(String nameUser2) {
        this.nameUser2 = nameUser2;
    }

    public String getTelponUser1() {
        return telponUser1;
    }

    public void setTelponUser1(String telponUser1) {
        this.telponUser1 = telponUser1;
    }

    public String getTelponUser2() {
        return telponUser2;
    }

    public void setTelponUser2(String telponUser2) {
        this.telponUser2 = telponUser2;
    }
}
