package Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.gwangtae.todo.MainActivity;
import com.example.gwangtae.todo.R;

import java.util.Calendar;

import DB.DBHelper;

public class MyService extends Service {
    
    // 진동 설정
    Vibrator vibrator;
    
    // 음악 설정
    MediaPlayer media_song;

    String channelId = "channel";
    String channelName = "ChannelName";
    
    Calendar cal;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.

        throw null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        media_song = MediaPlayer.create(this, R.raw.music);

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);

            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channelId);
        Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        int resquestID = (int)System.currentTimeMillis();

        PendingIntent pending_intent_main_activity = PendingIntent.getActivity(getApplicationContext(), resquestID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentTitle("ToDo 알림 안내")
//         builder.setContentTitle(intent.getExtras().getString("todo_title"))
                .setContentText("설정하신 시간대로 알람을 울립니다.")
//                 .setContentText(intent.getExtras().getString("todo_content"))
                .setDefaults(Notification.DEFAULT_ALL) // 알림, 사운드 진동 설정
                .setAutoCancel(true) // 알림 터치시 반응 후 삭제
                .setSmallIcon(android.R.drawable.btn_star)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.papers))
                .setBadgeIconType(R.drawable.papers)
                .setContentIntent(pending_intent_main_activity);

        notificationManager.notify(0, builder.build());

        vibrator.vibrate(new long[]{1000, 3000}, 0);
        media_song.start();

        //시작 ID 0또는 1
//        assert state != null;
//        switch (state) {
//            case "alarm on":
//                startId = 1;
//                Log.e("Start ID is", state);
//                break;
//            case "alarm off":
//                startId = 0;
//                Log.e("Start ID is", state);
//                break;
//            default:
//                startId = 0;
//                break;
//        }
//
//
//
//        //if else statements
//
//        // if there is no music playing, and the user pressed "alarm on"
//        // music should start playing
//        if(!this.isRunning && startId == 1){
//            Log.e("there is no music", "and you wand start");
//
//            media_song = MediaPlayer.create(this, R.raw.music);
//            media_song.start();
//
//            this.isRunning = true;
//            this.startId = 0;
//        }
//
//        // if there is music playing, and the user pressed "alarm off"
//        // music should stop playing
//        else if(this.isRunning && startId == 0){
//            Log.e("there is music", "and you wand end");
//
//            media_song.stop();
//            media_song.reset();
//
//            this.isRunning = false;
//            this.startId = 0;
//        }
//
//        // there are if the user random buttons
//        // just to bug-prof the app
//        // if there is no music playing, and the user pressed "alarm off"
//        // do nothing
//        else if(!this.isRunning && startId == 0){
//            Log.e("there is no music", "and you wand end");
//
//
//            this.isRunning = false;
//            this.startId = 0;
//        }
//
//        // if there is music playing and the user pressed "alarm on"
//        // do nothing
//        else if(this.isRunning && startId == 1){
//            Log.e("there is music", "and you wand start");
//
//            this.isRunning = true;
//            this.startId = 1;
//        }
//
//        // can't think of anything else, just to catch the odd event
//        else{
//            Log.e("else", "somehow you reached this");
//
//        }



        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.e("on Destroy called", "ta da");

        super.onDestroy();

        //Toast.makeText(this, "on Destroy called", Toast.LENGTH_SHORT).show();
    }
    
    class myStartceHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            cal = Calendar.getInstance();
            
            cal.set(Calendar.HOUR_OF_DAY, Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
            cal.set(Calendar.MINUTE, Calendar.getInstance().get(Calendar.MINUTE));

            log.e("RTC", String.valueOf(cal.getTimeInMillis()));
        }
    }
}
