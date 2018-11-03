package com.example.gwangtae.todo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class edit_record extends AppCompatActivity {


//    Button button_record;
    EditText edit_title, edit_content;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        edit_content = (EditText) findViewById(R.id.edit_content);
        edit_title = (EditText) findViewById(R.id.edit_title);



    }

    public void onClick(View view){
        int id = view.getId();

        if (id == R.id.bottom) { // 추가 버튼

        }


    }

}

