package tests.common;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import common.Point;

/**
 * Test suite for Point.java. Point contains two integers that represent
 * the x and y coordinates.
 * 
 * Testing strategy:
 * Create a new Point and correctly observe the x and y coordinates.
 * Test that two points with the same coordinates are equal
 * Test that two points with different coordinates are not equal
 * Test that a point doesn't equal a different object type
 * Test that two points that are .equal() have the same hashcode
 */
public class PointTest {
    /**
     * Test point construction and methods.
     */
    @Test
    public void testConstructor(){
        Point p = new Point(1,2);
        assertEquals(1, p.getX());
        assertEquals(2, p.getY());
    }


    /**
     * Test that two points with the same x and y coordinates, respectively, are equal
     */
    @Test
    public void testEquals(){
        Point p1 = new Point(1,2);
        Point p2 = new Point(1,2);
        assertEquals(true, p1.equals(p2));
    }
    
    /**
     * Test that two points with different coordinates aren't equal.
     */
    @Test
    public void testNotEqualsPoint(){
        Point p1 = new Point(1,2);
        Point p2 = new Point(3,4);
        assertEquals(false, p1.equals(p2));
    }
    
    /**
     * Test that Point.equals() doesn't blow up if passed a non-Color object
     */
    @Test
    public void testNotEqualsObject(){
        Point p1 = new Point(1, 2);
        assertEquals(false, p1.equals(""));
    }
    
    /**
     * Test that two points and are .equals() have equal hashcodes.
     */
    @Test
    public void testHashCodes(){
        Point p1 = new Point(1, 2);
        Point p2 = new Point(1, 2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }
}
