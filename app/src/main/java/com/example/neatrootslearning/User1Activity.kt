package com.example.neatrootslearning

import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import org.webrtc.PeerConnectionFactory

class User1Activity : ComponentActivity() {

    private lateinit var peerConnectionFactory: PeerConnectionFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user1)

        val callButton: Button = findViewById(R.id.call_button)
        callButton.setOnClickListener {
            initiateCall()
        }

        initializePeerConnectionFactory()
    }

    private fun initializePeerConnectionFactory() {
        val initializationOptions = PeerConnectionFactory.InitializationOptions.builder(this).createInitializationOptions()
        PeerConnectionFactory.initialize(initializationOptions)

        peerConnectionFactory = PeerConnectionFactory.builder().createPeerConnectionFactory()
    }

    private fun initiateCall() {
        // Simulate sending an offer to User 2
        sendOfferToUser2()
    }

    private fun sendOfferToUser2() {
        // Implement signaling code to send an offer to User 2
        // For simplicity, we're simulating the offer with a placeholder function
        simulateSendOffer()
    }

    private fun simulateSendOffer() {
        // Simulate sending an offer (e.g., using WebSocket or another signaling mechanism)
        // This would normally involve creating an SDP offer and sending it to User 2
    }
}
