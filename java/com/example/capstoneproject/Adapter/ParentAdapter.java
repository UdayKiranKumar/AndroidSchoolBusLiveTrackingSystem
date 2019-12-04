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

public class ParentAdapter
        extends RecyclerView.Adapter<ParentAdapter.MyViewHolder> {
    private List<Profile> parentList;
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
        public TextView tvPhoneNumber;
        public TextView tvAssignee;

        public TextView tvChild;

        public TextView tvId;
        public TextView tvArea;

        public MyViewHolder(View view){
            super(view);

            tvFullName = (TextView) view.findViewById(R.id.tvFullName);
            tvEmail = (TextView) view.findViewById(R.id.tvEmail);
            tvPhoneNumber = (TextView) view.findViewById(R.id.tvPhoneNumber);
            tvId = (TextView) view.findViewById(R.id.tvId);
            tvChild = (TextView) view.findViewById(R.id.tvChild);
            tvAssignee = (TextView) view.findViewById(R.id.tvAssignee);
            tvArea = (TextView) view.findViewById(R.id.tvArea);
        }
    }

    public ParentAdapter(List<Profile> parentList){
        this.parentList = parentList;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position){
        Profile d = parentList.get(position);
        holder.tvFullName.setText(d.getFullName());
        holder.tvEmail.setText(d.getEmail());
        holder.tvChild.setText(d.getNumber_of_child());
        holder.tvPhoneNumber.setText(d.getPhoneNumber());
        holder.tvAssignee.setText(d.getAssignee());
        holder.tvArea.setText(d.getArea());
    }

    @Override
    public int getItemCount(){
        return parentList.size();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.profile2_item, parent, false);
        return new MyViewHolder(v);
    }

}