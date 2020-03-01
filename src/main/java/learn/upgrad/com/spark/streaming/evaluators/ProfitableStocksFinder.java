package learn.upgrad.com.spark.streaming.evaluators;

import learn.upgrad.com.kafka.domain.ClosingPriceAverage;
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
import java.util.TreeMap;

public class ProfitableStocksFinder {
    public static void evaluate(JavaDStream<PriceDataResponse> directKafkaStreamMedia
            , Duration window
            , Duration slidingInterval) {
        TreeMap<Double,String> hashMap = new TreeMap<>();
        PairFlatMapFunction<PriceDataResponse, String, ClosingPriceAverage> pair = new PairFlatMapFunction<PriceDataResponse, String, ClosingPriceAverage>() {
            @Override
            public Iterator<Tuple2<String, ClosingPriceAverage>> call(PriceDataResponse priceData) throws Exception {
                List<Tuple2<String, ClosingPriceAverage>> lstTuples = new ArrayList<Tuple2<String, ClosingPriceAverage>>();
                lstTuples.add(new Tuple2<String, ClosingPriceAverage>(priceData.getSymbol(), new
                        ClosingPriceAverage(BigDecimal.ONE, priceData.getPriceData().getClose(), priceData.getPriceData().getOpen())));
                return lstTuples.iterator();
            }
        };
        Function2<ClosingPriceAverage, ClosingPriceAverage, ClosingPriceAverage> function = new Function2<ClosingPriceAverage, ClosingPriceAverage, ClosingPriceAverage>() {
            @Override
            public ClosingPriceAverage call(ClosingPriceAverage closingPriceAverage, ClosingPriceAverage closingPriceAverage2) throws Exception {
                closingPriceAverage
                        .setPeriodCount(closingPriceAverage.getPeriodCount().add(closingPriceAverage2
                                .getPeriodCount()));
                closingPriceAverage
                        .setTotalClosingPrice(closingPriceAverage.getTotalClosingPrice().add(closingPriceAverage2
                                .getTotalClosingPrice()));
                closingPriceAverage
                        .setTotalOpeningPrice(closingPriceAverage.getTotalOpeningPrice().add(closingPriceAverage2
                                .getTotalOpeningPrice()));
                return closingPriceAverage;
            }
        };
        directKafkaStreamMedia
                .window(window, slidingInterval)
                .flatMapToPair(pair)
                .reduceByKey(function)
                .mapToPair(s -> new Tuple2<>(s._2.getPriceIncrease(),s._1))
               .foreachRDD(rdd ->{
                   if(!rdd.isEmpty()){
                       rdd.sortByKey(BigDecimal::compareTo,false);
                       System.out.println("Profitable stock is : "+rdd.first()._2+" with delta between opening and closing price "+rdd.first()._1.toString());
                   }
               });
    }
}
