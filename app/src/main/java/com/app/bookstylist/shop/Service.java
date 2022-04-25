package com.app.bookstylist.shop;

public class Service {

    public Service(String name, String image, int shopid, int price, int time) {
        this.name = name;
        this.image = image;
        this.shopid = shopid;
        this.price = price;
        this.time = time;
    }

    private String name;
    private String image;
    private int shopid;
    private  int price;
    private int time;
    private boolean isChecked = false;

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

    public int getShopId() {
        return shopid;
    }

    public void setShopId(int shopid) {
        this.shopid = shopid;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public Service(String name, String image) {
        this.name = name;
        this.image = image;
    }


    public Service(){

    }

}
