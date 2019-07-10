package com.smartgreen.pump.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.smartgreen.pump.MainActivity;
import com.smartgreen.pump.R;
import com.smartgreen.pump.util.Util;

import java.util.Objects;

public class FragmentHome extends Fragment {
    private TextView mTvHomeDate;
    private TextView mTvHomeTime;
    private ImageView mIvHomeDevice;
    private ImageView mIvHomeWater;
    private ImageView mIvHomeEnergy;
    private ImageView mIvHomeVideo;
    private int mDelayMillis = 1000;
    private Handler handlerRepeat = new Handler();
    private TextView mTvHomePumpName;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return Objects.requireNonNull(inflater).inflate(R.layout.fragment_home, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTvHomeDate = view.findViewById(R.id.tv_home_date);
        mTvHomeTime = view.findViewById(R.id.tv_home_time);
        mIvHomeDevice = view.findViewById(R.id.iv_home_device);
        mIvHomeWater = view.findViewById(R.id.iv_home_water);
        mIvHomeEnergy = view.findViewById(R.id.iv_home_energe);
        mIvHomeVideo = view.findViewById(R.id.iv_home_video);
        mTvHomeDate.setText(Util.getStringDateShort());
        mTvHomeTime.setText(Util.getStringTimeShort());
        handlerRepeat.postDelayed(runnableRepeat, mDelayMillis);
        mTvHomePumpName=view.findViewById(R.id.tv_home_pump_name);
        Objects.requireNonNull(getContext());
        mTvHomePumpName.setText(Util.getPumpNameFromSP(getContext()));
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mIvHomeDevice.setOnClickListener(mClickEvent);
        mIvHomeWater.setOnClickListener(mClickEvent);
        mIvHomeEnergy.setOnClickListener(mClickEvent);
        mIvHomeVideo.setOnClickListener(mClickEvent);
    }
    public View.OnClickListener mClickEvent = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MainActivity parent = (MainActivity) getActivity();
            if (parent == null) {
                return;
            }
            int id = v.getId();
            switch (id) {
                case R.id.iv_home_device:
                    parent.mMenuNav.setSelectedItemId(R.id.navigation_data);
                    break;
                case R.id.iv_home_water:
                    parent.mMenuNav.setSelectedItemId(R.id.navigation_data);
                    break;
                case R.id.iv_home_energe:
                    //parent.mMenuNav.setSelectedItemId(R.id.navigation_data);
                    break;
                case R.id.iv_home_video:
                    //parent.mMenuNav.setSelectedItemId(R.id.navigation_data);
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            handlerRepeat.removeCallbacks(runnableRepeat);
        } else {
            handlerRepeat.postDelayed(runnableRepeat, mDelayMillis);
            Objects.requireNonNull(getContext());
            mTvHomePumpName.setText(Util.getPumpNameFromSP(getContext()));
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
            mTvHomeDate.setText(Util.getStringDateShort());
            mTvHomeTime.setText(Util.getStringTimeShort());
            handlerRepeat.postDelayed(this, mDelayMillis);
        }
    };
}
