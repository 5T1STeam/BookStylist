package com.app.bookstylist.shop;

public class Rates {
    public int getShopid() {
        return shopid;
    }

    public void setShopid(int shopid) {
        this.shopid = shopid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    private int shopid;
    private String userid;
    private String desc;
    private String img;
    private int rate;
    private String name;
    private String profileImage;
    private String createdate;



    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }



    public Rates(int shopid, String userid, String desc, String img, int rate) {
        this.shopid = shopid;
        this.userid = userid;
        this.desc = desc;
        this.img = img;
        this.rate = rate;
    }
    public Rates(){

    };
}
