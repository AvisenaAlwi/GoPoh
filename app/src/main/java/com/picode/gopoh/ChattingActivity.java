package com.picode.gopoh;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.picode.gopoh.Control.ControlChatting;
import com.picode.gopoh.View.IViewChat;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.picode.gopoh.AnnouncementServices.KEY_TEXT_REPLY;

public class ChattingActivity extends AppCompatActivity implements IViewChat {

    ControlChatting controlChatting;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.messagesList) MessagesList messagesList;
    @BindView(R.id.input) MessageInput messageInput;

    private boolean chatingAsAdmin = false, replyIntent = false;
    private String roomId, namaWisata, prefill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chating);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        if (getIntent() != null) {
            chatingAsAdmin = getIntent().getBooleanExtra("chatingAsAdmin", false);
            roomId = getIntent().getStringExtra("roomId");
            namaWisata = getIntent().getStringExtra("namaWisata");
            prefill = getIntent().getStringExtra("prefill");
            replyIntent = getIntent().getBooleanExtra("replyIntent", false);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setSubtitle("");
        }
        controlChatting = new ControlChatting(this, this, chatingAsAdmin, roomId, namaWisata);
        controlChatting.getChatsData();

        messageInput.setInputListener(input -> {
            controlChatting.sendMessage(input.toString());
            return true;
        });
        if (replyIntent && prefill != null && !prefill.isEmpty()) {
            controlChatting.sendMessage(prefill.concat(getMessageText(getIntent()).toString()));
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.cancel( getIntent().getIntExtra("notifId", 0) );
        } else if (prefill != null && !prefill.equals(""))
            messageInput.getInputEditText().setText(prefill);
    }

    private CharSequence getMessageText(Intent intent) {
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null) {
            return remoteInput.getCharSequence(KEY_TEXT_REPLY);
        }
        return null;
    }

    @Override
    public void onChatsObtained() {
        controlChatting.showChats(messagesList);
    }

    @Override
    public void onRoomChatObtained() {
        controlChatting.setupToolbar(toolbar);
    }
}
