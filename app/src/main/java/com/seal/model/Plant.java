package com.seal.model;

import java.util.ArrayList;

public class Plant {
    private int plantId;
    private String scienceName;
    private ArrayList<String> commonNames, descriptions;
    private ArrayList<byte[]> pictures;

    public Plant() {
    }

    public Plant(int plantId, String scienceName) {
        this.plantId = plantId;
        this.scienceName = scienceName;
    }

    public Plant(ArrayList<String> commonNames) {
        this.commonNames = commonNames;
    }

    public Plant(int plantId, String scienceName, ArrayList<String> commonNames, ArrayList<String> descriptions, ArrayList<byte[]> pictures) {
        this.plantId = plantId;
        this.scienceName = scienceName;
        this.commonNames = commonNames;
        this.descriptions = descriptions;
        this.pictures = pictures;
    }

    public int getPlantId() {
        return plantId;
    }

    public void setPlantId(int plantId) {
        this.plantId = plantId;
    }

    public String getScienceName() {
        return scienceName;
    }

    public void setScienceName(String scienceName) {
        this.scienceName = scienceName;
    }

    public ArrayList<String> getCommonNames() {
        return commonNames;
    }

    public void setCommonNames(ArrayList<String> commonNames) {
        this.commonNames = commonNames;
    }

    public ArrayList<String> getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(ArrayList<String> descriptions) {
        this.descriptions = descriptions;
    }

    public ArrayList<byte[]> getPictures() {
        return pictures;
    }

    public void setPictures(ArrayList<byte[]> pictures) {
        this.pictures = pictures;
    }
}

