package com.example.ecommerce;

public class Cart {
    String uId, pId, pImg, pTitle, pPrice, pCategory, pQuantity;

    Cart(String uId,String pId, String pImg,String pTitle,String pPrice, String pCategory,String pQuantity){
        this.pId=pId;
        this.uId=uId;
        this.pImg = pImg;
        this.pTitle =pTitle;
        this.pPrice =pPrice;
        this.pCategory=pCategory;
        this.pQuantity=pQuantity;
    }
}
