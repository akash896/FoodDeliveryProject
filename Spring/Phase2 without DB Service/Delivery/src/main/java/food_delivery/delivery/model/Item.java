package food_delivery.delivery.model;

import java.io.Serializable;

public class Item implements Serializable {

    private int itemId;
    private float price;
    private float qty;

    public Item(){}

    public Item(int itemId, float price, float qty) {
        this.itemId = itemId;
        this.price = price;
        this.qty = qty;
    }

    public int getItemId() { return itemId; }

    public void setItemId(int itemId) { this.itemId = itemId; }

    public float getPrice() { return price; }

    public void setPrice(float price) { this.price = price; }

    public float getQty() { return qty; }

    public void setQty(float qty) { this.qty = qty; }

    @Override
    public String toString() {
        return "Item{" +
                "itemId=" + itemId +
                ", price=" + price +
                ", quantity=" + qty +
                '}';
    }


}
