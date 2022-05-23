package food_delivery.delivery.model;


import food_delivery.delivery.db.ItemDB;

import java.io.Serializable;

public class Restaurant implements Serializable {


    private int restId;
    private ItemDB itemDB;

    public Restaurant(){}

    public Restaurant(int restId, ItemDB itemDB){
        this.restId = restId;
        this.itemDB = itemDB;
    }

    public int getRestId() { return restId; }

    public void setRestId(int restId) { this.restId = restId; }

    public ItemDB getItemDB() { return itemDB; }

    public void setItemDB(ItemDB itemDB) { this.itemDB = itemDB; }


    @Override
    public String toString() {
        return "Restaurant{" +
                "restId=" + restId +
                ", itemDB=" + itemDB.toString() +
                '}';
    }
}
