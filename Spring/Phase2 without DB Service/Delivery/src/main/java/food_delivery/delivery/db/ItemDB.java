package food_delivery.delivery.db;


import food_delivery.delivery.model.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemDB {

    private Map<Integer, Item> items;

    public ItemDB(){
        this.items =new HashMap<>();
    }

    public void createItem(Item item){
        items.put(item.getItemId(), item);
    }

    public Item getItem(int itemId){
        if(items.containsKey(itemId))
            return items.get(itemId);
        else
            return null;
    }

    public List<Item> getAllItems(){
        List<Item> itemList = new ArrayList<>();
        for(Integer k : items.keySet())
            itemList.add(items.get(k));
        return itemList;
    }

}
