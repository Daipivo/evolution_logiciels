package com.loggin.cli.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Product {
    private String id;

    private String name;
    private double price;
    private Date expirationDate;

    public Product(String name, double price, Date expirationDate) {
        this.name = name;
        this.price = price;
        this.expirationDate = expirationDate;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String expDateStr = expirationDate != null ? dateFormat.format(expirationDate) : "n/a";

        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", expirationDate=" + expDateStr +
                '}';
    }
}

