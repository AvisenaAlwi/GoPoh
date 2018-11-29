package com.picode.gopoh.Model;

import com.stfalcon.chatkit.commons.models.IDialog;
import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.IUser;

import java.util.ArrayList;
import java.util.List;

public class Dialog implements IDialog {

    private RoomChat roomChat;
    private IMessage lastMessage;

    public Dialog(RoomChat roomChat, IMessage lastMessage) {
        this.roomChat = roomChat;
        this.lastMessage = lastMessage;
    }

    @Override
    public String getId() {
        return roomChat.getId();
    }

    @Override
    public String getDialogPhoto() {
        return "";
    }

    @Override
    public String getDialogName() {
        return roomChat.getNameUser1();
    }

    @Override
    public List<? extends IUser> getUsers() {
        return new ArrayList<>();
    }

    @Override
    public IMessage getLastMessage() {
        return lastMessage;
    }

    @Override
    public void setLastMessage(IMessage message) {
        this.lastMessage = message;
    }

    @Override
    public int getUnreadCount() {
        return 0;
    }
}
