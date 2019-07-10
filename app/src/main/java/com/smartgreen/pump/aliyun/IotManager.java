package com.smartgreen.pump.aliyun;

import android.content.Context;
import android.text.TextUtils;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.aliyun.alink.dm.api.DeviceInfo;
import com.aliyun.alink.dm.api.IoTApiClientConfig;
import com.aliyun.alink.dm.model.ResponseModel;
import com.aliyun.alink.linkkit.api.ILinkKitConnectListener;
import com.aliyun.alink.linkkit.api.IoTDMConfig;
import com.aliyun.alink.linkkit.api.IoTH2Config;
import com.aliyun.alink.linkkit.api.IoTMqttClientConfig;
import com.aliyun.alink.linkkit.api.LinkKit;
import com.aliyun.alink.linkkit.api.LinkKitInitParams;
import com.aliyun.alink.linksdk.cmp.connect.channel.MqttPublishRequest;
import com.aliyun.alink.linksdk.cmp.connect.channel.MqttSubscribeRequest;
import com.aliyun.alink.linksdk.cmp.connect.hubapi.HubApiRequest;
import com.aliyun.alink.linksdk.cmp.core.base.AMessage;
import com.aliyun.alink.linksdk.cmp.core.base.ARequest;
import com.aliyun.alink.linksdk.cmp.core.base.AResponse;
import com.aliyun.alink.linksdk.cmp.core.base.ConnectState;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectNotifyListener;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectSendListener;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectSubscribeListener;
import com.aliyun.alink.linksdk.cmp.core.listener.IConnectUnscribeListener;
import com.aliyun.alink.linksdk.id2.Id2ItlsSdk;
import com.aliyun.alink.linksdk.tmp.device.payload.ValueWrapper;
import com.aliyun.alink.linksdk.tools.AError;
import com.google.gson.Gson;
import com.smartgreen.pump.model.DeviceData;
import com.smartgreen.pump.util.Util;
import java.util.HashMap;
import java.util.Map;

public class IotManager{
    private Context mContext;
    private DeviceInfo mDeviceInfo;
    private String mTopicGet;
    private String mTopicUpdate;
    public IotManager(Context context) {
        mContext = context;
    }
    public void connect() {
        mDeviceInfo = Util.getDeviceInfoFromSP(mContext);
        if (mDeviceInfo == null || TextUtils.isEmpty(mDeviceInfo.productKey) || TextUtils.isEmpty(mDeviceInfo.deviceName)) {
            return;
        }
        mTopicGet = "/" + mDeviceInfo.productKey + "/" + mDeviceInfo.deviceName + "/get";
        mTopicUpdate = "/" + mDeviceInfo.productKey + "/" + mDeviceInfo.deviceName + "/update";
        if (TextUtils.isEmpty(mDeviceInfo.deviceSecret)) {
            updateSecretFromYun(mDeviceInfo);
        } else {
            init();
        }
    }
    public void disConnect() {
        LinkKit.getInstance().deinit();
    }
    public void register() {
        LinkKit.getInstance().registerOnPushListener(mRegisterListener);
    }
    public void unRegister() {
        LinkKit.getInstance().unRegisterOnPushListener(mRegisterListener);
    }
    private void subscribe() {
        try {
            MqttSubscribeRequest subscribeRequest = new MqttSubscribeRequest();
            subscribeRequest.isSubscribe = true;
            subscribeRequest.topic = mTopicGet;
            LinkKit.getInstance().subscribe(subscribeRequest, subscribeListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void unSubscribe() {
        try {
            MqttSubscribeRequest unSubRequest = new MqttSubscribeRequest();
            unSubRequest.topic = mTopicGet;
            unSubRequest.isSubscribe = false;
            LinkKit.getInstance().unsubscribe(unSubRequest, unScribeListener);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void publish(String data) {
        try {
            MqttPublishRequest request = new MqttPublishRequest();
            request.qos = 0;
            request.isRPC = false;
            request.topic = mTopicUpdate;
            request.payloadObj = data;
            LinkKit.getInstance().publish(request, publishListener);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    private void updateSecretFromYun(DeviceInfo myDeviceInfo) {
        LinkKitInitParams params = new LinkKitInitParams();
        params.connectConfig = new IoTApiClientConfig();
        params.deviceInfo = myDeviceInfo;
        HubApiRequest hubApiRequest = new HubApiRequest();
        hubApiRequest.path = "/auth/register/device";
        LinkKit.getInstance().deviceRegister(mContext, params, hubApiRequest, mCallBackUpdateSecret);
    }
    private IConnectSendListener mCallBackUpdateSecret = new IConnectSendListener() {
        @Override
        public void onResponse(ARequest aRequest, AResponse aResponse) {
            if (aResponse == null || aResponse.data == null) {
                return;
            }
            ResponseModel<Map<String, String>> response = JSONObject.parseObject(aResponse.data.toString(),
                    new TypeReference<ResponseModel<Map<String, String>>>() {}.getType());
            if ("200".equals(response.code) && response.data != null && response.data.containsKey("deviceSecret") &&
                    !TextUtils.isEmpty(response.data.get("deviceSecret"))) {
                mDeviceInfo.deviceSecret = response.data.get("deviceSecret");
                Util.setDeviceInfoToSP(mContext, mDeviceInfo);
                init();
            }
        }
        @Override
        public void onFailure(ARequest aRequest, AError aError) {
        }
    };
    private void init() {
        IoTApiClientConfig userData = new IoTApiClientConfig();
        Map<String, ValueWrapper> propertyValues = new HashMap<>();
        LinkKitInitParams params = new LinkKitInitParams();
        params.deviceInfo = mDeviceInfo;
        params.propertyValues = propertyValues;
        params.connectConfig = userData;
        IoTH2Config ioTH2Config = new IoTH2Config();
        ioTH2Config.clientId = "client-id";
        ioTH2Config.endPoint = "https://" + mDeviceInfo.productKey + ioTH2Config.endPoint;
        params.iotH2InitParams = ioTH2Config;
        Id2ItlsSdk.init(mContext);
        IoTMqttClientConfig clientConfig = new IoTMqttClientConfig(mDeviceInfo.productKey, mDeviceInfo.deviceName,
                mDeviceInfo.deviceSecret);
        if ("itls_secret".equals(mDeviceInfo.deviceSecret)){
            clientConfig.channelHost = mDeviceInfo.productKey + ".itls.cn-shanghai.aliyuncs.com:1883";
            clientConfig.productSecret = mDeviceInfo.productSecret;
            clientConfig.secureMode = 8;
        }
        params.mqttClientConfig = clientConfig;
        IoTDMConfig ioTDMConfig = new IoTDMConfig();
        ioTDMConfig.enableNotify = true;
        params.ioTDMConfig = ioTDMConfig;
        LinkKit.getInstance().init(mContext, params, mCallBackInit);
    }
    private ILinkKitConnectListener mCallBackInit = new ILinkKitConnectListener() {
        @Override
        public void onError(AError aError) {
        }
        @Override
        public void onInitDone(Object data) {
            subscribe();
        }
    };
    private IConnectSubscribeListener subscribeListener = new IConnectSubscribeListener() {
        @Override
        public void onSuccess() {
            register();
        }
        @Override
        public void onFailure(AError aError) {
        }
    };
    private IConnectUnscribeListener unScribeListener = new IConnectUnscribeListener() {
        @Override
        public void onSuccess() {
        }
        @Override
        public void onFailure(AError aError) {
        }
    };
    private IConnectNotifyListener mRegisterListener = new IConnectNotifyListener() {
        @Override
        public void onNotify(String connectId, String topic, AMessage aMessage) {
            String pushData = new String((byte[]) aMessage.data);
            Gson gson = new Gson();
            DeviceData dd = gson.fromJson(pushData, DeviceData.class);
            Util.mDataCollect.add(dd);
        }
        @Override
        public boolean shouldHandle(String s, String s1) {
            return true;
        }
        @Override
        public void onConnectStateChange(String s, ConnectState connectState) {
        }
    };
    private IConnectSendListener publishListener = new IConnectSendListener() {
        @Override
        public void onResponse(ARequest aRequest, AResponse aResponse) {
        }
        @Override
        public void onFailure(ARequest aRequest, AError aError) {
        }
    };
}
