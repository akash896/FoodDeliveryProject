package food_delivery.delivery.model;

import java.io.Serializable;

public class RestaurantOrderModel implements Serializable {

    private int restId;
    private int itemId;
    private float qty;

    public RestaurantOrderModel(){}

    public RestaurantOrderModel(int restId, int itemId, float qty) {
        this.restId = restId;
        this.itemId = itemId;
        this.qty = qty;
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
        return "RestaurantOrderModel{" +
                "restId=" + restId +
                ", itemId=" + itemId +
                ", qty=" + qty +
                '}';
    }

} // class ends
