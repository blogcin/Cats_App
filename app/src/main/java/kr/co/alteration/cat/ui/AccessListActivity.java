package kr.co.alteration.cat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.util.HashMap;

import kr.co.alteration.cat.R;
import kr.co.alteration.cat.client.ClientConstants;
import kr.co.alteration.cat.client.DBManager;

public class AccessListActivity extends AppCompatActivity {

    private Intent intent = null;
    private String date = null;

    private DBManager dbManager = null;
    private HashMap<String, String> dataOfDay = null;

    private void init() {
        intent = getIntent();

        date = intent.getStringExtra(ClientConstants.DATA_DATE);
        dbManager = new DBManager();

        dataOfDay = dbManager.getDatas(date);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_list);

        init();

    }
}
