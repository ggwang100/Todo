package Service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.example.gwangtae.todo.MainActivity;

public class MyService extends Service {

    NotificationManager Notifi_M;
    myStartceHandler handler;

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.

        throw null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Notifi_M = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        handler = new myStartceHandler();
        mThread thread = new mThread(handler);
        thread.start();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    class myStartceHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
//            Intent intent = new Intent(MyService.this, MainActivity.class);
//            PendingIntent P = PendingIntent.getActivity(MyService.this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//
//            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext())
//                    .setContentTitle("알람입니다.")
//                    .setContentText("알람 발생");

            Toast.makeText(MyService.this, "알림 발생", Toast.LENGTH_SHORT).show();
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
