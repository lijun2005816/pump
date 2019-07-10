package com.smartgreen.pump.model;

public class DeviceData {
    public String product;
    public String device;
    public String time;
    public long timestamp;
    public float ch0;
    public float ch1;
    public float ch2;
    public float ch3;
    public float ch4;
    public float ch5;
    public float ch6;
    public float ch7;
    public DeviceData() {
    }
    public DeviceData(DeviceData data) {
        product = data.product;
        device = data.device;
        time = data.time;
        timestamp = data.timestamp;
        ch0 = data.ch0;
        ch1 = data.ch1;
        ch2 = data.ch2;
        ch3 = data.ch3;
        ch4 = data.ch4;
        ch5 = data.ch5;
        ch6 = data.ch6;
        ch7 = data.ch7;
    }
}
