package xcode.models;

import java.util.List;

public class SortedNumberList {
	
	private List<Long> numbers;

	public SortedNumberList(List<Long> numbers) {
		this.numbers = numbers;
	}
	
	public SortedNumberList() {
	}

	public List<Long> getNumbers() {
		return numbers;
	}

	public void setNumbers(List<Long> numbers) {
		this.numbers = numbers;
	}		
}
