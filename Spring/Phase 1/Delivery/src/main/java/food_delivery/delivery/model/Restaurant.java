package food_delivery.delivery.model;


import food_delivery.delivery.db.ItemDB;

public class Restaurant {
    private int restId;
    private ItemDB itemDB;

    public Restaurant(int restId, ItemDB itemDB){
        this.restId = restId;
        this.itemDB = itemDB;
    }

    public int getRestId() { return restId; }

    public void setRestId(int restId) { this.restId = restId; }

    public ItemDB getItemDB() { return itemDB; }

    public void setItemDB(ItemDB itemDB) { this.itemDB = itemDB; }

}  // class ends
