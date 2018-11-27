/**
 * 
 */
package auctionhouse;

/**
 * @author pbj
 */
public class Money implements Comparable<Money> {

    private double value;

    private static long getNearestPence(double pounds) {
        return Math.round(pounds * 100.0);
    }
    /**
     * normalise the input of type double to by 100 
     *
     * @param pounds  amount of money in double type 
     * @return double  takes long returned by getNearestPence and divide it by 100.0           
     */
    private static double normalise(double pounds) {
        return getNearestPence(pounds) / 100.0;

    }
    /**
    *  Returns the nearest pence of the value inputted by using the round function from the Math library. 
    * @param pounds a double value that contains the currency to be rounded off to the nearest pence
    * @return long the rounded off value
    */
    public Money(String pounds) {
        value = normalise(Double.parseDouble(pounds));
    }

    private Money(double pounds) {
        value = pounds;
    }

    /**
     * It takes an object of money and it return an new object of its value plus the value of money given 
     *
     * @param m  Money object to be added  
     * @return Money  new Object of Money as result of the summation of value the given object to the object value           
     */
    public Money add(Money m) {
        return new Money(value + m.value);
    }

    /**
     * It takes an object of money and it return an new about of its value minus the value of money given 
     *
     * @param m  Money object to be substructed  
     * @return Money  new Object of Money as result of the substruction of value the given object to the object value           
     */
    public Money subtract(Money m) {
        return new Money(value - m.value);
    }
     /**
     * It takes a double of percentage to add and returns new normalised value of money raised by that percentages
     *
     * @param percent  double percentage to be raised to 
     * @return Money  new Object of Money as result of the perctage given passed to the method          
     */
    public Money addPercent(double percent) {
        return new Money(normalise(value * (1 + percent / 100.0)));
    }
     /**
     * It prints the value of money with 2 decimal points
     *
     * @return String  a string formatted text of two decimal point float numbers          
     */
    @Override
    public String toString() {
        return String.format("%.2f", value);

    }
    /**
     * It uses a generic interface Comparable mathod to compare the value of two Money objects
     *
     * @param Money object of money to be compared to
     * @return int  an integer value showing which object is greater than another based on their values        
     */
    public int compareTo(Money m) {
        return Long.compare(getNearestPence(value), getNearestPence(m.value));
    }
    /**
     * It return a boolean value to indicate if a given of is less or Equal to the object itself
     *
     * @param Money object of money to be compared to
     * @return Boolean  an true or false value to indicate if the condition of less or equal is true       
     */
    public Boolean lessEqual(Money m) {
        return compareTo(m) <= 0;
    }
    /**
     * It return a boolean value to indicate if two Money objects are equal
     *
     * @param Money object of money to be compared to
     * @return boolean  true or false if two objects have same value based on their values        
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Money))
            return false;
        Money oM = (Money) o;
        return compareTo(oM) == 0;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(getNearestPence(value));
    }

}
