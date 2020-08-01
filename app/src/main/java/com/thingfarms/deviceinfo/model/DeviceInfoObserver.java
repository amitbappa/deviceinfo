package com.thingfarms.deviceinfo.model;

import android.util.Log;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class DeviceInfoObserver implements Observer<DeviceInfoData>{
    private static final String TAG = "DeviceInfoObserver";

    @Override
    public void onSubscribe(Disposable d) {
        Log.i(TAG, "onSubscribe: called");
    }

    @Override
    public void onNext(DeviceInfoData deviceInfo) {
        Log.i(TAG, "onNext: Observed value: " + deviceInfo.getInfoName() );

    }

    @Override
    public void onError(Throwable e) {
        Log.i(TAG, "onError: "+ e.getMessage());

    }

    @Override
    public void onComplete() {
        Log.i(TAG, "onComplete: Done !");

    }
}
