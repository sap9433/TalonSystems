package iit.talon;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;


@Component
public class KafKaConsumer {
    @Autowired
    private SimpMessagingTemplate webSocket;
    private static final Logger logger = LoggerFactory.getLogger(KafKaConsumer.class);

    @Value("${diskpath.property}")
    private String diskpath;
    @Value("${otherMachinepath1.property}")
    private String machine1;
    @Value("${otherMachinepath1.property}")
    private String machine2;

    private String[] dataDeserializer(String message) {
        String[] data = message.split("-");
        return data;
    }

    @KafkaListener(topics = "retrievedata", groupId = "coordinator")
    public void retrieveValue(String message) throws FileNotFoundException, IOException{
        String data[] = dataDeserializer(message);
        BufferedInputStream reader1 = new BufferedInputStream(new FileInputStream(diskpath+"/"+data[1]) );
        // BufferedInputStream reader2 = new BufferedInputStream(new FileInputStream(machine1+"/"+data[1]) );
        // BufferedInputStream reader3 = new BufferedInputStream(new FileInputStream(machine2+"/"+data[1]) );
        boolean running = true;
        while( running ) {
            //if( reader1.available() > 0 && reader2.available() > 0 && reader3.available() > 0) {
            if( reader1.available() > 0 ) {
                String val1 = IOUtils.toString(reader1, "UTF-8");
                // String val2 = IOUtils.toString(reader2, "UTF-8");
                // String val3 = IOUtils.toString(reader3, "UTF-8");
                //if(val1.equals(val2) && val2.equals(val3)){
                if(val1.equals(val1)){
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
        logger.info(message);
    }

    @KafkaListener(topics = "savedata", groupId = "${diskpath.property}")
    public void saveDataToDisk(String message) throws IOException{
        String data[] = dataDeserializer(message);
        File file = new File(diskpath+"/"+data[0]);
        file.getParentFile().mkdirs();
        FileWriter writer = new FileWriter(file);
        try {
            writer.write(data[1]);
        } catch(Exception e){
            logger.info(e.getMessage());
        }finally{
            writer.close();
        }
        logger.info(message);
    }
}