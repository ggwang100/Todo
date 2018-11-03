package Service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.gwangtae.todo.MainActivity;

public class Broadcast extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {   // 재부팅 완료되면
            Log.d("succ", "재부팅 완료");
            Intent i = new Intent(context, MainActivity.class); // 시작
            context.startService(i);
        }
    }
}
