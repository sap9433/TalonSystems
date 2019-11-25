package iit.talon.controllers;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ApiController
 */
@RestController
public class ApiController {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Value("${diskpath.property}")
    private String diskpath;
    private static final Logger logger = LoggerFactory.getLogger(ApiController.class);

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


    @RequestMapping(value = "/api/retrievedata")
    public SseEmitter sendData(@RequestParam Map<String, String> readquery) {
        String clientId = readquery.get("clientid");
        String key = readquery.get("key");
        SseEmitter emitter = new SseEmitter();
        ExecutorService sseMvcExecutor = Executors.newWorkStealingPool();
        sseMvcExecutor.execute(() -> {
            try {
                for (int i = 0; true; i++) {
                    String val1 = "null";
                    try {
                        BufferedInputStream reader1 = new BufferedInputStream(new FileInputStream(diskpath + "/" + key));
                        val1 = IOUtils.toString(reader1, "UTF-8");
                    }catch (Exception e){

                    }
                    SseEmitter.SseEventBuilder event = SseEmitter.event()
                            .data(val1)
                            .id(String.valueOf(i))
                            .name("/topic/backToClient/"+clientId+key);
                    emitter.send(event);
                    Thread.sleep(1000);
                }
            } catch (Exception ex) {
                emitter.completeWithError(ex);
            }
        });
        return emitter;
    }
}