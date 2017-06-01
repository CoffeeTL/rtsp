package com.testone.coffee.testone.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.testone.coffee.testone.R;
import com.testone.coffee.testone.modle.CameraInfoModle;
import com.testone.coffee.testone.modle.data.CameraManager;
import com.testone.coffee.testone.modle.data.ScreenShotManager;
import com.testone.coffee.testone.utils.DensityUtil;
import com.testone.coffee.testone.utils.TextSizeUtils;
import com.testone.coffee.testone.utils.TimeUtils;
import com.testone.coffee.testone.view.ui.LoadingDialog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;



/**
 * Created by coffee on 2017/5/16.
 */

public class CameraPlayActivity extends BaseActivity implements View.OnClickListener,TextureView.SurfaceTextureListener{
    private TextureView textureView;
    private MediaPlayer mediaPlayer;
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
    private RelativeLayout.LayoutParams params;
    private List<CameraInfoModle> modleList;
    private int current_index = 0;
    private int total_len;
    private boolean isBtnShow;
    private File source_file;
    private static final String TAG = "prepared";
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case 101:
                    time_label.setText(TimeUtils.getTime(System.currentTimeMillis()));
                    break;
            }
        }
    };
    private Timer timer;
    private TimerTask task;
    private LoadingDialog loadingDialog;
    private MediaRecorder mediaRecorder;

    public static void startPage(Context context,int current_index){
        Intent intent = new Intent(context,CameraPlayActivity.class);
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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bindView();
        registerListener();
        initView();
        isBtnShow = true;
        initData();
    }

    private void initData() {
        modleList = CameraManager.getInstance().with(this).using(cacheId).getDatas();
        current_index = getIntent().getIntExtra("index",0);
        total_len = modleList.size();
        timer = new Timer();
        task = new TimerTask(){
            @Override
            public void run() {
                Message msg = handler.obtainMessage();
                msg.what = 101;
                handler.sendMessage(msg);
            }
        };
        timer.schedule(task,1000,1000);
        if(modleList != null && modleList.size() != 0){
            textureView.setSurfaceTextureListener(this);
        }
    }

    private void initView() {
        params = (RelativeLayout.LayoutParams) btnplate.getLayoutParams();
        params.width = DensityUtil.getDeviceInfo(this)[0]/5;
        btnplate.setLayoutParams(params);
        loadingDialog = new LoadingDialog(this);
        card_lock_tv.setTextColor(0xffcccccc);
    }

    private void bindView() {
        textureView = (TextureView) findViewById(R.id.camera_play_activity_texture);
        name_label = (TextView) findViewById(R.id.camera_play_activity_nameLabel);
        time_label = (TextView) findViewById(R.id.camera_play_activity_timeLabel);
        btnplate = (RelativeLayout) findViewById(R.id.camera_play_activity_btnplate);
        card_capture_room = (CardView) findViewById(R.id.camera_play_activity_btnroom_captureroom);
        card_record_room = (CardView) findViewById(R.id.camera_play_activity_btnroom_recordroom);
        card_lock_room = (CardView) findViewById(R.id.camera_play_activity_btnroom_lockroom);
        card_next_room = (CardView) findViewById(R.id.camera_play_activity_btnroom_nextroom);
        card_back_room = (CardView) findViewById(R.id.camera_play_activity_btnroom_backroom);
        card_capture_tv = (TextView) findViewById(R.id.camera_play_activity_btnroom_captureroom_tv);
        card_record_tv = (TextView) findViewById(R.id.camera_play_activity_btnroom_recordroom_tv);
        card_lock_tv = (TextView) findViewById(R.id.camera_play_activity_btnroom_lockroom_tv);
        card_next_tv = (TextView) findViewById(R.id.camera_play_activity_btnroom_nextroom_tv);
        card_back_tv = (TextView) findViewById(R.id.camera_play_activity_btnroom_backroom_tv);
        main = findViewById(R.id.camera_play_activity_main);
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

    @Override
    public int getLayoutId() {
        return R.layout.camera_play_activity;
    }
    private boolean isRecording;
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.camera_play_activity_btnroom_captureroom:
                captureCurrentShotAndSave();
                break;
            case R.id.camera_play_activity_btnroom_recordroom:
                if(!isRecording){
                    isRecording = true;
                    doRecord();
                    card_record_tv.setText(R.string.stop_btn_record);
                }else{
                    isRecording = false;
                    stopRecord();
                    card_record_tv.setText(R.string.play_btn_record);
                    if(source_file.exists()){
                        Toast.makeText(this,"录像已经存储到了"+source_file.getAbsolutePath(),Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.camera_play_activity_btnroom_nextroom:
                current_index ++;
                if(current_index >= total_len){
                    current_index = 0;
                }
                loadingDialog.show();
                mSurface = new Surface(textureView.getSurfaceTexture());
                playVideo(current_index);
                break;
            case R.id.camera_play_activity_main:
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
            case R.id.camera_play_activity_btnroom_lockroom:
                break;
            case R.id.camera_play_activity_btnroom_backroom:
                finish();
                break;
        }
    }

    private void stopRecord() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Log.i(TAG,"step1");
                    mediaRecorder.setOnErrorListener(null);
                    mediaRecorder.setOnInfoListener(null);
                    mediaRecorder.setPreviewDisplay(null);
                    Log.i(TAG,"step2");
                    mediaRecorder.stop();
                } catch (IllegalStateException e) {
                    Log.i(TAG, Log.getStackTraceString(e));
                }catch (RuntimeException e) {
                    Log.i(TAG, Log.getStackTraceString(e));
                }catch (Exception e) {
                    Log.i(TAG, Log.getStackTraceString(e));
                }
                mediaRecorder.release();
                mediaRecorder = null;
            }
        }).start();

    }
    private void doRecord() {
        if(mediaRecorder == null){
            mediaRecorder = new MediaRecorder();
        }
        try {
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC_ELD);
            mediaRecorder.setPreviewDisplay(new Surface(textureView.getSurfaceTexture()));
            mediaRecorder.setVideoSize(textureView.getWidth(),textureView.getHeight());
            mediaRecorder.setVideoEncodingBitRate( 512*512);
            mediaRecorder.setVideoFrameRate(15);
            mediaRecorder.setOrientationHint(90);
            mediaRecorder.setMaxDuration(40 * 1000);
            mediaRecorder.setMaxFileSize(5000000);


            String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+TimeUtils.getCurrentDate()+".mp4";
            if (sdPath != null) {
                source_file = new File(sdPath);
                mediaRecorder.setOutputFile(source_file.getAbsolutePath());
                mediaRecorder.prepare();
                mediaRecorder.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void captureCurrentShotAndSave() {
        final String mPath = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+TimeUtils.getCurrentDate() + ".png";
        Bitmap bm = textureView.getBitmap();
        if(bm == null){
            Log.i("prepared","bitmap is null");
        }else{
            ScreenShotManager.get().with(this).putData(bm,modleList.get(current_index).getName());
        }
        OutputStream fout = null;
        File imageFile = new File(mPath);
        try {
            fout = new FileOutputStream(imageFile);
            bm.compress(Bitmap.CompressFormat.PNG, 90, fout);
            fout.flush();
            fout.close();
        } catch (FileNotFoundException e) {
            Log.e("prepared", "FileNotFoundException");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("prepared", "IOException");
            e.printStackTrace();
        }
        Toast.makeText(getApplicationContext(), "截图成功,已保存至 " + mPath, Toast.LENGTH_SHORT).show();
    }



    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    private Surface mSurface;
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        mSurface = new Surface(surface);
        loadingDialog.show();
        playVideo(current_index);
    }
    private Thread thread;
    private void playVideo(final int current){
        name_label.setText(modleList.get(current_index).getName());
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if(mediaPlayer != null){
                    mediaPlayer.release();
                    mediaPlayer = null;
                }
                try {
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setDataSource(modleList.get(current).turnIntoUrl());
                    mediaPlayer.setSurface(mSurface);
                    mediaPlayer.prepareAsync();
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mediaPlayer.start();
                            loadingDialog.dismiss();
                        }
                    });
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

}
