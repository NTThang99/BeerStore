package services;

import models.Order;

import java.util.List;

public interface IOrderService {
    List<Order> getOrders();

    void add(Order newOrder);

    List<Order> getUserOrdersList(long id);

    Order getOrderById(long id,List<Order> orderList);


    List<Order> getSearchOrderList(String searchContent, List<Order> userOrdersList);
}
