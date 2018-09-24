package xcode.controllers;


import javax.servlet.http.HttpServletRequest;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.HttpStatusCodeException;

import xcode.exceptions.InvalidCalculatorNumbersRequestException;
import xcode.exceptions.InvalidNbpResponseException;
import xcode.models.CalculatorNumbersRequest;
import xcode.models.CurrentCurrencyRate;
import xcode.models.SortedNumberList;
import xcode.service.CalculatorNumbersService;
import xcode.service.CurrencyRatesNbpService;

@Controller
public class CurrencyRatesAndCalculatorNumberController {

	private static Logger logger = LoggerFactory.getLogger(CurrencyRatesAndCalculatorNumberController.class);	
	
	private final CurrencyRatesNbpService CurrencyRatesNbpServiceImpl;
	
	private final CalculatorNumbersService  CalculatorNumbersServiceImpl;
	
	@Autowired
	public CurrencyRatesAndCalculatorNumberController(CurrencyRatesNbpService currencyRatesNbpServiceImpl,
			CalculatorNumbersService calculatorNumbersServiceImpl) {	
		CurrencyRatesNbpServiceImpl = currencyRatesNbpServiceImpl;
		CalculatorNumbersServiceImpl = calculatorNumbersServiceImpl;
	}	
	
	@RequestMapping(value = "/status/ping", method = RequestMethod.GET)
	public @ResponseBody String getPing() { 
		return new String("pong");
	}
	
	@RequestMapping(value = "/numbers/sort-command", method = RequestMethod.POST)
	public ResponseEntity<SortedNumberList> retrieveSortedNumbersList(@RequestBody CalculatorNumbersRequest calculatorNumbersRequest ) { 
		return new ResponseEntity<SortedNumberList>(
			CalculatorNumbersServiceImpl.getSortedNumberList(calculatorNumbersRequest), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/currencies/get-current-currency-value-command", method = RequestMethod.POST)
	public ResponseEntity<CurrentCurrencyRate> retrieveCurrentCurrencyRate(@RequestBody String codeCurrency) {   
		try {
			return new ResponseEntity<CurrentCurrencyRate> (
				CurrencyRatesNbpServiceImpl.getCurrentCurrencyRate(codeCurrency), HttpStatus.OK);	
		}catch(HttpStatusCodeException e) {
			logger.error("Nbp Api Exception",e);
			return  new ResponseEntity<CurrentCurrencyRate>(e.getStatusCode());
		}
	}
	
	@RequestMapping(value = "/numbers/sorting-numbers-view", method = RequestMethod.GET)
	public String getSortingNumbersView() { 
		return new String("sortingNumbersView");
	}
	
	@ExceptionHandler({InvalidCalculatorNumbersRequestException.class})
	public ResponseEntity<Object> handleInvalidCalculatorNumbersRequestException(HttpServletRequest req, Exception e ) {
		return new ResponseEntity<Object> (HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler({InvalidNbpResponseException.class})
	public ResponseEntity<Object> handleInvalidNbpResponseException(HttpServletRequest req, Exception e ) {
		return new ResponseEntity<Object> (HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler({Exception.class})
	public ResponseEntity<Object> handleException(HttpServletRequest req, Exception e ) {
		logger.error("Internal Server error", e);
		return new ResponseEntity<Object> (HttpStatus.INTERNAL_SERVER_ERROR);
	}
}