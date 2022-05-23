package food_delivery.database;

import food_delivery.database.model.Agent;
import food_delivery.database.model.OrderTable;
import food_delivery.database.repository.AgentRepository;
import food_delivery.database.repository.OrderTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DbController {

    private DbOperations dbOperations;

    @Autowired
    public DbController(AgentRepository agentRepository, OrderTableRepository orderRepository) {
        this.dbOperations = new DbOperations(agentRepository, orderRepository);
    }


    @GetMapping(value = "/reInitialize")
    public ResponseEntity reInitialize(){
        dbOperations.reInitialize();
        return new ResponseEntity(HttpStatus.CREATED);
    }

    //////////////////////////// AGENT APIs //////////////////////////////////////////////
    @PostMapping(value = "/saveAgent", consumes = "application/json", produces = "application/json")
    @ResponseBody public ResponseEntity saveAgent(@RequestBody Agent agent) {
        return dbOperations.saveAgent(agent);
    }

    // WORKING
    @GetMapping("/getAgent/{id}") @ResponseBody
    public ResponseEntity getAgentById(@PathVariable String id){
        return dbOperations.getAgentById(id);
    }

    // WORKING
    @GetMapping("/getAllAgents") @ResponseBody
    public List<Agent> getAllAgents(){
        return dbOperations.getAllAgents();
    }


    @PostMapping(value = "/resetAgent", consumes = "application/json", produces = "application/json")
    @ResponseBody public ResponseEntity resetAgent(@RequestBody Agent agent) {
        return dbOperations.resetAgent();
    }

    @GetMapping("/getLowestIdAgent") @ResponseBody
    public ResponseEntity getLowestIdAgent(){
        return dbOperations.getLowestIdAgent();
    }


    ///////////////////////////// ORDER APIs //////////////////////////////////////////////
    @PostMapping(value = "/saveOrder", consumes = "application/json", produces = "application/json")
    @ResponseBody public ResponseEntity saveAgent(@RequestBody OrderTable order) {
        return dbOperations.saveOrder(order);
    }

    @GetMapping("/getOrder/{id}") @ResponseBody
    public ResponseEntity getOrderById(@PathVariable String id){
        return dbOperations.getOrderById(id);
    }

    @GetMapping("/getAllOrders") @ResponseBody
    public List<OrderTable> getAllOrders(){
        return dbOperations.getAllOrders();
    }

    @PostMapping(value = "/resetOrders", consumes = "application/json", produces = "application/json")
    @ResponseBody public ResponseEntity resetOrders(@RequestBody List<OrderTable> orders) {
        return dbOperations.resetOrder(orders);
    }

    @GetMapping("/generateOrderId") @ResponseBody
    public ResponseEntity generateOrderId(){
        return dbOperations.generateOrderId();
    }

    @GetMapping("/getLowestIdOrder") @ResponseBody
    public ResponseEntity getLowestIdOrder(){
        return dbOperations.getLowestIdOrder();
    }

    //////////////////////////////////// CUSTOM APIs //////////////////////////////////////////////////

    @GetMapping("/add")
    public ResponseEntity add(){
        return dbOperations.add();
    }



} // class ends
