import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;


public class sample {

    private static String filePath = "/initialData.txt";

    public static void main(String[] args) {
        sample s = new sample();
        // s.firstAttempt();
        s.secondAttempt();
    }    

    void secondAttempt() {
        HashMap<Integer, Restaurant> restaurants = new HashMap<>();
        filePath = "/Users/Phoenix/IISc/Sem 02/PoDS/Projects/Assignments/Phase 01/restaurant" + filePath;
        try { 
            File f = new File(filePath);
            if (!f.exists()) {
                System.out.println("File Not Found: " + filePath);
            }

            //Creating a Scanner object
            Scanner sc = new Scanner(f);
            //StringBuffer to store the contents
            StringBuffer sb = new StringBuffer();
            //Appending each line to the buffer

            int fileParts = 0;
            while(sc.hasNext())
                {
                    if (sc.hasNext("\\*\\*\\*\\*")) {
                        sc.next();
                        fileParts++;
                    }

                    switch (fileParts) {
                        case 0: {
                            Restaurant rest = new Restaurant();
                            rest.setId(sc.nextInt());
                            int itemCount = sc.nextInt();
                            rest.setnumItems(itemCount);

                            while (itemCount > 0) {
                                Item item = new Item();
                                item.setId(sc.nextInt());
                                item.setPrice(sc.nextInt());
                                item.setQty(sc.nextInt());
                                rest.setItemById(item);
                                itemCount--;
                            }
                            restaurants.put(rest.getId(), rest);
                            System.out.println(rest.toString());
                        } break;

                        case 1: {
                            DeliveryAgent agent = new DeliveryAgent();
                            agent.setAgentId(sc.nextInt());
                            // agentsStatus.add(agent);
                            // agents.put(agent.getAgentId(), agent);

                        }   break;

                        case 2: {
                            System.out.println(sc.nextInt());
                        }   break;
                        
                        default: sc.next();
                    }
                }
                // Collections.sort(agentsStatus);

        } catch(Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }

    void firstAttempt() {
        HashMap<Integer, Restaurant> restaurants = new HashMap<>();
        filePath = "/Users/Phoenix/IISc/Sem 02/PoDS/Projects/Assignments/Phase 01/restaurant" + filePath;
        try {
            File f = new File(filePath);
            if (!f.exists()) {
                System.out.println("File Not Found: " + filePath);
            }
            FileReader fr = new FileReader(f);    
            BufferedReader br=new BufferedReader(fr);
            String line;  
            while((line=br.readLine())!=null)
            {
                System.out.println("Initial : " + line);
                line.trim();
                if (line.equalsIgnoreCase("****"))
                    break;
        
                String[] arrOfStr = line.split(" ");
                Restaurant rest = new Restaurant();
                rest.setId(Integer.parseInt(arrOfStr[0].trim()));
                rest.setnumItems(Integer.parseInt(arrOfStr[1].trim()));
                for (int i=0; i<rest.getnumItems(); i++) {
                    line = br.readLine();
                    System.out.println("Rest : " + line);
                    line.trim();
                    arrOfStr = line.split(" ");
                    Item item = new Item();
                    item.setId(Integer.parseInt(arrOfStr[0].trim()));
                    item.setPrice(Integer.parseInt(arrOfStr[1].trim()));
                    item.setQty(Integer.parseInt(arrOfStr[2].trim()));
                    rest.setItemById(item);
                }
                System.out.println("End : " + rest.getItems().size());
                restaurants.put(rest.getId(), rest);
                System.out.println(restaurants.size());
            }
            fr.close();    //closes the stream and release the resources  

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}

class Restaurant {

    int id, numItems;
    HashMap<Integer, Item> itemsMap = new HashMap<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getnumItems() {
        return numItems;
    }

    public void setnumItems(int numOfItems) {
        this.numItems = numOfItems;
    }

    public HashMap<Integer, Item> getItems() {
        return itemsMap;
    }

    public void setItems(HashMap<Integer, Item> items) {
        this.itemsMap= items;
    }

    public Item getItemById(int itemId) {
        return itemsMap.getOrDefault(itemId, null);
    }

    public void setItemById(Item item) {
        itemsMap.put(item.getId(), item);
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer(super.toString());
        sb.append(System.getProperty("line.separator"))
            .append("Restaurant Id: ").append(getId())
            .append(System.getProperty("line.separator"))
            .append("itemCount: ").append(getnumItems())
            .append(System.getProperty("line.separator"))
            .append("Items : ").append(System.getProperty("line.separator"));

        for (Map.Entry<Integer, Item> e: itemsMap.entrySet()) {
            sb.append("id: ").append(e.getKey())
                .append("price: ").append(((Item)e.getValue()).getPrice())
                .append("qty: ").append(((Item)e.getValue()).getQty()).append(System.getProperty("line.separator"));
        }

        return sb.toString();
    }
    
    
}

class Item {
    
    int id, price, qty;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

}
