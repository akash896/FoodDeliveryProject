package food_delivery.delivery.model;


import java.io.Serializable;

public class Agent implements Serializable {

    private int agentId;
    private int orderId;
    private String status;

    public Agent(){}

    public Agent(int agentId, int orderId, String status) {
        this.agentId = agentId;
        this.orderId = orderId;
        this.status = status;
    }

    public int getAgentId() {
        return agentId;
    }

    public void setAgentId(int agentId) {
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

    @Override
    public String toString() {
        return "Agent{" +
                "agentId=" + agentId +
                ", orderId=" + orderId +
                ", status='" + status + '\'' +
                '}';
    }
} // class ends
