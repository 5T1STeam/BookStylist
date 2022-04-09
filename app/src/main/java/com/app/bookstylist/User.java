package com.app.bookstylist;

import java.util.Date;

public class User {
    private String AddressPath;
    private Date Birthday;
    private Date CreateDate;
    private String Email;
    private Integer EmailConfirm;
    private String FullAdress;
    private Integer Id;
    private Long Latitude;
    private Long Longtitude;
    private String Name;
    private String PasswordHash;
    private String PhoneNumber;
    private Integer Point;
    private Integer RankId;
    private Integer RoleId;
    private String UserName;


    public User(String addressPath, Date birthday, Date createDate, String email, Integer emailConfirm, String fullAdress, Integer id, Long latitude, Long longtitude, String name, String passwordHash, String phoneNumber, Integer point, Integer rankId, Integer roleId, String userName) {
        AddressPath = addressPath;
        Birthday = birthday;
        CreateDate = createDate;
        Email = email;
        EmailConfirm = emailConfirm;
        FullAdress = fullAdress;
        Id = id;
        Latitude = latitude;
        Longtitude = longtitude;
        Name = name;
        PasswordHash = passwordHash;
        PhoneNumber = phoneNumber;
        Point = point;
        RankId = rankId;
        RoleId = roleId;
        UserName = userName;
    }

    public String getAddressPath() {
        return AddressPath;
    }

    public void setAddressPath(String addressPath) {
        AddressPath = addressPath;
    }

    public Date getBirthday() {
        return Birthday;
    }

    public void setBirthday(Date birthday) {
        Birthday = birthday;
    }

    public Date getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(Date createDate) {
        CreateDate = createDate;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public Integer getEmailConfirm() {
        return EmailConfirm;
    }

    public void setEmailConfirm(Integer emailConfirm) {
        EmailConfirm = emailConfirm;
    }

    public String getFullAdress() {
        return FullAdress;
    }

    public void setFullAdress(String fullAdress) {
        FullAdress = fullAdress;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }

    public Long getLatitude() {
        return Latitude;
    }

    public void setLatitude(Long latitude) {
        Latitude = latitude;
    }

    public Long getLongtitude() {
        return Longtitude;
    }

    public void setLongtitude(Long longtitude) {
        Longtitude = longtitude;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPasswordHash() {
        return PasswordHash;
    }

    public void setPasswordHash(String passwordHash) {
        PasswordHash = passwordHash;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public Integer getPoint() {
        return Point;
    }

    public void setPoint(Integer point) {
        Point = point;
    }

    public Integer getRankId() {
        return RankId;
    }

    public void setRankId(Integer rankId) {
        RankId = rankId;
    }

    public Integer getRoleId() {
        return RoleId;
    }

    public void setRoleId(Integer roleId) {
        RoleId = roleId;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }
}
