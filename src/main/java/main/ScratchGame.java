package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import main.pojos.BonusSymbols;
import main.pojos.GameResponse;
import main.pojos.JsonReader;
import main.pojos.ScratchGameConfig;
import main.pojos.StandardSymbol;
import main.util.SymbolChecker;

public class ScratchGame {

	public static final String SAME_SYMBOLS_VERTICALLY = "same_symbols_vertically";

	public static final String SAME_SYMBOLS_HORIZONTALLY = "same_symbols_horizontally";

	private GameResponse response;

	public ScratchGame() {
		setResponse(new GameResponse());
	}

	public static void main(String[] args) {

		String configFilePath;
		int bettingAmount;

		if (args.length != 4) {
			System.out.println("Usage: --config <config_file> --betting-amount <amount>");
			throw new IllegalArgumentException("Error: You must provide the arguments.");
		}

		// parse arguments
		HashMap<String, String> params = ScratchGame.getParams(args);
		bettingAmount = Integer.parseInt(params.get("bettingAmount"));
		configFilePath = params.get("configFilePath");

		if (bettingAmount < 1) {

			throw new IllegalArgumentException("Please provide a positive betting amount");

		}
		
		ScratchGameConfig config = JsonReader.readConfigFile(configFilePath);

		ScratchGame game = new ScratchGame();
		int rows = config.getRows();
		int columns = config.getColumns();

		String[][] matrix = game.generateMatrix(rows, columns, config);
		game.calculateReward(bettingAmount, matrix, config);
		game.printResponse();

	}

	private static HashMap<String, String> getParams(String[] args) {

		HashMap<String, String> params = new HashMap<String, String>();
		String configFilePath = null;
		String bettingAmount = null;

		// Parse command-line arguments
		for (int i = 0; i < args.length; i++) {
			if ("--config".equals(args[i]) && i + 1 < args.length) {
				configFilePath = args[i + 1];
				i++;
			} else if ("--betting-amount".equals(args[i]) && i + 1 < args.length) {
				try {
					bettingAmount = args[i + 1];
				} catch (NumberFormatException e) {
					System.out.println("Invalid betting amount. Please provide a valid integer.");

				}
				i++;
			}
		}
		params.put("configFilePath", configFilePath);
		params.put("bettingAmount", bettingAmount);
		return params;

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

					bonusSymbol = this.getRandomBonusSymbol(config.getProbabilities().getBonusSymbols());
					
					if (bonusSymbol != null && bonusSymbol != "MISS") {
						bonusSymbolAlreadyAdded = Boolean.TRUE;
					    symbol = bonusSymbol;

					}
				}
				// Place the chosen symbol in the matrix
				matrix[currentRow][currentColumn] = symbol;

			}
		}

		this.getResponse().setMatrix(matrix);
		return matrix;

	}

	private double calculateReward(int betAmount, String[][] matrix, ScratchGameConfig config) {

		// Create a HashMap to store each symbol counts
		double totalRewards = 0;
		double symbolRewardMultiplier = 0;
		double winCombinationRewardMultiplier = 0;
		double horizontalRewardMultiplier = 0;
		double verticalRewardMultiplier = 0;
		String winCombinationName;
		Map<String, List<String>> winCombinations = new HashMap<>();
		Map<String, Integer> symbolMapCounts = new HashMap<>();
		List<String> combinations;
		boolean winningGame = Boolean.FALSE;
		boolean hasSameSymbolsHorizontally = Boolean.FALSE;
		boolean hasSameSymbolsVertically = Boolean.FALSE;

		// get how many times a symbol appears in the matrix
		for (String[] row : matrix) {
			for (String symbol : row) {
				// Update the count of each symbol
				symbolMapCounts.put(symbol, symbolMapCounts.getOrDefault(symbol, 0) + 1);
			}
		}

		for (Map.Entry<String, Integer> entry : symbolMapCounts.entrySet()) {

			if (entry.getValue() >= 3) { // if >= 3 it is a winning symbol

				winningGame = Boolean.TRUE;
				combinations = new ArrayList<String>();
				symbolRewardMultiplier = config.getSymbolRewardMultiplier(entry.getKey());
				// get the win combination reward multiplier
				winCombinationRewardMultiplier = config.getWinCombinationRewardMultiplier(entry.getValue());
				winCombinationName = config.getWinCombinationName(entry.getValue());
				combinations.add(winCombinationName);
				winCombinations.put(entry.getKey(), combinations);
				totalRewards += betAmount * symbolRewardMultiplier * winCombinationRewardMultiplier;

				hasSameSymbolsHorizontally = this.hasSameSymbolsHorizontally(entry.getKey(), matrix);

				if (hasSameSymbolsHorizontally) {

					winCombinationName = SAME_SYMBOLS_HORIZONTALLY;
					combinations.add(winCombinationName);
					horizontalRewardMultiplier = config.getHorizontalRewardMultiplier();
					totalRewards *= horizontalRewardMultiplier;
				}

				hasSameSymbolsVertically = this.hasSameSymbolsVertically(entry.getKey(), matrix);

				if (hasSameSymbolsVertically) {

					winCombinationName = SAME_SYMBOLS_VERTICALLY;
					combinations.add(winCombinationName);
					verticalRewardMultiplier = config.getVerticalRewardMultiplier();
					totalRewards *= verticalRewardMultiplier;
				}

				// (bet_amount x reward(symbol_A) x reward(same_symbol_5_times) x
				// reward(same_symbols_vertically))
			}

			if (winningGame && SymbolChecker.isBonusSymbolMultiplier(entry.getKey())) {
				this.getResponse().setAppliedBonusSymbol(entry.getKey());
				totalRewards *= config.getSymbolRewardMultiplier(entry.getKey());

			}

			if (winningGame && SymbolChecker.isBonusSymbolExtra(entry.getKey())) {
				this.getResponse().setAppliedBonusSymbol(entry.getKey());
				totalRewards += config.getSymbolExtra(entry.getKey());

			}
		}

		this.getResponse().setReward(totalRewards);
		if (!winCombinations.isEmpty()) {
			this.getResponse().setAppliedWinningCombinations(winCombinations);
		}

		return totalRewards;

	}

	public boolean hasSameSymbolsHorizontally(String symbol, String[][] matrix) {

		boolean allSame;
		for (int row = 0; row < matrix.length; row++) {
			allSame = true; // Flag to track if all symbols in the row are the same as the given symbol
			for (int col = 0; col < matrix[row].length; col++) {
				if (!matrix[row][col].equals(symbol)) {
					allSame = false;
					break;
				}
			}
			if (allSame) {
				return true; // Found a row where the symbol is repeated
			}
		}
		return false; // No rows found with the repeated symbol
	}

	/**
	 * Checks if all symbols in any column of the matrix are the same.
	 *
	 * @param matrix the 2D array representing the matrix
	 * @return true if any column contains the same symbols, false otherwise
	 */
	public boolean hasSameSymbolsVertically(String symbol, String[][] matrix) {

		for (int col = 0; col < matrix[0].length; col++) {
			boolean allSame = true; // Flag to track if all symbols in the column are the same as the given symbol
			for (int row = 0; row < matrix.length; row++) {
				if (!matrix[row][col].equals(symbol)) {
					allSame = false;
					break; // No need to check further in this column
				}
			}
			if (allSame) {
				return true; // Found a column where the symbol is repeated
			}
		}
		return false; // No columns found with the repeated symbol
	}

	private String getRandomBonusSymbol(BonusSymbols bonusSymbols) {

		int totalSum = bonusSymbols.getSymbols().values().stream().mapToInt(Integer::intValue).sum();
		Map<String, Double> cumulativeProbabilities = new HashMap<>();

		String bonusSymbol = null;
		double probability;

		for (Map.Entry<String, Integer> entry : bonusSymbols.getSymbols().entrySet()) {

			probability = entry.getValue() / (double) totalSum;
			cumulativeProbabilities.put(entry.getKey(), probability);
		}

		Random random = new Random();

		double randomValue = random.nextDouble();

		double accumulatedWeight = 0;
		for (Map.Entry<String, Double> entry : cumulativeProbabilities.entrySet()) {
			accumulatedWeight += entry.getValue();
			if (randomValue <= accumulatedWeight) {
				bonusSymbol = entry.getKey();
				break;
			}

		}

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
		double probability;
		Map<String, Double> cumulativeProbabilities = new HashMap<>();

		for (StandardSymbol symbol : symbolProbabilities) {

			if (symbol.getRow() == currentRow && symbol.getColumn() == currentColumn) {
				totalSum += symbol.getSymbols().values().stream().mapToInt(Integer::intValue).sum();

				for (Map.Entry<String, Integer> entry : symbol.getSymbols().entrySet()) {

					probability = entry.getValue() / (double) totalSum;
					cumulativeProbabilities.put(entry.getKey(), probability);

				}

				break;

			}

		}

		// Pick a random number based on the total weight
		if (totalSum == 0) {
			throw new RuntimeException(
					"Json is incomplete. Some pair (row, column) does not have it probability definition. Modify the config.json");
		}

		double randomValue = random.nextDouble();
		String chosenSymbol = null;

		double accumulatedWeight = 0;
		for (Map.Entry<String, Double> entry : cumulativeProbabilities.entrySet()) {
			accumulatedWeight += entry.getValue();
			if (randomValue <= accumulatedWeight) {
				chosenSymbol = entry.getKey();
				break;
			}

		}

		return chosenSymbol;
	}

	public GameResponse getResponse() {
		return response;
	}

	public void setResponse(GameResponse response) {
		this.response = response;
	}

}
