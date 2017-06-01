package com.testone.coffee.testone.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

public class DeviceUtils {
    private Context context;
    private TelephonyManager tel;
    private WifiManager wm;
    private static DeviceUtils instance;
    public static DeviceUtils get(){
        if(instance == null){
            instance = new DeviceUtils();
        }
        return instance;
    }

    public DeviceUtils with(Context context) {
        this.context = context;
        tel = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        return this;
    }

    // get imei
    public String getImei() {

        if (Build.VERSION.SDK_INT >= 23) {
            if (PreferenceUtils.getBoolean(context, "is_granded_6.0", false)) {
                return tel.getDeviceId();
            } else {
                return "";
            }
        } else {
            return tel.getDeviceId();
        }
    }

    // get imsi
    public String getImsi() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (PreferenceUtils.getBoolean(context, "is_granded_6.0", false)) {
                return tel.getSubscriberId();
            } else {
                return "";
            }
        } else {
            return tel.getSubscriberId();
        }
    }

    // get phone number
    public String getPhonenumber() {

        if (Build.VERSION.SDK_INT >= 23) {
            if (PreferenceUtils.getBoolean(context, "is_granded_6.0", false)) {
                return tel.getLine1Number();
            } else {
                return "";
            }
        } else {
            return tel.getLine1Number();
        }
    }

    // get model number
    public String getModel() {
        return android.os.Build.MODEL;
    }

    // get SDK number
    public String getSdk() {
        return android.os.Build.VERSION.SDK;
    }

    // get release number
    public String getRelease() {
        return android.os.Build.VERSION.RELEASE;
    }

    public static int getVersionCode(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionCode;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return 0;
        }
    }

    // get mac address
    public String getMacAddress() {
        WifiInfo info = wm.getConnectionInfo();
        return info.getMacAddress();
    }

    public String getIpAddress() {
        String ipAddress = null;
        try {
            List<NetworkInterface> interfaces = Collections
                    .list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface iface : interfaces) {
                if (iface.getDisplayName().equals("eth0")) {
                    List<InetAddress> addresses = Collections.list(iface
                            .getInetAddresses());
                    for (InetAddress address : addresses) {
                        if (address instanceof Inet4Address) {
                            ipAddress = address.getHostAddress();
                        }
                    }
                } else if (iface.getDisplayName().equals("wlan0")) {
                    List<InetAddress> addresses = Collections.list(iface
                            .getInetAddresses());
                    for (InetAddress address : addresses) {
                        if (address instanceof Inet4Address) {
                            ipAddress = address.getHostAddress();
                        }
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return ipAddress;
    }


    // get pixel
    public String getPixel() {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager mWindowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        mWindowManager.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels; // 宽
        int height = dm.heightPixels; // 高

        // 在service中也能得到高和宽
        width = mWindowManager.getDefaultDisplay().getWidth();
        height = mWindowManager.getDefaultDisplay().getHeight();
        String strPM = width + " * " + height;
        return strPM;
    }

    // get lat
//    public double getlatitude() {
//        String provider = LocationManager.NETWORK_PROVIDER;
//        LocationManager locationManager = (LocationManager) context.
//
//        getSystemService(Context.LOCATION_SERVICE);// 获取LocationManager的一个实例
//
//        Location location = locationManager.getLastKnownLocation(provider);
//        double lat = location.getLatitude();
//        return lat;
//    }

    // get lng
//    public double getlontitude() {
//        String provider = LocationManager.NETWORK_PROVIDER;
//        LocationManager locationManager = (LocationManager) context
//                .getSystemService(Context.LOCATION_SERVICE);// 获取LocationManager的一个实例
//        Location location = locationManager.getLastKnownLocation(provider);
//        double lnt = location.getLongitude();
//        return lnt;
//    }

    // get language
    public String getlanguage() {
        Locale locale = context.getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        return language;

    }

    // get sim country iso
    public String getSimCountryIso() {

        if (Build.VERSION.SDK_INT >= 23) {
            if (PreferenceUtils.getBoolean(context, "is_granded_6.0", false)) {
                return tel.getSimCountryIso();
            } else {
                return "";
            }
        } else {
            return tel.getSimCountryIso();
        }
    }

    // get sim serialnumber
    public String getSimSerialnumber() {

        if (Build.VERSION.SDK_INT >= 23) {
            if (PreferenceUtils.getBoolean(context, "is_granded_6.0", false)) {
                return tel.getSimSerialNumber();
            } else {
                return "";
            }
        } else {
            return tel.getSimSerialNumber();
        }
    }


    public String getVersion() {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String getWifiIp(Context context) {
        WifiManager wifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();

        String ip = String.format("%d.%d.%d.%d", (ipAddress & 0xff),
                (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff),
                (ipAddress >> 24 & 0xff));
        return ip;
    }

    public static String getMobileIP() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = (NetworkInterface) en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String ipaddress = inetAddress.getHostAddress().toString();
                        return ipaddress;
                    }
                }
            }
        } catch (SocketException ex) {
            Log.e("getMobileIP", "Exception in Get IP Address: " + ex.toString());
        }
        return "";
    }

}
