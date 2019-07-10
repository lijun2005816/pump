package com.smartgreen.pump.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.smartgreen.pump.R;
import com.smartgreen.pump.util.Util;

public class FragmentDialogLogin extends DialogFragment implements View.OnClickListener {
    private EditText mUsername;
    private EditText mPassword;
    private LinearLayout mLlLoginFail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container);
        mUsername= view.findViewById(R.id.et_login_username);
        mPassword= view.findViewById(R.id.et_login_password);
        Button mBtnLogin = view.findViewById(R.id.btn_login);
        mBtnLogin.setOnClickListener(this);
        ImageView mIvBack = view.findViewById(R.id.iv_mine_back);
        mIvBack.setOnClickListener(this);
        mLlLoginFail = view.findViewById(R.id.ll_login_fail);
        return view;
    }
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login:
                new Thread(mRunnableLogin).start();
                break;
            case R.id.iv_mine_back:
                dismiss();
                break;
            default:
                break;
        }
    }
    private Runnable mRunnableLogin = new Runnable() {
        @Override
        public void run() {
            String url = "https://www.shsmartcloud.com/index.php/index/index/applogincheck";
            String username = mUsername.getText().toString();
            String password = mPassword.getText().toString();
            String passwordMd5 = Util.md5Decode(password);
            String param = String.format("username=%s&password=%s", username, passwordMd5);
            String result = Util.httpSendPost(url, param);
            if (result.equalsIgnoreCase("0") || result.equalsIgnoreCase("")) {
                mLlLoginFail.post(new Runnable() {
                    @Override
                    public void run() {
                        mLlLoginFail.setVisibility(View.VISIBLE);
                    }
                });
            } else {
                mLlLoginFail.post(new Runnable() {
                    @Override
                    public void run() {
                        mLlLoginFail.setVisibility(View.GONE);
                    }
                });
                if (getTargetFragment()== null) {
                    return;
                }
                Intent intent= new Intent();
                intent.putExtra(Util.USERNAME, username);
                intent.putExtra(Util.PASSWORD, password);
                getTargetFragment().onActivityResult(FragmentMine.REQUEST_CODE, Activity.RESULT_OK, intent);
            }
        }
    };
}
