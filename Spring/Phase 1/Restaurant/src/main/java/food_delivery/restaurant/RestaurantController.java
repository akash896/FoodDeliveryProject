package food_delivery.restaurant;


import food_delivery.restaurant.db.RestaurantDB;
import food_delivery.restaurant.model.Item;
import food_delivery.restaurant.model.Restaurant;
import food_delivery.restaurant.model.RestaurantOrderModel;
import food_delivery.restaurant.utility.DataUtils;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RestaurantController {
    private RestaurantDB restaurantDB;

    public RestaurantController(){
        this.restaurantDB = new RestaurantDB();
    }


    @PostMapping(value = "/reInitialize")
    public ResponseEntity reInitialize() {
        DataUtils dataUtils = new DataUtils();
        restaurantDB = dataUtils.initializeRestaurant();
//        System.out.println(new ResponseEntity(HttpStatus.CREATED));
        return new ResponseEntity(HttpStatus.CREATED);
    }

//    // custom mapping to show all Restaurants present
//    @RequestMapping(path = "/showRestaurants", produces = MediaType.APPLICATION_JSON_VALUE)
//    public @ResponseBody List<Restaurant> showCustomers() {
//        return restaurantDB.getAllRestaurants();
//    }


    @PostMapping(value = "/acceptOrder", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity acceptOrder(@RequestBody RestaurantOrderModel restOrder) {
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

    @PostMapping(value = "/refillItem", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity refillItem(@RequestBody RestaurantOrderModel restOrder) {
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

    //////////////////////////////////////////////////////////////////////////////////////////////////////////


} // class ends
