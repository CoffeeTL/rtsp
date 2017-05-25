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
    public String username;
    public String pwd;

    public CameraInfoModle(String name, String IPAddress, String port, String backString,String username,String pwd) {
        this.name = name;
        this.IPAddress = IPAddress;
        this.port = port;
        this.backString = backString;
        this.username = username;
        this.pwd = pwd;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String turnIntoUrl(){
        String portPath = "";
        if(!TextUtils.equals("",getPort())){
            portPath += ":"+getPort();
        }
        String usernamePath = "";
        if(!TextUtils.equals("",getUsername())){
            usernamePath += "/"+getUsername();
        }
        String pwdPath = "";
        if(!TextUtils.equals("",getPwd())){
            pwdPath += "/"+getPwd();
        }
        return "rtsp://"+getIPAddress()+portPath+usernamePath+pwdPath+"/"+getBackString();

    }
}
