package food_delivery.delivery.db;

import food_delivery.delivery.model.Restaurant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestaurantDB {
    private Map<Integer, Restaurant> restaurants;

    public RestaurantDB(){
        this.restaurants = new HashMap<>();
    }

    public void createRestaurant(Restaurant restaurant){
        restaurants.put(restaurant.getRestId(), restaurant);
    }

    public Restaurant getRestaurant(int restId){
        if(restaurants.containsKey(restId))
            return restaurants.get(restId);
        else
            return null;
    }

    public List<Restaurant> getAllRestaurants(){
        List<Restaurant> restList = new ArrayList<>();
        for(Integer k : restaurants.keySet())
            restList.add(restaurants.get(k));
        return restList;
    }

}
