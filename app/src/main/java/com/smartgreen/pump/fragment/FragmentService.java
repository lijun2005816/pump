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
import com.smartgreen.pump.util.Util;

import java.util.Objects;

public class FragmentService extends Fragment {
    private TextView mTvServiceDate;
    private int mDelayMillis = 60000;
    private Handler handlerRepeat = new Handler();
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
    private TextView mTvServicePumpName;
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
        mTvServicePumpName = view.findViewById(R.id.tv_service_pump_name);
        Objects.requireNonNull(getContext());
        mTvServicePumpName.setText(Util.getPumpNameFromSP(getContext()));
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mIvAboutApp.setOnClickListener(mClickEvent);
        mIvAboutUs.setOnClickListener(mClickEvent);
        mIvMore.setOnClickListener(mClickEvent);
        mIvGuide.setOnClickListener(mClickEvent);
    }
    public View.OnClickListener mClickEvent = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Objects.requireNonNull(getContext());
            Drawable drawClose = ContextCompat.getDrawable(getContext(), R.drawable.mine_detail);
            Drawable drawOpen = ContextCompat.getDrawable(getContext(), R.drawable.mine_detail_open);
            int id = v.getId();
            switch (id) {
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
