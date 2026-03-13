package com.bittercode.service;

import java.util.List;
import com.bittercode.model.Order;
import com.bittercode.model.StoreException;

public interface OrderService {
    void saveOrder(Order order) throws StoreException;
    List<Order> getOrdersByUsername(String username) throws StoreException;
}
