package com.project.agroworld.articles.viewholder;

import androidx.recyclerview.widget.RecyclerView;

import com.project.agroworld.articles.listener.DiseasesListener;
import com.project.agroworld.articles.model.DiseasesResponse;
import com.project.agroworld.databinding.DiseasesItemLayoutBinding;
import com.project.agroworld.utils.Constants;

public class DiseasesViewHolder extends RecyclerView.ViewHolder {

    DiseasesItemLayoutBinding binding;

    public DiseasesViewHolder(DiseasesItemLayoutBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void binDiseasesData(DiseasesResponse response, DiseasesListener listener) {
        binding.tvDiseaseName.setText(response.getDiseaseName());
        binding.tvPlantName.setText(response.getPlantName());
        Constants.bindImage(binding.ivDiseaseProduct, response.getImageLink(), binding.ivDiseaseProduct);
        binding.crdDisease.setOnClickListener(v -> {
            listener.onDiseaseItemClick(response);
        });

    }
}
