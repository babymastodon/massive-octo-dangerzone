package tests.common;

import org.junit.Test;

import static org.junit.Assert.*;

import common.Color;

/**
 * Test suite for Color.java. Color keeps its internal representation of colors
 * via three integers (RGB).
 * 
 * Testing strategy:
 * Create a new color and correctly observe the RGB values.
 * Create two colors with the same RGB values. Test their equality.
 * Create two colors with different RGB values. Test equality.
 * Create a color and test equality with a non-Color object.
 * Create two colors with the same RGB values. Test the equality of hashcodes.
 */
public class ColorTest {
    /**
     * Test color construction and methods.
     */
    @Test
    public void testConstructor(){
        Color p = new Color(1,2,3);
        assertEquals(1, p.getRed());
        assertEquals(2, p.getGreen());
        assertEquals(3, p.getBlue());
    }


    /**
     * Test that colors with the same RGB values are .equal()
     */
    @Test
    public void testEquals(){
        Color p1 = new Color(1,2,3);
        Color p2 = new Color(1,2,3);
        assertEquals(true, p1.equals(p2));
    }
    
    /**
     * Test that two colors with different RGB values don't .equal()
     */
    @Test
    public void testNotEqualColors(){
        Color p1 = new Color(1,2,3);
        Color p2 = new Color(4,5,6);
        assertEquals(false, p1.equals(p2));
    }
    
    /**
     * Test that .equals() doesn't generate errors for non-Color parameters
     */
    @Test
    public void testNotEqualObject(){
        Color p1 = new Color(10, 11, 12);
        assertEquals(false, p1.equals(""));
    }
    
    /**
     * Test that two colors that .equal() have the same hashcode
     */
    @Test
    public void testEqualsHashEquals(){
        Color p1 = new Color(1,2,3);
        Color p2 = new Color(1,2,3);
        assertEquals(p1.hashCode(), p2.hashCode());
    }
}