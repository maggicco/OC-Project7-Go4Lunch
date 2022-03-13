package com.maggicco.go4lunch.ui;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.maggicco.go4lunch.R;
import com.maggicco.go4lunch.databinding.FragmentWorkMatesBinding;
import com.maggicco.go4lunch.model.WorkMate;

import butterknife.BindView;
import butterknife.ButterKnife;

import java.util.ArrayList;
import java.util.List;


public class WorkMatesRecyclerViewAdapter extends RecyclerView.Adapter<WorkMatesRecyclerViewAdapter.ViewHolder> {

    List<WorkMate> workMateList;


    public WorkMatesRecyclerViewAdapter(List<WorkMate> workMateList) {

        this.workMateList = workMateList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_work_mates_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.mateName.setText(workMateList.get(position).getMateName());
        Glide.with(holder.itemView.getContext()).load(workMateList.get(position).getMatePhoto()).into(holder.matePhoto);

    }

    @Override
    public int getItemCount() {
        return workMateList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        //@BindView(R.id.workmates_image_item)
        ImageView matePhoto;
        @BindView(R.id.workmates_name_item)
        TextView mateName;

        public ViewHolder(View itemView) {
            super(itemView);
            matePhoto = itemView.findViewById(R.id.workmates_image_item);
            ButterKnife.bind(this, itemView);
        }

    }
}