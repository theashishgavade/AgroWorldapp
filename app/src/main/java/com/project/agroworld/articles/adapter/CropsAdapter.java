package com.project.agroworld.articles.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.agroworld.articles.listener.CropsClickListener;
import com.project.agroworld.articles.listener.FruitsClickListener;
import com.project.agroworld.articles.model.CropsResponse;
import com.project.agroworld.articles.viewholder.ArticleListViewHolder;
import com.project.agroworld.databinding.ArticleItemLayoutBinding;

import java.util.ArrayList;

public class CropsAdapter extends RecyclerView.Adapter<ArticleListViewHolder> {
    private ArrayList<CropsResponse> cropsResponseArrayList;
    private CropsClickListener listener;

    public CropsAdapter(ArrayList<CropsResponse> cropsResponseArrayList, CropsClickListener listener) {
        this.cropsResponseArrayList = cropsResponseArrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ArticleListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ArticleListViewHolder(ArticleItemLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleListViewHolder holder, int position) {
        CropsResponse cropsResponses = cropsResponseArrayList.get(position);
        holder.bindCropsData(cropsResponses, listener);
    }

    @Override
    public int getItemCount() {
        return cropsResponseArrayList.size();
    }
}
