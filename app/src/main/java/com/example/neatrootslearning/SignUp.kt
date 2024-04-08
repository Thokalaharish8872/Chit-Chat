package com.example.neatrootslearning

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUp : AppCompatActivity() {
//    companion object{
//        const val KEY = ""
//    }

    lateinit var database : DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        
        var SignUp=findViewById<Button>(R.id.btn_SignUp)
        var Already=findViewById<Button>(R.id.btn_Already)
        var name1=findViewById<TextInputEditText>(R.id.editname)
        var phone1=findViewById<TextInputEditText>(R.id.editphone)
        var password1=findViewById<TextInputEditText>(R.id.editpassword)


        SignUp.setOnClickListener(){
            var name = name1.text.toString()
            var phone = phone1.text.toString()
            var password=password1.text.toString()

            var user = users(name,phone,password)


            if(phone.isNotEmpty()) {
                    database=FirebaseDatabase.getInstance().getReference("users")
                    database.child(phone).get().addOnSuccessListener {

                    if (it.exists()) {
                        Toast.makeText(this, "phoneNumber already exist", Toast.LENGTH_SHORT).show()
                    } else {

                        var builder1 = AlertDialog.Builder(this)
                        builder1.setTitle("Verification page")
                        builder1.setMessage(
                            "Your phone Number is " + phone +
                                    " \nDo you want to edit"
                        )
                        builder1.setPositiveButton(
                            "Edit",
                            DialogInterface.OnClickListener { dialog, which ->

                            })
                        builder1.setNegativeButton(
                            "No",
                            DialogInterface.OnClickListener { dialog, which ->
                                database.child(phone).setValue(user).addOnSuccessListener {


                                }.addOnFailureListener {
                                    Toast.makeText(
                                        applicationContext,
                                        "Failed to create Account",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                var sharedPref = getSharedPreferences("UserDetails", Context.MODE_PRIVATE)
                                with (sharedPref.edit()) {
                                    putString("PhoneNumber", phone)
                                    putString("Name", name)
                                    putString("About","Hey! Nenu Chit Chat Vaadutunna")
                                    putString("Password",password)
                                    apply()

                                }
                                val editor = sharedPref.edit()
                                editor.putBoolean("isLoggedIn", true)
                                editor.apply()
                                sharedPref = getSharedPreferences("UserDetails", Context.MODE_PRIVATE)
                                val uri = sharedPref.getString("uri", null)



                                intent = Intent(applicationContext, StartUpPage::class.java)
//                                intent.putExtra(KEY, phoneNumber)
                                startActivity(intent)
                            })
                        builder1.show()


                    }
                }
            }
            else{
                Toast.makeText(applicationContext,"Please enter PhoneNumber",Toast.LENGTH_SHORT).show()
                }
        }


        Already.setOnClickListener(){
            intent= Intent(applicationContext,Login::class.java)
            startActivity(intent)
        }
//

    }
}