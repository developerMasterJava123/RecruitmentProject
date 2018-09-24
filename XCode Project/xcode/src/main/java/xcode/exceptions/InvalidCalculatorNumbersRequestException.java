package xcode.exceptions;

public class InvalidCalculatorNumbersRequestException extends RuntimeException {
	
    public InvalidCalculatorNumbersRequestException(String message) {
        super(message);
    }

    public InvalidCalculatorNumbersRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
