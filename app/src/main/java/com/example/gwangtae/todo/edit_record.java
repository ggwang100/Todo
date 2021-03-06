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

public class edit_record extends AppCompatActivity {

    EditText edit_title, edit_content;
    TextView text_alarm_date, text_alarm_time;
    String ID, alarm_date, alarm_time, hour, min;
    // String year, month, day;
    Intent mode;

    private static String TAG = "TODO";

    Button btn_add, btn_update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record);

        mode = getIntent();
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
            if(mode.getStringExtra("HOUR").equals("null") && mode.getStringExtra("MIN").equals("null")){
                text_alarm_date.setText("");
                text_alarm_time.setText("");
            } else {
                text_alarm_time.setText(mode.getStringExtra("HOUR") + ":" + mode.getStringExtra("MIN"));
            }


            alarm_date = mode.getStringExtra("ALARM_DATE");
            hour = mode.getStringExtra("HOUR");
            min = mode.getStringExtra("MIN");

            btn_add.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private DatePickerDialog.OnDateSetListener date_listener = new DatePickerDialog.OnDateSetListener() { // 다이얼로그 선택했으면 해당 리스너를 불러올 수 있다.
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//            String msg = String.format("%d-%d-%d", year,monthOfYear+1, dayOfMonth);
            // year = String.valueOf(year);
            // month = String.valueOf(monthOfYear+1);
            // day = String.valueOf(dayOfMonth);
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
            DatePickerDialog dialog = new DatePickerDialog(this, date_listener, Calendar.getInstance().get(Calendar.YEAR), Calendar.getInstance().get(Calendar.MONTH), Calendar.getInstance().get(Calendar.DAY_OF_MONTH)); // 날짜 선택할 수 있는 다이얼로그창을 띄운다.
            dialog.show(); // 다이얼로그 띄우기
        }

        if(id == R.id.btn_alarm_time){
            TimePickerDialog dialog = new TimePickerDialog(this, time_listener, Calendar.getInstance().get(Calendar.HOUR_OF_DAY), Calendar.getInstance().get(Calendar.MINUTE), true); // 날짜 선택할 수 있는 다이얼로그창을 띄운다.
            dialog.show(); // 다이얼로그 띄우기
        }

        if (id == R.id.btn_add) { // 추가 버튼
            InsertTask insertTask = new InsertTask();

            // 추가
            if (text_alarm_date.getText() == null && text_alarm_date.getText().length() == 0) {
                insertTask.execute("http://eungho77.ipdisk.co.kr:8000/TODO/insert.php", edit_title.getText().toString(), edit_content.getText().toString(), "", "", "");
                // insertTask.execute("http://eungho77.ipdisk.co.kr:8000/TODO/insert.php", edit_title.getText().toString(), edit_content.getText().toString(), "", "", "", "", "");
            } else {
                insertTask.execute("http://eungho77.ipdisk.co.kr:8000/TODO/insert.php", edit_title.getText().toString(), edit_content.getText().toString(), alarm_date, hour, min);
                // insertTask.execute("http://eungho77.ipdisk.co.kr:8000/TODO/insert.php", edit_title.getText().toString(), edit_content.getText().toString(), year, month, day, hour, min);
            }
        }
        if (id == R.id.btn_update){
            UpdateTask updateTask = new UpdateTask();
            updateTask.execute("http://eungho77.ipdisk.co.kr:8000/TODO/update.php", edit_title.getText().toString(), edit_content.getText().toString(), alarm_date, hour, min, ID);
            // updateTask.execute("http://eungho77.ipdisk.co.kr:8000/TODO/update.php", edit_title.getText().toString(), edit_content.getText().toString(), year, month, day, hour, min, ID);
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
            // String year = params[3];
            // String month = params[4];
            // String day = params[5];
            String hour = params[4];
            String min = params[5];
            // String hour = params[6];
            // String min = params[7];

            String data = "TITLE=" + title + "&CONTENT=" + content + "&ALARM_DATE=" + alarm_date + "&HOUR=" + hour + "&MIN=" + min;
            //String data = "TITLE=" + title + "&CONTENT=" + content + "&YEAR=" + year + "&MONTH=" + month + "&DAY=" + day + "&HOUR=" + hour + "&MIN=" + min;

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
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
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
            // String year = params[3];
            // String month = params[4];
            // String day = params[5];
            String hour = params[4];
            String min = params[5];
            // String hour = params[6];
            // String min = params[7];
            int ID = Integer.parseInt(params[6]);
            // int ID = Integer.parseInt(params[8]);

            String data = "TITLE=" + title + "&CONTENT=" + content + "&ALARM_DATE=" + alarm_date + "&HOUR=" + hour +  "&MIN=" + min + "&ID=" + ID;
            // String data = "TITLE=" + title + "&CONTENT=" + content + "&YEAR=" + year + "&MONTH=" + month + "&DAY=" + day + "&HOUR=" + hour +  "&MIN=" + min + "&ID=" + ID;
            
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

