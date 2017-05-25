package com.testone.coffee.testone.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.testone.coffee.testone.R;
import com.testone.coffee.testone.constant.RtspAddressConstant;
import com.testone.coffee.testone.modle.CameraInfoModle;
import com.testone.coffee.testone.modle.data.CameraManager;
import com.testone.coffee.testone.utils.DensityUtil;
import com.testone.coffee.testone.utils.RexUtils;
import com.testone.coffee.testone.utils.TextSizeUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by coffee on 2017/5/16.
 */

public class CameraAddActivity extends BaseActivity implements View.OnClickListener{
    private static int MARGIN_DIP = 10;
    private EditText edit_name;
    private EditText edit_ipAddress;
    private EditText edit_port;
    private EditText edit_backString;
    private EditText edit_username;
    private EditText edit_pwd;
    private RelativeLayout name_edit_layout;
    private RelativeLayout ip_edit_layout;
    private RelativeLayout port_edit_layout;
    private RelativeLayout backstring_edit_layout;
    private RelativeLayout username_edit_layout;
    private RelativeLayout pwd_edit_layout;
    private LinearLayout.LayoutParams name_layout_params;
    private LinearLayout.LayoutParams ip_layout_params;
    private LinearLayout.LayoutParams port_layout_params;
    private LinearLayout.LayoutParams backstring_layout_params;
    private LinearLayout.LayoutParams username_layout_params;
    private LinearLayout.LayoutParams pwd_layout_params;
    private TextView save_btn;
    private View back_btn;
    private List<RelativeLayout> layoutList;
    private List<LinearLayout.LayoutParams> paramsList;
    public static void startPage(Context context){
        Intent intent = new Intent(context,CameraAddActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindView();
        initView();
        setParams();
        back_btn.setOnClickListener(this);
        save_btn.setOnClickListener(this);
    }

    private void initView() {
        TextSizeUtils.calculateTextSizeByDimension(this,
                (TextView) findViewById(R.id.camera_add_activity_title),TextSizeUtils.DEFAULT_MAX_SIZE);
        TextSizeUtils.calculateTextSizeByDimension(this,
                (TextView) findViewById(R.id.camera_add_activity_name_editLayout_label),TextSizeUtils.DEFAULT_MEDIUM_SIZE);
        TextSizeUtils.calculateTextSizeByDimension(this,
                (TextView) findViewById(R.id.camera_add_activity_ip_editLayout_label),TextSizeUtils.DEFAULT_MEDIUM_SIZE);
        TextSizeUtils.calculateTextSizeByDimension(this,
                (TextView) findViewById(R.id.camera_add_activity_port_editLayout_label),TextSizeUtils.DEFAULT_MEDIUM_SIZE);
        TextSizeUtils.calculateTextSizeByDimension(this,
                (TextView) findViewById(R.id.camera_add_activity_backString_editLayout_label),TextSizeUtils.DEFAULT_MEDIUM_SIZE);
        TextSizeUtils.calculateTextSizeByDimension(this,
                (TextView) findViewById(R.id.camera_add_activity_username_editLayout_label),TextSizeUtils.DEFAULT_MEDIUM_SIZE);
        TextSizeUtils.calculateTextSizeByDimension(this,
                (TextView) findViewById(R.id.camera_add_activity_pwd_editLayout_label),TextSizeUtils.DEFAULT_MEDIUM_SIZE);
        TextSizeUtils.calculateTextSizeByDimension(this,save_btn,TextSizeUtils.DEFAULT_MEDIUM_SIZE);

        TextSizeUtils.calculateTextSizeByDimension(this,edit_name,TextSizeUtils.DEFAULT_MIN_SIZE);
        TextSizeUtils.calculateTextSizeByDimension(this,edit_ipAddress,TextSizeUtils.DEFAULT_MIN_SIZE);
        TextSizeUtils.calculateTextSizeByDimension(this,edit_port,TextSizeUtils.DEFAULT_MIN_SIZE);
        TextSizeUtils.calculateTextSizeByDimension(this,edit_backString,TextSizeUtils.DEFAULT_MIN_SIZE);
        TextSizeUtils.calculateTextSizeByDimension(this,edit_username,TextSizeUtils.DEFAULT_MIN_SIZE);
        TextSizeUtils.calculateTextSizeByDimension(this,edit_pwd,TextSizeUtils.DEFAULT_MIN_SIZE);
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
        layoutList.add(4,username_edit_layout);
        layoutList.add(5,pwd_edit_layout);
        paramsList.add(0,name_layout_params);
        paramsList.add(1,ip_layout_params);
        paramsList.add(2,port_layout_params);
        paramsList.add(3,backstring_layout_params);
        paramsList.add(4,username_layout_params);
        paramsList.add(5,pwd_layout_params);
        for (int i = 0; i < paramsList.size(); i++) {
            paramsList.get(i).width = screenWidth/2-2*MARGIN_DIP;
            paramsList.get(i).height = screenHeight/7;
            paramsList.get(i).setMargins(MARGIN_DIP,MARGIN_DIP,MARGIN_DIP,MARGIN_DIP);
            layoutList.get(i).setLayoutParams(paramsList.get(i));
        }
    }

    private void bindView() {
        edit_name = (EditText) findViewById(R.id.camera_add_activity_name_editLayout_edit);
        edit_ipAddress = (EditText) findViewById(R.id.camera_add_activity_ip_editLayout_edit);
        edit_port = (EditText) findViewById(R.id.camera_add_activity_port_editLayout_edit);
        edit_backString = (EditText) findViewById(R.id.camera_add_activity_backString_editLayout_edit);
        edit_username = (EditText) findViewById(R.id.camera_add_activity_username_editLayout_edit);
        edit_pwd = (EditText) findViewById(R.id.camera_add_activity_pwd_editLayout_edit);

        name_edit_layout = (RelativeLayout) findViewById(R.id.camera_add_activity_name_editLayout);
        ip_edit_layout = (RelativeLayout) findViewById(R.id.camera_add_activity_ip_editLayout);
        port_edit_layout = (RelativeLayout) findViewById(R.id.camera_add_activity_port_editLayout);
        backstring_edit_layout = (RelativeLayout) findViewById(R.id.camera_add_activity_backString_editLayout);
        username_edit_layout = (RelativeLayout) findViewById(R.id.camera_add_activity_username_editLayout);
        pwd_edit_layout = (RelativeLayout) findViewById(R.id.camera_add_activity_pwd_editLayout);

        name_layout_params = (LinearLayout.LayoutParams) name_edit_layout.getLayoutParams();
        ip_layout_params = (LinearLayout.LayoutParams) ip_edit_layout.getLayoutParams();
        port_layout_params = (LinearLayout.LayoutParams) port_edit_layout.getLayoutParams();
        backstring_layout_params = (LinearLayout.LayoutParams) backstring_edit_layout.getLayoutParams();
        username_layout_params = (LinearLayout.LayoutParams) username_edit_layout.getLayoutParams();
        pwd_layout_params = (LinearLayout.LayoutParams) pwd_edit_layout.getLayoutParams();
        save_btn = (TextView) findViewById(R.id.camera_add_activity_btnroom_savebtn);
        back_btn = findViewById(R.id.camera_add_activity_backroom);
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
            case R.id.camera_add_activity_backroom:
                finish();
                break;

        }
    }

    private void saveCameraInfo() {
        if(!RexUtils.isTrueIP(edit_ipAddress.getText().toString())){
           Toast.makeText(this,RtspAddressConstant.WRONG_IP_ADDRESS,Toast.LENGTH_SHORT).show();
        }else{
            CameraInfoModle infoModle = new CameraInfoModle(edit_name.getText().toString(),
                    edit_ipAddress.getText().toString(),
                    edit_port.getText().toString(),
                    edit_backString.getText().toString(),edit_username.getText().toString(),edit_pwd.getText().toString());
            CameraManager.getInstance().with(this).using(cacheId).
                    addDatas(infoModle);
            finish();
        }
    }
}
