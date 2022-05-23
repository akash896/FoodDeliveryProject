package food_delivery.delivery.repository;

import food_delivery.delivery.model.Agent;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface AgentRepository extends Repository<Agent, Integer> {
    void save(Agent agent);

    Agent findById(Integer agentId);

    List<Agent> findAll();

    public void deleteAll();

}
