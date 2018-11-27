/**
 * 
 */
package auctionhouse;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * @author pbj
 *
 */
public class MoneyTest {

    @Test    
    public void testAdd() {
        Money val1 = new Money("12.34");
        Money val2 = new Money("0.66");
        Money result = val1.add(val2);
        assertEquals("13.00", result.toString());
    }

    /*
     ***********************************************************************
     * BEGIN MODIFICATION AREA
     ***********************************************************************
     * Add all your JUnit tests for the Money class below.
     */

    @Test    
    public void testEquals() {
        Money val1 = new Money("12.55");
        Money val2 = new Money("12.55");
        Boolean result = val1.equals(val2);
        assertEquals(true, result);
    }

    @Test  
    public void testSubtract() {
        Money val1 = new Money("12.34");
        Money val2 = new Money("0.66");
        Money result = val1.subtract(val2);
        assertEquals("11.68", result.toString());
    }
    @Test  
    public void testAddPercent() {
        Money val1 = new Money("10");
        double percent1 = 10;
        String result1 = val1.addPercent(percent1).toString();
        assertEquals("11.00", result1);
        Money val2 = new Money("55");
        double percent2 = 90;
        String result2 = val2.addPercent(percent2).toString();
        assertEquals("104.50", result2);
    }
    @Test 
    public void testLessEquals() {
        Money val1 = new Money("12.55");
        Money val2 = new Money("12.55");
        Boolean result = val1.lessEqual(val2);
        assertEquals(true, result);
        Money val3 = new Money("12.5");
        result = val1.lessEqual(val3);
        assertEquals(false, result);
        Money val4 = new Money("12");
        result = val4.lessEqual(val1);
        assertEquals(true, result);

    }
    @Test 
    public void testCompareTo() {
        Money val1 = new Money("12.55");
        Money val2 = new Money("12.55");
        boolean result = val1.compareTo(val2) == 0;
        assertEquals(true, result);
        Money val3 = new Money("10.0");
        result = val1.compareTo(val3) < 0;
        assertEquals(false, result);
        Money val4 = new Money("12");
        result = val1.compareTo(val4) > 0;
        assertEquals(true, result);

    }

    /*
     * Put all class modifications above.
     ***********************************************************************
     * END MODIFICATION AREA
     ***********************************************************************
     */


}
