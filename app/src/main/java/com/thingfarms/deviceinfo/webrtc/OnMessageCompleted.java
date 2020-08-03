package com.thingfarms.deviceinfo.webrtc;

public interface OnMessageCompleted {
    void onTaskCompletedClient(String message);
    void onTaskCompletedServer(String message);
}
