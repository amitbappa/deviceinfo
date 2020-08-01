package com.thingfarms.deviceinfo.model;

import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

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
                wifi_mobileInfo.append("Wifi: ");
            }
            if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                isMobileConn |= networkInfo.isConnected();
                wifi_mobileInfo.append("Mobile: ");
            }
        }

        if (isWifiConn) {
            wifi_mobileInfo.append("Wifi data: ").append("\n");
            WifiManager wifiMgr = (WifiManager) AppApplication.getAppContext().getApplicationContext().getSystemService(AppApplication.getAppContext().WIFI_SERVICE);
            WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
            wifi_mobileInfo.append(" BSSID: " + wifiInfo.getBSSID()).append("\n");
            wifi_mobileInfo.append(" MacAddress: " + wifiInfo.getMacAddress()).append("\n");
            wifi_mobileInfo.append(" Frequency: " + wifiInfo.getFrequency()).append("\n");
            wifi_mobileInfo.append(" SSID: " + wifiInfo.getSSID()).append("\n");
            wifi_mobileInfo.append(" IP Address: " + wifiInfo.getIpAddress()).append("\n");
            wifi_mobileInfo.append(" Speed: " + wifiInfo.getLinkSpeed()).append("\n");
            wifi_mobileInfo.append(" Max Supported RX speed: " + wifiInfo.getMaxSupportedRxLinkSpeedMbps()).append("\n");
            wifi_mobileInfo.append(" Max Supported TX speed: " + wifiInfo.getMaxSupportedTxLinkSpeedMbps()).append("\n");
            wifi_mobileInfo.append(" Provider Name: " + wifiInfo.getPasspointProviderFriendlyName()).append("\n");
        }
        if (isMobileConn) {
            wifi_mobileInfo.append(" Mobile: ").append("\n");

        }
        return wifi_mobileInfo.toString();

    }
}
