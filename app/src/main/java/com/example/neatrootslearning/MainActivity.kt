package com.example.neatrootslearning

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sharedPref = getSharedPreferences("UserDetails", MODE_PRIVATE)
        var isLoggedIn = sharedPref.getBoolean("isLoggedIn", false)


        Handler().postDelayed({
            if(isLoggedIn) {
                // If user is already logged in, redirect them to the Home activity
                intent = Intent(applicationContext, StartUpPage::class.java)
            } else {
                // If user is not logged in, redirect them to the SignUp activity
                intent = Intent(applicationContext, SignUp::class.java)
            }
            startActivity(intent)
            finish()
        },1500)


    }
}