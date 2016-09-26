package kr.co.alteration.cat.client;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import kr.co.alteration.cat.MainActivity;
import kr.co.alteration.cat.R;
import kr.co.alteration.cat.alarm.AlarmActivity;
import kr.co.alteration.cat.ui.list.ListItem;

/**
 * Created by blogc on 2016-09-24.
 */
public class ClientService extends Service {
    private DBManager dbWriter;
    private DBManager dbManager;

    private SharedPreferences pref = null;
    private SharedPreferences.Editor editor = null;

    private NotificationManager NotifiM;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Client client = new Client(this);
        JSONObject result = client.getResult();

        NotifiM = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        pref = getSharedPreferences("pref", MODE_PRIVATE);
        editor = pref.edit();

        dbWriter = new DBManager(this, "History.db", null, 1);
        dbWriter.clear();
        dbWriter.getWritableDatabase();

        dbManager = new DBManager(this, "History.db", null, 1);
        dbManager.getReadableDatabase();

        try {
            HistoryParser historyParser = new HistoryParser(result);
            HistoryAnalyzer historyAnalyzer = null;

            historyAnalyzer = new HistoryAnalyzer(historyParser.getIndexofHistory(0), this);

            for(int i = 1; i < historyParser.length(); i++) {
                historyAnalyzer.addHistory(historyParser.getIndexofHistory(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        GregorianCalendar calendar = new GregorianCalendar();

        String date = String.format(Locale.KOREAN, "%04d/%02d/%02d",
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH)+1, calendar.get(Calendar.DATE));

        String dbResult = dbManager.get(date);
        int times = 0;
        int useTimes = 0;

        if (dbResult != null) {
            for(String item : dbResult.split(" ")) {
                times += 1;
                useTimes += Integer.parseInt(item);
            }
        }

        Log.d("ClientService", "Times : " + times);
        Log.d("ClientSerivce", "useTimes : " + useTimes);


        // check Alarm
        //
        //
        boolean noti = pref.getBoolean("alarmDisabled", false);
        int timesMin = pref.getInt("times_min", 0);
        int timesMax = pref.getInt("times_max", 0);

        int timeMin = pref.getInt("time_min", 0);
        int timeMax = pref.getInt("time_max", 0);
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);

        Log.d("timesMin", String.valueOf(timesMin));
        Log.d("timesMax", String.valueOf(timesMax));
        Log.d("timeMin", String.valueOf(timeMin));
        Log.d("timeMax", String.valueOf(timeMax));
        Log.d("noti", String.valueOf(noti));

        if (!noti) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
                if (pm.isInteractive())  {

                    Intent sIntent = new Intent(ClientService.this, MainActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(ClientService.this, 0, sIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                    Notification Notifi = new Notification.Builder(getApplicationContext())
                            .setContentTitle("고양이")
                            .setContentText("고양이가 설정한 조건에 충족하지 않습니다.")
                            .setSmallIcon(R.drawable.alarm_cat)
                            .setContentIntent(pendingIntent)
                            .build();

                    NotifiM.notify( 777 , Notifi);
                } else {
                    if (!(useTimes >= timeMin && useTimes <= timeMax)) {
                        Intent popupIntent = new Intent(this, AlarmActivity.class);
                        popupIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(popupIntent);
                    } else if (!(times >= timesMin && times <= timesMax)) {
                        Intent popupIntent = new Intent(this, AlarmActivity.class);
                        popupIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(popupIntent);
                    }
                }
            } else {
                if (pm.isScreenOn())  {

                    Intent sIntent = new Intent(ClientService.this, MainActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(ClientService.this, 0, sIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                    Notification Notifi = new Notification.Builder(getApplicationContext())
                            .setContentTitle("고양이")
                            .setContentText("고양이가 설정한 조건에 충족하지 않습니다.")
                            .setSmallIcon(R.drawable.alarm_cat)
                            .setContentIntent(pendingIntent)
                            .build();

                    NotifiM.notify( 777 , Notifi);
                } else {
                    if (!(useTimes >= timeMin && useTimes <= timeMax)) {
                        Log.d("asdf", "Start Alarm");
                        Intent popupIntent = new Intent(this, AlarmActivity.class);
                        popupIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(popupIntent);
                    } else if (!(times >= timesMin && times <= timesMax)) {
                        Log.d("asdf", "Start Alarm");
                        Intent popupIntent = new Intent(this, AlarmActivity.class);
                        popupIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(popupIntent);
                    }
                }
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
