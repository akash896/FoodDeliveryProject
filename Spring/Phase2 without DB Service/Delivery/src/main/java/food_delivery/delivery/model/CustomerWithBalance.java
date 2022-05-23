package food_delivery.delivery.model;

import java.io.Serializable;

public class CustomerWithBalance implements Serializable {
    private int custId;
    private float balance;

    public CustomerWithBalance(){}

    public CustomerWithBalance(int custId, float balance) {
        this.custId = custId;
        this.balance = balance;
    }

    public CustomerWithBalance(Customer customer){
        this.custId = customer.getCustId();
        this.balance = customer.getAmount();
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "CustomerWithBalance{" +
                "custId=" + custId +
                ", balance=" + balance +
                '}';
    }

} // class ends
