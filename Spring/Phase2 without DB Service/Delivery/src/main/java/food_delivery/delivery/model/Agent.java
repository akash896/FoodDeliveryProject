package food_delivery.delivery.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Agent implements Serializable{

    @Id
    private Integer agentId;
    @Column(name = "orderId", nullable = false)
    private Integer orderId;
    @Column(name = "status", nullable = false)
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
