package xcode.validators;


import java.util.ArrayList;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import xcode.exceptions.InvalidCalculatorNumbersRequestException;
import xcode.models.CalculatorNumbersRequest;
import xcode.models.TypeOrder;
import xcode.validators.CalculatorNumbersRequestValidator;
import xcode.validators.CalculatorNumbersRequestValidatorImpl;

public class CalculatorNumbersRequestValidatorTest {

	private final CalculatorNumbersRequestValidator validator = new CalculatorNumbersRequestValidatorImpl();
	
	@Rule
	public ExpectedException thrown = ExpectedException.none();
	
	@Test
	public void shouldThrow_whenOrderIsNull() {
		thrown.expect(InvalidCalculatorNumbersRequestException.class);
	    thrown.expectMessage("Invalid CalculatorNumbersRequest parameter - field order is null");
		CalculatorNumbersRequest request = new CalculatorNumbersRequest();
		request.setNumbers(new ArrayList<>());	 
		request.setOrder(null);	   
		validator.validate(request);	  
	}
	
	@Test
	public void shouldThrow_whenNumbersIsNull() {
		thrown.expect(InvalidCalculatorNumbersRequestException.class);
	    thrown.expectMessage("Invalid CalculatorNumbersRequest parameter - field numbers is null");
		CalculatorNumbersRequest request = new CalculatorNumbersRequest();
		request.setNumbers(null);   
		validator.validate(request);
	}
	
	@Test
	public void shouldThrow_whenOrderIsDiffrentThanASCAndDESCValue() {
		thrown.expect(InvalidCalculatorNumbersRequestException.class);
	    thrown.expectMessage("Invalid CalculatorNumbersRequest parameter - field order value is diffrent than ASC or DESC");
		CalculatorNumbersRequest request = new CalculatorNumbersRequest();
		request.setNumbers(new ArrayList<>());  
		request.setOrder(new String());
		validator.validate(request);
	}
	
	@Test(expected = Test.None.class)
	public void shouldNotThrow_whenOrderIsASCValue() {
		CalculatorNumbersRequest request = new CalculatorNumbersRequest();
		request.setNumbers(new ArrayList<>());  
		request.setOrder(new String(TypeOrder.ASC.toString()));
		validator.validate(request);
	}
	

	@Test(expected = Test.None.class)
	public void shouldNotThrow_whenOrderIsDESCValue() {
		CalculatorNumbersRequest request = new CalculatorNumbersRequest();
		request.setNumbers(new ArrayList<>());    
		request.setOrder(new String(TypeOrder.DESC.toString()));
		validator.validate(request);
	}
}