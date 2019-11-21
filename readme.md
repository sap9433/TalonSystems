- bin/zookeeper-server-start.sh config/zookeeper.properties
-  bin/kafka-server-start.sh config/server.properties 
- bin/kafka-server-start.sh config/server1.properties 

- bin/kafka-console-producer.sh --broker-list localhost:9091,localhost:9091 --topic sapy
- bin/kafka-console-consumer.sh --bootstrap-server localhost:9091,localhost:9092 --topic sapy --from-beginning

- (console producer) bin/kafka-console-producer.sh --broker-list localhost:9091,localhost:9091 --topic sapy
- (console consumer) bin/kafka-console-consumer.sh --bootstrap-server localhost:9091,localhost:9092 --topic sapy --from-beginning


- set enviornment var ,  `SPRING_CONFIG_LOCATION` in intellij to `/Users/diesel/Desktop/Assignments&Coursework/546/TalonSystems/src/main/resources/application-node1.properties`
- ./gradlew bootRun