package com.app.bookstylist.shop;

import android.os.Parcel;
import android.os.Parcelable;


public class ShopModal implements Parcelable {

    public ShopModal(){}
    protected ShopModal(Parcel in) {
    }

    public static final Creator<ShopModal> CREATOR = new Creator<ShopModal>() {
        @Override
        public ShopModal createFromParcel(Parcel in) {
            return new ShopModal(in);
        }

        @Override
        public ShopModal[] newArray(int size) {
            return new ShopModal[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

    }
}
