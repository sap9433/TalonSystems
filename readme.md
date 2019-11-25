- ./kafka_2.12-2.3.0/bin/zookeeper-server-start.sh config/zookeeper.properties
- ./kafka_2.12-2.3.0/bin/kafka-server-start.sh ./kafka_2.12-2.3.0/config/server.properties 
- ./kafka_2.12-2.3.0/bin/kafka-server-start.sh ./kafka_2.12-2.3.0/config/server1.properties 


- (console producer) ./kafka_2.12-2.3.0/bin/kafka-console-producer.sh --broker-list localhost:9091,localhost:9091 --topic sapy
- (console consumer) ./kafka_2.12-2.3.0/bin/kafka-console-consumer.sh --bootstrap-server localhost:9091,localhost:9092 --topic sapy --from-beginning


- set enviornment var ,  `SPRING_CONFIG_LOCATION` in intellij to `/Users/diesel/Desktop/Assignments&Coursework/546/TalonSystems/src/main/resources/application-node1.properties`
- ./gradlew bootRun