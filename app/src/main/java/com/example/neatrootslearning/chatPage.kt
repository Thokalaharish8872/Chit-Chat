package com.example.neatrootslearning

import MessageAdapter
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView


class chatPage : AppCompatActivity() {
    lateinit var Name: String
    lateinit var Profile: String
    lateinit var Phone: String
    lateinit var database: FirebaseDatabase
    lateinit var myRef: DatabaseReference
    lateinit var database2: DatabaseReference
    lateinit var uripic: String
    lateinit var about2: String
    lateinit var message:chatPage.Message
    var wallpaper1:Int=0

    lateinit var messageAdapter: MessageAdapter
    lateinit var messages: MutableList<Message>

    data class Message(var senderimage:String="" ,var recieverImageProf: String = "", val text: String = "", var isSentByUser:Boolean=false,var currentUserId:String="",var phonenum:String="")



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_page)

        var intent = getIntent()
        if (intent != null) {
            Name = intent.getStringExtra("Name").toString()
            Profile = intent.getStringExtra("Photo").toString()

            Phone = intent.getStringExtra("Phone").toString()
            about2 = intent.getStringExtra("About").toString()


        }
        var sharedPref = getSharedPreferences("UserDetails", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("Phone_of_recieve", Phone)
            apply()

        }

        var name = findViewById<TextView>(R.id.header_name)
        var profile_pic = findViewById<CircleImageView>(R.id.header_profile)
        var about = findViewById<TextView>(R.id.About)
        val textInputLayout = findViewById<TextInputLayout>(R.id.input_message)
        val textInputEditText = textInputLayout.editText
        var Music = findViewById<ImageButton>(R.id.Musicbtn)
        val sendButton = findViewById<ImageButton>(R.id.sendbtn)
        var menubtn=findViewById<ImageButton>(R.id.menubtn2)
        var wallpaper=findViewById<ImageView>(R.id.Chats_page_wallpaper2)
        var Online_Or_Not=findViewById<TextView>(R.id.header_Online)



//        var scrollToBottomButton =findViewById<ImageButton>(R.id.btn_scrolldown)
//        scrollToBottomButton.visibility = View.GONE
        textInputEditText?.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                textInputLayout.hint = ""
            } else {
                textInputLayout.hint = "Message"
            }
        }



        sharedPref = getSharedPreferences("UserDetails", Context.MODE_PRIVATE)
        about.setText(about2)
        var phoneNumber=sharedPref.getString("PhoneNumber",null)
        val roomId = if (phoneNumber!! < Phone) phoneNumber +"_"+ Phone else Phone +"_"+ phoneNumber


        var isuploaded=intent.getStringExtra("isuploaded2")
        if(isuploaded=="yes") {


            val imageUri = Uri.parse(intent.getStringExtra("imageUri"))
// Use the imageUri to load the image

// Use the imageUri to load the image
//                Toast.makeText(this@chatPage, imageUri.toString(), Toast.LENGTH_SHORT).show()

            if(imageUri!=null){
                Picasso.get().load(imageUri).into(wallpaper)
            }}
       database2=FirebaseDatabase.getInstance().getReference("users")
        database2.child(Phone).get().addOnSuccessListener {

                        var Is_online=it.child("Is_Online").value.toString()
//                        Toast.makeText(this, Is_online, Toast.LENGTH_SHORT).show()
                        if(Is_online=="True"){
                            Online_Or_Not.text="Online"

                        }else{
                            Online_Or_Not.text="Offline"

                        }}




        var pic = Profile
        sharedPref = getSharedPreferences("UserDetails", Context.MODE_PRIVATE)
        database = FirebaseDatabase.getInstance()
        myRef = database.getReference("conversations").child(roomId)
        database2=FirebaseDatabase.getInstance().getReference("users")
        database2.child(Phone).get().addOnSuccessListener {
            var addedprof = it.child("IsPhotoUploaded").value.toString()
            if (addedprof == "Yes") {
                if (Profile.isNotEmpty()) {
                    Picasso.get().load(Profile).into(profile_pic)
                }
            }
        }



        Music.setOnClickListener() {
            intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.spotify.com"))
            var choose = Intent.createChooser(intent, "Open With")
            startActivity(choose)
        }

        name.text = Name

        messages = mutableListOf()
        messageAdapter = MessageAdapter(this@chatPage,messages)


        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.adapter = messageAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        var newPosition = messageAdapter.itemCount - 1
        recyclerView.scrollToPosition(newPosition)

        //        scrollToBottomButton.setOnClickListener {
//             newPosition = messageAdapter.itemCount - 1
//            recyclerView.scrollToPosition(newPosition)
//            scrollToBottomButton.visibility = View.GONE
//        }
        fun isUserAtBottom(): Boolean {
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val pos = layoutManager.findLastCompletelyVisibleItemPosition()
            val numItems = messageAdapter.itemCount
            return pos >= numItems - 1
        }
        if(isUserAtBottom()){


        }else{
            newPosition = messageAdapter.itemCount - 1
            recyclerView.scrollToPosition(newPosition)
        }

        sendButton.setOnClickListener {
            val messageText = textInputEditText?.text.toString()




            if (messageText.isNotEmpty()) {
//                database2 = FirebaseDatabase.getInstance().getReference("users")
//                database2.child(Phone).get().addOnSuccessListener {
//
//                    Profile=it.child("picUri").value.toString()
                sharedPref = getSharedPreferences("UserDetails", Context.MODE_PRIVATE)
                phoneNumber = sharedPref.getString("PhoneNumber", null)
                var password = sharedPref.getString("Password", null)
                database2 = FirebaseDatabase.getInstance().getReference("users")
                database2.child(phoneNumber.toString()).get().addOnSuccessListener {
                    var uriimg = it.child("photo").value.toString()
//                    Toast.makeText(this@chatPage, phoneNumber.toString(), Toast.LENGTH_SHORT).show()
//                    Toast.makeText(this,Phone,Toast.LENGTH_SHORT).show()
                    var num=Phone

                    message = Message(senderimage = uriimg, recieverImageProf =  Profile, text =  messageText, isSentByUser = true,currentUserId=phoneNumber.toString())

                    // Push the chat message to the database
                    myRef.push().setValue(message)

                    recyclerView.postDelayed({
                        newPosition = messageAdapter.itemCount - 1
                        if (!isUserAtBottom()) {
//                            scrollToBottomButton.visibility = View.VISIBLE
//                            scrollToBottomButton.setOnClickListener {
//                                newPosition = messageAdapter.itemCount - 1
//                                recyclerView.scrollToPosition(newPosition)
//                                scrollToBottomButton.visibility = View.GONE
//                            }
                        } else {
                            recyclerView.scrollToPosition(newPosition)
                        }
                    }, 100)



                    // Clear the input field
                    textInputEditText?.setText("")
//                }
                }
            }
        }

        // Listen for incoming messages
        myRef.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, prevChildKey: String?) {
                val newMessage = dataSnapshot.getValue(Message::class.java)
                var time=1
                if (newMessage != null) {

                    fun isUserAtBottom(): Boolean {
                        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                        val pos = layoutManager.findLastCompletelyVisibleItemPosition()
                        val numItems = messageAdapter.itemCount
                        return pos >= numItems - 10
                    }
                    if(isUserAtBottom()){
                        recyclerView.postDelayed({
                            newPosition = messageAdapter.itemCount - 1
                            recyclerView.scrollToPosition(newPosition)
                        }, 100)
                    }
                    message= Message(phonenum = Phone)
//                    Toast.makeText(this@chatPage,Phone,Toast.LENGTH_SHORT).show()


                    // Check if the new message was sent by the current user
                    if (newMessage.currentUserId == phoneNumber.toString()) {
                        recyclerView.postDelayed({
                            newPosition = messageAdapter.itemCount - 1
                            if (isUserAtBottom()) {
//                                scrollToBottomButton.visibility = View.VISIBLE
//                                scrollToBottomButton.setOnClickListener {
//                                    newPosition = messageAdapter.itemCount - 1
//                                    recyclerView.scrollToPosition(newPosition)
//                                    scrollToBottomButton.visibility = View.GONE
//                                }
                            } else {
                                recyclerView.scrollToPosition(newPosition)
                            }
                        }, 100)
                    }

                    messages.add(newMessage)
//                    Toast.makeText(this@chatPage,messages.size.toString(),Toast.LENGTH_SHORT).show()
                    messageAdapter.notifyItemInserted(messages.size - 1)
                }
            }


            override fun onChildChanged(dataSnapshot: DataSnapshot, prevChildKey: String?) {}

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {}

            override fun onChildMoved(dataSnapshot: DataSnapshot, prevChildKey: String?) {}

            override fun onCancelled(databaseError: DatabaseError) {}
        })

        menubtn.setOnClickListener {
            showDialog()
//            Toast.makeText(this@chatPage,messages.size.toString(),Toast.LENGTH_SHORT).show()
        }
        textInputEditText?.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                textInputLayout.hint = ""
                if(messages.size<5){

                }
            } else {
                textInputLayout.hint = "Message"
            }
        }




    }
    private fun showDialog() {
        val options = arrayOf("Media", "Edit Contact", "Wallpaper","Advanced Settings")
        val builder = AlertDialog.Builder(ContextThemeWrapper(this, R.style.CustomAlertDialog))

            .setItems(options) { dialog, which ->
                when (which) {
                    0 -> {
                        // Handle "Media" selected
                        Toast.makeText(this, "Media selected", Toast.LENGTH_SHORT).show()
                    }
                    1 -> {
                        intent=Intent(this,Edit_Contact_details::class.java)
                        intent.putExtra("Name",Name)
                        intent.putExtra("Phone",Phone)
                        intent.putExtra("About",about2)
                        intent.putExtra("Photo",Profile)
//                        Toast.makeText(this,Profile,Toast.LENGTH_SHORT).show()
                        startActivity(intent)
                        // Handle "Search" selected
                    }
                    2 -> {
                        // Handle "Wallpaper" selected
                        intent=Intent(this,chat_page_wallpaper::class.java)
                        intent.putExtra("Name",Name)
                        intent.putExtra("Phone",Phone)
                        intent.putExtra("About",about2)
                        intent.putExtra("Photo",Profile)
                        startActivity(intent)
                        finish()
                    }
                    3 -> {
                        // Handle "Advanced Settings" selected
                        intent=Intent(this,advanced_edit::class.java)
                        intent.putExtra("Name",Name)
                        intent.putExtra("Phone",Phone)
                        intent.putExtra("About",about2)
                        intent.putExtra("Photo",Profile)
                        startActivity(intent)
                        finish()
                    }
                }
                // Handle the option selected
            }
        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(R.drawable.diologue_box_radius)

        dialog.show()
        val window = dialog.window

        // Create a new layout parameters object
        val layoutParams = WindowManager.LayoutParams()

        // Copy the existing layout parameters
        layoutParams.copyFrom(window?.attributes)

        // Set the gravity to top right
        layoutParams.gravity = Gravity.TOP or Gravity.RIGHT
        layoutParams.y=150
        layoutParams.x=10
        val displayMetrics = resources.displayMetrics
        val dialogWindowWidth = (displayMetrics.widthPixels * 0.47).toInt() // Width set to 80% of screen width
        layoutParams.width = dialogWindowWidth
        window?.attributes = layoutParams


    }


}