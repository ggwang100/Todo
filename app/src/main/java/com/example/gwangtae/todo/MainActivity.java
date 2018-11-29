package com.example.gwangtae.todo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;

import Adapter.SingerItem;
import Service.Broadcast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    AlarmManager alarm_Manager;
    SingerAdapter adapter;

    String id, title, content, date;
    ListView list;

    // 방 목록
    String mJsonString;
    private static String TAG = "TODO";

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        alarm_Manager = (AlarmManager) getSystemService(ALARM_SERVICE);

        list = (ListView) findViewById(R.id.list);
        adapter = new SingerAdapter();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SelectTask selectTask = new SelectTask();
        selectTask.execute("http://eungho77.ipdisk.co.kr:8000/TODO/select.php");

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            SingerItem item = (SingerItem) adapter.getItem(position);

                Intent READ = new Intent(getApplicationContext(), read.class);
                READ.putExtra("ID", item.getId());
                READ.putExtra("TITLE", item.getTitle());
                startActivity(READ);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            SelectTask selectTask = new SelectTask();
            selectTask.execute("http://eungho77.ipdisk.co.kr:8000/TODO/select.php");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void OnClick(View view) {
        int id = view.getId();

        if (id == R.id.btn_add) {
            Intent record = new Intent(this, edit_record.class);
            record.putExtra("MODE", "RECORD");
            startActivity(record);
            finish();
        }
    }

    private class SelectTask extends AsyncTask<String, Void, String> {
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
                if (responseStatusCode == HttpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                } else {
                    inputStream = httpURLConnection.getErrorStream();
                }

                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                StringBuilder sb = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
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

            if (result == null) {

                Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
                Log.e(TAG, errorString);
            } else {
                mJsonString = result;
                Log.d("succ", mJsonString);
                showResult();
            }
        }
    }

    private void showResult() {
        intent = new Intent(getApplicationContext(), Broadcast.class);

        Calendar cal = Calendar.getInstance();

        try {
            JSONObject jsonObject = new JSONObject(mJsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("RESULT"); // 추가


            PendingIntent[] pending_Intent = new PendingIntent[jsonArray.length()];

            adapter.items.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);

                adapter.addItem(new SingerItem(item.getString("NO"), item.getString("TITLE"), item.getString("CONTENT"), item.getString("CREATE_DATE"))); // 추가

                if(!item.getString("HOUR").equals("null") && !item.getString("MIN").equals("null")) {

                    // cal.set(Calendar.YEAR, Integer.parseInt(item.getString("YEAR")));
                    // cal.set(Calendar.MONTH, Integer.parseInt(item.getString("MONTH")));
                    // cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(item.getString("DAY")));
                    cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(item.getString("HOUR")));
                    cal.set(Calendar.MINUTE, Integer.parseInt(item.getString("MIN")));
                    
//                    intent.putExtra("alarm_mode", item.getString("ON"));
//                    intent.putExtra("todo_title", item.getString("TITLE"));
//                    intent.putExtra("todo_content", item.getString("CONTENT"));


                    pending_Intent[i] = PendingIntent.getBroadcast(MainActivity.this, i, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarm_Manager.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pending_Intent[i]);
                }

                // MySQL 칼람명 수정 필요
                // adapter.addItem(new SingerItem(item.getString("NO"), item.getString("TITLE"), item.getString("CONTENT"), item.getString("YEAR") + "-" + item.getString("MONTH") + "-" + item.getString("DAY"))); // 추가
                adapter.notifyDataSetChanged();
                list.setAdapter(adapter);
                
                // 시간 분이 둘 다 null이 아닐 때

            }
        } catch (JSONException e) {

            Log.d(TAG, "showResult : ", e);
        }
    }
}
