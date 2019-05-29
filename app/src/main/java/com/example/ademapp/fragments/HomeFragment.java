package com.example.ademapp.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ademapp.Device;
import com.example.ademapp.DeviceAdapter;
import com.example.ademapp.MainActivity;
import com.example.ademapp.R;
import com.github.mikephil.charting.data.Entry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView devicesList;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private ArrayList<Device> devices;
    private DeviceAdapter adapter;
    private LinearLayoutManager layoutManager;
    private ArrayList<Entry> values;

    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        devicesList = (RecyclerView)view.findViewById(R.id.recyclerView);

        FirebaseUser user = firebaseAuth.getCurrentUser();
        getUserDevices(user);

        return view;
    }

    private void getUserDevices(FirebaseUser user) {
        final String id = user.getUid();
        DatabaseReference userRef = firebaseDatabase.getReference("devices");
        userRef.orderByChild("owner").equalTo(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                devices = new ArrayList<>();
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    values = new ArrayList<>();
                    String name = data.child("name").getValue().toString();
                    String type = data.child("type").getValue().toString();
                    String code = data.child("code").getValue().toString();
                    for (DataSnapshot val : data.child("data").getChildren()){
                        float x = Float.parseFloat(val.child("x").getValue().toString());
                        float y = Float.parseFloat(val.child("y").getValue().toString());
                        values.add(new Entry(x, y));
                    }
                    Device newDevice = new Device(name, type, code);
                    newDevice.setData(values);
                    devices.add(newDevice);
                }
                adapter = new DeviceAdapter(getContext(), devices, 0);
                layoutManager = new LinearLayoutManager(getContext());
                devicesList.setLayoutManager(layoutManager);
                devicesList.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
        void onFragmentInteraction(Uri uri);
    }
}
