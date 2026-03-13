package com.bittercode.model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Order implements Serializable {

    private int id;
    private String username;
    private String bookBarcode;
    private String bookName;
    private String bookAuthor;
    private double price;
    private int quantity;
    private Timestamp orderDate;

    public Order() {}

    public Order(String username, String bookBarcode, String bookName, String bookAuthor, double price, int quantity) {
        this.username = username;
        this.bookBarcode = bookBarcode;
        this.bookName = bookName;
        this.bookAuthor = bookAuthor;
        this.price = price;
        this.quantity = quantity;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getBookBarcode() { return bookBarcode; }
    public void setBookBarcode(String bookBarcode) { this.bookBarcode = bookBarcode; }

    public String getBookName() { return bookName; }
    public void setBookName(String bookName) { this.bookName = bookName; }

    public String getBookAuthor() { return bookAuthor; }
    public void setBookAuthor(String bookAuthor) { this.bookAuthor = bookAuthor; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public Timestamp getOrderDate() { return orderDate; }
    public void setOrderDate(Timestamp orderDate) { this.orderDate = orderDate; }
}
