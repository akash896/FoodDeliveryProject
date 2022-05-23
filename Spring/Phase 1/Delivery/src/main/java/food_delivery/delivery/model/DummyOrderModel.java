package food_delivery.delivery.model;

public class DummyOrderModel {
    private int custId;
    private int restId;
    private int itemId;
    private float qty;

    public DummyOrderModel(int custId, int restId, int itemId, float qty) {
        this.custId = custId;
        this.restId = restId;
        this.itemId = itemId;
        this.qty = qty;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public int getRestId() {
        return restId;
    }

    public void setRestId(int restId) {
        this.restId = restId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public float getQty() {
        return qty;
    }

    public void setQty(float qty) {
        this.qty = qty;
    }

    @Override
    public String toString() {
        return "DummyOrderModel{" +
                "custId=" + custId +
                ", restId=" + restId +
                ", itemId=" + itemId +
                ", qty=" + qty +
                '}';
    }
} // class ends
