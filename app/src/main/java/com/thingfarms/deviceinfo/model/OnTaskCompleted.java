package com.thingfarms.deviceinfo.model;

import java.util.List;

public interface OnTaskCompleted<T> {
    void onTaskCompleted(final List<T> deviceInfoData);
}
