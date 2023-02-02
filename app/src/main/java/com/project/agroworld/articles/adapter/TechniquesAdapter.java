package com.project.agroworld.articles.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.agroworld.articles.listener.FruitsClickListener;
import com.project.agroworld.articles.model.TechniquesResponse;
import com.project.agroworld.articles.viewholder.ArticleListViewHolder;
import com.project.agroworld.databinding.ArticleItemLayoutBinding;

import java.util.ArrayList;

public class TechniquesAdapter extends RecyclerView.Adapter<ArticleListViewHolder> {
    private ArrayList<TechniquesResponse> techniquesResponseArrayList;
    private FruitsClickListener listener;
    Context context;

    public TechniquesAdapter(ArrayList<TechniquesResponse> techniquesResponseArrayList, FruitsClickListener listener) {
        this.techniquesResponseArrayList = techniquesResponseArrayList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ArticleListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ArticleListViewHolder(ArticleItemLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleListViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return techniquesResponseArrayList.size();
    }
}
