package com.example.capstoneproject;

import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.MenuInflater;
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

import com.example.capstoneproject.Adapter.DriverAdapter;
import com.example.capstoneproject.Models.Child;
import com.example.capstoneproject.Models.Profile;


public class DriverListFragment extends Fragment implements PopupMenu.OnMenuItemClickListener {
    View view;
    Button btnBack;
    int samplePosition;
    Profile profile;

    DriverAdapter driverAdapter;
    ArrayList<Profile> drivers;
String parentId;
    Button btnUnauthorized;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    private FirebaseUser user;
    private FirebaseAuth mAuth;
    PopupMenu popup;

    String driverId;

    FloatingActionButton fab_menu;

    public DriverListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_driver_list, container, false);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        fab_menu = (FloatingActionButton) getActivity().findViewById(R.id.fab_menu);

        Firebase.setAndroidContext(getActivity());

        btnBack = (Button) view.findViewById(R.id.btnBack);


        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference().child("Profile").child(user.getUid()).child("type");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Config.APP_TYPE = snapshot.getValue().toString();
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

        btnUnauthorized = (Button) view.findViewById(R.id.btnUnauthorized);

        btnUnauthorized.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), UnauthorizedDriversActivity.class);
                getContext().startActivity(intent);
            }
        });

        getChildrenList();
        return view;
    }

    private void getChildrenList(){
        drivers = new ArrayList<Profile>();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference().child("Profile");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                drivers.clear();
                ArrayList<StorageReference> storageReferences = new ArrayList<StorageReference>();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    Profile driver = new Profile();
                    if (ds.child("type").getValue().toString().equals("1")) {
                        driver.setFullName(ds.child("fullName").getValue().toString());
                        driver.setEmail(ds.child("email").getValue().toString());
                        driver.setId(ds.getKey().toString());
                        driver.setPassword(ds.child("email").getValue().toString());
                        driver.setType(ds.child("type").getValue().toString());
                        driver.setPhoneNumber(ds.child("phoneNumber").getValue().toString());
                        driver.setArea(ds.child("area").getValue().toString());
                        driver.setStatus(ds.child("status").getValue().toString());

                        try {
                            driver.setCapacity(ds.child("capacity").getValue().toString());
                            driver.setCurrent(ds.child("current").getValue().toString());
                        } catch (NullPointerException e) {
                            driver.setCapacity("0");
                            driver.setCurrent("0");
                        }
                        if (driver.getStatus().equals("1"))
                            drivers.add(driver);
                    }


                }

                driverAdapter = new DriverAdapter(drivers);
                driverAdapter.setContext(getContext());
                driverAdapter.notifyDataSetChanged();

                driverAdapter.setStorageReferences(storageReferences);

                RecyclerView rv = (RecyclerView) view.findViewById(R.id.lvDriver);

                rv.setAdapter(driverAdapter);
                LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                rv.setLayoutManager(llm);

                rv.addOnItemTouchListener(new RecyclerItemListener(getContext(), rv,
                        new RecyclerItemListener.RecyclerTouchListener() {
                            public void onClickItem(View v, int position) {
                                samplePosition = position;

                                if (Config.APP_TYPE.equals("3")){

                                    popup = new PopupMenu(getActivity(), v);
                                    MenuInflater inflater2 = popup.getMenuInflater();
                                    inflater2.inflate(R.menu.assign_driver, popup.getMenu());

                                    popup.setOnMenuItemClickListener(DriverListFragment.this);
                                    popup.show();
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
                ref.child("Profile").child(profile.getId()).setValue(profile);
                Toast toast=Toast.makeText(getActivity(), "Edit Capacity successful.", Toast.LENGTH_LONG);
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

        switch (item.getItemId()) {
            case R.id.mnuCapacity:

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Input Capacity");
                View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.input_box_layout, (ViewGroup) getView(), false);
                final EditText input = (EditText) viewInflated.findViewById(R.id.input);
                builder.setView(viewInflated);

                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Profile profile = new Profile();
                        profile = drivers.get(samplePosition);
                        profile.setCapacity(input.getText().toString());
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
                return true;
            case R.id.mnuUnauthorize:
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:

                                profile = drivers.get(samplePosition);
                                Firebase ref = new Firebase(Config.FIREBASE_URL);
                                profile.setStatus("0");
                                profile.setCapacity("0");
                                profile.setCurrent("0");
                                ref.child("Profile").child(profile.getId()).setValue(profile);

                                mFirebaseDatabase = FirebaseDatabase.getInstance();
                                myRef = mFirebaseDatabase.getReference().child("Children");
                                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot snapshot) {

                                        for (DataSnapshot ds : snapshot.getChildren()) {
                                            try {
                                                if (ds.child("driver").getValue().toString().equals(profile.getId())) {
                                                    Child child = new Child();
                                                    child.setAge(ds.child("age").getValue().toString());
                                                    child.setGender(ds.child("gender").getValue().toString());
                                                    child.setName(ds.child("name").getValue().toString());
                                                    child.setParent(ds.child("parent").getValue().toString());
                                                    child.setStatus("Home");
                                                    child.setTimeIn(ds.child("timeIn").getValue().toString());
                                                    child.setTimeOut(ds.child("timeOut").getValue().toString());
                                                    Firebase ref = new Firebase(Config.FIREBASE_URL);

                                                    ref.child("Children").child(child.getName()).setValue(child);

                                                    parentId = child.getParent();
                                                    mFirebaseDatabase = FirebaseDatabase.getInstance();
                                                    myRef = mFirebaseDatabase.getReference().child("Profile").child(parentId);
                                                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot snapshot) {

                                                            try {
                                                                Firebase ref = new Firebase(Config.FIREBASE_URL);
                                                                ref.child("Profile").child(parentId).child("assignee").setValue(null);
                                                            }
                                                            catch (Exception e) {}

                                                        }
                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {
                                                        }
                                                    });
                                                }
                                            }
                                            catch (Exception e) {}
                                        }
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });

                                mFirebaseDatabase = FirebaseDatabase.getInstance();
                                myRef = mFirebaseDatabase.getReference().child("ParentDriver");
                                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot snapshot) {

                                        for (DataSnapshot ds : snapshot.getChildren()) {
                                            try {
                                                if (ds.getValue().toString().equals(profile.getId())) {
                                                    Firebase ref = new Firebase(Config.FIREBASE_URL);
                                                    ref.child("ParentDriver").child(ds.getKey()).setValue(null);
                                                }
                                            }
                                            catch(Exception e) {}
                                        }
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });

                                Toast toast=Toast.makeText(getContext(), "Driver is now unauthorized to be assigned to a guardian.", Toast.LENGTH_LONG);
                                View view=toast.getView();
                                view.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_IN);
                                TextView text =view.findViewById(android.R.id.message);
                                text.setTextColor(Color.BLUE);
                                toast.show();

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder1 = new AlertDialog.Builder(getContext());
                builder1.setMessage("Are you sure you want to unauthorized this driver to be a school service driver?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();

                return true;
            default:
                return false;
        }
    }


}
