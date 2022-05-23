package food_delivery.delivery.db;

import food_delivery.delivery.model.Agent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AgentDB {
    private Map<Integer, Agent> agents;

    public AgentDB() {
        this.agents = new HashMap<>();
    }

    public void createAgent(Agent agent){
        agents.put(agent.getAgentId(), agent);
    }

    public Agent getAgent(int agentId){
        if(agents.containsKey(agentId))
            return agents.get(agentId);
        else
            return null;
    }

    public List<Agent> getAllAgents(){
        List<Agent> agentList = new ArrayList<>();
        for(Integer k : agents.keySet())
            agentList.add(agents.get(k));
        return agentList;
    }
}
