package com.testone.coffee.testone.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.testone.coffee.testone.R;
import com.testone.coffee.testone.modle.CameraInfoModle;
import com.testone.coffee.testone.modle.data.CameraManager;
import com.testone.coffee.testone.utils.DensityUtil;
import com.testone.coffee.testone.utils.TextSizeUtils;
import com.testone.coffee.testone.utils.TimeUtils;
import com.testone.coffee.testone.view.ui.LoadingDialog;

import org.videolan.libvlc.EventHandler;
import org.videolan.libvlc.IVideoPlayer;
import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.LibVlcException;

import java.io.File;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by coffee on 2017/6/1.
 */

public class CameraVlcActivity extends BaseActivity  implements SurfaceHolder.Callback, IVideoPlayer,View.OnClickListener {
    @Override
    public int getLayoutId() {
        return R.layout.camera_vlc_activity;
    }

    private SurfaceView mSurfaceView;
    private LibVLC mMediaPlayer;
    private SurfaceHolder mSurfaceHolder;
    private TextView name_label;
    private TextView time_label;
    private CardView card_capture_room;
    private CardView card_record_room;
    private CardView card_lock_room;
    private CardView card_next_room;
    private CardView card_back_room;
    private TextView card_capture_tv;
    private TextView card_record_tv;
    private TextView card_lock_tv;
    private TextView card_next_tv;
    private TextView card_back_tv;
    private RelativeLayout btnplate;
    private View main;
    private FrameLayout.LayoutParams params;

    private int mVideoHeight;
    private int mVideoWidth;
    private int mVideoVisibleHeight;
    private int mVideoVisibleWidth;
    private int mSarNum;
    private int mSarDen;

    private int total_len;

    private List<CameraInfoModle> modleList;
    private int current_index = 0;
    private LoadingDialog loadingDialog;
    private boolean isBtnShow;
    private Timer timer;
    private TimerTask task;

    public static void startPage(Context context, int current_index){
        Intent intent = new Intent(context,CameraVlcActivity.class);
        intent.putExtra("index",current_index);
        context.startActivity(intent);
    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindView();
        registerListener();
        initView();
        isBtnShow = true;
        initData();
    }

    private void initData() {
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.setFormat(PixelFormat.RGBX_8888);
        mSurfaceHolder.addCallback(this);

        EventHandler em = EventHandler.getInstance();
        em.addHandler(mVlcHandler);

        timer = new Timer();
        task = new TimerTask(){
            @Override
            public void run() {
                Message msg = mHandler.obtainMessage();
                msg.what = HANDLER_TIMER_TASK;
                mHandler.sendMessage(msg);
            }
        };
        timer.schedule(task,1000,1000);
        modleList = CameraManager.getInstance().with(this).using(cacheId).getDatas();
        current_index = getIntent().getIntExtra("index",0);
        total_len = modleList.size();

        this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        mSurfaceView.setKeepScreenOn(true);
        if(modleList != null && modleList.size() > 0){
            playVideo(current_index);
        }
    }
    private void playVideo(int pos){
        loadingDialog.show();
        loading_state = 1;
        if(mMediaPlayer != null){
            mMediaPlayer.stop();
            mMediaPlayer.destroy();
        }
        try {
            mMediaPlayer = LibVLC.getInstance();
            mMediaPlayer.init(this);
        } catch (LibVlcException e) {
            e.printStackTrace();
        }
        name_label.setText(modleList.get(pos).getName());
        mMediaPlayer.playMRL(modleList.get(pos).turnIntoUrl());
    }

    private void bindView() {
        mSurfaceView = (SurfaceView) findViewById(R.id.camera_vlc_activity_surface);
        name_label = (TextView) findViewById(R.id.camera_vlc_activity_nameLabel);
        time_label = (TextView) findViewById(R.id.camera_vlc_activity_timeLabel);
        btnplate = (RelativeLayout) findViewById(R.id.camera_vlc_activity_btnplate);
        card_capture_room = (CardView) findViewById(R.id.camera_vlc_activity_btnroom_captureroom);
        card_record_room = (CardView) findViewById(R.id.camera_vlc_activity_btnroom_recordroom);
        card_lock_room = (CardView) findViewById(R.id.camera_vlc_activity_btnroom_lockroom);
        card_next_room = (CardView) findViewById(R.id.camera_vlc_activity_btnroom_nextroom);
        card_back_room = (CardView) findViewById(R.id.camera_vlc_activity_btnroom_backroom);
        card_capture_tv = (TextView) findViewById(R.id.camera_vlc_activity_btnroom_captureroom_tv);
        card_record_tv = (TextView) findViewById(R.id.camera_vlc_activity_btnroom_recordroom_tv);
        card_lock_tv = (TextView) findViewById(R.id.camera_vlc_activity_btnroom_lockroom_tv);
        card_next_tv = (TextView) findViewById(R.id.camera_vlc_activity_btnroom_nextroom_tv);
        card_back_tv = (TextView) findViewById(R.id.camera_vlc_activity_btnroom_backroom_tv);
        main = findViewById(R.id.camera_vlc_activity_main);
        TextSizeUtils.calculateTextSizeByDimension(this,name_label,TextSizeUtils.DEFAULT_MIN_SIZE);
        TextSizeUtils.calculateTextSizeByDimension(this,time_label,TextSizeUtils.DEFAULT_MIN_SIZE);
        TextSizeUtils.calculateTextSizeByDimension(this,card_capture_tv,TextSizeUtils.DEFAULT_MIN_SIZE);
        TextSizeUtils.calculateTextSizeByDimension(this,card_record_tv,TextSizeUtils.DEFAULT_MIN_SIZE);
        TextSizeUtils.calculateTextSizeByDimension(this,card_lock_tv,TextSizeUtils.DEFAULT_MIN_SIZE);
        TextSizeUtils.calculateTextSizeByDimension(this,card_next_tv,TextSizeUtils.DEFAULT_MIN_SIZE);
        TextSizeUtils.calculateTextSizeByDimension(this,card_back_tv,TextSizeUtils.DEFAULT_MIN_SIZE);
    }
    private void registerListener() {
        card_capture_room.setOnClickListener(this);
        card_record_room.setOnClickListener(this);
        card_lock_room.setOnClickListener(this);
        card_next_room.setOnClickListener(this);
        card_back_room.setOnClickListener(this);
        main.setOnClickListener(this);
    }
    private void initView() {
        params = (FrameLayout.LayoutParams) btnplate.getLayoutParams();
        params.width = DensityUtil.getDeviceInfo(this)[0]/5;
        btnplate.setLayoutParams(params);
        loadingDialog = new LoadingDialog(this);
        card_lock_tv.setTextColor(0xffcccccc);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mSurfaceView.setKeepScreenOn(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            EventHandler em = EventHandler.getInstance();
            em.removeHandler(mVlcHandler);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        setSurfaceSize(mVideoWidth, mVideoHeight, mVideoVisibleWidth, mVideoVisibleHeight, mSarNum, mSarDen);
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.i("prepared","surfaceCreated");
        if (mMediaPlayer != null) {
            mSurfaceHolder = holder;
            mMediaPlayer.attachSurface(holder.getSurface(), this);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i("prepared","surfaceChanged"+format);
        mSurfaceHolder = holder;
        if (mMediaPlayer != null) {
            mMediaPlayer.attachSurface(holder.getSurface(), this);//, width, height
        }
        if (width > 0) {
            mVideoHeight = height;
            mVideoWidth = width;
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i("prepared","surfaceDestroyed");
        if (mMediaPlayer != null) {
            mMediaPlayer.detachSurface();
        }
    }

    @Override
    public void setSurfaceSize(int width, int height, int visible_width, int visible_height, int sar_num, int sar_den) {
        mVideoHeight = height;
        mVideoWidth = width;
        mVideoVisibleHeight = visible_height;
        mVideoVisibleWidth = visible_width;
        mSarNum = sar_num;
        mSarDen = sar_den;
        mHandler.removeMessages(HANDLER_SURFACE_SIZE);
        mHandler.sendEmptyMessage(HANDLER_SURFACE_SIZE);
    }

    private static final int HANDLER_BUFFER_START = 1;
    private static final int HANDLER_BUFFER_END = 2;
    private static final int HANDLER_SURFACE_SIZE = 3;
    private static final int HANDLER_TIMER_TASK = 4;
    private int loading_state = 0;

    private Handler mVlcHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg == null || msg.getData() == null)
                return;

            switch (msg.getData().getInt("event")) {
                case EventHandler.MediaPlayerPositionChanged:
                    if(loading_state == 1){
                        loading_state = 0;
                        loadingDialog.dismiss();
                    }
                    break;
                case EventHandler.MediaPlayerPlaying:
                    Log.i("prepared","MediaPlayerPlaying");
                    mHandler.removeMessages(HANDLER_BUFFER_END);
                    mHandler.sendEmptyMessage(HANDLER_BUFFER_END);
                    break;
                case EventHandler.MediaPlayerEndReached:
                    //播放完成
                    break;
            }

        }
    };

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HANDLER_BUFFER_START:
                    Log.i("prepared","HANDLER_BUFFER_START");
                    break;
                case HANDLER_BUFFER_END:
                    Log.i("prepared","HANDLER_BUFFER_END");
                    break;
                case HANDLER_SURFACE_SIZE:
                    Log.i("prepared","HANDLER_SURFACE_SIZE");
                    changeSurfaceSize();
                    break;
                case HANDLER_TIMER_TASK:
                    time_label.setText(TimeUtils.getTime(System.currentTimeMillis()));
                    break;
            }
        }
    };

    private void changeSurfaceSize() {
        mSurfaceHolder.setFixedSize(mVideoWidth, mVideoHeight);
        ViewGroup.LayoutParams lp = mSurfaceView.getLayoutParams();
        lp.width = DensityUtil.getDeviceInfo(this)[0];
        lp.height = DensityUtil.getDeviceInfo(this)[1];
        mSurfaceView.setLayoutParams(lp);
        mSurfaceView.invalidate();
    }

    private String sdPath = "";
    private boolean isRecording;
    private int index = 1;
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.camera_vlc_activity_btnroom_captureroom:
                String mPath = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+TimeUtils.getCurrentDate()+"_"+modleList.get(current_index).getName() + ".png";
                if(mMediaPlayer.takeSnapShot(mPath,DensityUtil.getDeviceInfo(this)[0],DensityUtil.getDeviceInfo(this)[1])){
                    Toast.makeText(this,"截图已经成功保存在"+mPath,Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this,"截图失败,请稍后重试",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.camera_vlc_activity_btnroom_recordroom:
                if(TextUtils.equals("",sdPath)){
                    sdPath += Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+TimeUtils.getCurrentDate()+"_"+modleList.get(current_index).getName();
                }
                if(mMediaPlayer.videoIsRecording()){
                    if(mMediaPlayer.videoRecordStop()){
                        card_record_tv.setText(R.string.play_btn_record);
                        Toast.makeText(this,"录像已经保存在"+sdPath,Toast.LENGTH_SHORT).show();
                        sdPath = "";
                    }
                }else{
                    if(mMediaPlayer.videoRecordStart(sdPath) && index == 2){
                        card_record_tv.setText(R.string.stop_btn_record);
                    }
                    if(2 != index){
                        index ++;
                    }
                }
                break;
            case R.id.camera_vlc_activity_btnroom_nextroom:
                current_index ++;
                if(current_index >= total_len){
                    current_index = 0;
                }
                loadingDialog.show();
                playVideo(current_index);
                break;
            case R.id.camera_vlc_activity_main:
                if(isBtnShow){
                    isBtnShow = false;
                    if(name_label.getVisibility() == View.VISIBLE){
                        name_label.setVisibility(View.GONE);
                    }
                    if(time_label.getVisibility() == View.VISIBLE){
                        time_label.setVisibility(View.GONE);
                    }
                    if(btnplate.getVisibility() == View.VISIBLE){
                        btnplate.setVisibility(View.GONE);
                    }
                }else{
                    isBtnShow = true;
                    if(name_label.getVisibility() == View.GONE){
                        name_label.setVisibility(View.VISIBLE);
                    }
                    if(time_label.getVisibility() == View.GONE){
                        time_label.setVisibility(View.VISIBLE);
                    }
                    if(btnplate.getVisibility() == View.GONE){
                        btnplate.setVisibility(View.VISIBLE);
                    }
                }
                break;
            case R.id.camera_vlc_activity_btnroom_lockroom:
                break;
            case R.id.camera_vlc_activity_btnroom_backroom:
                if(mMediaPlayer != null){
                    mMediaPlayer.destroy();
                }
                finish();
                break;
        }
    }
}
