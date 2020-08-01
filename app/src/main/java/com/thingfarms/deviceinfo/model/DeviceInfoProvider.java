package com.thingfarms.deviceinfo.model;

import java.util.ArrayList;
import java.util.List;
import io.reactivex.Observable;

public class DeviceInfoProvider {

    public static Observable<List<DeviceInfoData>> getDeviceInfoList(){
        DeviceInfoData deviceInfo;
        List<DeviceInfoData> deviceInfoList = new ArrayList<>();

        deviceInfoList = new RetrieveDeviceInfo().executeRetrieveProcess();


       /* for (int i=0; i<100;i++){
            deviceInfo = new DeviceInfoData("#"+i,("ABC"+i));
            //deviceInfo.setInfoName("#"+i);
           // deviceInfo.setInfoDetails("ABC"+i);
            deviceInfoList.add(deviceInfo);
        }*/


        return Observable.fromArray(deviceInfoList);
    }
}
