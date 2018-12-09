package visualsorting;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Util {
    
    /**
     * Reads a file and returns each line as a String in an array
     * @param url
     * @return 
     */
    protected static String[] readFile(File file) {
        Scanner reader;
        List<String> lines = new ArrayList<>();

        try {
            reader = new Scanner(file);
            while (reader.hasNextLine())
                lines.add(reader.nextLine());
        } 
        catch (Exception e) { e.printStackTrace(); }
        
        return lines.toArray(new String[lines.size()]);
    }
    
    
    /**
     * Writes a file
     * @param data
     * @param url
     * @return 
     */
    protected static boolean writeFile(String data, String url) {
        
        try {
            OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(url), StandardCharsets.UTF_8);
            writer.write(data);
            writer.close();
            
        } catch (FileNotFoundException ex) { 
            ex.printStackTrace(); 
            return false;
        } catch (IOException ex) {
            ex.printStackTrace(); 
            return false;
        }
        return true;
    }
    
    
    /**
     * integer[] |-> string in the form: "n1 n2 n3 n4 ..."
     * @param a
     * @return 
     */
    protected static String toStringArr(int[] a) {
        String s = "";
        for (int n : a) {
            s += n + " ";
        }
        return s;
    }
    
    
    /**
     * Places commas in a string according to where they would be in an integer number
     * @param numStr
     * @return 
     */
    protected static String commifyString(String numStr) {
        int i = numStr.length() - 3;
        while (i >= 1) {
            numStr = numStr.substring(0, i) + "," + numStr.substring(i);
            i -= 3;
        }
        return numStr;
    }
    
    
    /**
     * Determines if the array contains the number i
     * @param i
     * @param arr
     * @return 
     */
    protected static boolean contains(int i, int[] arr) {
        if (arr == null)
            return false;
        for (int n : arr) {
            if (i == n)
                return true;
        }
        return false;
    }
    
    
    /**
     * Shuffles an array
     * @param array
     * @return 
     */
    public static int[] shuffleArray(int[] array) {
        Random r = new Random(System.currentTimeMillis());
        for (int i = array.length - 1; i > 0; i--) {
          int index = r.nextInt(i + 1);
          
          // swap
          int a = array[index];
          array[index] = array[i];
          array[i] = a;
        }
        return array;
    }
}
