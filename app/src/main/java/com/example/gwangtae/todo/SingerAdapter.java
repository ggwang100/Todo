package com.example.gwangtae.todo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import Adapter.SingerItem;
import Adapter.SingerItemView;

class SingerAdapter extends BaseAdapter {
    ArrayList<SingerItem> items = new ArrayList<SingerItem>();

    @Override
    public int getCount() {
        return items.size();
    }

    public void addItem(SingerItem item){
        items.add(item);
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Context context = parent.getContext();

        SingerItemView view = new SingerItemView(context);
        SingerItem item = items.get(position);

        view.setTitle(item.getTitle());
        view.setContent(item.getContent());
        view.setDate(item.getDate());

        return view;
    }
}
