package learn.upgrad.com.kafka.deserializer;

import com.fasterxml.jackson.core.type.TypeReference;
import learn.upgrad.com.kafka.domain.PriceDataResponse;
import org.apache.kafka.common.serialization.Deserializer;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

public class PriceDataDeserializer implements Deserializer<PriceDataResponse> {

    public void configure(Map<String, ?> map, boolean b) {

    }

    public static boolean isJSONValid(String jsonInString ) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            mapper.readTree(jsonInString);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public PriceDataResponse deserialize(String s, byte[] bytes) {
            ObjectMapper mapper = new ObjectMapper();
            PriceDataResponse priceDataResponses = null;
            try {
                TypeReference<PriceDataResponse> mapType = new TypeReference<PriceDataResponse>() {};
                priceDataResponses=mapper.readValue(bytes,mapType);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return priceDataResponses;
    }

    public void close() {

    }
}
