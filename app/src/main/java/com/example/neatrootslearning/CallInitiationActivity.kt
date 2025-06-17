//package com.example.neatrootslearning
//
//import android.os.Bundle
//import android.util.Log
//import android.widget.Button
//import androidx.activity.ComponentActivity
//import org.webrtc.*
//
//class CallInitiationActivity : ComponentActivity() {
//
//    private lateinit var peerConnection: PeerConnection
//
//    companion object {
//        private const val TAG = "CallInitiationActivity"
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_call_initiation)
//
//        val acceptButton: Button = findViewById(R.id.accept_button)
//        val rejectButton: Button = findViewById(R.id.reject_button)
//
//        acceptButton.setOnClickListener {
//            // Handle accepting the call
//            startVideoCall()
//        }
//
//        rejectButton.setOnClickListener {
//            // Handle rejecting the call
//            finish() // Close the activity
//        }
//
//        // Set up WebRTC for peer connection and handle the incoming call
//        initializePeerConnection()
//    }
//
//    private fun initializePeerConnection() {
//        // Initialize WebRTC
//        val peerConnectionFactory = PeerConnectionFactory.builder().createPeerConnectionFactory()
//
//        val iceServers = listOf(
//            PeerConnection.IceServer.builder("stun:stun.l.google.com:19302").createIceServer()
//        )
//
//        val rtcConfig = PeerConnection.RTCConfiguration(iceServers)
//        peerConnection = peerConnectionFactory.createPeerConnection(rtcConfig, object : PeerConnection.Observer {
//            override fun onSignalingChange(newState: PeerConnection.SignalingState) {
//                Log.d(TAG, "Signaling state changed: $newState")
//            }
//
//            override fun onIceConnectionChange(newState: PeerConnection.IceConnectionState) {
//                Log.d(TAG, "ICE connection state changed: $newState")
//            }
//
//            override fun onIceConnectionReceivingChange(receiving: Boolean) {}
//
//            override fun onIceGatheringChange(newState: PeerConnection.IceGatheringState) {}
//
//            override fun onIceCandidate(candidate: IceCandidate) {
//                Log.d(TAG, "ICE candidate received: $candidate")
//                sendIceCandidateToRemotePeer(candidate)
//            }
//
//            override fun onIceCandidatesRemoved(candidates: Array<out IceCandidate>?) {}
//
//            override fun onAddStream(stream: MediaStream) {}
//
//            override fun onRemoveStream(stream: MediaStream) {}
//
//            override fun onDataChannel(dataChannel: DataChannel) {}
//
//            override fun onRenegotiationNeeded() {}
//        })!!
//    }
//
//    private fun startVideoCall() {
//        // Start the video call
//        // Implement signaling to exchange SDP offer/answer
//        // Set remote description, create answer, etc.
//    }
//
//
