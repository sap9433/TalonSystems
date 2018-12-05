package iit.talon.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 * ApiController
 */
@RestController
public class ApiController {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @RequestMapping("/api/savedata")
    public String saveData() {
        kafkaTemplate.send("savedata", "Some random data");
        return "Data Saved";
    }
    
    @RequestMapping("/api/retrievedata")
    public void sendData() {
        kafkaTemplate.send("retrievedata", "Some random data");
    }
    
}