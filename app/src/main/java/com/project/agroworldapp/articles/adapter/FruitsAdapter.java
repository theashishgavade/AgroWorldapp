package com.project.agroworldapp.articles.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.agroworldapp.articles.listener.FruitsClickListener;
import com.project.agroworldapp.articles.model.FruitsResponse;
import com.project.agroworldapp.articles.viewholder.ArticleListViewHolder;
import com.project.agroworldapp.databinding.ArticleItemLayoutBinding;

import java.util.ArrayList;

public class FruitsAdapter extends RecyclerView.Adapter<ArticleListViewHolder> {
    private final ArrayList<FruitsResponse> fruitsResponseArrayList;
    private final FruitsClickListener listener;

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
