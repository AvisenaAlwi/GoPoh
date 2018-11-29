package com.picode.gopoh.Control;

import android.content.Context;
import android.content.Intent;

import com.picode.gopoh.AddAnnouncementActivity;
import com.picode.gopoh.ChattingActivity;
import com.picode.gopoh.Interface.OnGetRoomChatsSuccess;
import com.picode.gopoh.Model.Dialog;
import com.picode.gopoh.Model.ModelAdminPanel;
import com.picode.gopoh.Model.ModelLogin;
import com.picode.gopoh.Model.ModelTempatWisata;
import com.picode.gopoh.Model.RoomChat;
import com.picode.gopoh.R;
import com.picode.gopoh.RegistrasiActivity;
import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.dialogs.DialogsList;
import com.stfalcon.chatkit.dialogs.DialogsListAdapter;

import java.util.List;

//import com.cooltechworks.views.shimmer.ShimmerRecyclerView;

public class ControlAdminPanel {

    private Context context;
    private DialogsList dialogsList;
    private ModelAdminPanel modelAdminPanel;
    private ModelTempatWisata modelTempatWisata;
    private DialogsListAdapter<Dialog> dialogsListAdapter;

    public ControlAdminPanel(Context context, DialogsList dialogsList, OnGetRoomChatsSuccess listener) {
        this.context = context;
        this.dialogsList = dialogsList;
        modelAdminPanel = new ModelAdminPanel();
        modelAdminPanel.getRoomChats(listener);
    }

    public void logout() {
        ModelLogin modelLogin = new ModelLogin();
        modelLogin.logout();
        Intent intent = new Intent(context, RegistrasiActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void showAddannouncement() {
        Intent intent = new Intent(context, AddAnnouncementActivity.class);
        context.startActivity(intent);
    }


    public void setupDialogList(List<RoomChat> roomChats) {
        dialogsListAdapter = new DialogsListAdapter<>((imageView, url, payload) -> {
            imageView.setImageResource(R.drawable.ic_person);
        });
        dialogsListAdapter.setOnDialogClickListener(dialog -> {

            Intent i = new Intent(context, ChattingActivity.class);
            i.putExtra("chatingAsAdmin", true);
            i.putExtra("roomId", dialog.getId());
            context.startActivity(i);

        });
        for (RoomChat roomChat : roomChats) {
            modelAdminPanel.getLastChatFromRoom(roomChat, chat -> {
                onNewMessage(roomChat, chat);
            });
        }

        dialogsList.setAdapter(dialogsListAdapter);
    }

    private void onNewMessage(RoomChat roomChat, IMessage chat) {
        if (!dialogsListAdapter.updateDialogWithMessage(roomChat.getId(), chat)) {
            Dialog dialog = new Dialog(roomChat, chat);
            dialogsListAdapter.upsertItem(dialog);
        }
        dialogsListAdapter.sortByLastMessageDate();
    }
}
