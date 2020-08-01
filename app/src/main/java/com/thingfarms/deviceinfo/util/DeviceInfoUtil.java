package com.thingfarms.deviceinfo.util;

import java.text.DecimalFormat;

public class DeviceInfoUtil {
    public static String storageReadable(Long val){
        final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(val)/Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(val/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

}
