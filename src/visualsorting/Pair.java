package visualsorting;

/**
 * @param <T>
 * @param <U>
 */
public class Pair<T, U> {

    private final T first;
    private final U second;

    public Pair(T first, U second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() { return first; }
    public T getKey() { return first; }
    
    public U getSecond() { return second; }
    public U getValue() { return second; }
}