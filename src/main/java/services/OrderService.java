package services;




import models.Order;
import utils.CSVUtils;
import utils.ValidateUtils;

import java.util.ArrayList;
import java.util.List;

public class OrderService implements IOrderService {
    private static final String path = "C:\\BeerStore\\src\\main\\java\\data\\orders.csv";

    @Override
    public List<Order> getOrders() {
        List<Order> orderList = new ArrayList<>();
        List<String> records = CSVUtils.readData(path);
        for (String record : records) {
            if(!record.equals("")){
                orderList.add(new Order(record));
            }
        }
        return orderList;
    }

    @Override
    public void add(Order newOrder) {
        List<Order> orderList = getOrders();

        orderList.add(newOrder);
        CSVUtils.writeData(path, orderList);
    }


    @Override
    public Order getOrderById(long id,List<Order> orderList) {
        for (Order order : orderList) {
            if (order.getId() == id)
                return order;
        }
        return null;
    }

    @Override
    public List<Order> getSearchOrderList(String searchContent, List<Order> userOrdersList) {
        List<Order> searchOrderList = new ArrayList<>();
        for (Order order : userOrdersList) {
            String orderPlus = order.toString() + ValidateUtils.convertMilliToDate(order.getCreationTime());
            if (orderPlus.toLowerCase().contains(searchContent)) {
                searchOrderList.add(order);
            }
        }
        return searchOrderList;
    }

    public List<Order> getUserOrdersList(long id) {
        List<Order> userOrderList = new ArrayList<>();
        for (Order userOrder : getOrders()) {
            if (userOrder.getUserId() == id) {
                userOrderList.add(userOrder);
            }
        }
        return userOrderList;
    }

}

