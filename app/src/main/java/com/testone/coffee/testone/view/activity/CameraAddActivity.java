package com.testone.coffee.testone.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.testone.coffee.testone.R;
import com.testone.coffee.testone.modle.CameraInfoModle;
import com.testone.coffee.testone.modle.CameraModle;
import com.testone.coffee.testone.modle.data.CameraManager;
import com.testone.coffee.testone.utils.DensityUtil;
import com.testone.coffee.testone.utils.RexUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by coffee on 2017/5/16.
 */

public class CameraAddActivity extends BaseActivity implements View.OnClickListener{
    private static int MARGIN_DIP = 10;
    private TextInputEditText edit_name;
    private TextInputEditText edit_ipAddress;
    private TextInputEditText edit_port;
    private TextInputEditText edit_backString;
    private TextInputLayout name_edit_layout;
    private TextInputLayout ip_edit_layout;
    private TextInputLayout port_edit_layout;
    private TextInputLayout backstring_edit_layout;
    private LinearLayout.LayoutParams name_layout_params;
    private LinearLayout.LayoutParams ip_layout_params;
    private LinearLayout.LayoutParams port_layout_params;
    private LinearLayout.LayoutParams backstring_layout_params;
    private TextView save_btn;
    private TextView cancel_btn;
    private List<CameraInfoModle> infoList;
    private List<TextInputLayout> layoutList;
    private List<LinearLayout.LayoutParams> paramsList;
    public static void startPage(Context context){
        Intent intent = new Intent(context,CameraAddActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindView();
        setParams();
        cancel_btn.setOnClickListener(this);
        save_btn.setOnClickListener(this);
        if(infoList == null){
            infoList = new ArrayList<>();
        }
    }
    private int screenWidth;
    private int screenHeight;
    private void setParams() {
        screenWidth = DensityUtil.getDeviceInfo(this)[0];
        screenHeight = DensityUtil.getDeviceInfo(this)[1];
        layoutList = new ArrayList<>();
        paramsList = new ArrayList<>();
        layoutList.add(0,name_edit_layout);
        layoutList.add(1,ip_edit_layout);
        layoutList.add(2,port_edit_layout);
        layoutList.add(3,backstring_edit_layout);
        paramsList.add(0,name_layout_params);
        paramsList.add(1,ip_layout_params);
        paramsList.add(2,port_layout_params);
        paramsList.add(3,backstring_layout_params);
        for (int i = 0; i < paramsList.size(); i++) {
            paramsList.get(i).width = screenWidth/2-2*MARGIN_DIP;
            paramsList.get(i).height = screenHeight/7;
            paramsList.get(i).setMargins(MARGIN_DIP,MARGIN_DIP,MARGIN_DIP,MARGIN_DIP);
            layoutList.get(i).setLayoutParams(paramsList.get(i));
        }
    }

    private void bindView() {
        edit_name = (TextInputEditText) findViewById(R.id.camera_add_activity_editroom_nameroom_edit);
        edit_ipAddress = (TextInputEditText) findViewById(R.id.camera_add_activity_editroom_iproom_edit);
        edit_port = (TextInputEditText) findViewById(R.id.camera_add_activity_editroom_portroom_edit);
        edit_backString = (TextInputEditText) findViewById(R.id.camera_add_activity_editroom_backstringoom_edit);
        name_edit_layout = (TextInputLayout) findViewById(R.id.camera_add_activity_name_editLayout);
        ip_edit_layout = (TextInputLayout) findViewById(R.id.camera_add_activity_ip_editLayout);
        port_edit_layout = (TextInputLayout) findViewById(R.id.camera_add_activity_port_editLayout);
        backstring_edit_layout = (TextInputLayout) findViewById(R.id.camera_add_activity_backstring_editLayout);
        name_layout_params = (LinearLayout.LayoutParams) name_edit_layout.getLayoutParams();
        ip_layout_params = (LinearLayout.LayoutParams) ip_edit_layout.getLayoutParams();
        port_layout_params = (LinearLayout.LayoutParams) port_edit_layout.getLayoutParams();
        backstring_layout_params = (LinearLayout.LayoutParams) backstring_edit_layout.getLayoutParams();
        save_btn = (TextView) findViewById(R.id.camera_add_activity_btnroom_savebtn);
        cancel_btn = (TextView) findViewById(R.id.camera_add_activity_btnroom_cancelbtn);
    }

    @Override
    public int getLayoutId() {
        return R.layout.camera_add_activity;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.camera_add_activity_btnroom_savebtn:
                saveCameraInfo();
                break;
            case R.id.camera_add_activity_btnroom_cancelbtn:
                finish();
                break;
        }
    }

    private void saveCameraInfo() {
        if(TextUtils.equals("",edit_name.getText().toString()) ||
                TextUtils.equals("",edit_ipAddress.getText().toString()) ||
                TextUtils.equals("",edit_backString.getText().toString())){
            Toast.makeText(this,R.string.add_edit_emptyinfo_error_hint,Toast.LENGTH_SHORT).show();
        }else if(!RexUtils.isTrueIP(edit_ipAddress.getText().toString())){
            //Toast.makeText(this,R.string.wrong_edit_info_error_hint,Toast.LENGTH_SHORT).show();
            edit_ipAddress.requestFocus();
            edit_ipAddress.setError("您填的IP地址不正确");

        }else{
            CameraInfoModle infoModle = new CameraInfoModle();
            infoModle.setName(edit_name.getText().toString());
            infoModle.setIPAddress(edit_ipAddress.getText().toString());
            infoModle.setPort(edit_port.getText().toString());
            infoModle.setBackString(edit_backString.getText().toString());
            CameraManager.getInstance().with(this).using(cacheId).
                    addDatas(new CameraModle(infoModle.getName(),infoModle.turnIntoUrl()));
            finish();
        }
    }
}
