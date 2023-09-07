package models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor


public class Order {
    private long id;
    private long userId;
    private String name;
    private String phoneNumber;
    private String address;
    private double grandTotal;
    private LocalDateTime creationTime;

    public Order(String record) {
        record = record.replaceAll("\"", "");
        String[] orders = record.split(",");
        id = Long.parseLong(orders[0]);
        userId = Long.parseLong(orders[1]);
        name = orders[2];
        phoneNumber = orders[3];
        address = orders[4];
        grandTotal = Double.parseDouble(orders[5]);
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        this.creationTime = LocalDateTime.parse(orders[6]);
    }

    public String toString() {
        return  id +
                "," + userId +
                "," + name +
                "," + phoneNumber +
                "," + address +
                "," + grandTotal +
                "," + creationTime;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Order other = (Order) obj;

        // Extract the LocalDate from this.creationTime
        LocalDate thisDate = this.creationTime.toLocalDate();

        // Compare the extracted LocalDate with the LocalDate in obj
        return thisDate.equals(other.creationTime);
    }
}
