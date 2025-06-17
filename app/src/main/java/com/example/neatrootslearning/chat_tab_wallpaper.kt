package com.example.neatrootslearning

import AppLifecycleObserver
import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.storage

class chat_tab_wallpaper : AppCompatActivity() {
    lateinit var database: DatabaseReference
    lateinit var myref: FirebaseDatabase
    lateinit var Phone: String
    lateinit var Name:String
    lateinit var About:String
    lateinit var Profile:String
    private lateinit var phoneNumber: String
    private val MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1
    private lateinit var lifecycleObserver: AppLifecycleObserver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_tab_wallpaper)
        var add_wallpaper = findViewById<ImageButton>(R.id.add_from_device2)
        var love_wallpaper = findViewById<ImageButton>(R.id.love2)
        var butterfly_wallpaper=findViewById<ImageButton>(R.id.butterfly2)
        var intent = getIntent()
        if (intent != null) {

            Phone = intent.getStringExtra("Phone").toString()
            Name= intent.getStringExtra("Name").toString()
            Profile = intent.getStringExtra("Photo").toString()
            About = intent.getStringExtra("About").toString()

        }
        var sharedPref = getSharedPreferences("UserDetails", Context.MODE_PRIVATE)
        phoneNumber = sharedPref.getString("PhoneNumber", null).toString()

        love_wallpaper.setOnClickListener {
            val progressDialog = ProgressDialog(this)
            progressDialog.setMessage("Please wait...")
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
            val resId = R.drawable.love_wallpaper
            val uri = Uri.parse("android.resource://" + packageName + "/" + resId)
            var ref = Firebase.storage.reference.child("users").child(phoneNumber.toString()).child("updated_wallpaper_chat_tab").putFile(uri).addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
//                        Toast.makeText(this, uri.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            database=FirebaseDatabase.getInstance().getReference("users")
            database.child(phoneNumber.toString()).child("updated_wallpaper_chat_tab").setValue(uri.toString())
            intent= Intent(this,StartUpPage::class.java)
            intent.putExtra("Name",Name)
            intent.putExtra("Phone",Phone)
            intent.putExtra("About",About)
            intent.putExtra("Photo",Profile)
            intent.putExtra("imageUri",uri.toString())
            intent.putExtra("isuploaded3", "yes")
            progressDialog.dismiss()
            startActivity(intent)
            finish()
        }

        butterfly_wallpaper.setOnClickListener {
            val progressDialog = ProgressDialog(this)
            progressDialog.setMessage("Please wait...")
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
            val resId = R.drawable.butterfly_wallpaper
            val uri = Uri.parse("android.resource://" + packageName + "/" + resId)
            var ref = Firebase.storage.reference.child("users").child(phoneNumber.toString()).child("updated_wallpaper_chat_tab").putFile(uri).addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
//                        Toast.makeText(this, uri.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            database=FirebaseDatabase.getInstance().getReference("users")
            database.child(phoneNumber.toString()).child("updated_wallpaper_chat_tab").setValue(uri.toString())
            intent= Intent(this,StartUpPage::class.java)
            intent.putExtra("Name",Name)
            intent.putExtra("Phone",Phone)
            intent.putExtra("About",About)
            intent.putExtra("Photo",Profile)
            intent.putExtra("imageUri",uri.toString())
            intent.putExtra("isuploaded3", "yes")
            progressDialog.dismiss()
            startActivity(intent)
            finish()
        }

        add_wallpaper.setOnClickListener() {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE
                )
            }
            intent = Intent()
            intent.action = Intent.ACTION_PICK
            intent.type = "image/*"
            imagelauncher.launch(intent)

        } }
    var imagelauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            if (it.data != null) {
                val progressDialog = ProgressDialog(this)
                progressDialog.setMessage("Please wait...")
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

                var sharedPref = getSharedPreferences("UserDetails", Context.MODE_PRIVATE)
                val phone5 = sharedPref.getString("PhoneNumber", null)

                var ref = Firebase.storage.reference.child("users").child(phone5.toString()).child("updated_wallpaper_chat_tab").putFile(it.data!!.data!!).addOnSuccessListener { taskSnapshot ->
                    taskSnapshot.storage.downloadUrl.addOnSuccessListener { uri ->
//                        Toast.makeText(this,uri.toString(),Toast.LENGTH_SHORT).show()







                        database=FirebaseDatabase.getInstance().getReference("users")
                        database.child(phone5.toString()).child("updated_wallpaper_chat_tab").setValue(uri.toString())
//                        Toast.makeText(this,Phone.toString(),Toast.LENGTH_SHORT).show()
                        progressDialog.dismiss()


                        Toast.makeText(this, "Wallpaper uploaded succesfully", Toast.LENGTH_SHORT).show()
                        var intent= Intent(this@chat_tab_wallpaper,MainActivity::class.java)
                        intent.putExtra("Name",Name)
                        intent.putExtra("Phone",Phone)
                        intent.putExtra("About",About)
                        intent.putExtra("Photo",Profile)
                        intent.putExtra("imageUri",uri.toString())
                        intent.putExtra("isuploaded3", "yes")
                        startActivity(intent)
                        finish()


                    }
                }
            }
            else{
                Toast.makeText(this,"no", Toast.LENGTH_SHORT).show()
            }
        }else{
            Toast.makeText(this,"no", Toast.LENGTH_SHORT).show()
        }
        lifecycleObserver = AppLifecycleObserver(phoneNumber!!)
        lifecycle.addObserver(lifecycleObserver)
    }
}