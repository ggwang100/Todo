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
import java.util.Calendar;

import Adapter.SingerItem;

public class edit_record extends AppCompatActivity {

    EditText edit_title, edit_content;
    TextView text_alarm_date, text_alarm_time;
    String ID, alarm_date, alarm_time, hour, min;
    int YEAR, MONTH, DAY, HOUR, MINUTE;
    Intent mode;

    private static String TAG = "TODO";

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

        if(mode.getStringExtra("MODE").equals("RECORD")){
            btn_update = (Button) findViewById(R.id.btn_update);
            btn_update.setVisibility(View.GONE);
        }

        if(mode.getStringExtra("MODE").equals("UPDATE")){
            btn_add = (Button) findViewById(R.id.btn_add);

            ID = mode.getStringExtra("ID");
            edit_title.setText(mode.getStringExtra("TITLE"));
            edit_content.setText(mode.getStringExtra("CONTENT"));
            text_alarm_date.setText(mode.getStringExtra("ALARM_DATE"));
            text_alarm_time.setText(mode.getStringExtra("HOUR") + ":" + mode.getStringExtra("MIN"));

            alarm_date = mode.getStringExtra("ALARM_DATE");
            hour = mode.getStringExtra("HOUR");
            min = mode.getStringExtra("MIN");

            btn_add.setVisibility(View.GONE);
        }
    }

    private DatePickerDialog.OnDateSetListener date_listener = new DatePickerDialog.OnDateSetListener() { // 다이얼로그 선택했으면 해당 리스너를 불러올 수 있다.
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//            String msg = String.format("%d-%d-%d", year,monthOfYear+1, dayOfMonth);
            alarm_date = year + "-" +( monthOfYear + 1)+ "-" + dayOfMonth;
            text_alarm_date.setText(alarm_date);
            // 1. 메인에서 YEAR, MONTH, DAY에서 현재 날짜를 가져온 후
            // 2. onClick 메소드에서 날짜 선택 버튼을 누르면 다이럴로그 창이 띄우는데 메인에서 구한 현재날짜를 다이얼로그에 보여준다.
            // 3. 다이얼로그 창에서 보여준 날짜를 임의로 선택해서 OK를 누를 꼉우 Toast 메시지를 뿌려주면서 임의로 선택한 날짜를 메시지로 통해 볼 수 있다.
        }
    };

    private TimePickerDialog.OnTimeSetListener time_listener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//            String msg = String.format("%d:%d", hourOfDay, minute);
            hour = String.valueOf(hourOfDay);
            min = String.valueOf(minute);
            alarm_time = hourOfDay + ":" + minute;
            text_alarm_time.setText(alarm_time);
        }
    };

    public void onClick(View view){
        int id = view.getId();

        if(id == R.id.btn_alarm_date){
            DatePickerDialog dialog = new DatePickerDialog(this, date_listener, YEAR, MONTH, DAY); // 날짜 선택할 수 있는 다이얼로그창을 띄운다.
            dialog.show(); // 다이얼로그 띄우기
        }

        if(id == R.id.btn_alarm_time){
            TimePickerDialog dialog = new TimePickerDialog(this, time_listener, HOUR, MINUTE, true); // 날짜 선택할 수 있는 다이얼로그창을 띄운다.
            dialog.show(); // 다이얼로그 띄우기
        }

        if (id == R.id.btn_add) { // 추가 버튼
            InsertTask insertTask = new InsertTask();

            // 추가
            if (text_alarm_date.getText() == null && text_alarm_date.getText().length() == 0) {
                insertTask.execute("http://eungho77.ipdisk.co.kr:8000/TODO/insert.php", edit_title.getText().toString(), edit_content.getText().toString(), "", "", "");
            } else {
                insertTask.execute("http://eungho77.ipdisk.co.kr:8000/TODO/insert.php", edit_title.getText().toString(), edit_content.getText().toString(), alarm_date, hour, min);
            }
        }
        if (id == R.id.btn_update){
            UpdateTask updateTask = new UpdateTask();
            updateTask.execute("http://eungho77.ipdisk.co.kr:8000/TODO/update.php", edit_title.getText().toString(), edit_content.getText().toString(), alarm_date, hour, min, ID);
//            Intent intent = new Intent();
//
//            intent.putExtra("TITLE", edit_title.getText().toString());
//            intent.putExtra("CONTENT", edit_content.getText().toString());
//            intent.putExtra("ALARM_DATE", text_alarm_date.getText());
//            intent.putExtra("ALARM_TIME", text_alarm_time.getText());
//            DBHelper dbHelper = new DBHelper(this);
//            dbHelper.onUpdate(ID, edit_title.getText().toString(), edit_content.getText().toString(), text_alarm_date.getText().toString(), text_alarm_time.getText().toString());
//            setResult(1, intent);
//            finish();
        }
    }

    private class InsertTask extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(edit_record.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected String doInBackground(String... params) {
            String serverURL = params[0];
            String title = params[1];
            String content = params[2];
            String alarm_date = params[3];
            String hour = params[4];
            String min = params[5];

            String data = "TITLE=" + title + "&CONTENT=" + content + "&ALARM_DATE=" + alarm_date + "&HOUR=" + hour + "&MIN=" + min;

            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(data.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }
                bufferedReader.close();

                return sb.toString().trim();

            } catch (Exception e) {
                Log.d(TAG, "GetData : Error ", e);
                errorString = e.toString();

                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();

            if (result == null){
                // 인텐트 실패
            }
            else {
                Intent intent = new Intent();
                setResult(1, intent);
                finish();
            }
        }
    }

    private class UpdateTask extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(edit_record.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected String doInBackground(String... params) {
            String serverURL = params[0];
            String title = params[1];
            String content = params[2];
            String alarm_date = params[3];
            String hour = params[4];
            String min = params[5];
            int ID = Integer.parseInt(params[6]);

            String data = "TITLE=" + title + "&CONTENT=" + content + "&ALARM_DATE=" + alarm_date + "&HOUR=" + hour +  "&MIN=" + min + "&ID=" + ID;

            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(data.getBytes("UTF-8"));
                outputStream.flush();
                outputStream.close();

                int responseStatusCode = httpURLConnection.getResponseCode();
                Log.d(TAG, "response code - " + responseStatusCode);

                InputStream inputStream;
                if(responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                }
                else{
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while((line = bufferedReader.readLine()) != null){
                    sb.append(line);
                }
                bufferedReader.close();

                return sb.toString().trim();

            } catch (Exception e) {
                Log.d(TAG, "GetData : Error ", e);
                errorString = e.toString();

                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            progressDialog.dismiss();

            if (result == null){
                // 인텐트 실패
            }
            else {
                Intent intent = new Intent(getApplicationContext(), read.class);

                intent.putExtra("MODE", "UPDATE");
                intent.putExtra("ID", ID);
                intent.putExtra("TITLE", edit_title.getText().toString());

                startActivity(intent);
                finish();
            }
        }
    }
}

