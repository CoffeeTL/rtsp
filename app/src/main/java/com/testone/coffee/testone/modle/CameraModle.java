package com.testone.coffee.testone.modle;

/**
 * Created by coffee on 2017/5/19.
 */

public class CameraModle {
    private String camera_name;
    private String rtsp_url;
    public CameraModle(String camera_name,String rtsp_url){
        this.camera_name = camera_name;
        this.rtsp_url = rtsp_url;
    }

    public String getCamera_name() {
        return camera_name;
    }

    public void setCamera_name(String camera_name) {
        this.camera_name = camera_name;
    }

    public String getRtsp_url() {
        return rtsp_url;
    }

    public void setRtsp_url(String rtsp_url) {
        this.rtsp_url = rtsp_url;
    }
}
