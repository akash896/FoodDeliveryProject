package food_delivery.delivery.model;

import java.io.Serializable;

public class Customer implements Serializable {
    private int custId;
    private float amount;

    // constructor
    public Customer(int custID, float amount) {
        this.custId = custID;
        this.amount = amount;
    }

    // Getter Methods
    public int getCustId() {        return custId;    }
    public float getAmount() {        return amount;    }

    // Setter Methods
    public void setCustId(int custId) {        this.custId = custId;    }
    public void setAmount(float amount) {        this.amount = amount;    }

    @Override  // toString method
    public String toString() {
        return "Customer{" + "custID=" + custId + ", wallet=" + amount +'}';
    }

} // class ends
