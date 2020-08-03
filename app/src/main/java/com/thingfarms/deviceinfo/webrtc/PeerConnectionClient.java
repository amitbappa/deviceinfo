package com.thingfarms.deviceinfo.webrtc;

import android.util.Log;

import com.thingfarms.deviceinfo.base.AppApplication;

import org.json.JSONException;
import org.json.JSONObject;
import org.webrtc.DataChannel;
import org.webrtc.IceCandidate;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnection;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.SdpObserver;
import org.webrtc.SessionDescription;

import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;

public class PeerConnectionClient {
    private String stun_server="stun:stun.l.google.com:19302";
    private  final String TAG = this.getClass().getSimpleName();
    private PeerConnectionFactory peerConnectionFactory;
    private PeerConnection localPeerConnection;
    private PeerConnection remotePeerConnection;
    private String localMessageReceived = " ";
    private String remoteMessageReceived = " ";

    private DataChannel sendChannel;
    private DataChannel receiveChannel;
    private OnMessageCompleted onTaskCompleted;
    List<PeerConnection.IceServer> iceServers = new LinkedList<>();
    MediaConstraints constraints;

    public PeerConnectionClient(OnMessageCompleted onTaskCompleted)
    {
        this.onTaskCompleted=onTaskCompleted;
        Log.d(TAG, PeerConnectionFactory.initializeAndroidGlobals(AppApplication.getAppContext(), true, true, true)
                ? "Success initAndroidGlobals" : "Failed initAndroidGlobals");
        peerConnectionFactory = new PeerConnectionFactory();
        createConnection();

    }
    public void sendData(String message)
    {
        if (sendChannel.state() == DataChannel.State.OPEN) {
            ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
            sendChannel.send(new DataChannel.Buffer(buffer, false));
        }

    }
    public void receiveData(String message)
    {
        if (!message.isEmpty()) {
            if (receiveChannel.state() == DataChannel.State.OPEN) {
                ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
                receiveChannel.send(new DataChannel.Buffer(buffer, false));
            }
        }
    }

    public void closeConnection()
    {
        if (sendChannel != null) {
            sendChannel.close();
            sendChannel.unregisterObserver();
        }
        if (receiveChannel != null) {
            receiveChannel.close();
            receiveChannel.unregisterObserver();
        }
        if (localPeerConnection != null)
            localPeerConnection.close();
        if (remotePeerConnection != null)
            remotePeerConnection.close();
    }
    public void createConnection(){

        iceServers.add(new PeerConnection.IceServer(stun_server));
        constraints = new MediaConstraints();
        localPeerConnection = peerConnectionFactory.createPeerConnection(iceServers, constraints, localPeerConnectionObserver);
        remotePeerConnection = peerConnectionFactory.createPeerConnection(iceServers, constraints, remotePeerConnectionObserver);
        sendChannel = localPeerConnection.createDataChannel("RTCDataChannel", new DataChannel.Init());
        sendChannel.registerObserver(localDataChannelObserver);
        localPeerConnection.createOffer(localSessionObserver, constraints);
    }

    PeerConnection.Observer localPeerConnectionObserver = new PeerConnection.Observer() {
        @Override
        public void onSignalingChange(PeerConnection.SignalingState signalingState) {
            Log.d(TAG, "localPeerConnectionObserver onSignalingChange() " + signalingState.name());
        }

        @Override
        public void onIceConnectionChange(PeerConnection.IceConnectionState iceConnectionState) {
            Log.d(TAG, "localPeerConnectionObserver onIceConnectionChange() " + iceConnectionState.name());
        }

        @Override
        public void onIceConnectionReceivingChange(boolean b) {
            Log.d(TAG, "localPeerConnectionObserver onIceConnectionReceivingChange(): " + b);
        }

        @Override
        public void onIceGatheringChange(PeerConnection.IceGatheringState iceGatheringState) {
            Log.d(TAG, "localPeerConnectionObserver onIceGatheringChange() " + iceGatheringState.name());
        }

        @Override
        public void onIceCandidate(IceCandidate iceCandidate) {
            Log.d(TAG, "localPeerConnectionObserver onIceCandidate: " + iceCandidate.toString());
            JSONObject json = new JSONObject();
            String mes;
            try {
                json.put("type", "candidate");
                json.put("sdpMLineIndex", iceCandidate.sdpMLineIndex);
                json.put("sdpMid", iceCandidate.sdpMid);
                json.put("candidate", iceCandidate.sdp);
                mes = json.toString();
                Log.d (TAG, "local iceCandidateJson" + mes);

                // Here, send a mes to the other party in the WebSocket, etc.
                // On the receiving side,
                JSONObject json2 = new JSONObject(mes);
                IceCandidate candidate = new IceCandidate(json2.getString("sdpMid"), json2.getInt("sdpMLineIndex"), json2.getString("candidate"));
                remotePeerConnection.addIceCandidate (candidate);

            } catch (org.json.JSONException ex) {
                Log.d(TAG, ex.toString());
            }
        }

        @Override
        public void onAddStream(MediaStream mediaStream) {

        }

        @Override
        public void onRemoveStream(MediaStream mediaStream) {

        }

        @Override
        public void onDataChannel(DataChannel dataChannel) {
            Log.d(TAG, "localPeerConnectionObserver onDataChannel()");
        }

        @Override
        public void onRenegotiationNeeded() {
            Log.d(TAG, "localPeerConnectionObserver onRenegotiationNeeded()");
        }
    };
    PeerConnection.Observer remotePeerConnectionObserver = new PeerConnection.Observer() {
        @Override
        public void onSignalingChange(PeerConnection.SignalingState signalingState) {
            Log.d(TAG, "remotePeerConnectionObserver onSignalingChange() " + signalingState.name());
        }

        @Override
        public void onIceConnectionChange(PeerConnection.IceConnectionState iceConnectionState) {
            Log.d(TAG, "remotePeerConnectionObserver onIceConnectionChange() " + iceConnectionState.name());
        }

        @Override
        public void onIceConnectionReceivingChange(boolean b) {
            Log.d(TAG, "remotePeerConnectionObserver onIceConnectionReceivingChange(): " + b);
        }

        @Override
        public void onIceGatheringChange(PeerConnection.IceGatheringState iceGatheringState) {
            Log.d(TAG, "remotePeerConnectionObserver onIceGatheringChange() " + iceGatheringState.name());
        }

        @Override
        public void onIceCandidate(IceCandidate iceCandidate) {
            Log.d(TAG, "remotePeerConnectionObserver onIceCandidate: " + iceCandidate.toString());

            JSONObject json = new JSONObject();

            String message;

            try {
                json.put("type", "candidate");
                json.put("sdpMLineIndex", iceCandidate.sdpMLineIndex);
                json.put("sdpMid", iceCandidate.sdpMid);
                json.put("candidate", iceCandidate.sdp);

                message = json.toString();

                Log.d (TAG, "remote iceCandidateJson" + message);

                // Here, send a message to the other party in the WebSocket, etc.
                // On the receiving side,

                JSONObject json2 = new JSONObject(message);
                IceCandidate candidate = new IceCandidate(json2.getString("sdpMid"), json2.getInt("sdpMLineIndex"), json2.getString("candidate"));
                localPeerConnection.addIceCandidate (candidate);

            } catch (org.json.JSONException ex) {
                Log.d(TAG, ex.toString());
            }
        }

        @Override
        public void onAddStream(MediaStream mediaStream) {

        }

        @Override
        public void onRemoveStream(MediaStream mediaStream) {

        }

        @Override
        public void onDataChannel(DataChannel dataChannel) {
            Log.d(TAG, "remotePeerConnectionObserver onDataChannel()");
            receiveChannel = dataChannel;
            receiveChannel.registerObserver(remoteDataChannelObserver);
        }

        @Override
        public void onRenegotiationNeeded() {
            Log.d(TAG, "remotePeerConnectionObserver onRenegotiationNeeded()");
        }
    };
    SdpObserver localSessionObserver = new SdpObserver() {
        @Override
        public void onCreateSuccess(SessionDescription sessionDescription) {
            Log.d(TAG, "local onCreateSuccess");

            localPeerConnection.setLocalDescription(localSessionObserver, sessionDescription);

            JSONObject json = new JSONObject();
            String message;

            try {
                json.put("type", sessionDescription.type.toString().toLowerCase());
                json.put("sdp", sessionDescription.description);

                message = json.toString();
                Log.d(TAG, message);

                /** Here we implement the signaling mecanism that will pass and retrieve the sdp message**/

                JSONObject json2 = new JSONObject(message);
                String type = json2.getString("type");
                String sdp = json2.getString("sdp");

                SessionDescription sdp2 = new SessionDescription(SessionDescription.Type.fromCanonicalForm(type), sdp);

                remotePeerConnection.setRemoteDescription(remoteSessionObserver, sdp2);
                MediaConstraints constraints = new MediaConstraints();
                remotePeerConnection.createAnswer(remoteSessionObserver, constraints);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onSetSuccess() {
            Log.d(TAG, "local onSetSuccess");
        }

        @Override
        public void onCreateFailure(String s) {
            Log.d(TAG, "local onCreateFailure " + s);
        }

        @Override
        public void onSetFailure(String s) {
            Log.d(TAG, "local onSetFailure " + s);
        }
    };
    SdpObserver remoteSessionObserver = new SdpObserver() {
        @Override
        public void onCreateSuccess(SessionDescription sessionDescription) {
            Log.d(TAG, "remote onCreateSuccess()");

            remotePeerConnection.setLocalDescription(remoteSessionObserver, sessionDescription);

            JSONObject json = new JSONObject();
            String message;

            try {
                json.put("type", sessionDescription.type.toString().toLowerCase());
                json.put("sdp", sessionDescription.description);

                message = json.toString();
                Log.d(TAG, message);

                /**Signaling Mechanism to exchange SessionDescription object information goes here**/

                JSONObject json2 = new JSONObject(message);
                String type = json2.getString("type");
                String sdp = json2.getString("sdp");
                SessionDescription sdp2 = new SessionDescription(SessionDescription.Type.fromCanonicalForm(type), sdp);
                localPeerConnection.setRemoteDescription(localSessionObserver, sdp2);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onSetSuccess() {
            Log.d(TAG, "remote onSetSuccess()");
        }

        @Override
        public void onCreateFailure(String s) {
            Log.d(TAG, "remote onCreateFailure() " + s);
        }

        @Override
        public void onSetFailure(String s) {
            Log.d(TAG, "remote onSetFailure()");
        }
    };
    DataChannel.Observer localDataChannelObserver = new DataChannel.Observer() {

        @Override
        public void onBufferedAmountChange(long l) {

        }

        @Override
        public void onStateChange() {
            Log.d(TAG, "localDataChannelObserver onStateChange() " + sendChannel.state().name());

//            if (sendChannel.state() == DataChannel.State.OPEN) {
//                String data = "from sendChannel to receiveChannel";
//                ByteBuffer buffer = ByteBuffer.wrap(data.getBytes());
//                sendChannel.send(new DataChannel.Buffer(buffer, false));
//            }
        }

        @Override
        public void onMessage(DataChannel.Buffer buffer) {
            Log.d(TAG, "localDataChannelObserver onMessage()");

            if (!buffer.binary) {
                int limit = buffer.data.limit();
                byte[] datas = new byte[limit];
                buffer.data.get(datas);
                localMessageReceived = new String(datas);
                onTaskCompleted.onTaskCompletedClient(localMessageReceived);


            }

        }
    };
    DataChannel.Observer remoteDataChannelObserver = new DataChannel.Observer() {
        @Override
        public void onBufferedAmountChange(long l) {


        }

        @Override
        public void onStateChange() {
            Log.d(TAG, "remoteDataChannel onStateChange() " + receiveChannel.state().name());

//            if (receiveChannel.state() == DataChannel.State.OPEN) {
//                String data = "from receiveChannel to sendChannel";
//                ByteBuffer buffer = ByteBuffer.wrap(data.getBytes());
//                receiveChannel.send(new DataChannel.Buffer(buffer, false));
//            }
        }

        @Override
        public void onMessage(DataChannel.Buffer buffer) {
            Log.d(TAG, "remoteDataChannel onMessage()");

            if (!buffer.binary) {
                int limit = buffer.data.limit();
                byte[] datas = new byte[limit];
                buffer.data.get(datas);
                remoteMessageReceived = new String(datas);
                onTaskCompleted.onTaskCompletedServer(remoteMessageReceived);
            }
        }

    };

}
