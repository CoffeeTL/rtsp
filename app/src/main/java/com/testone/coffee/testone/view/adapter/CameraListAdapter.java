package com.testone.coffee.testone.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.testone.coffee.testone.R;
import com.testone.coffee.testone.modle.CameraInfoModle;
import com.testone.coffee.testone.utils.TextSizeUtils;

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
        final int index = position;
        if(view == null){
            view = inflater.inflate(R.layout.camera_list_item,null);
            holder = new NameHolder();
            holder.name = (TextView) view.findViewById(R.id.camera_list_item_name);
            holder.ip = (TextView) view.findViewById(R.id.camera_list_item_ip);
            holder.del_iv = (ImageView) view.findViewById(R.id.camera_list_item_del);
            holder.main = view.findViewById(R.id.camera_list_item_clickroom);
            view.setTag(holder);
        }else{
            holder = (NameHolder) view.getTag();
        }
        TextSizeUtils.calculateTextSizeByDimension(context,holder.name,TextSizeUtils.DEFAULT_MEDIUM_SIZE);
        TextSizeUtils.calculateTextSizeByDimension(context,holder.ip,TextSizeUtils.DEFAULT_MIN_SIZE);
        if(pos == position){
            if(holder.del_iv.getVisibility() == View.GONE){
                holder.del_iv.setVisibility(View.VISIBLE);
            }
        }else{
            if(holder.del_iv.getVisibility() == View.VISIBLE){
                holder.del_iv.setVisibility(View.GONE);
            }
        }
        holder.name.setText(infoModleList.get(position).getName());
        holder.ip.setText("IP : "+infoModleList.get(position).getIPAddress());
        return view;
    }
    public void setInfoModleList(List<CameraInfoModle> infoModleList){
        this.infoModleList = infoModleList;
    }
    class NameHolder{
        public TextView name;
        public TextView ip;
        public ImageView del_iv;
        public View main;
    }
    private int pos;
    public void setLongClickposition(int position){
        this.pos = position;
    }
    public interface OnClickMainListener{
        void clickCamera(int i,String url);
    }
    private OnClickMainListener listener;
    public void setOnClickListener(OnClickMainListener listener){
        this.listener = listener;
    }
}
