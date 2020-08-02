package com.thingfarms.deviceinfo.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class DeviceInfoProvider implements OnTaskCompleted {
    private final String TAG = this.getClass().getSimpleName();
    private boolean isExecuteCompleted = false;
    private List<DeviceInfoData> deviceInfoList;

    public Observable<List<DeviceInfoData>> getDeviceInfoList() {
        (new Thread(new RetrieveDeviceInfo(this))).start();
        while (isExecuteCompleted != true) {
            Log.i(TAG, "Running");
        }
        return Observable.fromArray(deviceInfoList);
    }

    @Override
    public void onTaskCompleted(List deviceInfoData) {
        deviceInfoList = deviceInfoData;
        isExecuteCompleted = true;
    }
}
