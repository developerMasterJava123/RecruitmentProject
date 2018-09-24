package xcode.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import static org.hamcrest.core.Is.is;


import xcode.models.CalculatorNumbersRequest;
import xcode.service.CalculatorNumbersServiceImpl;
import xcode.validators.CalculatorNumbersRequestValidator;

@RunWith(MockitoJUnitRunner.class)
public class CalculatorNumbersServiceTest {
	
	@Mock
	private CalculatorNumbersRequestValidator validator;
	
	@InjectMocks
	private  CalculatorNumbersServiceImpl service;
	
	@Test()	
	public void shouldGetAscendingOrderListWhenWhenOrderIsASCValue() {
		List<Long> numberList = Stream.of(3L,2L,6L).collect(Collectors.toList());
		List<Long> ascOrderNumberList = Stream.of(2L,3L,6L).collect(Collectors.toList());		
		CalculatorNumbersRequest request = new CalculatorNumbersRequest();
		request.setNumbers(numberList);
		request.setOrder("ASC");
		service.getSortedNumberList(request);
		Assert.assertThat(numberList,is(ascOrderNumberList));	
	}
	
	@Test()	
	public void shouldGetAscendingOrderListWithLastNullsWhenOrderIsASCValueAndNumberListHasNulls() {
		List<Long> numberList = Stream.of(null,4L,2L,null,3L).collect(Collectors.toList());
		List<Long> ascOrderNumberList = Stream.of(2L,3L,4L,null,null).collect(Collectors.toList());
		CalculatorNumbersRequest request = new CalculatorNumbersRequest();
		request.setNumbers(numberList);
		request.setOrder("ASC");
		service.getSortedNumberList(request);
		Assert.assertThat(numberList,is(ascOrderNumberList));			
	}
	
	@Test()	
	public void shouldGetEmptyOrderListWhenOrderIsASCValueAndNumberListIsEmpty() {
		List<Long> numberList = new ArrayList<>();
		CalculatorNumbersRequest request = new CalculatorNumbersRequest();
		request.setNumbers(numberList);
		request.setOrder("ASC");
		service.getSortedNumberList(request);
		Assert.assertThat(numberList,is(numberList));			
	}
	
	@Test()	
	public void shouldGetNumberListWithNullsWhenOrderIsASCAndNumberListHasAllElementsNulls() {
		List<Long> numberList = new ArrayList<>(Arrays.asList(null,null));
		List<Long> ascOrderNumberList =new ArrayList<>(Arrays.asList(null,null));
		CalculatorNumbersRequest request = new CalculatorNumbersRequest();
		request.setNumbers(numberList);
		request.setOrder("ASC");
		service.getSortedNumberList(request);
		Assert.assertThat(numberList,is(ascOrderNumberList));			
	}
	
	@Test()	
	public void shouldGetTheSameListWhenOrderIsNotEqualDESCOrASCValue() {
		List<Long> numberList = Stream.of(4L,2L,3L).collect(Collectors.toList());
		List<Long> expecedNumberList = Stream.of(4L,2L,3L).collect(Collectors.toList());	
		CalculatorNumbersRequest request = new CalculatorNumbersRequest();
		request.setNumbers(numberList);
		request.setOrder("");
		service.getSortedNumberList(request);
		Assert.assertThat(numberList,is(expecedNumberList));			
	}
	
	@Test()	
	public void shouldGetDescendingOrderListWhenOrderIsDESCValue() {
		List<Long> numberList = Stream.of(4L,2L,3L).collect(Collectors.toList());
		List<Long> descOrderNumberList = Stream.of(4L,3L,2L).collect(Collectors.toList());
		CalculatorNumbersRequest request = new CalculatorNumbersRequest();
		request.setNumbers(numberList);
		request.setOrder("DESC");
		service.getSortedNumberList(request);
		Assert.assertThat(numberList,is(descOrderNumberList));			
	}
	
	@Test()	
	public void shouldGetEmptyOrderListWhenWhenOrderIsDESCValueAndNumberListIsEmpty() {
		List<Long> numberList = new ArrayList<>();
		CalculatorNumbersRequest request = new CalculatorNumbersRequest();
		request.setNumbers(numberList);
		request.setOrder("DESC");
		service.getSortedNumberList(request);
		Assert.assertThat(numberList,is(numberList));			
	}
	
	@Test()	
	public void shouldGetDescendingOrderListWithLastNullsWhenOrderIsDESCValueAndNumberListHasNulls() {
		List<Long> numberList = Stream.of(null,4L,2L,null,3L).collect(Collectors.toList());
		List<Long> descOrderNumberList = Stream.of(4L,3L,2L,null,null).collect(Collectors.toList());;
		CalculatorNumbersRequest request = new CalculatorNumbersRequest();
		request.setNumbers(numberList);
		request.setOrder("DESC");
		service.getSortedNumberList(request);
		Assert.assertThat(numberList,is(descOrderNumberList));			
	}
	
	@Test()	
	public void shouldGetNumberListWithNullsWhenOrderIsDESCAndNumberListHasAllElementsNulls() {
		List<Long> numberList = new ArrayList<>(Arrays.asList(null,null));
		List<Long> descOrderNumberList =new ArrayList<>(Arrays.asList(null,null));
		CalculatorNumbersRequest request = new CalculatorNumbersRequest();
		request.setNumbers(numberList);
		request.setOrder("DESC");
		service.getSortedNumberList(request);
		Assert.assertThat(numberList,is(descOrderNumberList));			
	}
}