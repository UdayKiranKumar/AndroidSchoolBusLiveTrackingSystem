package com.example.capstoneproject;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.example.capstoneproject.Models.SOS;

public class AddSOSFragment extends Fragment {
    View view;
    EditText etMessage;
    Button btnAdd;
    Button btnBack;

    private FirebaseUser user;
    private FirebaseAuth mAuth;

    String message;
    String date;
    String driverId;
    FloatingActionButton fab_menu;
    public AddSOSFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_add_so, container, false);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        Firebase.setAndroidContext(getActivity());
        btnAdd = (Button) view.findViewById(R.id.btnAdd);
        btnBack = (Button) view.findViewById(R.id.btnBack);
        fab_menu = (FloatingActionButton) getActivity().findViewById(R.id.fab_menu);

        driverId = user.getUid();



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
                addSOS();
            }
        });
        return view;
    }


    private void addSOS(){
        etMessage = (EditText) view.findViewById(R.id.etMessage);
        message = etMessage.getText().toString();

        if(!message.isEmpty()) {

            Firebase ref = new Firebase(Config.FIREBASE_URL);
            SOS destination = new SOS();

            destination.setMessage(message);

            String dateEpoch = String.valueOf(new Date().getTime());
            long epoch = Long.parseLong(dateEpoch);
            Date date = new Date(epoch);
            destination.setDate(new SimpleDateFormat("yyyy-MM-dd a hh:mm:ss").format(date));

            ref.child("SOS").child(driverId).push().setValue(destination);


            Toast toast=Toast.makeText(getActivity(), "Sent SOS Successfully to Parent.", Toast.LENGTH_LONG);
            View view=toast.getView();
            view.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_IN);
            TextView text =view.findViewById(android.R.id.message);
            text.setTextColor(Color.RED);
            toast.show();

            etMessage.setText("");
        }
        else
        {
            Toast.makeText(getActivity(), "Please type a message.", Toast.LENGTH_LONG).show();
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
