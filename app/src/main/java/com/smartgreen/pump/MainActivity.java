package com.smartgreen.pump;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import com.smartgreen.pump.aliyun.IotManager;
import com.smartgreen.pump.fragment.FragmentData;
import com.smartgreen.pump.fragment.FragmentHome;
import com.smartgreen.pump.fragment.FragmentMine;
import com.smartgreen.pump.fragment.FragmentService;
import com.smartgreen.pump.util.Util;
import java.io.File;
import static android.os.Environment.MEDIA_MOUNTED;
import static android.os.Environment.getExternalStorageDirectory;
import static android.os.Environment.getExternalStorageState;

public class MainActivity extends AppCompatActivity {
    private String TAG = getClass().getSimpleName();
    public IotManager mIotManager = new IotManager(this);
    private int NavNum = 4;
    private int index = 0;
    private String mIndexTag= "TAG";
    private Fragment[] fragments = new Fragment[NavNum];
    private String[] titles = new String[NavNum];
    public BottomNavigationView mMenuNav;
    private final int ReqCode = 1;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction tx = fm.beginTransaction();
            for (int i = 0; i < NavNum; i++) {
                tx.hide(fragments[i]);
            }
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    index = 0;
                    item.setIcon(R.mipmap.ic_home_color);
                    tx.show(fragments[index]);
                    tx.commit();
                    return true;
                case R.id.navigation_data:
                    index =1;
                    item.setIcon(R.mipmap.ic_data_color);
                    tx.show(fragments[index]);
                    tx.commit();
                    return true;
                case R.id.navigation_service:
                    index = 2;
                    item.setIcon(R.mipmap.ic_service_color);
                    tx.show(fragments[index]);
                    tx.commit();
                    return true;
                case R.id.navigation_mine:
                    index = 3;
                    item.setIcon(R.mipmap.ic_mine_color);
                    tx.show(fragments[index]);
                    tx.commit();
                    return true;
            }
            return false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mMenuNav = findViewById(R.id.nav_view);
        mMenuNav.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        titles[0] = getResources().getString(R.string.title_home);
        titles[1] = getResources().getString(R.string.title_data);
        titles[2] = getResources().getString(R.string.title_service);
        titles[3] = getResources().getString(R.string.title_mine);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction tx = fm.beginTransaction();
        if (savedInstanceState ==null) {
            fragments[0] = new FragmentHome();
            fragments[1] = new FragmentData();
            fragments[2] = new FragmentService();
            fragments[3] = new FragmentMine();
            for (int i = 0; i < NavNum; i++) {
                tx.add(R.id.frame_content, fragments[i], titles[i]);
                tx.hide(fragments[i]);
            }
            tx.show(fragments[index]);
            tx.commit();
        } else {
            for (int i=0;i<NavNum;i++) {
                fragments[i] = getSupportFragmentManager().findFragmentByTag(titles[i]);
                if(fragments[i] ==null) {
                    switch (i) {
                        case 0:
                            fragments[i] = new FragmentHome();
                            break;
                        case 1:
                            fragments[i] = new FragmentData();
                            break;
                        case 2:
                            fragments[i] = new FragmentService();
                            break;
                        case 3:
                            fragments[i] = new FragmentMine();
                            break;
                        default:
                            break;
                    }
                    tx.add(R.id.frame_content, fragments[i],titles[i]);
                }
                tx.hide(fragments[i]);
            }
            index = savedInstanceState.getInt(mIndexTag);
            tx.show(fragments[index]);
            tx.commit();
        }
        Util.mDataCollect.mFragmentData = (FragmentData) fragments[1];
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return;
        }
        if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, ReqCode);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(mIndexTag, index);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mIotManager != null) {
            mIotManager.unRegister();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (mIotManager != null) {
            mIotManager.register();
        }
    }
    @Override
    protected void onDestroy() {
        if (mIotManager != null) {
            mIotManager.unRegister();
            mIotManager.unSubscribe();
            mIotManager.disConnect();
        }
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        String FILE_FOLDER = "SmartPump";
        if (requestCode == ReqCode) {
            if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            if (!getExternalStorageState().equals(MEDIA_MOUNTED)) {
                return;
            }
            String filePath = String.format("%s/%s", getExternalStorageDirectory().getPath(), FILE_FOLDER);
            File dirApp = new File(filePath);
            if (dirApp.exists()) {
                return;
            }
            boolean res = dirApp.mkdirs();
            if (!res) {
                Log.i(TAG, "failed to make dir : " + filePath);
            }
        }
    }
}
