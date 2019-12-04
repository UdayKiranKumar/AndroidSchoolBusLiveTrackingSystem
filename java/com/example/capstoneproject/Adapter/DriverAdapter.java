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

import com.example.capstoneproject.Models.Profile;
import com.example.capstoneproject.R;

public class DriverAdapter
        extends RecyclerView.Adapter<DriverAdapter.MyViewHolder> {
    private List<Profile> driverList;
    private Context context;
    private ArrayList<StorageReference> storageReferences;

    public void setStorageReferences(ArrayList<StorageReference> storageReferences){
        this.storageReferences = storageReferences;
    }
    public void setContext(Context context){
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvFullName;
        public TextView tvEmail;

        public TextView tvCapacity;
        public TextView tvCurrent;

        public TextView tvPhoneNumber;
        public TextView tvArea;

        public TextView tvId;

        public MyViewHolder(View view){
            super(view);

            tvFullName = (TextView) view.findViewById(R.id.tvFullName);
            tvEmail = (TextView) view.findViewById(R.id.tvEmail);
            tvPhoneNumber = (TextView) view.findViewById(R.id.tvPhoneNumber);
            tvId = (TextView) view.findViewById(R.id.tvId);
            tvCapacity = (TextView) view.findViewById(R.id.tvCapacity);

            tvCurrent = (TextView) view.findViewById(R.id.tvCurrent);
            tvArea = (TextView) view.findViewById(R.id.tvArea);
        }
    }

    public DriverAdapter(List<Profile> driverList){
        this.driverList = driverList;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position){
        Profile d = driverList.get(position);
        holder.tvFullName.setText(d.getFullName());
        holder.tvEmail.setText(d.getEmail());
        holder.tvCapacity.setText(d.getCapacity());
        holder.tvCurrent.setText(d.getCurrent());
        holder.tvPhoneNumber.setText(d.getPhoneNumber());
        holder.tvArea.setText(d.getArea());
    }

    @Override
    public int getItemCount(){
        return driverList.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.profile_item, parent, false);
        return new MyViewHolder(v);
    }

}