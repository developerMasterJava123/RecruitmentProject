package xcode.service;

import xcode.models.CurrentCurrencyRate;

public interface CurrencyRatesNbpService {	

	public CurrentCurrencyRate getCurrentCurrencyRate(String codeCurrency);
}
