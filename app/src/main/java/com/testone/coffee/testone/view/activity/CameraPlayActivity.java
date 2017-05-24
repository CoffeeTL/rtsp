package com.testone.coffee.testone.view.activity;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.testone.coffee.testone.R;
import com.testone.coffee.testone.modle.CameraInfoModle;
import com.testone.coffee.testone.modle.CameraModle;
import com.testone.coffee.testone.modle.data.CameraManager;
import com.testone.coffee.testone.utils.DensityUtil;
import com.testone.coffee.testone.utils.TextSizeUtils;
import com.testone.coffee.testone.utils.TimeUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by coffee on 2017/5/16.
 */

public class CameraPlayActivity extends BaseActivity implements View.OnClickListener {
    private VideoView videoView;
    private TextView name_label;
    private TextView time_label;
    private LinearLayout btnplate;
    private TextView audio_btn;
    private TextView record_btn;
    private TextView switch_btn;
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
    private AudioManager audioManager;
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
        params = (RelativeLayout.LayoutParams) btnplate.getLayoutParams();
        params.width = DensityUtil.getDeviceInfo(this)[0]/5;
        params.height = DensityUtil.getDeviceInfo(this)[1]/2;
        btnplate.setLayoutParams(params);
        isBtnShow = true;
        audioManager = (AudioManager)getSystemService(AUDIO_SERVICE);
        current_audio = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        if(current_audio > 0){
            audio_btn.setText(R.string.turn_off_audio);
        }else{
            audio_btn.setText(R.string.turn_on_audio);
        }
        modleList = CameraManager.getInstance().with(this).using(cacheId).getDatas();
        current_index = getIntent().getIntExtra("index",0);
        total_len = modleList.size();
        timer = new Timer();
        if(modleList != null && modleList.size() != 0){
            startPlay(current_index);
        }

    }

    private void bindView() {
        videoView = (VideoView) findViewById(R.id.camera_play_activity_videoView);
        name_label = (TextView) findViewById(R.id.camera_play_activity_nameLabel);
        time_label = (TextView) findViewById(R.id.camera_play_activity_timeLabel);
        btnplate = (LinearLayout) findViewById(R.id.camera_play_activity_btnplate);
        audio_btn = (TextView) findViewById(R.id.camera_play_activity_audioBtn);
        record_btn = (TextView) findViewById(R.id.camera_play_activity_recordBtn);
        switch_btn = (TextView) findViewById(R.id.camera_play_activity_switchBtn);
        main = findViewById(R.id.camera_play_activity_main);
        TextSizeUtils.calculateTextSizeByDimension(this,name_label,TextSizeUtils.DEFAULT_MIN_SIZE);
        TextSizeUtils.calculateTextSizeByDimension(this,time_label,TextSizeUtils.DEFAULT_MIN_SIZE);
        TextSizeUtils.calculateTextSizeByDimension(this,audio_btn,TextSizeUtils.DEFAULT_MIN_SIZE);
        TextSizeUtils.calculateTextSizeByDimension(this,record_btn,TextSizeUtils.DEFAULT_MIN_SIZE);
        TextSizeUtils.calculateTextSizeByDimension(this,switch_btn,TextSizeUtils.DEFAULT_MIN_SIZE);
    }

    private void registerListener() {
        audio_btn.setOnClickListener(this);
        record_btn.setOnClickListener(this);
        switch_btn.setOnClickListener(this);
        main.setOnClickListener(this);
    }

    private void startPlay(int current_index) {
        name_label.setText(modleList.get(current_index).getName());
        task = new TimerTask(){
            @Override
            public void run() {
                Message msg = handler.obtainMessage();
                msg.what = 101;
                handler.sendMessage(msg);
            }
        };
        timer.schedule(task,1000,1000);
        final MediaController controll = new MediaController(this);
        controll.setMediaPlayer(videoView);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Log.i("************", "注册在媒体文件播放完毕时调用的回调函数。");
            }
        });
        videoView.setVideoURI(Uri.parse(modleList.get(current_index).turnIntoUrl()));
        videoView.setMediaController(controll);
        videoView.requestFocus();
        videoView.start();
        controll.show();
    }


    @Override
    public int getLayoutId() {
        return R.layout.camera_play_activity;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.camera_play_activity_audioBtn:
                controlAudio();
                break;
            case R.id.camera_play_activity_recordBtn:
                if(!isRecording){
                    isRecording = true;
                    doRecord();
                    record_btn.setText(R.string.stop_btn_record);
                }else{
                    isRecording = false;
                    stopRecord();
                    record_btn.setText(R.string.play_btn_record);
                    Toast.makeText(this,"录像已经存储到了"+source_file.getAbsolutePath(),Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.camera_play_activity_switchBtn:
                videoView.stopPlayback();
                current_index ++;
                if(current_index >= total_len){
                    current_index = 0;
                }
                startPlay(current_index);
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
        }
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
        mediaRecorder.setPreviewDisplay(videoView.getHolder().getSurface());
        mediaRecorder.setMaxDuration(30000);
        mediaRecorder.setMaxFileSize(30000000);
        try {
            mediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mediaRecorder.start();
    }

    private int current_audio;
    private void controlAudio() {
        current_audio = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int max =audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        if(current_audio > 0){
            current_audio = 0;
            audio_btn.setText(R.string.turn_on_audio);
        }else{
            current_audio = max/3;
            audio_btn.setText(R.string.turn_off_audio);
        }
        if(current_audio>=0&&current_audio<=max){
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,current_audio,AudioManager.FLAG_PLAY_SOUND);
        }else {
            return;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mediaRecorder != null){
            mediaRecorder.stop();
            mediaRecorder.release();
        }
        if(videoView != null){
            videoView.setVideoURI(null);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(videoView != null){
            videoView.stopPlayback();
        }
    }
}
