package com.github.crazyatom.subsamplingscaleimagedrawview.views;

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

import com.github.crazyatom.subsamplingscaleimagedrawview.R;
import com.github.crazyatom.subsamplingscaleimagedrawview.drawviews.BaseDrawView;
import com.github.crazyatom.subsamplingscaleimagedrawview.util.Utillity;

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
            View view = layoutInflater.inflate(resource, null);
            TextView name = (TextView) view.findViewById(R.id.text1);
            name.setText(drawView.getName(false));
            TextView info = (TextView) view.findViewById(R.id.text2);
            info.setText("by" + drawView.getCreater() + " " + Utillity.getTimeString(drawView.getUpdateTime(), null));
            return view;
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
