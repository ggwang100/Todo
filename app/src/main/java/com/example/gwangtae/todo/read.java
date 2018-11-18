package com.example.gwangtae.todo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class read extends AppCompatActivity{

    TextView TITLE, CONTENT, ALARM_DATE, CREATE_DATE, ALARM_TIME;
    String UPDATE_ALARM_DATE, UPDATE_ALARM_TIME, UPDATE_TITLE;
    Intent data;

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


        TITLE.setText("제목 : " + data.getStringExtra("TITLE"));
        CONTENT.setText(data.getStringExtra("CONTENT"));
        ALARM_DATE.setText("알람 날짜 : " + data.getStringExtra("ALARM_DATE"));
        ALARM_TIME.setText("시간 : : " + data.getStringExtra("ALARM_TIME"));
        CREATE_DATE.setText("작성 날짜 : " + data.getStringExtra("CREATE_DATE"));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(10, intent);
        finish();
        super.onBackPressed();
    }

    public void onClick(View view) {
        int id = view.getId();
        Intent intent = new Intent(this, edit_record.class);
        if (id == R.id.btn_update) { // 수정 버튼
            if(update_ok) {
                intent.putExtra("MODE", "UPDATE");
                intent.putExtra("ID",data.getStringExtra("ID"));
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
            intent = new Intent();
            intent.putExtra("ID", data.getStringExtra("ID"));
            setResult(2, intent);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1001 && resultCode == 1){
            update_ok = true;
            UPDATE_TITLE = data.getStringExtra("TITLE");
            UPDATE_ALARM_DATE = data.getStringExtra("ALARM_DATE");
            UPDATE_ALARM_TIME = data.getStringExtra("ALARM_TIME");

            TITLE.setText("제목 : " + UPDATE_TITLE);
            CONTENT.setText(data.getStringExtra("CONTENT"));
            ALARM_DATE.setText("알람 날짜 : " + UPDATE_ALARM_DATE);
            ALARM_TIME.setText("시간 : " + UPDATE_ALARM_TIME);
        }
    }
}
