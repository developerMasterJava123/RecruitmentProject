package xcode.models;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CurrentCurrencyRate {
	
	private BigDecimal value;

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}
	
	public CurrentCurrencyRate(BigDecimal value) {
        this.value = value.setScale(4, RoundingMode.HALF_UP);
    }

	public CurrentCurrencyRate() {
	}	
}
