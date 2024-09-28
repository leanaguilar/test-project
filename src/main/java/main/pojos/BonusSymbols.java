package main.pojos;

import java.util.Map;

//Class to map the bonus symbols in the probabilities section
public class BonusSymbols {
	
	private Map<String, Integer> symbols;

	// Getters and Setters
	public Map<String, Integer> getSymbols() {
		return symbols;
	}

	public void setSymbols(Map<String, Integer> symbols) {
		this.symbols = symbols;
	}
}