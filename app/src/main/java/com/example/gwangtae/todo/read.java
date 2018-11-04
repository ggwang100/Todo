package com.example.gwangtae.todo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class read extends AppCompatActivity{

    int _id;
    TextView TITLE, CONTENT, ALARM, CREATE_DATE;
    Intent data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read);

        data = getIntent();

        TITLE = (TextView) findViewById(R.id.text_title);
        CONTENT = (TextView) findViewById(R.id.text_content);
        ALARM = (TextView) findViewById(R.id.text_alarm);
        CREATE_DATE = (TextView) findViewById(R.id.text_date);


        TITLE.setText("제목 : " + data.getStringExtra("TITLE"));
        CONTENT.setText(data.getStringExtra("CONTENT"));
        ALARM.setText("알람 : " + data.getStringExtra("ALARM"));
        CREATE_DATE.setText("작성 날짜 : " + data.getStringExtra("CREATE_DATE"));
    }

    public void onClick(View view) {
        int id = view.getId();
        Intent intent;
        if (id == R.id.btn_update) { // 수정 버튼
//            intent = new Intent(this, edit_record.class);
        }

        if (id == R.id.btn_delete) { // 추가 버튼
            intent = new Intent();
            intent.putExtra("ID", data.getStringExtra("ID"));
            setResult(2, intent);
            finish();
        }
    }
}
