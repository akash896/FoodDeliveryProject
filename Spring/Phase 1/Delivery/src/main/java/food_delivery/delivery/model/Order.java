package food_delivery.delivery.model;

public class Order {

    private int orderID;
    private int agentId;
    private int custId;
    private int restId;
    private int itemId;
    private float qty;
    private String status;

    public Order(int orderID, int agentId, int custId, int restId, int itemId, float qty, String status) {
        this.orderID = orderID;
        this.agentId = agentId;
        this.custId = custId;
        this.restId = restId;
        this.itemId = itemId;
        this.qty = qty;
        this.status = status;
    }

    public int getOrderID() {
        return orderID;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public int getAgentId() {
        return agentId;
    }

    public void setAgentId(int agentId) {
        this.agentId = agentId;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public int getRestId() {
        return restId;
    }

    public void setRestId(int restId) {
        this.restId = restId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public float getQty() {
        return qty;
    }

    public void setQty(float qty) {
        this.qty = qty;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderID=" + orderID +
                ", agentId=" + agentId +
                ", custId=" + custId +
                ", restId=" + restId +
                ", itemId=" + itemId +
                ", qty=" + qty +
                ", status='" + status + '\'' +
                '}';
    }
} // class ends
