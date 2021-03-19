package com.i3developer.shayari;

import java.util.List;

public class ShayariImageData {
    private String imagePath;
    private List<String> categories;

    public ShayariImageData() {
    }

    public ShayariImageData(String imagePath, List<String> categories) {
        this.imagePath = imagePath;
        this.categories = categories;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }
}
