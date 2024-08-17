package com.example.neatrootslearning

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.webrtc.AudioSource
import org.webrtc.AudioTrack
import org.webrtc.Camera1Enumerator
import org.webrtc.Camera2Enumerator
import org.webrtc.CameraEnumerator
import org.webrtc.DataChannel
import org.webrtc.DefaultVideoDecoderFactory
import org.webrtc.DefaultVideoEncoderFactory
import org.webrtc.EglBase
import org.webrtc.IceCandidate
import org.webrtc.MediaConstraints
import org.webrtc.MediaStream
import org.webrtc.PeerConnection
import org.webrtc.PeerConnectionFactory
import org.webrtc.SdpObserver
import org.webrtc.SessionDescription
import org.webrtc.SurfaceTextureHelper
import org.webrtc.SurfaceViewRenderer
import org.webrtc.VideoCapturer
import org.webrtc.VideoSource
import org.webrtc.VideoTrack

class CallActivity : ComponentActivity() {

    private lateinit var peerConnectionFactory: PeerConnectionFactory
    private lateinit var localVideoTrack: VideoTrack
    private lateinit var localAudioTrack: AudioTrack
    private lateinit var localVideoView: SurfaceViewRenderer
    private lateinit var localAudioSource: AudioSource
    private lateinit var localVideoSource: VideoSource
    private lateinit var peerConnection: PeerConnection

    companion object {
        private const val TAG = "CallActivity"
        private val eglBaseContext = EglBase.create().eglBaseContext
        private const val PERMISSION_REQUEST_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_call)

        // Initialize views
        localVideoView = findViewById(R.id.local_video_view)

        // Check and request permissions
        if (checkPermissions()) {
            initializeWebRTC()
        } else {
            requestPermissions()
        }
    }

    private fun checkPermissions(): Boolean {
        val requiredPermissions = arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.INTERNET
        )
        return requiredPermissions.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(this, arrayOf(
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.INTERNET
        ), PERMISSION_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                initializeWebRTC()
            } else {
                // Handle the case when permissions are not granted
            }
        }
    }

    private fun initializeWebRTC() {
        // Initialize WebRTC
        initializePeerConnectionFactory()
        createLocalMediaStream()
        initializePeerConnections()
        startLocalVideoCapture()
    }

    private fun initializePeerConnectionFactory() {
        val initializationOptions = PeerConnectionFactory.InitializationOptions.builder(this).createInitializationOptions()
        PeerConnectionFactory.initialize(initializationOptions)

        val options = PeerConnectionFactory.Options()
        peerConnectionFactory = PeerConnectionFactory.builder()
            .setOptions(options)
            .setVideoEncoderFactory(DefaultVideoEncoderFactory(eglBaseContext, true, true))
            .setVideoDecoderFactory(DefaultVideoDecoderFactory(eglBaseContext))
            .createPeerConnectionFactory()
    }

    private fun createLocalMediaStream() {
        val mediaConstraints = MediaConstraints()
        localAudioSource = peerConnectionFactory.createAudioSource(mediaConstraints)
        localVideoSource = peerConnectionFactory.createVideoSource(false)

        localAudioTrack = peerConnectionFactory.createAudioTrack("101", localAudioSource)
        localVideoTrack = peerConnectionFactory.createVideoTrack("102", localVideoSource)

        localVideoView.init(eglBaseContext, null)
        localVideoTrack.addSink(localVideoView)
    }

    private fun initializePeerConnections() {
        val iceServers = listOf(
            PeerConnection.IceServer.builder("stun:stun.l.google.com:19302").createIceServer()
        )

        val rtcConfig = PeerConnection.RTCConfiguration(iceServers)
        peerConnection = peerConnectionFactory.createPeerConnection(rtcConfig, object : PeerConnection.Observer {
            override fun onSignalingChange(newState: PeerConnection.SignalingState) {
                Log.d(TAG, "Signaling state changed: $newState")
            }

            override fun onIceConnectionChange(newState: PeerConnection.IceConnectionState) {
                Log.d(TAG, "ICE connection state changed: $newState")
            }

            override fun onIceConnectionReceivingChange(receiving: Boolean) {}

            override fun onIceGatheringChange(newState: PeerConnection.IceGatheringState) {}

            override fun onIceCandidate(candidate: IceCandidate) {
                // Send ICE candidate to the remote peer
                Log.d(TAG, "ICE candidate received: $candidate")
                sendIceCandidateToRemotePeer(candidate)
            }

            override fun onIceCandidatesRemoved(candidates: Array<out IceCandidate>?) {}

            override fun onAddStream(stream: MediaStream) {}

            override fun onRemoveStream(stream: MediaStream) {}

            override fun onDataChannel(dataChannel: DataChannel) {}

            override fun onRenegotiationNeeded() {}
        })!!
    }

    private fun startLocalVideoCapture() {
        val videoCapturer = createVideoCapturer()
        val surfaceTextureHelper = SurfaceTextureHelper.create("CaptureThread", eglBaseContext)
        videoCapturer?.initialize(surfaceTextureHelper, applicationContext, localVideoSource.capturerObserver)
        videoCapturer?.startCapture(1280, 720, 30)
    }

    private fun createVideoCapturer(): VideoCapturer? {
        return if (Camera2Enumerator.isSupported(this)) {
            createCameraCapturer(Camera2Enumerator(this))
        } else {
            createCameraCapturer(Camera1Enumerator(true))
        }
    }

    private fun createCameraCapturer(enumerator: CameraEnumerator): VideoCapturer? {
        val deviceNames = enumerator.deviceNames

        // Try to find a front-facing camera
        for (deviceName in deviceNames) {
            if (enumerator.isFrontFacing(deviceName)) {
                return enumerator.createCapturer(deviceName, null)
            }
        }

        // Front-facing camera not found, try to find a back-facing camera
        for (deviceName in deviceNames) {
            if (enumerator.isBackFacing(deviceName)) {
                return enumerator.createCapturer(deviceName, null)
            }
        }

        return null
    }

    private fun sendIceCandidateToRemotePeer(candidate: IceCandidate) {
        // Implement sending ICE candidate to the remote peer via signaling server
    }

    // Handle SDP offer and answer
    private fun createOffer() {
        val mediaConstraints = MediaConstraints()

        peerConnection.createOffer(object : SdpObserver {
            override fun onCreateSuccess(desc: SessionDescription) {
                // Set local SDP offer
                peerConnection.setLocalDescription(this, desc)

                // Send the SDP offer to the remote peer via signaling server
                sendOfferToRemotePeer(desc)
            }

            override fun onSetSuccess() {}
            override fun onCreateFailure(error: String) {}
            override fun onSetFailure(error: String) {}
        }, mediaConstraints)
    }

    private fun sendOfferToRemotePeer(desc: SessionDescription) {
        // Implement sending SDP offer to the signaling server
    }

    private fun setRemoteDescription(desc: SessionDescription) {
        peerConnection.setRemoteDescription(object : SdpObserver {
            override fun onSetSuccess() {}
            override fun onSetFailure(error: String) {}
            override fun onCreateSuccess(desc: SessionDescription) {}
            override fun onCreateFailure(error: String) {}
        }, desc)
    }
}
