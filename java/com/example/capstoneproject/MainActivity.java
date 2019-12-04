package com.example.capstoneproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FirebaseUser user;
    private FirebaseAuth mAuth;
    FloatingActionButton fab_home_info, fab_menu, fab_sos, fab_profile, fab_login, fab_register, fab_logout, fab_children,
            fab_follow, fab_admin_map, fab_driver_lists, fab_parent_lists,
            fab_parent_to_driver,fab_abt,fab_feed,fab_share,fab_dev,fab_rt;

    Animation fabOpen, fabClose, rotateForward, rotateBackward;
    boolean isOpen = false;
    String login = "";

    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try{
            if (Config.APP_TYPE.equals("3"))
                setTitle("School Bus Tracking (Admin)");
            else if (Config.APP_TYPE.equals("2"))
                setTitle("School Bus Tracking (Guardian)");
            else if (Config.APP_TYPE.equals("1"))
                setTitle("School Bus Tracking (Driver)");
        }
        catch (NullPointerException e){setTitle("School Bus Tracking");}

        fab_home_info = (FloatingActionButton) findViewById(R.id.fab_home_info);
        fab_menu = (FloatingActionButton) findViewById(R.id.fab_menu);
        fab_profile = (FloatingActionButton) findViewById(R.id.fab_profile);
        fab_sos = (FloatingActionButton) findViewById(R.id.fab_sos);
        fab_abt = (FloatingActionButton) findViewById(R.id.fab_abt);
        fab_feed = (FloatingActionButton) findViewById(R.id.fab_feed);
        fab_dev = (FloatingActionButton) findViewById(R.id.fab_dev);
        fab_share = (FloatingActionButton) findViewById(R.id.fab_share);
        fab_rt = (FloatingActionButton) findViewById(R.id.fab_rt);
        fab_login = (FloatingActionButton) findViewById(R.id.fab_login);
        fab_logout = (FloatingActionButton) findViewById(R.id.fab_logout);
        fab_register = (FloatingActionButton) findViewById(R.id.fab_register);
        fab_children = (FloatingActionButton) findViewById(R.id.fab_children);
        fab_follow = (FloatingActionButton) findViewById(R.id.fab_follow);
        fab_admin_map = (FloatingActionButton) findViewById(R.id.fab_admin_map);
        fab_driver_lists = (FloatingActionButton) findViewById(R.id.fab_driver_lists);
        fab_parent_lists = (FloatingActionButton) findViewById(R.id.fab_parent_lists);
        fab_parent_to_driver = (FloatingActionButton) findViewById(R.id.fab_parent_to_driver);


        fabOpen = AnimationUtils.loadAnimation(this,R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(this,R.anim.fab_close);

        rotateForward = AnimationUtils.loadAnimation(this,R.anim.rotate_forward);
        rotateBackward = AnimationUtils.loadAnimation(this,R.anim.rotate_backward);

        fab_menu.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast toast= Toast.makeText(MainActivity.this, "Press to Open Menu", Toast.LENGTH_LONG);
                View view=toast.getView();
                view.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_IN);
                TextView text =view.findViewById(android.R.id.message);
                text.setTextColor(Color.MAGENTA);
                toast.show();
                return false;
            }
        });

        fab_admin_map.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast toast=Toast.makeText(MainActivity.this, "Set School Location in Google Map", Toast.LENGTH_LONG);
                View view=toast.getView();
                view.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_IN);
                TextView text =view.findViewById(android.R.id.message);
                text.setTextColor(Color.MAGENTA);
                toast.show();
                return false;
            }
        });

        fab_follow.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast toast=Toast.makeText(MainActivity.this, "Track Driver Location", Toast.LENGTH_LONG);
                View view=toast.getView();
                view.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_IN);
                TextView text =view.findViewById(android.R.id.message);
                text.setTextColor(Color.MAGENTA);
                toast.show();
                return false;
            }
        });

        fab_profile.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast toast=Toast.makeText(MainActivity.this, "Set Home Location", Toast.LENGTH_LONG);
                View view=toast.getView();
                view.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_IN);
                TextView text =view.findViewById(android.R.id.message);
                text.setTextColor(Color.MAGENTA);
                toast.show();
                return false;
            }
        });

        fab_abt.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast toast=Toast.makeText(MainActivity.this, "Contact Us", Toast.LENGTH_LONG);
                View view=toast.getView();
                view.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_IN);
                TextView text =view.findViewById(android.R.id.message);
                text.setTextColor(Color.MAGENTA);
                toast.show();
                return false;
            }
        });
        fab_feed.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast toast=Toast.makeText(MainActivity.this, "FeedbackForm", Toast.LENGTH_LONG);
                View view=toast.getView();
                view.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_IN);
                TextView text =view.findViewById(android.R.id.message);
                text.setTextColor(Color.MAGENTA);
                toast.show();
                return false;
            }
        });

        fab_dev.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast toast=Toast.makeText(MainActivity.this, "Developers", Toast.LENGTH_LONG);
                View view=toast.getView();
                view.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_IN);
                TextView text =view.findViewById(android.R.id.message);
                text.setTextColor(Color.MAGENTA);
                toast.show();
                return false;
            }
        });

        fab_share.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast toast=Toast.makeText(MainActivity.this, "Share", Toast.LENGTH_LONG);
                View view=toast.getView();
                view.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_IN);
                TextView text =view.findViewById(android.R.id.message);
                text.setTextColor(Color.MAGENTA);
                toast.show();
                return false;
            }
        });

        fab_rt.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast toast=Toast.makeText(MainActivity.this, "Rating Bar", Toast.LENGTH_LONG);
                View view=toast.getView();
                view.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_IN);
                TextView text =view.findViewById(android.R.id.message);
                text.setTextColor(Color.MAGENTA);
                toast.show();
                return false;
            }
        });

        fab_children.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast toast=Toast.makeText(MainActivity.this, "View Children List", Toast.LENGTH_LONG);
                View view=toast.getView();
                view.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_IN);
                TextView text =view.findViewById(android.R.id.message);
                text.setTextColor(Color.MAGENTA);
                toast.show();
                return false;
            }
        });

        fab_sos.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast toast=Toast.makeText(MainActivity.this, "View SOS List", Toast.LENGTH_LONG);
                View view=toast.getView();
                view.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_IN);
                TextView text =view.findViewById(android.R.id.message);
                text.setTextColor(Color.MAGENTA);
                toast.show();
                return false;
            }
        });

        fab_parent_lists.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast toast= Toast.makeText(MainActivity.this, "Manage Guardian List", Toast.LENGTH_LONG);
                View view=toast.getView();
                view.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_IN);
                TextView text =view.findViewById(android.R.id.message);
                text.setTextColor(Color.MAGENTA);
                toast.show();
                return false;
            }
        });

        fab_parent_to_driver.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast toast=Toast.makeText(MainActivity.this, "Assign Driver to Parent", Toast.LENGTH_LONG);
                View view=toast.getView();
                view.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_IN);
                TextView text =view.findViewById(android.R.id.message);
                text.setTextColor(Color.MAGENTA);
                toast.show();
                return false;
            }
        });

        fab_logout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast toast=Toast.makeText(MainActivity.this, "Logout", Toast.LENGTH_LONG);
                View view=toast.getView();
                view.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_IN);
                TextView text =view.findViewById(android.R.id.message);
                text.setTextColor(Color.MAGENTA);
                toast.show();
                return false;
            }
        });

        fab_login.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast toast=Toast.makeText(MainActivity.this, "Login", Toast.LENGTH_LONG);
                View view=toast.getView();
                view.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_IN);
                TextView text =view.findViewById(android.R.id.message);
                text.setTextColor(Color.MAGENTA);
                toast.show();
                return false;
            }
        });

        fab_register.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast toast=Toast.makeText(MainActivity.this, "Register", Toast.LENGTH_LONG);
                View view=toast.getView();
                view.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_IN);
                TextView text =view.findViewById(android.R.id.message);
                text.setTextColor(Color.MAGENTA);
                toast.show();
                return false;
            }
        });

        fab_driver_lists.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast toast= Toast.makeText(MainActivity.this, "Manage Registered Driver List", Toast.LENGTH_LONG);
                View view=toast.getView();
                view.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_IN);
                TextView text =view.findViewById(android.R.id.message);
                text.setTextColor(Color.MAGENTA);
                toast.show();
                return false;
            }
        });

        fab_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFab();
                try{
                    if (Config.APP_TYPE.equals("3"))
                        setTitle("School Bus Tracking (Admin)");
                    else if (Config.APP_TYPE.equals("2"))
                        setTitle("School Bus Tracking (Guardian)");
                    else if (Config.APP_TYPE.equals("1"))
                        setTitle("School Bus Tracking (Driver)");
                }
                catch (NullPointerException e){setTitle("School Bus Tracking");}
            }
        });

        fab_home_info.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                try {
                    if (Config.APP_TYPE.equals("1")) {
                        Intent intent = new Intent(MainActivity.this, DriverInfoActivity.class);
                        MainActivity.this.startActivity(intent);
                    } else if (Config.APP_TYPE.equals("2")) {
                        Intent intent = new Intent(MainActivity.this, ParentInfoActivity.class);
                        MainActivity.this.startActivity(intent);
                    } else if (Config.APP_TYPE.equals("3")) {
                        Intent intent = new Intent(MainActivity.this, AdminInfoActivity.class);
                        MainActivity.this.startActivity(intent);
                    } else {
                        Intent intent = new Intent(MainActivity.this, HomeInfoActivity.class);
                        MainActivity.this.startActivity(intent);
                    }
                }
                catch  (Exception e){
                    Intent intent = new Intent(MainActivity.this, HomeInfoActivity.class);
                    MainActivity.this.startActivity(intent);
                }
            }
        });
        fab_follow.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                mFirebaseDatabase = FirebaseDatabase.getInstance();
                myRef = mFirebaseDatabase.getReference().child("ParentDriver").child(user.getUid());
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        try {
                            Intent intent = new Intent(MainActivity.this, DriverMapsActivity.class);
                            intent.putExtra("driverId", snapshot.getValue().toString());
                            MainActivity.this.startActivity(intent);
                        }
                        catch(NullPointerException e){
                           Toast toast= Toast.makeText(MainActivity.this, "Please Contact Admin to use this Feature.", Toast.LENGTH_LONG);
                            View view=toast.getView();
                            view.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_IN);
                            TextView text =view.findViewById(android.R.id.message);
                            text.setTextColor(Color.RED);
                            toast.show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
        });

        fab_profile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                animateFab();
                Intent intent = new Intent(MainActivity.this, ParentMapsActivity.class);
                MainActivity.this.startActivity(intent);

            }
        });

        fab_admin_map.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                animateFab();
                Intent intent = new Intent(MainActivity.this, AdminMapsActivity.class);
                MainActivity.this.startActivity(intent);

            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_feed);
        fab_feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(MainActivity.this, FeedbackForm.class);
                startActivity(intent);
            }
        });

        FloatingActionButton fababt = (FloatingActionButton) findViewById(R.id.fab_abt);
        fab_abt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(MainActivity.this, AboutUs.class);
                startActivity(intent);
            }
        });

        FloatingActionButton fabshare = (FloatingActionButton) findViewById(R.id.fab_share);
        fab_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(MainActivity.this, Share.class);
                startActivity(intent);
            }
        });

        FloatingActionButton fabdev = (FloatingActionButton) findViewById(R.id.fab_dev);
        fab_dev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(MainActivity.this, Developers.class);
                startActivity(intent);
            }
        });

        FloatingActionButton fabrt = (FloatingActionButton) findViewById(R.id.fab_rt);
        fab_rt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(MainActivity.this, Ratingbar.class);
                startActivity(intent);
            }
        });

        fab_parent_to_driver.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                animateFab();
                Intent intent = new Intent(MainActivity.this, ParentToDriverActivity.class);
                MainActivity.this.startActivity(intent);

            }
        });

        fab_sos.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){

                mFirebaseDatabase = FirebaseDatabase.getInstance();
                myRef = mFirebaseDatabase.getReference().child("Profile").child(user.getUid()).child("type");
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {

                        animateFab();
                        Config.APP_TYPE = snapshot.getValue().toString();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        SOSFragment sosFragment = new SOSFragment();
                        AddSOSFragment addSOSFragment = new AddSOSFragment();

                        if (Config.APP_TYPE.equals("1"))
                            fragmentTransaction.replace(R.id.fragment_container, addSOSFragment);
                        else
                            fragmentTransaction.replace(R.id.fragment_container, sosFragment);

                        fragmentTransaction.commit();
                        mainGone();

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });


            }
        });



        fab_children.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                animateFab();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                ChildrenFragment childrenFragment = new ChildrenFragment();
                fragmentTransaction.replace(R.id.fragment_container, childrenFragment);
                fragmentTransaction.commit();
                mainGone();
            }
        });

        fab_driver_lists.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                animateFab();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                DriverListFragment driverListFragment = new DriverListFragment();
                fragmentTransaction.replace(R.id.fragment_container, driverListFragment);
                fragmentTransaction.commit();
                mainGone();
            }
        });

        fab_parent_lists.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                animateFab();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                ParentListFragment parentListFragment = new ParentListFragment();
                fragmentTransaction.replace(R.id.fragment_container, parentListFragment);
                fragmentTransaction.commit();
                mainGone();
            }
        });

        fab_register.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                animateFab();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                PolicyFragment registerFragment = new PolicyFragment();
                fragmentTransaction.replace(R.id.fragment_container, registerFragment);
                fragmentTransaction.commit();
                mainGone();
            }
        });

        fab_logout.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v){
               animateFab();
               AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
               builder.setTitle("Logout");
               builder.setMessage("Are you sure you want to logout?");

               builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                   public void onClick(DialogInterface dialog, int which) {
                       // Do nothing but close the dialog
                       FirebaseAuth.getInstance().signOut();
                       setTitle("School Bus Tracking");
                       Toast toast=Toast.makeText(getBaseContext(), "You are now Logged out.", Toast.LENGTH_LONG);
                       View view=toast.getView();
                       view.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_IN);
                       TextView text =view.findViewById(android.R.id.message);
                       text.setTextColor(Color.RED);
                       toast.show();
                       dialog.dismiss();
                   }
               });

               builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                   @Override
                   public void onClick(DialogInterface dialog, int which) {

                       // Do nothing
                       dialog.dismiss();
                   }
               });

               AlertDialog alert = builder.create();
               alert.show();
           }
        });

        fab_login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                animateFab();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                LoginFragment loginFragment = new LoginFragment();
                fragmentTransaction.replace(R.id.fragment_container, loginFragment);
                fragmentTransaction.commit();
                mainGone();
            }
        });
    }

    private void animateFab(){
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        if(isOpen){
            if(user == null){
                fab_register.startAnimation(rotateForward);
                fab_login.startAnimation(fabClose);
                fab_register.startAnimation(fabClose);
                fab_login.setVisibility(View.INVISIBLE);
                fab_register.setVisibility(View.INVISIBLE);
                fab_dev.startAnimation(fabClose);
                fab_dev.setVisibility(View.INVISIBLE);
                fab_share.startAnimation(fabClose);
                fab_share.setVisibility(View.INVISIBLE);
                fab_abt.startAnimation(fabClose);
                fab_abt.setVisibility(View.INVISIBLE);
                //fab_home_info.startAnimation(fabClose);
                //fab_home_info.setVisibility(View.INVISIBLE);

            }
            else {
                mFirebaseDatabase = FirebaseDatabase.getInstance();
                myRef = mFirebaseDatabase.getReference().child("Profile").child(user.getUid()).child("type");
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        Config.APP_TYPE = snapshot.getValue().toString();
                        fab_menu.startAnimation(rotateForward);
                        fab_sos.startAnimation(fabClose);
                        fab_logout.startAnimation(fabClose);
                        fab_logout.setVisibility(View.INVISIBLE);
                        fab_sos.setVisibility(View.INVISIBLE);
                        fab_rt.startAnimation(fabClose);
                        fab_rt.setVisibility(View.INVISIBLE);
                        fab_feed.startAnimation(fabClose);
                        fab_feed.setVisibility(View.INVISIBLE);
                        //fab_home_info.startAnimation(fabClose);
                        //fab_home_info.setVisibility(View.INVISIBLE);
                        if (Config.APP_TYPE.equals("1")){
                            fab_children.setVisibility(View.INVISIBLE);
                            fab_children.startAnimation(fabClose);
                        }
                        else if (Config.APP_TYPE.equals("2")){
                            fab_profile.setVisibility(View.INVISIBLE);
                            fab_profile.startAnimation(fabClose);
                            fab_follow.setVisibility(View.INVISIBLE);
                            fab_follow.startAnimation(fabClose);

                            fab_children.setVisibility(View.INVISIBLE);
                            fab_children.startAnimation(fabClose);
                        }
                        else {
                            fab_admin_map.setVisibility(View.INVISIBLE);
                            fab_admin_map.startAnimation(fabClose);
                            fab_driver_lists.startAnimation(fabClose);
                            fab_driver_lists.setVisibility(View.INVISIBLE);
                            fab_parent_lists.startAnimation(fabClose);
                            fab_parent_lists.setVisibility(View.INVISIBLE);
                            fab_parent_to_driver.startAnimation(fabClose);
                            fab_parent_to_driver.setVisibility(View.INVISIBLE);
                        }

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
            }
            isOpen = false;
        }
        else{
            if(user == null){
                fab_register.startAnimation(rotateBackward);
                fab_login.startAnimation(fabOpen);
                fab_register.startAnimation(fabOpen);
                fab_login.setVisibility(View.VISIBLE);
                fab_register.setVisibility(View.VISIBLE);
                fab_dev.startAnimation(fabOpen);
                fab_dev.setVisibility(View.VISIBLE);
                fab_share.startAnimation(fabOpen);
                fab_share.setVisibility(View.VISIBLE);
                fab_abt.startAnimation(fabOpen);
                fab_abt.setVisibility(View.VISIBLE);
                //fab_home_info.startAnimation(fabOpen);
                //fab_home_info.setVisibility(View.VISIBLE);
            }
            else {
                mFirebaseDatabase = FirebaseDatabase.getInstance();
                myRef = mFirebaseDatabase.getReference().child("Profile").child(user.getUid()).child("type");
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        Config.APP_TYPE = snapshot.getValue().toString();
                        fab_menu.startAnimation(rotateBackward);

                        //fab_home_info.startAnimation(fabOpen);
                        //fab_home_info.setVisibility(View.VISIBLE);
                        fab_rt.startAnimation(fabOpen);
                        fab_rt.setVisibility(View.VISIBLE);
                        fab_feed.startAnimation(fabOpen);
                        fab_feed.setVisibility(View.VISIBLE);
                        fab_sos.startAnimation(fabOpen);
                        fab_logout.startAnimation(fabOpen);
                        fab_logout.setVisibility(View.VISIBLE);
                        fab_sos.setVisibility(View.VISIBLE);
                        if (Config.APP_TYPE.equals("1")){
                            fab_children.setVisibility(View.VISIBLE);
                            fab_children.startAnimation(fabOpen);
                        }

                        else if (Config.APP_TYPE.equals("2")){
                            fab_profile.startAnimation(fabOpen);
                            fab_profile.setVisibility(View.VISIBLE);
                            fab_follow.startAnimation(fabOpen);
                            fab_follow.setVisibility(View.VISIBLE);

                            fab_children.setVisibility(View.VISIBLE);
                            fab_children.startAnimation(fabOpen);
                        }
                        else{
                            fab_admin_map.startAnimation(fabOpen);
                            fab_admin_map.setVisibility(View.VISIBLE);
                            fab_driver_lists.startAnimation(fabOpen);
                            fab_driver_lists.setVisibility(View.VISIBLE);
                            fab_parent_lists.startAnimation(fabOpen);
                            fab_parent_lists.setVisibility(View.VISIBLE);
                            fab_parent_to_driver.startAnimation(fabOpen);
                            fab_parent_to_driver.setVisibility(View.VISIBLE);
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

            }
            isOpen = true;
        }
    }

    public void removeFragment(){
        try {
            List<Fragment> fragments = getSupportFragmentManager().getFragments();
            if (fragments != null) {
                for (Fragment fragment : fragments) {
                    getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                }
            }
        }
        catch (NullPointerException e){}
    }

    public void mainGone(){
        fab_menu.setVisibility(View.GONE);
    }

}
