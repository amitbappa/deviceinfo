package com.thingfarms.deviceinfo.util;

import java.text.DecimalFormat;

public class DeviceInfoUtil {
    public static String convertBytes (long size){
        long Kb = 1  * 1024;
        long Mb = Kb * 1024;
        long Gb = Mb * 1024;
        long Tb = Gb * 1024;
        long Pb = Tb * 1024;
        long Eb = Pb * 1024;

        if (size <  Kb)                 return floatForm(        size     ) + " byte";
        if (size >= Kb && size < Mb)    return floatForm((double)size / Kb) + " KB";
        if (size >= Mb && size < Gb)    return floatForm((double)size / Mb) + " MB";
        if (size >= Gb && size < Tb)    return floatForm((double)size / Gb) + " GB";
        if (size >= Tb && size < Pb)    return floatForm((double)size / Tb) + " TB";
        if (size >= Pb && size < Eb)    return floatForm((double)size / Pb) + " PB";
        if (size >= Eb)                 return floatForm((double)size / Eb) + " EB";

        return "anything...";
    }
    public static String floatForm (double d)    {
        return new DecimalFormat("#.##").format(d);
    }

    public static String storageReadable(Long val){
        final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(val)/Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(val/Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

}
