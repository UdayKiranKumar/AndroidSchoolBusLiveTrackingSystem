package com.example.capstoneproject;


import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import com.example.capstoneproject.Adapter.SOSAdapter;
import com.example.capstoneproject.Models.SOS;


public class SOSFragment extends Fragment {

    View view;
    SOSAdapter sosAdapter;
    ArrayList<SOS> sosList;

    Button btnBack;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    FloatingActionButton fab_menu;
    String driverId;
    public SOSFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_sos, container, false);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        driverId = user.getUid();
        btnBack = (Button) view.findViewById(R.id.btnBack);
        fab_menu = (FloatingActionButton) getActivity().findViewById(R.id.fab_menu);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeFragment();
                fab_menu.setVisibility(View.VISIBLE);
            }
        });
        getSosList();
        return view;
    }

    private void getSosList(){
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference().child("ParentDriver").child(user.getUid());
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                try {
                    sosList = new ArrayList<SOS>();
                    mFirebaseDatabase = FirebaseDatabase.getInstance();
                    myRef = mFirebaseDatabase.getReference().child("SOS").child(snapshot.getValue().toString());
                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            sosList.clear();
                            ArrayList<StorageReference> storageReferences = new ArrayList<StorageReference>();
                            for (DataSnapshot ds : snapshot.getChildren()) {
                                FirebaseStorage storage = FirebaseStorage.getInstance();
                                SOS destination = new SOS();
                                destination.setDate(ds.child("date").getValue().toString());
                                destination.setMessage(ds.child("message").getValue().toString());

                                sosList.add(destination);
                            }

                            sosAdapter = new SOSAdapter(sosList);
                            sosAdapter.setContext(getContext());
                            sosAdapter.notifyDataSetChanged();


                            sosAdapter.setStorageReferences(storageReferences);

                            RecyclerView rv = (RecyclerView) view.findViewById(R.id.lvSOS);

                            rv.setAdapter(sosAdapter);
                            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                            llm.setOrientation(LinearLayoutManager.VERTICAL);
                            rv.setLayoutManager(llm);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
                catch (NullPointerException e){}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

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
