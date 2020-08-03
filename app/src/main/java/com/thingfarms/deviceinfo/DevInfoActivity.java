package com.thingfarms.deviceinfo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thingfarms.deviceinfo.adapter.DeviceInfoAdapter;
import com.thingfarms.deviceinfo.model.DeviceInfoData;
import com.thingfarms.deviceinfo.model.DeviceInfoProvider;
import com.thingfarms.deviceinfo.util.DeviceInfoUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class DevInfoActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    private CompositeDisposable disposables = new CompositeDisposable();
    private DeviceInfoAdapter deviceInfoAdapter;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.loading)
    ViewSwitcher viewLoading;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deviceinfo);
        initView();
        if (DeviceInfoUtil.checkAndRequestPermissions(this)) {
            loadData();
        }
    }

    private void initView() {
        ButterKnife.bind(this);
        viewLoading.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        deviceInfoAdapter = new DeviceInfoAdapter();
        recyclerView.setAdapter(deviceInfoAdapter);
    }

    private void loadData() {
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
                        deviceInfoAdapter.updateData(deviceInfo);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "completed!");
                        viewLoading.setVisibility(View.INVISIBLE);

                    }
                });
    }

    private Observable<DeviceInfoData> getUsersObservable() {
        return new DeviceInfoProvider().getDeviceInfoList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap((Function<List<DeviceInfoData>, ObservableSource<DeviceInfoData>>) infos -> {
                    deviceInfoAdapter.setData(infos);
                    return Observable.fromIterable(infos);
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.dispose();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
    String permissions[], int[] grantResults) {
        Log.d(TAG, "Permission callback called-------");
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<>();
                perms.put(Manifest.permission.READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    if (perms.get(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED
                            &&        perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        loadData();
                    } else {
                        Toast.makeText(this,  R.string.permission_msg, Toast.LENGTH_LONG)
                                .show();
                    }
                }
            }
        }
    }
}
