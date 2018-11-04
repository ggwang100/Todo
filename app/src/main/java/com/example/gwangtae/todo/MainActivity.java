package com.example.gwangtae.todo;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import DB.DBAdapter;
import DB.DBHelper;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DBHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    Cursor cursor;
    DBAdapter dbAdapter;

    String title, content, date;
    ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        list = (ListView) findViewById(R.id.list);
        selectDB();
        dbAdapter.notifyDataSetChanged();

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                cursor.moveToPosition(position);
                Intent READ = new Intent(getApplicationContext(), read.class);
                READ.putExtra("ID", cursor.getString(cursor.getColumnIndex("_id")));
                READ.putExtra("TITLE", cursor.getString(cursor.getColumnIndex("TITLE")));
                READ.putExtra("CONTENT", cursor.getString(cursor.getColumnIndex("CONTENT")));
                READ.putExtra("CREATE_DATE", cursor.getString(cursor.getColumnIndex("CREATE_DATE")));
                READ.putExtra("ALARM", cursor.getString(cursor.getColumnIndex("ALARM")));
                startActivityForResult(READ, 1000);
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

        if(id == R.id.btn_add){
            Intent record = new Intent(this, edit_record.class);
            startActivityForResult(record, 1000);
        }
    }

    public void selectDB(){

        dbHelper = new DBHelper(this);
        sqLiteDatabase = dbHelper.getWritableDatabase();

        String SQL = "SELECT * FROM TODO";
        cursor = sqLiteDatabase.rawQuery(SQL, null);

        startManagingCursor(cursor);
        dbAdapter = new DBAdapter(this, cursor);
        list.setAdapter(dbAdapter);
    }

    // 2018-11-04 추가
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1000 && resultCode == 1){
            String TITLE = data.getStringExtra("TITLE");
            String CONTENT = data.getStringExtra("CONTENT");
            String ALARM = data.getStringExtra("ALARM");

            Toast.makeText(this, ALARM, Toast.LENGTH_SHORT).show();

            dbHelper.onInsert(TITLE, CONTENT, ALARM);

            list.setAdapter(dbAdapter);
            dbAdapter.changeCursor(cursor);
        }

        if(requestCode == 1000 && resultCode == 2){
            String ID = data.getStringExtra("ID");

            dbHelper.onDelete(Integer.parseInt(ID));

            list.setAdapter(dbAdapter);
            dbAdapter.changeCursor(cursor);
        }
    }
}
