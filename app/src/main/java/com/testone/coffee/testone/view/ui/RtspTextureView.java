package com.testone.coffee.testone.view.ui;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.view.Surface;
import android.view.TextureView;

import java.io.IOException;

/**
 * Created by coffee on 2017/5/25.
 */

public class RtspTextureView extends TextureView implements TextureView.SurfaceTextureListener {
    private MediaPlayer mediaPlayer;

    public RtspTextureView(Context context) {
        super(context);
        setSurfaceTextureListener(this);
        initView();
    }

    private void initView() {
        mediaPlayer = new MediaPlayer();
    }
    public void setPlayDataSource(String url){
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.setSurface(msurface);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void stopPlay() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
    public void pausePlay(){
        if(mediaPlayer != null){
            mediaPlayer.pause();
        }
    }
    public void startPlay(){
        if(mediaPlayer != null){
            mediaPlayer.start();
        }
    }
    private Surface msurface;
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        msurface = new Surface(surface);
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }
}
