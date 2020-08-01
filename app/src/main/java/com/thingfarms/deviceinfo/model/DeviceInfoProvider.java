package com.thingfarms.deviceinfo.model;

import java.util.ArrayList;
import java.util.List;
import io.reactivex.Observable;

public class DeviceInfoProvider {

    public static Observable<List<DeviceInfoData>> getDeviceInfoList(){
        DeviceInfoData deviceInfo;
        List<DeviceInfoData> deviceInfoList;
        deviceInfoList = new RetrieveDeviceInfo().executeRetrieveProcess();
        return Observable.fromArray(deviceInfoList);
    }
}
