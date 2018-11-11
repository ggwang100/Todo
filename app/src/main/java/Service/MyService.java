package Service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

import DB.DBHelper;

public class MyService extends Service {

    NotificationManager Notifi_M;
    myStartceHandler handler;

    DBHelper helper;
    SQLiteDatabase sqLiteDatabase;

    int year, month, day;

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
            year = Calendar.getInstance().get(Calendar.YEAR);
            month = Calendar.getInstance().get(Calendar.MONTH);
            day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

            String new_date = String.valueOf(year) + "-" + String.valueOf(month) + "-" + String.valueOf(day);

            Log.d("DATE", "handleMessage: " + new_date);

            // 날짜 비교후 일치하면 알람 발생

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
