package learn.upgrad.com.kafka.listener;

import learn.upgrad.com.kafka.config.KafkaConfiguration;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.apache.spark.streaming.kafka010.*;

import java.util.Collections;
import java.util.Set;

public class KafkaListener {

    public static JavaDStream getDataStream(JavaStreamingContext jstream, String topicName, String bootstapserver,String groupId){
        Set<String> topics = Collections.singleton(topicName);
        return KafkaUtils
                .createDirectStream(jstream,
                LocationStrategies.PreferConsistent(),
                ConsumerStrategies.Subscribe(topics,KafkaConfiguration.getKafkaProperties(bootstapserver,groupId)))
                .map(stream -> {
                    if(stream.value()!=null){
                        System.out.println(stream.value().toString());
                    }
                    return stream.value();
                })
                .cache();
    }
}
