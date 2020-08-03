package com.thingfarms.deviceinfo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.thingfarms.deviceinfo.util.DeviceInfoUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = this.getClass().getSimpleName();
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    @BindView(R.id.btn_appinfo)
    Button btn_appinfo;
    @BindView(R.id.btn_webrtc)
    Button btn_webrtc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        addListener();
        if (!DeviceInfoUtil.checkAndRequestPermissions(this)) {
            Toast.makeText(this, R.string.permission_msg, Toast.LENGTH_LONG)
                    .show();
        }
    }

    private void initView() {
        ButterKnife.bind(this);
    }

    private void addListener() {
        btn_appinfo.setOnClickListener(this);
        btn_webrtc.setOnClickListener(this);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.d(TAG, "Permission callback called-------");
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<>();
                perms.put(Manifest.permission.READ_PHONE_STATE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);

                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    if (perms.get(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED &&
                            perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED &&
                            perms.get(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED &&
                            perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                            perms.get(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED &&
                            perms.get(Manifest.permission.MODIFY_AUDIO_SETTINGS) == PackageManager.PERMISSION_GRANTED
                    ) {
                        addListener();
                    } else {
                        Toast.makeText(this, R.string.permission_msg, Toast.LENGTH_LONG)
                                .show();
                    }
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_appinfo:

                if (!DeviceInfoUtil.checkAndRequestPermissions(this)) {
                    Toast.makeText(this, R.string.permission_msg, Toast.LENGTH_LONG)
                            .show();
                } else {
                    startActivity(new Intent(MainActivity.this, DevInfoActivity.class));

                }
                break;
            case R.id.btn_webrtc:
                if (!DeviceInfoUtil.checkAndRequestPermissions(this)) {
                    Toast.makeText(this, R.string.permission_msg, Toast.LENGTH_LONG)
                            .show();
                } else {
                    startActivity(new Intent(MainActivity.this, WebRTCActivity.class));
                }
                break;
        }
    }


}