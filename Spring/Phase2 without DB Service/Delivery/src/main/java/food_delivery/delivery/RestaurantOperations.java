package food_delivery.delivery;

import com.fasterxml.jackson.databind.ObjectMapper;
import food_delivery.delivery.db.AgentDB;
import food_delivery.delivery.db.RestaurantDB;
import food_delivery.delivery.model.*;
import food_delivery.delivery.utility.DataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class RestaurantOperations {


    private RestaurantDB restaurantDB;
    @Autowired
    private Environment env;
    private String deliveryApp;
    private String restaurantApp;
    private String walletApp;

    public RestaurantOperations(Environment env){
        DataUtils dataUtils = new DataUtils();
        this.env = env;
        this.deliveryApp = env.getProperty("deliveryApp");
        this.restaurantApp = env.getProperty("restaurantApp");
        this.walletApp = env.getProperty("walletApp");
//        restaurantDB = dataUtils.initializeRestaurant();
    }



    public boolean checkBalancePresent(int custId, int restId, int itemId, float quantity) {
        float billingPrice = 0;
        Price price = getItemPrice(new RestaurantOrderModel(restId, itemId,quantity));
        if(price!=null) {
            // calculating price of order
            billingPrice = quantity * price.getPrice();
            System.out.println("billing price = " + billingPrice);
        }
        else {
            return false;
        }
        Customer customer = new Customer(custId, billingPrice);
//        return sendCustomerPostRequest("http://host.docker.internal:8082/deductBalance", customer);
        return sendCustomerPostRequest(walletApp+"deductBalance", customer);
    }

    public Price getItemPrice(RestaurantOrderModel restaurantOrderModel){
        Price price = null;
        RestTemplate restTemplate = new RestTemplate();
        String URI = restaurantApp+"getPriceOfItem";
        HttpEntity<RestaurantOrderModel> request = new HttpEntity<>(restaurantOrderModel);
        try {
            ResponseEntity<String> response = restTemplate.exchange(URI, HttpMethod.POST, request, String.class);
            System.out.println();
            ObjectMapper mapper = new ObjectMapper();
            String body = response.getBody();
            //System.out.println("BODY = "+ body);
            price = mapper.readValue(body, Price.class);
            System.out.println("Price receivd  = " + price.toString());
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return price;
    }


    private boolean sendCustomerPostRequest(String URI, Customer customer) {
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Customer> request = new HttpEntity<>(customer);
        try {
            ResponseEntity<Customer> response = restTemplate.exchange(URI, HttpMethod.POST, request, Customer.class);
            if (response.getStatusCode().toString().indexOf("201") != -1) {
                return true;
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    private Customer getCustomer(String URI) {
        RestTemplate restTemplate = new RestTemplate();
        Customer customer = restTemplate.getForObject(URI, Customer.class);
        return customer;
    }

    public boolean checkAcceptOrder(int restId, int itemId, float qty) {
        RestaurantOrderModel restOrder = new RestaurantOrderModel(restId, itemId, qty);
//        String URI = "http://host.docker.internal:8080/acceptOrder";
        String URI = restaurantApp+"acceptOrder";
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<RestaurantOrderModel> request = new HttpEntity<>(restOrder);
        try {
            ResponseEntity<String> response = restTemplate.exchange(URI, HttpMethod.POST, request, String.class);
            MediaType contentType = response.getHeaders().getContentType();
            HttpStatus statusCode = response.getStatusCode();
            if (response.getStatusCode().toString().indexOf("201") != -1) {
                return true;
            }
            else
                return false;
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean creditBalance(int custId, int restId, int itemId, float qty) {
        float billingPrice = 0;
        if(restaurantDB.getRestaurant(restId) != null) {
            Restaurant restaurant = restaurantDB.getRestaurant(restId);
            Item item;
            item = restaurant.getItemDB().getItem(itemId);
            billingPrice = qty * item.getPrice();
        }
        else
            return false;
        Customer customer = new Customer(custId, billingPrice);
//        return sendCustomerPostRequest("http://host.docker.internal:8082/addBalance", customer);
        return sendCustomerPostRequest(walletApp+"addBalance", customer);
    }

    public Agent getLowestIdAgent(AgentDB agentDB){
        Agent agent = null;
        List<Agent> agentList = agentDB.getAllAgents();
        for(Agent a : agentList){
            if(agent == null && a.getStatus().equals("available"))
                agent = a;
            if(agent!=null && a.getStatus().equals("available")){
                if(a.getAgentId() < agent.getAgentId())
                    agent = a;
            }
        }
        return agent;
    }


}
