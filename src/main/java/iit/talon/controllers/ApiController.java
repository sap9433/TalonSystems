package iit.talon.controllers;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * ApiController
 */
@RestController
public class ApiController {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Value("${diskpath.property}")
    private String diskpath;

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
    public void sendData(@RequestParam Map<String, String> readquery) {
        String clientId = readquery.get("clientId");
        String key = readquery.get("key");
        kafkaTemplate.send("retrievedata", dataFormattor(clientId,key));
    }
    
}