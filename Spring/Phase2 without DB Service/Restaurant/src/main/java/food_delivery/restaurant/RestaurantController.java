package food_delivery.restaurant;

import food_delivery.restaurant.model.Restaurant;
import food_delivery.restaurant.model.RestaurantOrderModel;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RestaurantController {
    private RestaurantOperations restaurantOperations;

    public RestaurantController(){
        this.restaurantOperations = new RestaurantOperations();
    }

    @PostMapping(value = "/reInitialize")
    public ResponseEntity reInitialize() {
        return restaurantOperations.reInitialize();
    }

    @PostMapping(value = "/acceptOrder", consumes = "application/json", produces = "application/json")
    @ResponseBody public ResponseEntity acceptOrder(@RequestBody RestaurantOrderModel restOrder) {
        return restaurantOperations.acceptOrder(restOrder);
    }

    @PostMapping(value = "/refillItem", consumes = "application/json", produces = "application/json")
    @ResponseBody public ResponseEntity refillItem(@RequestBody RestaurantOrderModel restOrder) {
        return restaurantOperations.refillItem(restOrder);
    }

    @PostMapping(value = "/getPriceOfItem", consumes = "application/json", produces = "application/json")
    @ResponseBody public ResponseEntity getPriceOfItem(@RequestBody RestaurantOrderModel restOrder) {
        return restaurantOperations.getPriceOfItem(restOrder);
    }

    //////////////////////////////////////////// CUSTOM APIs //////////////////////////////////////////////////////////////

      // custom mapping to show all Restaurants present
    @RequestMapping(path = "/showRestaurants", produces = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody List<Restaurant> showCustomers() {
        return restaurantOperations.showCustomers();
    }

} // class ends
