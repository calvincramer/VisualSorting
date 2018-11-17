/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package visualsorting;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
        CHOOSE_RANDOMLY,
    }
    
    /**
     * The overall structure of the whole array
     */
    public enum ArrayStructure {
        ASCENDING, 
        DESCENDING, 
        SHUFFLED, 
        PARTIALLY_SHUFFLED,
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
        Random rng = new Random(System.currentTimeMillis());
        List<Integer> arr = new ArrayList<>(size);
        //get numbers
        switch (numType) {
            case UNIQUE: {
                for (int i = 1; i <= size; i++) 
                    arr.add(i);
                break;
            }
            case SOME_PAIRS: {
                for (int i = 1; i <= size; i++) 
                    arr.add(i);
                for (int n = 0; n < (int) size * 0.2; n++) {
                    int i = rng.nextInt(size - 1);
                    arr.set(i + 1, arr.get(i));
                }
                break;
            }
            case ALL_PAIRED: {
                for (int i = 1; i <= size; i++) 
                    arr.add(i);
                for (int i = 0; i < size - 1; i += 2) 
                    arr.set(i + 1, arr.get(i));
                break;
            }
            case CHOOSE_RANDOMLY: {
                for (int n = 1; n <= size; n++) {
                    arr.add(rng.nextInt(size) + 1);
                }
                arr.sort(null);
                break;
            }
            default: {
                System.out.println("Unrecognized number type");
                System.exit(1);
                  break;     
            }
        }
        
        //give structure to numbers
        switch (structure) {
            case ASCENDING: {
                arr.sort(null);
                break;
            }
            case DESCENDING: {
                arr.sort(null);
                //reverse array
                List<Integer> temp = new ArrayList<>();
                while (!arr.isEmpty())
                    temp.add(0, arr.remove(0));
                arr = temp;
                break;
            }
            case SHUFFLED: {
                for (int i = arr.size() - 1; i > 0; i--) {
                    int index = rng.nextInt(i + 1);
                    // swap
                    int temp = arr.get(index);
                    arr.set(index, arr.get(i));
                    arr.set(i, temp);
                }
                break;
            }
            case PARTIALLY_SHUFFLED: {
                for (int n = 1; n <= (int) size * 0.2; n++) {
                    int index1 = rng.nextInt(size);
                    int index2 = rng.nextInt(size);
                    // swap
                    int temp = arr.get(index1);
                    arr.set(index1, arr.get(index2));
                    arr.set(index2, temp);
                }
                break;
            }
            default: {
                System.out.println("Unrecognized array structure");
                System.exit(1);
                break;
            }
        }
        
        //convert into int array
        int[] toReturn = new int[size];
        for (int i = 0; i < size; i++)
            toReturn[i] = arr.get(i);
        return toReturn;
    }

}
