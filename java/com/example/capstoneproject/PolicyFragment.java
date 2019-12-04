package com.example.capstoneproject;


import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class PolicyFragment extends Fragment {

    Button btnBack;
    Button btnContinue;
    CheckBox checkBox;
    FloatingActionButton fab_menu;

    View view;
    public PolicyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_policy, container, false);

        fab_menu = (FloatingActionButton) getActivity().findViewById(R.id.fab_menu);
        btnBack = (Button) view.findViewById(R.id.btnBack);
        btnContinue = (Button) view.findViewById(R.id.btnContinue);
        checkBox = (CheckBox) view.findViewById(R.id.checkBox);

        btnBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                removeFragment();
                fab_menu.setVisibility(View.VISIBLE);
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                removeFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                RegisterFragment registerFragment = new RegisterFragment();
                fragmentTransaction.replace(R.id.fragment_container, registerFragment);
                fragmentTransaction.commit();
            }
        });

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    btnContinue.setEnabled(isChecked);
            }
        });

        return view;
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
