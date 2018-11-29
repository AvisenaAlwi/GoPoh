package com.picode.gopoh.Control;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.Toolbar;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.picode.gopoh.Interface.OnGetChatComplete;
import com.picode.gopoh.Model.Chat;
import com.picode.gopoh.Model.ModelChatting;
import com.picode.gopoh.R;
import com.picode.gopoh.View.IViewChat;
import com.pixplicity.easyprefs.library.Prefs;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;
import com.stfalcon.chatkit.utils.DateFormatter;

public class ControlChatting implements OnGetChatComplete {

    Context context;
    ModelChatting modelChatting;
    String roomId, myId, namaWisata, opponentId, chatWith, noTelp;
    MessagesListAdapter<Chat> adapter;

    IViewChat iViewChat;
    private boolean chatingAsAdmin;

    public ControlChatting(Context context, IViewChat listener, boolean chatingAsAdmin, String roomId, String namaWisata) {
        myId = Prefs.getString("id", "unknow");
        this.context = context;
        this.iViewChat = listener;
        this.adapter = new MessagesListAdapter<>(myId, null);
        this.chatingAsAdmin = chatingAsAdmin;
        this.namaWisata = namaWisata;

        this.roomId = roomId;

        FirebaseFirestore.getInstance().collection("rooms")
                .document(roomId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot != null) {
                        String idUser1 = documentSnapshot.getString("idUser1");
                        String idUser2 = documentSnapshot.getString("idUser2");
                        if (idUser1.equalsIgnoreCase(myId)) {
                            opponentId = idUser2;
                            chatWith = documentSnapshot.getString("nameUser2");
                            noTelp = documentSnapshot.getString("telponUser2");
                        } else {
                            opponentId = idUser1;
                            chatWith = documentSnapshot.getString("nameUser1");
                            noTelp = documentSnapshot.getString("telponUser1");
                        }

                        iViewChat.onRoomChatObtained();
                    }
                });


    }

    public void getChatsData() {
        if (modelChatting == null)
            modelChatting = new ModelChatting(context, roomId);
        modelChatting.getChats(this);
    }

    public void showChats(MessagesList messagesList) {
        adapter.setDateHeadersFormatter(date -> {
            if (DateFormatter.isToday(date)) {
                return "Hari Ini";
            } else if (DateFormatter.isYesterday(date)) {
                return "Kemarin";
            } else {
                return DateFormatter.format(date, DateFormatter.Template.STRING_DAY_MONTH_YEAR);
            }
        });
        messagesList.setAdapter(this.adapter);
    }

    public void setupToolbar(Toolbar toolbar) {
        if (!chatingAsAdmin) {
            toolbar.inflateMenu(R.menu.menu_chat_room);
            toolbar.setOnMenuItemClickListener(menuItem -> {
                switch (menuItem.getItemId()) {
                    case R.id.telp:
                        showTelp();
                        break;
                }
                return true;
            });
            toolbar.setTitle("Admin " + namaWisata);
            toolbar.setSubtitle(chatWith);
        } else {
            toolbar.setTitle(chatWith);
            toolbar.setSubtitle(noTelp);
        }
    }

    private void showTelp() {
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + noTelp)));
    }

    @Override
    public void onChatsChanged(QuerySnapshot queryDocumentSnapshots) {
        for (DocumentChange z : queryDocumentSnapshots.getDocumentChanges()) {
            String chatId = z.getDocument().getId();
            Chat chat = new Chat(
                    chatId,
                    z.getDocument().getString("roomId"),
                    z.getDocument().getString("message"),
                    z.getDocument().getString("senderId"),
                    z.getDocument().getString("senderName"),
                    z.getDocument().getDate("time")
            );
            adapter.addToStart(chat, true);
        }
        // panggil method ini untuk memberitahukan ke view bahwa data telah didapatkan
        iViewChat.onChatsObtained();
    }

    public void sendMessage(String message) {
        modelChatting.sendMessage(message);
    }
}
