package main.pojos;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

//Class to map the probabilities section
public class Probabilities {
	
	@JsonProperty("standard_symbols")
	private List<StandardSymbol> standardSymbols;
	
	@JsonProperty("bonus_symbols")
	private BonusSymbols bonusSymbols;

	// Getters and Setters
	public List<StandardSymbol> getStandardSymbols() {
		return standardSymbols;
	}

	public void setStandardSymbols(List<StandardSymbol> standardSymbols) {
		this.standardSymbols = standardSymbols;
	}

	public BonusSymbols getBonusSymbols() {
		return bonusSymbols;
	}

	public void setBonusSymbols(BonusSymbols bonusSymbols) {
		this.bonusSymbols = bonusSymbols;
	}
}