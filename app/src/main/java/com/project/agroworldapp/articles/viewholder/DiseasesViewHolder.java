package com.project.agroworldapp.articles.viewholder;

import androidx.recyclerview.widget.RecyclerView;

import com.project.agroworldapp.articles.listener.DiseasesListener;
import com.project.agroworldapp.articles.listener.InsectControlListener;
import com.project.agroworldapp.articles.model.DiseasesResponse;
import com.project.agroworldapp.articles.model.InsectControlResponse;
import com.project.agroworldapp.databinding.ArticleItemLayoutBinding;
import com.project.agroworldapp.utils.Constants;

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
