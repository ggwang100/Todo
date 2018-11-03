package com.example.gwangtae.todo;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class DBAdapter extends CursorAdapter {

    public DBAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.list_item, viewGroup, false);

        return v;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        final TextView title = (TextView) view.findViewById(R.id.text_title);
        final TextView content = (TextView) view.findViewById(R.id.text_content);
        final TextView date = (TextView) view.findViewById(R.id.text_date);

        title.setText("제목 : " + cursor.getString(cursor.getColumnIndex("title")));
        content.setText("내용 : " + cursor.getString(cursor.getColumnIndex("content")));
        date.setText("생성 날짜 : " + cursor.getString(cursor.getColumnIndex("create_memo")));
    }
}
