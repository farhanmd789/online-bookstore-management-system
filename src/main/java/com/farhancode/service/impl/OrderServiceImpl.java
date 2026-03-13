package com.bittercode.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.bittercode.model.Order;
import com.bittercode.model.StoreException;
import com.bittercode.service.OrderService;
import com.bittercode.util.DBUtil;

public class OrderServiceImpl implements OrderService {

    private static final String saveOrderQuery =
        "INSERT INTO orders (username, book_barcode, book_name, book_author, price, quantity) VALUES (?, ?, ?, ?, ?, ?)";

    private static final String getOrdersByUsernameQuery =
        "SELECT id, username, book_barcode, book_name, book_author, price, quantity, order_date FROM orders WHERE username = ? ORDER BY order_date DESC";

    @Override
    public void saveOrder(Order order) throws StoreException {
        Connection con = DBUtil.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement(saveOrderQuery);
            ps.setString(1, order.getUsername());
            ps.setString(2, order.getBookBarcode());
            ps.setString(3, order.getBookName());
            ps.setString(4, order.getBookAuthor());
            ps.setDouble(5, order.getPrice());
            ps.setInt(6, order.getQuantity());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Order> getOrdersByUsername(String username) throws StoreException {
        List<Order> orders = new ArrayList<>();
        Connection con = DBUtil.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement(getOrdersByUsernameQuery);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Order o = new Order();
                o.setId(rs.getInt("id"));
                o.setUsername(rs.getString("username"));
                o.setBookBarcode(rs.getString("book_barcode"));
                o.setBookName(rs.getString("book_name"));
                o.setBookAuthor(rs.getString("book_author"));
                o.setPrice(rs.getDouble("price"));
                o.setQuantity(rs.getInt("quantity"));
                o.setOrderDate(rs.getTimestamp("order_date"));
                orders.add(o);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }
}
