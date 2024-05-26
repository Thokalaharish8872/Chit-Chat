package com.example.neatrootslearning

import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class Login : AppCompatActivity() {
        companion object{
            const val KEY = ""
        }
    lateinit var database : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        var phone1=findViewById<TextInputEditText>(R.id.editphone)
        var password1=findViewById<TextInputEditText>(R.id.editpassword)
        var Dont=findViewById<Button>(R.id.btn_Dont)
        var Login=findViewById<Button>(R.id.btn_Login)
        var sharedPref = getSharedPreferences("UserDetails", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.clear() // This will clear all data in shared preferences
        editor.apply()



        Login.setOnClickListener() {
            val progressDialog = ProgressDialog(this)
            progressDialog.setMessage("Loading...")
            progressDialog.show()
            val window = progressDialog.window

            // Create a new layout parameters object
            val layoutParams = WindowManager.LayoutParams()

            // Copy the existing layout parameters
            layoutParams.copyFrom(window?.attributes)

            var phone = phone1.text.toString()
            var password = password1.text.toString()
            database = FirebaseDatabase.getInstance().getReference("users")
            database.child(phone).get().addOnSuccessListener {
                var name = it.child("name").value.toString()


                var user = users(name, phone, password)
                if (phone.isNotEmpty()) {
                    if(password.isNotEmpty()){


                    database = FirebaseDatabase.getInstance().getReference("users")
                    database.child(phone).get().addOnSuccessListener {
                        if (it.exists()) {
                            progressDialog.dismiss()
                            var PhoneNumber = it.child("phone").value
                            var Password = it.child("password").value
                            if (Password == password) {
                                var builder1 = AlertDialog.Builder(this)
                                builder1.setTitle("Verification page")
                                builder1.setMessage(
                                    "Your phone Number is " + phone +
                                            " \nDo you want to edit"
                                )
                                builder1.setPositiveButton("Edit",
                                    DialogInterface.OnClickListener { dialog, which ->

                                    })
                                builder1.setNegativeButton(
                                    "No",
                                    DialogInterface.OnClickListener { dialog, which ->
                                        val userUpdates = mapOf<String, Any>(
                                            "name" to name,
                                            "phone" to phone,
                                            "password" to password
                                        )

                                        database.child(phone).updateChildren(userUpdates).addOnSuccessListener {

                                        }.addOnFailureListener {
                                            Toast.makeText(
                                                applicationContext,
                                                "Failed to create Account",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                        val sharedPref = getSharedPreferences("UserDetails", Context.MODE_PRIVATE)
                                        with (sharedPref.edit()) {
                                            putString("PhoneNumber", phone)
                                            putString("Name", name)
                                            putString("Password",password)

                                            apply()
                                        }
                                        val editor = sharedPref.edit()
                                        editor.putBoolean("isLoggedIn", true)
                                        editor.apply()

//                                        Toast.makeText(this,phone,Toast.LENGTH_SHORT).show()
                                        intent = Intent(applicationContext, StartUpPage::class.java)
                                        intent.putExtra(KEY,phone)
//                        intent.putExtra(KEY, phone)
                                        startActivity(intent)
                                    })
                                builder1.show()
                            } else {
                                progressDialog.dismiss()
                                Toast.makeText(
                                    applicationContext,
                                    "Password incorrect",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            progressDialog.dismiss()
                            Toast.makeText(this, "phoneNumber Does not exist", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }.addOnFailureListener {
                        progressDialog.dismiss()
                        Toast.makeText(
                            applicationContext,
                            "Failed to Login",
                            Toast.LENGTH_SHORT
                        ).show()
                    }}
                else{
                    progressDialog.dismiss()
                    Toast.makeText(
                        applicationContext,
                        "Please Enter Your Password",
                        Toast.LENGTH_SHORT
                    ).show()

                }} else {
                    progressDialog.dismiss()
                    Toast.makeText(
                        applicationContext,
                        "Please Enter PhoneNumber",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
            Dont.setOnClickListener() {
                intent = Intent(applicationContext, SignUp::class.java)
                startActivity(intent)
            }



        }

}