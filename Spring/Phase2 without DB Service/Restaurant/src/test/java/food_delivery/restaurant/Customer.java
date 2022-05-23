package food_delivery.restaurant;

public class Customer {
    private int custId;
    private int amount;

    // constructor
    public Customer(int custID, int wallet) {
        this.custId = custID;
        this.amount = wallet;
    }

    // Getter Methods
    public int getCustId() {        return custId;    }
    public int getAmount() {        return amount;    }

    // Setter Methods
    public void setCustId(int custId) {        this.custId = custId;    }
    public void setAmount(int amount) {        this.amount = amount;    }

    @Override  // toString method
    public String toString() {
        return "Customer{" + "custID=" + custId + ", wallet=" + amount +'}';
    }

}
