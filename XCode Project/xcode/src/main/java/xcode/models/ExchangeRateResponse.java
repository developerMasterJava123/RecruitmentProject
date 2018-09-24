package xcode.models;

import java.math.BigDecimal;
import java.util.List;


public class  ExchangeRateResponse {
	
	public static class Rate {
		
		private String currency;
		
		public String getCurrency() {
			return currency;
		}

		public void setCurrency(String currency) {
			this.currency = currency;
		}

		private String code;	
		
		private BigDecimal mid;

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public BigDecimal getMid() {
			return mid;
		}

		public void setMid(BigDecimal mid) {
			this.mid = mid;
		}

		@Override
		public String toString() {
			return "Rate [code=" + code + ", mid=" + mid + "]";
		}

		public Rate() {
		}
			
		
		
	}
	
	private String table;
	
	private String no;
	
	private String effectiveDate;
	
	private List<Rate> rates;

	public String getTable() {
		return table;
	}

	public void setTable(String table) {
		this.table = table;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public List<Rate> getRates() {
		return rates;
	}

	public void setRates(List<Rate> rates) {
		this.rates = rates;
	}

	@Override
	public String toString() {
		return "ExchangeRatesResponse [table=" + table + ", no=" + no + ", effectiveDate=" + effectiveDate + ", rates="
				+ rates + "]";
	}

	public ExchangeRateResponse() {
	}

	public ExchangeRateResponse(String table, String no, String effectiveDate, List<Rate> rates) {
		super();
		this.table = table;
		this.no = no;
		this.effectiveDate = effectiveDate;
		this.rates = rates;
	}
	
}
