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


public class DriverList2Fragment extends Fragment implements PopupMenu.OnMenuItemClickListener {
    View view;
    Button btnBack;

    DriverAdapter driverAdapter;
    ArrayList<Profile> drivers;

    String parentId;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    Profile profile;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    PopupMenu popup;

    String parent_id;
    String children_count;
    String driver;
    String area;

    FloatingActionButton fab_menu;

    public DriverList2Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_driver_list, container, false);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        Bundle args = getArguments();
        parent_id = args.getString("parentId", "");
        children_count = args.getString("count","0");
        area = args.getString("area", "");

        Button btnUnauthorized;
        btnUnauthorized = (Button) view.findViewById(R.id.btnUnauthorized);

        btnUnauthorized.setVisibility(View.GONE);

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference().child("ParentDriver").child(parent_id);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                try {
                    driver = snapshot.getValue().toString();
                }
                catch(NullPointerException e){
                    driver = "";
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        fab_menu = (FloatingActionButton) getActivity().findViewById(R.id.fab_menu);

        Firebase.setAndroidContext(getActivity());

        btnBack = (Button) view.findViewById(R.id.btnBack);


        btnBack.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           removeFragment();
                                           getActivity().findViewById(R.id.fragment_container).setVisibility(View.GONE);
                                           getActivity().findViewById(R.id.llButtons).setVisibility(View.VISIBLE);
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
                        driver.setStatus(ds.child("status").getValue().toString());
                        driver.setArea(ds.child("area").getValue().toString());

                        try {
                            driver.setCapacity(ds.child("capacity").getValue().toString());
                            driver.setCurrent(ds.child("current").getValue().toString());
                        } catch (NullPointerException e) {
                            driver.setCapacity("0");
                            driver.setCurrent("0");
                        }
                        if (area.equals(driver.getArea())) {
                            if (driver.getStatus().equals("1"))
                                drivers.add(driver);
                        }
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
                                final int samplePosition = position;
                                profile = new Profile();
                                profile = drivers.get(position);
                                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        switch (which){
                                            case DialogInterface.BUTTON_POSITIVE:

                                                if(driver.isEmpty()) {
                                                    if (Integer.parseInt(profile.getCapacity()) < (Integer.parseInt(children_count) + Integer.parseInt(profile.getCurrent()))) {
                                                        Toast toast=Toast.makeText(getActivity(), "Driver capacity is not enough for the count of child for this parent.", Toast.LENGTH_LONG);
                                                        View view=toast.getView();
                                                        view.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_IN);
                                                        TextView text =view.findViewById(android.R.id.message);
                                                        text.setTextColor(Color.RED);
                                                        toast.show();

                                                    } else {
                                                        int a = Integer.parseInt(children_count) + Integer.parseInt(profile.getCurrent());
                                                        profile.setCurrent(Integer.toString(a));
                                                        addChild(profile);

                                                        mFirebaseDatabase = FirebaseDatabase.getInstance();
                                                        myRef = mFirebaseDatabase.getReference().child("Children");
                                                        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot snapshot) {

                                                                for (DataSnapshot ds : snapshot.getChildren()) {
                                                                    try {
                                                                        if (ds.child("parent").getValue().toString().equals(parent_id)) {
                                                                            Child child = new Child();
                                                                            child.setAge(ds.child("age").getValue().toString());
                                                                            child.setGender(ds.child("gender").getValue().toString());
                                                                            child.setName(ds.child("name").getValue().toString());
                                                                            child.setDriver(profile.getId());
                                                                            child.setParent(ds.child("parent").getValue().toString());
                                                                            child.setStatus("Home");
                                                                            child.setTimeIn(ds.child("timeIn").getValue().toString());
                                                                            child.setTimeOut(ds.child("timeOut").getValue().toString());
                                                                            Firebase ref = new Firebase(Config.FIREBASE_URL);

                                                                            parentId = child.getParent();
                                                                            ref.child("Children").child(child.getName()).setValue(child);


                                                                        }
                                                                    }
                                                                    catch (Exception e) {}
                                                                }
                                                                String a = "";
                                                                if(a.isEmpty()){}
                                                            }
                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {
                                                            }
                                                        });
                                                    }
                                                }
                                                else{
                                                    Toast toast=Toast.makeText(getActivity(), "A driver is already assigned to this account.", Toast.LENGTH_LONG);
                                                    View view=toast.getView();
                                                    view.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_IN);
                                                    TextView text =view.findViewById(android.R.id.message);
                                                    text.setTextColor(Color.MAGENTA);
                                                    toast.show();
                                                }
                                                break;

                                            case DialogInterface.BUTTON_NEGATIVE:
                                                //No button clicked
                                                break;
                                        }
                                    }
                                };

                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                                        .setNegativeButton("No", dialogClickListener).show();

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
        ref.child("Profile").child(profile.getId()).setValue(profile);
        ref.child("ParentDriver").child(parent_id).setValue(profile.getId());
        Toast toast=Toast.makeText(getActivity(), "Parent is now set to this driver", Toast.LENGTH_LONG);
        View view=toast.getView();
        view.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_IN);
        TextView text =view.findViewById(android.R.id.message);
        text.setTextColor(Color.BLUE);
        toast.show();

        ref = new Firebase(Config.FIREBASE_URL);
        ref.child("Profile").child((parent_id)).child("assignee").setValue(profile.getFullName());
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
