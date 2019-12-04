package com.example.capstoneproject;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class LoginFragment extends Fragment {
    View view;

    private FirebaseAuth fireBaseAuth;
    private ProgressDialog progressDialog;

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference myRef;

    Button signup;
    FragmentManager fragmentManager;

    Button backButton;
    Button loginButton;
    EditText etEmail;
    EditText etPassword;

    String email;
    String password;
    FloatingActionButton fab_menu;

    public LoginFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_login, container, false);

        fireBaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(getActivity());
        fab_menu = (FloatingActionButton) getActivity().findViewById(R.id.fab_menu);

        Firebase.setAndroidContext(getActivity());
        signup = (Button) view.findViewById(R.id.udayreg);
        signup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                PolicyFragment registerFragment = new PolicyFragment();
                fragmentTransaction.replace(R.id.fragment_container, registerFragment);
                fragmentTransaction.commit();
            }
        });

        backButton = (Button) view.findViewById(R.id.btnBack);
        loginButton = (Button) view.findViewById(R.id.btnLogin);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFragment();
                fab_menu.setVisibility(View.VISIBLE);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                login();
            }
        });

        return view;
    }


    public void login() {
        etEmail = (EditText) view.findViewById(R.id.etEmail);
        etPassword = (EditText) view.findViewById(R.id.etPassword);
        fab_menu.setVisibility(View.VISIBLE);
        email = etEmail.getText().toString();
        password = etPassword.getText().toString();

        if(email.isEmpty() || password.isEmpty() ){
            Toast  toast =Toast.makeText(getActivity(), "Please Enter Email & Password", Toast.LENGTH_LONG);
            View view = toast.getView();
            view.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_IN);
            TextView text = view.findViewById(android.R.id.message);
            text.setTextColor(Color.RED);
            toast.show();
        }

       else {
            if (password!= null) {
                progressDialog.setMessage("Logging in Please Wait...");
                progressDialog.show();
                fireBaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    if (!user.isEmailVerified()) {
                                        user.sendEmailVerification()
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            FirebaseAuth.getInstance().signOut();
                                                            Toast toast = Toast.makeText(getActivity(), "Login Failed, Please Verify from your Email, Verification Sent to Email " + email, Toast.LENGTH_LONG);
                                                            View view = toast.getView();
                                                            view.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_IN);
                                                            TextView text = view.findViewById(android.R.id.message);
                                                            text.setTextColor(Color.RED);
                                                            toast.show();
                                                            removeFragment();
                                                            progressDialog.dismiss();
                                                        }
                                                    }
                                                });
                                    } else {
                                        progressDialog.dismiss();
                                        mFirebaseDatabase = FirebaseDatabase.getInstance();
                                        myRef = mFirebaseDatabase.getReference().child("Profile").child(user.getUid()).child("type");
                                        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot snapshot) {
                                                Config.APP_TYPE = snapshot.getValue().toString();

                                                try {
                                                    if (Config.APP_TYPE.equals("3"))
                                                        getActivity().setTitle("School Bus Tracking (Admin)");
                                                    else if (Config.APP_TYPE.equals("2"))
                                                        getActivity().setTitle("School Bus Tracking (Parent)");
                                                    else if (Config.APP_TYPE.equals("1"))
                                                        getActivity().setTitle("School Bus Tracking (Driver)");
                                                } catch (NullPointerException e) {
                                                }

                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                            }
                                        });

                                        Toast toast = Toast.makeText(getActivity(), "You are now logged in.", Toast.LENGTH_LONG);
                                        View view = toast.getView();
                                        view.getBackground().setColorFilter(Color.LTGRAY, PorterDuff.Mode.SRC_IN);
                                        TextView text = view.findViewById(android.R.id.message);
                                        text.setTextColor(Color.BLUE);
                                        toast.show();
                                        removeFragment();
                                    }
                                }
                            }

                        });
            }

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
