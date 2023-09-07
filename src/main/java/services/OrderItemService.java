package services;

import models.OrderItem;
import utils.CSVUtils;

import java.util.ArrayList;
import java.util.List;

public class OrderItemService implements IOrderItemService {
    private static final String path = "C:\\BeerStore\\src\\main\\java\\data\\ordersItems.csv";
    @Override
    public List<OrderItem> getOrderItems() {
        List<OrderItem> orderItemList = new ArrayList<>();
        List<String> records = CSVUtils.readData(path);
        for (String record : records) {
            if(!record.equals("")){
                orderItemList.add(new OrderItem(record));
            }

        }
        return orderItemList;
    }

    @Override
    public void addMoreOrderItems(ArrayList<OrderItem> newOrderItemList) {
        List<OrderItem> orderItemList = getOrderItems();
        orderItemList.addAll(newOrderItemList);
        CSVUtils.writeData(path, orderItemList);
    }

    @Override
    public List<OrderItem> getUserOrderItemList(long orderID) {
        List<OrderItem> userOrderItemList = new ArrayList<>();
        for (OrderItem orderItem : getOrderItems()) {
            if (orderItem.getOrderID() == orderID) {
                userOrderItemList.add(orderItem);
            }
        }
        return userOrderItemList;
    }

    @Override
    public boolean isItemOrdered(long id) {
        List<OrderItem> orderItems = getOrderItems();
        for (OrderItem orderItem : orderItems) {
            if (orderItem.getBeerID() == id) {
                return true;
            }
        }
        return false;
    }
}
