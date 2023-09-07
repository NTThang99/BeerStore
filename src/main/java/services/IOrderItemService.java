package services;

import models.OrderItem;

import java.util.ArrayList;
import java.util.List;

public interface IOrderItemService {

    List<OrderItem> getOrderItems();

    void addMoreOrderItems(ArrayList<OrderItem> newOrderItemList);

    List<OrderItem> getUserOrderItemList(long orderID);

    boolean isItemOrdered(long id);
}
