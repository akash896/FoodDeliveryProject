package food_delivery.wallet;

import food_delivery.wallet.Model.Customer;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class MapController {
    private WalletOperations walletOperations;

    public MapController(){
        this.walletOperations = new WalletOperations();
    }

    /* Set balance of all customers to the initial value as given in the /initialData.txt file.
    Return HTTP status code 201. */
    @PostMapping(value = "/reInitialize")
    public synchronized ResponseEntity reInitialize() {
        return walletOperations.reInitialize();
    }

    /* Increase the balance of custId num by z, and return HTTP status code 201. */
    @PostMapping(value = "/addBalance", consumes = "application/json", produces = "application/json")
    @ResponseBody public ResponseEntity addBalance(@RequestBody Customer newCustomer) {
        return walletOperations.addBalance(newCustomer);
    }

    /* If current balance of custId num is less than z, return HTTP status code 410, else
    reduce custId num's balance by z and return HTTP status code 201. */
    @PostMapping(value = "/deductBalance", consumes = "application/json", produces = "application/json")
    public synchronized ResponseEntity deductBalance(@RequestBody Customer newCustomer) {
        return walletOperations.deductBalance(newCustomer);
    }


    /* where num is a custId. Return HTTP status code 200, and response JSON of the
    form {“custId”: num, “balance”: z}, where z is the current balance of custId num. */
    @GetMapping("/balance/{id}") @ResponseBody
    public synchronized ResponseEntity getCustomerById(@PathVariable String id) {
        return walletOperations.getCustomerById(id);
    }

    ////////////////////////////////// CUSTOM APIs ///////////////////////////////////////////////////////

      // custom mapping to show all customers present
//    @RequestMapping(path = "/showCustomers", produces = MediaType.APPLICATION_JSON_VALUE)
//    public @ResponseBody List<Customer> showCustomers() {
//        return walletOperations.showCustomers();
//    }


} // class ends
