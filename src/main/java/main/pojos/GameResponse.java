package main.pojos;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class GameResponse {
    private String[][] matrix;  // 2D array for the matrix
    private double reward;  // Reward value
    private Map<String, List<String>> appliedWinningCombinations;  // Map for winning combinations
    private String appliedBonusSymbol;  // Applied bonus symbol

    // Getters and Setters
    public String[][] getMatrix() {
        return matrix;
    }

    public void setMatrix(String[][] matrix) {
        this.matrix = matrix;
    }

    public double getReward() {
        return reward;
    }

    public void setReward(double totalReward) {
        this.reward = totalReward;
    }

    public Map<String, List<String>> getAppliedWinningCombinations() {
        return appliedWinningCombinations;
    }

    public void setAppliedWinningCombinations(Map<String, List<String>> appliedWinningCombinations) {
        this.appliedWinningCombinations = appliedWinningCombinations;
    }

    public String getAppliedBonusSymbol() {
        return appliedBonusSymbol;
    }

    public void setAppliedBonusSymbol(String appliedBonusSymbol) {
        this.appliedBonusSymbol = appliedBonusSymbol;
    }

	public void printResponse() {
		 
		//cuando no es winning no mostrar applied wining combinations ni bonus symbol
		ObjectMapper objectMapper = new ObjectMapper();
		 try {
	            String jsonOutput = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(this);
	            System.out.println("JSON Representation of the MatrixResponse Object:");
	            System.out.println(jsonOutput);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		
	}
}