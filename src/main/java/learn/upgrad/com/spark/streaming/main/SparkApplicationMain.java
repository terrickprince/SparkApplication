package learn.upgrad.com.spark.streaming.main;


import learn.upgrad.com.kafka.domain.PriceDataResponse;
import learn.upgrad.com.kafka.listener.KafkaListener;
import learn.upgrad.com.spark.streaming.evaluators.ProfitableStocksFinder;
import learn.upgrad.com.spark.streaming.evaluators.SimpleMovingAverageCalculator;
import learn.upgrad.com.spark.streaming.evaluators.VolumeEvaluator;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

import java.io.FileNotFoundException;
import java.io.PrintStream;

public class SparkApplicationMain {

    public static void main(String[] args) throws InterruptedException, FileNotFoundException {
        if(args.length!=3){
            System.err.println("Invalid argument list");
            System.exit(1);
        }
        JavaStreamingContext jstream = getJavaStreamingContext();
        saveConsoleText();
        JavaDStream<PriceDataResponse> javaDStream = KafkaListener
                .getDataStream(jstream,args[1],args[0]+":9092",args[2]);
        SimpleMovingAverageCalculator.evaluate(javaDStream,Durations.minutes(10),Durations.minutes(5));
        ProfitableStocksFinder.evaluate(javaDStream,Durations.minutes(10),Durations.minutes(5));
        VolumeEvaluator.evaluate(javaDStream,Durations.minutes(10));
        jstream.start();
        jstream.awaitTermination();
        jstream.stop();
    }

    private static JavaStreamingContext getJavaStreamingContext() {
        SparkConf sparkConf = new SparkConf().setAppName("stockevaluator").setMaster("local[*]");
        JavaSparkContext jsc = new JavaSparkContext(sparkConf);
        jsc.setCheckpointDir("checkpoint-dir/");
        JavaStreamingContext jstream = new JavaStreamingContext(jsc, Durations.minutes(1));
        jstream.sparkContext().setLogLevel("WARN");
        return jstream;
    }

    private static void saveConsoleText() throws FileNotFoundException {
        PrintStream fileOut = new PrintStream("./console.txt");
        PrintStream fileError = new PrintStream("./error.txt");
        System.setOut(fileOut);
        System.setErr(fileError);
    }
}
