package com.obsuen.mycroppy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DosAndDonts : AppCompatActivity() {
    private  lateinit var newRecyclerView: RecyclerView
    private lateinit var  newArrayList: ArrayList<dos>
    lateinit var imageid : Array<Int>
    lateinit var heading : Array<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dos_and_donts)
        imageid = arrayOf(
            R.drawable.a,
            R.drawable.b)
        heading  = arrayOf(
            "The Algorithm works well with single leaf at a time, so try uploading images with one leaf",
            "The Algorithm won't predict when more leaves in one image, it is good at predicting one leaf at a time"
        )
        newRecyclerView = findViewById(R.id.mydos)
        newRecyclerView.layoutManager = LinearLayoutManager(this)
        newRecyclerView.setHasFixedSize(true)

        newArrayList = arrayListOf<dos>()
        getUserdata()

    }
    private fun getUserdata() {
        for(i in imageid.indices){
            val does = dos(imageid[i],heading[i])
            newArrayList.add(does)
        }
        newRecyclerView.adapter = DosAdapter(newArrayList)
    }
}

