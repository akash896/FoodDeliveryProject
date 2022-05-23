package food_delivery.restaurant.utility;


import food_delivery.restaurant.model.Item;
import food_delivery.restaurant.db.ItemDB;
import food_delivery.restaurant.db.RestaurantDB;
import food_delivery.restaurant.model.Restaurant;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataUtils {

    public RestaurantDB initializeRestaurant(){
        RestaurantDB restaurantDB = new RestaurantDB();
        ItemDB itemDB = new ItemDB();
        List<Restaurant> restaurants = new ArrayList<>();
        try
        {
            File file=new File("/initialData.txt");    //creates a new file instance
//            File file = new File("src/main/resources/initialData.txt");
            FileReader fr=new FileReader(file);   //reads the file
            BufferedReader br=new BufferedReader(fr);  //creates a buffering character input stream
            StringBuffer sb=new StringBuffer();    //constructs a string buffer with no characters
            String line;

            while(!(line=br.readLine()).equals("****")){
                String fields[] = line.split(" ");
                int numOfItems = Integer.parseInt(fields[1]);
                int restId = Integer.parseInt(fields[0]);
                // System.out.println("RestId = "+ restId);
                List<Item> itemList = new ArrayList<>();

                // reading the Items present in each Restaurants
                itemDB = new ItemDB();
                for(int i=0; i<numOfItems; i++){
                    String itemsFields[] = br.readLine().split(" ");
                    int itemId = Integer.parseInt(itemsFields[0]);
                    float price = Float.parseFloat(itemsFields[1]);
                    float quantity = Float.parseFloat(itemsFields[2]);
                    Item createdItem = new Item(itemId, price, quantity);
                    // System.out.println("Item = "+createdItem.toString());
                    itemDB.createItem(createdItem); // list of item under one Restaurant
                }
                Restaurant createdRestaurant = new Restaurant(restId, itemDB);
                restaurantDB.createRestaurant(createdRestaurant);
            }

            fr.close();    //closes the stream and release the resources
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return restaurantDB;
    }

}// class ends
