package com.project.agroworld.articles.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.agroworld.articles.listener.FruitsClickListener;
import com.project.agroworld.articles.model.FruitsResponse;
import com.project.agroworld.articles.viewholder.ArticleListViewHolder;
import com.project.agroworld.databinding.ArticleItemLayoutBinding;

import java.util.ArrayList;

public class FruitsAdapter extends RecyclerView.Adapter<ArticleListViewHolder> {
    private ArrayList<FruitsResponse> fruitsResponseArrayList;
    private FruitsClickListener listener;

    public FruitsAdapter(ArrayList<FruitsResponse> fruitsResponseArrayList, FruitsClickListener listener) {
        this.fruitsResponseArrayList = fruitsResponseArrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ArticleListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ArticleListViewHolder(ArticleItemLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleListViewHolder holder, int position) {
        FruitsResponse response = fruitsResponseArrayList.get(position);
        holder.bindFruitsData(response, listener);
    }

    @Override
    public int getItemCount() {
        return fruitsResponseArrayList.size();
    }
}
