package com.testone.coffee.testone.modle;

/**
 * Created by coffee on 2017/5/27.
 */

public class ScreenShotModle {
    public String capture_id;
    public String screen_name;
    public String device_id ;
    public String capture_time;
    public byte[] capture_stream;

    public ScreenShotModle(String capture_id, String screen_name, String device_id, String capture_time, byte[] capture_stream) {
        this.capture_id = capture_id;
        this.screen_name = screen_name;
        this.device_id = device_id;
        this.capture_time = capture_time;
        this.capture_stream = capture_stream;
    }

    public String getCapture_id() {
        return capture_id;
    }

    public void setCapture_id(String capture_id) {
        this.capture_id = capture_id;
    }

    public String getScreen_name() {
        return screen_name;
    }

    public void setScreen_name(String screen_name) {
        this.screen_name = screen_name;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getCapture_time() {
        return capture_time;
    }

    public void setCapture_time(String capture_time) {
        this.capture_time = capture_time;
    }

    public byte[] getCapture_stream() {
        return capture_stream;
    }

    public void setCapture_stream(byte[] capture_stream) {
        this.capture_stream = capture_stream;
    }
}
