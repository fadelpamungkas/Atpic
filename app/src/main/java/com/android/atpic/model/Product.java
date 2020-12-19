package com.android.atpic.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Comparator;


public class Product implements Parcelable {
    private String id, name, desc, id_user, id_category, upload_date, photoURL;
    private long sold, price;

    public Product(){
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getId_category() {
        return id_category;
    }

    public void setId_category(String id_category) {
        this.id_category = id_category;
    }

    public String getUpload_date() {
        return upload_date;
    }

    public void setUpload_date(String upload_date) {
        this.upload_date = upload_date;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public long getSold() {
        return sold;
    }

    public void setSold(long sold) {
        this.sold = sold;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public Product(Parcel in) {
        id = in.readString();
        name = in.readString();
        desc = in.readString();
        id_user = in.readString();
        id_category = in.readString();
        upload_date = in.readString();
        photoURL = in.readString();
        sold = in.readLong();
        price = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(desc);
        dest.writeString(id_user);
        dest.writeString(id_category);
        dest.writeString(upload_date);
        dest.writeString(photoURL);
        dest.writeLong(sold);
        dest.writeLong(price);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public static Comparator<Product> BySold = new Comparator<Product>() {
        @Override
        public int compare(Product o1, Product o2) {
            return - Long.valueOf(o1.sold).compareTo(Long.valueOf(o2.sold));
        }
    };
}

