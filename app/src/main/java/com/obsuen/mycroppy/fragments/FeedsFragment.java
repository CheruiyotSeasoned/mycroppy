package com.obsuen.mycroppy.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.obsuen.mycroppy.adapters.DiseasesAdapter;
import com.obsuen.mycroppy.R;
import com.obsuen.mycroppy.models.Model;

import java.util.ArrayList;
import java.util.List;

public class FeedsFragment extends Fragment {
    private RecyclerView recyclerView;
    DatabaseReference database;
    DiseasesAdapter adapter;
    AdView myadView;

    public FeedsFragment() {
        // Required empty public constructor
    }

    List<Model> itemList;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_feeds, container, false);
//        AdView adView = new AdView(getContext());
//        AdSize adSize = new AdSize(300, 50);
////        adView.setAdSize(AdSize.BANNER);
//        adView.setAdSize(adSize);
//        adView.setAdUnitId("ca-app-pub-4436513707280900/7559468464");
//        MobileAds.initialize(getContext(), new OnInitializationCompleteListener() {
//            @Override
//            public void onInitializationComplete(InitializationStatus initializationStatus) {
//            }
//        });
        myadView  = view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        myadView.loadAd(adRequest);

        recyclerView=view.findViewById(R.id.recyclerview);
        database = FirebaseDatabase.getInstance().getReference("Popular Diseases");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        itemList = new ArrayList<>();
        adapter = new DiseasesAdapter(itemList,getContext());

        recyclerView.setAdapter(adapter);

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                    Model model = dataSnapshot.getValue(Model.class);
                    itemList.add(model);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        return view;
    }




}

