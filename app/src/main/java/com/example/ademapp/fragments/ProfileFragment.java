package com.example.ademapp.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ademapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private EditText viewUsername, viewEmail, viewID, viewPass, viewNewPass;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private FirebaseUser user;
    private Button btnUpdateUser, btnUpdatePass;

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        viewUsername = (EditText) view.findViewById(R.id.viewUsername);
        viewEmail = (EditText) view.findViewById(R.id.viewEmail);
        viewID = (EditText) view.findViewById(R.id.viewId);
        viewPass = (EditText) view.findViewById(R.id.viewPass);
        viewNewPass = (EditText) view.findViewById(R.id.viewNewPass);
        btnUpdateUser = (Button)view.findViewById(R.id.btnUpdateUser);
        btnUpdatePass = (Button)view.findViewById(R.id.btnUpdatePass);

        user = firebaseAuth.getCurrentUser();
        setUserInfo(user);

        btnUpdateUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userid = user.getUid();
                String newUsername = viewUsername.getText().toString();
                if(newUsername.isEmpty()){
                    Toast.makeText(getContext(), R.string.login_err, Toast.LENGTH_SHORT).show();
                }else {
                    DatabaseReference ref = database.getReference("usuarios/" + userid);
                    ref.child("username").setValue(newUsername);
                    Toast.makeText(getContext(), R.string.success_update_user, Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnUpdatePass.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String actualPass = viewPass.getText().toString();
                final String newPass = viewNewPass.getText().toString();
                if(actualPass.isEmpty() || newPass.isEmpty()){
                    Toast.makeText(getContext(), R.string.login_err, Toast.LENGTH_SHORT).show();
                }else {
                    AuthCredential credentials = EmailAuthProvider.getCredential(user.getEmail(), actualPass);
                    user.reauthenticate(credentials).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                user.updatePassword(newPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getContext(), R.string.success_update_pass, Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(getContext(), R.string.fail_update_pass, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(getContext(), R.string.fail_current_pass, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            }
        });
        return view;
    }

    private void setUserInfo(FirebaseUser user) {
        final String id = user.getUid();
        DatabaseReference userRef = database.getReference("usuarios/"+id);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String username = dataSnapshot.child("username").getValue().toString();
                String email = dataSnapshot.child("email").getValue().toString();
                viewUsername.setText(username);
                viewEmail.setText(email);
                viewID.setText(id);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
