package kr.co.alteration.cat.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import kr.co.alteration.cat.R;
import kr.co.alteration.cat.client.Client;
import kr.co.alteration.cat.client.DBManager;
import kr.co.alteration.cat.client.HistoryAnalyzer;
import kr.co.alteration.cat.client.HistoryParser;
import kr.co.alteration.cat.ui.list.ListAdapter;
import kr.co.alteration.cat.ui.list.ListItem;

public class CalendarActivity extends AppCompatActivity {
    private final String TAG = "CalendarActivity";
    private Button btnPrevious = null;
    private Button btnNext = null;
    private Button btnDatePicker = null;

    private TextView tv_recent_pages = null;
    private ListView listviewDatas = null;
    private ListAdapter listAdapter = null;

    private int indexOfPage = 0;
    private final int PAGES = 7;

    private DBManager dbManager = null;
    private DBManager dbWriter = null;

    private ArrayList<String> dates;

    private ArrayList<ListItem> listItemArrayList;

    private TextView tv_times_sum;
    private TextView tv_dates;

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            dates = new ArrayList<>();
            dbManager = new DBManager(CalendarActivity.this, "History.db", null, 1);
            dbManager.getReadableDatabase();

            Calendar cal = Calendar.getInstance();

            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, monthOfYear+1);
            cal.set(Calendar.DATE, dayOfMonth);

            for(int i = 1; i <= 7; i++) {
                dates.add(String.format(Locale.KOREAN, "%04d/%02d/%02d", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE)));
                cal.add(Calendar.DATE, 1);
            }

            listItemArrayList = new ArrayList<>();

            int times = 0;

            String dbResult = dbManager.get(dates.get(0));

            if (dbResult != null) {
                for(String item : dbResult.split(" ")) {
                    times += 1;
                    ListItem listItem = new ListItem(item);
                    listItemArrayList.add(listItem);
                }
            }

            tv_times_sum.setText("총합 : " + times + "번");
            listAdapter.setListViewItemList(listItemArrayList);
            listAdapter.notifyDataSetChanged();
            indexOfPage = 1;
            tv_recent_pages.setText(indexOfPage + "/" + PAGES);
            tv_dates.setText(dates.get(0));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        Client client = new Client(this);
        JSONObject result = client.getResult();

        dbWriter = new DBManager(this, "History.db", null, 1);
        dbWriter.clear();
        dbWriter.getWritableDatabase();

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

        btnPrevious = (Button) findViewById(R.id.btn_previous);
        btnNext = (Button) findViewById(R.id.btn_next);
        btnDatePicker = (Button) findViewById(R.id.btn_date_picker);
        tv_recent_pages = (TextView)findViewById(R.id.tv_recent_pages);
        tv_times_sum = (TextView)findViewById(R.id.tv_times_sum);
        tv_dates = (TextView)findViewById(R.id.tv_date);

        listviewDatas = (ListView)findViewById(R.id.listview_access_list);
        listAdapter = new ListAdapter();

        listviewDatas.setAdapter(listAdapter);

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (indexOfPage > 1) {
                    indexOfPage -= 1;
                    tv_recent_pages.setText(indexOfPage + "/" + PAGES);

                    listItemArrayList = new ArrayList<>();

                    String dbResult = dbManager.get(dates.get(indexOfPage-1));

                    int times = 0;

                    if (dbResult != null) {
                        for(String item : dbResult.split(" ")) {
                            times += 1;
                            ListItem listItem = new ListItem(item);
                            listItemArrayList.add(listItem);
                        }
                    }

                    tv_times_sum.setText("총합 : " + times + "번");
                    listAdapter.setListViewItemList(listItemArrayList);
                    listAdapter.notifyDataSetChanged();
                    tv_dates.setText(dates.get(indexOfPage-1));
                }

            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (indexOfPage > 0 && indexOfPage < PAGES) {
                    indexOfPage += 1;
                    tv_recent_pages.setText(indexOfPage + "/" + PAGES);

                    listItemArrayList = new ArrayList<>();

                    String dbResult = dbManager.get(dates.get(indexOfPage-1));

                    int times = 0;

                    if (dbResult != null) {
                        for(String item : dbResult.split(" ")) {
                            times += 1;
                            ListItem listItem = new ListItem(item);
                            listItemArrayList.add(listItem);
                        }
                    }

                    tv_times_sum.setText("총합 : " + times + "번");
                    tv_dates.setText(dates.get(indexOfPage-1));
                    listAdapter.setListViewItemList(listItemArrayList);
                    listAdapter.notifyDataSetChanged();
                }
            }
        });

        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GregorianCalendar calendar = new GregorianCalendar();

                new DatePickerDialog(CalendarActivity.this, dateSetListener,
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();


            }
        });


    }


}
