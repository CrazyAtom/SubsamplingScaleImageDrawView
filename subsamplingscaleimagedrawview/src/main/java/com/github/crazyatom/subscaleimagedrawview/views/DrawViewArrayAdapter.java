package com.github.crazyatom.subscaleimagedrawview.views;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import com.github.crazyatom.subscaleimagedrawview.drawviews.BaseDrawView;

/**
 * Created by hangilit on 2017. 7. 19..
 */

public class DrawViewArrayAdapter extends ArrayAdapter<BaseDrawView> {

    private final int resource;
    private ArrayList<BaseDrawView> items = new ArrayList<>();

    public DrawViewArrayAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
        this.resource = resource;
    }

    @Override
    public void add(@Nullable BaseDrawView object) {
        items.add(object);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        BaseDrawView drawView = getItem(position);
        if (drawView != null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            TextView textView = (TextView) layoutInflater.inflate(resource, null);
            textView.setText(drawView.getName(false));
            return textView;
        }
        return convertView;
    }

    @Nullable
    @Override
    public BaseDrawView getItem(int position) {
        return items.get(position);
    }

    @Override
    public int getCount() {
        return items.size();
    }
}
