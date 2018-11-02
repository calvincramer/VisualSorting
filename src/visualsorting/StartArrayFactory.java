/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visualsorting;

/**
 * Factory class to generate an array given a certain structure
 * @author CalvinLaptop
 */
public class StartArrayFactory {
    
    /**
     * Determines how to choose the numbers that are going to be in the array
     */
    public enum NumberType {
        UNIQUE, 
        SOME_PAIRS, 
        ALL_PAIRED, 
        SOME_TRIPLETS, 
        MANY_TRIPLETS, 
        CHOOSE_RANDOMLY
    }
    
    /**
     * The overall structure of the whole array
     */
    public enum ArrayStructure {
        ASCENDING, DESCENDING, SHUFFLED, PARTIALLY_SHUFFLED
    }
    
    public static int[] generate(int size) {
        return generate(size, ArrayStructure.SHUFFLED, NumberType.UNIQUE);
    }
    
    public static int[] generate(int size, NumberType numType) {
        return generate(size, ArrayStructure.SHUFFLED, numType);
    }
    
    public static int[] generate(int size, ArrayStructure structure) {
        return generate(size, structure, NumberType.UNIQUE);
    }
    
    /**
     * TODO: IMPLEMENT
     * @param size
     * @param structure
     * @param numType
     * @return 
     */
    public static int[] generate(int size, ArrayStructure structure, NumberType numType) {
        System.out.println("NOT IMPLEMENTED YET");
        return null;
    }

}
