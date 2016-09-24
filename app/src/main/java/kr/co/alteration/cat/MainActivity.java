package kr.co.alteration.cat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import kr.co.alteration.cat.ui.CalendarActivity;

public class MainActivity extends AppCompatActivity {
    private static boolean alarmStatus = false;
    private final String TAG = "MainActivity";
    private Switch turnSwitch;
    private Button btnHistory = null;
    private Button btnNotfiSetting = null;

    private SharedPreferences pref = null;
    private SharedPreferences.Editor editor = null;

    public static boolean getAlarmStatus() {
        return alarmStatus;
    }

    public static void setAlarmStatus(boolean status) {
        alarmStatus = status;
    }

    private void initView() {
        this.turnSwitch = (Switch) findViewById(R.id.turn_alarm);
        pref = getSharedPreferences("pref", MODE_PRIVATE);
        editor = pref.edit();


        if (pref.getBoolean("alarmDisabled", false)) {
            turnSwitch.setChecked(true);
            alarmStatus = true;
            Log.d(TAG, "Alarm is disabled");

        } else {
            turnSwitch.setChecked(false);
            alarmStatus = false;
            Log.d(TAG, "Alarm is enabled");
        }

        turnSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {

                if (checked) {
                    Log.d(TAG, "Alarm is disabled");
                    editor.putBoolean("alarmDisabled", true);
                    alarmStatus = true;
                } else {
                    Log.d(TAG, "Alarm is enabled");
                    editor.putBoolean("alarmDisabled", false);
                    alarmStatus = false;
                }
            }
        });

        btnHistory = (Button) findViewById(R.id.btn_calendar);
        btnNotfiSetting = (Button) findViewById(R.id.btn_notification_menu);

        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CalendarActivity.class));
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }
}
