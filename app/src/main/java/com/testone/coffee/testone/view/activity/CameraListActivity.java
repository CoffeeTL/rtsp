package com.testone.coffee.testone.view.activity;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.testone.coffee.testone.R;
import com.testone.coffee.testone.RtspApplication;
import com.testone.coffee.testone.modle.CameraInfoModle;
import com.testone.coffee.testone.modle.CameraModle;
import com.testone.coffee.testone.modle.data.CameraManager;
import com.testone.coffee.testone.utils.DensityUtil;
import com.testone.coffee.testone.view.ui.flowlayout.FlowLayout;
import com.testone.coffee.testone.view.ui.flowlayout.TagAdapter;
import com.testone.coffee.testone.view.ui.flowlayout.TagFlowLayout;


import java.util.ArrayList;
import java.util.List;


/**
 * Created by coffee on 2017/5/16.
 */

public class CameraListActivity extends BaseActivity implements View.OnClickListener{
    private TextView btn_add;
    private TextView btn_del;
    private TextView btn_watch;
    //@BindView(R.id.camera_list_activity_grid)GridView gridView;
    //private CameraListAdapter listAdapter;
    private TagFlowLayout flowLayout;
    private int current_index = -1;
    private List<CameraModle> modleList;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindView();
        btn_add.setOnClickListener(this);
        btn_del.setOnClickListener(this);
        btn_watch.setOnClickListener(this);
        flowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                current_index = position;
                return true;
            }
        });

    }

    private void bindView() {
        btn_add = (TextView) findViewById(R.id.camera_list_activity_btnroom_addBtn);
        btn_del = (TextView) findViewById(R.id.camera_list_activity_btnroom_delBtn);
        btn_watch = (TextView) findViewById(R.id.camera_list_activity_btnroom_watchBtn);
        flowLayout = (TagFlowLayout) findViewById(R.id.camera_list_activity_tagLayout);
    }

    @Override
    public int getLayoutId() {
        return R.layout.camera_list_activity;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.camera_list_activity_btnroom_addBtn:
                CameraAddActivity.startPage(this);
                break;
            case R.id.camera_list_activity_btnroom_delBtn:
                if(current_index > -1){
                    AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                    dialog.setTitle(R.string.dialog_title);
                    dialog.setMessage("确定要删除选中摄像头吗?");
                    dialog.setNegativeButton(R.string.dialog_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener(){

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            modleList.remove(current_index);
                            CameraManager.getInstance().with(CameraListActivity.this).
                                    using(cacheId).deleteOne(modleList.get(current_index).getRtsp_url());
                            refresh();
                        }
                    });
                    dialog.show();
                }else{
                    Toast.makeText(this,"请选中一个摄像头",Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.camera_list_activity_btnroom_watchBtn:
                if(current_index > -1){
                    CameraPlayActivity.startPage(this,current_index);
                }else{
                    Toast.makeText(this,"请选中一个摄像头",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void refresh() {
        flowLayout.setAdapter(new TagAdapter<CameraModle>(modleList) {

            @Override
            public View getView(FlowLayout parent, int position, CameraModle s) {
                View tv =  LayoutInflater.from(CameraListActivity.this)
                        .inflate(R.layout.camera_list_item,
                                flowLayout, false);
                TextView name = (TextView) tv.findViewById(R.id.camera_list_item_name);
                name.setText(modleList.get(position).getCamera_name());
                LinearLayout main = (LinearLayout) tv.findViewById(R.id.camera_list_item_main);
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) main.getLayoutParams();
                params.width = DensityUtil.getDeviceInfo(CameraListActivity.this)[0]/4;
                params.height = DensityUtil.getDeviceInfo(CameraListActivity.this)[1]/4;
                main.setLayoutParams(params);
                return tv;
            }
        });
    }

    //private List<CameraInfoModle> infoList;
    @Override
    protected void onResume() {
        super.onResume();
        //modleList = CameraData.getInstance().with(this).getDatas();
        modleList = CameraManager.getInstance().with(this).using(cacheId).getDatas();
        if(modleList !=null  && modleList.size() != 0){
            for (CameraModle infoModle : modleList) {
                Log.i("cameraInfo",infoModle.getRtsp_url());
            }
            refresh();
        }
    }
}
