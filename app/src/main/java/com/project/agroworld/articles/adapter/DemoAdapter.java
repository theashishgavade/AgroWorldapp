package com.project.agroworld.articles.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.agroworld.articles.listener.CropsClickListener;
import com.project.agroworld.articles.model.CropsResponse;
import com.project.agroworld.databinding.DemoItemLayoutBinding;

import java.util.ArrayList;

public class DemoAdapter extends RecyclerView.Adapter<DemoAdapter.DemoViewHolder> {
    private ArrayList cropsResponseArrayList;
    private final CropsClickListener listener;

    public<T> DemoAdapter(ArrayList<T> cropsResponseArrayList, CropsClickListener listener) {
        this.cropsResponseArrayList = cropsResponseArrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DemoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DemoViewHolder(DemoItemLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DemoViewHolder holder, int position) {
        holder.bindData();
    }

    @Override
    public int getItemCount() {
        return cropsResponseArrayList.size();
    }

    class DemoViewHolder extends RecyclerView.ViewHolder{
        private DemoItemLayoutBinding binding;

        public DemoViewHolder(DemoItemLayoutBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindData(){

        }
    }
}


