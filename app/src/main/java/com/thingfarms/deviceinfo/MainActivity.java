package com.thingfarms.deviceinfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.thingfarms.deviceinfo.adapter.DeviceInfoAdapter;
import com.thingfarms.deviceinfo.model.DeviceInfoData;
import com.thingfarms.deviceinfo.model.DeviceInfoProvider;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    private CompositeDisposable disposables = new CompositeDisposable();
    private DeviceInfoAdapter adapterRV;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        loadData();
    }

    private void initView()
    {
        ButterKnife.bind(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapterRV = new DeviceInfoAdapter();
        recyclerView.setAdapter(adapterRV);
    }
    private void loadData(){
        getUsersObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DeviceInfoData>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposables.add(d);
                    }

                    @Override
                    public void onNext(DeviceInfoData deviceInfo) {
                        adapterRV.updateData(deviceInfo);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "completed!");
                    }
                });
        //.subscribeWith(new DeviceInfoObserver());
    }
    private Observable<DeviceInfoData> getUsersObservable() {
        return DeviceInfoProvider.getDeviceInfoList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap((Function<List<DeviceInfoData>, ObservableSource<DeviceInfoData>>) users -> {
                    adapterRV.setData(users);
                    return Observable.fromIterable(users);
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.dispose();
    }


    private boolean hasPermission() {
        return ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_NETWORK_STATE)
                == PackageManager.PERMISSION_GRANTED ;
    }

}