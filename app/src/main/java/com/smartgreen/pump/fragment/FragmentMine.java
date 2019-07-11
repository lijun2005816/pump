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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

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
    private int mDeviceInfoCount = 3;
    private int[][] mTvDeviceInfoId = {
            {R.id.tv_product1_key, R.id.tv_product1_secret, R.id.tv_device1_key, R.id.tv_device1_secret, R.id.btn_product1_key},
            {R.id.tv_product2_key, R.id.tv_product2_secret, R.id.tv_device2_key, R.id.tv_device2_secret, R.id.btn_product2_key},
            {R.id.tv_product3_key, R.id.tv_product3_secret, R.id.tv_device3_key, R.id.tv_device3_secret, R.id.btn_product3_key},
    };
    private int mTvDeviceInfoIndex = 0;
    private int mTvDeviceInfoLength = 4;
    private TextView[][] mTvDeviceInfo = new TextView[mDeviceInfoCount][mTvDeviceInfoLength];
    private int mBtnDeviceInfoIndex = mTvDeviceInfoIndex + mTvDeviceInfoLength;
    private int mBtnDeviceInfoLength = 1;
    private RadioButton[][] mBtnDeviceCheck = new RadioButton[mDeviceInfoCount][mBtnDeviceInfoLength];

    private int mChCount = 8;
    private int[][] mTvRangeWarningId = {
            {R.id.tv_ch1_range, R.id.tv_ch1_warning_low, R.id.tv_ch1_warning_high, R.id.tv_ch1_ok, R.id.sp_ch1_range, R.id.et_ch1_warning_low, R.id.et_ch1_warning_high, R.id.btn_ch1_range},
            {R.id.tv_ch2_range, R.id.tv_ch2_warning_low, R.id.tv_ch2_warning_high, R.id.tv_ch2_ok, R.id.sp_ch2_range, R.id.et_ch2_warning_low, R.id.et_ch2_warning_high, R.id.btn_ch2_range},
            {R.id.tv_ch3_range, R.id.tv_ch3_warning_low, R.id.tv_ch3_warning_high, R.id.tv_ch3_ok, R.id.sp_ch3_range, R.id.et_ch3_warning_low, R.id.et_ch3_warning_high, R.id.btn_ch3_range},
            {R.id.tv_ch4_range, R.id.tv_ch4_warning_low, R.id.tv_ch4_warning_high, R.id.tv_ch4_ok, R.id.sp_ch4_range, R.id.et_ch4_warning_low, R.id.et_ch4_warning_high, R.id.btn_ch4_range},
            {R.id.tv_ch5_range, R.id.tv_ch5_warning_low, R.id.tv_ch5_warning_high, R.id.tv_ch5_ok, R.id.sp_ch5_range, R.id.et_ch5_warning_low, R.id.et_ch5_warning_high, R.id.btn_ch5_range},
            {R.id.tv_ch6_range, R.id.tv_ch6_warning_low, R.id.tv_ch6_warning_high, R.id.tv_ch6_ok, R.id.sp_ch6_range, R.id.et_ch6_warning_low, R.id.et_ch6_warning_high, R.id.btn_ch6_range},
            {R.id.tv_ch7_range, R.id.tv_ch7_warning_low, R.id.tv_ch7_warning_high, R.id.tv_ch7_ok, R.id.sp_ch7_range, R.id.et_ch7_warning_low, R.id.et_ch7_warning_high, R.id.btn_ch7_range},
            {R.id.tv_ch8_range, R.id.tv_ch8_warning_low, R.id.tv_ch8_warning_high, R.id.tv_ch8_ok, R.id.sp_ch8_range, R.id.et_ch8_warning_low, R.id.et_ch8_warning_high, R.id.btn_ch8_range},
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
        }
        for(int i = 0; i < mDeviceInfoCount; i++) {
            for (int j = mBtnDeviceInfoIndex; j < mBtnDeviceInfoIndex +mBtnDeviceInfoLength; j++) {
                mBtnDeviceCheck[i][j-mBtnDeviceInfoIndex] = view.findViewById(mTvDeviceInfoId[i][j]);
            }
        }

        for(int i=0;i<mChCount ;i++) {
            for(int j = mTvChIndex; j< mTvChIndex + mTvChLength; j++) {
                mTvMineChRange[i][j-mTvChIndex] = view.findViewById(mTvRangeWarningId[i][j]);
            }
        }
        for(int i=0;i<mChCount;i++) {
            for(int j = mSpChIndex; j< mSpChIndex + mSpChLength; j++) {
                mSpMineChRange[i][j-mSpChIndex] = view.findViewById(mTvRangeWarningId[i][j]);
            }
        }
        for(int i=0;i<mChCount;i++) {
            for(int j = mEtChIndex; j< mEtChIndex + mEtChLength; j++) {
                mEtMineChWarning[i][j-mEtChIndex] = view.findViewById(mTvRangeWarningId[i][j]);
            }
        }
        for(int i=0;i<mChCount;i++) {
            for(int j = mBtnChIndex; j< mBtnChIndex + mBtnChLength; j++) {
                mBtnMineChRange[i][j-mBtnChIndex] = view.findViewById(mTvRangeWarningId[i][j]);
            }
        }
        for(int i = 0; i< mChCount; i++) {
            String range = mTvMineChRange[i][0].getText().toString();
            String warningLow = mTvMineChRange[i][1].getText().toString();
            String warningHigh = mTvMineChRange[i][2].getText().toString();
            Util.setChRangeWarning(i, range, warningLow, warningHigh);
        }
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
        }
        for(int i=0;i<mChCount;i++) {
            for(int j = mBtnChIndex; j< mBtnChIndex + mBtnChLength; j++) {
                mBtnMineChRange[i][j-mBtnChIndex].setOnClickListener(mClickEvent);
            }
        }
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
                    initPump();
                    Util.setDeviceInfoToSP(getContext(), mDeviceInfo);
                    new Thread(mIotStartRunnable).start();
                }
            });
        }
    };
    private void initPump(){
        Objects.requireNonNull(getContext());
        String[] tempRange = getContext().getResources().getStringArray(R.array.temp_range);
        ArrayAdapter<String> aa = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, tempRange);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        String tempDefault = getContext().getResources().getString(R.string.temp_range_default);
        String tempLow = getContext().getResources().getString(R.string.temp_warning_low_default);
        String tempHigh = getContext().getResources().getString(R.string.temp_warning_high_default);
        String tempUnit = getContext().getResources().getString(R.string.temp_unit);
        for(int i=0;i<2;i++) {
            mSpMineChRange[i][0].setAdapter(aa);
            mTvMineChRange[i][0].setText(tempDefault);
            mTvMineChRange[i][1].setText(tempLow);
            mTvMineChRange[i][2].setText(tempHigh);
            mTvMineChRange[i][3].setText(tempUnit);
        }
        String[] vibRange = getContext().getResources().getStringArray(R.array.vib_range);
        ArrayAdapter<String> bb = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, vibRange);
        bb.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        String vibDefault = getContext().getResources().getString(R.string.vib_range_default);
        String vibLow = getContext().getResources().getString(R.string.vib_warning_low_default);
        String vibHigh = getContext().getResources().getString(R.string.vib_warning_high_default);
        String vibUnit = getContext().getResources().getString(R.string.vib_unit);
        for(int i=2;i<6;i++) {
            mSpMineChRange[i][0].setAdapter(bb);
            mTvMineChRange[i][0].setText(vibDefault);
            mTvMineChRange[i][1].setText(vibLow);
            mTvMineChRange[i][2].setText(vibHigh);
            mTvMineChRange[i][3].setText(vibUnit);
        }
        String[] pressRange = getContext().getResources().getStringArray(R.array.press_range);
        ArrayAdapter<String> cc = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, pressRange);
        cc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        String pressDefault = getContext().getResources().getString(R.string.press_range_default);
        String pressLow = getContext().getResources().getString(R.string.press_warning_low_default);
        String pressHigh = getContext().getResources().getString(R.string.press_warning_high_default);
        String pressUnit = getContext().getResources().getString(R.string.press_unit);
        for(int i=6;i<7;i++) {
            mSpMineChRange[i][0].setAdapter(cc);
            mTvMineChRange[i][0].setText(pressDefault);
            mTvMineChRange[i][1].setText(pressLow);
            mTvMineChRange[i][2].setText(pressHigh);
            mTvMineChRange[i][3].setText(pressUnit);
        }
        String[] volumeRange = getContext().getResources().getStringArray(R.array.volume_range);
        ArrayAdapter<String> dd = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, volumeRange);
        dd.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        String volumeDefault = getContext().getResources().getString(R.string.volume_range_default);
        String volumeLow = getContext().getResources().getString(R.string.volume_warning_low_default);
        String volumeHigh = getContext().getResources().getString(R.string.volume_warning_high_default);
        String volumeUnit = getContext().getResources().getString(R.string.volume_unit);
        for(int i=7;i<8;i++) {
            mSpMineChRange[i][0].setAdapter(dd);
            mTvMineChRange[i][0].setText(volumeDefault);
            mTvMineChRange[i][1].setText(volumeLow);
            mTvMineChRange[i][2].setText(volumeHigh);
            mTvMineChRange[i][3].setText(volumeUnit);
        }
        for(int i = 0; i< mChCount; i++) {
            String range = mTvMineChRange[i][0].getText().toString();
            String warningLow = mTvMineChRange[i][1].getText().toString();
            String warningHigh = mTvMineChRange[i][2].getText().toString();
            Util.setChRangeWarning(i, range, warningLow, warningHigh);
        }
    }
    private void initMotor(){
        Objects.requireNonNull(getContext());
        String[] tempRange = getContext().getResources().getStringArray(R.array.temp_range);
        ArrayAdapter<String> aa = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, tempRange);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        String tempDefault = getContext().getResources().getString(R.string.temp_range_default);
        String tempLow = getContext().getResources().getString(R.string.temp_warning_low_default);
        String tempHigh = getContext().getResources().getString(R.string.temp_warning_high_default);
        String tempUnit = getContext().getResources().getString(R.string.temp_unit);
        for(int i=0;i<2;i++) {
            mSpMineChRange[i][0].setAdapter(aa);
            mTvMineChRange[i][0].setText(tempDefault);
            mTvMineChRange[i][1].setText(tempLow);
            mTvMineChRange[i][2].setText(tempHigh);
            mTvMineChRange[i][3].setText(tempUnit);
        }
        for(int i=6;i<8;i++) {
            mSpMineChRange[i][0].setAdapter(aa);
            mTvMineChRange[i][0].setText(tempDefault);
            mTvMineChRange[i][1].setText(tempLow);
            mTvMineChRange[i][2].setText(tempHigh);
            mTvMineChRange[i][3].setText(tempUnit);
        }
        String[] vibRange = getContext().getResources().getStringArray(R.array.vib_range);
        ArrayAdapter<String> bb = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, vibRange);
        bb.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        String vibDefault = getContext().getResources().getString(R.string.vib_range_default);
        String vibLow = getContext().getResources().getString(R.string.vib_warning_low_default);
        String vibHigh = getContext().getResources().getString(R.string.vib_warning_high_default);
        String vibUnit = getContext().getResources().getString(R.string.vib_unit);
        for(int i=2;i<6;i++) {
            mSpMineChRange[i][0].setAdapter(bb);
            mTvMineChRange[i][0].setText(vibDefault);
            mTvMineChRange[i][1].setText(vibLow);
            mTvMineChRange[i][2].setText(vibHigh);
            mTvMineChRange[i][3].setText(vibUnit);
        }
        for(int i = 0; i< mChCount; i++) {
            String range = mTvMineChRange[i][0].getText().toString();
            String warningLow = mTvMineChRange[i][1].getText().toString();
            String warningHigh = mTvMineChRange[i][2].getText().toString();
            Util.setChRangeWarning(i, range, warningLow, warningHigh);
        }
    }
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
                    //initPump();
                    break;
                case R.id.btn_product2_key:
                    mBtnDeviceSettingClick(R.id.btn_product2_key);
                    //initMotor();
                    break;
                case R.id.btn_product3_key:
                    mBtnDeviceSettingClick(R.id.btn_product3_key);
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
            String cmd = String.format("{'cmd':'cpp%s[%s][%s]'}", index, Util.mChRangeWarning[index][4], Util.mChRangeWarning[index][5]);
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
