package com.example.neatrootslearning

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView

class MainActivity2 : AppCompatActivity() {
    var current = 0
    lateinit var image : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)




                var next = findViewById<ImageButton>(R.id.nextbtn)
                var prev =  findViewById<ImageButton>(R.id.prevbtn)
                var pic1 = findViewById<ImageView>(R.id.img_pic1)
                var pic2 = findViewById<ImageView>(R.id.img_pic2)

                next.setOnClickListener(){
                    var showingImage = "pic$current"
                    var showingimgInt = this.resources.getIdentifier(showingImage,"id",packageName)
                    image = findViewById(showingimgInt)
                    image.visibility = View.INVISIBLE
                    current=(2+current+1)%2

                    var showingimage = "pic$current"
                    var showingimageint = this.resources.getIdentifier(showingimage,"id",packageName)
                    image = findViewById(showingimageint)
                    image.visibility = View.VISIBLE


                }
                prev.setOnClickListener(){
                    var hidingimage = "pic$current"
                    var hidingimageint = this.resources.getIdentifier(hidingimage,"id",packageName)
                    image = findViewById(hidingimageint)
                    image.visibility = View.INVISIBLE
                    current=(2+current-1)%2


                    var showingimage = "pic$current"
                    var showingimageint = this.resources.getIdentifier(showingimage,"id",packageName)
                    image = findViewById(showingimageint)
                    image.visibility = View.VISIBLE

                }


            }
        }
