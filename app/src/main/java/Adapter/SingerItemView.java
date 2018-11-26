package Adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.gwangtae.todo.R;

public class SingerItemView extends LinearLayout {

    TextView text_title, text_content, text_date;

    public SingerItemView(Context context) {
        super(context);
        init(context);
    }

    public SingerItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context){
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.list_item, this, true);

        text_title = (TextView) findViewById(R.id.text_title);
        text_content = (TextView) findViewById(R.id.text_content);
        text_date = (TextView) findViewById(R.id.text_date);
    }

    public void setTitle(String title){
        text_title.setText(title);
    }

    public void setContent(String content){
        text_content.setText(content);
    }

    public void setDate(String date){
        text_date.setText(date);
    }
}
