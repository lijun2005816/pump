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
    public float ch8;
    public float ch9;
    public float ch10;
    public float ch11;
    public float ch12;
    public float ch13;
    public float ch14;
    public float ch15;

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
        ch8 = data.ch8;
        ch9 = data.ch9;
        ch10 = data.ch10;
        ch11 = data.ch11;
        ch12 = data.ch12;
        ch13 = data.ch13;
        ch14 = data.ch14;
        ch15 = data.ch15;
    }
}
