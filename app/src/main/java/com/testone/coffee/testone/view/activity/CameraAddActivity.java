package com.testone.coffee.testone.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.testone.coffee.testone.R;
import com.testone.coffee.testone.modle.CameraInfoModle;
import com.testone.coffee.testone.modle.CameraModle;
import com.testone.coffee.testone.modle.data.CameraManager;
import com.testone.coffee.testone.utils.RexUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by coffee on 2017/5/16.
 */

public class CameraAddActivity extends BaseActivity implements View.OnClickListener{
    private EditText edit_name;
    private EditText edit_ipAddress;
    private EditText edit_port;
    private EditText edit_backString;
    private TextView save_btn;
    private TextView cancel_btn;
    private List<CameraInfoModle> infoList;
    private List<CameraModle> modleList;
    public static void startPage(Context context){
        Intent intent = new Intent(context,CameraAddActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindView();
        cancel_btn.setOnClickListener(this);
        save_btn.setOnClickListener(this);
        if(infoList == null){
            infoList = new ArrayList<>();
        }
    }

    private void bindView() {
        edit_name = (EditText) findViewById(R.id.camera_add_activity_editroom_nameroom_edit);
        edit_ipAddress = (EditText) findViewById(R.id.camera_add_activity_editroom_iproom_edit);
        edit_port = (EditText) findViewById(R.id.camera_add_activity_editroom_portroom_edit);
        edit_backString = (EditText) findViewById(R.id.camera_add_activity_editroom_backstringoom_edit);
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
            Toast.makeText(this,"您填的IP地址不正确",Toast.LENGTH_SHORT).show();
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
