package visualsorting;

/**
 * Credit: https://stackoverflow.com/questions/6010843/java-how-to-store-data-triple-in-a-list
 * @param <T>
 * @param <U>
 * @param <V> 
 */
public class Triplet<T, U, V> {

    private final T first;
    private final U second;
    private final V third;

    public Triplet(T first, U second, V third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public T getFirst() { return first; }
    public U getSecond() { return second; }
    public V getThird() { return third; }
}