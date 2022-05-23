package food_delivery.wallet.utility;

import food_delivery.wallet.Model.Customer;
import food_delivery.wallet.db.CustomerDB;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataUtils {

    public CustomerDB initializeCustomer(){
        CustomerDB customerDB = new CustomerDB();
        int starLine = 0;
        float balance = 0;
        List<Integer> custIDs = new ArrayList<>();
        try
        {
            File file=new File("/initialData.txt");    //creates a new file instance
//            File file = new File("src/main/resources/initialData.txt");
            FileReader fr=new FileReader(file);   //reads the file
            BufferedReader br=new BufferedReader(fr);  //creates a buffering character input stream
            StringBuffer sb=new StringBuffer();    //constructs a string buffer with no characters
            String line;
            while((line=br.readLine())!=null)
            {
                if(line.equals("****")) {
                    starLine++;
                    continue;
                }
                if(starLine==2){
                    custIDs.add(Integer.parseInt(line));
                }
                if(starLine==3){
                    balance = Float.parseFloat(line);
                }
            }
            for(Integer id : custIDs){
                Customer customer = new Customer(id, balance);
                customerDB.createCustomer(customer);
                //System.out.println(customer.toString());
            }
            fr.close();    //closes the stream and release the resources
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return customerDB;
    }

}// class ends
