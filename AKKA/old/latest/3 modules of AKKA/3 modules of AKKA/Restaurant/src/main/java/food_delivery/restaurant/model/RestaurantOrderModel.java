package food_delivery.restaurant.model;

public class RestaurantOrderModel {
    private int restId;
    private int itemId;
    private float qty;

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
                ", quantity=" + qty +
                '}';
    }
}
