package com.thingfarms.deviceinfo.model;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.thingfarms.deviceinfo.base.AppApplication;

import java.util.List;

public class InstalledAppList implements DeviceInfo {
    private final String TAG = this.getClass().getSimpleName();

    @Override
    public String getDeviceInfoTitle() {
        return "Installed App List";
    }

    @Override
    public String getDeviceInfoDetails() {
        return getList();
    }

    private String getList()
    {
        final PackageManager pm = AppApplication.getAppContext().getPackageManager();
        //get a list of installed apps.
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        StringBuilder sb = new StringBuilder();
        for (ApplicationInfo packageInfo : packages) {
           sb.append( packageInfo.loadLabel(AppApplication.getAppContext().getPackageManager()).toString()).append(",");
            Log.d(TAG, "Installed package :" + packageInfo.packageName);
            Log.d(TAG, "Source dir : " + packageInfo.sourceDir);
            Log.d(TAG, "Launch Activity :" + pm.getLaunchIntentForPackage(packageInfo.packageName));
        }
        return sb.toString();
    }
}
