package xcode.validators;

import org.springframework.stereotype.Component;

import xcode.exceptions.InvalidCalculatorNumbersRequestException;
import xcode.models.CalculatorNumbersRequest;
import xcode.models.TypeOrder;

@Component
public class CalculatorNumbersRequestValidatorImpl implements CalculatorNumbersRequestValidator  {

	@Override
	public void validate(CalculatorNumbersRequest request) {
		if(request.getNumbers() == null) {
			throw new  InvalidCalculatorNumbersRequestException("Invalid CalculatorNumbersRequest parameter - field numbers is null");
		}
		if(request.getOrder() == null) {
			throw new  InvalidCalculatorNumbersRequestException("Invalid CalculatorNumbersRequest parameter - field order is null");			
		}
		if(!request.getOrder().equals(TypeOrder.ASC.toString()) &&  !request.getOrder().equals(TypeOrder.DESC.toString())) {		
			throw new  InvalidCalculatorNumbersRequestException("Invalid CalculatorNumbersRequest parameter - field order value is diffrent than ASC or DESC");
		}	
	}
}

