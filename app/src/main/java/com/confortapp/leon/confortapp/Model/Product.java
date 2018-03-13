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

    private String products_color1, products_color2, products_color3, products_color4;

    public Product() {
    }

    public Product(String name, String image, String description, String price, String menuId, String discount, String products_color1, String products_color2, String products_color3, String products_color4) {
        this.name = name;
        this.image = image;
        this.description = description;
        this.price = price;
        this.menuId = menuId;
        this.discount = discount;
        this.products_color1 = products_color1;
        this.products_color2 = products_color2;
        this.products_color3 = products_color3;
        this.products_color4 = products_color4;
    }

    public String getProducts_color1() {
        return products_color1;
    }

    public void setProducts_color1(String products_color1) {
        this.products_color1 = products_color1;
    }

    public String getProducts_color2() {
        return products_color2;
    }

    public void setProducts_color2(String products_color2) {
        this.products_color2 = products_color2;
    }

    public String getProducts_color3() {
        return products_color3;
    }

    public void setProducts_color3(String products_color3) {
        this.products_color3 = products_color3;
    }

    public String getProducts_color4() {
        return products_color4;
    }

    public void setProducts_color4(String products_color4) {
        this.products_color4 = products_color4;
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
