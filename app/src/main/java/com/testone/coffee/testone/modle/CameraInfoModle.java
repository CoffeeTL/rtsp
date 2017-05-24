package com.testone.coffee.testone.modle;

import android.text.TextUtils;

/**
 * Created by coffee on 2017/5/16.
 */

public class CameraInfoModle {
    public String name;
    public String IPAddress;
    public String port;
    public String backString;

    public CameraInfoModle(String name, String IPAddress, String port, String backString) {
        this.name = name;
        this.IPAddress = IPAddress;
        this.port = port;
        this.backString = backString;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIPAddress() {
        return IPAddress;
    }

    public void setIPAddress(String IPAddress) {
        this.IPAddress = IPAddress;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getBackString() {
        return backString;
    }

    public void setBackString(String backString) {
        this.backString = backString;
    }

    public String turnIntoUrl(){
        if(TextUtils.equals("",getPort())){
            return "rtsp://"+getIPAddress()+"/"+getBackString();
        }else{
            return "rtsp://"+getIPAddress()+":"+getPort()+"/"+getBackString();
        }

    }
}
