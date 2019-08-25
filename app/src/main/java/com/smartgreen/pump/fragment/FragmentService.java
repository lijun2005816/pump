package com.smartgreen.pump.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.smartgreen.pump.R;
import com.smartgreen.pump.model.DeviceData;
import com.smartgreen.pump.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class FragmentService extends Fragment {
    private TextView mTvServiceDate;
    private int mDelayMillis = 60000;
    private Handler handlerRepeat = new Handler();
    private TextView mTvServicePumpName;
    private Boolean isForeGround = true;
    private TextView mTvTimeMotor;
    private TextView mTvMotorCh1;
    private TextView mTvMotorCh2;
    private TextView mTvMotorCh3;
    private TextView mTvMotorCh4;
    private TextView mTvMotorCh5;
    private TextView mTvMotorCh6;
    private TextView mTvMotorCh7;
    private TextView mTvMotorCh8;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return Objects.requireNonNull(inflater).inflate(R.layout.fragment_service, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTvServiceDate = view.findViewById(R.id.tv_service_date);
        mTvServiceDate.setText(Util.getStringDateShort());
        handlerRepeat.postDelayed(runnableRepeat, mDelayMillis);
        Objects.requireNonNull(getContext());
        mTvServicePumpName = view.findViewById(R.id.tv_service_pump_name);
        mTvServicePumpName.setText(Util.getPumpNameFromSP(getContext()));
        mTvTimeMotor = view.findViewById(R.id.tv_time_motor);
        mTvMotorCh1 = view.findViewById(R.id.tv_motor_ch1);
        mTvMotorCh2 = view.findViewById(R.id.tv_motor_ch2);
        mTvMotorCh3 = view.findViewById(R.id.tv_motor_ch3);
        mTvMotorCh4 = view.findViewById(R.id.tv_motor_ch4);
        mTvMotorCh5 = view.findViewById(R.id.tv_motor_ch5);
        mTvMotorCh6 = view.findViewById(R.id.tv_motor_ch6);
        mTvMotorCh7 = view.findViewById(R.id.tv_motor_ch7);
        mTvMotorCh8 = view.findViewById(R.id.tv_motor_ch8);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    public View.OnClickListener mClickEvent = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
        }
    };
    public void updateView(DeviceData data) {
        Objects.requireNonNull(getContext());
        if (isForeGround) {
            switch (Util.mDeviceType) {
                case 2:
                    mTvTimeMotor.setText(String.format(Locale.CHINA, "%s", Util.strToDateTime(data.time)));
                    mTvMotorCh1.setText(String.format(Locale.CHINA, "%s", data.ch0));
                    mTvMotorCh2.setText(String.format(Locale.CHINA, "%s", data.ch1));
                    mTvMotorCh3.setText(String.format(Locale.CHINA, "%s", data.ch2));
                    mTvMotorCh4.setText(String.format(Locale.CHINA, "%s", data.ch3));
                    mTvMotorCh5.setText(String.format(Locale.CHINA, "%s", data.ch4));
                    mTvMotorCh6.setText(String.format(Locale.CHINA, "%s", data.ch5));
                    mTvMotorCh7.setText(String.format(Locale.CHINA, "%s", data.ch6));
                    mTvMotorCh8.setText(String.format(Locale.CHINA, "%s", data.ch7));
                    break;

                default:
                    break;
            }
        }
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            Objects.requireNonNull(getContext());
            mTvServicePumpName.setText(Util.getPumpNameFromSP(getContext()));
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        handlerRepeat.postDelayed(runnableRepeat, mDelayMillis);
    }
    @Override
    public void onPause() {
        super.onPause();
        handlerRepeat.removeCallbacks(runnableRepeat);
    }
    @Override
    public void onDestroy() {
        handlerRepeat.removeCallbacks(runnableRepeat);
        super.onDestroy();
    }
    private Runnable runnableRepeat = new Runnable() {
        @Override
        public void run() {
            mTvServiceDate.setText(Util.getStringDateShort());
            handlerRepeat.postDelayed(this, mDelayMillis);
        }
    };
}
