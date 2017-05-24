package com.testone.coffee.testone.view.activity;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.testone.coffee.testone.R;
import com.testone.coffee.testone.RtspApplication;
import com.testone.coffee.testone.modle.CameraInfoModle;
import com.testone.coffee.testone.modle.CameraModle;
import com.testone.coffee.testone.modle.data.CameraManager;
import com.testone.coffee.testone.utils.DensityUtil;
import com.testone.coffee.testone.utils.TextSizeUtils;
import com.testone.coffee.testone.view.adapter.CameraListAdapter;
import com.testone.coffee.testone.view.ui.flowlayout.FlowLayout;
import com.testone.coffee.testone.view.ui.flowlayout.TagAdapter;
import com.testone.coffee.testone.view.ui.flowlayout.TagFlowLayout;


import java.util.ArrayList;
import java.util.List;


/**
 * Created by coffee on 2017/5/16.
 */

public class CameraListActivity extends BaseActivity implements View.OnClickListener{
    private GridView gridView;
    private CameraListAdapter listAdapter;
    private View add_camera_btn;
    private View grid_room;
    private int current_index = -1;
    private List<CameraInfoModle> modleList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindView();
        initView();
        initObject();
        registerListener();

    }

    private void initView() {
        TextSizeUtils.calculateTextSizeByDimension(this,
                (TextView) findViewById(R.id.camera_list_activity_title),TextSizeUtils.DEFAULT_MAX_SIZE);

    }

    private void registerListener() {
        add_camera_btn.setOnClickListener(this);
        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                current_index = position;
                listAdapter.setLongClickposition(position);
                listAdapter.notifyDataSetChanged();
                return true;
            }
        });
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                if(-1 == current_index){
                    CameraPlayActivity.startPage(CameraListActivity.this,position);
                }else if(current_index == position){
                    AlertDialog.Builder dialog = new AlertDialog.Builder(CameraListActivity.this);
                    dialog.setTitle(R.string.dialog_title);
                    dialog.setMessage("确定要删除摄像头 "+modleList.get(position).getName()+" 吗?");
                    dialog.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialog.setPositiveButton(R.string.dialog_sure, new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            CameraManager.getInstance().with(CameraListActivity.this).
                                    using(cacheId).deleteOne(modleList.get(position).turnIntoUrl());
                            modleList.remove(position);
                            backToNormal();
                        }
                    });
                    dialog.show();
                }else {
                    backToNormal();
                }
            }
        });
        grid_room.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(-1 != current_index){
                    backToNormal();
                }
            }
        });
    }
    private void backToNormal(){
        current_index = -1;
        listAdapter.setLongClickposition(current_index);
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(current_index != -1){
                backToNormal();
            }else{
                finish();
            }
        }
        return true;
    }

    private void initObject() {
        listAdapter = new CameraListAdapter(this);
        gridView.setAdapter(listAdapter);
        listAdapter.setLongClickposition(current_index);
        listAdapter.notifyDataSetChanged();
    }

    private void bindView() {
        gridView = (GridView) findViewById(R.id.camera_list_activity_grid);
        add_camera_btn = findViewById(R.id.camera_list_activity_addroom);
        grid_room = findViewById(R.id.camera_list_activity_gridroom);
    }

    @Override
    public int getLayoutId() {
        return R.layout.camera_list_activity;
    }

    @Override
    public void onClick(View v) {
       switch(v.getId()){
           case R.id.camera_list_activity_addroom:
               CameraAddActivity.startPage(this);
               break;
        }

    }



    @Override
    protected void onResume() {
        super.onResume();
        modleList = CameraManager.getInstance().with(this).using(cacheId).getDatas();
        if(modleList !=null  && modleList.size() != 0){
            for (CameraInfoModle infoModle : modleList) {
                Log.i("cameraInfo",infoModle.turnIntoUrl());
            }
            listAdapter.setInfoModleList(modleList);
            listAdapter.notifyDataSetChanged();
        }
    }
}
