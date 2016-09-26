package kr.co.alteration.cat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.HashMap;

import kr.co.alteration.cat.R;
import kr.co.alteration.cat.client.ClientConstants;
import kr.co.alteration.cat.client.DBManager;
import kr.co.alteration.cat.ui.list.ListAdapter;
import kr.co.alteration.cat.ui.list.ListItem;

public class AccessListActivity extends AppCompatActivity {

    private Intent intent = null;
    private String date = null;

    private DBManager dbManager = null;
    private HashMap<String, String> dataOfDay = null;

    private ListView listView_accessList;


    private void init() {
        intent = getIntent();

        date = intent.getStringExtra(ClientConstants.DATA_DATE);
        //dbManager = new DBManager();
        //dataOfDay = dbManager.getDatas(date);

        listView_accessList = (ListView)findViewById(R.id.listview_access_list);
        ListAdapter listAdapter = new ListAdapter();
        listView_accessList.setAdapter(listAdapter);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_list);

        init();

    }
}
