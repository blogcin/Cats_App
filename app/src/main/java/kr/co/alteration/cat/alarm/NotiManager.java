package kr.co.alteration.cat.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by blogc on 2016-09-24.
 */
public class NotiManager extends BroadcastReceiver {
    private final String TAG = "NotiManager";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Log.d(TAG, "OK, Alarm is registered");
        }
    }
}
