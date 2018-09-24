package xcode.models;

import java.util.List;


public class CalculatorNumbersRequest {
	
	private List<Long> numbers;

	private String order;

	public List<Long> getNumbers() {
		return numbers;
	}

	public void setNumbers(List<Long> numbers) {
		this.numbers = numbers;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}


	public CalculatorNumbersRequest() {
	}

	public CalculatorNumbersRequest(List<Long> numbers, String order) {
		this.numbers = numbers;
		this.order = order;
	}


}
