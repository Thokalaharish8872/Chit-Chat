package com.example.neatrootslearning

import AppLifecycleObserver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.itsxtt.patternlock.PatternLockView

class Hidden_chats_page : AppCompatActivity() {
    private lateinit var lifecycleObserver: AppLifecycleObserver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hidden_chats_page)
        val patternLockView: PatternLockView = findViewById(R.id.patternLockView)
        var sharedPref = getSharedPreferences("UserDetails", Context.MODE_PRIVATE)
        val isLockSet = sharedPref.getBoolean("IS_Lock_set", false)

        if (isLockSet) {
            // User has already set a pattern, proceed with pattern validation
            patternLockView.setOnPatternListener(object : PatternLockView.OnPatternListener {
                override fun onStarted() {
                    // Called when the user starts drawing the pattern
                }

                override fun onProgress(ids: ArrayList<Int>) {
                    // Called as the user draws the pattern (optional)
                }

                override fun onComplete(ids: ArrayList<Int>): Boolean {
                    // Implement your pattern validation logic here
                    // Compare the user-entered pattern (ids) with the expected pattern
                    // Return true if correct, false otherwise
                    // Example: Check if ids match a predefined pattern stored in your app
                    // Replace this with your actual validation logic
                    return false // Placeholder; replace with your implementation
                }
            })
        } else {
            // User hasn't set a pattern, prompt them to set a new pattern
            // For example, navigate to a new activity where the user can set a new pattern
            val intent = Intent(this, Set_new_pattern::class.java)
            startActivity(intent)
            finish() // Optional: Close this activity to prevent going back to it
        }
       sharedPref = getSharedPreferences("UserDetails", Context.MODE_PRIVATE)
       var phoneNumber = sharedPref.getString("PhoneNumber", null).toString()

        lifecycleObserver = AppLifecycleObserver(phoneNumber!!)
        lifecycle.addObserver(lifecycleObserver)
    }
}
