package com.thingfarms.deviceinfo.model;

import java.util.ArrayList;
import java.util.List;

public class RetrieveDeviceInfo implements Runnable {
    private OnTaskCompleted listener;

    public RetrieveDeviceInfo(OnTaskCompleted listener) {
        this.listener=listener;
    }

    public List<DeviceInfoData> executeRetrieveProcess() {
        DeviceInfo deviceInfo;
        List<DeviceInfoData> deviceInfoDataList = new ArrayList<>();
        deviceInfo = new InstalledAppList();
        deviceInfoDataList.add(new DeviceInfoData(deviceInfo.getDeviceInfoTitle(), deviceInfo.getDeviceInfoDetails()));
        deviceInfo = new StorageInfo();
        deviceInfoDataList.add(new DeviceInfoData(deviceInfo.getDeviceInfoTitle(), deviceInfo.getDeviceInfoDetails()));
        deviceInfo = new MemoryInformation();
        deviceInfoDataList.add(new DeviceInfoData(deviceInfo.getDeviceInfoTitle(), deviceInfo.getDeviceInfoDetails()));
        deviceInfo = new CPUInfo();
        deviceInfoDataList.add(new DeviceInfoData(deviceInfo.getDeviceInfoTitle(), deviceInfo.getDeviceInfoDetails()));
        deviceInfo = new WifiMobileInfo();
        deviceInfoDataList.add(new DeviceInfoData(deviceInfo.getDeviceInfoTitle(), deviceInfo.getDeviceInfoDetails()));
        deviceInfo = new SensorDataInfo();
        deviceInfoDataList.add(new DeviceInfoData(deviceInfo.getDeviceInfoTitle(), deviceInfo.getDeviceInfoDetails()));

        return deviceInfoDataList;
    }

    @Override
    public void run() {
        listener.onTaskCompleted(executeRetrieveProcess());
    }
}
