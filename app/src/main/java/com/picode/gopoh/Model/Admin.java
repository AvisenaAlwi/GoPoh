package com.picode.gopoh.Model;

public class Admin extends User {

    private long nomorTelp;

    public Admin(String id, String name, long nomorTelp) {
        super(id, name);
        this.nomorTelp = nomorTelp;
    }

    public long getNomorTelp() {
        return nomorTelp;
    }

    public void setNomorTelp(long nomorTelp) {
        this.nomorTelp = nomorTelp;
    }
}
