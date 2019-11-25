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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


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
    public void saveDataToDisk(String message) {
        String data[] = dataDeserializer(message);
        List<String> servers = new ArrayList (Arrays.asList(diskpath, machine1, machine2));
        servers.parallelStream().forEach((server) -> {
            File file1 = new File(server+"/"+data[0]);
            file1.getParentFile().mkdirs();
            FileWriter writer1 = null;
            try {
                writer1 = new FileWriter(file1);
                writer1.write(data[1]);
                writer1.close();
            }catch(Exception e){
                logger.info(e.getMessage());
            }
        });
    }
}