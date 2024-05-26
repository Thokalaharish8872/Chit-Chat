package com.example.neatrootslearning

import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
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
        var inputPassword =findViewById<TextInputLayout>(R.id.inputpassword)
        val inputPhoneLayout: TextInputLayout = findViewById(R.id.inputphone)

        inputPhoneLayout.setBoxStrokeColor(ContextCompat.getColor(this, R.color.StaleBlue))
        inputPhoneLayout.hint="Phone"
        inputPhoneLayout.setHintTextColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.StaleBlue)))
        inputPassword.setBoxStrokeColor(ContextCompat.getColor(this, R.color.StaleBlue))
        inputPassword.hint="Password"
        inputPassword.setHintTextColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.StaleBlue)))
//        fun hello(){
        SignUp.setOnClickListener(){
            phone1=findViewById<TextInputEditText>(R.id.editphone)
            var name = name1.text.toString()
            var phone = phone1.text.toString()
            var password=password1.text.toString()




            var user = users(name,phone,password, about = "Hey! Nenu Chit Chat Vaadutunna")


            if(phone.isNotEmpty()) {
                if(name.isNotEmpty()){
                    if(password.isNotEmpty()){



                if(phone.length<10){
                    Toast.makeText(this,"Please enter a valid PhoneNumber",Toast.LENGTH_SHORT).show()
                    inputPhoneLayout.setBoxStrokeColor(ContextCompat.getColor(this, R.color.orange))
                    inputPhoneLayout.hint="Please enter a valid PhoneNumber"
                    inputPhoneLayout.setHintTextColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.orange)))
//                      hello()

                }else{
                    inputPhoneLayout.setBoxStrokeColor(ContextCompat.getColor(this, R.color.StaleBlue))
                    inputPhoneLayout.hint="Phone"
                    inputPhoneLayout.setHintTextColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.StaleBlue)))
                    if(password.length<8){
                        Toast.makeText(this,"Please Enter 8 Digit Password",Toast.LENGTH_SHORT).show()
                        inputPassword.setBoxStrokeColor(ContextCompat.getColor(this, R.color.orange))
                        inputPassword.hint="Please Enter 8 Digit Password"
                        inputPassword.setHintTextColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.orange)))

                    }
                    else{
                        inputPassword.setBoxStrokeColor(ContextCompat.getColor(this, R.color.StaleBlue))
                        inputPassword.hint="Password"
                        inputPassword.setHintTextColor(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.StaleBlue)))
//                    Toast.makeText(this,"Phone",Toast.LENGTH_SHORT).show()

                    val progressDialog = ProgressDialog(this)
                    progressDialog.setMessage("Loading...")
                    progressDialog.show()
                    val window = progressDialog.window

                    // Create a new layout parameters object
                    val layoutParams = WindowManager.LayoutParams()

                    // Copy the existing layout parameters
                    layoutParams.copyFrom(window?.attributes)

                    // Set the gravity to top right


                    layoutParams.x=20
                    val displayMetrics = resources.displayMetrics
                    val dialogWindowWidth = (displayMetrics.widthPixels * 0.6).toInt() // Width set to 80% of screen width
                    layoutParams.width = dialogWindowWidth
                    window?.attributes = layoutParams
                    database=FirebaseDatabase.getInstance().getReference("users")
                    database.child(phone).get().addOnSuccessListener {
//                        progressDialog.dismiss()
                    if (it.exists()) {
                        progressDialog.dismiss()
                        Toast.makeText(this, "phoneNumber already exist", Toast.LENGTH_SHORT).show()
                    } else {
                        progressDialog.dismiss()

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
                                database=FirebaseDatabase.getInstance().getReference("users")
                                database.child(phone).child("IsPhotoUploaded").setValue("Not")



                                intent = Intent(applicationContext, StartUpPage::class.java)
//                                intent.putExtra(KEY, phoneNumber)
                                startActivity(intent)
                            })
                        builder1.show()


                    }
                }}}}
                else{
                        Toast.makeText(applicationContext,"Please Enter Password",Toast.LENGTH_SHORT).show()
                }}
                else{
                    Toast.makeText(applicationContext,"Please Enter Your Name",Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(applicationContext,"Please Enter PhoneNumber",Toast.LENGTH_SHORT).show()
                }
        }


        Already.setOnClickListener(){
            intent= Intent(applicationContext,Login::class.java)
            startActivity(intent)
        }
//

    }
}