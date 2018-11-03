package com.example.gwangtae.todo;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class edit_record extends AppCompatActivity {

    EditText edit_title, edit_content;
    TextView text_alarm;
    int YEAR, MONTH, DAY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record);

        edit_content = (EditText) findViewById(R.id.edit_content);
        edit_title = (EditText) findViewById(R.id.edit_title);
        text_alarm = (TextView) findViewById(R.id.text_alarm);

        YEAR = Calendar.getInstance().get(Calendar.YEAR); // 현재 년도를 가져온다.
        MONTH = Calendar.getInstance().get(Calendar.MONTH); // 현재 월을 가져온다. 기본으로 0월부터 11월까지 설정되어 있어 +1 증가해줘야 1월부터 12월까지 표기된다.
        DAY = Calendar.getInstance().get(Calendar.DAY_OF_MONTH); // 현재 일을 가져온다.
    }

    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() { // 다이얼로그 선택했으면 해당 리스너를 불러올 수 있다.
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Toast.makeText(getApplicationContext(), year + "년" +( monthOfYear + 1) + "월" + dayOfMonth +"일", Toast.LENGTH_SHORT).show();
            text_alarm.setText(year + "년" +( monthOfYear + 1)+ "월" + dayOfMonth +"일");
            // 1. 메인에서 YEAR, MONTH, DAY에서 현재 날짜를 가져온 후
            // 2. onClick 메소드에서 날짜 선택 버튼을 누르면 다이럴로그 창이 띄우는데 메인에서 구한 현재날짜를 다이얼로그에 보여준다.
            // 3. 다이얼로그 창에서 보여준 날짜를 임의로 선택해서 OK를 누를 꼉우 Toast 메시지를 뿌려주면서 임의로 선택한 날짜를 메시지로 통해 볼 수 있다.
        }
    };

    public void onClick(View view){
        int id = view.getId();

        if(id == R.id.btn_alarm){
            DatePickerDialog dialog = new DatePickerDialog(this, listener, YEAR, MONTH, DAY); // 날짜 선택할 수 있는 다이얼로그창을 띄운다.
            dialog.show(); // 다이얼로그 띄우기
        }

        if (id == R.id.bottom) { // 추가 버튼
             Intent intent = new Intent(this, MainActivity.class);
              startActivity(intent);
        }
    }
}

