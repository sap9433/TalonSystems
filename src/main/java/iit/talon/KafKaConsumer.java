package iit.talon;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;


@Component
public class KafKaConsumer {
    private static final Logger logger = LoggerFactory.getLogger(KafKaConsumer.class);
    @Autowired
    KafkaTemplate kafkaTemplate;
    @Value("${diskpath.property}")
    private String diskpath;
    @Value("${otherMachinepath1.property}")
    private String machine1;
    @Value("${otherMachinepath2.property}")
    private String machine2;

    private String[] dataDeserializer(String message) {
        String[] data = message.split("-");
        return data;
    }

    @KafkaListener(topics = "savedata")
    @Transactional
    public void saveDataToDisk(String message) throws IOException{
        String data[] = dataDeserializer(message);
        File file1 = new File(diskpath+"/"+data[0]);
        File file2 = new File(machine1+"/"+data[0]);
        File file3 = new File(machine2+"/"+data[0]);
        file1.getParentFile().mkdirs();
        file2.getParentFile().mkdirs();
        file3.getParentFile().mkdirs();
        FileWriter writer1 = new FileWriter(file1);
        FileWriter writer2 = new FileWriter(file2);
        FileWriter writer3 = new FileWriter(file3);
        try {
            writer1.write(data[1]);
            writer2.write(data[1]);
            writer3.write(data[1]);
        } catch(Exception e){
            logger.info(e.getMessage());
        }finally{
            writer1.close();
            writer2.close();
            writer3.close();
        }
        logger.info(message);
    }
}