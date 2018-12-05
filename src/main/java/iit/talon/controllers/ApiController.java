package iit.talon.controllers;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.apache.commons.io.IOUtils;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

/**
 * ApiController
 */
@RestController
public class ApiController {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Value("${diskpath.property}")
    private String diskpath;
    @Value("${otherMachinepath1.property}")
    private String machine1;
    @Value("${otherMachinepath1.property}")
    private String machine2;
    @Autowired
    private SimpMessagingTemplate webSocket;

    private String dataFormattor(String data1, String data2) {
        return data1+"-"+data2;
    }
    @RequestMapping(method = RequestMethod.POST,
    consumes = {"application/x-www-form-urlencoded"},
    value = "/api/savedata")
    public String saveData(@RequestParam Map<String, String> savequery){
        String key = savequery.get("key");
        String value = savequery.get("value");
        kafkaTemplate.send("savedata", dataFormattor(key,value));
        return "Data successfully Saved";
    }
    @RequestMapping(method = RequestMethod.POST,
    consumes = {"application/x-www-form-urlencoded"},
    value = "/api/retrievedata")
    public void sendData(@RequestParam Map<String, String> readquery) throws FileNotFoundException, IOException{
        String clientId = readquery.get("clientId");
        String key = readquery.get("key");
        //Delete

        String data[] = {clientId, key};
        BufferedInputStream reader1 = new BufferedInputStream(new FileInputStream(diskpath+"/"+data[1]) );
        BufferedInputStream reader2 = new BufferedInputStream(new FileInputStream(machine1+"/"+data[1]) );
        BufferedInputStream reader3 = new BufferedInputStream(new FileInputStream(machine2+"/"+data[1]) );
        boolean running = true;
        while( running ) {
            if( reader1.available() > 0 && reader2.available() > 0 && reader3.available() > 0) {
            //if( reader1.available() > 0 ) {
                String val1 = IOUtils.toString(reader1, "UTF-8");
                String val2 = IOUtils.toString(reader2, "UTF-8");
                String val3 = IOUtils.toString(reader3, "UTF-8");
                if(val1.equals(val2) && val2.equals(val3)){
                //if(val1.equals(val1)){
                    webSocket.convertAndSend("/topic/backToClient/"+data[0], val1);
                    running = false;
                } else {
                    webSocket.convertAndSend("/topic/backToClient/"+data[0], "Different nodes give separate Data");
                    running = false;
                }
            }
            else {
                try {
                    Thread.sleep(150);
                }
                catch( InterruptedException ex ) {
                    running = false;
                }
            }
        }
        //kafkaTemplate.send("retrievedata", dataFormattor(clientId,key));
    }
    
}