package com.picode.gopoh;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;
import android.support.v4.app.TaskStackBuilder;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pixplicity.easyprefs.library.Prefs;

import java.util.HashSet;

import io.nlopez.smartlocation.SmartLocation;

public class AnnouncementServices extends Service {

    public static final String GROUP_NOTIFICATION_ANNOUNCEMENT = "com.picode.gopoh.Announcement";
    public static final String KEY_TEXT_REPLY = "key_text_reply";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
        FirebaseFirestore.getInstance().collection("announcements")
                .addSnapshotListener((queryDocumentSnapshots, e) -> {
                    HashSet<String> setAnnouncement = (HashSet<String>) Prefs.getStringSet("announcements", new HashSet<>());

                    if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty()) {
                        for (DocumentChange change : queryDocumentSnapshots.getDocumentChanges()) {
                            DocumentSnapshot doc = change.getDocument();
                            int radius = doc.getLong("radius").intValue();
                            Location notifLocation = new Location("");
                            notifLocation.setLatitude(doc.getGeoPoint("location").getLatitude());
                            notifLocation.setLongitude(doc.getGeoPoint("location").getLongitude());
                            Location lastLocation = SmartLocation.with(this).location().getLastLocation();
                            if (lastLocation != null && notifLocation.distanceTo(lastLocation) <= radius) {

                                if (!setAnnouncement.contains(doc.getId())) {
                                    showNotif(doc.getString("idAdmin"), doc.getString("namaWisata"), doc.getString("message"));
                                    setAnnouncement.add(doc.getId());
                                    Prefs.putStringSet("announcements", setAnnouncement);
                                }

                            }
                        }
                    }
                });
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void showNotif(String idAdmin, String namaWisata, String message) {
        FirebaseFirestore.getInstance().collection("rooms")
                .whereEqualTo("idUser1", Prefs.getString("id", "unknow"))
                .whereEqualTo("idUser2", idAdmin)
                .limit(1)
                .get()
                .addOnSuccessListener(qds -> {
                    if(!qds.isEmpty()){
                        int NOTIF_ID = (int) System.currentTimeMillis();
                        DocumentSnapshot doc = qds.getDocuments().get(0);
                        int endIndex = message.length() <= 100 ? message.length() : 100;
                        Intent intent = new Intent(this, ChattingActivity.class);
                        intent.putExtra("notifId", NOTIF_ID);
                        intent.putExtra("roomId", doc.getId());
                        intent.putExtra("namaWisata", namaWisata);
                        intent.putExtra("prefill", "[BALASAN PENGUMUMAN]\n".trim() +
                                "Pengumuman = " + message.substring(0, endIndex) + " ...\n====================\n".trim());
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

                        String replyLabel = "Balas pengumuman ini";
                        RemoteInput remoteInput = new RemoteInput.Builder(KEY_TEXT_REPLY)
                                .setLabel(replyLabel)
                                .build();

                        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
    // Adds the back stack for the Intent (but not the Intent itself)
                        stackBuilder.addParentStack(MainActivity.class);
    // Adds the Intent that starts the Activity to the top of the stack
                        Intent intentReply = getMessageReplyIntent(doc.getId(), namaWisata, message);
                        intentReply.putExtra("notifId", NOTIF_ID);
                        stackBuilder.addNextIntent(intentReply);

                        // Build a PendingIntent for the reply action to trigger.
                        PendingIntent replyPendingIntent =
                                stackBuilder.getPendingIntent(200, PendingIntent.FLAG_UPDATE_CURRENT);

                        NotificationCompat.Action action =
                                new NotificationCompat.Action.Builder(R.drawable.ic_send,
                                        "Balas", replyPendingIntent)
                                        .addRemoteInput(remoteInput)
                                        .setAllowGeneratedReplies(true)
                                        .build();

                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "GoPoh")
                                .setSmallIcon(R.drawable.ic_loudspeaker)
                                .setContentTitle("Pengumuman")
                                .setSubText(namaWisata)
                                .setContentText(message)
                                .setPriority(NotificationCompat.PRIORITY_MAX)
                                .setStyle(new NotificationCompat.BigTextStyle()
                                        .bigText(message))
                                // Set the intent that will fire when the user taps the notification
                                .setContentIntent(pendingIntent)
                                .addAction(action)
                                .setGroup(GROUP_NOTIFICATION_ANNOUNCEMENT)
                                .setGroupSummary(true)
                                .setAutoCancel(true);
                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                        notificationManager.notify(NOTIF_ID, mBuilder.build());
                    }
                });
    }

    private Intent getMessageReplyIntent(String id, String namaWisata, String message) {
        int endIndex = message.length() <= 100 ? message.length() : 100;
        Intent intent = new Intent(this, ChattingActivity.class);
        intent.putExtra("roomId", id);
        intent.putExtra("namaWisata", namaWisata);
        intent.putExtra("prefill", "[BALASAN PENGUMUMAN]\n" +
                "Pengumuman = " + message.substring(0, endIndex) + " ...\n====================\n");
        intent.putExtra("replyIntent", true);
        return intent;
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "GoPoh Channel";
            String description = "GoPoh Channel description";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("GoPoh", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null)
                notificationManager.createNotificationChannel(channel);
        }
    }
}
