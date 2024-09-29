package main.util;

import java.util.HashSet;
import java.util.Set;

public class SymbolChecker {

    // Define the set of known bonus symbols
    private static final Set<String> BONUS_SYMBOLS_MULTIPLIER = new HashSet<>();
    
    private static final Set<String> BONUS_SYMBOLS_EXTRA = new HashSet<>();

    static {
    	BONUS_SYMBOLS_MULTIPLIER.add("10x");
    	BONUS_SYMBOLS_MULTIPLIER.add("5x");
      
    }
    
    static {
     
    	BONUS_SYMBOLS_EXTRA.add("+1000");
    	BONUS_SYMBOLS_EXTRA.add("+500");
     
    }

    // Function to check if a given symbol is a bonus symbol multiplier
    public static boolean isBonusSymbolMultiplier(String symbol) {
        return BONUS_SYMBOLS_MULTIPLIER.contains(symbol);
    }
    
 // Function to check if a given symbol is a bonus symbol extra
    public static boolean isBonusSymbolExtra(String symbol) {
        return BONUS_SYMBOLS_EXTRA.contains(symbol);
    }

}	