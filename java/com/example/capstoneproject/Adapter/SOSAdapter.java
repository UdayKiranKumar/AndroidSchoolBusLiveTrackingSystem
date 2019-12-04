package com.example.capstoneproject.Adapter;


import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import com.example.capstoneproject.Models.SOS;
import com.example.capstoneproject.R;

public class SOSAdapter
        extends RecyclerView.Adapter<SOSAdapter.MyViewHolder> {
    private List<SOS> sosList;
    private Context context;
    private ArrayList<StorageReference> storageReferences;

    public void setStorageReferences(ArrayList<StorageReference> storageReferences){
        this.storageReferences = storageReferences;
    }
    public void setContext(Context context){
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvDate;
        public TextView tvMessage;

        public MyViewHolder(View view){
            super(view);
            tvDate = (TextView) view.findViewById(R.id.tvDate);
            tvMessage = (TextView) view.findViewById(R.id.tvMessage);
        }
    }

    public SOSAdapter(List<SOS> childrenList){
        this.sosList = childrenList;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position){
        SOS d = sosList.get(position);
        holder.tvDate.setText(d.getDate());
        holder.tvMessage.setText(d.getMessage());
    }

    @Override
    public int getItemCount(){
        return sosList.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sos_item, parent, false);
        return new MyViewHolder(v);
    }

}