package com.testone.coffee.testone.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.testone.coffee.testone.R;
import com.testone.coffee.testone.modle.CameraInfoModle;
import com.testone.coffee.testone.modle.data.CameraManager;
import com.testone.coffee.testone.utils.DensityUtil;
import com.testone.coffee.testone.utils.TextSizeUtils;
import com.testone.coffee.testone.utils.TimeUtils;
import com.testone.coffee.testone.view.ui.LoadingDialog;

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

public class CameraPlayActivity extends BaseActivity implements View.OnClickListener,TextureView.SurfaceTextureListener {
    private TextureView textureView;
    private MediaPlayer mediaPlayer;
    private TextView name_label;
    private TextView time_label;
    private CardView card_capture_room;
    private CardView card_record_room;
    private CardView card_lock_room;
    private CardView card_next_room;
    private CardView card_play_room;
    private TextView card_capture_tv;
    private TextView card_record_tv;
    private TextView card_lock_tv;
    private TextView card_next_tv;
    private TextView card_play_tv;
    private RelativeLayout btnplate;
    private View main;
    private RelativeLayout.LayoutParams params;
    private List<CameraInfoModle> modleList;
    private int current_index = 0;
    private int total_len;
    private boolean isBtnShow;
    private MediaRecorder mediaRecorder;
    private String record_path = Environment.getExternalStorageDirectory().getAbsolutePath()+ File.separator+System.currentTimeMillis()+".mp4";
    private File source_file;
    private boolean isRecording;
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

    public static void startPage(Context context,int current_index){
        Intent intent = new Intent(context,CameraPlayActivity.class);
        intent.putExtra("index",current_index);
        context.startActivity(intent);
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
            //startPlay(current_index);
            textureView.setSurfaceTextureListener(this);
        }
    }

    private void initView() {
        params = (RelativeLayout.LayoutParams) btnplate.getLayoutParams();
        params.width = DensityUtil.getDeviceInfo(this)[0]/5;
        params.height = DensityUtil.getDeviceInfo(this)[1];
        btnplate.setLayoutParams(params);
        card_play_tv.setText(R.string.play_stop);
        loadingDialog = new LoadingDialog(this);
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
        card_play_room = (CardView) findViewById(R.id.camera_play_activity_btnroom_playroom);
        card_capture_tv = (TextView) findViewById(R.id.camera_play_activity_btnroom_captureroom_tv);
        card_record_tv = (TextView) findViewById(R.id.camera_play_activity_btnroom_recordroom_tv);
        card_lock_tv = (TextView) findViewById(R.id.camera_play_activity_btnroom_lockroom_tv);
        card_next_tv = (TextView) findViewById(R.id.camera_play_activity_btnroom_nextroom_tv);
        card_play_tv = (TextView) findViewById(R.id.camera_play_activity_btnroom_playroom_tv);
        main = findViewById(R.id.camera_play_activity_main);
        TextSizeUtils.calculateTextSizeByDimension(this,name_label,TextSizeUtils.DEFAULT_MIN_SIZE);
        TextSizeUtils.calculateTextSizeByDimension(this,time_label,TextSizeUtils.DEFAULT_MIN_SIZE);
        TextSizeUtils.calculateTextSizeByDimension(this,card_capture_tv,TextSizeUtils.DEFAULT_MIN_SIZE);
        TextSizeUtils.calculateTextSizeByDimension(this,card_record_tv,TextSizeUtils.DEFAULT_MIN_SIZE);
        TextSizeUtils.calculateTextSizeByDimension(this,card_lock_tv,TextSizeUtils.DEFAULT_MIN_SIZE);
        TextSizeUtils.calculateTextSizeByDimension(this,card_next_tv,TextSizeUtils.DEFAULT_MIN_SIZE);
        TextSizeUtils.calculateTextSizeByDimension(this,card_play_tv,TextSizeUtils.DEFAULT_MIN_SIZE);
    }

    private void registerListener() {
        card_capture_room.setOnClickListener(this);
        card_record_room.setOnClickListener(this);
        card_lock_room.setOnClickListener(this);
        card_next_room.setOnClickListener(this);
        card_play_room.setOnClickListener(this);
        main.setOnClickListener(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.camera_play_activity;
    }

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
                    Toast.makeText(this,"录像已经存储到了"+source_file.getAbsolutePath(),Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.camera_play_activity_btnroom_nextroom:
                //videoView.stopPlayback();
                current_index ++;
                if(current_index >= total_len){
                    current_index = 0;
                }
                loadingDialog.show();
                if(mediaPlayer != null){
                    mediaPlayer.stop();
                    mediaPlayer .reset();
                }
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
            case R.id.camera_play_activity_btnroom_playroom:
                if(mediaPlayer.isPlaying()){
                    card_play_tv.setText(R.string.play_start);
                    mediaPlayer.pause();
                }else{
                    card_play_tv.setText(R.string.play_stop);
                    mediaPlayer.start();
                }
                break;
        }
    }
    private Thread capture_thread;
    private boolean is_Captured;
    private void captureCurrentShotAndSave() {
        is_Captured = false;
        Log.i("prepared","start capture");
        final String mPath = Environment.getExternalStorageDirectory().getAbsolutePath()+File.separator+TimeUtils.getCurrentDate() + ".png";
        Log.i("prepared","Capturing Screenshot: " + mPath);
        capture_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                if(!is_Captured){
                    Bitmap bm = textureView.getBitmap();
                    if(bm == null)
                        Log.i("prepared","bitmap is null");
                    OutputStream fout = null;
                    File imageFile = new File(mPath);
                    try {
                        fout = new FileOutputStream(imageFile);
                        if(bm.compress(Bitmap.CompressFormat.PNG, 90, fout)){

                            is_Captured = true;
                        }
                        fout.flush();
                        fout.close();
                    } catch (FileNotFoundException e) {
                        Log.e("prepared", "FileNotFoundException");
                        e.printStackTrace();
                    } catch (IOException e) {
                        Log.e("prepared", "IOException");
                        e.printStackTrace();
                    }
                }
            }
        });
        capture_thread.start();
        Toast.makeText(getApplicationContext(), "截图成功,已保存至 " + mPath, Toast.LENGTH_SHORT).show();
    }


    private void stopRecord() {
        if(source_file != null && source_file.exists()){
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
        }
    }

    private void doRecord() {
        if(mediaRecorder != null){
            mediaRecorder.stop();
            mediaRecorder.release();
            mediaRecorder = null;
        }
        source_file = new File(record_path);
        mediaRecorder = new MediaRecorder();
        //mediaRecorder.setOrientationHint(90);
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.VOICE_RECOGNITION);
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
        mediaRecorder.setVideoSize(DensityUtil.getDeviceInfo(this)[0], DensityUtil.getDeviceInfo(this)[1]);
        mediaRecorder.setVideoFrameRate(3);
        mediaRecorder.setVideoEncodingBitRate( DensityUtil.getDeviceInfo(this)[0]*DensityUtil.getDeviceInfo(this)[1]);
        mediaRecorder.setOutputFile(source_file.getAbsolutePath());
        mediaRecorder.setPreviewDisplay(mSurface);
        mediaRecorder.setMaxDuration(30000);
        mediaRecorder.setMaxFileSize(30000000);
        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaRecorder.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mediaRecorder != null){
            mediaRecorder.stop();
            mediaRecorder.release();
        }

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
                try {
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setDataSource(modleList.get(current).turnIntoUrl());
                    mediaPlayer.setSurface(mSurface);
                    mediaPlayer.prepare();
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
