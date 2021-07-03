package com.obsuen.mycroppy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class DiseasesActivity : AppCompatActivity() {
    private  lateinit var newRecyclerView: RecyclerView
    private lateinit var  newArrayList: ArrayList<diseases>
    lateinit var imageid : Array<Int>
    lateinit var heading : Array<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diseases)
        imageid = arrayOf(
            R.drawable.stemrot,
            R.drawable.powerdy,
            R.drawable.leafcurl
        )
        heading  = arrayOf(
            "Curling tomato leaves may be a sign of a viral infection. Normally this virus is transmitted through whiteflies or through infected transplants.",
            "Tomato powerdy Caused by the fungus Oidium lycopersicum, is a relatively new disease in North America that has the potential to affect both field and greenhouse tomato production.",
        "Tomato stemrot Stem rot, is a fungal disease. It appears sporadically around the time tomatoes start to flower due to the favorable conditions that heavy tomato foliage cover creates."
        )
        newRecyclerView = findViewById(R.id.recyclerview)
        newRecyclerView.layoutManager = LinearLayoutManager(this)
        newRecyclerView.setHasFixedSize(true)


        newArrayList = arrayListOf<diseases>()
        getUserdata()
    }

    private fun getUserdata() {
        for(i in imageid.indices){
            val diseases = diseases(imageid[i],heading[i])
            newArrayList.add(diseases)
        }
        newRecyclerView.adapter = MyAdapter(newArrayList)
    }
}