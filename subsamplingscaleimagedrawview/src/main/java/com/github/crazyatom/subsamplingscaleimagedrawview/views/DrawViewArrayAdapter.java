package com.github.crazyatom.subsamplingscaleimagedrawview.views;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.github.crazyatom.subsamplingscaleimagedrawview.R;
import com.github.crazyatom.subsamplingscaleimagedrawview.drawviews.BaseDrawView;
import com.github.crazyatom.subsamplingscaleimagedrawview.util.Utillity;

/**
 * Created by hangilit on 2017. 7. 19..
 */

public class DrawViewArrayAdapter extends ArrayAdapter<BaseDrawView> {

    private final int resource;
    private Map<BaseDrawView.DrawViewType, ArrayList<BaseDrawView>> map = new HashMap<>();
    private ArrayList<Pair<String, BaseDrawView>> items = new ArrayList<>();

    public DrawViewArrayAdapter(@NonNull Context context, @LayoutRes int resource) {
        super(context, resource);
        this.resource = resource;
    }

    @Override
    public void add(@Nullable BaseDrawView object) {
        if (map.containsKey(object.getType()) == false) {
            map.put(object.getType(), new ArrayList<BaseDrawView>());
        } else {
            map.get(object.getType()).add(object);
        }

        String name = object.getName(false) + " " + (map.get(object.getType()).size() + 1);
        items.add(Pair.create(name, object));

//        items.add(Pair.create(object.getName(false), object));
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        BaseDrawView drawView = getItem(position);
        if (drawView != null) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(resource, null);

            TextView tvName = (TextView) view.findViewById(R.id.text1);
            String name = map.get(drawView.getType()).size() > 0 ? items.get(position).first : drawView.getName(false);
            tvName.setText(name);
//            tvName.setText(drawView.getName(false));

            TextView tvInfo = (TextView) view.findViewById(R.id.text2);
            tvInfo.setText("by" + drawView.getCreater() + " " + Utillity.getTimeString(drawView.getUpdateTime(), null));
            return view;
        }
        return convertView;
    }

    @Nullable
    @Override
    public BaseDrawView getItem(int position) {
        return items.get(position).second;
    }

    @Override
    public int getCount() {
        return items.size();
    }
}
