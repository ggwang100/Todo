package com.example.gwangtae.todo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class read extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read);

    }

    public void onClick(View view) {
        int id = view.getId();

        if (id == R.id.btn_update) { // 추가 버튼

            Intent intent = new Intent(this, edit_record.class);
            startActivity(intent);
        }

        if (id == R.id.btn_delete) { // 추가 버튼

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}
