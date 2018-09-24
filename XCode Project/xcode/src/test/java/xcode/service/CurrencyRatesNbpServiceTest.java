package xcode.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import xcode.exceptions.InvalidNbpResponseException;
import xcode.models.CurrentCurrencyRate;
import xcode.models.ExchangeRateResponse;
import xcode.service.CurrencyRatesNbpService;
import xcode.service.CurrencyRatesNbpServiceImpl;


@RunWith(MockitoJUnitRunner.class)
public class CurrencyRatesNbpServiceTest {
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Mock
	private RestTemplate mockRestTemplate;

	private CurrencyRatesNbpService service;
	
	private List<ExchangeRateResponse>  exchangeRateResponseList;
	private ExchangeRateResponse exchangeEurRateResponse;
 
	@Before
	public void setUp() {
		exchangeRateResponseList =  new ArrayList<>();
		exchangeEurRateResponse = new ExchangeRateResponse();
	    service = new CurrencyRatesNbpServiceImpl("", mockRestTemplate);
	}

	@Test
	public void shouldThrowInvalidNbpResponseExceptionWhenRatesIsNull() {
		exchangeEurRateResponse.setRates(null);
		exchangeRateResponseList.add(exchangeEurRateResponse);
		
		ResponseEntity<List<ExchangeRateResponse>> response = new ResponseEntity<>(exchangeRateResponseList, HttpStatus.OK);		
		Mockito.when(mockRestTemplate.exchange(anyString(), 
				any(HttpMethod.class),
				any(), 
				ArgumentMatchers.<ParameterizedTypeReference<List<ExchangeRateResponse>>>any()))
			.thenReturn(response);
	
		thrown.expect(InvalidNbpResponseException.class);
	    thrown.expectMessage("Response not contains rates");     
		service.getCurrentCurrencyRate("EUR");
	}
	
	@Test
	public void shouldThrowInvalidNbpResponseExceptionWhenRatesIsEmptyAndCodeCurrencyIsNull() {
		exchangeEurRateResponse.setRates(new ArrayList<>());
		exchangeRateResponseList.add(exchangeEurRateResponse);
		
		ResponseEntity<List<ExchangeRateResponse>> response = new ResponseEntity<>(exchangeRateResponseList, HttpStatus.OK);		
		Mockito.when(mockRestTemplate.exchange(anyString(), 
				any(HttpMethod.class),
				any(), 
				ArgumentMatchers.<ParameterizedTypeReference<List<ExchangeRateResponse>>>any()))
			.thenReturn(response);
	
		thrown.expect(InvalidNbpResponseException.class);
	    thrown.expectMessage("Response not contains searched current currency rate value");     
		service.getCurrentCurrencyRate(null);
	}	
	
	@Test
	public void shouldThrowInvalidNbpResponseExceptionWhenRatesIsEmptyAndCodeCurrencyIsNotNull() {
		exchangeEurRateResponse.setRates(new ArrayList<>());
		exchangeRateResponseList.add(exchangeEurRateResponse);
		
		ResponseEntity<List<ExchangeRateResponse>> response = new ResponseEntity<>(exchangeRateResponseList, HttpStatus.OK);		
		Mockito.when(mockRestTemplate.exchange(anyString(), 
				any(HttpMethod.class),
				any(), 
				ArgumentMatchers.<ParameterizedTypeReference<List<ExchangeRateResponse>>>any()))
			.thenReturn(response);
	
		thrown.expect(InvalidNbpResponseException.class);
	    thrown.expectMessage("Response not contains searched current currency rate value");     
		service.getCurrentCurrencyRate("EUR");
	}
	
	@Test
	public void shouldThrowInvalidNbpResponseExceptionWhenExchangeRateResponseListIsNull() {
		exchangeRateResponseList = null;	
		ResponseEntity<List<ExchangeRateResponse>> response = new ResponseEntity<>(exchangeRateResponseList, HttpStatus.OK);
		Mockito.when(mockRestTemplate.exchange(anyString(), 
				any(HttpMethod.class),
            	any(), 
            	ArgumentMatchers.<ParameterizedTypeReference<List<ExchangeRateResponse>>>any()))
			.thenReturn(response);
				
		thrown.expect(InvalidNbpResponseException.class);
	    thrown.expectMessage("Response not contains rates");     
		service.getCurrentCurrencyRate("EUR");
	}
	
	@Test
	public void shouldThrowInvalidNbpResponseExceptionWhenExchangeRateResponseListIsEmpty() {
		ResponseEntity<List<ExchangeRateResponse>> response = new ResponseEntity<>(exchangeRateResponseList, HttpStatus.OK);
		Mockito.when(mockRestTemplate.exchange(anyString(), 
				any(HttpMethod.class),
            	any(), 
            	ArgumentMatchers.<ParameterizedTypeReference<List<ExchangeRateResponse>>>any()))
			.thenReturn(response);			
		thrown.expect(InvalidNbpResponseException.class);
	    thrown.expectMessage("Response not contains rates");     
		service.getCurrentCurrencyRate("EUR");
	}
	
	@Test
	public void shouldGetValidCurrentCurrencyRateWhenRatesIsNotEmptyAndCodeCurrencyIsPresent() {
		ExchangeRateResponse.Rate eurRate = new ExchangeRateResponse.Rate();
		eurRate.setCode("EUR");
		eurRate.setMid(new BigDecimal(4.4136435));
		ExchangeRateResponse.Rate usaRate = new ExchangeRateResponse.Rate();
		usaRate.setCode("USA");
		usaRate.setMid(new BigDecimal(3.563));		
		List<ExchangeRateResponse.Rate> rates =new ArrayList<>();
		rates.add(eurRate);
		rates.add(usaRate);
		exchangeEurRateResponse.setRates(rates);
		exchangeRateResponseList.add(exchangeEurRateResponse);
		
		ResponseEntity<List<ExchangeRateResponse>> response = new ResponseEntity<>(exchangeRateResponseList, HttpStatus.OK);
		Mockito.when(mockRestTemplate.exchange(anyString(), 
				any(HttpMethod.class),
            	any(), 
            	ArgumentMatchers.<ParameterizedTypeReference<List<ExchangeRateResponse>>>any()))
			.thenReturn(response);
				
		CurrentCurrencyRate currentCurrencyRate = service.getCurrentCurrencyRate("EUR");
		Assert.assertThat(currentCurrencyRate.getValue(),is(  new BigDecimal(4.4136,new MathContext(5, RoundingMode.HALF_UP))));
	}
	
	@Test
	public void shouldGetValidCurrentCurrencyRateWhenRatesIsNotEmptyAndCodeCurrencyIsPresentButMidValueIsNull() {
		ExchangeRateResponse.Rate eurRate = new ExchangeRateResponse.Rate();
		eurRate.setCode("EUR");
		eurRate.setMid(null);
		ExchangeRateResponse.Rate usaRate = new ExchangeRateResponse.Rate();
		usaRate.setCode("USA");
		usaRate.setMid(new BigDecimal(3.563));		
		List<ExchangeRateResponse.Rate> rates =new ArrayList<>();
		rates.add(eurRate);
		rates.add(usaRate);
		exchangeEurRateResponse.setRates(rates);
		exchangeRateResponseList.add(exchangeEurRateResponse);
		
		ResponseEntity<List<ExchangeRateResponse>> response = new ResponseEntity<>(exchangeRateResponseList, HttpStatus.OK);
		Mockito.when(mockRestTemplate.exchange(anyString(), 
				any(HttpMethod.class),
            	any(), 
            	ArgumentMatchers.<ParameterizedTypeReference<List<ExchangeRateResponse>>>any()))
			.thenReturn(response);
				
		thrown.expect(InvalidNbpResponseException.class);	
		thrown.expectMessage("Response not contains searched current currency rate value");
		service.getCurrentCurrencyRate("EUR");
	}
	
	@Test
	public void shouldThrowInvalidNbpResponseExceptionWhenRatesIsNotEmptyAndCodeCurrencyIsNotPresent() {
		ExchangeRateResponse.Rate eurRate = new ExchangeRateResponse.Rate();
		eurRate.setCode("EUR");
		eurRate.setMid(new BigDecimal(4.4136435));
		ExchangeRateResponse.Rate usaRate = new ExchangeRateResponse.Rate();
		usaRate.setCode("USA");
		usaRate.setMid(new BigDecimal(3.563));		
		List<ExchangeRateResponse.Rate> rates =new ArrayList<>();
		rates.add(eurRate);
		rates.add(usaRate);
		exchangeEurRateResponse.setRates(rates);
		exchangeRateResponseList.add(exchangeEurRateResponse);
		
		ResponseEntity<List<ExchangeRateResponse>> response = new ResponseEntity<>(exchangeRateResponseList, HttpStatus.OK);
		Mockito.when(mockRestTemplate.exchange(anyString(), 
				any(HttpMethod.class),
            	any(), 
            	ArgumentMatchers.<ParameterizedTypeReference<List<ExchangeRateResponse>>>any()))
			.thenReturn(response);
				
		thrown.expect(InvalidNbpResponseException.class);
	    thrown.expectMessage("Response not contains searched current currency rate value");     
		service.getCurrentCurrencyRate("PL");
	}	
	
	@Test
	public void shouldThrowInvalidNbpResponseExceptionWhenRatesIsNotEmptyAndCodeCurrencyIsNull() {
		ExchangeRateResponse.Rate eurRate = new ExchangeRateResponse.Rate();
		eurRate.setCode("EUR");
		eurRate.setMid(new BigDecimal(4.4136435));
		ExchangeRateResponse.Rate usaRate = new ExchangeRateResponse.Rate();
		usaRate.setCode("USA");
		usaRate.setMid(new BigDecimal(3.563));		
		List<ExchangeRateResponse.Rate> rates =new ArrayList<>();
		rates.add(eurRate);
		rates.add(usaRate);
		exchangeEurRateResponse.setRates(rates);
		exchangeRateResponseList.add(exchangeEurRateResponse);
		
		ResponseEntity<List<ExchangeRateResponse>> response = new ResponseEntity<>(exchangeRateResponseList, HttpStatus.OK);
		Mockito.when(mockRestTemplate.exchange(anyString(), 
				any(HttpMethod.class),
            	any(), 
            	ArgumentMatchers.<ParameterizedTypeReference<List<ExchangeRateResponse>>>any()))
			.thenReturn(response);
				
		thrown.expect(InvalidNbpResponseException.class);
	    thrown.expectMessage("Response not contains searched current currency rate value");     
		service.getCurrentCurrencyRate(null);
	}	
}