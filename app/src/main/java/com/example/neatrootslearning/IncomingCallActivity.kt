package com.example.neatrootslearning

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity

class IncomingCallActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_incomming_call)

        val incomingCallText: TextView = findViewById(R.id.incoming_call_text)
        val acceptButton: Button = findViewById(R.id.accept_button)
        val declineButton: Button = findViewById(R.id.decline_button)

        // Display who is calling
        incomingCallText.text = "Incoming call from User 1"

        acceptButton.setOnClickListener {
            // Handle accepting the call
            handleAcceptCall()
        }

        declineButton.setOnClickListener {
            // Handle declining the call
            handleDeclineCall()
        }
    }

    private fun handleAcceptCall() {
        // Implement logic to accept the call and start video call activity
        startActivity(Intent(this, CallActivity::class.java).apply {
            putExtra("isIncomingCall", true)
        })
        finish() // Close the incoming call screen
    }

    private fun handleDeclineCall() {
        // Implement logic to decline the call
        finish() // Close the incoming call screen
    }
}
