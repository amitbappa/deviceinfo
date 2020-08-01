package com.thingfarms.deviceinfo.model;

import android.app.ActivityManager;

import com.thingfarms.deviceinfo.base.AppApplication;
import com.thingfarms.deviceinfo.util.DeviceInfoUtil;

import static android.content.Context.ACTIVITY_SERVICE;

public class MemoryInformation implements DeviceInfo {
    @Override
    public String getDeviceInfoTitle() {
        return "Memory Info";
    }

    @Override
    public String getDeviceInfoDetails() {
        return getMemoryInfo();
    }
    private  String getMemoryInfo(){
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        ActivityManager activityManager = (ActivityManager) AppApplication.getAppContext().getSystemService(ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(memoryInfo);
        StringBuilder sb = new StringBuilder();

        try {
            sb.append("Capacity: " + DeviceInfoUtil.storageReadable(memoryInfo.totalMem));
        }
        catch(Exception e){
            sb.append("Capacity: " +0);
        }

        try {
            sb.append("  Used: " + DeviceInfoUtil.storageReadable(memoryInfo.totalMem - memoryInfo.availMem));
        }
        catch(Exception exp){
            sb.append("  Used: " +0);
        }
        return  sb.toString();
    }
}
