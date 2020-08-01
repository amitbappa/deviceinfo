package com.thingfarms.deviceinfo.model;

import android.os.Environment;
import android.os.StatFs;

import java.text.DecimalFormat;

public class StorageInfo implements DeviceInfo {
    @Override
    public String getDeviceInfoTitle() {
        return "Storage Info";
    }

    @Override
    public String getDeviceInfoDetails() {
        return getStorageInfo();
    }


    private  String getStorageInfo(){
        long availableSpace = -1L;
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        availableSpace = getInternalTotalSpace();

        final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(availableSpace)/Math.log10(1024));
        StringBuilder sb= new StringBuilder();
        sb.append("Capacity: ");
        sb.append((new DecimalFormat("#,##0.#").format(availableSpace/Math.pow(1024, digitGroups)) + " " + units[digitGroups]));

        availableSpace = getInternalUsedSpace();

        sb.append("  Used: ");
        sb.append((new DecimalFormat("#,##0.#").format(availableSpace/Math.pow(1024, digitGroups)) + " " + units[digitGroups]));
        return sb.toString();
    }

    private long getInternalFreeSpace()    {
        //Get free Bytes...
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());

        long bytesAvailable = stat.getBlockSizeLong() * stat.getAvailableBlocksLong();
        return bytesAvailable;
    }

    private long getInternalTotalSpace()    {
        //Get total Bytes
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());

        long bytesTotal = (stat.getBlockSizeLong() * stat.getBlockCountLong());
        return bytesTotal;
    }

    private long getInternalUsedSpace()    {
        //Get used Bytes
        long bytesUsed = getInternalTotalSpace() - getInternalFreeSpace();
        return bytesUsed;
    }
}
