package com.example.gwangtae.todo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class read extends AppCompatActivity{

    TextView TITLE, CONTENT, ALARM, CREATE_DATE;
    String UPDATE_TITLE, UPDATE_ALARM;
    Intent data;

    boolean update_ok;

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
                intent.putExtra("ALARM", UPDATE_ALARM);
            } else {
                intent.putExtra("MODE", "UPDATE");
                intent.putExtra("ID",data.getStringExtra("ID"));
                intent.putExtra("TITLE",data.getStringExtra("TITLE"));
                intent.putExtra("CONTENT", data.getStringExtra("CONTENT"));
                intent.putExtra("ALARM", data.getStringExtra("ALARM"));
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
            UPDATE_ALARM = data.getStringExtra("ALARM");

            TITLE.setText("제목 : " + UPDATE_TITLE);
            CONTENT.setText(data.getStringExtra("CONTENT"));
            ALARM.setText("알람 : " + UPDATE_ALARM);
        }
    }
}
