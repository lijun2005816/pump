package com.smartgreen.pump.fragment;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Looper;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.aliyun.alink.dm.api.DeviceInfo;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.smartgreen.pump.R;
import com.smartgreen.pump.model.DeviceData;
import com.smartgreen.pump.util.Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static android.os.Environment.getExternalStorageDirectory;
import static com.smartgreen.pump.util.Util.httpSendPost;

public class FragmentData extends Fragment {
    private Button mBtnRealtime;
    private Button mBtnCurve;
    private Button mBtnSave;
    private Button mBtnHistory;
    private TextView mTvTime;
    private TextView mTvTempMotor;
    private TextView mTvTempRound1;
    private TextView mTvTempRound2;
    private TextView mTvTempFix1;
    private TextView mTvTempFix2;
    private TextView mTvVibAms;
    private TextView mTvPressOutput;
    private TextView mTvVolumeWater;
    private Boolean isForeGround;
    private LinearLayout mLlRealtime;
    private LinearLayout mLlCurve;
    private LinearLayout mLlSave;
    private LinearLayout mLlHistory;
    private LineChart mLineChartTemp;
    private LineChart mLineChartVib;
    private LineChart mLineChartPress;
    private LineChart mLineChartVolume;
    private LineData mLineDataTemp = new LineData();
    private LineData mLineDataVib = new LineData();
    private LineData mLineDataPress = new LineData();
    private LineData mLineDataVolume = new LineData();
    private long mStartTime = (long) (17532) * (24) * (3600) * (1000);
    private List<Integer> mColor = new ArrayList<>();
    private Button mBtnExportLocal;
    private Button mBtnExportCloud;
    private DatePicker mDateStart;
    private DatePicker mDateEnd;
    private TimePicker mTimeStart;
    private TimePicker mTimeEnd;
    private int mColorPrimary;
    private String path;
    private LineChart mLineChartViewTemp;
    private LineChart mLineChartViewVib;
    private LineChart mLineChartViewPress;
    private LineChart mLineChartViewVolume;
    private LineData mLineDataViewTemp = new LineData();
    private LineData mLineDataViewVib = new LineData();
    private LineData mLineDataViewPress = new LineData();
    private LineData mLineDataViewVolume = new LineData();
    private TextView mTvDataPumpName;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mStartTime = System.currentTimeMillis();
        return Objects.requireNonNull(inflater).inflate(R.layout.fragment_data, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mColor.add(Color.RED);
        mColor.add(Color.MAGENTA);
        mColor.add(Color.GREEN);
        mColor.add(Color.BLACK);
        mColor.add(Color.BLUE);
        mBtnRealtime = view.findViewById(R.id.btn_realtime);
        mBtnCurve = view.findViewById(R.id.btn_curve);
        mBtnSave = view.findViewById(R.id.btn_save);
        mBtnHistory = view.findViewById(R.id.btn_history);
        mTvTime = view.findViewById(R.id.tv_data_time);
        mTvTempMotor = view.findViewById(R.id.tv_temp_motor);
        mTvTempRound1 = view.findViewById(R.id.tv_temp_round1);
        mTvTempRound2 = view.findViewById(R.id.tv_temp_round2);
        mTvTempFix1 = view.findViewById(R.id.tv_temp_fix1);
        mTvTempFix2 = view.findViewById(R.id.tv_temp_fix2);
        mTvVibAms = view.findViewById(R.id.tv_vib_ams);
        mTvPressOutput = view.findViewById(R.id.tv_press_output);
        mTvVolumeWater = view.findViewById(R.id.tv_volu_water);
        isForeGround = true;
        mTvTime.setText(Util.getStringDateTime());
        mLlRealtime = view.findViewById(R.id.ll_realtime);
        mLlCurve = view.findViewById(R.id.ll_curve);
        mLlSave = view.findViewById(R.id.ll_save);
        mLlHistory = view.findViewById(R.id.ll_history);
        mLineChartTemp = view.findViewById(R.id.chart_temp);
        mLineChartVib = view.findViewById(R.id.chart_vib);
        mLineChartPress = view.findViewById(R.id.chart_press);
        mLineChartVolume = view.findViewById(R.id.chart_volu);
        initLineChart(mLineChartTemp, mLineDataTemp, 2);
        initLineChart(mLineChartVib, mLineDataVib, 4);
        initLineChart(mLineChartPress, mLineDataPress, 1);
        initLineChart(mLineChartVolume, mLineDataVolume, 1);
        mDateStart = view.findViewById(R.id.dp_save_start);
        mDateEnd = view.findViewById(R.id.dp_save_end);
        mTimeStart = view.findViewById(R.id.tp_save_start);
        mTimeStart.setIs24HourView(true);
        mTimeEnd = view.findViewById(R.id.tp_save_end);
        mTimeEnd.setIs24HourView(true);
        mBtnExportLocal = view.findViewById(R.id.btn_save_local);
        mBtnExportCloud = view.findViewById(R.id.btn_save_cloud);
        Objects.requireNonNull(getActivity());
        mColorPrimary = ContextCompat.getColor(getActivity(), R.color.colorPrimary);
        mLineChartViewTemp = view.findViewById(R.id.chart_history_temp);
        mLineChartViewVib = view.findViewById(R.id.chart_data_history_vib);
        mLineChartViewPress = view.findViewById(R.id.chart_data_history_press);
        mLineChartViewVolume = view.findViewById(R.id.chart_data_history_volu);
        initLineChart(mLineChartViewTemp, mLineDataViewTemp, 2);
        initLineChart(mLineChartViewVib, mLineDataViewVib, 4);
        initLineChart(mLineChartViewPress, mLineDataViewPress, 1);
        initLineChart(mLineChartViewVolume, mLineDataViewVolume, 1);
        mTvDataPumpName = view.findViewById(R.id.tv_data_pump_name);
        Objects.requireNonNull(getContext());
        mTvDataPumpName.setText(Util.getPumpNameFromSP(getContext()));
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mBtnRealtime.setOnClickListener(mClickEvent);
        mBtnCurve.setOnClickListener(mClickEvent);
        mBtnSave.setOnClickListener(mClickEvent);
        mBtnHistory.setOnClickListener(mClickEvent);
        mBtnExportLocal.setOnClickListener(mClickEvent);
        mBtnExportCloud.setOnClickListener(mClickEvent);
    }
    public View.OnClickListener mClickEvent = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.btn_realtime:
                    mBtnRealtime.setTextColor(Color.WHITE);
                    mBtnRealtime.setBackgroundColor(mColorPrimary);
                    mLlRealtime.setVisibility(View.VISIBLE);
                    mBtnCurve.setTextColor(Color.GRAY);
                    mBtnCurve.setBackgroundColor(Color.WHITE);
                    mLlCurve.setVisibility(View.GONE);
                    mBtnSave.setTextColor(Color.GRAY);
                    mBtnSave.setBackgroundColor(Color.WHITE);
                    mLlSave.setVisibility(View.GONE);
                    mBtnHistory.setTextColor(Color.GRAY);
                    mBtnHistory.setBackgroundColor(Color.WHITE);
                    mLlHistory.setVisibility(View.GONE);
                    break;
                case R.id.btn_curve:
                    mBtnRealtime.setTextColor(Color.GRAY);
                    mBtnRealtime.setBackgroundColor(Color.WHITE);
                    mLlRealtime.setVisibility(View.GONE);
                    mBtnCurve.setTextColor(Color.WHITE);
                    mBtnCurve.setBackgroundColor(mColorPrimary);
                    mLlCurve.setVisibility(View.VISIBLE);
                    mBtnSave.setTextColor(Color.GRAY);
                    mBtnSave.setBackgroundColor(Color.WHITE);
                    mLlSave.setVisibility(View.GONE);
                    mBtnHistory.setTextColor(Color.GRAY);
                    mBtnHistory.setBackgroundColor(Color.WHITE);
                    mLlHistory.setVisibility(View.GONE);
                    break;
                case R.id.btn_save:
                    mBtnRealtime.setTextColor(Color.GRAY);
                    mBtnRealtime.setBackgroundColor(Color.WHITE);
                    mLlRealtime.setVisibility(View.GONE);
                    mBtnCurve.setTextColor(Color.GRAY);
                    mBtnCurve.setBackgroundColor(Color.WHITE);
                    mLlCurve.setVisibility(View.GONE);
                    mBtnSave.setTextColor(Color.WHITE);
                    mBtnSave.setBackgroundColor(mColorPrimary);
                    mLlSave.setVisibility(View.VISIBLE);
                    mBtnHistory.setTextColor(Color.GRAY);
                    mBtnHistory.setBackgroundColor(Color.WHITE);
                    mLlHistory.setVisibility(View.GONE);
                    break;
                case R.id.btn_history:
                    mBtnRealtime.setTextColor(Color.GRAY);
                    mBtnRealtime.setBackgroundColor(Color.WHITE);
                    mLlRealtime.setVisibility(View.GONE);
                    mBtnCurve.setTextColor(Color.GRAY);
                    mBtnCurve.setBackgroundColor(Color.WHITE);
                    mLlCurve.setVisibility(View.GONE);
                    mBtnSave.setTextColor(Color.GRAY);
                    mBtnSave.setBackgroundColor(Color.WHITE);
                    mLlSave.setVisibility(View.GONE);
                    mBtnHistory.setTextColor(Color.WHITE);
                    mBtnHistory.setBackgroundColor(mColorPrimary);
                    mLlHistory.setVisibility(View.VISIBLE);
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("*/*");
                    intent.addCategory(Intent.CATEGORY_OPENABLE);
                    startActivityForResult(intent, 1);
                    break;
                case R.id.btn_save_local:
                    Toast.makeText(getContext(), "本地数据保存中……", Toast.LENGTH_LONG).show();
                    new Thread(runnableExportLocal).start();
                    break;
                case R.id.btn_save_cloud:
                    Toast.makeText(getContext(), "云端数据保存中……", Toast.LENGTH_LONG).show();
                    new Thread(runnableExportCloud).start();
                    break;
                default:
                    break;
            }
        }
    };
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        isForeGround = !hidden;
        if (!hidden) {
            Objects.requireNonNull(getContext());
            mTvDataPumpName.setText(Util.getPumpNameFromSP(getContext()));
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        isForeGround = true;
    }
    @Override
    public void onPause() {
        super.onPause();
        isForeGround = false;
    }
    @Override
    public void onDestroy() {
        isForeGround = false;
        super.onDestroy();
    }
    public void updateView(DeviceData data) {
        if (isForeGround) {
            mTvTime.setText(String.format(Locale.CHINA, "%s", Util.strToDateTime(data.time)));
            mTvTempMotor.setText(String.format(Locale.CHINA, "%s", data.ch0));
            mTvTempRound1.setText(String.format(Locale.CHINA, "%s", data.ch1));
            mTvTempRound2.setText(String.format(Locale.CHINA, "%s", data.ch2));
            mTvTempFix1.setText(String.format(Locale.CHINA, "%s", data.ch3));
            mTvTempFix2.setText(String.format(Locale.CHINA, "%s", data.ch4));
            mTvVibAms.setText(String.format(Locale.CHINA, "%s", data.ch5));
            mTvPressOutput.setText(String.format(Locale.CHINA, "%s", data.ch6));
            mTvVolumeWater.setText(String.format(Locale.CHINA, "%s", data.ch7));
            List<Float> temp = new ArrayList<>();
            temp.add(data.ch0);
            temp.add(data.ch1);
            addEntry(mLineChartTemp, mLineDataTemp, temp, data.timestamp-mStartTime, temp.size());
            List<Float> vib = new ArrayList<>();
            vib.add(data.ch2);
            vib.add(data.ch3);
            vib.add(data.ch4);
            vib.add(data.ch5);
            addEntry(mLineChartVib, mLineDataVib, vib, data.timestamp-mStartTime, vib.size());
            List<Float> press = new ArrayList<>();
            press.add(data.ch6);
            addEntry(mLineChartPress, mLineDataPress, press, data.timestamp-mStartTime, press.size());
            List<Float> volume = new ArrayList<>();
            volume.add(data.ch7);
            addEntry(mLineChartVolume, mLineDataVolume, volume, data.timestamp-mStartTime, volume.size());
        }
    }

    public void clearView(){
        clearEntry(mLineDataTemp);
        clearEntry(mLineDataVib);
        clearEntry(mLineDataPress);
        clearEntry(mLineDataVolume);
    }

    private void initLineChart(LineChart mLineChart, LineData mLineData, int mLineNum) {
        mLineChart.setDrawBorders(true);
        Description des = new Description();
        des.setText("");
        mLineChart.setDescription(des);
        Legend legend = mLineChart.getLegend();
        legend.setForm(Legend.LegendForm.LINE);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setDrawInside(false);
        YAxis mRightAxis = mLineChart.getAxisRight();
        mRightAxis.setDrawLabels(false);
        XAxis mXAxis = mLineChart.getXAxis();
        mXAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        mXAxis.setAxisMinimum(0f);
        mXAxis.setAxisMaximum(2000000f);
        int count = 5;
        mXAxis.setLabelCount(count, false);
        mXAxis.setValueFormatter(formatter);
        for(int i = 0; i < mLineNum; i++) {
            mLineData.addDataSet(createDataSet(i));
        }
        mLineChart.setData(mLineData);
    }
    private LineDataSet createDataSet(int index) {
        String label = String.format("%s%s", "CH", index + 1);
        LineDataSet mLineDataSet = new LineDataSet(null, label);
        mLineDataSet.setDrawCircles(true);
        mLineDataSet.setDrawValues(false);
        mLineDataSet.setVisible(true);
        mLineDataSet.setColor(mColor.get(index));
        mLineDataSet.setCircleColor(mColor.get(index));
        return mLineDataSet;
    }

    public void addEntry(LineChart mLineChart, LineData lineData, List<Float> data, long time, int mLineNum) {
        for (int i = 0; i < mLineNum; i++) {
            Entry entry = new Entry(time, data.get(i));
            lineData.addEntry(entry, i);
            if (lineData.getDataSetByIndex(i).getXMax() - lineData.getDataSetByIndex(i).getXMin() > 2000000f) {
                Entry xRemove = lineData.getDataSetByIndex(i).getEntryForIndex(0);
                lineData.removeEntry(xRemove, i);
            }
        }
        long xMinTime = (long) Math.floor(lineData.getDataSetByIndex(0).getXMin());
        long xMaxTime = (long) Math.floor(lineData.getDataSetByIndex(0).getXMax());
        XAxis mXAxis = mLineChart.getXAxis();
        mXAxis.setAxisMinimum(xMinTime);
        mXAxis.setAxisMaximum(xMaxTime);
        lineData.notifyDataChanged();
        mLineChart.notifyDataSetChanged();
        mLineChart.invalidate();
    }
    public void clearEntry(LineData lineData) {
        int num = lineData.getDataSetCount();
        int count = lineData.getEntryCount()/num;
        for (int i = 0; i < num; i++) {
            for(int j =0; j < count; j++) {
                Entry xRemove = lineData.getDataSetByIndex(i).getEntryForIndex(0);
                lineData.removeEntry(xRemove, i);
            }
        }
    }
    private ValueFormatter formatter = new ValueFormatter() {
        @Override
        public String getFormattedValue(float value) {
            SimpleDateFormat mDf = new SimpleDateFormat("HH:mm:ss", Locale.CHINA);
            long timeMil = Float.valueOf(value).longValue() + mStartTime;
            return mDf.format(timeMil);
        }
    };

    private Runnable runnableExportLocal = new Runnable() {
        @Override
        public void run() {
            String fileFolder = "SmartPump";
            String exportType = "local";
            String TIME_DATA = "yyyyMMddHHmmss";
            String FILE_EXT = "csv";
            DeviceInfo deviceInfo = Util.getDeviceInfoFromSP(getContext());
            String product = "product_sgagr";
            String device = deviceInfo.deviceName;
            device = device.replace("device_1", "device_0");
            try {
                String filePath = String.format("%s/%s", getExternalStorageDirectory().getPath(), fileFolder);
                File dirApp = new File(filePath);
                if (!dirApp.exists()) {
                    boolean res = dirApp.mkdirs();
                    if (!res) {
                        return;
                    }
                }
                SimpleDateFormat df = new SimpleDateFormat(TIME_DATA, Locale.CHINA);
                String TimeNow = df.format(System.currentTimeMillis());
                String fileName = String.format("%s/%s_%s_%s_%s.%s", filePath, TimeNow, product, device, exportType, FILE_EXT);
                File file = new File(fileName);
                if (!file.exists()) {
                    boolean res = file.createNewFile();
                    if (!res) {
                        return;
                    }
                }
                FileOutputStream fos = new FileOutputStream(file);
                OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
                String exportStr = ExportData();
                osw.write(exportStr);
                osw.flush();
                fos.flush();
                fos.close();
                osw.close();
                Looper.prepare();
                Toast.makeText(FragmentData.this.getContext(), "完成本地数据保存", Toast.LENGTH_LONG).show();
                Looper.loop();
            } catch (IOException e) {
                Looper.prepare();
                Toast.makeText(FragmentData.this.getContext(), "本地数据保存错误", Toast.LENGTH_LONG).show();
                Looper.loop();
                e.printStackTrace();
            }
        }
    };

    public String ExportData() {
        String TIME_DATA = "yyyy-MM-dd HH:mm:ss";
        String JSON_KEY_TIME = "time";
        int CH_COUNT = 8;
        String JSON_KEY_CH = "ch";
        SimpleDateFormat df = new SimpleDateFormat(TIME_DATA, Locale.CHINA);
        StringBuilder sb = new StringBuilder();
        sb.append(JSON_KEY_TIME);
        for (int i = 0; i < CH_COUNT; i++) {
            String label = String.format(",%s%s", JSON_KEY_CH, i + 1);
            sb.append(label);
        }
        sb.append("\n");
        if (mLineDataTemp == null) {
            return sb.toString();
        }
        if (mLineDataTemp.getDataSetByIndex(0) == null) {
            return sb.toString();
        }
        for (int i = 0; i < mLineDataTemp.getDataSetByIndex(0).getEntryCount(); i++) {
            float x = mLineDataTemp.getDataSetByIndex(0).getEntryForIndex(i).getX();
            long timeMil = Float.valueOf(x).longValue() + mStartTime;
            sb.append(df.format(timeMil));
            for (int j = 0; j < 2; j++) {
                float y = mLineDataTemp.getDataSetByIndex(j).getEntryForIndex(i).getY();
                sb.append(",");
                sb.append(y);
            }
            for (int j = 0; j < 4; j++) {
                float y = mLineDataVib.getDataSetByIndex(j).getEntryForIndex(i).getY();
                sb.append(",");
                sb.append(y);
            }
            for (int j = 0; j < 1; j++) {
                float y = mLineDataPress.getDataSetByIndex(j).getEntryForIndex(i).getY();
                sb.append(",");
                sb.append(y);
            }
            for (int j = 0; j < 1; j++) {
                float y = mLineDataVolume.getDataSetByIndex(j).getEntryForIndex(i).getY();
                sb.append(",");
                sb.append(y);
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private Runnable runnableExportCloud = new Runnable() {
        @Override
        public void run() {
            String FILE_FOLDER = "SmartPump";
            String exportType = "cloud";
            String TIME_DATA = "yyyyMMddHHmmss";
            String FILE_EXT = "csv";
            DeviceInfo deviceInfo = Util.getDeviceInfoFromSP(getContext());
            String product = "product_sgagr";
            String device = deviceInfo.deviceName;
            device = device.replace("device_1", "device_0");
            try {
                String filePath = String.format("%s/%s", getExternalStorageDirectory().getPath(), FILE_FOLDER);
                File dirApp = new File(filePath);
                if (!dirApp.exists()) {
                    boolean res = dirApp.mkdirs();
                    if (!res) {
                        return;
                    }
                }
                SimpleDateFormat df = new SimpleDateFormat(TIME_DATA, Locale.CHINA);
                String TimeNow = df.format(System.currentTimeMillis());
                String fileName = String.format("%s/%s_%s_%s_%s.%s", filePath, TimeNow, product, device, exportType, FILE_EXT);
                File file = new File(fileName);
                if (!file.exists()) {
                    boolean res = file.createNewFile();
                    if (!res) {
                        return;
                    }
                }
                FileOutputStream fos = new FileOutputStream(file);
                OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
                String url = "https://www.shsmartcloud.com/index.php/service/index/duohe";
                int yearStart = mDateStart.getYear();
                int monthStart = mDateStart.getMonth();
                int dayStart = mDateStart.getDayOfMonth();
                int yearEnd = mDateEnd.getYear();
                int monthEnd = mDateEnd.getMonth();
                int dayEnd = mDateEnd.getDayOfMonth();
                int hourStart = mTimeStart.getHour();
                int minuteStart = mTimeStart.getMinute();
                int hourEnd = mTimeEnd.getHour();
                int minuteEnd = mTimeEnd.getMinute();
                String timeStart = String.format(Locale.CHINA, "%04d%02d%02d%02d%02d00", yearStart, monthStart + 1, dayStart, hourStart, minuteStart);
                SimpleDateFormat sdtStart = new SimpleDateFormat(TIME_DATA, Locale.CHINA);
                long milStart = sdtStart.parse(timeStart).getTime();
                String timeEnd = String.format(Locale.CHINA, "%04d%02d%02d%02d%02d59", yearEnd, monthEnd + 1, dayEnd, hourEnd, minuteEnd);
                SimpleDateFormat sdtEnd = new SimpleDateFormat(TIME_DATA, Locale.CHINA);
                long milEnd = sdtEnd.parse(timeEnd).getTime();
                long count = (milEnd - milStart) /3600000 + 1;
                long delta = (milEnd - milStart) / count;
                String title = "time,ch1,ch2,ch3,ch4,ch5,ch6,ch7,ch8\n";
                StringBuilder sb = new StringBuilder(title);
                for (int i = 0; i< count; i++) {
                    String param = String.format("product=%s&device=%s&idstart=%s&idend=%s", product, device, (milStart + i*delta)*1000, (milStart + (i+1)*delta)*1000);
                    sb.append(httpSendPost(url, param).replace("<br/>", "\n"));
                }
                String exportStr = sb.toString();
                osw.write(exportStr);
                osw.flush();
                fos.flush();
                fos.close();
                osw.close();
                Looper.prepare();
                Toast.makeText(FragmentData.this.getContext(), "完成云端数据保存", Toast.LENGTH_LONG).show();
                Looper.loop();
            } catch (IOException | ParseException e) {
                Looper.prepare();
                Toast.makeText(FragmentData.this.getContext(), "云端数据保存出错", Toast.LENGTH_LONG).show();
                Looper.loop();
                e.printStackTrace();
            }
        }
    };
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode != 1) {
            return;
        }
        Uri uri = data.getData();
        if (uri == null ) {
            return;
        }
        path =getFilePath(getContext(), uri);
        if (path == null) {
            return;
        }
        new Thread(runnableImport).start();
    }
    public String getFilePath(final Context context, final Uri uri) {
        if (DocumentsContract.isDocumentUri(context, uri)) {
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }
    public String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        final String column = "_data";
        final String[] projection = {column};
        try (Cursor cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        }
        return null;
    }
    public boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }
    public boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }
    public boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
    private Runnable runnableImport = new Runnable() {
        @Override
        public void run() {
            clearEntry(mLineDataViewTemp);
            clearEntry(mLineDataViewVib);
            clearEntry(mLineDataViewPress);
            clearEntry(mLineDataViewVolume);
            String TIME_DATA = "yyyy-MM-dd HH:mm:ss";
            int CH_COUNT = 8;
            try {
                File file = new File(path);
                String line;
                FileInputStream fis = new FileInputStream(file);
                InputStreamReader inputReader = new InputStreamReader(fis);
                BufferedReader buffReader = new BufferedReader(inputReader);
                SimpleDateFormat df = new SimpleDateFormat(TIME_DATA, Locale.CHINA);
                if ( buffReader.readLine() == null) {
                    return;
                }
                while (( line = buffReader.readLine()) != null) {
                    String[] list = line.split(",");
                    if(list.length != CH_COUNT +1) {
                        break;
                    }
                    long time = df.parse(list[0]).getTime();
                    List<Float> tempList = new ArrayList<>();
                    tempList.add(Float.valueOf(list[1]));
                    tempList.add(Float.valueOf(list[2]));
                    addEntry(mLineChartViewTemp, mLineDataViewTemp, tempList, time-mStartTime, tempList.size());
                    List<Float> vibList = new ArrayList<>();
                    vibList.add(Float.valueOf(list[3]));
                    vibList.add(Float.valueOf(list[4]));
                    vibList.add(Float.valueOf(list[5]));
                    vibList.add(Float.valueOf(list[6]));
                    addEntry(mLineChartViewVib, mLineDataViewVib, vibList, time-mStartTime, vibList.size());
                    List<Float> pressList = new ArrayList<>();
                    pressList.add(Float.valueOf(list[6]));
                    addEntry(mLineChartViewPress, mLineDataViewPress, pressList, time-mStartTime, pressList.size());
                    List<Float> volumeList = new ArrayList<>();
                    volumeList.add(Float.valueOf(list[6]));
                    addEntry(mLineChartViewVolume, mLineDataViewVolume, volumeList, time-mStartTime, volumeList.size());
                }
                fis.close();
            } catch (java.io.IOException | ParseException e) {
                e.printStackTrace();
            }
        }
    };
}
