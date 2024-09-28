package main.pojos;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ScratchGameConfig {

	int columns;
	int rows;

	private Map<String, Symbol> symbols;

	private Probabilities probabilities;

	@JsonProperty("win_combinations")
	private Map<String, WinCombination> winCombinations;

	public int getColumns() {
		return columns;
	}

	public Map<String, Symbol> getSymbols() {
		return symbols;
	}

	public void setSymbols(Map<String, Symbol> symbols) {
		this.symbols = symbols;
	}

	public Probabilities getProbabilities() {
		return probabilities;
	}

	public void setProbabilities(Probabilities probabilities) {
		this.probabilities = probabilities;
	}

	public Map<String, WinCombination> getWinCombinations() {
		return winCombinations;
	}

	public void setWinCombinations(Map<String, WinCombination> winCombinations) {
		this.winCombinations = winCombinations;
	}

	public void setColumns(int columns) {
		this.columns = columns;
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}

	public double getSymbolRewardMultiplier(String key) {
		Symbol symbol = this.getSymbols().get(key);
		return symbol.getRewardMultiplier();
	}

	public double getWinCombinationRewardMultiplier(Integer value) {

		WinCombination winCombination;
		double rewardMultiplier = 0;
		for (Map.Entry<String, WinCombination> entry : this.getWinCombinations().entrySet()) {
		
			winCombination = entry.getValue();
			if (winCombination.getCount() == value) {
				rewardMultiplier = winCombination.getRewardMultiplier();
				break;
			}
		}
		
		return rewardMultiplier;
	}

}
