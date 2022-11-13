package com.limelight.utils;

import android.util.Log;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

public class HXSIpParse {

    public static int[] parseIp(String ip){
        boolean isArea = false;
        int[] result;
        String[] ipSplit = ip.split("\\.");
        for (int i = 0; i<ipSplit.length;i++){
            if (!isNumeric(ipSplit[i])){
                isArea = true;
                break;
            }
        }
        if (isArea){
            try {
                String ipString = InetAddress.getByName(ip).getHostAddress();
                Log.d("ip",ipString);
                return parseIp(ipString);

            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        result = new int[ipSplit.length];
        for (int i = 0;i<ipSplit.length;i++){
            result[i] = Integer.parseInt(ipSplit[i]);
        }
        return result;
    }

    public static boolean isNumeric(String string){
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(string).matches();
    }

}
