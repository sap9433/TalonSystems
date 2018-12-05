package iit.talon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;


@Component
public class KafKaConsumer {
    @Autowired
    private SimpMessagingTemplate webSocket;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    private static final Logger logger = LoggerFactory.getLogger(KafKaConsumer.class);

    @Value("${diskpath.property}")
    private String diskpath;

   @KafkaListener(topics = "savedata")
    public void listen(String message) throws Exception{
        logger.info("Received Messasge: " + diskpath);
        kafkaTemplate.send("writeack", "Data Saved");
    }

    @KafkaListener(topics = "retrievedata")
    public void processSound(String message) {
        webSocket.convertAndSend("/topic/backToClient", message);
        logger.info(message);
    }

    @KafkaListener(topics = "writeack")
    public void sendWriteAck(String message) throws Exception{
        logger.info("Message Successfully written: " + message);
        webSocket.convertAndSend("/topic/writeack", message);
    }
}