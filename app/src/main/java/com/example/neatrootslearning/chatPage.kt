package com.example.neatrootslearning

import AppLifecycleObserver
import MessageAdapter
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupMenu
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
import com.google.firebase.database.ValueEventListener
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
    private lateinit var activityContext: Context
    private lateinit var lifecycleObserver: AppLifecycleObserver



    lateinit var messageAdapter: MessageAdapter
    lateinit var messages: MutableList<Message>

    data class Message(var senderimage:String="" ,var recieverImageProf: String = "", val text: String = "", var isSentByUser:Boolean=false,var currentUserId:String="",var phonenum:String="",var isSystemMessage:Boolean=false,var timestamp: Long = System.currentTimeMillis())



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
        var phoneNumber=sharedPref.getString("PhoneNumber",null)
        val roomId = if (phoneNumber!! < Phone) phoneNumber +"_"+ Phone else Phone +"_"+ phoneNumber
        var is_chatted = sharedPref.getBoolean("Is Chatted with ${Phone}",false)
        if(is_chatted==false){
//            sendSystemMessage("New Conversation","New Conversation")
        }
        with(sharedPref.edit()) {
            putString("Phone_of_recieve", Phone)
            putBoolean("Is Chatted with ${Phone}",true)
            apply()

        }
        activityContext=this

        var name = findViewById<TextView>(R.id.header_name)
        var profile_pic = findViewById<CircleImageView>(R.id.header_profile)
        var about = findViewById<TextView>(R.id.About)
        val textInputLayout = findViewById<TextInputLayout>(R.id.input_message)
        val textInputEditText = textInputLayout.editText
        var Music = findViewById<ImageButton>(R.id.Musicbtn)
        val sendButton = findViewById<ImageButton>(R.id.sendbtn)
        var menubtn=findViewById<ImageButton>(R.id.menubtn2)
        var wallpaper=findViewById<ImageView>(R.id.Chats_page_wallpaper2)
        var song_name=findViewById<TextView>(R.id.Song)
        var online=findViewById<TextView>(R.id.header_Online)
//        var callbtn=findViewById<ImageButton>(R.id.Callbtn)
//        callbtn.setOnClickListener(){
//            intent=Intent(this,CallActivity::class.java)
//        startActivity(intent)}
        var count=0
//        callbtn.setOnClickListener(){
//            intent=Intent(this@chatPage,call_page_activity::class.java)
//            startActivity(intent)
//        }
        val userStatusRef = FirebaseDatabase.getInstance().reference.child("users").child(Phone).child("status")
        userStatusRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val status = snapshot.getValue(String::class.java)
                if (status == "online") {
                    online.setText("Online")
                } else {
                    online.setText("Offline")
                    // Update UI to show user is offline
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })




//        var scrollToBottomButton =findViewById<ImageButton>(R.id.btn_scrolldown)
//        scrollToBottomButton.visibility = View.GONE
        textInputEditText?.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                textInputLayout.hint = ""
            } else {
                textInputLayout.hint = "Message"
            }
        }
        var database = FirebaseDatabase.getInstance()
        sharedPref = getSharedPreferences("UserDetails", Context.MODE_PRIVATE)
        about.setText(about2)


        val songIdRef: DatabaseReference =
            database.getReference("Songs/${roomId}/Song Id")





        songIdRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                var isplaying1 = database.getReference("Songs/$roomId").get().addOnSuccessListener { data ->
                    if(data.exists()){
//                    val isplaying = data.child("isPlaying").value
                        song_name.text = data.child("Song Name").value.toString()
                        if (data.child("played_by").value != phoneNumber) {




                            val editor = sharedPref.edit()
                            editor.putBoolean("isplay", false)
                            editor.apply()

                            val songId = snapshot.value
//                            messages = mutableListOf()

                                if(song_name.text.toString()!="null"){


                                    var builder1 = AlertDialog.Builder(this@chatPage)
                                    builder1.setTitle("Verification page")
                                    builder1.setMessage(
                                        "$Name has played a ${song_name.text} Previously Do you want to continue Listening"
                                    )
                                    builder1.setPositiveButton(

                                        "Yes",

                                        DialogInterface.OnClickListener { dialog, which ->

                                            val trackUrl = "https://open.spotify.com/track/$songId"
                                            val uri = Uri.parse(trackUrl)
                                            val intent2 = Intent(Intent.ACTION_VIEW, uri)
                                            startActivity(intent2)
                                            Handler(Looper.getMainLooper()).postDelayed({
                                                val returnIntent =
                                                    Intent(this@chatPage, chatPage::class.java)
                                                returnIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                                                startActivity(returnIntent)
                                            }, 5000)
                                            sendSystemMessage("You Just played ${song_name.text.toString()}","${song_name.text}","yes")
//                                    sendSystemMessage("You Just played ${song_name.text.toString()}","${song_name.text}")


                                        })
                                    builder1.setNegativeButton(
                                        "No",
                                        DialogInterface.OnClickListener { dialog, which ->



                                        })
                                    builder1.show()



                                }
                            else{
//                            sendSystemMessage("$Name Just played ${song_name.text.toString()}","${song_name.text}")



                                val trackUrl = "https://open.spotify.com/track/$songId"
                                val uri = Uri.parse(trackUrl)
                                val intent2 = Intent(Intent.ACTION_VIEW, uri)
                                startActivity(intent2)
                                Handler(Looper.getMainLooper()).postDelayed({
                                    val returnIntent = Intent(this@chatPage, chatPage::class.java)
                                    returnIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                                    startActivity(returnIntent)
                                }, 5000)
                                Toast.makeText(this@chatPage, "Song Playing", Toast.LENGTH_SHORT).show()
                            }



                        }

                        else{
                            sendSystemMessage("You Just played ${song_name.text.toString()}","${song_name.text}","no")
                            Toast.makeText(this@chatPage, "Song Playing23", Toast.LENGTH_SHORT).show()

                        }
                    }
                }}

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
            }
        })



        var isuploaded=intent.getStringExtra("isuploaded2")
        if(isuploaded=="yes") {


            val imageUri = Uri.parse(intent.getStringExtra("imageUri"))
// Use the imageUri to load the image

// Use the imageUri to load the image
//                Toast.makeText(this@chatPage, imageUri.toString(), Toast.LENGTH_SHORT).show()

            if(imageUri!=null){
                Picasso.get().load(imageUri).into(wallpaper)
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
//            sendSystemMessage("hey")

            intent = Intent(this,musicpage_spotify::class.java)

            startActivity(intent)
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

                    message = Message(senderimage = uriimg, recieverImageProf =  Profile, text =  messageText, isSentByUser = true,currentUserId=phoneNumber.toString(), timestamp = System.currentTimeMillis())
                    var data=database2.child(phoneNumber.toString()).child("Added Contacts").child(Phone).child("timestamp").setValue(System.currentTimeMillis())
                    // Push the chat message to the database
                    var messageId=myRef.push().setValue(message)


//                    var LastMessage=FirebaseDatabase.getInstance().getReference("Last Messages").child(roomId)
//                    LastMessage.child(messageId.toString()).setValue(message)
//                    LastMessage.child("Last Message text").setValue(messageText)
//                    Toast.makeText(this@chatPage,messageId.toString(),Toast.LENGTH_SHORT).show()


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
                    Log.e("Chatpage", "hello Hi${count++}")


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
        }
        messageAdapter.setItemOnLongClickListener { position->
            // Handle the long-click event
            val message = messages[position]
            // Get the view of the item at the position
            val v = recyclerView.layoutManager?.findViewByPosition(position)

            // Create a PopupMenu object
            val popup = PopupMenu(activityContext, v)

            // Inflate your menu resource into the PopupMenu
            popup.menuInflater.inflate(R.menu.menu_for_message, popup.menu)

            // Set a click listener for menu item click
            popup.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.menu_button -> {
                        Toast.makeText(this@chatPage,"Unable to Forward Message",Toast.LENGTH_SHORT).show()

                        // Handle option 1 click
                        true
                    }
                    R.id.menu_button2 -> {
                        // Handle option 2 click
                        myRef.child(phoneNumber.toString()).child("Added Contacts").child(Phone).child(messages[position].text).removeValue()
                        Toast.makeText(this@chatPage,"Contact Deleted Successfully",Toast.LENGTH_SHORT).show()
                        true
                    }
                    R.id.menu_button3 -> {
                        Toast.makeText(this@chatPage,"Message Has Been Added to Contacts",Toast.LENGTH_SHORT).show()


//                         }
                    }
                    R.id.menu_button4 -> {
                        Toast.makeText(this@chatPage,"Copied To Clipboard",Toast.LENGTH_SHORT).show()


//                         }
                    }
                }
//                            database.child(phoneNumber.toString()).child("Added Contacts").child(arraylist2[position].phone).setValue("none")


                true
            }




//                 Finally show the PopupMenu
            popup.show()

            true
            // Return true to indicate the event was handled
        }
        // Replace with the actual phone number
        lifecycleObserver = AppLifecycleObserver(phoneNumber!!)
        lifecycle.addObserver(lifecycleObserver)
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
    private fun sendSystemMessage(text: String,text1:String,isyes:String) {
        val systemMessage = Message(
            text = text,
            isSystemMessage = true
        )
        var count=0



        var sharedPref = getSharedPreferences("UserDetails", Context.MODE_PRIVATE)
//        about.setText(about2)
        var phoneNumber=sharedPref.getString("PhoneNumber",null)

        val roomId = if (phoneNumber!! < Phone) phoneNumber +"_"+ Phone else Phone +"_"+ phoneNumber

        var lastmsg=FirebaseDatabase.getInstance().getReference("Last Messages/${roomId}").get().addOnSuccessListener {

            if(it.exists()) {
                Toast.makeText(this@chatPage,"text1",Toast.LENGTH_SHORT).show()
                var Lastmsg = it.child("Last Message text").value.toString()
//                Toast.makeText(this@chatPage,Lastmsg,Toast.LENGTH_SHORT).show()

                if (Lastmsg != text1) {

                    Toast.makeText(this@chatPage,text1,Toast.LENGTH_SHORT).show()
//                    Toast.makeText(this@chatPage,Lastmsg,Toast.LENGTH_SHORT).show()

                    var LastMessage =
                        FirebaseDatabase.getInstance().getReference("Last Messages").child(roomId)

//        myRef.child("Last Message").child("Last Message Id").setValue(messageId)
                    LastMessage.child("Last Message text").setValue(text1)
                    myRef.push().setValue(systemMessage)


                }
                else if(isyes=="yes"){
                    Toast.makeText(this@chatPage,text1,Toast.LENGTH_SHORT).show()
//                    Toast.makeText(this@chatPage,Lastmsg,Toast.LENGTH_SHORT).show()

                    var LastMessage =
                        FirebaseDatabase.getInstance().getReference("Last Messages").child(roomId)

//        myRef.child("Last Message").child("Last Message Id").setValue(messageId)
                    LastMessage.child("Last Message text").setValue(text1)
                    myRef.push().setValue(systemMessage)

                }
                else if(isyes=="no"){
                    Toast.makeText(this@chatPage,"hey",Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(this@chatPage,"text2",Toast.LENGTH_SHORT).show()
                var lastmsg=FirebaseDatabase.getInstance().getReference("Last Messages/${roomId}/Last Message text").setValue("New Conversation")
                myRef.push().setValue(systemMessage)
            }
        }


//        LastMessage.child("Last Message Time").setValue()

    }


}