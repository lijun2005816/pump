package com.smartgreen.pump.model;

import android.util.Log;

import com.google.gson.Gson;
import com.smartgreen.pump.fragment.FragmentData;
import com.smartgreen.pump.fragment.FragmentService;
import com.smartgreen.pump.util.Util;

import java.util.ArrayList;
import java.util.List;

public class DeviceDataCollect {
    private List<DeviceData> mDataList = new ArrayList<>();
    private List<DeviceData> mDataListShow = new ArrayList<>();
    public FragmentData mFragmentData = null;
    public FragmentService mFragmentService = null;
    public DeviceDataCollect() {
    }
    public void add(DeviceData data) {
        int maxLength = 1000;
        int size = mDataList.size();
        if (size > maxLength) {
            mDataList.remove(size-1);
        }
        int sizeShow = mDataListShow.size();
        if (size > maxLength) {
            mDataListShow.remove(sizeShow-1);
        }
        mDataList.add(data);
        Gson gson = new Gson();
        String jsonDD = gson.toJson(data);
        Log.i("DeviceDataCollect", jsonDD);
        DeviceData dataScaled = Util.getDeviceDataScaled(data);
        mDataListShow.add(dataScaled);
        String jsonDDC = gson.toJson(dataScaled);
        Log.i("DeviceDataCollect", jsonDDC);
        if(mFragmentData != null) {
            mFragmentData.updateView(Util.getDeviceDataScaled(data));
        }
        if (mFragmentService != null) {
            mFragmentService.updateView(Util.getDeviceDataScaled(data));
        }
    }
}
