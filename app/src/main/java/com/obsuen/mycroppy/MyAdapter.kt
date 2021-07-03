package com.obsuen.mycroppy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView

class MyAdapter(private  val diseaselist : ArrayList<diseases>):
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_disease,parent,false)
        return  MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = diseaselist[position]
        holder.dimage.setImageResource(currentItem.dimage)
        holder.tvheading.text = currentItem.heading
    }

    override fun getItemCount(): Int {
        return diseaselist.size
    }
    class MyViewHolder(itemView:View): RecyclerView.ViewHolder(itemView){
        val dimage : ShapeableImageView = itemView.findViewById(R.id.title_image)
        val tvheading : TextView = itemView.findViewById(R.id.tvheading)

    }


    }