package Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
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

    myStartceHandler handler;

    DBHelper helper;
    SQLiteDatabase sqLiteDatabase;

    int year, month, day;
    Vibrator vibrator;

    MediaPlayer media_song;
    int startId;
    boolean isRunning;

    String channelId = "channel";
    String channelName = "ChannelName";

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.

        throw null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        helper = new DBHelper(getApplicationContext());
        sqLiteDatabase = helper.getWritableDatabase();

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

        PendingIntent pending_intent_main_activity = PendingIntent.getActivity(this, resquestID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setContentTitle("ToDo 알림 안내")
                .setContentText("설정하신 시간대로 알람을 울립니다.")
                .setDefaults(Notification.DEFAULT_ALL) // 알림, 사운드 진동 설정
                .setAutoCancel(true) // 알림 터치시 반응 후 삭제
                .setSmallIcon(android.R.drawable.btn_star)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.papers))
                .setBadgeIconType(R.drawable.papers)
                .setContentIntent(pending_intent_main_activity);

        notificationManager.notify(0, builder.build());

        vibrator.vibrate(new long[]{1000, 3000}, 0);

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

        handler = new myStartceHandler();
        mThread thread = new mThread(handler);
        thread.start();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.e("on Destroy called", "ta da");

        super.onDestroy();
        this.isRunning = false;

        //Toast.makeText(this, "on Destroy called", Toast.LENGTH_SHORT).show();
    }

    class myStartceHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            year = Calendar.getInstance().get(Calendar.YEAR);
            month = Calendar.getInstance().get(Calendar.MONTH);
            day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

            String new_date = String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(day);

            Log.d("DATE", "handleMessage: " + new_date);

            // 날짜 비교후 일치하면 알람 발생

//            Toast.makeText(MyService.this, "알림 발생", Toast.LENGTH_SHORT).show();
        }
    }

    class mThread extends Thread {

        Handler handler;
        boolean isRun = true;

        public mThread(Handler handler){
            this.handler = handler;
        }

        public void stopForever(){
            synchronized (this) {
                this.isRun = false;
            }
        }

        @Override
        public void run() {
            while(isRun){
                handler.sendEmptyMessage(0);
                try{
                    Thread.sleep(10000);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
