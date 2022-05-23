package food_delivery.database.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class OrderTable implements Serializable{

    @Id
    private Integer orderId;
    @Column(name = "AGENTID", nullable = false)
    private Integer agentId;
    @Column(name = "CUSTID", nullable = false)
    private Integer custId;
    @Column(name = "RESTID", nullable = false)
    private Integer restId;
    @Column(name = "ITEMID", nullable = false)
    private Integer itemId;
    @Column(name = "QTY", nullable = false)
    private Float qty;
    @Column(name = "STATUS", nullable = false)
    private String status;

    public OrderTable(){}

    public OrderTable(Integer orderId, Integer agentId, Integer custId, Integer restId, Integer itemId, Float qty, String status) {
        this.orderId = orderId;
        this.agentId = agentId;
        this.custId = custId;
        this.restId = restId;
        this.itemId = itemId;
        this.qty = qty;
        this.status = status;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getAgentId() {
        return agentId;
    }

    public void setAgentId(Integer agentId) {
        this.agentId = agentId;
    }

    public Integer getCustId() {
        return custId;
    }

    public void setCustId(Integer custId) {
        this.custId = custId;
    }

    public Integer getRestId() {
        return restId;
    }

    public void setRestId(Integer restId) {
        this.restId = restId;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Float getQty() {
        return qty;
    }

    public void setQty(Float qty) {
        this.qty = qty;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", agentId=" + agentId +
                ", custId=" + custId +
                ", restId=" + restId +
                ", itemId=" + itemId +
                ", qty=" + qty +
                ", status='" + status + '\'' +
                '}';
    }
} // class ends
