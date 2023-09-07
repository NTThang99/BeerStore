package models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class OrderItem {

    private long id;
    private long orderID;
    private long beerID;
    private String beerName;
    private double pricePerPill;
    private int quantity;
    private double totalPrice;
    private LocalDateTime creationTime;

    public OrderItem(String record) {
        record = record.replaceAll("\"", "");
        String[] orderItems = record.split(",");
        id = Long.parseLong(orderItems[0]);
        orderID = Long.parseLong(orderItems[1]);
        beerID = Long.parseLong(orderItems[2]);
        beerName = orderItems[3];
        pricePerPill = Double.parseDouble(orderItems[4]);
        quantity = Integer.parseInt(orderItems[5]);
        totalPrice = Double.parseDouble(orderItems[6]);
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        this.creationTime = LocalDateTime.parse(orderItems[7]);
    }

    public String toString() {
        return id +
                "," + orderID +
                "," + beerID +
                "," + beerName +
                "," + pricePerPill +
                "," + quantity +
                "," + totalPrice +
                "," + creationTime;

    }
    public double getTotalPrice() {
        return getPricePerPill()*getQuantity();
    }

}
