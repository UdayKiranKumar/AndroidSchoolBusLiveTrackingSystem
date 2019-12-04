package com.example.capstoneproject;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
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

import com.example.capstoneproject.Adapter.ParentAdapter;
import com.example.capstoneproject.Models.Child;
import com.example.capstoneproject.Models.Profile;


public class ParentListFragment extends Fragment implements PopupMenu.OnMenuItemClickListener {
    View view;
    Button btnBack;

    ParentAdapter parentAdapter;
    ArrayList<Profile> parents;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    private FirebaseUser user;
    private FirebaseAuth mAuth;
    PopupMenu popup;

    String driverId;

    FloatingActionButton fab_menu;

    public ParentListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_driver_list, container, false);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        fab_menu = (FloatingActionButton) getActivity().findViewById(R.id.fab_menu);

        Button btnUnauthorized;
        btnUnauthorized = (Button) view.findViewById(R.id.btnUnauthorized);

        btnUnauthorized.setVisibility(View.GONE);

        Firebase.setAndroidContext(getActivity());

        btnBack = (Button) view.findViewById(R.id.btnBack);


        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference().child("ParentDriver").child(user.getUid());
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                try {
                    driverId = snapshot.getValue().toString();
                }
                catch(NullPointerException e){
                    driverId = "";
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           removeFragment();
                                           fab_menu.setVisibility(View.VISIBLE);
                                       }
                                   });

        getChildrenList();
        return view;
    }

    private void getChildrenList(){
        parents = new ArrayList<Profile>();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference().child("Profile");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                parents.clear();
                ArrayList<StorageReference> storageReferences = new ArrayList<StorageReference>();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    Profile driver = new Profile();
                    if (ds.child("type").getValue().toString().equals("2")) {
                        driver.setFullName(ds.child("fullName").getValue().toString());
                        driver.setEmail(ds.child("email").getValue().toString());
                        driver.setId(ds.getKey().toString());
                        driver.setPassword(ds.child("email").getValue().toString());
                        driver.setType(ds.child("type").getValue().toString());
                        driver.setPhoneNumber(ds.child("phoneNumber").getValue().toString());
                        driver.setArea(ds.child("area").getValue().toString());

                        try{
                            driver.setAssignee(ds.child("assignee").getValue().toString());
                        }
                        catch (NullPointerException e){
                            driver.setAssignee("Not assigned.");
                        }

                        try {
                            driver.setNumber_of_child(ds.child("number_of_child").getValue().toString());
                        } catch (NullPointerException e) {
                            driver.setNumber_of_child("0");
                        }
                        parents.add(driver);
                    }


                }

                parentAdapter = new ParentAdapter(parents);
                parentAdapter.setContext(getContext());
                parentAdapter.notifyDataSetChanged();

                parentAdapter.setStorageReferences(storageReferences);

                RecyclerView rv = (RecyclerView) view.findViewById(R.id.lvDriver);

                rv.setAdapter(parentAdapter);
                LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                rv.setLayoutManager(llm);

                rv.addOnItemTouchListener(new RecyclerItemListener(getContext(), rv,
                        new RecyclerItemListener.RecyclerTouchListener() {
                            public void onClickItem(View v, int position) {
                                final int samplePosition = position;
                                if (driverId.isEmpty()){
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                    builder.setTitle("Input Number Children");
                                   View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.input_box_layout, (ViewGroup) getView(), false);
                                    final EditText input = (EditText) viewInflated.findViewById(R.id.input);
                                    builder.setView(viewInflated);

                                    builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Profile profile = new Profile();
                                            profile = parents.get(samplePosition);
                                            profile.setNumber_of_child(input.getText().toString());
                                            addChild(profile);
                                            dialog.dismiss();
                                        }
                                    });
                                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    });

                                    builder.show();
                                }
                                else {
                                    Toast toast=Toast.makeText(getActivity(), "You cannot edit this account because it is already set to a driver and might cause a discrepancy.", Toast.LENGTH_LONG);
                                    View view=toast.getView();
                                    view.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_IN);
                                    TextView text =view.findViewById(android.R.id.message);
                                    text.setTextColor(Color.RED);
                                    toast.show();
                                }
                            }

                            public void onLongClickItem(View v, int position) {

                            }
                        }));
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void addChild(Profile profile){
                Firebase ref = new Firebase(Config.FIREBASE_URL);
                profile.setCurrent("0");
                profile.setCapacity("0");
                ref.child("Profile").child(profile.getId()).setValue(profile);
               Toast toast= Toast.makeText(getActivity(), "Edit Children Number successful.", Toast.LENGTH_LONG);
        View view=toast.getView();
        view.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_IN);
        TextView text =view.findViewById(android.R.id.message);
        text.setTextColor(Color.BLUE);
        toast.show();
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


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Firebase ref = new Firebase(Config.FIREBASE_URL);
        Child destination = new Child();
        switch (item.getItemId()) {
            case R.id.mnuFollowChild:

                return true;
            default:
                return false;
        }
    }


}
