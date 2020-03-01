package learn.upgrad.com.kafka.config;

import learn.upgrad.com.kafka.deserializer.PriceDataDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.HashMap;
import java.util.Map;

public class KafkaConfiguration {
    private KafkaConfiguration(){}
    public static Map<String, Object> getKafkaProperties(String bootstrapServer,String groupId){
        Map<String, Object> kafkaParams = new HashMap<>();
        kafkaParams.put("bootstrap.servers", bootstrapServer);
        kafkaParams.put("value.deserializer", PriceDataDeserializer.class);
        kafkaParams.put("key.deserializer", StringDeserializer.class);
        kafkaParams.put("group.id", groupId);
        kafkaParams.put("auto.offset.reset", "latest");
        kafkaParams.put("enable.auto.commit", true);
        return kafkaParams;
    }
}
