package services;
import models.Beer;
import java.util.List;

public interface IBeerService {
    List<Beer> getBeers();



    Beer getBeerById(int id);


    void add(Beer newBeer);

    void update(Beer newBeer);

    void remove(long id);

    boolean isIdExisted(int id);

    List<Beer> getSearchBeerList(String searchContent, List<Beer> userBeersList);
}
