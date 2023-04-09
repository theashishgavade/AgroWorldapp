package com.project.agroworld.articles.viewholder;

import androidx.recyclerview.widget.RecyclerView;

import com.project.agroworld.articles.listener.DiseasesListener;
import com.project.agroworld.articles.listener.InsectControlListener;
import com.project.agroworld.articles.model.DiseasesResponse;
import com.project.agroworld.articles.model.InsectControlResponse;
import com.project.agroworld.databinding.ArticleItemLayoutBinding;
import com.project.agroworld.utils.Constants;

public class DiseasesViewHolder extends RecyclerView.ViewHolder {

    ArticleItemLayoutBinding binding;

    public DiseasesViewHolder(ArticleItemLayoutBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void binDiseasesData(DiseasesResponse response, DiseasesListener listener) {
        binding.tvTitleArticleItem.setText(response.getDiseaseName());
        binding.tvSeasonArticleItem.setText(response.getPlantName());
        Constants.bindImage(binding.ivArticleItem, response.getImageLink(), binding.ivArticleItem);
        binding.crdArticleItem.setOnClickListener(v -> {
            listener.onDiseaseItemClick(response);
        });
    }

    public void bindInsectControlData(InsectControlResponse response, InsectControlListener listener) {
        binding.tvTitleArticleItem.setText(response.getInsectName());
        binding.tvSeasonArticleItem.setText(response.getPlantName());
        Constants.bindImage(binding.ivArticleItem, response.getImageLink(), binding.ivArticleItem);
        binding.crdArticleItem.setOnClickListener(v -> {
            listener.onInsectControlItemClick(response);
        });
    }
}
