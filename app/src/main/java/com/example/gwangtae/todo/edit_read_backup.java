package com.example.gwangtae.todo;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Time;
import java.util.Calendar;

import DB.DBHelper;

public class edit_read_backup extends AppCompatActivity {

    EditText edit_title, edit_content;
    TextView text_alarm_date, text_alarm_time;
    int YEAR, MONTH, DAY, HOUR, MINUTE, ID;
    Intent mode;

    Button btn_add, btn_update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record);

        mode = getIntent();

        YEAR = Calendar.getInstance().get(Calendar.YEAR); // 현재 년도를 가져온다.
        MONTH = Calendar.getInstance().get(Calendar.MONTH); // 현재 월을 가져온다. 기본으로 0월부터 11월까지 설정되어 있어 +1 증가해줘야 1월부터 12월까지 표기된다.
        DAY = Calendar.getInstance().get(Calendar.DAY_OF_MONTH); // 현재 일을 가져온다.
        HOUR = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        MINUTE = Calendar.getInstance().get(Calendar.MINUTE);

        edit_content = (EditText) findViewById(R.id.edit_content);
        edit_title = (EditText) findViewById(R.id.edit_title);
        text_alarm_date = (TextView) findViewById(R.id.text_alarm_date);
        text_alarm_time = (TextView) findViewById(R.id.text_alarm_time);

        if (mode.getStringExtra("MODE").equals("READ")) {
            btn_update = (Button) findViewById(R.id.btn_update);
            btn_update.setVisibility(View.GONE);
        }

        if (mode.getStringExtra("MODE").equals("UPDATE")) {
            btn_add = (Button) findViewById(R.id.btn_add);
            ID = Integer.parseInt(mode.getStringExtra("ID"));
            edit_title.setText(mode.getStringExtra("TITLE"));
            edit_content.setText(mode.getStringExtra("CONTENT"));
            text_alarm_date.setText(mode.getStringExtra("ALARM_DATE"));
            text_alarm_time.setText(mode.getStringExtra("ALARM_TIME"));

            btn_add.setVisibility(View.GONE);
        }
    }

    private DatePickerDialog.OnDateSetListener date_listener = new DatePickerDialog.OnDateSetListener() { // 다이얼로그 선택했으면 해당 리스너를 불러올 수 있다.
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//            String msg = String.format("%d-%d-%d", year,monthOfYear+1, dayOfMonth);
            text_alarm_date.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
            // 1. 메인에서 YEAR, MONTH, DAY에서 현재 날짜를 가져온 후
            // 2. onClick 메소드에서 날짜 선택 버튼을 누르면 다이럴로그 창이 띄우는데 메인에서 구한 현재날짜를 다이얼로그에 보여준다.
            // 3. 다이얼로그 창에서 보여준 날짜를 임의로 선택해서 OK를 누를 꼉우 Toast 메시지를 뿌려주면서 임의로 선택한 날짜를 메시지로 통해 볼 수 있다.
        }
    };

    private TimePickerDialog.OnTimeSetListener time_listener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//            String msg = String.format("%d:%d", hourOfDay, minute);
            text_alarm_time.setText(hourOfDay + ":" + minute);
        }
    };

    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.btn_alarm_date) {
            DatePickerDialog dialog = new DatePickerDialog(this, date_listener, YEAR, MONTH, DAY); // 날짜 선택할 수 있는 다이얼로그창을 띄운다.
            dialog.show(); // 다이얼로그 띄우기
        }

        if (id == R.id.btn_alarm_time) {
            TimePickerDialog dialog = new TimePickerDialog(this, time_listener, HOUR, MINUTE, true); // 날짜 선택할 수 있는 다이얼로그창을 띄운다.
            dialog.show(); // 다이얼로그 띄우기
        }

        if (id == R.id.btn_add) { // 추가 버튼
            Intent intent = new Intent();

            if (text_alarm_date.getText() == null && text_alarm_date.getText().length() == 0) {
                intent.putExtra("TITLE", edit_title.getText().toString());
                intent.putExtra("CONTENT", edit_content.getText().toString());
                intent.putExtra("ALARM_DATE", "null");
                intent.putExtra("ALARM_TIME", "null");
            } else {
                intent.putExtra("TITLE", edit_title.getText().toString());
                intent.putExtra("CONTENT", edit_content.getText().toString());
                intent.putExtra("ALARM_DATE", text_alarm_date.getText());
                intent.putExtra("ALARM_TIME", text_alarm_time.getText());
            }

            setResult(1, intent);
            finish();
        }

        if (id == R.id.btn_update) {
            Intent intent = new Intent();

            intent.putExtra("TITLE", edit_title.getText().toString());
            intent.putExtra("CONTENT", edit_content.getText().toString());
            intent.putExtra("ALARM_DATE", text_alarm_date.getText());
            intent.putExtra("ALARM_TIME", text_alarm_time.getText());
            DBHelper dbHelper = new DBHelper(this);
            dbHelper.onUpdate(ID, edit_title.getText().toString(), edit_content.getText().toString(), text_alarm_date.getText().toString(), text_alarm_time.getText().toString());
            setResult(1, intent);
            finish();
        }
    }
}