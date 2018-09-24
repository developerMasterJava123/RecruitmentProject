package xcode;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.Matchers.*;


import xcode.models.CalculatorNumbersRequest;
import xcode.models.CurrentCurrencyRate;
import xcode.models.SortedNumberList;
import xcode.models.TypeOrder;


import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class IntegrationTestApplication {
	
   @Autowired
   private TestRestTemplate restTemplate;
   
   private final String VALID_CODE_CURRENCY_PARAM = new String("EUR");
   						
   private final String NOT_FOUND_CODE_CURRENCY_PARAM = new String("NOT_FOUND_CODE_CURRENCY_PARAM");
  
   @Test
   public void shouldValidBodyAndHttpStatusOkWhenGetStatusPingMethodIsInvoked() {  
	   HttpHeaders headers = new HttpHeaders();
	   headers.setContentType(MediaType.APPLICATION_JSON);
	   ResponseEntity<String> response = restTemplate.getForEntity("/status/ping",  String.class );
	   Assert.assertThat(response.getBody(),is( "pong"));
	   Assert.assertThat(response.getStatusCode(),is(HttpStatus.OK));	   
   }
 
   @Test
   public void shouldGetAscendingOrderListWhenOrderIsASCValue() throws JsonProcessingException {
	   List<Long> numberList = Stream.of(3L,2L,6L).collect(Collectors.toList());
	   List<Long> AscOrderNumberList = Stream.of(2L,3L,6L).collect(Collectors.toList());		
	   CalculatorNumbersRequest request = new CalculatorNumbersRequest();
	   request.setNumbers(numberList);
	   request.setOrder(TypeOrder.ASC.toString());
	   ResponseEntity<SortedNumberList> response = restTemplate.postForEntity("/numbers/sort-command", request, SortedNumberList.class );
	   Assert.assertThat(response.getBody().getNumbers(),is(AscOrderNumberList));	
	   Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK));
   }
   
   @Test	
	public void shouldGetAscendingOrderListWithLastNullsWhenOrderIsASCValueAndNumberListHasNulls() {
		List<Long> numberList = Stream.of(null,4L,2L,null,3L).collect(Collectors.toList());
		List<Long> ascOrderNumberList = Stream.of(2L,3L,4L,null,null).collect(Collectors.toList());
		CalculatorNumbersRequest request = new CalculatorNumbersRequest();
		request.setNumbers(numberList);
		request.setOrder(TypeOrder.ASC.toString());
		ResponseEntity<SortedNumberList> response = restTemplate.postForEntity("/numbers/sort-command", request, SortedNumberList.class );
		Assert.assertThat(response.getBody().getNumbers(), is(ascOrderNumberList));
		Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK));			
	}
   
   @Test()	
	public void shouldGetEmptyOrderListWhenOrderIsASCValueAndNumberListIsEmpty() {
		List<Long> numberList = new ArrayList<>();
		CalculatorNumbersRequest request = new CalculatorNumbersRequest();
		request.setNumbers(numberList);
		request.setOrder(TypeOrder.ASC.toString());
		ResponseEntity<SortedNumberList> response = restTemplate.postForEntity("/numbers/sort-command", request, SortedNumberList.class );
		Assert.assertThat(response.getBody().getNumbers(),is(numberList));			
		Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK));
	}
	
	
	@Test
	public void shouldGetNumberListWithNullsWhenOrderIsASCAndNumberListHasAllElementsNulls() {
		List<Long> numberList = new ArrayList<>(Arrays.asList(null,null));
		List<Long> ascOrderNumberList =new ArrayList<>(Arrays.asList(null,null));
		CalculatorNumbersRequest request = new CalculatorNumbersRequest();
		request.setNumbers(numberList);
		request.setOrder(TypeOrder.ASC.toString());
		ResponseEntity<SortedNumberList> response = restTemplate.postForEntity("/numbers/sort-command", request, SortedNumberList.class );
		Assert.assertThat(response.getBody().getNumbers(), is(ascOrderNumberList));		
		Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK));
	}
	
	@Test	
	public void shouldGetDescendingOrderListWhenOrderIsDESCValue() {
		List<Long> numberList = Stream.of(4L,2L,3L).collect(Collectors.toList());
		List<Long> descOrderNumberList = Stream.of(4L,3L,2L).collect(Collectors.toList());
		CalculatorNumbersRequest request = new CalculatorNumbersRequest();
		request.setNumbers(numberList);
		request.setOrder(TypeOrder.DESC.toString());
		ResponseEntity<SortedNumberList> response = restTemplate.postForEntity("/numbers/sort-command", request, SortedNumberList.class );
		Assert.assertThat(response.getBody().getNumbers(),is(descOrderNumberList));			
		Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK));
	}
	
	@Test	
	public void shouldGetDescendingOrderListWithLastNullsWhenOrderIsDESCValueAndNumberListHasNulls() {
		List<Long> numberList = Stream.of(null,4L,2L,null,3L).collect(Collectors.toList());
		List<Long> descOrderNumberList = Stream.of(4L,3L,2L,null,null).collect(Collectors.toList());;
		CalculatorNumbersRequest request = new CalculatorNumbersRequest();
		request.setNumbers(numberList);
		request.setOrder(TypeOrder.DESC.toString());
		ResponseEntity<SortedNumberList> response = restTemplate.postForEntity("/numbers/sort-command", request, SortedNumberList.class );
		Assert.assertThat(response.getBody().getNumbers(), is(descOrderNumberList));			
	}
	
	@Test
	public void shouldGetEmptyOrderListWhenOrderIsDESCValueAndNumberListIsEmpty() {
		List<Long> numberList = new ArrayList<>();
		CalculatorNumbersRequest request = new CalculatorNumbersRequest();
		request.setNumbers(numberList);
		request.setOrder(TypeOrder.DESC.toString());
		ResponseEntity<SortedNumberList> response = restTemplate.postForEntity("/numbers/sort-command", request, SortedNumberList.class );
		Assert.assertThat(response.getBody().getNumbers(),is(numberList));			
		Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK));
	}
	
	@Test
	public void shouldGetNumberListWithNullsWhenOrderIsDESCAndNumberListHasAllElementsNulls() {
		List<Long> numberList = new ArrayList<>(Arrays.asList(null,null));
		List<Long> descOrderNumberList =new ArrayList<>(Arrays.asList(null,null));
		CalculatorNumbersRequest request = new CalculatorNumbersRequest();
		request.setNumbers(numberList);
		request.setOrder(TypeOrder.DESC.toString());
		ResponseEntity<SortedNumberList> response = restTemplate.postForEntity("/numbers/sort-command", request, SortedNumberList.class );
		Assert.assertThat(response.getBody().getNumbers(), is(descOrderNumberList));	
		Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK));
	}
	
	@Test
	public void shouldBadRequestHttpStatusWhenOrderIsNotEqualDESCOrASCValue() {
		CalculatorNumbersRequest request = new CalculatorNumbersRequest();
		request.setNumbers(new ArrayList<>());
		request.setOrder("");
		ResponseEntity<SortedNumberList> response = restTemplate.postForEntity("/numbers/sort-command", request, SortedNumberList.class );
		Assert.assertThat(response.getBody(),is(Matchers.nullValue()));			
		Assert.assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
	}
	
	@Test
	public void shouldBadRequestHttpStatusWhenOrderIsNull() {
		CalculatorNumbersRequest request = new CalculatorNumbersRequest();
		request.setNumbers(new ArrayList<>());
		request.setOrder(null);
		ResponseEntity<SortedNumberList> response = restTemplate.postForEntity("/numbers/sort-command", request, SortedNumberList.class );
		Assert.assertThat(response.getBody(),is(Matchers.nullValue()));			
		Assert.assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
	}
	
	@Test	
	public void shouldBadRequestHttpStatusWhenNumberListIsNull() {
		CalculatorNumbersRequest request = new CalculatorNumbersRequest();
		request.setNumbers(null);
		request.setOrder(TypeOrder.DESC.toString());
		ResponseEntity<SortedNumberList> response = restTemplate.postForEntity("/numbers/sort-command", request, SortedNumberList.class );
		Assert.assertThat(response.getBody(),Matchers.is(Matchers.nullValue()));			
		Assert.assertThat(response.getStatusCode(), is(HttpStatus.BAD_REQUEST));
	}
	
	@Test
	public void shouldGetValidCurrencyRateValueWhenCodeCurrencyIsValid() {
		HttpEntity<String> request = new HttpEntity<>(VALID_CODE_CURRENCY_PARAM);
		ResponseEntity<CurrentCurrencyRate> response = restTemplate.exchange(
				"/currencies/get-current-currency-value-command",
				  HttpMethod.POST,
				  request,
				  new ParameterizedTypeReference<CurrentCurrencyRate>(){});
		Assert.assertThat(response.getBody().getValue(),Matchers.greaterThanOrEqualTo( new BigDecimal(3.21,new MathContext(5, RoundingMode.HALF_UP))));
		Assert.assertThat(response.getStatusCode(), is(HttpStatus.OK));
	}
	
	@Test
	public void shouldGetNotFoundHttpStatusWhenCodeCurrencyIsNotFound() {
		HttpEntity<String> request = new HttpEntity<>(NOT_FOUND_CODE_CURRENCY_PARAM);
		ResponseEntity<CurrentCurrencyRate> response = restTemplate.exchange(
				"/currencies/get-current-currency-value-command",
				  HttpMethod.POST,
				  request,
				  new ParameterizedTypeReference<CurrentCurrencyRate>(){});
		Assert.assertThat(response.getBody(),Matchers.nullValue());
		Assert.assertThat(response.getStatusCode(), is(HttpStatus.NOT_FOUND));
	}
	
}