package com.smartgreen.pump.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.aliyun.alink.dm.api.DeviceInfo;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.smartgreen.pump.model.DeviceData;
import com.smartgreen.pump.model.DeviceDataCollect;
import com.smartgreen.pump.model.UserInfo;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

public class Util {
    public static String USERNAME = "username";
    public static String PASSWORD = "password";
    private static String mSpSetting = "smartpump";
    private static String mSpUserKey = "userinfo";
    private static String mSpDeviceKey = "deviceinfo";
    private static String mSpPumpName = "pumpname";
    public static DeviceDataCollect mDataCollect = new DeviceDataCollect();
    private static float mCurrentMin = 4.0f;
    private static float mCurrentMax = 20.0f;
    public static float[][] mChRangeWarning = new float[8][6];

    public static String httpSendPost(String url, String param) {
        try {
            URL realUrl = new URL(url);
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection)realUrl.openConnection();
            httpsURLConnection.setConnectTimeout(20000);
            httpsURLConnection.setDoOutput(true);
            httpsURLConnection.setDoInput(true);
            httpsURLConnection.setRequestMethod("POST");
            httpsURLConnection.setUseCaches(false);
            httpsURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            byte[] dataInput = param.getBytes();
            httpsURLConnection.setRequestProperty("Content-Length", String.valueOf(dataInput.length));
            OutputStream outputStream = httpsURLConnection.getOutputStream();
            outputStream.write(dataInput);
            int response = httpsURLConnection.getResponseCode();
            if (response == HttpsURLConnection.HTTP_OK) {
                InputStream inputStream = httpsURLConnection.getInputStream();
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] dataOutput = new byte[1024];
                int len;
                while((len = inputStream.read(dataOutput)) != -1) {
                    byteArrayOutputStream.write(dataOutput, 0, len);
                }
                return new String(byteArrayOutputStream.toByteArray());
            } else {
                return "";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String md5Decode(String content) {
        byte[] hash = new byte[32];
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            hash = md.digest(content.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10){
                hex.append("0");
            }
            hex.append(Integer.toHexString(b & 0xFF));
        }
        return hex.toString();
    }

/*
    public static DeviceInfo getDeviceInfoFromRaw(Context context) {
        DeviceInfo deviceInfo = null;
        InputStreamReader isr = null;
        BufferedReader br = null;
        try {
            InputStream is = context.getResources().openRawResource(R.raw.deviceinfo);
            isr = new InputStreamReader(is);
            br = new BufferedReader(isr);
            String line;
            StringBuilder Result = new StringBuilder();
            while ((line = br.readLine()) != null) {
                Result.append(line);
            }
            Gson gson = new Gson();
            deviceInfo = gson.fromJson(Result.toString(), DeviceInfo.class);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (isr != null){
                    isr.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
        return deviceInfo;
    }
*/

    public static DeviceInfo getDeviceInfoFromSP(Context context) {
        DeviceInfo deviceInfo = null;
        try {
            SharedPreferences authInfo = context.getSharedPreferences(mSpSetting, 0);
            String di = authInfo.getString(mSpDeviceKey, null);
            Gson gson = new Gson();
            deviceInfo = gson.fromJson(di, DeviceInfo.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deviceInfo;
    }

    public static void setDeviceInfoToSP(Context context, DeviceInfo deviceInfo) {
        SharedPreferences preferences = context.getSharedPreferences(mSpSetting, 0);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String di = gson.toJson(deviceInfo);
        editor.putString(mSpDeviceKey, di);
        editor.apply();
    }

    public static UserInfo getUserInfoFromSP(Context context) {
        UserInfo userInfo = null;
        try {
            SharedPreferences authInfo = context.getSharedPreferences(mSpSetting, 0);
            String di = authInfo.getString(mSpUserKey, null);
            Gson gson = new Gson();
            userInfo = gson.fromJson(di, UserInfo.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userInfo;
    }

    public static void setUserInfoToSP(Context context, UserInfo userInfo) {
        SharedPreferences preferences = context.getSharedPreferences(mSpSetting, 0);
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String di = gson.toJson(userInfo);
        editor.putString(mSpUserKey, di);
        editor.apply();
    }

    public static String getPumpNameFromSP(Context context) {
        SharedPreferences authInfo = context.getSharedPreferences(mSpSetting, 0);
        return authInfo.getString(mSpPumpName, null);
    }
    public static void setPumpNameToSP(Context context, String pump) {
        SharedPreferences preferences = context.getSharedPreferences(mSpSetting, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(mSpPumpName, pump);
        editor.apply();
    }

    public static String strToDateTime(String strDate) {
        SimpleDateFormat formatterFrom = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        ParsePosition pos = new ParsePosition(0);
        Date currentTime = formatterFrom.parse(strDate, pos);
        SimpleDateFormat formatterDest = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss", Locale.CHINA);
        return formatterDest.format(currentTime);
    }

    public static String getStringDateTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss", Locale.CHINA);
        Date currentTime = new Date();
        return formatter.format(currentTime);
    }

    public static String getStringTimeShort() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
        Date currentTime = new Date();
        return formatter.format(currentTime);
    }

    public static String getStringDateShort() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日", Locale.CHINA);
        Date currentTime = new Date();
        return formatter.format(currentTime);
    }

    public static ArrayList<DeviceInfo> getDeviceInfoListFromJson(String strByJson) {
        JsonParser parser = new JsonParser();
        JsonArray jsonArray = parser.parse(strByJson).getAsJsonArray();
        Gson gson = new Gson();
        ArrayList<DeviceInfo> deviceInfoArrayList = new ArrayList<>();
        for (JsonElement element : jsonArray) {
            DeviceInfo deviceInfo = gson.fromJson(element, DeviceInfo.class);
            deviceInfoArrayList.add(deviceInfo);
        }
        return deviceInfoArrayList;
    }

    public static void setChRangeWarning(int index, String range, String warningLow, String warningHigh) {
        String[] rangeList = range.split("~");
        if (rangeList.length < 2) {
            return;
        }
        String rangeLow = rangeList[0];
        mChRangeWarning[index][0] = Float.valueOf(rangeLow);
        String rangeHigh = rangeList[1];
        mChRangeWarning[index][1] = Float.valueOf(rangeHigh);
        mChRangeWarning[index][2] = Float.valueOf(warningLow);
        mChRangeWarning[index][3] = Float.valueOf(warningHigh);
        mChRangeWarning[index][4] = (mChRangeWarning[index][2]-mChRangeWarning[index][0])/(mChRangeWarning[index][1]-mChRangeWarning[index][0])*(mCurrentMax-mCurrentMin)+mCurrentMin;
        mChRangeWarning[index][5] = (mChRangeWarning[index][3]-mChRangeWarning[index][0])/(mChRangeWarning[index][1]-mChRangeWarning[index][0])*(mCurrentMax-mCurrentMin)+mCurrentMin;
    }
    public static DeviceData getDeviceDataScaled(DeviceData origin) {
        DeviceData data = new DeviceData(origin);
        data.ch0 = (origin.ch0-mCurrentMin)/(mCurrentMax-mCurrentMin)*(mChRangeWarning[0][1]-mChRangeWarning[0][0])+mChRangeWarning[0][0];
        data.ch1 = (origin.ch1-mCurrentMin)/(mCurrentMax-mCurrentMin)*(mChRangeWarning[1][1]-mChRangeWarning[1][0])+mChRangeWarning[1][0];
        data.ch2 = (origin.ch2-mCurrentMin)/(mCurrentMax-mCurrentMin)*(mChRangeWarning[2][1]-mChRangeWarning[2][0])+mChRangeWarning[2][0];
        data.ch3 = (origin.ch3-mCurrentMin)/(mCurrentMax-mCurrentMin)*(mChRangeWarning[3][1]-mChRangeWarning[3][0])+mChRangeWarning[3][0];
        data.ch4 = (origin.ch4-mCurrentMin)/(mCurrentMax-mCurrentMin)*(mChRangeWarning[4][1]-mChRangeWarning[4][0])+mChRangeWarning[4][0];
        data.ch5 = (origin.ch5-mCurrentMin)/(mCurrentMax-mCurrentMin)*(mChRangeWarning[5][1]-mChRangeWarning[5][0])+mChRangeWarning[5][0];
        data.ch6 = (origin.ch6-mCurrentMin)/(mCurrentMax-mCurrentMin)*(mChRangeWarning[6][1]-mChRangeWarning[6][0])+mChRangeWarning[6][0];
        data.ch7 = (origin.ch7-mCurrentMin)/(mCurrentMax-mCurrentMin)*(mChRangeWarning[7][1]-mChRangeWarning[7][0])+mChRangeWarning[7][0];
        return data;
    }
}
