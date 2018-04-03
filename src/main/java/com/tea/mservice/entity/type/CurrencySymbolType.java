package com.tea.mservice.entity.type;


public enum CurrencySymbolType {

	/** USD - $ */
	USD("USD", "$"),
	/** CNY - �? */
	CNY("CNY", "�?"),
	/** THB - �? */
	THB("THB", "�?");

	private String currency;
	private String symbol;

	private CurrencySymbolType(String currency, String symbol) {
		this.currency = currency;
		this.symbol = symbol;
	}

	public String getCurrency() {
		return currency;
	}

	public String getSymbol() {
		return symbol;
	}

	/**
	 *
	 * @param currency
	 *            USD/CNY/THB
	 * @return default USD
	 */
	public static CurrencySymbolType instance(String currency) {
		for (CurrencySymbolType curr : values()) {
			if (curr.getCurrency().equalsIgnoreCase(currency)) {
				return curr;
			}
		}
		return CurrencySymbolType.USD;
	}


}
