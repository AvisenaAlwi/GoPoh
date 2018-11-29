package com.picode.gopoh.Control;

import android.content.Context;
import android.content.Intent;

import com.picode.gopoh.AddAdminActivity;
import com.picode.gopoh.AdminPanelActivity;
import com.picode.gopoh.Model.ModelLogin;
import com.picode.gopoh.View.IViewLogin;

public class ControlLogin {

    ModelLogin modelLogin = new ModelLogin();
    private Context context;
    private IViewLogin listener;

    public ControlLogin(Context context, IViewLogin listener) {
        this.context = context;
        this.listener = listener;
        if (modelLogin.idAlreadyLogin()) {
            openAdminPanel();
            return;
        }
    }

    public void openAdminPanel() {
        Intent intent = new Intent(context, AdminPanelActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public void loginAsAdmin(String email, String password) {
        modelLogin.cekLogin(email, password, listener);
    }

    public void loginAsSuperAdmin(String email, String password) {
        if (email.equals("superadmin@admin.com") && password.equals("superadmin")) {
            // login berhasil
            Intent intent = new Intent(context, AddAdminActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else {
            listener.onLoginResult(false, "Login salah");
        }
    }
}
