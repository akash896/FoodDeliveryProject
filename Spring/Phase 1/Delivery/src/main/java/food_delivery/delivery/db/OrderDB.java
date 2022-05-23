package food_delivery.delivery.db;

import food_delivery.delivery.model.Order;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDB {

    private Map<Integer, Order> orders;

    public OrderDB() {
        this.orders = new HashMap<>();
    }

    public void createOrder(Order order){
        orders.put(order.getOrderID(), order);
    }

    public Order getOrder(int orderId){
        if(orders.containsKey(orderId))
            return orders.get(orderId);
        else
            return null;
    }


    public List<Order> getAllOrders(){
        List<Order> orderList = new ArrayList<>();
        for(Integer k : orders.keySet())
            orderList.add(orders.get(k));
        return orderList;
    }

} // class ends
