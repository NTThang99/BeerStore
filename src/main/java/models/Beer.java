package models;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Beer {
    private long id;
    private String beerName;
    private double alcoholConcentration;
    private int quantity;
    private double pricePerPill;
    private String productionDate;
    private String expirationDate;
    private String note;

    public Beer(String raw) {
        raw = raw.replaceAll("\"", "");
        String[] beerInformation = raw.split(",");
        this.id = Long.parseLong(beerInformation[0]);
        this.beerName = beerInformation[1];
        this.alcoholConcentration = Double.parseDouble(beerInformation[2]);
        this.quantity = Integer.parseInt(beerInformation[3]);
        this.pricePerPill = Double.parseDouble(beerInformation[4]);
        this.productionDate = beerInformation[5];
        this.expirationDate = beerInformation[6];
        this.note = beerInformation[7];
    }

    @Override
    public String toString() {
        return id +
                "," + beerName +
                "," + alcoholConcentration +
                "," + quantity +
                "," + pricePerPill +
                "," + productionDate +
                "," + expirationDate +
                "," + note;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
      Beer beer = (Beer) o;
      return true;
    }

    @Override
    public int hashCode() {
        return 1;
    }
}
