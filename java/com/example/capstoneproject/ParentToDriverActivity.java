package com.example.capstoneproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
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

import com.example.capstoneproject.Adapter.ParentAdapter;
import com.example.capstoneproject.Models.Child;
import com.example.capstoneproject.Models.Profile;

public class ParentToDriverActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {
    Button btnBack;

    ParentAdapter parentAdapter;
    ArrayList<Profile> parents;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    private FirebaseUser user;
    private FirebaseAuth mAuth;
    PopupMenu popup;

    String parentId;
    String count;
    String area;

    FloatingActionButton fab_menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parent_to_driver);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        fab_menu = (FloatingActionButton) findViewById(R.id.fab_menu);

        Firebase.setAndroidContext(ParentToDriverActivity.this);

        btnBack = (Button) findViewById(R.id.btnBack);


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ParentToDriverActivity.this, MainActivity.class);
                ParentToDriverActivity.this.startActivity(intent);
            }
        });

        getChildrenList();
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
                        driver.setPhoneNumber(ds.child("phoneNumber").getValue().toString());
                        driver.setType(ds.child("type").getValue().toString());
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
                parentAdapter.setContext(ParentToDriverActivity.this);
                parentAdapter.notifyDataSetChanged();

                parentAdapter.setStorageReferences(storageReferences);

                RecyclerView rv = (RecyclerView) findViewById(R.id.lvParent);

                rv.setAdapter(parentAdapter);
                LinearLayoutManager llm = new LinearLayoutManager(ParentToDriverActivity.this);
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                rv.setLayoutManager(llm);

                rv.addOnItemTouchListener(new RecyclerItemListener(ParentToDriverActivity.this, rv,
                        new RecyclerItemListener.RecyclerTouchListener() {
                            public void onClickItem(View v, int position) {
                                Profile parent = new Profile();
                                parent = parents.get(position);

                                if (Integer.parseInt(parent.getNumber_of_child()) <= 0){
                                    Toast toast=Toast.makeText(ParentToDriverActivity.this, "Please set number of child for this parent.", Toast.LENGTH_LONG);
                                    View view=toast.getView();
                                    view.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_IN);
                                    TextView text =view.findViewById(android.R.id.message);
                                    text.setTextColor(Color.BLUE);
                                    toast.show();
                                }
                                else{
                                    parentId = parent.getId();
                                    count = parent.getNumber_of_child();
                                    area = parent.getArea();
                                    popup = new PopupMenu(ParentToDriverActivity.this, v);
                                    MenuInflater inflater2 = popup.getMenuInflater();
                                    inflater2.inflate(R.menu.driver_list_menu, popup.getMenu());

                                    popup.setOnMenuItemClickListener(ParentToDriverActivity.this);
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

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Firebase ref = new Firebase(Config.FIREBASE_URL);
        Child destination = new Child();
        switch (item.getItemId()) {
            case R.id.mnuChildren:
                Bundle bundle1 = new Bundle();

                bundle1.putString("parentId", parentId );

                FragmentManager fragmentManager1 = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction1 = fragmentManager1.beginTransaction();
                Children2Fragment children2Fragment = new Children2Fragment();
                children2Fragment.setArguments(bundle1);
                fragmentTransaction1.replace(R.id.fragment_container, children2Fragment);
                fragmentTransaction1.commit();

                findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
                findViewById(R.id.llButtons).setVisibility(View.GONE);
                return true;
            case R.id.mnuDriver:
                Bundle bundle = new Bundle();

                bundle.putString("parentId", parentId );
                bundle.putString("count",count);
                bundle.putString("area", area);

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                DriverList2Fragment driverListFragment = new DriverList2Fragment();
                driverListFragment.setArguments(bundle);
                fragmentTransaction.replace(R.id.fragment_container, driverListFragment);
                fragmentTransaction.commit();

                findViewById(R.id.fragment_container).setVisibility(View.VISIBLE);
                findViewById(R.id.llButtons).setVisibility(View.GONE);
                return true;

            case R.id.mnuUnAssign:
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:

                                mFirebaseDatabase = FirebaseDatabase.getInstance();
                                myRef = mFirebaseDatabase.getReference().child("Children");
                                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot snapshot) {

                                        for (DataSnapshot ds : snapshot.getChildren()) {
                                            try {
                                                if (ds.child("parent").getValue().toString().equals(parentId)) {
                                                    Child child = new Child();
                                                    child.setAge(ds.child("age").getValue().toString());
                                                    child.setGender(ds.child("gender").getValue().toString());
                                                    child.setName(ds.child("name").getValue().toString());
                                                    child.setParent(ds.child("parent").getValue().toString());
                                                    child.setStatus("Home");
                                                    child.setTimeIn(ds.child("timeIn").getValue().toString());
                                                    child.setTimeOut(ds.child("timeOut").getValue().toString());
                                                    Firebase ref = new Firebase(Config.FIREBASE_URL);
                                                    String driver = ds.child("driver").getValue().toString();
                                                    ref.child("Children").child(child.getName()).setValue(child);

                                                    mFirebaseDatabase = FirebaseDatabase.getInstance();
                                                    myRef = mFirebaseDatabase.getReference().child("Profile").child(driver);
                                                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot snapshot) {

                                                            try {
                                                                Firebase ref = new Firebase(Config.FIREBASE_URL);
                                                                ref.child("Profile").child(snapshot.child("id").getValue().toString()).child("current").setValue(String.valueOf(Integer.parseInt(snapshot.child("current").getValue().toString()) - Integer.parseInt(count)));
                                                            }
                                                            catch (Exception e) {}

                                                        }
                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {
                                                        }
                                                    });

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

                                                    mFirebaseDatabase = FirebaseDatabase.getInstance();
                                                    myRef = mFirebaseDatabase.getReference().child("ParentDriver").child(parentId);
                                                    myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot snapshot) {

                                                            try {
                                                                Firebase ref = new Firebase(Config.FIREBASE_URL);
                                                                ref.child("ParentDriver").child(parentId).setValue(null);
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
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(ParentToDriverActivity.this);
                builder.setMessage("Are you sure you want to remove assigned driver?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();



                return true;
            default:
                return false;
        }
    }



}
