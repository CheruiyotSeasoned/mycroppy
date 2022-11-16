package com.obsuen.mycroppy.adapters;


import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import com.bumptech.glide.Glide;
import com.obsuen.mycroppy.R;
import com.obsuen.mycroppy.helpers.Functions;
import com.obsuen.mycroppy.models.UserModel;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    List<UserModel> datalist;
    private Context context;

    public UserAdapter(List<UserModel> datalist,Context context) {

        this.datalist=datalist;
        this.context=context;

    }

    @NonNull
    @Override
    public UserAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_disease,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.ViewHolder holder, final int position) {
        UserModel item = datalist.get(position);
        Glide.with(context).load(item.getImage_url()).dontAnimate().into(holder.itemImage);
        holder.name.setText(item.getPredictedname());
        holder.timestamp.setText(Functions.getDate(Long.parseLong(item.getTimestamp())));
        holder.accuracy.setText(Functions.decimalToPercent(item.getAccuracy()));
        Glide.with(context).load(item.getImage_url()).dontAnimate().into(holder.itemImage);


    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView itemImage;
        TextView name,accuracy,timestamp;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            itemImage=itemView.findViewById(R.id.title_image);
            name=itemView.findViewById(R.id.tvheading);
            accuracy=itemView.findViewById(R.id.tv_accuracy);
            timestamp=itemView.findViewById(R.id.tv_timestamp);

        }
    }
}