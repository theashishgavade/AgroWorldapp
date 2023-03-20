package com.project.agroworld.articles.model;

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

    public void setAdvantages(String advantages) {
        this.advantages = advantages;
    }

    public String getAdvantages() {
        return advantages;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setReferenceLink(String referenceLink) {
        this.referenceLink = referenceLink;
    }

    public String getReferenceLink() {
        return referenceLink;
    }

    public void setNeedOfIt(String needOfIt) {
        this.needOfIt = needOfIt;
    }

    public String getNeedOfIt() {
        return needOfIt;
    }

    public void setTechniqueName(String techniqueName) {
        this.techniqueName = techniqueName;
    }

    public String getTechniqueName() {
        return techniqueName;
    }

    public void setTechniqueDetail(String techniqueDetail) {
        this.techniqueDetail = techniqueDetail;
    }

    public String getTechniqueDetail() {
        return techniqueDetail;
    }

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