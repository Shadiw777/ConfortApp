package com.confortapp.leon.confortapp.Model;

/**
 * Created by Leon on 12.02.2018.
 */

public class Product {
    private String name;
    private String image;
    private String description;
    private String price;
    private String menuId;
    private String discount;

    public Product() {
    }

    public Product(String name, String image, String description, String price, String menuId, String discount) {
        this.name = name;
        this.image = image;
        this.description = description;
        this.price = price;
        this.menuId = menuId;
        this.discount = discount;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
}
