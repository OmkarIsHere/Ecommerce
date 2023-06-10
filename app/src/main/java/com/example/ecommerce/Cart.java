package com.example.ecommerce;

public class Cart {
    String  pId, pImg, pTitle, pPrice, pCategory, pQuantity;

    Cart(String pId, String pImg,String pTitle,String pPrice, String pCategory,String pQuantity){
        this.pId=pId;
        this.pImg = pImg;
        this.pTitle =pTitle;
        this.pPrice =pPrice;
        this.pCategory=pCategory;
        this.pQuantity=pQuantity;
    }
}
