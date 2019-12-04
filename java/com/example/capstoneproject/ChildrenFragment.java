package com.example.capstoneproject;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuInflater;
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

import com.example.capstoneproject.Adapter.ChildAdapter;
import com.example.capstoneproject.Models.Child;


public class ChildrenFragment extends Fragment implements PopupMenu.OnMenuItemClickListener {
    View view;
    Button btnBack;
    Button btnAdd;


    ChildAdapter childAdapter;
    ArrayList<Child> children;
    String age;
    String gender;

    String parentId;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    private FirebaseUser user;
    private FirebaseAuth mAuth;
    PopupMenu popup;

    String driverId;
    String childName;
    String timeIn;
    String timeOut;
    FloatingActionButton fab_menu;

    public ChildrenFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_children, container, false);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        parentId = user.getUid();
        fab_menu = (FloatingActionButton) getActivity().findViewById(R.id.fab_menu);

        Firebase.setAndroidContext(getActivity());

        btnBack = (Button) view.findViewById(R.id.btnBack);
        btnAdd = (Button) view.findViewById(R.id.btnAdd);




        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference().child("Profile").child(user.getUid()).child("type");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Config.APP_TYPE = snapshot.getValue().toString();
                if(Config.APP_TYPE.equals("1"))
                    btnAdd.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                removeFragment();
                fab_menu.setVisibility(View.VISIBLE);
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                removeFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                AddChildFragment addChildFragment = new AddChildFragment();
                fragmentTransaction.replace(R.id.fragment_container, addChildFragment);
                fragmentTransaction.commit();
            }
        });

        getChildrenList();
        return view;
    }

    private void getChildrenList(){
        children = new ArrayList<Child>();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference().child("Children");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                children.clear();
                ArrayList<StorageReference> storageReferences = new ArrayList<StorageReference>();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageRef = storage.getReference().child("Children").child(ds.child("name").getValue().toString());
                    Child destination = new Child();
                    destination.setName(ds.child("name").getValue().toString());
                    destination.setParent(ds.child("parent").getValue().toString());
                    destination.setStatus(ds.child("status").getValue().toString());
                    destination.setAge(ds.child("age").getValue().toString());
                    destination.setGender(ds.child("gender").getValue().toString());
                    destination.setTimeIn(ds.child("timeIn").getValue().toString());
                    destination.setTimeOut(ds.child("timeOut").getValue().toString());

                    try{
                        destination.setDriver(ds.child("driver").getValue().toString());
                    }
                    catch(NullPointerException e){}



                    storageReferences.add(storageRef);
                    if(Config.APP_TYPE.equals("1")) {
                        try{
                            if (!destination.getDriver().isEmpty()) {
                                if(destination.getDriver().equals(user.getUid()))
                                    children.add(destination);
                            }
                        }
                        catch(NullPointerException e){}

                    }
                    else{
                        if (!destination.getParent().isEmpty()) {
                            if(destination.getParent().equals(user.getUid()))
                                children.add(destination);
                        }
                    }
                }

                childAdapter = new ChildAdapter(children);
                childAdapter.setContext(getContext());
                childAdapter.notifyDataSetChanged();

                childAdapter.setStorageReferences(storageReferences);

                RecyclerView rv = (RecyclerView) view.findViewById(R.id.lvChildren);

                rv.setAdapter(childAdapter);
                LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                rv.setLayoutManager(llm);

                rv.addOnItemTouchListener(new RecyclerItemListener(getContext(), rv,
                        new RecyclerItemListener.RecyclerTouchListener() {
                            public void onClickItem(View v, int position) {
                                System.out.println(Config.APP_TYPE);
                                if (Config.APP_TYPE.equals("1")){
                                    popup = new PopupMenu(getActivity(), v);
                                    MenuInflater inflater2 = popup.getMenuInflater();
                                    inflater2.inflate(R.menu.children_menu, popup.getMenu());

                                    popup.setOnMenuItemClickListener(ChildrenFragment.this);
                                    popup.show();
                                    parentId = ((TextView) v.findViewById(R.id.tvParent)).getText().toString();
                                    driverId = ((TextView) v.findViewById(R.id.tvDriver)).getText().toString();
                                    childName = ((TextView) v.findViewById(R.id.tvName)).getText().toString();
                                    age = ((TextView) v.findViewById(R.id.tvAge)).getText().toString();
                                    gender = ((TextView) v.findViewById(R.id.tvGender)).getText().toString();
                                    timeIn = ((TextView) v.findViewById(R.id.tvTimeIn)).getText().toString();
                                    timeOut = ((TextView) v.findViewById(R.id.tvTimeOut)).getText().toString();
                                }
                                else if (Config.APP_TYPE == "2"){

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
                Intent intentQueue = new Intent(getActivity(), ChildMapsActivity.class);
                intentQueue.putExtra("parentId", parentId);
                getActivity().startActivity(intentQueue);
                return true;
            case R.id.mnuRide:


                destination.setName(childName);
                destination.setParent(parentId);
                destination.setDriver(driverId);
                destination.setAge(age);
                destination.setGender(gender);
                destination.setTimeIn(timeIn);
                destination.setTimeOut(timeOut);
                destination.setStatus("School Bus");

                ref.child("Children").child(childName).setValue(destination);

                Toast toast=Toast.makeText(getActivity(), childName +" is now in school bus.", Toast.LENGTH_LONG);
                View view=toast.getView();
                view.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_IN);
                TextView text =view.findViewById(android.R.id.message);
                text.setTextColor(Color.BLUE);
                toast.show();

                return true;
            case R.id.mnuDropH:
                destination.setName(childName);
                destination.setParent(parentId);
                destination.setDriver(driverId);
                destination.setAge(age);
                destination.setGender(gender);
                destination.setTimeIn(timeIn);
                destination.setTimeOut(timeOut);
                destination.setStatus("Home");

                ref.child("Children").child(childName).setValue(destination);

                Toast toast1=Toast.makeText(getActivity(), childName +" is now at home.", Toast.LENGTH_LONG);
                View view1=toast1.getView();
                view1.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_IN);
                TextView text1 =view1.findViewById(android.R.id.message);
                text1.setTextColor(Color.MAGENTA);
                toast1.show();
                return true;
            case R.id.mnuDropS:
                destination.setName(childName);
                destination.setParent(parentId);
                destination.setDriver(driverId);
                destination.setAge(age);
                destination.setGender(gender);
                destination.setTimeIn(timeIn);
                destination.setTimeOut(timeOut);
                destination.setStatus("School");

                ref.child("Children").child(childName).setValue(destination);

                Toast toast2=Toast.makeText(getActivity(), childName +" is now in School.", Toast.LENGTH_LONG);
                View view2=toast2.getView();
                view2.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_IN);
                TextView text2 =view2.findViewById(android.R.id.message);
                text2.setTextColor(Color.BLACK);
                toast2.show();
                return true;
            default:
                return false;
        }
    }


}
