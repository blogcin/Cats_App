package kr.co.alteration.cat.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import kr.co.alteration.cat.MainActivity;

/**
 * Created by blogc on 2016-09-24.
 */
public class NotiManager extends BroadcastReceiver {
    private final String TAG = "NotiManager";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
            Intent Intent = new Intent(context, MainActivity.class);
            PendingIntent pIntent = PendingIntent.getActivity(context, 0, Intent, 0);

            alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + 10, pIntent);

            Log.d(TAG, "OK, Alarm is registered");
        }
    }
}
