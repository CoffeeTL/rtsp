package com.testone.coffee.testone.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.testone.coffee.testone.R;
import com.testone.coffee.testone.modle.CameraInfoModle;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by coffee on 2017/5/16.
 */

public class CameraListAdapter extends BaseAdapter {
    private Context context;
    private List<CameraInfoModle> infoModleList;
    private LayoutInflater inflater;
    public CameraListAdapter(Context context){
        this.context = context;
        inflater = LayoutInflater.from(context);
        infoModleList = new ArrayList<>();
    }
    @Override
    public int getCount() {
        return infoModleList.size();
    }

    @Override
    public Object getItem(int position) {
        return infoModleList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        NameHolder holder = null;
        if(view == null){
            view = inflater.inflate(R.layout.camera_list_item,null);
            holder = new NameHolder();
            holder.name = (TextView) view.findViewById(R.id.camera_list_item_name);
            view.setTag(holder);
        }else{
            holder = (NameHolder) view.getTag();
        }
        holder.name.setText(infoModleList.get(position).getName());
        return view;
    }
    public void setInfoModleList(List<CameraInfoModle> infoModleList){
        this.infoModleList = infoModleList;
    }
    class NameHolder{
        public TextView name;
    }
}
