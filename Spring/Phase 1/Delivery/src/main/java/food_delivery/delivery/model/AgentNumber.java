package food_delivery.delivery.model;

public class AgentNumber {
    private int agentId;

    public AgentNumber(int agentId) {
        this.agentId = agentId;
    }

    public AgentNumber(){}

    public int getAgentId() {
        return agentId;
    }

    public void setAgentId(int agentId) {
        this.agentId = agentId;
    }

    @Override
    public String toString() {
        return "AgentNumber{" +
                "agentId=" + agentId +
                '}';
    }

} // class ends

