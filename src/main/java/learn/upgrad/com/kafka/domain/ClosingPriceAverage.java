package learn.upgrad.com.kafka.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class ClosingPriceAverage implements Serializable {
    public ClosingPriceAverage(BigDecimal periodCount, BigDecimal totalClosingPrice, BigDecimal totalOpeningPrice) {
        this.periodCount = periodCount;
        this.totalClosingPrice = totalClosingPrice;
        this.totalOpeningPrice = totalOpeningPrice;
    }

    public BigDecimal getPeriodCount() {
        return periodCount;
    }

    public void setPeriodCount(BigDecimal periodCount) {
        this.periodCount = periodCount;
    }

    public BigDecimal getTotalClosingPrice() {
        return totalClosingPrice;
    }

    public void setTotalClosingPrice(BigDecimal totalClosingPrice) {
        this.totalClosingPrice = totalClosingPrice;
    }

    private BigDecimal periodCount;
    private BigDecimal totalClosingPrice;

    public BigDecimal getTotalOpeningPrice() {
        return totalOpeningPrice;
    }

    public void setTotalOpeningPrice(BigDecimal totalOpeningPrice) {
        this.totalOpeningPrice = totalOpeningPrice;
    }

    private BigDecimal totalOpeningPrice;

    public BigDecimal getAverageClosingPrice(){
        return this.getTotalClosingPrice().divide(this.getPeriodCount(),5, RoundingMode.CEILING);
    }

    public BigDecimal getAverageOpeningPrice(){
        return this.getTotalOpeningPrice().divide(this.getPeriodCount(),5, RoundingMode.CEILING);
    }

    public BigDecimal getPriceIncrease(){
        return this.getAverageClosingPrice().subtract(this.getAverageOpeningPrice());
    }
}
