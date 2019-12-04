package com.example.capstoneproject;

import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.PopupMenu;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.client.Firebase;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;


public class ViewDriverFragment extends Fragment {
    View view;

    Button btnBack;
    ImageView ivPic;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseUser user;
    private FirebaseAuth mAuth;

    TextView tvFullName;
    TextView tvEmail;
    TextView tvCapacity;
    TextView tvCurrent;
    TextView tvPhoneNumber;

    String driverId;
    String parent;
    String phoneNumber;
    FloatingActionButton fab_menu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_view_driver, container, false);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        Firebase.setAndroidContext(getActivity());
        btnBack = (Button) view.findViewById(R.id.btnBack);
        tvFullName = (TextView) view.findViewById(R.id.tvFullName);
        tvEmail = (TextView) view.findViewById(R.id.tvEmail);
        tvCapacity = (TextView) view.findViewById(R.id.tvCapacity);
        tvCurrent = (TextView) view.findViewById(R.id.tvCurrent);
        tvPhoneNumber = (TextView) view.findViewById(R.id.tvPhoneNumber);
        Bundle args = getArguments();
        driverId = args.getString("driverId", "");
        fab_menu = (FloatingActionButton) getActivity().findViewById(R.id.fab_menu);

        ivPic = (ImageView) view.findViewById(R.id.ivPic);


        parent = user.getUid();

        btnBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                removeFragment();
                getActivity().findViewById(R.id.fragment_container).setVisibility(View.GONE);
                getActivity().findViewById(R.id.llButtons).setVisibility(View.VISIBLE);
            }
        });

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference().child("Profile").child(driverId);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                tvFullName.setText(snapshot.child("fullName").getValue().toString());
                tvEmail.setText(snapshot.child("email").getValue().toString());
                tvPhoneNumber.setText(snapshot.child("phoneNumber").getValue().toString());
                try {
                    tvCapacity.setText(snapshot.child("capacity").getValue().toString());
                    tvCurrent.setText(snapshot.child("current").getValue().toString());
                }
                catch (NullPointerException e){
                    tvCapacity.setText("0");
                    tvCurrent.setText("0");
                }
                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageRef = storage.getReference().child("Profile").child(driverId);

                Glide.with(getContext())
                        .using(new FirebaseImageLoader())
                        .load(storageRef)
                        .into(ivPic);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return view;
    }



    public void removeFragment(){
        List<Fragment> fragments = getActivity().getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (fragment != null)
                getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
        }
    }
}
