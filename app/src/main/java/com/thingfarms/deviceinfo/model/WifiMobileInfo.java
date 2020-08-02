package com.thingfarms.deviceinfo.model;


import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;

import com.thingfarms.deviceinfo.base.AppApplication;

public class WifiMobileInfo implements DeviceInfo {
    @Override
    public String getDeviceInfoTitle() {
        return "Wifi/Mobile info :";
    }

    @Override
    public String getDeviceInfoDetails() {
        return getWifiMobileInfo();
    }

    private String getWifiMobileInfo() {
        StringBuilder wifi_mobileInfo = new StringBuilder();
        ConnectivityManager connMgr =
                (ConnectivityManager) AppApplication.getAppContext().getSystemService(AppApplication.getAppContext().CONNECTIVITY_SERVICE);
        boolean isWifiConn = false;
        boolean isMobileConn = false;
        for (Network network : connMgr.getAllNetworks()) {
            NetworkInfo networkInfo = connMgr.getNetworkInfo(network);
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                isWifiConn |= networkInfo.isConnected();
            }
            if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                isMobileConn |= networkInfo.isConnected();
            }
        }
        wifi_mobileInfo.append("  Wifi info: ").append("\n");
        wifi_mobileInfo.append(" Is Wifi connected: " ).append(isWifiConn?"Yes":"No").append("\n");
        wifi_mobileInfo.append(getWifiData());
        wifi_mobileInfo.append("\n").append(" Mobile info: ").append("\n");
        wifi_mobileInfo.append(" Is Mobile connected: " ).append(isMobileConn?"Yes":"No").append("\n");
        wifi_mobileInfo.append(getMobileData());

        return wifi_mobileInfo.toString();
    }

    private String getWifiData()
    {
        StringBuilder wifi_info = new StringBuilder();
        WifiManager wifiMgr = (WifiManager) AppApplication.getAppContext().getApplicationContext().getSystemService(AppApplication.getAppContext().WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        wifi_info.append(" BSSID: " + wifiInfo.getBSSID()).append("\n");
        wifi_info.append(" MacAddress: " + wifiInfo.getMacAddress()).append("\n");
        wifi_info.append(" Frequency: " + wifiInfo.getFrequency()).append("\n");
        wifi_info.append(" SSID: " + wifiInfo.getSSID()).append("\n");
        wifi_info.append(" IP Address: " + wifiInfo.getIpAddress()).append("\n");
        wifi_info.append(" Speed: " + wifiInfo.getLinkSpeed()).append("\n");
        return wifi_info.toString();
    }

    private String getMobileData() {
        StringBuilder mobileData = new StringBuilder();
        TelephonyManager tm = (TelephonyManager) AppApplication.getAppContext().getSystemService(AppApplication.getAppContext().TELEPHONY_SERVICE);
        int phoneType = tm.getPhoneType();
        switch (phoneType) {
            case (TelephonyManager.PHONE_TYPE_CDMA):
                mobileData.append("Phone Network Type: ").append("CDMA").append("\n");
                break;
            case (TelephonyManager.PHONE_TYPE_GSM):
                mobileData.append("Phone Network Type: ").append("GSM").append("\n");
                break;
            case (TelephonyManager.PHONE_TYPE_NONE):
                mobileData.append("Phone Network Type: ").append("NONE").append("\n");
                break;
        }

        mobileData.append("IMEI Number: ").append(tm.getDeviceId()).append("\n");
        mobileData.append("Sim Serial Number: ").append(tm.getSimSerialNumber()).append("\n");
        mobileData.append("Network Country ISO: ").append(tm.getNetworkCountryIso()).append("\n");
        mobileData.append("SIM Country ISO: ").append(tm.getSimCountryIso()).append("\n");
        mobileData.append("In Roaming: ").append(tm.isNetworkRoaming() ? "Yes" : "No");
        return mobileData.toString();
    }
}
