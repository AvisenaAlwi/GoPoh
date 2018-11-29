package com.picode.gopoh.Model;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.picode.gopoh.Interface.OnGetLastChatFromRoomSuccess;
import com.picode.gopoh.Interface.OnGetRoomChatsSuccess;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.ArrayList;
import java.util.List;

public class ModelAdminPanel {
    public void getRoomChats(OnGetRoomChatsSuccess listener) {
        String idString = Prefs.getString("id", "unknow");
        FirebaseFirestore.getInstance()
                .collection("rooms")
                .whereEqualTo("idUser2", idString)
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (queryDocumentSnapshots == null)
                        return;
                    List<RoomChat> roomChats = new ArrayList<>();
                    for (DocumentChange zz : queryDocumentSnapshots.getDocumentChanges()) {
                        QueryDocumentSnapshot z = zz.getDocument();
                        roomChats.add(
                                new RoomChat(
                                        z.getId(),
                                        z.getString("idUser1"),
                                        z.getString("idUser2"),
                                        z.getString("nameUser1"),
                                        z.getString("nameUser2"),
                                        z.getString("telponUser1"),
                                        z.getString("telponUser2")
                                )
                        );
                    }

                    listener.onGetRoomChatsChanged(roomChats);
                });
    }

    public void getLastChatFromRoom(RoomChat roomChat, OnGetLastChatFromRoomSuccess listener) {
        FirebaseFirestore.getInstance()
                .collection("chats")
                .whereEqualTo("roomId", roomChat.getId())
                .orderBy("time", Query.Direction.DESCENDING)
                .limit(1)
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    if (queryDocumentSnapshots != null && queryDocumentSnapshots.size() != 0) {
                        DocumentSnapshot z = queryDocumentSnapshots.getDocuments().get(0);
                        Chat lastChat = new Chat(
                                z.getId(),
                                z.getString("roomId"),
                                z.getString("message"),
                                z.getString("senderId"),
                                z.getString("senderName"),
                                z.getDate("time")
                        );

                        listener.onGetLastChatFromRoomSuccess(lastChat);
                    }
                });
    }
}
