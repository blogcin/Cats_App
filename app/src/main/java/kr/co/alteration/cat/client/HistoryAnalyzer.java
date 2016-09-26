package kr.co.alteration.cat.client;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by blogc on 2016-09-24.
 */
public class HistoryAnalyzer {
    private static String basedType = null;

    private static Calendar basedDate = null;
    private final String TAG = "HistoryAnalyzer";

    private DBManager dbManager;

    public HistoryAnalyzer(JSONObject el, Context context) throws IOException {

        dbManager = new DBManager(context, "History.db", null, 1);
        dbManager.getWritableDatabase();

        Date date = new Date();
        date.getTime();
        if (el != null) {
            try {
                basedDate = Calendar.getInstance();

                // %04d-%02d-%02d %02d:%02d:%02d
                String jsonDate = el.getString(ClientConstants.DATA_DATE);

                basedDate = getDataCalendar(jsonDate);
                basedType = el.getString(ClientConstants.DATA_TYPE);
            } catch (JSONException e) {
                Log.d(TAG, "Failed to analyze history");
                e.printStackTrace();
            }
        } else {
            throw new IOException();
        }
    }

    private Calendar getDataCalendar(String jsonDate) {

        // 2016-09-25 12:00:05
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
        formatter.setLenient(false);
        Date date = null;
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));

        try {
            date = formatter.parse(jsonDate);
            calendar.setTime(date);
            calendar.add(Calendar.MONTH, 1);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return calendar;
    }

    // ca2 - ca1
    private int getTimes(Calendar ca1, Calendar ca2) {
        return (int) ((ca2.getTimeInMillis() - ca1.getTimeInMillis()) / 1000);
    }

    private String getDate(Calendar calendar) {
        return String.format(Locale.KOREAN, "%04d/%02d/%02d", calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
    }

    public boolean addHistory(JSONObject el) {
        if (el != null) {
            try {

                el.getString(ClientConstants.DATA_DATE);

                Log.d(TAG, "Received : " + el.getString(ClientConstants.DATA_DATE));
                String type = el.getString(ClientConstants.DATA_TYPE);
                Log.d(TAG, type);
                Log.d(TAG, "previous " + basedType);

                if (type.equals(ClientConstants.DATA_IN)) {
                    if (!basedType.equals(ClientConstants.DATA_OUT)) {
                        return false;
                    }

                    // Add To Database
                    Calendar beforeDate = basedDate;

                    Calendar afterDate = getDataCalendar(el.getString(ClientConstants.DATA_DATE));

                    basedDate = afterDate;
                    basedType = type;

                    return true;
                } else if (type.equals(ClientConstants.DATA_OUT)) {
                    if (!basedType.equals(ClientConstants.DATA_IN)) {
                        return false;
                    }

                    Calendar afterDate = getDataCalendar(el.getString(ClientConstants.DATA_DATE));

                    Log.d(TAG, "Year : " + String.valueOf(afterDate.get(Calendar.YEAR)));
                    Log.d(TAG, "Month : " + String.valueOf(afterDate.get(Calendar.MONTH)));
                    Log.d(TAG, "Date : " + String.valueOf(afterDate.get(Calendar.DATE)));

                    Calendar beforeDate = basedDate;

                    int times = getTimes(beforeDate, afterDate);

                    if (times > 500) {
                        return false;
                    } else {
                        Log.d(TAG, "DB Added : " + getDate(afterDate) + ", " + times);
                        dbManager.add(getDate(afterDate), String.valueOf(times));
                    }
                    basedDate = afterDate;
                    basedType = type;
                    return true;
                }
            } catch (JSONException e) {
                Log.d(TAG, "Failed to analyze history");
                e.printStackTrace();
                return false;
            }
        } else {
            return false;
        }

        return false;
    }

}
