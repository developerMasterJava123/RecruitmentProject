package xcode.service;

import java.math.BigDecimal;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import xcode.exceptions.InvalidNbpResponseException;
import xcode.models.CurrentCurrencyRate;
import xcode.models.ExchangeRateResponse;

@Service
public class CurrencyRatesNbpServiceImpl implements CurrencyRatesNbpService {
	
	private final String URL_NBP_EXCHANGE_RATE;
	
	private final RestTemplate restTemplate;
	
	@Autowired
	public CurrencyRatesNbpServiceImpl(@Value("${currency.rate.nbp.api}") String urlNbpExchangeRate, RestTemplate restTemplate) {
			URL_NBP_EXCHANGE_RATE = urlNbpExchangeRate;
	        this.restTemplate = restTemplate;
	}

	private List<ExchangeRateResponse> getExchangeRateResponse() {
		return restTemplate.exchange(
			 URL_NBP_EXCHANGE_RATE,
			 HttpMethod.GET,
			 null,
			 new ParameterizedTypeReference<List<ExchangeRateResponse>>(){}).getBody();
	}
	
	private BigDecimal getCurrentCurrencyFromExchangeRateResponse(String codeCurrency)  {
		List<ExchangeRateResponse> response = getExchangeRateResponse();
		if(response != null && !response.isEmpty() && response.get(0).getRates() != null){
			return response.get(0).getRates().stream()
					.filter(s-> s.getCode() != null &&  s.getMid() != null &&  s.getCode().equals(codeCurrency))
					.map(ExchangeRateResponse.Rate::getMid)
					.findFirst()
					.orElseThrow(() -> new InvalidNbpResponseException("Response not contains searched current currency rate value"));	
		} else {
			throw new  InvalidNbpResponseException("Response not contains rates");
		}	
	}
	
	@Override
	public CurrentCurrencyRate getCurrentCurrencyRate(String codeCurrency) {
		BigDecimal value = getCurrentCurrencyFromExchangeRateResponse(codeCurrency);
		return new CurrentCurrencyRate(value);
	}
}