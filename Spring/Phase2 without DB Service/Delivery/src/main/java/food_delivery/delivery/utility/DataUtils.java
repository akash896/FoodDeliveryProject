package food_delivery.delivery.utility;

import food_delivery.delivery.model.Agent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataUtils {

    public DataUtils(){}

    public List<Agent> getAgentList(){
        List<Agent> agentList = new ArrayList<>();
        int starLine = 0;
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
                if(starLine==1) {
                    int agentId = Integer.parseInt(line);
                    agentList.add(new Agent(agentId, -1, "signed-out"));
                }
            }
            fr.close();    //closes the stream and release the resources
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }
        return agentList;
    }

}// class ends
