package kr.co.alteration.cat.ui;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import kr.co.alteration.cat.R;

public class NotificationSettingActivity extends AppCompatActivity {
    private Button tv_time_apply;
    private Button tv_times_apply;

    private EditText editText_min_sec;
    private EditText editText_max_sec;

    private EditText editText_min_times;
    private EditText editText_max_times;

    private SharedPreferences pref = null;
    private SharedPreferences.Editor editor = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_setting);

        pref = getSharedPreferences("pref", MODE_PRIVATE);
        editor = pref.edit();

        tv_time_apply = (Button)findViewById(R.id.button_time_apply);
        tv_times_apply = (Button)findViewById(R.id.button_times_apply);

        editText_min_sec = (EditText)findViewById(R.id.edittext_min_sec);
        editText_max_sec = (EditText)findViewById(R.id.editText_max_sec);

        editText_max_times = (EditText)findViewById(R.id.edittext_max_times);
        editText_min_times = (EditText)findViewById(R.id.edittext_min_times);


        tv_time_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int min = Integer.parseInt(editText_min_sec.getText().toString());
                int max = Integer.parseInt(editText_max_sec.getText().toString());

                editor.putInt("time_min", min);
                editor.putInt("time_max", max);
                editor.commit();
            }
        });

        tv_times_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int min = Integer.parseInt(editText_min_times.getText().toString());
                int max = Integer.parseInt(editText_max_times.getText().toString());

                editor.putInt("times_min", min);
                editor.putInt("times_max", max);
                editor.commit();
            }
        });
    }
}
