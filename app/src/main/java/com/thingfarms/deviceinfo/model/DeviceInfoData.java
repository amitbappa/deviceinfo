package com.thingfarms.deviceinfo.model;

public class DeviceInfoData {
    private String info_name;
    private String info_details;

    public DeviceInfoData(String info_name,String info_details ){
        this.info_name = info_name;
        this.info_details = info_details;

    }
    public String getInfoName() {
        return info_name;
    }

    public void setInfoName(String info_name) {
        this.info_name = info_name;
    }

    public String getInfoDetails() {
        return info_details;
    }

    public void setInfoDetails(String info_details) {
        this.info_details = info_details;
    }

}
