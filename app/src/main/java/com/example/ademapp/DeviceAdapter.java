package com.example.ademapp;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder> {

    ArrayList<Device> devices;
    public DeviceAdapter(Context context, ArrayList<Device> devices) {
        this.devices = devices;
    }

    @Override
    public DeviceViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.device_row, viewGroup, false);
        return new DeviceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DeviceViewHolder holder, int i) {
        final Device device = devices.get(i);
        holder.name.setText(device.getName());
        holder.type.setText(device.getType());
        holder.code.setText(device.getCode());
    }

    @Override
    public int getItemCount() {
        return devices.size();
    }

    public static class DeviceViewHolder extends RecyclerView.ViewHolder{

        private TextView name, type, code;

        public DeviceViewHolder(View itemView) {
            super(itemView);
            name = (TextView)itemView.findViewById(R.id.dispName);
            type = (TextView)itemView.findViewById(R.id.dispType);
            code = (TextView)itemView.findViewById(R.id.dispCode);
        }
    }

}
