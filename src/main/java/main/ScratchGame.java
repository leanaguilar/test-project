package main;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import main.pojos.BonusSymbols;
import main.pojos.GameResponse;
import main.pojos.JsonReader;
import main.pojos.ScratchGameConfig;
import main.pojos.StandardSymbol;

public class ScratchGame {

	private GameResponse response;

	public ScratchGame() {
		setResponse(new GameResponse());
	}

	public static void main(String[] args) {

		JsonReader reader = new JsonReader();
		ScratchGameConfig config = reader.readConfigFile("config.json");
		int betAmount = 100;

		ScratchGame game = new ScratchGame();
		int rows = config.getRows();
		int columns = config.getColumns();

		String[][] matrix = game.generateMatrix(rows, columns, config);
		double totalReward = game.calculateReward(betAmount, matrix, config);
		String bonusSymbol = "+1000";
		String[] applied_winning_combinations = null; // falta esto
		game.printResponse();

	}

	private void printResponse() {

		this.getResponse().printResponse();

	}

	public String[][] generateMatrix(int rows, int columns, ScratchGameConfig config) {

		String[][] matrix = new String[rows][columns];
		List<StandardSymbol> symbolProbabilities = config.getProbabilities().getStandardSymbols();
		String symbol;
		String bonusSymbol = null;
		boolean bonusSymbolAlreadyAdded = Boolean.FALSE;

		for (int currentRow = 0; currentRow < rows; currentRow++) {

			for (int currentColumn = 0; currentColumn < columns; currentColumn++) {

				// for each cell get a random symbol based on probability
				symbol = this.getRandomStandardSymbolBasedOnProbability(currentRow, currentColumn, symbolProbabilities);
				// decide if we can add a bonus symbol by probability, if so then we replace the
				// previous generated symbol
				// a bonus symbol can appear only once in a matrix
				if (!bonusSymbolAlreadyAdded) {

					bonusSymbol = this.getBonusSymbol(config.getProbabilities().getBonusSymbols());
					if (bonusSymbol != null) {
						bonusSymbolAlreadyAdded = Boolean.TRUE;
						if (bonusSymbol != "MISS") {
							symbol = bonusSymbol;
						}

					}
				}
				// Place the chosen symbol in the matrix
				matrix[currentRow][currentColumn] = symbol;

			}
		}

		this.getResponse().setAppliedBonusSymbol(bonusSymbol);
		this.getResponse().setMatrix(matrix);
		return matrix;

	}

	private double calculateReward(int betAmount, String[][] matrix, ScratchGameConfig config) {

		// Create a HashMap to store each symbol counts
		double totalRewards = 0;
		double symbolRewardMultiplier = 0;
		double winCombinationRewardMultiplier = 0;
		Map<String, Integer> symbolMapCounts = new HashMap<>();

		// get how many times a symbol appears in the matrix
		for (String[] row : matrix) {
			for (String symbol : row) {
				// Update the count of each symbol
				symbolMapCounts.put(symbol, symbolMapCounts.getOrDefault(symbol, 0) + 1);
			}
		}

		for (Map.Entry<String, Integer> entry : symbolMapCounts.entrySet()) {

			if (entry.getValue() >= 3) { // if >= 3 it is a winning symbol
				symbolRewardMultiplier = config.getSymbolRewardMultiplier(entry.getKey());
				// get the win combination reward multiplier
				winCombinationRewardMultiplier = config.getWinCombinationRewardMultiplier(entry.getValue());
				// chequear si es horizontal vertical y multiplcar tmb en ese caso
				totalRewards += betAmount * symbolRewardMultiplier * winCombinationRewardMultiplier;

				// (bet_amount x reward(symbol_A) x reward(same_symbol_5_times) x
				// reward(same_symbols_vertically))
			}
		}
		// chequear si hay simbolo bonus y sumar

		// validar win combinations
		this.getResponse().setReward(totalRewards);
		return totalRewards;

	}

	private String getBonusSymbol(BonusSymbols bonusSymbols) {

		int totalSum = bonusSymbols.getSymbols().values().stream().mapToInt(Integer::intValue).sum();
		Map<String, Double> cumulativeProbabilities = new HashMap<>();
		double cumulativeSum = 0.0;
		String bonusSymbol = null;
		double probability;

		for (Map.Entry<String, Integer> entry : bonusSymbols.getSymbols().entrySet()) {

			probability = entry.getValue() / (double) totalSum;
			cumulativeSum += probability;
			cumulativeProbabilities.put(entry.getKey(), cumulativeSum);
		}

		Random random = new Random();
		double randomValue = random.nextDouble();

		// Select the bonus symbol based on the random value
		for (Map.Entry<String, Double> entry : cumulativeProbabilities.entrySet()) {
			if (randomValue <= entry.getValue()) {
				bonusSymbol = entry.getKey(); // Return the selected bonus symbol
			}
		}

		// In case nothing is selected (edge case), return the last symbol
		// bonusSymbol = (String)
		// cumulativeProbabilities.keySet().toArray()[cumulativeProbabilities.size() -
		// 1];

		return bonusSymbol;

	}

	/**
	 * Returns a random symbol
	 * 
	 * // Calculate the sum of standard symbols for the currentRow,currentColumn
	 * current position // pre condition: every cell needs to have its probability
	 * definition, if it // doesnt have then the code is not going to work // I
	 * assume that if there is no definition of probability for all cells then //
	 * json is incomplete
	 * 
	 * @param currentRow
	 * @param currentColumn
	 * @param symbolProbabilities
	 * @return
	 */
	private String getRandomStandardSymbolBasedOnProbability(int currentRow, int currentColumn,
			List<StandardSymbol> symbolProbabilities) {

		Random random = new Random();

		int totalSum = 0;
		double cumulativeSum = 0.0;
		double probability;
		Map<String, Double> cumulativeProbabilities = new HashMap<>();

//		 for (Map.Entry<String, Integer> entry : symbolProbabilities.get    ) {
//	        	
//	        	probability = entry.getValue() / (double) totalWeight;
//	            cumulativeSum += probability;
//	            cumulativeProbabilities.put(entry.getKey(), cumulativeSum); 
//		    }
//		
//		for (StandardSymbol probability : symbolProbabilities) {
//
//			if (probability.getRow() == currentRow && probability.getColumn() == currentColumn) {
//				totalWeight += probability.getSymbols().values().stream().mapToInt(Integer::intValue).sum();
//				cumulativeProbabilities.put(probability.getSymbols().get, totalWeight); 
//				
//				
//				break;
//s
//			}
//
//		}

		for (StandardSymbol symbol : symbolProbabilities) {

			if (symbol.getRow() == currentRow && symbol.getColumn() == currentColumn) {
				totalSum += symbol.getSymbols().values().stream().mapToInt(Integer::intValue).sum();

				for (Map.Entry<String, Integer> entry : symbol.getSymbols().entrySet()) {

					probability = entry.getValue() / (double) totalSum;
					cumulativeSum += probability;
					cumulativeProbabilities.put(entry.getKey(), cumulativeSum);
					
				}

				break;

			}

		}

//		for (Map.Entry<String, Integer> entry : symbolProbabilities.g    ) {
//
//			probability = entry.getValue() / (double) totalSum;
//			cumulativeSum += probability;
//			cumulativeProbabilities.put(entry.getKey(), cumulativeSum);
//		}

		// Pick a random number based on the total weight
		if (totalSum == 0) {
			throw new RuntimeException(
					"Json is incomplete. Some pair (row, column) does not have it probability definition. Modify the config.json");
		}

		int randomValue = random.nextInt();
		String chosenSymbol = null;

		// Select the bonus symbol based on the random value
		for (Map.Entry<String, Double> entry : cumulativeProbabilities.entrySet()) {
			if (randomValue <= entry.getValue()) {
				chosenSymbol = entry.getKey();
			}
		}

//		for (StandardSymbol standardSymbol : symbolProbabilities) {
//			for (Map.Entry<String, Integer> entry : standardSymbol.getSymbols().entrySet()) {
//
//				if (randomValue < entry.getValue()) {
//					chosenSymbol = entry.getKey();
//					break;
//				}
//				randomValue -= entry.getValue();
//			}
//			if (chosenSymbol != null)
//				break; // Break the loop if symbol found
//		}

		return chosenSymbol;
	}

	public GameResponse getResponse() {
		return response;
	}

	public void setResponse(GameResponse response) {
		this.response = response;
	}

}
