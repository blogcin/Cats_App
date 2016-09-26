package kr.co.alteration.cat.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import kr.co.alteration.cat.MainActivity;
import kr.co.alteration.cat.client.ClientService;

/**
 * Created by blogc on 2016-09-24.
 */
public class NotiManager extends BroadcastReceiver {
    private final String TAG = "NotiManager";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);

            Intent Intent = new Intent(context, ClientService.class);
            PendingIntent pIntent = PendingIntent.getService(context, 0, Intent, 0);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 3 * 60 * 1000, pIntent);
        }
    }
}
