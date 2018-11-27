package com.example.gwangtae.todo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class read extends AppCompatActivity{

    TextView TITLE, CONTENT, ALARM_DATE, CREATE_DATE, ALARM_TIME;
    String UPDATE_ALARM_DATE, UPDATE_ALARM_TIME, UPDATE_TITLE;
    Intent data;
    
    String mJsonString;

    private static String TAG = "TODO";

    boolean update_ok;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read);

        data = getIntent();

        TITLE = (TextView) findViewById(R.id.text_title);
        CONTENT = (TextView) findViewById(R.id.text_content);
        ALARM_DATE = (TextView) findViewById(R.id.text_alarm_date);
        ALARM_TIME = (TextView) findViewById(R.id.text_alarm_time);
        CREATE_DATE = (TextView) findViewById(R.id.text_date);
        
        SelectTask selectTask = new SelectTask();
        selectTask.execute("eungho77.ipdisk.co.kr:8000/TODO/select_serach_title.php", data.getStringExtra("TITLE"), data.getStringExtra("NO"));
    }

    public void onClick(View view) {
        int id = view.getId();
        Intent intent = new Intent(this, edit_record.class);
        if (id == R.id.btn_update) { // 수정 버튼
            if(update_ok) {
                intent.putExtra("MODE", "UPDATE");
                intent.putExtra("ID", data.getStringExtra("ID"));
                intent.putExtra("TITLE", UPDATE_TITLE);
                intent.putExtra("CONTENT", CONTENT.getText().toString());
                intent.putExtra("ALARM_DATE", UPDATE_ALARM_DATE);
                intent.putExtra("ALARM_TIME", UPDATE_ALARM_TIME);
            } else {
                intent.putExtra("MODE", "UPDATE");
                intent.putExtra("ID",data.getStringExtra("ID"));
                intent.putExtra("TITLE",data.getStringExtra("TITLE"));
                intent.putExtra("CONTENT", data.getStringExtra("CONTENT"));
                intent.putExtra("ALARM_DATE", data.getStringExtra("ALARM_DATE"));
                intent.putExtra("ALARM_TIME", data.getStringExtra("ALARM_TIME"));
            }

            startActivityForResult(intent, 1001);
        }

        if (id == R.id.btn_delete) { // 삭제 버튼
            DeleteTask delectTask = new DeleteTask();
            delectTask.execute("http://eungho77.ipdisk.co.kr:8000/TODO/delete.php", data.getStringExtra("TITLE"), data.getStringExtra("NO"));
        }
    }
    
    private class SelectTask extends AsyncTask<String, Void, String>{
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(MainActivity.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected String doInBackground(String... params) {
            String serverURL = params[0];
            String title = params[1];
            int id = Integer.parseInt(params[2]);
            
             String data = "TITLE=" + title + "&ID=" + id;

            try {
                URL url = new URL(serverURL);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                httpURLConnection.setReadTimeout(5000);
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.connect();

                OutputStream outputStream = httpURLConnection.getOutputStream();
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

                Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
                Log.e(TAG, errorString);
            }
            else {
                mJsonString = result;
                Log.d("succ", mJsonString);
                showResult();
            }
        }
    }

    private void showResult(){
        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("RESULT");

            for(int i=0;i<jsonArray.length();i++){

                JSONObject item = jsonArray.getJSONObject(i);
                
                TITLE.setText("제목 : " + item.getString("TITLE"));
                CONTENT.setText(item.getString("CONTENT"));
                ALARM_DATE.setText("알람 날짜 : " + data.getStringExtra("ALARM_DATE"));
                ALARM_TIME.setText("시간 : " + data.getStringExtra("ALARM_TIME"));
                CREATE_DATE.setText("작성 날짜 : " + item.getString("CREATE_DATE"));
                
            }
        } catch (JSONException e) {
            Log.d(TAG, "showResult : ", e);
        }
    }

    private class DeleteTask extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;
        String errorString = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = ProgressDialog.show(read.this,
                    "Please Wait", null, true, true);
        }

        @Override
        protected String doInBackground(String... params) {
            String serverURL = params[0];
            String title = params[1];
            int id = Integer.parseInt(params[2]);

            String data = "TITLE=" + title + "&ID=" + id;

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
}
