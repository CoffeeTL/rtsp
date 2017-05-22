package com.testone.coffee.testone.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by coffee on 2017/5/16.
 */

public class RexUtils {
    public static boolean isTrueIP(String ip){
        String urlregEx = "^(?=^.{3,255}$)[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+$"; //表示a或F
        String ipregex = "((2[0-4]\\d|25[0-5]|[01]?\\d\\d?)\\.){3}(2[0-4]\\d|25[0-5]|[01]?\\d\\d?)";
        Pattern urlpat = Pattern.compile(urlregEx);
        Pattern ippat = Pattern.compile(ipregex);
        Matcher urlmat = urlpat.matcher(ip);
        Matcher ipmat = ippat.matcher(ip);
        boolean urlresult = urlmat.find();
        boolean ipresult = ipmat.find();
        return urlresult || ipresult;
    }
}
