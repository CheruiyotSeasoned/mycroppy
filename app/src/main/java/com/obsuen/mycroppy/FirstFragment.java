package com.obsuen.mycroppy;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class FirstFragment extends Fragment {
    private RecyclerView recyclerView;

    public FirstFragment() {
        // Required empty public constructor
    }

    List<Model> itemList;
    ArrayList<Integer> imageid;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_first, container, false);

        recyclerView=view.findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //initData();

        recyclerView.setAdapter(new DiseasesAdapter(initData(),getContext()));




        return view;
    }

    private List<Model> initData() {

        itemList=new ArrayList<>();
        itemList.add(new Model(R.drawable.attendance_summary,"disease 1"));
        itemList.add(new Model(R.drawable.leafcurl,"video 1"));
        itemList.add(new Model(R.drawable.powerdy,"video 1"));
        itemList.add(new Model(R.drawable.stemrot,"video 1"));
        itemList.add(new Model(R.drawable.tomato,"video 1"));
        itemList.add(new Model(R.drawable.crop,"video 1"));



        return itemList;
    }


    }

