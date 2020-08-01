package com.thingfarms.deviceinfo.model;

import android.hardware.Sensor;
import android.hardware.SensorManager;

import com.thingfarms.deviceinfo.base.AppApplication;

import java.util.List;

public class SensorDataInfo implements DeviceInfo {
    @Override
    public String getDeviceInfoTitle() {
        return "Device Sensor";
    }

    @Override
    public String getDeviceInfoDetails() {
        return getDeviceSensorsData();
    }

    private  String getDeviceSensorsData()
    {
        SensorManager sensorManager;
        sensorManager = (SensorManager) AppApplication.getAppContext().getSystemService(AppApplication.getAppContext().SENSOR_SERVICE);

        List<Sensor> deviceSensors = sensorManager.getSensorList(Sensor.TYPE_ALL);
        StringBuilder sensorData= new StringBuilder();

        for (Sensor sensor:deviceSensors ) {
            sensorData.append("\n" +" Name: "+ sensor.getName() + "\n" +" Vendor: " +sensor.getVendor() + "\n" + " Version: " +sensor.getVersion());
            sensorData.append("\n");
        }

        return sensorData.toString();
    }
}
