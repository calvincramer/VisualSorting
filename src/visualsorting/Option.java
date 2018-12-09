package visualsorting;

/**
* Represents an option
* @param <T> type of the option
*/
public class Option <T> {
   private T data;

   
   public Option(T data) {
       this.data = data;
   }

   
   public void setData(T data) {
       this.data = data;
   }

   
   public T getData() {
       return data;
   }

   
   @Override
   public String toString() {
       return data.toString();
   }
}