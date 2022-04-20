package com.app.bookstylist.book;

import java.io.Serializable;

public class ServiceCheck implements Serializable {
    private String name, image;
    private boolean isChecked = false;

    public ServiceCheck(){
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setCheckSelect(boolean checkSelect) {
        this.isChecked = checkSelect;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
