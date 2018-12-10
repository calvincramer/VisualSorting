package visualsorting;

import java.awt.Color;
import java.awt.Insets;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import visualsorting.sorters.InsertionSort;
import visualsorting.StartArrayFactory.*;
import visualsorting.Transition.TransitionType;

/**
 * Encapsulates all of the options
 * @author Calvin
 */
public class Options {
    
    //default values
    private static final Class<?> DEFAULT_SORTER_CLASS   = InsertionSort.class;
    private static final int DEFAULT_NUM_ELEMENTS         = 64;
    private static final String DEFAULT_SOUND_PACK        = "piano";
    private static final int DEFAULT_CLOCK_SPEED          = 70;
    private static final int DEFAULT_START_DELAY          = 1000;
    private static final int DEFAULT_NUM_SIMUL_SOUNDS     = 1;
    private static final NumberType DEFAULT_START_ARRAY_NUMBERS_TYPE  = NumberType.UNIQUE;
    private static final ArrayStructure DEFAULT_START_ARRAY_STRUCTURE = ArrayStructure.SHUFFLED;
    private static final double DEFAULT_GAP_WIDTH            = 0.5;
    private static final Color DEFAULT_DEFAULT_COLOR         = new Color(0  , 120, 255);
    private static final Color DEFAULT_SELECTED_COLOR       = new Color(0  , 255, 180);
    private static final Color DEFAULT_SWAP_COLOR_1         = new Color(255, 0  , 255);
    private static final Color DEFAULT_SWAP_COLOR_2         = new Color(183, 74 , 247);
    private static final Color DEFAULT_BACKGROUND_COLOR      = new Color(20 , 20 , 20 );
    private static final Color DEFAULT_TEXT_COLOR            = Color.WHITE;
    private static final boolean DEFAULT_IS_ANIMATED         = false;
    private static final TransitionType DEFAULT_TRANSITION_TYPE     = TransitionType.LINEAR;
    private static final boolean DEFAULT_SHOW_SWAP_ARROWS    = false;
    private static final Color DEFAULT_SWAP_ARROW_COLOR     = new Color(0  , 255, 0  );
    private static final String DEAULT_FONT_FAMILY          = "Courier New";
    private static final int DEFAULT_FONT_SIZE               = 16;
    private static final boolean DEFAULT_ANTI_ALIAS          = true;
    private static final boolean DEFAULT_ANTI_ALIAS_FONT     = true;
    private static final Insets DEFAULT_GRAPH_INSETS         = new Insets(15, 15, 15, 15);
    
    
    /**
     * Option enum that lists ALL options
     * Or class option that specifies the data type for the options as well?
     * 
     * If the file does not have all options, add those options to the file
     * If the file doesn't specify all options, default ones are used for the ones not specified 
     * 
     * What about the options that are enums, should print all options in the options file?
     * 
     * SOME WAY FOR ELSEWHERE IN THE PROJECT TO GET OPTION VALUE WITHOUT CASTING?
     */
    private static final Map<String, Option<?>> allOptionsDefault;
    private Map<String, Option<?>> optionsMap;
    static {
        //initialize allOptionsDefault
        allOptionsDefault = new HashMap<String, Option<?>>() {{
            put("SORTER_CLASS",         new Option<>(DEFAULT_SORTER_CLASS));
            put("NUM_ELEMENTS",         new Option<>(DEFAULT_NUM_ELEMENTS));
            put("SOUND_PACK",           new Option<>(DEFAULT_SOUND_PACK));
            put("CLOCK_SPEED",          new Option<>(DEFAULT_CLOCK_SPEED));
            put("START_DELAY",          new Option<>(DEFAULT_START_DELAY));
            put("NUM_SIMUL_SOUNDS",     new Option<>(DEFAULT_NUM_SIMUL_SOUNDS));
            put("START_ARRAY_NUMBERS_TYPE", new Option<>(DEFAULT_START_ARRAY_NUMBERS_TYPE));
            put("START_ARRAY_STRUCTURE", new Option<>(DEFAULT_START_ARRAY_STRUCTURE));
            put("GAP_WIDTH",            new Option<>(DEFAULT_GAP_WIDTH));
            put("DEFAULT_COLOR",        new Option<>(DEFAULT_DEFAULT_COLOR));
            put("SELECTED_COLOR",       new Option<>(DEFAULT_SELECTED_COLOR));
            put("SWAP_COLOR_1",         new Option<>(DEFAULT_SWAP_COLOR_1));
            put("SWAP_COLOR_2",         new Option<>(DEFAULT_SWAP_COLOR_2));
            put("BACKGROUND_COLOR",     new Option<>(DEFAULT_BACKGROUND_COLOR));
            put("TEXT_COLOR",           new Option<>(DEFAULT_TEXT_COLOR));
            put("IS_ANIMATED",          new Option<>(DEFAULT_IS_ANIMATED));
            put("SHOW_SWAP_ARROWS",     new Option<>(DEFAULT_SHOW_SWAP_ARROWS));
            put("FONT_FAMILY",          new Option<>(DEAULT_FONT_FAMILY));
            put("FONT_SIZE",            new Option<>(DEFAULT_FONT_SIZE));
            put("ANTI_ALIAS",           new Option<>(DEFAULT_ANTI_ALIAS));
            put("ANTI_ALIAS_FONT",      new Option<>(DEFAULT_ANTI_ALIAS_FONT));
            put("GRAPH_INSETS",         new Option<>(DEFAULT_GRAPH_INSETS));
            put("SWAP_ARROW_COLOR",     new Option<>(DEFAULT_SWAP_ARROW_COLOR));
        }};
    }
    
    public static void main(String[] args) {
        Option<Integer> opt = new Option<>(new Integer(5));
        Integer data = opt.getData();
        System.out.println(opt.getData());
        System.out.println(opt.getData().getClass());

        Option<String> opt2 = new Option<>("abc");
        String data2 = opt2.getData();
        System.out.println(opt2.getData());
        System.out.println(opt2.getData().getClass());
        
        Option<NumberType> opt3 = new Option<NumberType>(DEFAULT_START_ARRAY_NUMBERS_TYPE);
        NumberType data3 = opt3.getData();
        System.out.println(opt3.getData());
        System.out.println(opt3.getData().getClass());
    }
    
    
    /**
     * Creates default options object
     */
    public Options() {
        this(new String[0]);
    }
    
    
    /**
     * Parses the options
     * @param options the array of option lines from the file
     */
    public Options(String[] options) {
        //initialize options map
        this.optionsMap = new HashMap<>();
        //populate options map with default values
        for (Entry<String, Option<?>> entry : Options.allOptionsDefault.entrySet())
            this.optionsMap.put(entry.getKey(), entry.getValue());
        //read options from options array, replace option values
        for (String optionLine : options) {
                //System.out.println(s);
                String[] optionNameValue = optionLine.split("=");
                if (optionNameValue.length != 2)    //disregard any line that doesn't have exactly one '='
                    continue;
                String option = optionNameValue[0].trim();
                String optionValue = optionNameValue[1].trim();
                
                this.setOption(option, optionValue);
        }
    }
    
    
    public Option<?> getOption(String optionStr) {
        return this.optionsMap.get(optionStr);
    }
    
  
    /**
     * Called at initialization, sets the option given the specified default class for the data
     * @param option
     * @param data 
     */
    private void setOption(String option, String data) {
        //try to find option in optionsMap
        if (!this.optionsMap.containsKey(option)) {
            System.err.println("UNRECOGNIZED OPTION: " + option);
            return;
        }

        //get class of option to set
        //System.out.println(option);
        Option actOption = this.optionsMap.get(option);
        //System.out.println(actOption.getData());
        Class<?> optionClass = this.optionsMap.get(option).getData().getClass();
        //System.out.println(optionClass);
        
        //set option according to the class
        if (optionClass == Integer.class)       this.optionsMap.put(option, new Option<Integer>(readAsInt(data)));
        else if (optionClass == Double.class)   this.optionsMap.put(option, new Option<Double>(readAsDouble(data)));
        else if (optionClass == String.class)   this.optionsMap.put(option, new Option<String>(readAsString(data)));
        else if (optionClass == NumberType.class)   this.optionsMap.put(option, new Option<NumberType>(readAsNumberType(data)));
        else if (optionClass == ArrayStructure.class)   this.optionsMap.put(option, new Option<ArrayStructure>(readAsArrayStructure(data)));
        else if (optionClass == Color.class)        this.optionsMap.put(option, new Option<Color>(readAsColor(data)));
        else if (optionClass == Boolean.class)   this.optionsMap.put(option, new Option<Boolean>(readAsBool(data)));
        else if (optionClass == Insets.class)   this.optionsMap.put(option, new Option<Insets>(readAsInsets(data)));
        else if (optionClass == Class.class)    this.optionsMap.put(option, new Option<Class<?>>(readAsSorterClass(data)));
        else {
            System.err.println("Recognized the option name, but could not read the option as a/an: " + optionClass);
            return;
        }
        
        //System.out.println("After setting: " + this.optionsMap.get(option));
        //System.out.println("");
    }
    
    
    //convienence methods to read strings as certain types
    //<editor-fold defaultstate="collapsed" desc="readAs...  ">
    public Class<?> readAsSorterClass(String s) {
        try {
            return Class.forName("visualsorting.sorters." + s);
        } catch (ClassNotFoundException ex) {
            System.out.println("COULD NOT FIND THE CLASS: " + s);
            ex.printStackTrace();
            System.exit(1);
        }
        return null;
    }
    
    
    public int readAsInt(String s) {
        return Integer.parseInt(s);
    }
    
    
    public double readAsDouble(String s) {
        return Double.parseDouble(s);
    }
    
    
    public String readAsString(String s) {
        return s.trim();
    }
    
    
    public NumberType readAsNumberType(String s) {
        s = s.trim();
        if      (s.equals("UNIQUE"))            return NumberType.UNIQUE;
        else if (s.equals("SOME_PAIRS"))        return NumberType.SOME_PAIRS;
        else if (s.equals("ALL_PAIRED"))        return NumberType.ALL_PAIRED;
        else if (s.equals("CHOOSE_RANDOMLY"))   return NumberType.CHOOSE_RANDOMLY;
        else {
            System.out.println("COULD NOT RECOGNIZE THE NUMBER TYPE");
            System.exit(1);
        }
        return null;
    }
    
    
    public ArrayStructure readAsArrayStructure(String s) {
        s = s.trim();
        if (s.equals("ASCENDING"))                  return ArrayStructure.ASCENDING;
        else if (s.equals("DESCENDING"))            return ArrayStructure.DESCENDING;
        else if (s.equals("SHUFFLED"))              return ArrayStructure.SHUFFLED;
        else if (s.equals("PARTIALLY_SHUFFLED"))    return ArrayStructure.PARTIALLY_SHUFFLED;
        else {
            System.out.println("COULD NOT RECOGNIZE THE ARRAY STRUCTURE");
            System.exit(1);
        }
        return null;
    }
    
    
    public Color readAsColor(String s) {
        String[] split = s.trim().split(" ");
        if (split.length == 3) {
            return new Color(readAsInt(split[0]),
                    readAsInt(split[1]),
                    readAsInt(split[2]) );
        }
        else if (split.length == 4) {
            return new Color(readAsInt(split[0]),
                    readAsInt(split[1]),
                    readAsInt(split[2]),
                    readAsInt(split[3]) );
        }
        else {
            System.out.println("OPTION DOES NOT HAVE ENOUGH FIELDS FOR A COLOR TYPE");
            System.exit(1);
        }
        return null;
    }
    
    
    public boolean readAsBool(String s) {
        if (s.trim().equals("true"))
            return true;
        else if (s.trim().equals("false"))
            return false;
        else {
            System.out.println("BOOLEAN CAN ONLY BE 'true' OR 'false'.");
            System.exit(1);
        }
        return false;
    }
    
    
    public Insets readAsInsets(String s) {
        String[] split = s.trim().split(" ");
        if (split.length != 4) {
            System.out.println("INSETS MUST HAVE 4 INTEGERS");
            System.exit(1);
        }
        return new Insets(readAsInt(split[0]),
                readAsInt(split[1]),
                readAsInt(split[2]),
                readAsInt(split[3]) );
    }

  
    public TransitionType readAsTransition(String s) {
        s = s.trim();
        for (int i = 0; i < TransitionType.values().length; i++) {
            TransitionType type = TransitionType.values()[i];
            if (s.equals(type.toString()))
                return type;
        }
        System.out.println("COULD NOT RECOGNIZE THE TRANSITION NAME");
        System.exit(1);
        return null;
    }
    //</editor-fold>
}
