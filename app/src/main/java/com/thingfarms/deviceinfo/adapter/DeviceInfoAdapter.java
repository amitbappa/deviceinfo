package com.thingfarms.deviceinfo.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thingfarms.deviceinfo.R;
import com.thingfarms.deviceinfo.model.DeviceInfoData;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DeviceInfoAdapter extends RecyclerView.Adapter<DeviceInfoAdapter.MyViewHolder> {

    private List<DeviceInfoData> userList = new ArrayList<>();

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.device_row, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.bind(userList.get(position));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.thumbnail)
        TextView thumbnail;
        @BindView(R.id.viewSwitcher)
        ViewSwitcher viewSwitcher;
        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.phone)
        TextView phone;
        @BindView(R.id.email)
        TextView email;
        @BindView(R.id.progressBar)
        ProgressBar progressBar;
        @BindView(R.id.todo)
        TextView todo;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(DeviceInfoData user) {
            thumbnail.setTextColor(randomColor());
            thumbnail.setText(String.valueOf(user.getInfoName().toUpperCase().charAt(0)));
            name.setText(user.getInfoName());
            phone.setText(user.getInfoDetails());
           // email.setText(user.getEmail());

            viewSwitcher.setDisplayedChild(0);
            /*if (user.getTodoList()!=null && !user.getTodoList().isEmpty()) {
                viewSwitcher.setDisplayedChild(1);
                todo.setText(user.getTodoList().size() + " TODOS");
            }*/
        }
    }

    public void setData(List<DeviceInfoData> data) {
        userList.addAll(data);
        notifyDataSetChanged();
    }

    public void updateData(DeviceInfoData user) {
        userList.set(userList.indexOf(user), user);
        notifyItemChanged(userList.indexOf(user));
    }

    private int randomColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }
}
