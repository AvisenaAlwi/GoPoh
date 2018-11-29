package com.picode.gopoh.Model;

import android.content.Context;

import com.google.firebase.firestore.FirebaseFirestore;
import com.picode.gopoh.Interface.OnGetChatComplete;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.Date;
import java.util.HashMap;

public class ModelChatting {

    private Context context;
    private String roomId;

    public ModelChatting(Context context, String roomId) {
        this.context = context;
        this.roomId = roomId;
    }

    public void sendMessage(String message) {
        // input -> charsequence yang disubmit
        String id = System.currentTimeMillis() + "";
        HashMap<String, Object> mapChat = new HashMap<>();
        mapChat.put("roomId", roomId);
        mapChat.put("message", message);
        mapChat.put("senderId", Prefs.getString("id", "unknow"));
        mapChat.put("senderName", Prefs.getString("nama", "unknow"));
        mapChat.put("time", new Date());
        FirebaseFirestore.getInstance().collection("chats")
                .document(id)
                .set(mapChat);
    }

    public void getChats(OnGetChatComplete listener) {
        FirebaseFirestore.getInstance().collection("chats")
                .whereEqualTo("roomId", this.roomId)
                .orderBy("time")
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (queryDocumentSnapshots != null)
                        listener.onChatsChanged(queryDocumentSnapshots);
                });
    }
}
