package learn.upgrad.com.kafka.domain;

import java.io.Serializable;

public class PriceDataResponse implements Serializable {
	private String symbol;
	private PriceData priceData;
	private String timestamp;

	public void setSymbol(String symbol){
		this.symbol = symbol;
	}

	public String getSymbol(){
		return symbol;
	}

	public void setPriceData(PriceData priceData){
		this.priceData = priceData;
	}

	public PriceData getPriceData(){
		return priceData;
	}

	public void setTimestamp(String timestamp){
		this.timestamp = timestamp;
	}

	public String getTimestamp(){
		return timestamp;
	}

	@Override
 	public String toString(){
		return 
			"{" +
			"symbol = '" + symbol + '\'' +
					"PriceData{" +
					"volume = '" + this.getPriceData().getVolume() + '\'' +
					",high = '" + this.getPriceData().getHigh() + '\'' +
					",low = '" + this.getPriceData().getLow() + '\'' +
					",close = '" + this.getPriceData().getClose() + '\'' +
					",open = '" + this.getPriceData().getOpen() + '\'' +
					"}"+
			",timestamp = '" + timestamp+
			"}";
		}
}
