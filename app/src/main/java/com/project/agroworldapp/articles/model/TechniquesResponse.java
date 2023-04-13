package com.project.agroworldapp.articles.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class TechniquesResponse implements Serializable {

    @SerializedName("advantages")
    private String advantages;

    @SerializedName("image_link")
    private String imageLink;

    @SerializedName("reference_link")
    private String referenceLink;

    @SerializedName("need_of_it")
    private String needOfIt;

    @SerializedName("technique_name")
    private String techniqueName;

    @SerializedName("technique_detail")
    private String techniqueDetail;

    public String getAdvantages() {
        return advantages;
    }

    public void setAdvantages(String advantages) {
        this.advantages = advantages;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getReferenceLink() {
        return referenceLink;
    }

    public void setReferenceLink(String referenceLink) {
        this.referenceLink = referenceLink;
    }

    public String getNeedOfIt() {
        return needOfIt;
    }

    public void setNeedOfIt(String needOfIt) {
        this.needOfIt = needOfIt;
    }

    public String getTechniqueName() {
        return techniqueName;
    }

    public void setTechniqueName(String techniqueName) {
        this.techniqueName = techniqueName;
    }

    public String getTechniqueDetail() {
        return techniqueDetail;
    }

    public void setTechniqueDetail(String techniqueDetail) {
        this.techniqueDetail = techniqueDetail;
    }

    @NonNull
    @Override
    public String toString() {
        return
                "FruitsResponse{" +
                        "advantages = '" + advantages + '\'' +
                        ",image_link = '" + imageLink + '\'' +
                        ",reference_link = '" + referenceLink + '\'' +
                        ",need_of_it = '" + needOfIt + '\'' +
                        ",technique_name = '" + techniqueName + '\'' +
                        ",technique_detail = '" + techniqueDetail + '\'' +
                        "}";
    }
}