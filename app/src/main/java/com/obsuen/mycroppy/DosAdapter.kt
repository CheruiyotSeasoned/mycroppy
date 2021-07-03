package com.obsuen.mycroppy

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView

class DosAdapter(private val doslist : ArrayList<dos>):
    RecyclerView.Adapter<DosAdapter.MyViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DosAdapter.MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.dosanddonts,parent,false)
        return DosAdapter.MyViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: DosAdapter.MyViewHolder, position: Int) {
        val currentItem = doslist[position]
        holder.dimage.setImageResource(currentItem.dosimage)
        holder.tvheading.text = currentItem.dosheading
    }
    override fun getItemCount(): Int {
        return doslist.size
    }
    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val dimage : ShapeableImageView = itemView.findViewById(R.id.title_Image)
        val tvheading : TextView = itemView.findViewById(R.id.tvnewheading)

    }
}
