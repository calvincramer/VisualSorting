package visualsorting;

import java.awt.Color;
import java.awt.Insets;
import visualsorting.sorters.InsertionSort;
import visualsorting.StartArrayFactory.*;
import visualsorting.Transition.TransitionType;

/**
 * Encapsulates all of the options
 * @author Calvin
 */
public class Options {
    //all options
    public Class<?> SORTER_CLASS    = DEFAULT_SORTER_CLASS;
    public int NUM_ELEMENTS         = DEAULT_NUM_ELEMENTS;
    public String SOUND_PACK        = DEAULT_SOUND_PACK;
    public int CLOCK_SPEED          = DEAULT_CLOCK_SPEED;
    public int START_DELAY          = DEAULT_START_DELAY;
    public int NUM_SIMUL_SOUNDS     = DEAULT_NUM_SIMUL_SOUNDS;
    public NumberType START_ARRAY_NUMBERS_TYPE  = DEAULT_START_ARRAY_NUMBERS_TYPE;
    public ArrayStructure START_ARRAY_STRUCTURE = DEAULT_START_ARRAY_STRUCTURE;
    
    public double GAP_WIDTH         = DEAULT_GAP_WIDTH;
    public Color DEFAULT_COLOR      = DEAULT_DEFAULT_COLOR;
    public Color SELECTED_COLOR     = DEFAULT_SELECTED_COLOR;
    public Color SWAP_COLOR_1       = DEFAULT_SWAP_COLOR_1;
    public Color SWAP_COLOR_2       = DEFAULT_SWAP_COLOR_2;
    public Color BACKGROUND_COLOR   = DEAULT_BACKGROUND_COLOR;
    public Color TEXT_COLOR         = DEAULT_TEXT_COLOR;
    public boolean IS_ANIMATED      = DEAULT_IS_ANIMATED;
    public TransitionType TRANSITION_TYPE = DEFAULT_TRANSITION_TYPE;
    public boolean SHOW_SWAP_ARROWS = DEAULT_SHOW_SWAP_ARROWS;
    public Color SWAP_ARROW_COLOR   = DEFAULT_SWAP_ARROW_COLOR;
    public String FONT_FAMILY       = DEAULT_FONT_FAMILY;
    public int FONT_SIZE            = DEAULT_FONT_SIZE;
    public boolean ANTI_ALIAS       = DEAULT_ANTI_ALIAS;
    public boolean ANTI_ALIAS_FONT  = DEAULT_ANTI_ALIAS_FONT;
    public Insets GRAPH_INSETS      = DEAULT_GRAPH_INSETS;
    
    //default values
    private static final Class<?> DEFAULT_SORTER_CLASS   = InsertionSort.class;
    private static final int DEAULT_NUM_ELEMENTS         = 64;
    private static final String DEAULT_SOUND_PACK        = "piano";
    private static final int DEAULT_CLOCK_SPEED          = 70;
    private static final int DEAULT_START_DELAY          = 1000;
    private static final int DEAULT_NUM_SIMUL_SOUNDS     = 1;
    private static final NumberType DEAULT_START_ARRAY_NUMBERS_TYPE  = NumberType.UNIQUE;
    private static final ArrayStructure DEAULT_START_ARRAY_STRUCTURE = ArrayStructure.SHUFFLED;
    
    private static final double DEAULT_GAP_WIDTH            = 0.5;
    private static final Color DEAULT_DEFAULT_COLOR         = new Color(0  , 120, 255);
    private static final Color DEFAULT_SELECTED_COLOR       = new Color(0  , 255, 180);
    private static final Color DEFAULT_SWAP_COLOR_1         = new Color(255, 0  , 255);
    private static final Color DEFAULT_SWAP_COLOR_2         = new Color(183, 74 , 247);
    private static final Color DEAULT_BACKGROUND_COLOR      = new Color(20 , 20 , 20 );
    private static final Color DEAULT_TEXT_COLOR            = Color.WHITE;
    private static final boolean DEAULT_IS_ANIMATED         = false;
    private static final TransitionType DEFAULT_TRANSITION_TYPE     = TransitionType.LINEAR;
    private static final boolean DEAULT_SHOW_SWAP_ARROWS    = false;
    private static final Color DEFAULT_SWAP_ARROW_COLOR     = new Color(0  , 255, 0  );
    private static final String DEAULT_FONT_FAMILY          = "Courier New";
    private static final int DEAULT_FONT_SIZE               = 16;
    private static final boolean DEAULT_ANTI_ALIAS          = true;
    private static final boolean DEAULT_ANTI_ALIAS_FONT     = true;
    private static final Insets DEAULT_GRAPH_INSETS         = new Insets(15, 15, 15, 15);
    
    /**
     * Creates default options object
     */
    public Options() {
        //do nothing
    }
    
    /**
     * Parses the options
     * @param options the array of option lines from the file
     */
    public Options(String[] options) {
        for (String optionLine : options) {
                //System.out.println(s);
                String[] optionNameValue = optionLine.split("=");
                if (optionNameValue.length != 2)
                    continue;
                String option = optionNameValue[0].trim();
                String optionValue = optionNameValue[1].trim();
                
                if      (option.equals("SORTER_CLASS"))             this.SORTER_CLASS =             readAsSorterClass(optionValue);
                else if (option.equals("NUM_ELEMENTS"))             this.NUM_ELEMENTS =             readAsInt(optionValue);
                else if (option.equals("SOUND_PACK"))               this.SOUND_PACK =               readAsString(optionValue);
                else if (option.equals("CLOCK_SPEED"))              this.CLOCK_SPEED =              readAsInt(optionValue);
                else if (option.equals("START_DELAY"))              this.START_DELAY =              readAsInt(optionValue);
                else if (option.equals("NUM_SIMUL_SOUNDS"))         this.NUM_SIMUL_SOUNDS =         readAsInt(optionValue);
                else if (option.equals("START_ARRAY_NUMBERS_TYPE")) this.START_ARRAY_NUMBERS_TYPE = readAsNumberType(optionValue);
                else if (option.equals("START_ARRAY_STRUCTURE"))    this.START_ARRAY_STRUCTURE =    readAsArrayStructure(optionValue);
                else if (option.equals("GAP_WIDTH"))                this.GAP_WIDTH =                readAsDouble(optionValue);
                else if (option.equals("DEFAULT_COLOR"))            this.DEFAULT_COLOR =            readAsColor(optionValue);
                else if (option.equals("SELECTED_COLOR"))           this.SELECTED_COLOR =           readAsColor(optionValue);
                else if (option.equals("SWAP_COLOR_1"))             this.SWAP_COLOR_1 =             readAsColor(optionValue);
                else if (option.equals("SWAP_COLOR_2"))             this.SWAP_COLOR_2 =             readAsColor(optionValue);
                else if (option.equals("BACKGROUND_COLOR"))         this.BACKGROUND_COLOR =         readAsColor(optionValue);
                else if (option.equals("TEXT_COLOR"))               this.TEXT_COLOR =               readAsColor(optionValue);
                else if (option.equals("IS_ANIMATED"))              this.IS_ANIMATED =              readAsBool(optionValue);
                else if (option.equals("TRANSITION_TYPE"))          this.TRANSITION_TYPE =          readAsTransition(optionValue);
                else if (option.equals("SHOW_SWAP_ARROWS"))         this.SHOW_SWAP_ARROWS =         readAsBool(optionValue);
                else if (option.equals("SWAP_ARROW_COLOR"))        this.SWAP_ARROW_COLOR =         readAsColor(optionValue);
                else if (option.equals("FONT_FAMILY"))              this.FONT_FAMILY =              readAsString(optionValue);
                else if (option.equals("FONT_SIZE"))                this.FONT_SIZE =                readAsInt(optionValue);
                else if (option.equals("ANTI_ALIAS"))               this.ANTI_ALIAS =               readAsBool(optionValue);
                else if (option.equals("ANTI_ALIAS_FONT"))          this.ANTI_ALIAS_FONT =          readAsBool(optionValue);
                else if (option.equals("GRAPH_INSETS"))             this.GRAPH_INSETS =             readAsInsets(optionValue);
                else {
                    System.out.println("DID NOT RECOGNIZE THE OPTION: " + option + "  FOR: " + optionLine);
                }
        }
    }
    
    
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


}
