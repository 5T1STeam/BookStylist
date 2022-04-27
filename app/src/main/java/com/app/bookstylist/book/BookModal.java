package com.app.bookstylist.book;

public class BookModal {
    private String complete;
    private Integer price;
    private String service;
    private String sid;
    private String time;
    private String uid;
    private String bid;
    private String shopName;
    private String shopImg;
    public BookModal(){

    }

    public String getBid() {
        return bid;
    }

    public void setBid(String bid) {
        this.bid = bid;
    }

    public BookModal(String complete, Integer price, String service, String sid, String time, String uid, String bid) {
        this.complete = complete;
        this.price = price;
        this.service = service;
        this.sid = sid;
        this.time = time;
        this.uid = uid;
        this.bid = bid;
    }

    public String getComplete() {
        return complete;
    }

    public void setComplete(String complete) {
        this.complete = complete;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopImg() {
        return shopImg;
    }

    public void setShopImg(String shopImg) {
        this.shopImg = shopImg;
    }
}
