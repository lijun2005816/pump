package com.smartgreen.pump.model;

public class UserInfo {
    public String mUsername;
    public String mPassword;
    public String mCompany;
    public String mDepartment;
    public String mRole;
    public UserInfo() {
    }
    public UserInfo(String username, String password, String company, String department, String role) {
        mUsername = username;
        mPassword = password;
        mCompany = company;
        mDepartment = department;
        mRole = role;
    }
}
