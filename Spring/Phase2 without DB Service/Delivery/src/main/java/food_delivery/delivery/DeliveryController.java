package food_delivery.delivery;

import food_delivery.delivery.model.AgentNumber;
import food_delivery.delivery.model.DummyOrderModel;
import food_delivery.delivery.model.OrderNumber;
import food_delivery.delivery.repository.AgentRepository;
import food_delivery.delivery.repository.OrderTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class DeliveryController {

    private DbOperations dbOperations;
    DeliveryOperations deliveryOperations;

    @Autowired
    public DeliveryController(AgentRepository agentRepository, OrderTableRepository orderRepository, Environment env) {
        this.deliveryOperations = new DeliveryOperations(agentRepository, orderRepository, env);

    }

    // WORKING
    @PostMapping(value = "/reInitialize")
    public ResponseEntity reInitialize(){
        return deliveryOperations.reInitialize();
    }

    @PostMapping(value = "/requestOrder", consumes = "application/json", produces = "application/json")
    @ResponseBody public ResponseEntity requestOrder(@RequestBody DummyOrderModel dummyOrderModel) {
        return deliveryOperations.requestOrder(dummyOrderModel);
    }


    @PostMapping(value = "/agentSignIn", consumes = "application/json", produces = "application/json")
    @ResponseBody public ResponseEntity agentSignIn(@RequestBody AgentNumber agentNumber) {
        return deliveryOperations.agentSignIn(agentNumber);
    }

    // WORKING
    @PostMapping(value = "/agentSignOut", consumes = "application/json", produces = "application/json")
    @ResponseBody public ResponseEntity agentSignOut(@RequestBody AgentNumber agentNumber) {
        return deliveryOperations.agentSignOut(agentNumber);
    }

    @PostMapping(value = "/orderDelivered", consumes = "application/json", produces = "application/json")
    @ResponseBody public ResponseEntity orderDelivered(@RequestBody OrderNumber orderNumber) {
        return deliveryOperations.orderDelivered(orderNumber);
    }

    @GetMapping("/order/{id}") @ResponseBody
    public ResponseEntity getOrderById(@PathVariable String id) {
        return deliveryOperations.getOrderById(id);
    }

    // WORKING
    @GetMapping("/agent/{id}") @ResponseBody
    public ResponseEntity getAgentById(@PathVariable String id) {
        return deliveryOperations.getAgentById(id);
    }



    ///////////////////////////// CUSTOM APIs ///////////////////////////////////////////////

    @GetMapping("/getCustomer/{id}") @ResponseBody
    public ResponseEntity getCustomerById(@PathVariable String id) {
        return deliveryOperations.getCustomerById(id);
    }




//    // custom mapping to show all Agents present
//    @RequestMapping(path = "/showAgents", produces = MediaType.APPLICATION_JSON_VALUE)
//    public @ResponseBody List<Agent> showAgents() {
//        return deliveryOperations.showAgents();
//    }

//    // custom mapping to show all Orders present
//    @RequestMapping(path = "/showOrders", produces = MediaType.APPLICATION_JSON_VALUE)
//    public @ResponseBody List<Order> showOrders() {
//        return deliveryOperations.showOrders();
//    }

    // WORKING
//    @PostMapping(value = "/updateAgent", consumes = "application/json", produces = "application/json")
//    @ResponseBody public ResponseEntity updateAgent(@RequestBody Agent agent) {
//        boolean res = deliveryOperations.updateAgent(agent);
//        if(res == true)
//            return new ResponseEntity(HttpStatus.CREATED);
//        else
//            return new ResponseEntity(HttpStatus.GONE);
//    }


//
//    @GetMapping(value = "/reInitialize")
//    public ResponseEntity reInitialize(){
//        dbOperations.reInitialize();
//        return new ResponseEntity(HttpStatus.CREATED);
//    }
//
//    //////////////////////////// AGENT APIs //////////////////////////////////////////////
//    @PostMapping(value = "/saveAgent", consumes = "application/json", produces = "application/json")
//    @ResponseBody public ResponseEntity saveAgent(@RequestBody Agent agent) {
//        return dbOperations.saveAgent(agent);
//    }
//
//    // WORKING
//    @GetMapping("/getAgent/{id}") @ResponseBody
//    public ResponseEntity getAgentById(@PathVariable String id){
//        return dbOperations.getAgentById(id);
//    }
//
//    // WORKING
//    @GetMapping("/getAllAgents") @ResponseBody
//    public List<Agent> getAllAgents(){
//        return dbOperations.getAllAgents();
//    }
//
//
//    @PostMapping(value = "/resetAgent", consumes = "application/json", produces = "application/json")
//    @ResponseBody public ResponseEntity resetAgent(@RequestBody Agent agent) {
//        return dbOperations.resetAgent();
//    }
//
//    @GetMapping("/getLowestIdAgent") @ResponseBody
//    public ResponseEntity getLowestIdAgent(){
//        return dbOperations.getLowestIdAgent();
//    }
//
//
//    ///////////////////////////// ORDER APIs //////////////////////////////////////////////
//    @PostMapping(value = "/saveOrder", consumes = "application/json", produces = "application/json")
//    @ResponseBody public ResponseEntity saveAgent(@RequestBody OrderTable order) {
//        return dbOperations.saveOrder(order);
//    }
//
//    @GetMapping("/getOrder/{id}") @ResponseBody
//    public ResponseEntity getOrderById(@PathVariable String id){
//        return dbOperations.getOrderById(id);
//    }
//
//    @GetMapping("/getAllOrders") @ResponseBody
//    public List<OrderTable> getAllOrders(){
//        return dbOperations.getAllOrders();
//    }
//
//    @PostMapping(value = "/resetOrders", consumes = "application/json", produces = "application/json")
//    @ResponseBody public ResponseEntity resetOrders(@RequestBody List<OrderTable> orders) {
//        return dbOperations.resetOrder(orders);
//    }
//
//    @GetMapping("/generateOrderId") @ResponseBody
//    public ResponseEntity generateOrderId(){
//        return dbOperations.generateOrderId();
//    }
//
//    @GetMapping("/getLowestIdOrder") @ResponseBody
//    public ResponseEntity getLowestIdOrder(){
//        return dbOperations.getLowestIdOrder();
//    }
//
//    //////////////////////////////////// CUSTOM APIs //////////////////////////////////////////////////
//
//    @GetMapping("/add")
//    public ResponseEntity add(){
//        return dbOperations.add();
//    }



} // class ends
