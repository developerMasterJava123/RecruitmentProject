package xcode.service;

import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import xcode.models.CalculatorNumbersRequest;
import xcode.models.SortedNumberList;
import xcode.models.TypeOrder;
import xcode.validators.CalculatorNumbersRequestValidator;

@Service
public class CalculatorNumbersServiceImpl implements CalculatorNumbersService {

	private final CalculatorNumbersRequestValidator validatorImpl;
	
	@Autowired
	public CalculatorNumbersServiceImpl(CalculatorNumbersRequestValidator validatorImpl) {
		this.validatorImpl = validatorImpl;
	}
	
	@Override
	public SortedNumberList getSortedNumberList(CalculatorNumbersRequest calculatorNumbersRequest) {	
		initializeValidation(calculatorNumbersRequest);
		List<Long> numberList = calculatorNumbersRequest.getNumbers();
		if(calculatorNumbersRequest.getOrder().equals(TypeOrder.ASC.toString())) {
			numberList.sort(Comparator.nullsLast(Comparator.naturalOrder()));
		} else if(calculatorNumbersRequest.getOrder().equals(TypeOrder.DESC.toString())) {
			numberList.sort(Comparator.nullsLast(Comparator.reverseOrder()));
		}
		return new SortedNumberList(numberList);
	}
	
	private void initializeValidation(CalculatorNumbersRequest calculatorNumbersRequest) {
		validatorImpl.validate(calculatorNumbersRequest);
	}
}