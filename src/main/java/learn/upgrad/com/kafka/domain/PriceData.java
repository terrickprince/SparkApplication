package learn.upgrad.com.kafka.domain;

import java.io.Serializable;
import java.math.BigDecimal;

public class PriceData implements Serializable {
	private BigDecimal volume;
	private BigDecimal high;
	private BigDecimal low;
	private BigDecimal close;
	private BigDecimal open;

	public void setVolume(BigDecimal volume){
		this.volume = volume;
	}

	public BigDecimal getVolume(){
		return volume;
	}

	public void setHigh(BigDecimal high){
		this.high = high;
	}

	public BigDecimal getHigh(){
		return high;
	}

	public void setLow(BigDecimal low){
		this.low = low;
	}

	public BigDecimal getLow(){
		return low;
	}

	public void setClose(BigDecimal close){
		this.close = close;
	}

	public BigDecimal getClose(){
		return close;
	}

	public void setOpen(BigDecimal open){
		this.open = open;
	}

	public BigDecimal getOpen(){
		return open;
	}
}
