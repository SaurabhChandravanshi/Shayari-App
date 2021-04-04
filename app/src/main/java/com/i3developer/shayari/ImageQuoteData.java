package com.i3developer.shayari;

public class ImageQuoteData {
    private String category,imagePath;

    public ImageQuoteData() {
    }

    public ImageQuoteData(String category, String imagePath) {
        this.category = category;
        this.imagePath = imagePath;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
