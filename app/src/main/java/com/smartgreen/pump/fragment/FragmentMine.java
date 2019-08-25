package com.smartgreen.pump.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.aliyun.alink.dm.api.DeviceInfo;
import com.smartgreen.pump.MainActivity;
import com.smartgreen.pump.R;
import com.smartgreen.pump.model.UserInfo;
import com.smartgreen.pump.util.Util;

import java.util.ArrayList;
import java.util.Objects;

public class FragmentMine extends Fragment {
    public static final int REQUEST_CODE = 1;
    private Button mBtnMineLogin;
    private TextView mTvMineWelcome;
    private ImageView mIvMyInfo;
    private Boolean mIvMyInfoStatus;
    private ImageView mIvDevice;
    private Boolean mIvDeviceStatus;
    private LinearLayout mLlMyInfo;
    private LinearLayout mLlDevice;
    private TextView mTvMineUsername;
    private TextView mTvMinePassword;
    private TextView mTvMineCompany;
    private TextView mTvMineDepartment;
    private TextView mTvMineRole;
    private UserInfo mUserInfo;

    private FragmentDialogLogin mFragment;
    private TextView mTvMinePump;
    private EditText mEtMinePump;
    private Button mBtnMinePump;

    private DeviceInfo mDeviceInfo;
    ArrayList<DeviceInfo> mDeviceInfoList = new ArrayList<>();
    private int mDeviceInfoCount = 2;
    private int[][] mTvDeviceInfoId = {
            {R.id.tv_product1_key, R.id.tv_product1_secret, R.id.tv_device1_key, R.id.tv_device1_secret, R.id.btn_product1_key, R.id.tb_device1_type},
            {R.id.tv_product2_key, R.id.tv_product2_secret, R.id.tv_device2_key, R.id.tv_device2_secret, R.id.btn_product2_key, R.id.tb_device2_type},
    };
    private int mTvDeviceInfoIndex = 0;
    private int mTvDeviceInfoLength = 4;
    private TextView[][] mTvDeviceInfo = new TextView[mDeviceInfoCount][mTvDeviceInfoLength];
    private int mBtnDeviceInfoIndex = mTvDeviceInfoIndex + mTvDeviceInfoLength;
    private int mBtnDeviceInfoLength = 1;
    private RadioButton[][] mBtnDeviceCheck = new RadioButton[mDeviceInfoCount][mBtnDeviceInfoLength];
    private int mTbDeviceInfoIndex = mBtnDeviceInfoIndex + mBtnDeviceInfoLength;
    private int mTbDeviceInfoLength = 1;
    private ToggleButton[][] mTbDeviceInfo = new ToggleButton[mDeviceInfoCount][mTbDeviceInfoLength];

    private int mChCount = 16;
    private int[][] mTvRangeWarningId = {
            {R.id.tv_ch1_range, R.id.tv_ch1_warning_low, R.id.tv_ch1_warning_high, R.id.tv_ch1_ok, R.id.sp_ch1_range, R.id.et_ch1_warning_low, R.id.et_ch1_warning_high, R.id.btn_ch1_range},
            {R.id.tv_ch2_range, R.id.tv_ch2_warning_low, R.id.tv_ch2_warning_high, R.id.tv_ch2_ok, R.id.sp_ch2_range, R.id.et_ch2_warning_low, R.id.et_ch2_warning_high, R.id.btn_ch2_range},
            {R.id.tv_ch3_range, R.id.tv_ch3_warning_low, R.id.tv_ch3_warning_high, R.id.tv_ch3_ok, R.id.sp_ch3_range, R.id.et_ch3_warning_low, R.id.et_ch3_warning_high, R.id.btn_ch3_range},
            {R.id.tv_ch4_range, R.id.tv_ch4_warning_low, R.id.tv_ch4_warning_high, R.id.tv_ch4_ok, R.id.sp_ch4_range, R.id.et_ch4_warning_low, R.id.et_ch4_warning_high, R.id.btn_ch4_range},
            {R.id.tv_ch5_range, R.id.tv_ch5_warning_low, R.id.tv_ch5_warning_high, R.id.tv_ch5_ok, R.id.sp_ch5_range, R.id.et_ch5_warning_low, R.id.et_ch5_warning_high, R.id.btn_ch5_range},
            {R.id.tv_ch6_range, R.id.tv_ch6_warning_low, R.id.tv_ch6_warning_high, R.id.tv_ch6_ok, R.id.sp_ch6_range, R.id.et_ch6_warning_low, R.id.et_ch6_warning_high, R.id.btn_ch6_range},
            {R.id.tv_ch7_range, R.id.tv_ch7_warning_low, R.id.tv_ch7_warning_high, R.id.tv_ch7_ok, R.id.sp_ch7_range, R.id.et_ch7_warning_low, R.id.et_ch7_warning_high, R.id.btn_ch7_range},
            {R.id.tv_ch8_range, R.id.tv_ch8_warning_low, R.id.tv_ch8_warning_high, R.id.tv_ch8_ok, R.id.sp_ch8_range, R.id.et_ch8_warning_low, R.id.et_ch8_warning_high, R.id.btn_ch8_range},
            {R.id.tv_ch1_range_motor, R.id.tv_ch1_warning_low_motor, R.id.tv_ch1_warning_high_motor, R.id.tv_ch1_ok_motor, R.id.sp_ch1_range_motor, R.id.et_ch1_warning_low_motor, R.id.et_ch1_warning_high_motor, R.id.btn_ch1_range_motor},
            {R.id.tv_ch2_range_motor, R.id.tv_ch2_warning_low_motor, R.id.tv_ch2_warning_high_motor, R.id.tv_ch2_ok_motor, R.id.sp_ch2_range_motor, R.id.et_ch2_warning_low_motor, R.id.et_ch2_warning_high_motor, R.id.btn_ch2_range_motor},
            {R.id.tv_ch3_range_motor, R.id.tv_ch3_warning_low_motor, R.id.tv_ch3_warning_high_motor, R.id.tv_ch3_ok_motor, R.id.sp_ch3_range_motor, R.id.et_ch3_warning_low_motor, R.id.et_ch3_warning_high_motor, R.id.btn_ch3_range_motor},
            {R.id.tv_ch4_range_motor, R.id.tv_ch4_warning_low_motor, R.id.tv_ch4_warning_high_motor, R.id.tv_ch4_ok_motor, R.id.sp_ch4_range_motor, R.id.et_ch4_warning_low_motor, R.id.et_ch4_warning_high_motor, R.id.btn_ch4_range_motor},
            {R.id.tv_ch5_range_motor, R.id.tv_ch5_warning_low_motor, R.id.tv_ch5_warning_high_motor, R.id.tv_ch5_ok_motor, R.id.sp_ch5_range_motor, R.id.et_ch5_warning_low_motor, R.id.et_ch5_warning_high_motor, R.id.btn_ch5_range_motor},
            {R.id.tv_ch6_range_motor, R.id.tv_ch6_warning_low_motor, R.id.tv_ch6_warning_high_motor, R.id.tv_ch6_ok_motor, R.id.sp_ch6_range_motor, R.id.et_ch6_warning_low_motor, R.id.et_ch6_warning_high_motor, R.id.btn_ch6_range_motor},
            {R.id.tv_ch7_range_motor, R.id.tv_ch7_warning_low_motor, R.id.tv_ch7_warning_high_motor, R.id.tv_ch7_ok_motor, R.id.sp_ch7_range_motor, R.id.et_ch7_warning_low_motor, R.id.et_ch7_warning_high_motor, R.id.btn_ch7_range_motor},
            {R.id.tv_ch8_range_motor, R.id.tv_ch8_warning_low_motor, R.id.tv_ch8_warning_high_motor, R.id.tv_ch8_ok_motor, R.id.sp_ch8_range_motor, R.id.et_ch8_warning_low_motor, R.id.et_ch8_warning_high_motor, R.id.btn_ch8_range_motor},
    };
    private int mTvChIndex = 0;
    private int mTvChLength = 4;
    private TextView[][] mTvMineChRange = new TextView[mChCount][mTvChLength];
    private int mSpChIndex = mTvChIndex + mTvChLength;
    private int mSpChLength = 1;
    private Spinner[][] mSpMineChRange = new Spinner[mChCount][mSpChLength];
    private int mEtChIndex = mSpChIndex + mSpChLength;
    private int mEtChLength = 2;
    private EditText[][] mEtMineChWarning = new EditText[mChCount][mEtChLength];
    private int mBtnChIndex = mEtChIndex + mEtChLength;
    private int mBtnChLength = 1;
    private Button[][] mBtnMineChRange = new Button[mChCount][mBtnChLength];

    private ImageView mIvAboutApp;
    private LinearLayout mLlAboutAppDetail;
    private Boolean mIvAboutAppStatus;
    private ImageView mIvAboutUs;
    private LinearLayout mLlAboutUsDetail;
    private Boolean mIvAboutUsStatus;
    private ImageView mIvMore;
    private LinearLayout mLlMoreDetail;
    private Boolean mIvMoreStatus;
    private ImageView mIvGuide;
    private LinearLayout mLlGuideDetail;
    private Boolean mIvGuideStatus;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return Objects.requireNonNull(inflater).inflate(R.layout.fragment_mine, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBtnMineLogin = view.findViewById(R.id.btn_mine_login);
        mTvMineWelcome = view.findViewById(R.id.tv_mine_welcome);
        mIvMyInfo = view.findViewById(R.id.iv_myinfo_detail);
        mIvDevice = view.findViewById(R.id.iv_device_detail);
        mIvMyInfoStatus = false;
        mIvDeviceStatus = false;
        mLlMyInfo = view.findViewById(R.id.ll_myinfo);
        mLlDevice = view.findViewById(R.id.ll_device);
        mTvMineUsername = view.findViewById(R.id.tv_mine_username);
        mTvMinePassword = view.findViewById(R.id.tv_mine_password);
        mTvMineCompany = view.findViewById(R.id.tv_mine_company);
        mTvMineDepartment = view.findViewById(R.id.tv_mine_department);
        mTvMineRole = view.findViewById(R.id.tv_mine_role);
        mUserInfo = Util.getUserInfoFromSP(getContext());
        if (mUserInfo != null) {
            mBtnMineLogin.setVisibility(View.GONE);
            mTvMineWelcome.setVisibility(View.VISIBLE);
            mTvMineWelcome.setText(mUserInfo.mUsername);
            mTvMineUsername.setText(mUserInfo.mUsername);
            mTvMinePassword.setText(mUserInfo.mPassword);
            mTvMineCompany.setText(mUserInfo.mCompany);
            mTvMineDepartment.setText(mUserInfo.mDepartment);
            mTvMineRole.setText(mUserInfo.mRole);
            new Thread(mRunnableGetDevice).start();
        }

        mDeviceInfo = Util.getDeviceInfoFromSP(getContext());
        mTvMinePump = view.findViewById(R.id.tv_mine_pump);
        mEtMinePump = view.findViewById(R.id.et_mine_pump);
        mBtnMinePump = view.findViewById(R.id.btn_mine_pump);
        Objects.requireNonNull(getContext());
        mTvMinePump.setText(Util.getPumpNameFromSP(getContext()));
        mEtMinePump.setText(Util.getPumpNameFromSP(getContext()));

        for(int i = 0; i < mDeviceInfoCount; i++) {
            for(int j = mTvDeviceInfoIndex; j < mTvDeviceInfoIndex + mTvDeviceInfoLength; j++) {
                mTvDeviceInfo[i][j-mTvDeviceInfoIndex] = view.findViewById(mTvDeviceInfoId[i][j]);
            }
            for (int j = mBtnDeviceInfoIndex; j < mBtnDeviceInfoIndex +mBtnDeviceInfoLength; j++) {
                mBtnDeviceCheck[i][j-mBtnDeviceInfoIndex] = view.findViewById(mTvDeviceInfoId[i][j]);
            }
            for (int j = mTbDeviceInfoIndex; j < mTbDeviceInfoIndex +mTbDeviceInfoLength; j++) {
                mTbDeviceInfo[i][j-mTbDeviceInfoIndex] = view.findViewById(mTvDeviceInfoId[i][j]);
            }
        }

        for(int i=0;i<mChCount ;i++) {
            for(int j = mTvChIndex; j< mTvChIndex + mTvChLength; j++) {
                mTvMineChRange[i][j-mTvChIndex] = view.findViewById(mTvRangeWarningId[i][j]);
            }
            for(int j = mSpChIndex; j< mSpChIndex + mSpChLength; j++) {
                mSpMineChRange[i][j-mSpChIndex] = view.findViewById(mTvRangeWarningId[i][j]);
            }
            for(int j = mEtChIndex; j< mEtChIndex + mEtChLength; j++) {
                mEtMineChWarning[i][j-mEtChIndex] = view.findViewById(mTvRangeWarningId[i][j]);
            }
            for(int j = mBtnChIndex; j< mBtnChIndex + mBtnChLength; j++) {
                mBtnMineChRange[i][j-mBtnChIndex] = view.findViewById(mTvRangeWarningId[i][j]);
            }
            String range = mTvMineChRange[i][0].getText().toString();
            String warningLow = mTvMineChRange[i][1].getText().toString();
            String warningHigh = mTvMineChRange[i][2].getText().toString();
            Util.setChRangeWarning(i, range, warningLow, warningHigh);
        }
        mIvAboutApp = view.findViewById(R.id.iv_service_about_app);
        mLlAboutAppDetail = view.findViewById(R.id.ll_service_about_app_detail);
        mIvAboutAppStatus = false;
        mIvAboutUs = view.findViewById(R.id.iv_service_about_us);
        mLlAboutUsDetail = view.findViewById(R.id.ll_service_about_us_detail);
        mIvAboutUsStatus = false;
        mIvMore = view.findViewById(R.id.iv_service_more);
        mLlMoreDetail = view.findViewById(R.id.ll_service_more_detail);
        mIvMoreStatus = false;
        mIvGuide = view.findViewById(R.id.iv_service_guide);
        mLlGuideDetail = view.findViewById(R.id.ll_service_guide_detail);
        mIvGuideStatus = false;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mTvMineWelcome.setOnClickListener(mClickEvent);
        mBtnMineLogin.setOnClickListener(mClickEvent);
        mIvMyInfo.setOnClickListener(mClickEvent);
        mIvDevice.setOnClickListener(mClickEvent);
        mTvMinePump.setOnClickListener(mClickEvent);
        mBtnMinePump.setOnClickListener(mClickEvent);

        for(int i = 0; i < mDeviceInfoCount; i++) {
            for (int j = mBtnDeviceInfoIndex; j < mBtnDeviceInfoIndex +mBtnDeviceInfoLength; j++) {
                mBtnDeviceCheck[i][j-mBtnDeviceInfoIndex].setOnClickListener(mClickEvent);
            }
        }
        for(int i=0;i<mChCount ;i++) {
            for(int j = mTvChIndex; j< mTvChIndex + mTvChLength; j++) {
                mTvMineChRange[i][j-mTvChIndex].setOnClickListener(mClickEvent);
            }
            for(int j = mBtnChIndex; j< mBtnChIndex + mBtnChLength; j++) {
                mBtnMineChRange[i][j-mBtnChIndex].setOnClickListener(mClickEvent);
            }
        }
        mIvAboutApp.setOnClickListener(mClickEvent);
        mIvAboutUs.setOnClickListener(mClickEvent);
        mIvMore.setOnClickListener(mClickEvent);
        mIvGuide.setOnClickListener(mClickEvent);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== REQUEST_CODE){
            final String username= data.getStringExtra(Util.USERNAME);
            final String password= data.getStringExtra(Util.PASSWORD);
            mFragment.dismiss();
            mBtnMineLogin.post(new Runnable() {
                @Override
                public void run() {
                    mTvMineUsername.setText(username);
                    mTvMinePassword.setText(password);
                    mTvMineWelcome.setText(username);
                    mTvMineWelcome.setVisibility(View.VISIBLE);
                    mBtnMineLogin.setVisibility(View.GONE);
                }
            });
            mUserInfo = new UserInfo(username, password, mTvMineCompany.getText().toString(), mTvMineDepartment.getText().toString(), mTvMineRole.getText().toString());
            Objects.requireNonNull(getContext());
            Util.setUserInfoToSP(getContext(), mUserInfo);
            new Thread(mRunnableGetDevice).start();
        }
    }
    private Runnable mRunnableGetDevice = new Runnable() {
        @Override
        public void run() {
            Objects.requireNonNull(getContext());
            String url = "https://www.shsmartcloud.com/index.php/index/index/account2device";
            String param = String.format("username=%s&password=%s", mUserInfo.mUsername, Util.md5Decode(mUserInfo.mPassword));
            String result = Util.httpSendPost(url, param);
            mDeviceInfoList = Util.getDeviceInfoListFromJson(result);
            if(mDeviceInfoList.size() == 0) {
                return;
            }
            mBtnMineLogin.post(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < mDeviceInfoList.size() && i < mDeviceInfoCount; i++) {
                        DeviceInfo deviceInfo = mDeviceInfoList.get(i);
                        mTvDeviceInfo[i][0].setText(deviceInfo.productKey);
                        mTvDeviceInfo[i][1].setText(deviceInfo.productSecret);
                        mTvDeviceInfo[i][2].setText(deviceInfo.deviceName);
                        mTvDeviceInfo[i][3].setText(deviceInfo.deviceSecret);
                    }
                    mDeviceInfo = mDeviceInfoList.get(0);
                    mBtnDeviceCheck[0][0].setChecked(true);
                    //initPump();
                    Util.setDeviceInfoToSP(getContext(), mDeviceInfo);
                    boolean type = mTbDeviceInfo[0][0].isChecked();
                    if(type) {
                        Util.mDeviceType = 1;
                    } else {
                        Util.mDeviceType = 2;
                    }
                    new Thread(mIotStartRunnable).start();
                }
            });
        }
    };
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            Objects.requireNonNull(getContext());
            mTvMinePump.setText(Util.getPumpNameFromSP(getContext()));
            mEtMinePump.setText(Util.getPumpNameFromSP(getContext()));
        }
    }
    public View.OnClickListener mClickEvent = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Objects.requireNonNull(getContext());
            Objects.requireNonNull(getActivity());
            Drawable drawClose = ContextCompat.getDrawable(getContext(), R.drawable.mine_detail);
            Drawable drawOpen = ContextCompat.getDrawable(getContext(), R.drawable.mine_detail_open);
            int id = v.getId();
            switch (id) {
                case R.id.tv_mine_welcome:
                    mUserInfo = null;
                    Util.setUserInfoToSP(getContext(), mUserInfo);
                    mTvMineWelcome.setVisibility(View.GONE);
                    mBtnMineLogin.setVisibility(View.VISIBLE);
                    break;
                case R.id.tv_mine_pump:
                    mTvMinePump.setVisibility(View.GONE);
                    mEtMinePump.setVisibility(View.VISIBLE);
                    mBtnMinePump.setVisibility(View.VISIBLE);
                    break;
                case R.id.btn_mine_pump:
                    String name = mEtMinePump.getText().toString();
                    mTvMinePump.setText(name);
                    mTvMinePump.setVisibility(View.VISIBLE);
                    mEtMinePump.setVisibility(View.GONE);
                    mBtnMinePump.setVisibility(View.GONE);
                    break;
                case R.id.btn_mine_login:
                    mFragment = new FragmentDialogLogin();
                    mFragment.setTargetFragment(FragmentMine.this, REQUEST_CODE);
                    mFragment.show(Objects.requireNonNull(getFragmentManager()), "login");
                    break;
                case R.id.iv_myinfo_detail:
                    if (mIvMyInfoStatus) {
                        mIvMyInfo.setImageDrawable(drawClose);
                        mIvMyInfoStatus = false;
                        mLlMyInfo.setVisibility(View.GONE);
                    } else {
                        mIvMyInfo.setImageDrawable(drawOpen);
                        mIvMyInfoStatus = true;
                        mLlMyInfo.setVisibility(View.VISIBLE);
                    }
                    break;
                case R.id.iv_device_detail:
                    if (mIvDeviceStatus) {
                        mIvDevice.setImageDrawable(drawClose);
                        mIvDeviceStatus = false;
                        mLlDevice.setVisibility(View.GONE);
                    } else {
                        mIvDevice.setImageDrawable(drawOpen);
                        mIvDeviceStatus = true;
                        mLlDevice.setVisibility(View.VISIBLE);
                    }
                    break;

                case R.id.btn_product1_key:
                    mBtnDeviceSettingClick(R.id.btn_product1_key);
                    if(mTbDeviceInfo[0][0].isChecked()) {
                        Util.mDeviceType = 1;
                    } else {
                        Util.mDeviceType = 2;
                    }
                    break;
                case R.id.btn_product2_key:
                    mBtnDeviceSettingClick(R.id.btn_product2_key);
                    if(mTbDeviceInfo[1][0].isChecked()) {
                        Util.mDeviceType = 1;
                    } else {
                        Util.mDeviceType = 2;
                    }
                    break;

                case R.id.tv_ch1_range:
                    mTvRangeClick(R.id.tv_ch1_range);
                    break;
                case R.id.tv_ch1_warning_low:
                    mTvWarningLowClick(R.id.tv_ch1_warning_low);
                    break;
                case R.id.tv_ch1_warning_high:
                    mTvWarningHighClick(R.id.tv_ch1_warning_high);
                    break;
                case R.id.btn_ch1_range:
                    mBtnRangeWarningClick(R.id.btn_ch1_range);
                    break;

                case R.id.tv_ch2_range:
                    mTvRangeClick(R.id.tv_ch2_range);
                    break;
                case R.id.tv_ch2_warning_low:
                    mTvWarningLowClick(R.id.tv_ch2_warning_low);
                    break;
                case R.id.tv_ch2_warning_high:
                    mTvWarningHighClick(R.id.tv_ch2_warning_high);
                    break;
                case R.id.btn_ch2_range:
                    mBtnRangeWarningClick(R.id.btn_ch2_range);
                    break;

                case R.id.tv_ch3_range:
                    mTvRangeClick(R.id.tv_ch3_range);
                    break;
                case R.id.tv_ch3_warning_low:
                    mTvWarningLowClick(R.id.tv_ch3_warning_low);
                    break;
                case R.id.tv_ch3_warning_high:
                    mTvWarningHighClick(R.id.tv_ch3_warning_high);
                    break;
                case R.id.btn_ch3_range:
                    mBtnRangeWarningClick(R.id.btn_ch3_range);
                    break;

                case R.id.tv_ch4_range:
                    mTvRangeClick(R.id.tv_ch4_range);
                    break;
                case R.id.tv_ch4_warning_low:
                    mTvWarningLowClick(R.id.tv_ch4_warning_low);
                    break;
                case R.id.tv_ch4_warning_high:
                    mTvWarningHighClick(R.id.tv_ch4_warning_high);
                    break;
                case R.id.btn_ch4_range:
                    mBtnRangeWarningClick(R.id.btn_ch4_range);
                    break;

                case R.id.tv_ch5_range:
                    mTvRangeClick(R.id.tv_ch5_range);
                    break;
                case R.id.tv_ch5_warning_low:
                    mTvWarningLowClick(R.id.tv_ch5_warning_low);
                    break;
                case R.id.tv_ch5_warning_high:
                    mTvWarningHighClick(R.id.tv_ch5_warning_high);
                    break;
                case R.id.btn_ch5_range:
                    mBtnRangeWarningClick(R.id.btn_ch5_range);
                    break;

                case R.id.tv_ch6_range:
                    mTvRangeClick(R.id.tv_ch6_range);
                    break;
                case R.id.tv_ch6_warning_low:
                    mTvWarningLowClick(R.id.tv_ch6_warning_low);
                    break;
                case R.id.tv_ch6_warning_high:
                    mTvWarningHighClick(R.id.tv_ch6_warning_high);
                    break;
                case R.id.btn_ch6_range:
                    mBtnRangeWarningClick(R.id.btn_ch6_range);
                    break;

                case R.id.tv_ch7_range:
                    mTvRangeClick(R.id.tv_ch7_range);
                    break;
                case R.id.tv_ch7_warning_low:
                    mTvWarningLowClick(R.id.tv_ch7_warning_low);
                    break;
                case R.id.tv_ch7_warning_high:
                    mTvWarningHighClick(R.id.tv_ch7_warning_high);
                    break;
                case R.id.btn_ch7_range:
                    mBtnRangeWarningClick(R.id.btn_ch7_range);
                    break;

                case R.id.tv_ch8_range:
                    mTvRangeClick(R.id.tv_ch8_range);
                    break;
                case R.id.tv_ch8_warning_low:
                    mTvWarningLowClick(R.id.tv_ch8_warning_low);
                    break;
                case R.id.tv_ch8_warning_high:
                    mTvWarningHighClick(R.id.tv_ch8_warning_high);
                    break;
                case R.id.btn_ch8_range:
                    mBtnRangeWarningClick(R.id.btn_ch8_range);
                    break;

                case R.id.tv_ch1_range_motor:
                    mTvRangeClick(R.id.tv_ch1_range_motor);
                    break;
                case R.id.tv_ch1_warning_low_motor:
                    mTvWarningLowClick(R.id.tv_ch1_warning_low_motor);
                    break;
                case R.id.tv_ch1_warning_high_motor:
                    mTvWarningHighClick(R.id.tv_ch1_warning_high_motor);
                    break;
                case R.id.btn_ch1_range_motor:
                    mBtnRangeWarningClick(R.id.btn_ch1_range_motor);
                    break;

                case R.id.tv_ch2_range_motor:
                    mTvRangeClick(R.id.tv_ch2_range_motor);
                    break;
                case R.id.tv_ch2_warning_low_motor:
                    mTvWarningLowClick(R.id.tv_ch2_warning_low_motor);
                    break;
                case R.id.tv_ch2_warning_high_motor:
                    mTvWarningHighClick(R.id.tv_ch2_warning_high_motor);
                    break;
                case R.id.btn_ch2_range_motor:
                    mBtnRangeWarningClick(R.id.btn_ch2_range_motor);
                    break;

                case R.id.tv_ch3_range_motor:
                    mTvRangeClick(R.id.tv_ch3_range_motor);
                    break;
                case R.id.tv_ch3_warning_low_motor:
                    mTvWarningLowClick(R.id.tv_ch3_warning_low_motor);
                    break;
                case R.id.tv_ch3_warning_high_motor:
                    mTvWarningHighClick(R.id.tv_ch3_warning_high_motor);
                    break;
                case R.id.btn_ch3_range_motor:
                    mBtnRangeWarningClick(R.id.btn_ch3_range_motor);
                    break;

                case R.id.tv_ch4_range_motor:
                    mTvRangeClick(R.id.tv_ch4_range_motor);
                    break;
                case R.id.tv_ch4_warning_low_motor:
                    mTvWarningLowClick(R.id.tv_ch4_warning_low_motor);
                    break;
                case R.id.tv_ch4_warning_high_motor:
                    mTvWarningHighClick(R.id.tv_ch4_warning_high_motor);
                    break;
                case R.id.btn_ch4_range_motor:
                    mBtnRangeWarningClick(R.id.btn_ch4_range_motor);
                    break;

                case R.id.tv_ch5_range_motor:
                    mTvRangeClick(R.id.tv_ch5_range_motor);
                    break;
                case R.id.tv_ch5_warning_low_motor:
                    mTvWarningLowClick(R.id.tv_ch5_warning_low_motor);
                    break;
                case R.id.tv_ch5_warning_high_motor:
                    mTvWarningHighClick(R.id.tv_ch5_warning_high_motor);
                    break;
                case R.id.btn_ch5_range_motor:
                    mBtnRangeWarningClick(R.id.btn_ch5_range_motor);
                    break;

                case R.id.tv_ch6_range_motor:
                    mTvRangeClick(R.id.tv_ch6_range_motor);
                    break;
                case R.id.tv_ch6_warning_low_motor:
                    mTvWarningLowClick(R.id.tv_ch6_warning_low_motor);
                    break;
                case R.id.tv_ch6_warning_high_motor:
                    mTvWarningHighClick(R.id.tv_ch6_warning_high_motor);
                    break;
                case R.id.btn_ch6_range_motor:
                    mBtnRangeWarningClick(R.id.btn_ch6_range_motor);
                    break;

                case R.id.tv_ch7_range_motor:
                    mTvRangeClick(R.id.tv_ch7_range_motor);
                    break;
                case R.id.tv_ch7_warning_low_motor:
                    mTvWarningLowClick(R.id.tv_ch7_warning_low_motor);
                    break;
                case R.id.tv_ch7_warning_high_motor:
                    mTvWarningHighClick(R.id.tv_ch7_warning_high_motor);
                    break;
                case R.id.btn_ch7_range_motor:
                    mBtnRangeWarningClick(R.id.btn_ch7_range_motor);
                    break;

                case R.id.tv_ch8_range_motor:
                    mTvRangeClick(R.id.tv_ch8_range_motor);
                    break;
                case R.id.tv_ch8_warning_low_motor:
                    mTvWarningLowClick(R.id.tv_ch8_warning_low_motor);
                    break;
                case R.id.tv_ch8_warning_high_motor:
                    mTvWarningHighClick(R.id.tv_ch8_warning_high_motor);
                    break;
                case R.id.btn_ch8_range_motor:
                    mBtnRangeWarningClick(R.id.btn_ch8_range_motor);
                    break;
                case R.id.iv_service_guide:
                    if (mIvGuideStatus) {
                        mIvGuide.setImageDrawable(drawClose);
                        mIvGuideStatus = false;
                        mLlGuideDetail.setVisibility(View.GONE);
                    } else {
                        mIvGuide.setImageDrawable(drawOpen);
                        mIvGuideStatus = true;
                        mLlGuideDetail.setVisibility(View.VISIBLE);
                    }
                    break;
                case R.id.iv_service_more:
                    if (mIvMoreStatus) {
                        mIvMore.setImageDrawable(drawClose);
                        mIvMoreStatus = false;
                        mLlMoreDetail.setVisibility(View.GONE);
                    } else {
                        mIvMore.setImageDrawable(drawOpen);
                        mIvMoreStatus = true;
                        mLlMoreDetail.setVisibility(View.VISIBLE);
                    }
                    break;
                case R.id.iv_service_about_us:
                    if (mIvAboutUsStatus) {
                        mIvAboutUs.setImageDrawable(drawClose);
                        mIvAboutUsStatus = false;
                        mLlAboutUsDetail.setVisibility(View.GONE);
                    } else {
                        mIvAboutUs.setImageDrawable(drawOpen);
                        mIvAboutUsStatus = true;
                        mLlAboutUsDetail.setVisibility(View.VISIBLE);
                    }
                    break;
                case R.id.iv_service_about_app:
                    if (mIvAboutAppStatus) {
                        mIvAboutApp.setImageDrawable(drawClose);
                        mIvAboutAppStatus = false;
                        mLlAboutAppDetail.setVisibility(View.GONE);
                    } else {
                        mIvAboutApp.setImageDrawable(drawOpen);
                        mIvAboutAppStatus = true;
                        mLlAboutAppDetail.setVisibility(View.VISIBLE);
                    }
                    break;
                default:
                    break;
            }
        }
    };
    private void mBtnDeviceSettingClick(int id){
        int index=0;
        for(int i = 0; i < mDeviceInfoCount; i++) {
            for (int j = mBtnDeviceInfoIndex; j < mBtnDeviceInfoIndex +mBtnDeviceInfoLength; j++) {
                if (id == mTvDeviceInfoId[i][j]) {
                    index = i;
                    mBtnDeviceCheck[i][j-mBtnDeviceInfoIndex].setChecked(true);
                } else {
                    mBtnDeviceCheck[i][j-mBtnDeviceInfoIndex].setChecked(false);
                }
            }
        }
        Objects.requireNonNull(getContext());
        mDeviceInfo.productKey = mTvDeviceInfo[index][0].getText().toString();
        mDeviceInfo.productSecret = mTvDeviceInfo[index][1].getText().toString();
        mDeviceInfo.deviceName = mTvDeviceInfo[index][2].getText().toString();
        mDeviceInfo.deviceSecret = mTvDeviceInfo[index][3].getText().toString();
        if (mDeviceInfo == null || mDeviceInfo.productKey.isEmpty() || mDeviceInfo.deviceName.isEmpty()) {
            return;
        }
        Util.setDeviceInfoToSP(getContext(), mDeviceInfo);
        new Thread(mIotStartRunnable).start();
    }

    private void mTvRangeClick(int id) {
        int index=0;
        for(int i=0;i<mChCount ;i++) {
            for(int j = mTvChIndex; j< mTvChIndex + mTvChLength; j++) {
                if (id == mTvRangeWarningId[i][j]) {
                    index = i;
                    break;
                }
            }
        }
        mTvMineChRange[index][0].setVisibility(View.GONE);
        mSpMineChRange[index][0].setVisibility(View.VISIBLE);
        mTvMineChRange[index][3].setVisibility(View.GONE);
        mBtnMineChRange[index][0].setVisibility(View.VISIBLE);
    }

    private void mTvWarningLowClick(int id) {
        int index=0;
        for(int i=0;i<mChCount ;i++) {
            for(int j = mTvChIndex; j< mTvChIndex + mTvChLength; j++) {
                if (id == mTvRangeWarningId[i][j]) {
                    index = i;
                }
            }
        }
        mEtMineChWarning[index][0].setText(mTvMineChRange[index][1].getText());
        mTvMineChRange[index][1].setVisibility(View.GONE);
        mEtMineChWarning[index][0].setVisibility(View.VISIBLE);
        mTvMineChRange[index][3].setVisibility(View.GONE);
        mBtnMineChRange[index][0].setVisibility(View.VISIBLE);
    }

    private void mTvWarningHighClick(int id) {
        int index=0;
        for(int i=0;i<mChCount ;i++) {
            for(int j = mTvChIndex; j< mTvChIndex + mTvChLength; j++) {
                if (id == mTvRangeWarningId[i][j]) {
                    index = i;
                }
            }
        }
        mEtMineChWarning[index][1].setText(mTvMineChRange[index][2].getText());
        mTvMineChRange[index][2].setVisibility(View.GONE);
        mEtMineChWarning[index][1].setVisibility(View.VISIBLE);
        mTvMineChRange[index][3].setVisibility(View.GONE);
        mBtnMineChRange[index][0].setVisibility(View.VISIBLE);
    }

    private void mBtnRangeWarningClick(int id) {
        int index=0;
        for(int i=0;i<mChCount;i++) {
            for(int j = mBtnChIndex; j< mBtnChIndex + mBtnChLength; j++) {
                if (id == mTvRangeWarningId[i][j]) {
                    index = i;
                }
            }
        }
        String range = mSpMineChRange[index][0].getSelectedItem().toString();
        String warningLow = mEtMineChWarning[index][0].getText().toString();
        String warningHigh = mEtMineChWarning[index][1].getText().toString();
        mTvMineChRange[index][0].setText(range);
        mTvMineChRange[index][1].setText(warningLow);
        mTvMineChRange[index][2].setText(warningHigh);
        mTvMineChRange[index][0].setVisibility(View.VISIBLE);
        mSpMineChRange[index][0].setVisibility(View.GONE);
        mTvMineChRange[index][1].setVisibility(View.VISIBLE);
        mEtMineChWarning[index][0].setVisibility(View.GONE);
        mTvMineChRange[index][2].setVisibility(View.VISIBLE);
        mEtMineChWarning[index][1].setVisibility(View.GONE);
        mTvMineChRange[index][3].setVisibility(View.VISIBLE);
        mBtnMineChRange[index][0].setVisibility(View.GONE);
        Util.setChRangeWarning(index, range, warningLow, warningHigh);
        Objects.requireNonNull(getActivity());
        MainActivity activity = (MainActivity) getActivity();
        if (activity.mIotManager != null) {
            int chIndex = 0;
            switch (Util.mDeviceType) {
                case 1:
                    chIndex = index;
                    break;
                case 2:
                    chIndex = index - 8;
                    break;
                default:
                    break;
            }
            String cmd = String.format("{'cmd':'cpp%s[%s][%s]'}", chIndex, Util.mChRangeWarning[index][4], Util.mChRangeWarning[index][5]);
            activity.mIotManager.publish(cmd);
        }
    }

    Runnable mIotStartRunnable = new Runnable() {
        @Override
        public void run() {
            synchronized (FragmentData.class) {
                Objects.requireNonNull(getActivity());
                MainActivity activity = (MainActivity) getActivity();
                if (activity.mIotManager != null) {
                    activity.mIotManager.unRegister();
                    activity.mIotManager.unSubscribe();
                    activity.mIotManager.disConnect();
                    Util.mDataCollect.mFragmentData.clearView();
                    activity.mIotManager.connect();
                }
            }
        }
    };
}
