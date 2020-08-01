package com.thingfarms.deviceinfo.model;

import java.io.IOException;
import java.io.InputStream;

public class CPUInfo implements DeviceInfo {
    
    @Override
    public String getDeviceInfoTitle() {
        return "CPU Info";
    }

    @Override
    public String getDeviceInfoDetails() {
        return getCPUInfo();
    }

    private  String getCPUInfo()  {
        ProcessBuilder processBuilder;
        StringBuilder cpuInfo =new StringBuilder();
        String[] DATA = {"/system/bin/cat", "/proc/cpuinfo"};
        InputStream inputStream = null;
        Process process ;
        byte[] byteArray ;
        byteArray = new byte[1024];

        try{
            processBuilder = new ProcessBuilder(DATA);
            process = processBuilder.start();
            inputStream = process.getInputStream();
            while(inputStream.read(byteArray) != -1){
                cpuInfo.append(new String(byteArray));
                //Holder = Holder + new String(byteArry);
            }
            inputStream.close();
        } catch(IOException ex){
            ex.printStackTrace();
            cpuInfo.append("Error in getting CPU info");
        }
        finally {
            if(null!=inputStream){
                try {
                    inputStream.close();
                }
                catch(IOException ex){
                    ex.printStackTrace();
                }
            }
        }
        return cpuInfo.toString();
    }
}
