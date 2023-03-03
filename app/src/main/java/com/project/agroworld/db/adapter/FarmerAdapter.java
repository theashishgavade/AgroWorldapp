package com.project.agroworld.db.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.project.agroworld.databinding.FarmerItemLayoutBinding;
import com.project.agroworld.db.FarmerModel;
import com.project.agroworld.db.listener.OnItemClickListener;
import com.project.agroworld.db.viewholder.FarmerViewHolder;

public class FarmerAdapter extends ListAdapter<FarmerModel, FarmerViewHolder> {
    private OnItemClickListener listener;
    Context context;
    public FarmerAdapter(Context context, OnItemClickListener listener) {
        super(DIFF_CALLBACK);
        this.context = context;
        this.listener = listener;
    }
    private static final DiffUtil.ItemCallback<FarmerModel> DIFF_CALLBACK = new DiffUtil.ItemCallback<FarmerModel>() {
        @Override
        public boolean areItemsTheSame(FarmerModel oldItem, FarmerModel newItem) {
            return oldItem.getId() == newItem.getId();
        }
        @Override
        public boolean areContentsTheSame(FarmerModel oldItem, FarmerModel newItem) {
            // below line is to check the course name, description and course duration.
            return oldItem.getTask().equals(newItem.getTask()) &&
                    oldItem.getDesc().equals(newItem.getDesc());
        }
    };

    @NonNull
    @Override
    public FarmerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FarmerViewHolder(FarmerItemLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FarmerViewHolder holder, int position) {
        FarmerModel model = getCourseAt(position);
        holder.bindData(model, listener, context);
    }
    public FarmerModel getCourseAt(int position) {
        return getItem(position);
    }
}
