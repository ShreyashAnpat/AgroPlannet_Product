package com.cctpl.agroplannetproduct.ui.profile;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.cctpl.agroplannetproduct.Login;
import com.cctpl.agroplannetproduct.R;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends Fragment {
    Button signOut ;
    FirebaseAuth auth ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        signOut = view.findViewById(R.id.signOut);
        auth = FirebaseAuth.getInstance() ;
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                startActivity(new Intent(view.getContext(), Login.class));

            }
        });

        return  view ;
    }

}
