package learn.upgrad.com.spark.streaming.evaluators;

import learn.upgrad.com.kafka.domain.PriceDataResponse;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFlatMapFunction;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import scala.Tuple2;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class VolumeEvaluator {
    public static void evaluate(JavaDStream<PriceDataResponse> directKafkaStreamMedia
            , Duration window){
        PairFlatMapFunction<PriceDataResponse, String, BigDecimal> pair = new PairFlatMapFunction<PriceDataResponse, String, BigDecimal>() {
            @Override
            public Iterator<Tuple2<String, BigDecimal>> call(PriceDataResponse priceData) throws Exception {
                List<Tuple2<String, BigDecimal>> lstTuples = new ArrayList<Tuple2<String, BigDecimal>>();
                lstTuples.add(new Tuple2<String, BigDecimal>(priceData.getSymbol(), priceData.getPriceData().getVolume().abs()));
                return lstTuples.iterator();
            }
        };
        Function2<BigDecimal, BigDecimal, BigDecimal> function = new Function2<BigDecimal, BigDecimal, BigDecimal>() {
            @Override
            public BigDecimal call(BigDecimal bigDecimal, BigDecimal bigDecimal2) throws Exception {
                return bigDecimal.add(bigDecimal);
            }
        };
        directKafkaStreamMedia
                .window(window,window)
                .flatMapToPair(pair)
                .reduceByKey(function)
                .mapToPair(s -> new Tuple2<>(s._2,s._1))
                .foreachRDD(rdd ->{
                    if(!rdd.isEmpty()){
                        rdd.sortByKey(BigDecimal::compareTo,false);
                        System.out.println("High volume stock trading now is : "+rdd.first()._2+" with consolidated volume "+rdd.first()._1.toString());
                    }
        });
    }
}
