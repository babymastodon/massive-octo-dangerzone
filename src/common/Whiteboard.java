package common;

import java.awt.Point;
import java.awt.image.BufferedImage;

public class Whiteboard {
    public final int WIDTH;
    public final int HEIGHT;
    public Whiteboard(int width, int height){
        this.WIDTH = width;
        this.HEIGHT = height;
    }

    /**
     * Copy the pixels in the given whiteboard into this whiteboard.
     * The other whiteboard is not locked during this process
     * @param other: the other whiteboard we are copying from
     */
    public void copy(Whiteboard other){

    }

    /**
     * Find all the points on the line segment between and including
     * point1 and point2 and draw them with the specified color.
     * Note that having point1 and point2 be the same means one should
     * should just draw that point.
     * @param point1: one end of the line segment
     * @param point2: the other end of the line segment
     * @param color: the color to make the line
     * @return an array of all the points in the modified line segment
     */
    public Point[] drawLine(Point point1, Point point2, Color color){

    }

    /**
     * Sets the color at the point to color. The point's x and y 
     * coordinates must be within the board's dimensions.
     * @param point: the point to color in
     * @param color: the color the point should now have
     */
    public void setPixel(Point point, Color color){

    }

    /**
     * Return the color at the point. The point must be within the
     * board's dimensions
     * @param point: the point we want to know the color of
     * @return the color at point
     */
    public Color getPixel(Point point){

    }

    /**
     * Create a BufferedImage that can be used to copy the pixel data
     * via the copyPixelData() function. 
     * @return a BufferedImage with the same width and height as the
     * whiteboard, using the predefined type "TYPE_INT_RGB"
     */
    public BufferedImage makeBuffer(){

    }

    /**
     * Copy the pixel data from the whiteboard into the BufferedImage
     * buffer. The buffer should have been constructed by makeBuffere()
     * or elsewhere with the same parameters
     * @param buffer: the buffer we copy the pixel data into from
     * the whiteboard
     */
    public void copyPixelData(BufferedImage buffer){

    }
}
