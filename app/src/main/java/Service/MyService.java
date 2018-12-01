package Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.gwangtae.todo.R;
import com.example.gwangtae.todo.read;

import java.util.Calendar;

public class MyService extends Service {
    
    myStartceHandler handler;
    NotificationCompat.Builder builder;
    NotificationManager notificationManager;

    Intent intent;
    
    // 진동 설정
    Vibrator vibrator;
    
    // 음악 설정
    MediaPlayer media_song;

    String channelId = "channel";
    String channelName = "ChannelName";

    int startId;
    boolean isRunning;

    Calendar cal;
    
    int hour, min;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.

        throw null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String state = intent.getExtras().getString("extra");
        
        handler = new myStartceHandler();
        mThread thread = new mThread(handler);
        thread.start();
        this.intent = intent;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            alarm_start();
            startForeground(1, builder.build());
        }else{
            alarm_start();
            notificationManager.notify(0, builder.build());
        }

        assert state != null;
        switch (state) {
            case "alarm on":
                startId = 1;
                Log.e("Start ID is", state);
                Log.e("Start ID is", "ID" + startId);
                break;
            case "alarm off":
                startId = 0;
                Log.e("Start ID is", state);
                Log.e("Start ID is", "ID" + startId);
                break;
            default:
                startId = 0;
                break;
        }

        if(!this.isRunning && startId == 1){
            Log.e("there is no music", "and you wand start");
            media_song = MediaPlayer.create(this, R.raw.music);
            vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

            vibrator.vibrate(new long[]{1000, 3000}, 0);
            media_song.start();

            this.isRunning = true;
            this.startId = 0;
        }

        else if(this.isRunning && startId == 0){
            Log.e("there is music", "and you wand end");

            vibrator.cancel();
            media_song.stop();
            media_song.reset();

           this.isRunning = false;
            this.startId = 0;
        }

        else if(!this.isRunning && startId == 0){
            Log.e("there is no music", "and you wand end");


            this.isRunning = false;
            this.startId = 0;
        }

        else if(this.isRunning && startId == 1){
            Log.e("there is music", "and you wand start");

            this.isRunning = true;
            this.startId = 1;
        }

        else{
            Log.e("else", "somehow you reached this");
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.e("on Destroy called", "ta da");

        super.onDestroy();

        this.isRunning = false;
    }
    
    public void alarm_start(){
        notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);


        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(channelId, channelName, importance);

            notificationManager.createNotificationChannel(mChannel);
        }

        builder = new NotificationCompat.Builder(getApplicationContext(), channelId);
        Intent notificationIntent = new Intent(getApplicationContext(), read.class);
        notificationIntent.putExtra("ID", intent.getExtras().getString("todo_no"));
        notificationIntent.putExtra("TITLE", intent.getExtras().getString("todo_title"));

        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        int resquestID = (int)System.currentTimeMillis();

        PendingIntent pending_intent_main_activity = PendingIntent.getActivity(getApplicationContext(), resquestID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

          builder.setContentTitle(intent.getExtras().getString("todo_title"))
                .setContentText(intent.getExtras().getString("todo_content"))
                .setDefaults(Notification.DEFAULT_ALL) // 알림, 사운드 진동 설정
                .setAutoCancel(true) // 알림 터치시 반응 후 삭제
                .setSmallIcon(android.R.drawable.btn_star)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.papers))
                .setBadgeIconType(R.drawable.papers)
                .setContentIntent(pending_intent_main_activity);
    }
    
    class myStartceHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            cal = Calendar.getInstance();
            
            hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            min = Calendar.getInstance().get(Calendar.MINUTE);
            
            cal.set(Calendar.HOUR_OF_DAY, hour);
            cal.set(Calendar.MINUTE, min);
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
                    Thread.sleep(1000);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
