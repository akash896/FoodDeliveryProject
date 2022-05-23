package food_delivery.wallet.db;

import food_delivery.wallet.Model.Customer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerDB {
    private Map<Integer, Customer> customers = new HashMap<>();

    public  Customer getCustomer(int custId){
        if(customers.containsKey(custId))
            return customers.get(custId);
        else
            return null;
    }

    public List<Customer> getAllCustomers(){
        List<Customer> custList = new ArrayList<Customer>();
        for(Integer k : customers.keySet())
            custList.add(customers.get(k));
        return custList;
    }

    public void createCustomer(Customer customer){
        customers.put(customer.getCustId(), customer);
    }

}
