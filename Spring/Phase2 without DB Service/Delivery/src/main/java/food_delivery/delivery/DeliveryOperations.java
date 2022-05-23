package food_delivery.delivery;

import com.fasterxml.jackson.databind.ObjectMapper;
import food_delivery.delivery.model.*;
import food_delivery.delivery.repository.AgentRepository;
import food_delivery.delivery.repository.OrderTableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class DeliveryOperations {

    private DbOperations dbOperations;
    private RestaurantOperations restaurantOperations;
    @Autowired
    private Environment env;
    private String deliveryApp;
    private String restaurantApp;
    private String walletApp;

    public DeliveryOperations(AgentRepository agentRepository, OrderTableRepository orderRepository, Environment env){
        this.dbOperations = new DbOperations(agentRepository, orderRepository);
        this.env = env;
        this.deliveryApp = env.getProperty("deliveryApp");
        this.restaurantApp = env.getProperty("restaurantApp");
        this.walletApp = env.getProperty("walletApp");
        this.restaurantOperations = new RestaurantOperations(env);
    }

    public synchronized ResponseEntity reInitialize() {
        dbOperations.reInitialize();
        return new ResponseEntity(HttpStatus.CREATED);
    }

    public synchronized ResponseEntity requestOrder(DummyOrderModel dummyOrderModel) {
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
        OrderNumber orderNumber = dbOperations.generateOrderId();
        Integer orderId = orderNumber.getOrderId();
//        System.out.println("Order ID ==================" + orderId);
        OrderTable order = new OrderTable(orderId, -1, dummyOrderModel.getCustId(), dummyOrderModel.getRestId(), dummyOrderModel.getItemId(),
                dummyOrderModel.getQty(), "unassigned");
//        orderQueue.add(order);
//        orderDB.createOrder(order);
        dbOperations.saveOrder(order);
//        orderId++;
//        Agent agent = restaurantOperations.getLowestIdAgent(agentDB);
        Agent agent = dbOperations.getLowestIdAgent();
        OrderTable lowestOrder = dbOperations.getLowestIdOrder();
        if(lowestOrder!=null && agent!=null){
            OrderTable unassignedOrder = order;
            unassignedOrder.setStatus("assigned");
            unassignedOrder.setAgentId(agent.getAgentId());
            agent.setStatus("unavailable");
            agent.setOrderId(unassignedOrder.getOrderId());
//            agentDB.createAgent(agent);
//            orderDB.createOrder(unassignedOrder);
            dbOperations.saveOrder(order);
            dbOperations.saveAgent(agent);
        }
        return new ResponseEntity(orderNumber, HttpStatus.CREATED);
    }

    public synchronized ResponseEntity agentSignIn(AgentNumber agentNumber) {
        Integer agentId = agentNumber.getAgentId();
        Agent agent = null;
        agent = dbOperations.getAgentById(String.valueOf(agentId));
        if(agent == null)
            new ResponseEntity(HttpStatus.CREATED);
        if(agent.getStatus().equals("signed-out")){
            agent.setStatus("available");
            dbOperations.saveAgent(agent);
            OrderTable order = dbOperations.getLowestIdOrder();
            //System.out.println("Lowest Order ============== "+ order.toString());
            if(order==null)
                return new ResponseEntity(HttpStatus.CREATED);
            else{ // there exist some order that is in unassigned status
                agent = dbOperations.getLowestIdAgent();
                //System.out.println("Lowest Agent ======" + agent.toString());
                if(agent == null)
                    return new ResponseEntity(HttpStatus.CREATED);
                OrderTable unassignedOrder = order;
                unassignedOrder.setStatus("assigned");
                unassignedOrder.setAgentId(agentId);
                agent.setStatus("unavailable");
                agent.setOrderId(unassignedOrder.getOrderId());
//                agentDB.createAgent(agent);
                dbOperations.saveAgent(agent);
                //deliveryOrderMap.put(unassignedOrder.getOrderId(), unassignedOrder);
//                orderDB.createOrder(unassignedOrder);
                dbOperations.saveOrder(unassignedOrder);
                //System.out.println("Updated agent = "+ agent.toString());
                //System.out.println("Updated Order = "+unassignedOrder.toString());
            }
        }
        return new ResponseEntity(HttpStatus.CREATED);
    }

    public synchronized ResponseEntity agentSignOut(AgentNumber agentNumber) {
        int agentId = agentNumber.getAgentId();
        //System.out.println("AgentNumber = "+agentNumber.toString());
        Agent agent = new Agent(1,-1,"test");
        agent = dbOperations.getAgentById(String.valueOf(agentNumber.getAgentId()));
        if(agent == null)
            return new ResponseEntity(HttpStatus.CREATED);
        if(agent.getStatus().equals("available"))
            agent.setStatus("signed-out");
        dbOperations.saveAgent(agent);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    public synchronized ResponseEntity orderDelivered(OrderNumber orderNumber) {
        Integer orderId = orderNumber.getOrderId();
        OrderTable order = dbOperations.getOrderById(String.valueOf(orderId));
        if(order == null)
            return new ResponseEntity(HttpStatus.CREATED);
        OrderTable unassignedOrder = order;
        if(unassignedOrder.getStatus().equals("assigned")){
            Integer agentId = unassignedOrder.getAgentId();
            Agent agent = dbOperations.getAgentById(String.valueOf(agentId));
            agent.setStatus("available"); // making that agent as available as order is delivered
            unassignedOrder.setStatus("delivered");
//            agentDB.createAgent(agent);
            dbOperations.saveAgent(agent);
//            orderDB.createOrder(unassignedOrder);
            dbOperations.saveOrder(unassignedOrder);

            // there exist some order which can be assigned to an agent
            agent = dbOperations.getLowestIdAgent();
            order = dbOperations.getLowestIdOrder();
            if(order!=null && agent != null){
                unassignedOrder = order;
                unassignedOrder.setStatus("assigned");
                unassignedOrder.setAgentId(agent.getAgentId());
                agent.setStatus("unavailable");
                agent.setOrderId(unassignedOrder.getOrderId());
//                agentDB.createAgent(agent);
                dbOperations.saveAgent(agent);
//                orderDB.createOrder(unassignedOrder);
                dbOperations.saveOrder(unassignedOrder);

            }
        }
        return new ResponseEntity(HttpStatus.CREATED);
    }

    public ResponseEntity getOrderById(String id) {
        Integer orderId = Integer.parseInt(id);
        OrderTable order = dbOperations.getOrderById(id);
        //System.out.println("Order = "+ order.toString());
        if(order == null)
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        else {
            DummyGetOrder dummyGetOrder = new DummyGetOrder(order.getOrderId(), order.getStatus(), order.getAgentId());
            return new ResponseEntity(dummyGetOrder, HttpStatus.OK);
        }
    }

    public ResponseEntity getAgentById(String id) {
        int agentId = Integer.parseInt(id);
        Agent agent = dbOperations.getAgentById(id);
        if(agent == null)
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        DummyGetAgent dummyGetAgent = new DummyGetAgent(agentId, agent.getStatus());
        return new ResponseEntity(dummyGetAgent, HttpStatus.OK);
    }

    public ResponseEntity getCustomerById(String id){
        CustomerWithBalance customer = new CustomerWithBalance();
        String URI = walletApp+"balance/" + id;
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> entity = restTemplate.getForEntity(URI, String.class);
            ObjectMapper mapper = new ObjectMapper();
            String body = entity.getBody();
            customer = mapper.readValue(body, CustomerWithBalance.class);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity(customer, HttpStatus.CREATED);
    }

    ////////////////////////////////  EXTRA AGENT METHODS ////////////////////////////////////////////////
//
//    public boolean reInitializeDatabase() {
//        String URI = "http://localhost:8083/reInitialize";
//        RestTemplate restTemplate = new RestTemplate();
//        try {
//            ResponseEntity<String> response = restTemplate.getForEntity(URI, String.class);
//            MediaType contentType = response.getHeaders().getContentType();
//            HttpStatus statusCode = response.getStatusCode();
//            if (response.getStatusCode().toString().indexOf("201") != -1) {
//                return true;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//
//    public boolean updateAgent(Agent agent){
////        String URI = "http://host.docker.internal:8083/saveAgent";
//        String URI = "http://localhost:8083/saveAgent";
//        RestTemplate restTemplate = new RestTemplate();
//        HttpEntity<Agent> request = new HttpEntity<>(agent);
//        try {
//            ResponseEntity<String> response = restTemplate.exchange(URI, HttpMethod.POST, request, String.class);
//            MediaType contentType = response.getHeaders().getContentType();
//            HttpStatus statusCode = response.getStatusCode();
//            if (response.getStatusCode().toString().indexOf("201") != -1) {
//                return true;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    public Agent getAgentfromDB(String id){
//        Agent agent = null;
////        String URI = "http://host.docker.internal:8083/getAgent/"+id;
//        String URI = "http://localhost:8083/getAgent/"+id;
//        RestTemplate restTemplate = new RestTemplate();
//        try {
////        String response = restTemplate.getForObject(URI,  String.class);
//            ResponseEntity<String> entity = restTemplate.getForEntity(URI, String.class);
//            ObjectMapper mapper = new ObjectMapper();
//            String body = entity.getBody();
//            //System.out.println("BODY = "+ body);
//            agent = mapper.readValue(body, Agent.class);
//            //System.out.println("Agent received ======= " + agent.toString());
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//        return agent;
//    }
//
////    public Agent getLowestIdAgent(AgentDB agentDB){
////        Agent agent = null;
////        List<Agent> agentList = agentDB.getAllAgents();
////        for(Agent a : agentList){
////            if(agent == null && a.getStatus().equals("available"))
////                agent = a;
////            if(agent!=null && a.getStatus().equals("available")){
////                if(a.getAgentId() < agent.getAgentId())
////                    agent = a;
////            }
////        }
////        return agent;
////    }
//
//    public Agent getLowestIdAgentFromDB(){
//        Agent agent = null;
//        String URI = "http://localhost:8083/getLowestIdAgent";
//        RestTemplate restTemplate = new RestTemplate();
//        try {
////        String response = restTemplate.getForObject(URI,  String.class);
//            ResponseEntity<String> entity = restTemplate.getForEntity(URI, String.class);
//            ObjectMapper mapper = new ObjectMapper();
//            String body = entity.getBody();
//            //System.out.println("BODY = "+ body);
//            if(body == null)
//                return null;
//            agent = mapper.readValue(body, Agent.class);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return agent;
//    }
//
//
//    ///////////////////////// EXTRA ORDER METHODS ////////////////////////////////////////////////////
//
//    public boolean resetOrder(List<OrderTable> orderList) {
////        String URI = "http://host.docker.internal:8083/resetOrder";
//        String URI = "http://localhost:8083/resetOrder";
//        RestTemplate restTemplate = new RestTemplate();
//        HttpEntity<List<OrderTable>> request = new HttpEntity<>(orderList);
//        try {
//            ResponseEntity<String> response = restTemplate.exchange(URI, HttpMethod.POST, request, String.class);
//            MediaType contentType = response.getHeaders().getContentType();
//            HttpStatus statusCode = response.getStatusCode();
//            if (response.getStatusCode().toString().indexOf("201") != -1) {
//                return true;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    public boolean updateOrder(OrderTable order){
////        String URI = "http://host.docker.internal:8083/saveOrder";
//        String URI = "http://localhost:8083/saveOrder";
//        RestTemplate restTemplate = new RestTemplate();
//        HttpEntity<OrderTable> request = new HttpEntity<>(order);
//        try {
//            ResponseEntity<String> response = restTemplate.exchange(URI, HttpMethod.POST, request, String.class);
//            MediaType contentType = response.getHeaders().getContentType();
//            HttpStatus statusCode = response.getStatusCode();
//            if (response.getStatusCode().toString().indexOf("201") != -1) {
//                return true;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return false;
//    }
//
//    public OrderTable getOrderfromDB(Integer orderId){
//        OrderTable order = null;
////        String URI = "http://host.docker.internal:8083/getOrder/"+String.valueOf(orderId);
//        String URI = "http://localhost:8083/getOrder/"+String.valueOf(orderId);
//        RestTemplate restTemplate = new RestTemplate();
//        try {
////        String response = restTemplate.getForObject(URI,  String.class);
//            ResponseEntity<String> entity = restTemplate.getForEntity(URI, String.class);
//            ObjectMapper mapper = new ObjectMapper();
//            String body = entity.getBody();
////            System.out.println("BODY = "+ body);
//            if(body == null)
//                return null;
//            order = mapper.readValue(body, OrderTable.class);
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//        return order;
//    }
//
////    public Order getLowestIdOrder(OrderDB orderDB){
////        Order order = null;
////        List<Order> orderList = orderDB.getAllOrders();
////        for(Order a : orderList){
////            if(order == null)
////                order = a;
////            else{
////                if(a.getOrderId() < order.getOrderId())
////                    order = a;
////            }
////        }
////        return order;
////    }
//
//    public OrderNumber generateOrderIdFromDB() {
//        OrderNumber orderNumber = null;
//        String URI = "http://localhost:8083/generateOrderId";
//        RestTemplate restTemplate = new RestTemplate();
//        try {
////        String response = restTemplate.getForObject(URI,  String.class);
//            ResponseEntity<String> entity = restTemplate.getForEntity(URI, String.class);
//            ObjectMapper mapper = new ObjectMapper();
//            String body = entity.getBody();
//            //System.out.println("BODY = "+ body);
//            orderNumber = mapper.readValue(body, OrderNumber.class);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return orderNumber;
//    }
//
//    public OrderTable getLowestIdOrderFromDB(){
//        OrderTable order = null;
//        String URI = "http://localhost:8083/getLowestIdOrder";
//        RestTemplate restTemplate = new RestTemplate();
//        try {
////        String response = restTemplate.getForObject(URI,  String.class);
//            ResponseEntity<String> entity = restTemplate.getForEntity(URI, String.class);
//            ObjectMapper mapper = new ObjectMapper();
//            String body = entity.getBody();
////            System.out.println("BODY = "+ body);
//            if(body == null) {
////                System.out.println("returning null");
//                return null;
//            }
//            order = mapper.readValue(body, OrderTable.class);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return order;
//    }
//
//    ///////////////////////////// CUSTOM Functions ////////////////////////////////////////////
//
//    public @ResponseBody
//    List<Agent> showAgents() {
//        List<Agent> agentList = new ArrayList<>();
////        String URI = "http://host.docker.internal:8083/getAgent/"+id;
//        String URI = "http://localhost:8083/getAllAgents";
//        RestTemplate restTemplate = new RestTemplate();
//        try {
////        String response = restTemplate.getForObject(URI,  String.class);
//            ResponseEntity<String> entity = restTemplate.getForEntity(URI, String.class);
//            ObjectMapper mapper = new ObjectMapper();
//            String body = entity.getBody();
////            System.out.println("BODY = "+ body);
//            agentList = (List<Agent>) mapper.readValue(body, Agent.class);
//            //System.out.println("Agent received ======= " + agent.toString());
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//        return agentList;
//    }
//
////    public @ResponseBody List<OrderTable> showOrders() {
////        return orderDB.getAllOrders();
////    }



} // class ends
