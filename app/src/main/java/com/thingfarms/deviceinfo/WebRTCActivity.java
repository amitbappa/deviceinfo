package com.thingfarms.deviceinfo;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.thingfarms.deviceinfo.webrtc.OnMessageCompleted;
import com.thingfarms.deviceinfo.webrtc.PeerConnectionClient;

public class WebRTCActivity extends AppCompatActivity implements OnMessageCompleted {

    private  final String TAG = this.getClass().getSimpleName();
    private Handler mHandler;
    private PeerConnectionClient peerConnectionClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webrtc);

        try {
            peerConnectionClient = new PeerConnectionClient(this);
        }catch (Exception exp){
            exp.printStackTrace();
            EditText messageLocal = (EditText) findViewById(R.id.localPeerSendMessageEditText);
            messageLocal.setText("ERROR");
            EditText messageRemote = (EditText) findViewById(R.id.remotePeerSendMessageEditText);
            messageRemote.setText("ERROR");
        }
        mHandler = new Handler();
        Log.d(TAG, "init MainActivity onCreate");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
        setSendButtonListeners();
    }

    private void setSendButtonListeners() {
        Button localSend = (Button) findViewById(R.id.localPeerButtonSend);
        assert localSend != null;
        localSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText messageET = (EditText) findViewById(R.id.localPeerSendMessageEditText);
                assert messageET != null;
                String message = messageET.getText().toString();
                if (!message.isEmpty()) {
                    peerConnectionClient.sendData(message);
                }
            }
        });

        Button remoteSend = (Button) findViewById(R.id.remotePeerSendButton);
        assert remoteSend != null;

        remoteSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText messageET = (EditText) findViewById(R.id.remotePeerSendMessageEditText);
                assert messageET != null;
                String message = messageET.getText().toString();
                if (!message.isEmpty()) {
                    peerConnectionClient.receiveData(message);

                }
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        try {
            peerConnectionClient.closeConnection();
        } catch (Exception exp) {
            exp.printStackTrace();
        }

    }

    @Override
    public void onTaskCompletedClient(final String message) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                TextView messageReceived = (TextView) findViewById(R.id.localPeerMessageReceived);
                messageReceived.setText(message);
            }
        });
    }
    @Override
    public void onTaskCompletedServer(final String message) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                TextView messageReceived = (TextView) findViewById(R.id.remotePeerMessageReceived);
                messageReceived.setText(message);
            }
        });
    }
}
