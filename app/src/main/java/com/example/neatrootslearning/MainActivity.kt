package com.example.neatrootslearning

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    lateinit var database:DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var sharedPref = getSharedPreferences("UserDetails", MODE_PRIVATE)
        var isLoggedIn = sharedPref.getBoolean("isLoggedIn", false)



        Handler().postDelayed({
            if(isLoggedIn) {
                val userId = sharedPref.getString("PhoneNumber", null)
                database=FirebaseDatabase.getInstance().getReference("users")
                database.child(userId.toString()).child("Is_Online").setValue("True")
                intent=Intent(this,StartUpPage::class.java)

                startActivity(intent)
                finish()
                // If user is already logged in, redirect them to the Home activity

            } else {
                // If user is not logged in, redirect them to the SignUp activity
                intent = Intent(applicationContext, SignUp::class.java)
                startActivity(intent)
                finish()
            }

        },1000)


    }
}