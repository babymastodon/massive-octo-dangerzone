package tests.common;

import org.junit.Test;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import common.Point;

public class PointTest {

	/**
	 * Point tests
	 */
	
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
	 * Test point equality and hashcode.
	 */
	@Test
	public void testEquals(){
      Point p1 = new Point(1,2);
      Point p2 = new Point(1,2);
      Point p3 = new Point(1,4);
      assertEquals(true, p1.equals(p2));
      assertEquals(false, p1.equals(p3));
      assertEquals(false, p1.equals(""));
      assertEquals(p1.hashCode(), p2.hashCode());
	}
}
