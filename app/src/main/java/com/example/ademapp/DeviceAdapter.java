package com.example.ademapp;


import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder> {
    Context context;
    ArrayList<Device> devices;
    LineDataSet lineDataSet;
    LineData data;
    int layoutType;
    FirebaseDatabase firebaseDatabase;

    public DeviceAdapter(Context context, ArrayList<Device> devices, int layoutType) {
        this.context = context;
        this.devices = devices;
        this.layoutType = layoutType;
    }

    @Override
    public DeviceViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view;
        if(this.layoutType == 0){
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.device_row, viewGroup, false);
        }else{
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.device_settings, viewGroup, false);
        }
        firebaseDatabase = FirebaseDatabase.getInstance();
        return new DeviceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DeviceViewHolder holder, int i) {
        final Device device = devices.get(i);
        holder.name.setText(device.getName());
        holder.code.setText(device.getCode());

        if(this.layoutType == 0){
            holder.type.setText(device.getType());
            lineDataSet = new LineDataSet(device.getData(), "Consumo");
            data = new LineData(lineDataSet);
            holder.lineChart.setData(data);
            holder.lineChart.notifyDataSetChanged();
            holder.lineChart.invalidate();
        }else{

            holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String name = holder.name.getText().toString();
                    final String type = holder.devType.getSelectedItem().toString();
                    if(name.isEmpty() || type.isEmpty()){
                        Toast.makeText(context, R.string.login_err, Toast.LENGTH_SHORT).show();
                    }else {
                        DatabaseReference ref = firebaseDatabase.getReference("devices");
                        ref.orderByChild("code").equalTo(device.getCode()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot dev : dataSnapshot.getChildren()) {
                                    dev.getRef().child("name").setValue(name);
                                    dev.getRef().child("type").setValue(type);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return devices.size();
    }

    public static class DeviceViewHolder extends RecyclerView.ViewHolder{

        private TextView name, type, code;
        private LineChart lineChart;
        private Button btnUpdate;
        private Spinner devType;

        public DeviceViewHolder(View itemView) {
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.dispName);
            type = (TextView)itemView.findViewById(R.id.dispType);
            code = (TextView)itemView.findViewById(R.id.dispCode);
            btnUpdate = (Button)itemView.findViewById(R.id.btnUpdateDev);
            devType = (Spinner)itemView.findViewById(R.id.devType);
            lineChart = (LineChart)itemView.findViewById(R.id.chart);
        }
    }

}
