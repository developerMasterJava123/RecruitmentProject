package xcode.service;


import xcode.models.CalculatorNumbersRequest;
import xcode.models.SortedNumberList;

public interface CalculatorNumbersService {
	
	SortedNumberList getSortedNumberList(CalculatorNumbersRequest calculatorNumbersRequest);	
}
