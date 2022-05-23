package food_delivery.wallet;

import food_delivery.wallet.Model.Customer;
import food_delivery.wallet.Model.CustomerWithBalance;
import food_delivery.wallet.db.CustomerDB;
import food_delivery.wallet.utility.DataUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

public class WalletOperations {
    private CustomerDB customerDB;
    private List<Customer> customers;

    public WalletOperations(){
        this.customers = new ArrayList<>();
        this.customerDB = new CustomerDB();
    }


    public synchronized ResponseEntity reInitialize() {
        DataUtils dataUtils = new DataUtils();
        customerDB = dataUtils.initializeCustomer();
        customers = new ArrayList<>();
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public synchronized ResponseEntity addBalance(@RequestBody Customer newCustomer) {
        // updating the amount of the customer
        if(customerDB.getCustomer(newCustomer.getCustId()) != null){
            Customer c = customerDB.getCustomer(newCustomer.getCustId());
            c.setAmount(newCustomer.getAmount() + c.getAmount());
            customerDB.createCustomer(c);
        }
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public synchronized ResponseEntity deductBalance(@RequestBody Customer newCustomer) {
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

    public ResponseEntity getCustomerById(String id) {
        int ID = Integer.parseInt(id);
        if(customerDB.getCustomer(ID) != null) { // customer present in Data
            CustomerWithBalance customerWithBalance = new CustomerWithBalance(customerDB.getCustomer(ID));
            return new ResponseEntity(customerWithBalance, HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND); // when no match found
    }

    /////////////////////////// CUSTOM Functions ///////////////////////////////////////////////////////////

    public @ResponseBody List<Customer> showCustomers() {
        return customerDB.getAllCustomers();
    }


} // class ends
