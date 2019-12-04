package com.example.capstoneproject;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.capstoneproject.Models.Child;
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

import static android.app.Activity.RESULT_OK;


public class AddChildFragment extends Fragment {
    View view;
    EditText etName;
    Button btnAdd;
    Button btnBack;
    ImageView ivPic;
    RadioGroup rgGender;
    RadioButton rbGender;
    EditText etAge;
    EditText etTimeIn;
    EditText etTimeOut;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;
    private FirebaseUser user;
    private FirebaseAuth mAuth;

    ArrayList<Child> children;

    String name;
    String parent;
    String driver;
    String count;
    String age;
    String gender;
    String currentChildren;
    FloatingActionButton fab_menu;
    String timeIn;
    String timeOut;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_child, container, false);
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        Firebase.setAndroidContext(getActivity());
        btnAdd = (Button) view.findViewById(R.id.btnAdd);
        btnBack = (Button) view.findViewById(R.id.btnBack);
        fab_menu = (FloatingActionButton) getActivity().findViewById(R.id.fab_menu);

        ivPic = (ImageView) view.findViewById(R.id.ivPic);

        parent = user.getUid();

        mFirebaseDatabase = FirebaseDatabase.getInstance();
        myRef = mFirebaseDatabase.getReference().child("ParentDriver").child(user.getUid());
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

        myRef = mFirebaseDatabase.getReference().child("Profile").child(user.getUid());
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                try {
                    count = snapshot.child("number_of_child").getValue().toString();
                }
                catch(NullPointerException e){count = "0";}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        myRef = mFirebaseDatabase.getReference().child("Children");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                children = new ArrayList<Child>();
                children.clear();
                ArrayList<StorageReference> storageReferences = new ArrayList<StorageReference>();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    Child destination = new Child();
                    destination.setName(ds.child("name").getValue().toString());
                    destination.setParent(ds.child("parent").getValue().toString());
                    destination.setStatus(ds.child("status").getValue().toString());

                    try {
                        destination.setDriver(ds.child("driver").getValue().toString());
                    } catch (NullPointerException e) {
                    }
                    if (destination.getParent().equals(user.getUid()))
                        children.add(destination);

                }

                currentChildren = Integer.toString(children.size());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });



    ivPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(pickPhoto , 1);//one can be replaced with any action code
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
                addChild();
            }
        });
        return view;
    }

     public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    ivPic.setImageURI(selectedImage);
                    ivPic.setTag(selectedImage);
                }

                break;
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    ivPic.setImageURI(selectedImage);
                    ivPic.setTag(selectedImage);
                }
                break;
        }
    }

    private void addChild(){
        etName = (EditText) view.findViewById(R.id.etName);
        name = etName.getText().toString();
        rgGender = (RadioGroup) view.findViewById(R.id.rgGender);
        etAge = (EditText) view.findViewById(R.id.etAge);
        rbGender = (RadioButton) view.findViewById(rgGender.getCheckedRadioButtonId());
        gender = rbGender.getText().toString();
        age = etAge.getText().toString();
        etTimeIn = (EditText) view.findViewById(R.id.etTimeIn);
        etTimeOut = (EditText) view.findViewById(R.id.etTimeOut);
        timeIn = etTimeIn.getText().toString();
        timeOut = etTimeOut.getText().toString();


        if (Integer.parseInt(count) >= (Integer.parseInt(currentChildren) + 1)) {
            if (!timeOut.isEmpty()) {
                if (!timeIn.isEmpty()) {
                    if (!driver.isEmpty()) {
                        if (!name.isEmpty()) {
                            if (!age.isEmpty()) {
                                if (ivPic.getTag() != null) {
                                    Firebase ref = new Firebase(Config.FIREBASE_URL);
                                    Child destination = new Child();

                                    destination.setName(name);
                                    destination.setParent(parent);
                                    destination.setDriver(driver);
                                    destination.setAge(age);
                                    destination.setGender(gender);
                                    destination.setStatus("Home");
                                    destination.setTimeIn(timeIn);
                                    destination.setTimeOut(timeOut);

                                    ref.child("Children").child(name).setValue(destination);

                                    String path = ivPic.getTag().toString();
                                    Uri uri = Uri.parse(path);
                                    FirebaseStorage storage = FirebaseStorage.getInstance();
                                    StorageReference storageRef = storage.getReference().child("Children").child(name);
                                    storageRef.putFile(uri);


                                    Toast toast=Toast.makeText(getActivity(), "Add child successful.", Toast.LENGTH_LONG);
                                    View view=toast.getView();
                                    view.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_IN);
                                    TextView text =view.findViewById(android.R.id.message);
                                    text.setTextColor(Color.BLUE);
                                    toast.show();

                                    etName.setText("");
                                } else {
                                    Toast.makeText(getActivity(), "Please choose a photo for this child", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(getActivity(), "Please fill age field", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Please fill name field.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Please contact admin first to continue.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Please fill time in field.", Toast.LENGTH_LONG).show();
                }
            }
            else {
                Toast.makeText(getActivity(), "Please fill time out field.", Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(getActivity(), "Please contact admin first to continue.", Toast.LENGTH_LONG).show();
        }

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
