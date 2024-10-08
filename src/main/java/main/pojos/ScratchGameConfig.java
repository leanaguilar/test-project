package main.pojos;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import main.ScratchGame;

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

	public double getWinCombinationRewardMultiplier(Integer count) {

		WinCombination winCombination;
		double rewardMultiplier = 0;
		for (Map.Entry<String, WinCombination> entry : this.getWinCombinations().entrySet()) {
		
			winCombination = entry.getValue();
			if (winCombination.getCount() == count) {
				rewardMultiplier = winCombination.getRewardMultiplier();
				break;
			}
		}
		
		return rewardMultiplier;
	}

	public double getSymbolExtra(String key) {
		Symbol symbol = this.getSymbols().get(key);
		return symbol.getExtra();
	}

	public String getWinCombinationName(Integer value) {
		
		WinCombination winCombination;
		String name = null;
		for (Map.Entry<String, WinCombination> entry : this.getWinCombinations().entrySet()) {
		
			winCombination = entry.getValue();
			if (winCombination.getCount() == value) {
				name = entry.getKey();
				break;
			}
		}
		return name;
	}

	public double getHorizontalRewardMultiplier() {

        WinCombination winCombination;
		double rewardMultiplier = 0;
		for (Map.Entry<String, WinCombination> entry : this.getWinCombinations().entrySet()) {
		
			if (entry.getKey().equals(ScratchGame.SAME_SYMBOLS_HORIZONTALLY)) {
				winCombination = entry.getValue();
				rewardMultiplier = winCombination.getRewardMultiplier();
				break;
			}
		}
		
		return rewardMultiplier;
	}
	
	public double getVerticalRewardMultiplier() {

        WinCombination winCombination;
		double rewardMultiplier = 0;
		for (Map.Entry<String, WinCombination> entry : this.getWinCombinations().entrySet()) {
		
			if (entry.getKey().equals(ScratchGame.SAME_SYMBOLS_VERTICALLY)) {
				winCombination = entry.getValue();
				rewardMultiplier = winCombination.getRewardMultiplier();
				break;
			}
		}
		
		return rewardMultiplier;
	}

}
