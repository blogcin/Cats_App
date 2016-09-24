package kr.co.alteration.cat.client;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by blogc on 2016-09-24.
 */
public class HistoryAnalyzer {
    private static String basedType = null;
    private static String basedDate = null;
    private final String TAG = "HistoryAnalyzer";

    public HistoryAnalyzer(JSONObject el) throws IOException {
        if (el != null) {
            try {
                basedDate = el.getString(ClientConstants.DATA_DATE);
                basedDate = el.getString(ClientConstants.DATA_TYPE);
            } catch (JSONException e) {
                Log.d(TAG, "Failed to analyze history");
                e.printStackTrace();
            }
        } else {
            throw new IOException();
        }
    }

    public boolean addHistory(JSONObject el) {
        if (el != null) {
            try {
                el.getString(ClientConstants.DATA_DATE);


                String type = el.getString(ClientConstants.DATA_TYPE);

                if (type.equals(ClientConstants.DATA_IN)) {
                    if (!basedType.equals(ClientConstants.DATA_OUT)) {
                        return false;
                    }

                    // Add To Database
                    String date = el.getString(ClientConstants.DATA_DATE);

                    return true;
                } else if (type.equals(ClientConstants.DATA_OUT)) {
                    if (!basedType.equals(ClientConstants.DATA_IN)) {
                        return false;
                    }

                    // Add To Database
                    String date = el.getString(ClientConstants.DATA_DATE);

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
