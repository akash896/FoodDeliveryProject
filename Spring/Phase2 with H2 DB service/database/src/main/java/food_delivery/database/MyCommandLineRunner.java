package food_delivery.database;

import food_delivery.database.model.Agent;
import food_delivery.database.repository.AgentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;

public class MyCommandLineRunner implements CommandLineRunner {
    private AgentRepository agentRepository;

    @Autowired
    public MyCommandLineRunner(AgentRepository agentRepository){
        this.agentRepository = agentRepository;
    }
    @Override
    public void run(String... args) throws Exception {
        Agent agent = new Agent(201, 1000, "unassigned");
        this.agentRepository.save(agent);

    }
}
