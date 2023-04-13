package com.project.agroworldapp.articles.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.project.agroworldapp.articles.listener.DiseasesListener;
import com.project.agroworldapp.articles.model.DiseasesResponse;
import com.project.agroworldapp.articles.viewholder.DiseasesViewHolder;
import com.project.agroworldapp.databinding.ArticleItemLayoutBinding;

import java.util.List;

public class DiseaseAdapter extends RecyclerView.Adapter<DiseasesViewHolder> {

    private final List<DiseasesResponse> diseasesResponseList;
    private final DiseasesListener listener;

    public DiseaseAdapter(List<DiseasesResponse> diseasesResponseList, DiseasesListener listener) {
        this.diseasesResponseList = diseasesResponseList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DiseasesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DiseasesViewHolder(ArticleItemLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DiseasesViewHolder holder, int position) {
        DiseasesResponse response = diseasesResponseList.get(position);
        holder.binDiseasesData(response, listener);
    }

    @Override
    public int getItemCount() {
        return diseasesResponseList.size();
    }
}
