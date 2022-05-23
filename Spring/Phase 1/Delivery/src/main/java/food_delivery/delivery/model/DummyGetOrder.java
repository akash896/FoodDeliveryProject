package food_delivery.delivery.model;

public class DummyGetOrder {

    private int orderId;
    private String status;
    private int agentId;

    public DummyGetOrder(int orderId, String status, int agentId) {
        this.orderId = orderId;
        this.status = status;
        this.agentId = agentId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getAgentId() {
        return agentId;
    }

    public void setAgentId(int agentId) {
        this.agentId = agentId;
    }

    @Override
    public String toString() {
        return "DummyGetOrder{" +
                "orderId=" + orderId +
                ", status='" + status + '\'' +
                ", agentId=" + agentId +
                '}';
    }
} // class ends
