package food_delivery.restaurant;

import food_delivery.restaurant.db.RestaurantDB;
import food_delivery.restaurant.model.Item;
import food_delivery.restaurant.model.Price;
import food_delivery.restaurant.model.Restaurant;
import food_delivery.restaurant.model.RestaurantOrderModel;
import food_delivery.restaurant.utility.DataUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

public class RestaurantOperations {
    private RestaurantDB restaurantDB;

    public RestaurantOperations(){
        this.restaurantDB = new RestaurantDB();
    }

    public synchronized ResponseEntity reInitialize() {
        DataUtils dataUtils = new DataUtils();
        restaurantDB = dataUtils.initializeRestaurant();
//        System.out.println(new ResponseEntity(HttpStatus.CREATED));
        return new ResponseEntity(HttpStatus.CREATED);
    }

    public synchronized ResponseEntity acceptOrder(RestaurantOrderModel restOrder) {
        //System.out.println("INSIDE ACCEPT ORDER with order = \n"+ restOrder.toString());
        if(restaurantDB.getRestaurant(restOrder.getRestId()) == null)
            return new ResponseEntity(HttpStatus.GONE);
        Restaurant restaurant = restaurantDB.getRestaurant(restOrder.getRestId());
        if(restaurant.getItemDB().getItem(restOrder.getItemId()) == null) // item is not present in that restaurant
            return new ResponseEntity(HttpStatus.GONE);
        else{ // item present in restaurant
            Item item = restaurant.getItemDB().getItem(restOrder.getItemId());
            if(restOrder.getQty() <= item.getQty()){ // enough item quantity present in Restaurant
                item.setQty(item.getQty() - restOrder.getQty());
                restaurant.getItemDB().createItem(item);
            }
            else{ // not enough quantity present in Restaurant
                return new ResponseEntity(HttpStatus.GONE);
            }
        }
        return new ResponseEntity(HttpStatus.CREATED);
    }

    public synchronized ResponseEntity refillItem(RestaurantOrderModel restOrder) {
        if(restaurantDB.getRestaurant(restOrder.getRestId()) == null)
            return new ResponseEntity(HttpStatus.CREATED);
        Restaurant restaurant = restaurantDB.getRestaurant(restOrder.getRestId());
        if(restaurant.getItemDB().getItem(restOrder.getItemId()) == null) // item is not present in that restaurant
            return new ResponseEntity(HttpStatus.CREATED);
        else{ // item present in restaurant
            Item item = restaurant.getItemDB().getItem(restOrder.getItemId());
            item.setQty(item.getQty() + restOrder.getQty());
            restaurant.getItemDB().createItem(item);
        }
        return new ResponseEntity(HttpStatus.CREATED);
    }

    ////////////////////////////////// CUSTOM Functions /////////////////////////////////////////////////////

    public @ResponseBody List<Restaurant> showCustomers() {
        return restaurantDB.getAllRestaurants();
    }

    public ResponseEntity getRestaurantFromRestaurant(String id) {
        Restaurant restaurant = restaurantDB.getRestaurant(Integer.parseInt(id));
        return new ResponseEntity(restaurant, HttpStatus.OK);
    }

    public ResponseEntity getPriceOfItem(RestaurantOrderModel restOrder) {
        Restaurant restaurant = null;
        Item item = null;
        restaurant = restaurantDB.getRestaurant(restOrder.getRestId());
        item = restaurant.getItemDB().getItem(restOrder.getItemId());
        Price price = null;
        if(item!=null)
            price = new Price(item.getPrice());
        return new ResponseEntity(price, HttpStatus.CREATED);
    }
} // class ends
