package xcode.controllers;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import com.fasterxml.jackson.databind.ObjectMapper;

import xcode.controllers.CurrencyRatesAndCalculatorNumberController;
import xcode.exceptions.InvalidCalculatorNumbersRequestException;
import xcode.exceptions.InvalidNbpResponseException;
import xcode.models.CalculatorNumbersRequest;
import xcode.models.SortedNumberList;
import xcode.service.CalculatorNumbersService;
import xcode.service.CurrencyRatesNbpService;

import static org.hamcrest.core.Is.is;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.ArrayList;

//Explanations - This test checks HttpStatus Response depending on exceptions
@RunWith(SpringRunner.class)
@WebMvcTest(CurrencyRatesAndCalculatorNumberController.class)
public class CurrencyRatesAndCalculatorNumberControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private  CurrencyRatesNbpService CurrencyRatesNbpServiceImpl;
	
	@MockBean
	private CalculatorNumbersService  CalculatorNumbersServiceImpl;
	
	@Autowired
    ObjectMapper objectMapper;
	
	@Test
	public void shouldRetrieveHttpStatusOkAndContentBodyWhenStatusPingIsInvoked()throws Exception {		    ;
	    MockHttpServletResponse response = mockMvc.perform(get("/status/ping/")
	    									 .contentType(MediaType.APPLICATION_JSON))
	    								     .andReturn().getResponse();	  
	    Assert.assertThat(response.getStatus(), is (HttpStatus.OK.value()));   
	    Assert.assertThat(response.getContentAsString(), is("pong"));
	}
	
	@Test
	public void shouldRetrieveBadRequestHttpStatusWhenCalculatorNumbersRequestExceptionIsThrown() throws Exception {
	    String requestJson = objectMapper.writeValueAsString(new CalculatorNumbersRequest());
	    Mockito.when(CalculatorNumbersServiceImpl.getSortedNumberList(
	    			any(CalculatorNumbersRequest.class)))
	    		.thenThrow(InvalidCalculatorNumbersRequestException.class);	    
	    MockHttpServletResponse response = mockMvc.perform(post("/numbers/sort-command")
	    									 .contentType(MediaType.APPLICATION_JSON)
	    								     .content(requestJson))
	    									 .andReturn().getResponse();
	    Assert.assertThat(response.getStatus(), is (HttpStatus.BAD_REQUEST.value()));   		
	}
	
	@Test
	public void shouldRetrieveOkHttpStatusWhenExceptionIsNotThrownInCalculatorNumberService() throws Exception {
	    String requestJson = objectMapper.writeValueAsString(new CalculatorNumbersRequest());
	    Mockito.when(CalculatorNumbersServiceImpl.getSortedNumberList(any(CalculatorNumbersRequest.class)))
				.thenReturn(new SortedNumberList(new ArrayList<>()));	
	    MockHttpServletResponse response = mockMvc.perform(post("/numbers/sort-command")
	    									 .contentType(MediaType.APPLICATION_JSON)
	    								     .content(requestJson))
	    									 .andReturn().getResponse();	    
	    Assert.assertThat(response.getStatus(), is (HttpStatus.OK.value()));   			
	}
		
	@Test
	public void shouldRetrievesNotFoundHttpStatusWhenInvalidNbpResponseExceptionIsThrown() throws Exception {		    
	    Mockito.when(CurrencyRatesNbpServiceImpl.getCurrentCurrencyRate(anyString()))
				.thenThrow( InvalidNbpResponseException.class);
	    MockHttpServletResponse response = mockMvc.perform(post("/currencies/get-current-currency-value-command")
	    									 .contentType(MediaType.APPLICATION_JSON)
	    								     .content(objectMapper.writeValueAsString(new String())))
	    									 .andReturn().getResponse();	    
	    Assert.assertThat(response.getStatus(), is (HttpStatus.NOT_FOUND.value()));   			
	}
	
	@Test
	public void shouldRetrieveOkHttpStatusWhenExceptionIsNotThrownInCurrencyRatesNbpService() throws Exception {		    
	    MockHttpServletResponse response = mockMvc.perform(post("/currencies/get-current-currency-value-command")
	    									 .contentType(MediaType.APPLICATION_JSON)
	    								     .content(objectMapper.writeValueAsString(new String())))
	    									 .andReturn().getResponse();	    
	    Assert.assertThat(response.getStatus(), is (HttpStatus.OK.value()));   
	}
	
	@Test
	public void shouldRetrieveClientErrorHttpStatusWhenHttpClientErrorExceptionIsThrown() throws Exception {		    
		HttpClientErrorException clientException = new HttpClientErrorException(HttpStatus.NOT_FOUND);
		Mockito.when(CurrencyRatesNbpServiceImpl.getCurrentCurrencyRate(anyString()))
			.thenThrow( clientException);
	    MockHttpServletResponse response = mockMvc.perform(post("/currencies/get-current-currency-value-command")
	    									 .contentType(MediaType.APPLICATION_JSON)
	    								     .content(objectMapper.writeValueAsString(new String())))
	    									 .andReturn().getResponse();	    
	    Assert.assertThat(response.getStatus(), is (HttpStatus.NOT_FOUND.value()));   
	}
	
	@Test
	public void shouldRetrieveServerErrorHttpStatusWhenHttpServerErrorExceptionIsThrown() throws Exception {		    
		HttpServerErrorException serverException = new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR);
		Mockito.when(CurrencyRatesNbpServiceImpl.getCurrentCurrencyRate(anyString()))
			.thenThrow( serverException);
	    MockHttpServletResponse response = mockMvc.perform(post("/currencies/get-current-currency-value-command")
	    									 .contentType(MediaType.APPLICATION_JSON)
	    								     .content(objectMapper.writeValueAsString(new String())))
	    									 .andReturn().getResponse();	    
	    Assert.assertThat(response.getStatus(), is (HttpStatus.INTERNAL_SERVER_ERROR.value()));   
	}

	@Test
	public void shouldRetrieveInternalServerErrorHttpStatusWhenUnexpectedExceptionIsThrown() throws Exception {		    
		Mockito.when(CurrencyRatesNbpServiceImpl.getCurrentCurrencyRate(anyString()))
			.thenThrow( NullPointerException.class);
	    MockHttpServletResponse response = mockMvc.perform(post("/currencies/get-current-currency-value-command")
	    									 .contentType(MediaType.APPLICATION_JSON)
	    								     .content(objectMapper.writeValueAsString(new String())))
	    									 .andReturn().getResponse();	    
	    Assert.assertThat(response.getStatus(), is (HttpStatus.INTERNAL_SERVER_ERROR.value()));   
	}
}