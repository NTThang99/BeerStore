package services;

import models.Beer;
import models.Order;
import models.OrderItem;
import utils.CSVUtils;
import utils.ValidateUtils;

import java.util.ArrayList;
import java.util.List;

public class BeerService implements IBeerService {
    public static String path ="C:\\BeerStore\\src\\main\\java\\data\\beers.csv";

    @Override
    public List<Beer> getBeers() {
        List<Beer> newBeersList = new ArrayList<>();
        List<String> records = CSVUtils.readData(path);
        for (String record : records) {
            newBeersList.add(new Beer(record));
        }
        return newBeersList;
    }

    @Override
    public Beer getBeerById(int id) {
        List<Beer> beers = getBeers();
        for (Beer beer : beers) {
            if (beer.getId() == id && beer.getQuantity() != 0){
                return beer;
            }
        }
        return null;
    }

    @Override
    public void add(Beer newBeer) {
    List<Beer> beers = getBeers();
    beers.add(newBeer);
    CSVUtils.writeData(path,beers);
    }
    @Override
    public void update(Beer newBeer) {
        List<Beer> beers = getBeers();
        for (Beer beer: beers) {
            if (newBeer.getBeerName() != null && !newBeer.getBeerName().isEmpty())
                beer.setBeerName(newBeer.getBeerName());
            if (newBeer.getAlcoholConcentration() != beer.getAlcoholConcentration())
                    beer.setAlcoholConcentration(newBeer.getAlcoholConcentration());
            if (newBeer.getQuantity() != beer.getQuantity())
                beer.setQuantity(newBeer.getQuantity());
            if (newBeer.getPricePerPill() != beer.getPricePerPill())
                beer.setPricePerPill(newBeer.getPricePerPill());
            if (newBeer.getProductionDate() != null && !newBeer.getProductionDate().isEmpty())
                beer.setProductionDate(newBeer.getProductionDate());
            if (newBeer.getExpirationDate() != null && !newBeer.getExpirationDate().isEmpty())
                beer.setExpirationDate(newBeer.getExpirationDate());
            if (newBeer.getNote() != null && !newBeer.getNote().isEmpty())
                beer.setNote(newBeer.getNote());
            CSVUtils.writeData(path, beers);
            break;
        }

    }

    @Override
    public void remove(Beer beer) {
        List<Beer> beers = getBeers();
        beers.remove(beer);
        CSVUtils.writeData(path,beers);
    }

    @Override
    public boolean isIdExisted(int id) {
        return getBeerById(id) != null;
    }

    @Override
    public List<Beer> getSearchBeerList(String searchContent, List<Beer> userBeersList) {
        List<Beer>beerListSearch = new ArrayList<>();
        for (Beer beerSearch : userBeersList) {
            if (beerSearch.getBeerName().toLowerCase().contains(searchContent)) {
                beerListSearch.add(beerSearch);
            }
        }
        return beerListSearch;
    }
}
