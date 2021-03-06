package tests.common;

import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.junit.Test;

import common.Color;
import common.Point;
import common.Whiteboard;

/**
 * Testing suite for Whiteboard.java. 
 *
 *
 * Testing strategy:
 * Create a new board and check that it is all white.
 * A bunch of drawLine() tests:
 *          draw a single point with no width.
 *          draw a single point with some width
 *          draw a vertical line with no width
 *          draw a vertical line with a width
 *          draw a line along a diagonal with integer slope and no width
 *          draw a line along a diagonal with integer slope and a width
 *          draw a line along a diagonal with non-integer slope and no width
 *          draw a line along a diagonal with non-integer slope and a width
 *          draw a line along a diagonal with a different non-integer slope and no width
 * Test .equals() and .hashcode() methods
 * .getPixel() is tested in almost all the tests anyways.
 * Test setPixel() only sets the color for one pixel
 * Test makeBuffer() returns a BufferedImage of the right size and type
 * Test copyPixelData() puts the data from the board into the bufferedImage
 * 
 */
public class WhiteboardTests {

    /**
     * The basic whiteboard is all white
     */
    @Test
    public void testInitialColorsWhite(){
        Whiteboard wb = new Whiteboard();
        Color white = new Color();
        for (int i = 0; i < Whiteboard.WIDTH; i ++){
            for (int j = 0; j < Whiteboard.HEIGHT; j ++){
                Point p = new Point(i, j);
                assertEquals(true, wb.getPixel(p).equals(white));
            }
        }
    }

    /**
     * A bunch of tests for drawLine() coming up:
     */
    
    /**
     * Clicking a single point with just width 1 only colors in that point.
     * That point is returned in an array
     */
    @Test
    public void drawLineSamePoint(){
        Whiteboard wb = new Whiteboard();
        Color newColor = new Color(5, 12, 40);
        Color white = new Color();
        ArrayList<Point> expectedPoints = new ArrayList<Point>();
        expectedPoints.add(new Point(20, 65));
        ArrayList<Point> modifiedPoints = new ArrayList<Point>(wb.drawLine(new Point(20, 65), new Point(20, 65), newColor, 1));
        for (int i = 0; i < Whiteboard.WIDTH; i ++){
            for (int j = 0; j < Whiteboard.HEIGHT; j ++){
                Point p = new Point(i, j);
                if (i == 20 && j == 65){
                    assertEquals(true, wb.getPixel(p).equals(newColor));
                }
                else{
                    assertEquals(true, wb.getPixel(p).equals(white));
                }
            }
        }
        //check that array reflects all modifiedPoints
        assertEquals(true, expectedPoints.get(0).equals(modifiedPoints.get(0)));
    }

    /**
     * Checks that the points I expected to be modified in tests are indeed modified when
     * drawLine() is called. Ordering doesn't matter, but there must be an exact one to one matchup.
     * @param expectedPoints
     * @param modifiedPoints
     */
    private void testArraysEqual(ArrayList<Point> expectedPoints, ArrayList<Point> modifiedPoints){
        assertEquals(modifiedPoints.size(), expectedPoints.size());
        for (int i = 0; i < modifiedPoints.size(); i ++){
            Point p = modifiedPoints.get(i);
            assertEquals(true, expectedPoints.contains(p));
            expectedPoints.remove(p);
        }
        assertEquals(0, expectedPoints.size());
    }
    
    /**
     * Tests for when a user clicks only a single point, but has a width to the pain brush.
     * Tests both coloring of whiteboard and the array returned
     */
    @Test
    public void drawLineSamePointWidth(){
        Whiteboard wb = new Whiteboard();
        Color newColor = new Color(5, 12, 40);
        Color white = new Color();
        ArrayList<Point> expectedPoints = new ArrayList<Point>();
        for (int i = 99; i < 101; i ++){
            for (int j = 99; j < 101; j ++){
                expectedPoints.add(new Point(i, j));
            }
        }
        ArrayList<Point> modifiedPoints = new ArrayList<Point>(wb.drawLine(new Point(100, 100), new Point(100, 100), newColor, 2));
        for (int i = 0; i < Whiteboard.WIDTH; i ++){
            for (int j = 0; j < Whiteboard.HEIGHT; j ++){
                if ((i == 99 || i == 100) && (j == 99 || j == 100)){
                    assertEquals(true, wb.getPixel(new Point(i, j)).equals(newColor));
                }
                else{
                    assertEquals(true, wb.getPixel(new Point(i, j)).equals(white));
                }
            }
        }

        testArraysEqual(expectedPoints, modifiedPoints);
    }

    /**
     * Tests when a user draws a vertical line and no width.
     * Tests both coloring and array
     */
    @Test
    public void drawLineVertical(){
        Whiteboard wb = new Whiteboard();
        Color newColor = new Color(100, 2, 50);
        Color white = new Color();
        ArrayList<Point> expectedPoints = new ArrayList<Point>();
        for (int i = 300; i < 301; i ++){
            for (int j = 200; j < 501; j ++){
                expectedPoints.add(new Point(i, j));
            }
        }
        ArrayList<Point> modifiedPoints = new ArrayList<Point>(wb.drawLine(new Point(300, 200), new Point(300, 500), newColor, 1));

        for (int i = 0; i < Whiteboard.WIDTH; i ++){
            for (int j = 0; j < Whiteboard.HEIGHT; j ++){
                Point p = new Point(i, j);
                if (i == 300 && j >= 200 && j <= 500){
                    assertEquals(true, wb.getPixel(p).equals(newColor));
                }
                else{
                    assertEquals(true, wb.getPixel(p).equals(white));
                }
            }
        }
        
        testArraysEqual(expectedPoints, modifiedPoints);
    }

    /**
     * Tests for a user drawing a vertical line with a width
     * Tests both coloring of board and array
     */
    @Test
    public void drawLineVerticalWidth(){
        Whiteboard wb = new Whiteboard();
        Color newColor = new Color(100, 2, 50);
        Color white = new Color();

        ArrayList<Point> expectedPoints = new ArrayList<Point>();
        for (int i = 299; i < 302; i ++){
            for (int j = 199; j < 502; j ++){
                expectedPoints.add(new Point(i, j));
            }
        }
        ArrayList<Point> modifiedPoints = new ArrayList<Point>(wb.drawLine(new Point(300, 200), new Point(300, 500), newColor, 3));

        for (int i = 0; i < Whiteboard.WIDTH; i ++){
            for (int j = 0; j < Whiteboard.HEIGHT; j ++){
                Point p = new Point(i, j);
                if ((i >= 299 && i <= 301) && j >= 199 && j <= 501){
                    assertEquals(true, wb.getPixel(p).equals(newColor));
                }

                else{
                    assertEquals(true, wb.getPixel(p).equals(white));
                }
            }
        }
        testArraysEqual(expectedPoints, modifiedPoints);
    }

    /**
     * Tests when a user draws a vertical line that has an integer slope (and no width)
     * Test both coloring and array
     */
    @Test
    public void drawLinePerfectDiagonal(){
        Whiteboard wb = new Whiteboard();
        Color newColor = new Color(0, 0, 0);
        Color white = new Color();
        
        ArrayList<Point> expectedPoints = new ArrayList<Point>();
        for (int i = 100; i < 201; i ++){
            for (int j = i; j <= i+1 && j <= 200; j ++){
                expectedPoints.add(new Point(i, j));
            }
        }
        

        ArrayList<Point> modifiedPoints = new ArrayList<Point>(wb.drawLine(new Point(100, 100), new Point(200, 200), newColor, 1));

        for (int i = 0; i < Whiteboard.WIDTH; i ++){
            for (int j = 0; j < Whiteboard.HEIGHT; j ++){
                Point p = new Point(i, j);
                if ((j == i || j - i == 1) && i >= 100 && i <= 200 && j >= 100 && j <= 200){
                    assertEquals(true, wb.getPixel(p).equals(newColor));
                }
                else{
                    assertEquals(true, wb.getPixel(p).equals(white));
                }
            }
        }
        
        testArraysEqual(expectedPoints, modifiedPoints);
    }

    /**
     * positive slope that doesn't fit perfectly along a diagonal and no width.
     * Tests both coloring and array
     */
     @Test
     public void drawLineImperfectDiagonalUpRight(){
        Whiteboard wb = new Whiteboard();
        Color newColor = new Color(10, 2, 25);
        Color white = new Color();
        
        ArrayList<Point> expectedPoints = new ArrayList<Point>();
        for (int i = 100; i <= 105; i ++){
            for (int j = 100 + 40*(i-100); j <= 100+40*(i-99) && j <= 300; j ++){
                expectedPoints.add(new Point(i, j));
            }
        }

        ArrayList<Point> modifiedPoints = new ArrayList<Point>(wb.drawLine(new Point(100, 100), new Point(105, 300), newColor, 1));

        for (int i = 0; i < Whiteboard.WIDTH; i ++){
            for (int j = 0; j < Whiteboard.HEIGHT; j ++){
                Point p = new Point(i, j);
                if ((i == 100 && j >= 100 && j <= 140) ||
                        (i == 101 && j >= 140 && j <= 180) ||
                        (i == 102 && j >= 180 && j <= 220) ||
                        (i == 103 && j >= 220 && j <= 260) ||
                        (i == 104 && j >= 260 && j <= 300) ||
                        (i == 105 && j == 300)){
                    assertEquals(true, wb.getPixel(p).equals(newColor));
                }
                else{
                    assertEquals(true, wb.getPixel(p).equals(white));
                }
            }
        }
        
        testArraysEqual(expectedPoints, modifiedPoints);
     }

     /**
      * positive slope with a width!
      * Only tests coloring (by now other tests have established that the array returned correctly
      * represents the new coloring of the board)
      */
     @Test
     public void drawLineImperfectDiagonalUpRightWidth(){
         Whiteboard wb = new Whiteboard();
         Color newColor = new Color(10, 12, 26);
         Color white = new Color();
         
         wb.drawLine(new Point(100, 100), new Point(110, 105), newColor, 5);

         for (int i = 0; i < Whiteboard.WIDTH; i ++){
             for (int j = 0; j < Whiteboard.HEIGHT; j ++){
                 Point p = new Point(i, j);
                 if ((i == 98 && j >= 98 && j <= 102) ||
                         (i == 99 && j >= 98 && j <= 103) ||
                         (i == 100 && j >= 98 && j <= 103) ||
                         (i == 101 && j >= 98 && j <= 104) ||
                         (i == 102 && j >= 98 && j <= 104) ||
                         (i == 103 && j >= 98 && j <= 105) ||
                         (i == 104 && j >= 99 && j <= 105) ||
                         (i == 105 && j >= 99 && j <= 106) ||
                         (i == 106 && j >= 100 && j <= 106) ||
                         (i == 107 && j >= 100 && j <= 107) ||
                         (i == 108 && j >= 101 && j <= 107) ||
                         (i == 109 && j >= 101 && j <= 107) ||
                         (i == 110 && j >= 102 && j <= 107) ||
                         (i == 111 && j >= 102 && j <= 107) ||
                         (i == 112 && j >= 103 && j <= 107)){
                     assertEquals(true, wb.getPixel(p).equals(newColor));
                 }
                 else{
                     assertEquals(true, wb.getPixel(p).equals(white));
                 }
             }
         }
     }

     /**
      * negative slope that doesn't fit perfectly along a diagonal and no width
      * Only tests diagonal
      */
     @Test
     public void drawLineImperfectDiagonalDownLeft(){
         Whiteboard wb = new Whiteboard();
         Color newColor = new Color(120, 12, 255);
         Color white = new Color();
         wb.drawLine(new Point(105, 200), new Point(100, 500), newColor, 1);

         for (int i = 0; i < Whiteboard.WIDTH; i ++){
             for (int j = 0; j < Whiteboard.HEIGHT; j ++){
                 Point p = new Point(i, j);
                 if ((i == 100 && j >= 440 && j <= 500) ||
                         (i == 101 && j >= 380 && j <= 440) ||
                         (i == 102 && j >= 320 && j <= 380) ||
                         (i == 103 && j >= 260 && j <= 320) ||
                         (i == 104 && j >= 200 && j <= 260) ||
                         (i == 105 && j == 200)){
                     assertEquals(true, wb.getPixel(p).equals(newColor));
                 }
                 else{
                     assertEquals(true, wb.getPixel(p).equals(white));
                 }
             }
         }
     }
     
     /**
      * End of tests for drawLine()
      */
     
     /**
      * Test color equality and hashcode of whiteboards themselves.
      */
     @Test
     public void testEquals(){
         Whiteboard w1 = new Whiteboard();
         Whiteboard w2 = new Whiteboard();
         Whiteboard w3 = new Whiteboard();

         w1.setPixel(new Point(1,2), new Color(1,2,3));
         w2.setPixel(new Point(1,2), new Color(1,2,3));

         assertEquals(true, w1.equals(w2));
         assertEquals(false, w1.equals(w3));
         assertEquals(false, w1.equals(""));
         assertEquals(w1.hashCode(), w2.hashCode());
     }
     
     /**
      * Test that setting a pixel to a specific color only sets the color for that pixel
      */
     @Test
     public void testSetPixel(){
         Whiteboard w1 = new Whiteboard();
         Color newColor = new Color(5, 12, 40);
         w1.setPixel(new Point(4, 197), newColor);
         for (int i = 0; i < Whiteboard.WIDTH; i ++){
             for (int j = 0; j < Whiteboard.HEIGHT; j ++){
                 Point p = new Point(i, j);
                 if (i == 4 && j == 197){
                     assertEquals(true, w1.getPixel(p).equals(newColor));
                 }
                 else{
                     assertEquals(true, w1.getPixel(p).equals(new Color()));
                 }
             }
         }
     }
     
     /**
      * Test that makeBuffer() makes a BufferedImage of the right size and type.
      */
     @Test
     public void testMakeBuffer(){
         BufferedImage bi = Whiteboard.makeBuffer();
         assertEquals(600, bi.getHeight());
         assertEquals(800, bi.getWidth());
         assertEquals(1, bi.getType());
         
     }
     
     /**
      * Test that copyPixelData() changes the data in bufferedImage.
      */
     @Test
     public void testCopyPixelData(){
         Whiteboard w1 = new Whiteboard();
         BufferedImage bi = Whiteboard.makeBuffer();
         Color newColor = new Color(1, 2, 3);
         w1.setPixel(new Point(100, 200), newColor);
         w1.copyPixelData(bi);
         //the indices are weird because the index base is different for a whiteboard
         //and a buffered image
         assertEquals(3, (bi.getTile(0, 0).getSample(100, 399, 2)));
         assertEquals(2, (bi.getTile(0, 0).getSample(100, 399, 1)));
         assertEquals(1, (bi.getTile(0, 0).getSample(100, 399, 0)));
     }
}
