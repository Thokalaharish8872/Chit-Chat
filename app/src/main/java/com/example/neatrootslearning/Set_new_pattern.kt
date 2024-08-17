package com.example.neatrootslearning

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.itsxtt.patternlock.PatternLockView

class Set_new_pattern : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_set_new_pattern)

        val patternLockView: PatternLockView = findViewById(R.id.patternLockView)
        var userPattern: ArrayList<Int> = ArrayList()
        Toast.makeText(this,"Create a new Password",Toast.LENGTH_SHORT).show()

        patternLockView.setOnPatternListener(object : PatternLockView.OnPatternListener {
            override fun onStarted() {
                // Called when the user starts drawing the pattern
            }

            override fun onProgress(ids: ArrayList<Int>) {
                // Called as the user draws the pattern (optional)
                userPattern = ids
            }

            override fun onComplete(ids: ArrayList<Int>): Boolean {
                // Called when the user completes drawing the pattern
                // Implement your pattern validation logic here
                // Compare the user-entered pattern (ids) with the expected pattern
                // Return true if correct, false otherwise
                // Example: Check if ids match a predefined pattern stored in your app
                // Replace this with your actual validation logic
                val isCorrect = validatePattern(userPattern, ids)
                if (isCorrect) {
                    // Pattern is correct, save it or perform any other action
                } else {
                    // Show an error message to the user
                }
                return isCorrect
            }
        })
    }

    private fun validatePattern(expectedPattern: ArrayList<Int>, userPattern: ArrayList<Int>): Boolean {
        // Implement your pattern validation logic here
        // Compare the user-entered pattern with the expected pattern
        // Return true if correct, false otherwise
        // Example: Check if userPattern matches expectedPattern
        return userPattern == expectedPattern
    }
}
