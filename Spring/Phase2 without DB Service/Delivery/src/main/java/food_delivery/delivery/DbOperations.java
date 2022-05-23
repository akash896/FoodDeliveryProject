package food_delivery.delivery;

import com.fasterxml.jackson.databind.ObjectMapper;
import food_delivery.delivery.model.*;
import food_delivery.delivery.repository.AgentRepository;
import food_delivery.delivery.repository.OrderTableRepository;
import food_delivery.delivery.utility.DataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class DbOperations {

    private final AgentRepository agentRepository;
    private final OrderTableRepository orderRepository;
    private DataUtils dataUtils;
    private OrderNumber orderNumber;

    @Autowired
    public DbOperations(AgentRepository agentRepository, OrderTableRepository orderRepository) {
        this.agentRepository = agentRepository;
        this.orderRepository = orderRepository;
        this.dataUtils = new DataUtils();
        orderNumber = new OrderNumber(1000);
        resetAgent();
    }

    @Transactional
    public synchronized ResponseEntity reInitialize() {
        System.out.println("Inside init");

        agentRepository.deleteAll();
        orderRepository.deleteAll();
        orderNumber.setOrderId(1000);
        resetAgent();
        System.out.println("Completed init");
        return new ResponseEntity(HttpStatus.CREATED);
    }

    ////////////////// AGENT METHODS ////////////////////////////////////////////
    // save agent to DB
    @Transactional
    public synchronized ResponseEntity saveAgent(Agent agent) {
        try {
            agentRepository.save(agent);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity(agent, HttpStatus.CREATED);
    }

    // get agent from DB
    @Transactional
    public Agent getAgentById(String id){
        Integer agentId = Integer.parseInt(id);
        Agent agent = null;
        try {
            agent = agentRepository.findById(agentId);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return agent;
    }

    // get all agents from DB
    @Transactional
    public @ResponseBody List<Agent> getAllAgents(){
        List<Agent> agentList = null;
        try {
            agentList = agentRepository.findAll();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return agentList;
    }

    @Transactional
    public ResponseEntity resetAgent(){
        for(Agent agentItrator : dataUtils.getAgentList()){
            System.out.println("Agent = " + agentItrator);
            agentRepository.save(agentItrator);
        }
        return new ResponseEntity(HttpStatus.CREATED);
    }

    @Transactional
    public synchronized Agent getLowestIdAgent(){
        Agent agent = null;
        List<Agent> agentList = getAllAgents();
        for(Agent a : agentList){
            if(agent == null && a.getStatus().equals("available"))
                agent = a;
            if(agent!=null && a.getStatus().equals("available")){
                if(a.getAgentId() < agent.getAgentId())
                    agent = a;
            }
        }
        return agent;
    }

    //////////////////////// ORDER METHODS //////////////////////////////////
    // save order to DB
    @Transactional
    public synchronized ResponseEntity saveOrder(OrderTable orderTable) {
        System.out.println("OrderTable = "+orderTable.toString());
        try {
            //orderTable.setOrderId(orderNumber.getOrderId()-1);
            orderRepository.save(orderTable);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity(orderTable, HttpStatus.CREATED);
    }

    // get order from DB
    @Transactional
    public synchronized OrderTable getOrderById(String id){
        Integer orderId = Integer.parseInt(id);
        OrderTable order = null;
        try {
            order = orderRepository.findById(orderId);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return order;
    }

    // get all orders from DB
    @Transactional
    public @ResponseBody List<OrderTable> getAllOrders(){
        List<OrderTable> orders = null;
        try {
            orders = orderRepository.findAll();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return orders;
    }

    @Transactional
    public @ResponseBody ResponseEntity resetOrder(List<OrderTable> orders){
        for(OrderTable orderItr : orders){
            orderRepository.save(orderItr);
        }
        return new ResponseEntity(HttpStatus.CREATED);
    }


    public synchronized OrderNumber generateOrderId() {
        OrderNumber obj = new OrderNumber(orderNumber.getOrderId());
        orderNumber.setOrderId(orderNumber.getOrderId()+1);
        return obj;
    }

    @Transactional
    public synchronized OrderTable getLowestIdOrder(){
        OrderTable order = null;
        List<OrderTable> orderList = getAllOrders();
        if(orderList == null)
            return null;
        for(OrderTable a : orderList){
            if(order == null && a.getStatus().equals("unassigned")) {
                order = a;
                continue;
            }
            if(order!=null && a.getStatus().equals("unassigned")){
                if(a.getOrderId() < order.getOrderId())
                    order = a;
            }
        }
        return order;
    }


    ///////////////////////////// CUSTOM METHODS ////////////////////////////////////////////////////////

    @Transactional
    public ResponseEntity add(){
        Agent agent = new Agent(201, 1000, "unassigned");
        try {
            agentRepository.save(agent);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity("Agent added to DB", HttpStatus.OK);
    }


} // class ends
