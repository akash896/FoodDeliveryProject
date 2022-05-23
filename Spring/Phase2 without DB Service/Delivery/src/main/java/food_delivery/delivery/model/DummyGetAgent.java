package food_delivery.delivery.model;

import java.io.Serializable;

public class DummyGetAgent implements Serializable {

    private int agentId;
    private String status;

    public DummyGetAgent(){}

    public DummyGetAgent(int agentId, String status) {
        this.agentId = agentId;
        this.status = status;
    }

    public int getAgentId() {
        return agentId;
    }

    public void setAgentId(int agentId) {
        this.agentId = agentId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "DummyGetAgent{" +
                "agentId=" + agentId +
                ", status='" + status + '\'' +
                '}';
    }

}
