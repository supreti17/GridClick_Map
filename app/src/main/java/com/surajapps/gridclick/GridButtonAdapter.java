package com.surajapps.gridclick;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by upret on 4/27/2017.
 */

public class GridButtonAdapter extends BaseAdapter {

    private Context context;
    private Integer[] numList = {1,2,3,4};

    public GridButtonAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return numList.length;
    }

    @Override
    public Object getItem(int position) {
        return numList[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Button button;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            button = new Button(context);
            button.setLayoutParams(new GridView.LayoutParams(400, 200));
            button.setPadding(8, 8, 8, 8);
            button.setFocusable(false);
            button.setFocusableInTouchMode(false);
            button.setClickable(false);
        } else {
            button = (Button) convertView;
        }
        if (position == 0) {
            button.setText("Books List");
        } else if (position == 1) {
            button.setText("About");
        } else if (position == 2) {
            button.setText("Location");
        } else {
            button.setText("Button " + numList[position]);
        }
        return button;
    }
}
