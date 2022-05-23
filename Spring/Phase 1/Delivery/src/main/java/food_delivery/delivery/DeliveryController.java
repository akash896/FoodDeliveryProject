package food_delivery.delivery;

import food_delivery.delivery.db.AgentDB;
import food_delivery.delivery.db.OrderDB;
import food_delivery.delivery.db.RestaurantDB;
import food_delivery.delivery.model.*;
import food_delivery.delivery.utility.DataUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class DeliveryController {
    AgentDB agentDB;
    OrderDB orderDB;
    RestaurantDB restaurantDB;
    RestaurantOperations restaurantOperations;
    private Integer orderId;
    Queue<Order> orderQueue; // contains orders that are in unassigned status
    //Queue<Agent> availableAgentQueue;

    public DeliveryController() {
        this.orderDB = new OrderDB();
        this.agentDB = new AgentDB();
        restaurantDB = new RestaurantDB();
        restaurantOperations = new RestaurantOperations();
        this.orderId = 1000;
        orderQueue = new LinkedList<Order>();
        //availableAgentQueue = new LinkedList<>();
    }

    @PostMapping(value = "/reInitialize")
    public ResponseEntity reInitialize() {
        DataUtils dataUtils = new DataUtils();
        agentDB = dataUtils.initializeAgents();
        restaurantDB = dataUtils.initializeRestaurant();
        orderId = 1000;
        orderDB = new OrderDB();
        restaurantOperations = new RestaurantOperations();
        orderQueue = new LinkedList<Order>();
        return new ResponseEntity(HttpStatus.CREATED);
    }

//    // custom mapping to show all Agents present
//    @RequestMapping(path = "/showAgents", produces = MediaType.APPLICATION_JSON_VALUE)
//    public @ResponseBody List<Agent> showAgents() {
//        return agentDB.getAllAgents();
//    }
//
//    // custom mapping to show all Orders present
//    @RequestMapping(path = "/showOrders", produces = MediaType.APPLICATION_JSON_VALUE)
//    public @ResponseBody List<Order> showOrders() {
//        return orderDB.getAllOrders();
//    }

    
    @PostMapping(value = "/requestOrder", consumes = "application/json", produces = "application/json")
    @ResponseBody public ResponseEntity requestOrder(@RequestBody DummyOrderModel dummyOrderModel) {
        // check if customer have enough balance to order that item
        boolean balancePresent = restaurantOperations.checkBalancePresent(dummyOrderModel.getCustId(), dummyOrderModel.getRestId(),
                dummyOrderModel.getItemId(), dummyOrderModel.getQty());
        boolean acceptOrder = false;
        if(balancePresent == true) {
            acceptOrder = restaurantOperations.checkAcceptOrder(dummyOrderModel.getRestId(), dummyOrderModel.getItemId(),
                    dummyOrderModel.getQty());
            if(acceptOrder == false){
                restaurantOperations.creditBalance(dummyOrderModel.getCustId(), dummyOrderModel.getRestId(),
                        dummyOrderModel.getItemId(), dummyOrderModel.getQty());
            }
        }
        if(balancePresent == false || acceptOrder==false){
            return new ResponseEntity(HttpStatus.GONE);
        }
        Order order = new Order(orderId, -1, dummyOrderModel.getCustId(), dummyOrderModel.getRestId(), dummyOrderModel.getItemId(),
                                dummyOrderModel.getQty(), "unassigned");
        orderQueue.add(order);
        orderDB.createOrder(order);
        orderId++;
        Agent agent = restaurantOperations.getLowestIdAgent(agentDB);
        if(!orderQueue.isEmpty() && agent!=null){
            Order unassignedOrder = orderQueue.poll();
            unassignedOrder.setStatus("assigned");
            unassignedOrder.setAgentId(agent.getAgentId());
            agent.setStatus("unavailable");
            agent.setOrderId(unassignedOrder.getOrderID());
            agentDB.createAgent(agent);
            orderDB.createOrder(unassignedOrder);
        }
        OrderNumber orderNumber = new OrderNumber(orderId-1);
        return new ResponseEntity(orderNumber, HttpStatus.CREATED);
    }

    @PostMapping(value = "/agentSignIn", consumes = "application/json", produces = "application/json")
    @ResponseBody public ResponseEntity agentSignIn(@RequestBody AgentNumber agentNumber) {
        int agentId = agentNumber.getAgentId();
        Agent agent = agentDB.getAgent(agentId);
        if(agentDB.getAgent(agentId) == null)
            new ResponseEntity(HttpStatus.CREATED);
        if(agent.getStatus().equals("signed-out")){
            agent.setStatus("available");
            if(orderQueue.isEmpty())
                return new ResponseEntity(HttpStatus.CREATED);
            else{ // there exist some order that is in unassigned status
                agent = restaurantOperations.getLowestIdAgent(agentDB);
                Order unassignedOrder = orderQueue.poll();
                unassignedOrder.setStatus("assigned");
                unassignedOrder.setAgentId(agentId);
                agent.setStatus("unavailable");
                agent.setOrderId(unassignedOrder.getOrderID());
                agentDB.createAgent(agent);
                //deliveryOrderMap.put(unassignedOrder.getOrderID(), unassignedOrder);
                orderDB.createOrder(unassignedOrder);
            }
        }
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PostMapping(value = "/agentSignOut", consumes = "application/json", produces = "application/json")
    @ResponseBody public ResponseEntity agentSignOut(@RequestBody AgentNumber agentNumber) {
        int agentId = agentNumber.getAgentId();
        Agent agent = agentDB.getAgent(agentId);
        if(agent == null)
            return new ResponseEntity(HttpStatus.CREATED);
        if(agent.getStatus().equals("available"))
            agent.setStatus("signed-out");
        agentDB.createAgent(agent);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @PostMapping(value = "/orderDelivered", consumes = "application/json", produces = "application/json")
    @ResponseBody public ResponseEntity orderDelivered(@RequestBody OrderNumber orderNumber) {
        int orderId = orderNumber.getOrderId();
        if(orderDB.getOrder(orderId) == null)
            return new ResponseEntity(HttpStatus.CREATED);
        Order unassignedOrder = orderDB.getOrder(orderId);
        if(unassignedOrder.getStatus().equals("assigned")){
            int agentId = unassignedOrder.getAgentId();
            Agent agent = agentDB.getAgent(agentId);
            agent.setStatus("available"); // making that agent as available as order is delivered
            unassignedOrder.setStatus("delivered");
            agentDB.createAgent(agent);
            orderDB.createOrder(unassignedOrder);

            // there exist some order which can be assigned to an agent
            agent = restaurantOperations.getLowestIdAgent(agentDB);
            if(!orderQueue.isEmpty() && agent != null){
                unassignedOrder = orderQueue.poll();
                unassignedOrder.setStatus("assigned");
                unassignedOrder.setAgentId(agent.getAgentId());
                agent.setStatus("unavailable");
                agent.setOrderId(unassignedOrder.getOrderID());
                agentDB.createAgent(agent);
                orderDB.createOrder(unassignedOrder);
            }

        }
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @GetMapping("/order/{id}") @ResponseBody
    public ResponseEntity getOrderById(@PathVariable String id) {
        int orderId = Integer.parseInt(id);
        if(orderDB.getOrder(orderId) == null)
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        else {
            Order order = orderDB.getOrder(orderId);
            DummyGetOrder dummyGetOrder = new DummyGetOrder(order.getOrderID(), order.getStatus(), order.getAgentId());
            return new ResponseEntity(dummyGetOrder, HttpStatus.OK);
        }
    }

    @GetMapping("/agent/{id}") @ResponseBody
    public ResponseEntity getAgentById(@PathVariable String id) {
        int agentId = Integer.parseInt(id);
        Agent agent = agentDB.getAgent(agentId);
        if(agent == null)
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        DummyGetAgent dummyGetAgent = new DummyGetAgent(agentId, agent.getStatus());
        return new ResponseEntity(dummyGetAgent, HttpStatus.OK);
    }
    
} // class ends


