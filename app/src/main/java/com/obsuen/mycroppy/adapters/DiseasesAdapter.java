package com.obsuen.mycroppy.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import com.bumptech.glide.Glide;
import com.obsuen.mycroppy.R;
import com.obsuen.mycroppy.models.Model;

public class DiseasesAdapter extends RecyclerView.Adapter<DiseasesAdapter.ViewHolder> {

    List<Model> itemList1;
    private Context context;

    public DiseasesAdapter(List<Model> itemList,Context context) {

        this.itemList1=itemList;
        this.context=context;

    }

    @NonNull
    @Override
    public DiseasesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_diseases,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DiseasesAdapter.ViewHolder holder, final int position) {
        Model item = itemList1.get(position);
        Glide.with(context).load(item.getImage_url()).dontAnimate().into(holder.itemImage);
        holder.name.setText(item.getName());
        holder.symptoms.setText(item.getSymptoms());


    }

    @Override
    public int getItemCount() {
        return itemList1.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView itemImage;
        TextView name,symptoms;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemImage=itemView.findViewById(R.id.disease_ic);
            name=itemView.findViewById(R.id.name_disease);
            symptoms=itemView.findViewById(R.id.symptoms);

        }
    }
}