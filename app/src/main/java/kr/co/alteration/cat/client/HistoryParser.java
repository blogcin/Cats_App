package kr.co.alteration.cat.client;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by blogc on 2016-09-24.
 */
public class HistoryParser {
    private final String TAG = "HistoryParser";
    private JSONObject history = null;

    public HistoryParser(JSONObject data) throws IOException {
        if (data != null) {
            history = data;
        } else {
            throw new IOException();
        }
    }

    public int length() {
        return history.length();
    }

    public JSONObject getIndexofHistory(int index) {

        if (index >= history.length()) {
            return null;
        }

        try {
            return new JSONObject(history.getString(String.valueOf(index)));
        } catch (JSONException e) {
            Log.d(TAG, "Failed to parse history objects");
            e.printStackTrace();
        }

        return null;
    }

    public String getTypeofElement(JSONObject element) {
        if (element != null) {
            try {
                return element.getString(ClientConstants.DATA_TYPE);
            } catch (JSONException e) {
                Log.d(TAG, "Failed to parse objects");
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    public String getDateOfElement(JSONObject element) {
        if (element != null) {
            try {
                return element.getString(ClientConstants.DATA_DATE);
            } catch (JSONException e) {
                Log.d(TAG, "Failed to parse objects");
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }
}
