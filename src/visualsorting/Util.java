package visualsorting;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
    
    
    protected static String[] readFileFromJar(String fileName) {
        InputStream in = Util.class.getResourceAsStream("/" + fileName);
        if (in == null) {
            System.out.println("Could not load " + fileName + " as an inputstream from jar");
            return null;
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        if (reader == null) {
            System.out.println("Could not make BufferedReader from inputstream");
            return null;
        }
        
        List<String> lines = new ArrayList<>();
        try {
            String line = reader.readLine();
            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            System.out.println("IOerror while reading file");
            e.printStackTrace();
            return null;
        }
        
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
    protected static <T> String toStringArr(T[] a) {
        String s = "";
        for (T obj : a)
            s += obj.toString() + " ";
        return s;
    }
    
    /**
     * integer[] |-> string in the form: "n1 n2 n3 n4 ..."
     * @param a
     * @return 
     */
    protected static <T> String toStringArr(List<T> a) {
        String s = "";
        for (T obj : a)
            s += obj.toString() + " ";
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
     * Shuffles an array of generic type
     * Doesn't support primitive types :(
     * @param array
     * @return A shuffled array, such that there is no discernable pattern to the array
     */
    public static <T> T[] shuffleArray(T[] array) {
        Random r = new Random(System.currentTimeMillis());
        for (int i = array.length - 1; i > 0; i--) {
          int index = r.nextInt(i + 1);
          
          // swap
          T a = array[index];
          array[index] = array[i];
          array[i] = a;
        }
        return array;
    }
    
    /**
     * Shuffles an array of generic type
     * Doesn't support primitive types :(
     * @param array
     * @return A shuffled array, such that there is no discernable pattern to the array
     */
    public static <T> List<T> shuffleArray(List<T> list) {
        Random r = new Random(System.currentTimeMillis());
        for (int i = list.size() - 1; i > 0; i--) {
          int index = r.nextInt(i + 1);
          
          // swap
          T a = list.get(index);
          list.set(index, list.get(i));
          list.set(i, a);
        }
        return list;
    }
    
    /**
     * Determines if the algorithm is finished.
     * It is finished if the array is sorted.
     * @return 
     */
    public static <U extends Comparable<U>> boolean isSorted(U[] array) {
        for(int i = 0; i < array.length - 1; i++) {
            if (array[i].compareTo(array[i+1]) > 0)
                return false;
        }
        return true;
    }
    
    /**
     * Convenience method to swap two elements in the array  
     * @param i
     * @param j 
     */
    public static <U extends Comparable<U>> void swap(int i, int j, U[] array) {
        U temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
    
    /**
     * Convenience method to print an array in the form [o1, o2, ... , on]
     * @param array the input array
     */
    public static <T> void printArray(T[] array) {
        System.out.print("[");
        for (int i = 0; i < array.length - 1; i++)
            System.out.print(array[i].toString() + ", ");
        System.out.println(array[array.length - 1].toString() + "]");
    }
    
    /**
     * Convenience method to print an array in the form [o1, o2, ... , on]
     * @param array the input array
     */
    public static <T> void printArray(List<T> list) {
        System.out.print("[");
        for (int i = 0; i < list.size() - 1; i++)
            System.out.print(list.get(i).toString() + ", ");
        System.out.println(list.get(list.size() - 1).toString() + "]");
    }
    
    /**
     * Because I want the syntactic sugar like: Integer[] arr = {1,2,3};
     * @param <T>
     * @param arr
     * @return 
     */
    public static <T> List<T> oneLineInitList(T[] arr) {
        List<T> list = new ArrayList<>();
        for (T obj : arr)
            list.add(obj);
        return list;
    }
    
    /**
     * Converts an array to a list of any type
     * @param <T>
     * @param arr
     * @return 
     */
    public static <T> List<T> arrayToList(T[] arr) {
        return Util.oneLineInitList(arr);
    }
}
