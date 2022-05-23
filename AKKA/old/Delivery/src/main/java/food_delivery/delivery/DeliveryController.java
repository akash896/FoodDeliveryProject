package food_delivery.delivery;

import food_delivery.delivery.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class DeliveryController {
    private DeliveryOperations deliveryOperations;

    public DeliveryController() {  // constructor
        this.deliveryOperations = new DeliveryOperations();
    }

    // WORKING
    @PostMapping(value = "/reInitialize")
    public synchronized ResponseEntity reInitialize(){
        return deliveryOperations.reInitialize();
    }

    @PostMapping(value = "/requestOrder", consumes = "application/json", produces = "application/json")
    @ResponseBody public ResponseEntity requestOrder(@RequestBody DummyOrderModel dummyOrderModel) {
        return deliveryOperations.requestOrder(dummyOrderModel);
    }


    @PostMapping(value = "/agentSignIn", consumes = "application/json", produces = "application/json")
    @ResponseBody public synchronized ResponseEntity agentSignIn(@RequestBody AgentNumber agentNumber) {
        return deliveryOperations.agentSignIn(agentNumber);
    }

    // WORKING
    @PostMapping(value = "/agentSignOut", consumes = "application/json", produces = "application/json")
    @ResponseBody public synchronized ResponseEntity agentSignOut(@RequestBody AgentNumber agentNumber) {
        return deliveryOperations.agentSignOut(agentNumber);
    }

    @PostMapping(value = "/orderDelivered", consumes = "application/json", produces = "application/json")
    @ResponseBody public synchronized ResponseEntity orderDelivered(@RequestBody OrderNumber orderNumber) {
        return deliveryOperations.orderDelivered(orderNumber);
    }

    @GetMapping("/order/{id}") @ResponseBody
    public synchronized ResponseEntity getOrderById(@PathVariable String id) {
        return deliveryOperations.getOrderById(id);
    }

    // WORKING
    @GetMapping("/agent/{id}") @ResponseBody
    public synchronized ResponseEntity getAgentById(@PathVariable String id) {
        return deliveryOperations.getAgentById(id);
    }

    ///////////////////////////// CUSTOM APIs ///////////////////////////////////////////////

//    // custom mapping to show all Agents present
//    @RequestMapping(path = "/showAgents", produces = MediaType.APPLICATION_JSON_VALUE)
//    public @ResponseBody List<Agent> showAgents() {
//        return deliveryOperations.showAgents();
//    }
//
//    // custom mapping to show all Orders present
//    @RequestMapping(path = "/showOrders", produces = MediaType.APPLICATION_JSON_VALUE)
//    public @ResponseBody List<Order> showOrders() {
//        return deliveryOperations.showOrders();
//    }
//
//    // WORKING
//    @PostMapping(value = "/updateAgent", consumes = "application/json", produces = "application/json")
//    @ResponseBody public ResponseEntity updateAgent(@RequestBody Agent agent) {
//        boolean res = deliveryOperations.updateAgent(agent);
//        if(res == true)
//            return new ResponseEntity(HttpStatus.CREATED);
//        else
//            return new ResponseEntity(HttpStatus.GONE);
//    }

} // class ends


