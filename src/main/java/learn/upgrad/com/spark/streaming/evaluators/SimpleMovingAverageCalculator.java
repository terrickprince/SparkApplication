package learn.upgrad.com.spark.streaming.evaluators;

import learn.upgrad.com.kafka.domain.ClosingPriceAverage;
import learn.upgrad.com.kafka.domain.PriceDataResponse;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFlatMapFunction;
import org.apache.spark.streaming.Duration;
import org.apache.spark.streaming.api.java.JavaDStream;
import scala.Tuple2;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SimpleMovingAverageCalculator {
    public static void evaluate(JavaDStream<PriceDataResponse> directKafkaStreamMedia
            , Duration window
            , Duration slidingInterval){

        PairFlatMapFunction<PriceDataResponse, String, ClosingPriceAverage> pairFlatMapFunction = new PairFlatMapFunction<PriceDataResponse, String, ClosingPriceAverage>() {
            @Override
            public Iterator<Tuple2<String, ClosingPriceAverage>> call(PriceDataResponse priceData) throws Exception {
                List<Tuple2<String, ClosingPriceAverage>> lstTuples = new ArrayList<Tuple2<String, ClosingPriceAverage>>();
                lstTuples.add(new Tuple2<String, ClosingPriceAverage>(priceData.getSymbol(),new
                        ClosingPriceAverage(BigDecimal.ONE,priceData.getPriceData().getClose(), BigDecimal.ZERO)));
                return lstTuples.iterator();
            }
        };

        Function2<ClosingPriceAverage, ClosingPriceAverage, ClosingPriceAverage> function = new Function2<ClosingPriceAverage, ClosingPriceAverage, ClosingPriceAverage>() {
            @Override
            public ClosingPriceAverage call(ClosingPriceAverage closingPriceAverage, ClosingPriceAverage closingPriceAverage2) throws Exception {
                closingPriceAverage.setPeriodCount(closingPriceAverage.getPeriodCount()
                        .add(closingPriceAverage2.getPeriodCount()));
                closingPriceAverage.setTotalClosingPrice(closingPriceAverage.getTotalClosingPrice()
                        .add(closingPriceAverage2.getTotalClosingPrice()));
                return closingPriceAverage;
            }
        };
        directKafkaStreamMedia
                .window(window,slidingInterval)
                .flatMapToPair(pairFlatMapFunction)
                .reduceByKey(function)
                .foreachRDD(rdd ->{
                    rdd.foreach(tuple->{
                        System.out.println("Simple Moving average for currency: "+tuple._1()+" is "+tuple._2().getAverageClosingPrice().toString());
                    });
                });
    }
}
