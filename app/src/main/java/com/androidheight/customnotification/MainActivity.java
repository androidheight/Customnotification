package com.androidheight.customnotification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    boolean currentVersionSupportBigNotification = currentVersionSupportBigNotification();
boolean SONG_PAUSED =false;
   static  int NOTIFICATION_ID = 100;

    public static final String NOTIFY_PREVIOUS = "com.androidheight.customnotification.previous";
    public static final String NOTIFY_DELETE = "com.androidheight.customnotification.delete";
    public static final String NOTIFY_PAUSE = "com.androidheight.customnotification.pause";
    public static final String NOTIFY_PLAY = "com.androidheight.customnotification.play";
    public static final String NOTIFY_NEXT = "com.androidheight.customnotification.next";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        newNotification();
    }


    private void newNotification() {
        String songName = "Song Name";
        String albumName = "Album Name";
        RemoteViews simpleContentView = new RemoteViews(getApplicationContext().getPackageName(),R.layout.custom_notification_layout);
        RemoteViews expandedView = new RemoteViews(getApplicationContext().getPackageName(), R.layout.big_custom_notification);

        Notification notification = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(songName).build();

        setListeners(simpleContentView);
        setListeners(expandedView);

        notification.contentView = simpleContentView;
        if(currentVersionSupportBigNotification){
            notification.bigContentView = expandedView;
        }

        try{

                notification.contentView.setImageViewResource(R.id.imageViewAlbumArt, R.drawable.music);
                if(currentVersionSupportBigNotification){
                    notification.bigContentView.setImageViewResource(R.id.imageViewAlbumArt, R.drawable.music);
                }
            }
        catch(Exception e){
            e.printStackTrace();
        }
        if(SONG_PAUSED){
            notification.contentView.setViewVisibility(R.id.btnPause, View.GONE);
            notification.contentView.setViewVisibility(R.id.btnPlay, View.VISIBLE);

            if(currentVersionSupportBigNotification){
                notification.bigContentView.setViewVisibility(R.id.btnPause, View.GONE);
                notification.bigContentView.setViewVisibility(R.id.btnPlay, View.VISIBLE);
            }
            SONG_PAUSED=false;
        }else{
            notification.contentView.setViewVisibility(R.id.btnPause, View.VISIBLE);
            notification.contentView.setViewVisibility(R.id.btnPlay, View.GONE);

            if(currentVersionSupportBigNotification){
                notification.bigContentView.setViewVisibility(R.id.btnPause, View.VISIBLE);
                notification.bigContentView.setViewVisibility(R.id.btnPlay, View.GONE);
            }
            SONG_PAUSED=true;
        }

        notification.contentView.setTextViewText(R.id.textSongName, songName);
        notification.contentView.setTextViewText(R.id.textAlbumName, albumName);
        if(currentVersionSupportBigNotification){
            notification.bigContentView.setTextViewText(R.id.textSongName, songName);
            notification.bigContentView.setTextViewText(R.id.textAlbumName, albumName);
        }
        notification.flags |= Notification.FLAG_ONGOING_EVENT;
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mNotificationManager.notify(NOTIFICATION_ID, notification);
        
    }

    /**
     * Notification click listeners
     * @param view
     */
    public void setListeners(RemoteViews view) {
        Intent previous = new Intent(NOTIFY_PREVIOUS);
        Intent delete = new Intent(NOTIFY_DELETE);
        Intent pause = new Intent(NOTIFY_PAUSE);
        Intent next = new Intent(NOTIFY_NEXT);
        Intent play = new Intent(NOTIFY_PLAY);

        PendingIntent pPrevious = PendingIntent.getBroadcast(getApplicationContext(), 0, previous, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btnPrevious, pPrevious);

        PendingIntent pDelete = PendingIntent.getBroadcast(getApplicationContext(), 0, delete, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btnDelete, pDelete);

        PendingIntent pPause = PendingIntent.getBroadcast(getApplicationContext(), 0, pause, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btnPause, pPause);

        PendingIntent pNext = PendingIntent.getBroadcast(getApplicationContext(), 0, next, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btnNext, pNext);

        PendingIntent pPlay = PendingIntent.getBroadcast(getApplicationContext(), 0, play, PendingIntent.FLAG_UPDATE_CURRENT);
        view.setOnClickPendingIntent(R.id.btnPlay, pPlay);

    }

    public static boolean currentVersionSupportBigNotification() {
        int sdkVersion = android.os.Build.VERSION.SDK_INT;
        if(sdkVersion >= android.os.Build.VERSION_CODES.JELLY_BEAN){
            return true;
        }
        return false;
    }


    public static class NotificationBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

                if (intent.getAction().equals(NOTIFY_PLAY)) {
                    Toast.makeText(context,"Play Clicked",Toast.LENGTH_LONG).show();
                } else if (intent.getAction().equals(NOTIFY_PAUSE)) {
                    Toast.makeText(context,"Pause Clicked",Toast.LENGTH_LONG).show();
                } else if (intent.getAction().equals(NOTIFY_NEXT)) {
                    Toast.makeText(context,"Next Clicked",Toast.LENGTH_LONG).show();
                } else if (intent.getAction().equals(NOTIFY_DELETE)) {
                    NotificationManager mNotificationManager = (NotificationManager)context. getSystemService(Context.NOTIFICATION_SERVICE);

                    mNotificationManager.cancel(NOTIFICATION_ID);
                }else if (intent.getAction().equals(NOTIFY_PREVIOUS)) {
                    Toast.makeText(context,"Previous Clicked",Toast.LENGTH_LONG).show();
                }
            }


        public String ComponentName() {
            return this.getClass().getName();
        }

    }
}
