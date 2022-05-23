package food_delivery.delivery.model;

public class OrderNumber {
    private int orderId;

    public OrderNumber(int orderId) {
        this.orderId = orderId;
    }

    public OrderNumber(){}

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    @Override
    public String toString() {
        return "OrderNumber{" +
                "orderId=" + orderId +
                '}';
    }

} // class ends
