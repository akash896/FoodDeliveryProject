package food_delivery.wallet;

import food_delivery.wallet.Model.Customer;
import food_delivery.wallet.Model.CustomerWithBalance;
import food_delivery.wallet.db.CustomerDB;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import food_delivery.wallet.utility.DataUtils;
import java.util.ArrayList;
import java.util.List;

@RestController
public class MapController {
    private CustomerDB customerDB;
    List<Customer> customers;
    public MapController(){
        this.customers = new ArrayList<>();
        this.customerDB = new CustomerDB();
    }

//    // custom mapping to show all customers present
//    @RequestMapping(path = "/showCustomers", produces = MediaType.APPLICATION_JSON_VALUE)
//    public @ResponseBody List<Customer> showCustomers() {
//        return customerDB.getAllCustomers();
//    }

    /* Set balance of all customers to the initial value as given in the /initialData.txt file.
    Return HTTP status code 201. */
    @PostMapping(value = "/reInitialize")
    public ResponseEntity reInitialize() {
        DataUtils dataUtils = new DataUtils();
        customerDB = dataUtils.initializeCustomer();
        customers = new ArrayList<>();
        return new ResponseEntity(HttpStatus.CREATED);
    }


    /* Increase the balance of custId num by z, and return HTTP status code 201. */
    @PostMapping(value = "/addBalance", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponseEntity addBalance(@RequestBody Customer newCustomer) {
        // updating the amount of the customer
        if(customerDB.getCustomer(newCustomer.getCustId()) != null){
            Customer c = customerDB.getCustomer(newCustomer.getCustId());
            c.setAmount(newCustomer.getAmount() + c.getAmount());
            customerDB.createCustomer(c);
        }
        return new ResponseEntity(HttpStatus.CREATED);
    }


    /* If current balance of custId num is less than z, return HTTP status code 410, else
    reduce custId num's balance by z and return HTTP status code 201. */
    @PostMapping(value = "/deductBalance", consumes = "application/json", produces = "application/json")
    public ResponseEntity deductBalance(@RequestBody Customer newCustomer) {
        // updating the amount of the customer
        if(customerDB.getCustomer(newCustomer.getCustId()) != null){
            Customer c = customerDB.getCustomer(newCustomer.getCustId());
            if(c.getAmount() >= newCustomer.getAmount()) {
                c.setAmount(c.getAmount() - newCustomer.getAmount());
                customerDB.createCustomer(c); // storing customer with updated amount
            }
            else{
                // Not Enough Balance
                return new ResponseEntity(HttpStatus.GONE);
            }
        }
        return new ResponseEntity(HttpStatus.CREATED);
    }


    /* where num is a custId. Return HTTP status code 200, and response JSON of the
    form {“custId”: num, “balance”: z}, where z is the current balance of custId num. */
    @GetMapping("/balance/{id}") @ResponseBody
    public ResponseEntity getCustomerById(@PathVariable String id) {
        int ID = Integer.parseInt(id);
        if(customerDB.getCustomer(ID) != null) { // customer present in Data
            CustomerWithBalance customerWithBalance = new CustomerWithBalance(customerDB.getCustomer(ID));
            return new ResponseEntity(customerWithBalance, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND); // when no match found
    }

} // class ends
