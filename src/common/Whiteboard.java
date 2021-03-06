package common;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;


/**
 * A two-dimensional bitmap of pixels representing an image.
 *
 * The lower left-hand corner is at the (0,0) index by convention
 * (standard cartesian coordinates).
 *
 * Thread safety:
 *      Not thread safe. Multi-threaded access to this object must be
 *          protected by a lock.
 */
public class Whiteboard {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    private final Color[][] board = new Color[WIDTH][HEIGHT];

    /**
     * Construct a blank (white) board
     */
    public Whiteboard(){
        initializeBoard();
    }

    /**
     * Set all colors in the board to white
     */
    private void initializeBoard(){
        for (int i = 0; i < WIDTH; i ++){
            for (int j = 0; j < HEIGHT; j ++){
                board[i][j] = new Color(); //the default color is white
            }
        }
    }

    /**
     * Find all the points on the line segment between and including
     * point1 and point2 and draw them with the specified color.
     *
     * Note that having point1 and point2 be the same means one should
     * should just draw that point.
     *
     * @param point1: one end of the line segment
     * @param point2: the other end of the line segment
     * @param color: the color to make the line
     * @param width: side length of the square that represents the area of the brush centered at a given
     * point.
     * @return an array of all the points in the modified line segment.
     */
    public List<Point> drawLine(Point point1, Point point2, Color color, int width){
        assert checkPointInBounds(point1);
        assert checkPointInBounds(point2);

        double sideLength = width/2.0;
        if (width == 1){
            sideLength = 0;
        }
        
        if (point1.equals(point2)){
            //same point
            return Arrays.asList(drawSquare(point1, sideLength, color));
        }

        if (point1.getX() == point2.getX()){
            //vertical line

            return  Arrays.asList(drawVerticalLine(point1, point2, color, sideLength));
        }

        //general case (diagonal or horizontal line)
        Point leftPoint = point1;
        Point rightPoint = point2;
        if (point1.getX() >= point2.getX()){
            leftPoint = point2;
            rightPoint = point1;
        }
        boolean leftIsBelow = leftPoint.getY() <= rightPoint.getY();

        double slope = (double)(rightPoint.getY() - leftPoint.getY())/(rightPoint.getX() - leftPoint.getX());
        ArrayList<Point> changePoints = new ArrayList<Point>();
        
        int counter = 0;

        for (int i = leftPoint.getX(); i <= rightPoint.getX() ; i ++){
            int y = (int)(slope*counter) + leftPoint.getY();

            if (leftIsBelow){
                while (y <= (int)(slope*(counter+1)) + leftPoint.getY() && 
                        y <= rightPoint.getY() + Math.ceil(sideLength)){
                    Point[] squarePoints = drawSquare(new Point(i, y), sideLength, color);
                    changePoints.addAll(Arrays.asList(squarePoints));
                    y++;
                }
            }

            else{
                while (y >= (int)(slope*(counter+1)) + leftPoint.getY() && 
                        y >= rightPoint.getY() - Math.ceil(sideLength)){
                    Point[] squarePoints = drawSquare(new Point(i, y), sideLength, color);
                    changePoints.addAll(Arrays.asList(squarePoints));
                    y--;
                }
            }
            counter ++;
        }
        
        return Arrays.asList(removeDuplicates(changePoints));
    }
    
    private Point[] drawVerticalLine(Point point1, Point point2, Color color, double sideLength){
        ArrayList<Point> changedPoints = new ArrayList<Point>();

        int x = point1.getX();
        for (int i = Math.min(point1.getY(), point2.getY()); i <= Math.max(point1.getY(), point2.getY()) ; i ++){
            Point[] newPoints = drawSquare(new Point(x, i), sideLength, color);
            changedPoints.addAll(Arrays.asList(newPoints));
        }
        
        HashSet<Point> hs = new HashSet<Point>();
        hs.addAll(changedPoints);
        ArrayList<Point> noDuplicates = new ArrayList<Point>();
        noDuplicates.addAll(hs);

        Point[] coloredPoints = new Point[noDuplicates.size()];
        for (int i = 0; i < noDuplicates.size(); i ++){
            coloredPoints[i] = noDuplicates.get(i);
        }
        
        return coloredPoints;
    }

    private Point[] removeDuplicates(ArrayList<Point> changePoints){
        HashSet<Point> hs = new HashSet<Point>();
        hs.addAll(changePoints);
        ArrayList<Point> noDuplicates = new ArrayList<Point>();
        noDuplicates.addAll(hs);

        Point[] coloredPoints = new Point[noDuplicates.size()];
        for (int i = 0; i < noDuplicates.size(); i ++){
            coloredPoints[i] = noDuplicates.get(i);
        }
        return coloredPoints;
    }
    
    /**
     * Draw a square of pixels centered at a point.
     *
     * if sideLength == 0, just do that point.
     * @param center
     * @param sideLength
     * @param color
     * @return
     */
    private Point[] drawSquare(Point center, double sideLength, Color color){
        ArrayList<Point> changedPoints = new ArrayList<Point>();
        int x = center.getX();
        int y = center.getY();

        if (sideLength == 0 && checkPointInBounds(center)){
            board[x][y] = color;
            Point[] singlePoint = {center};
            return singlePoint;
        }

        for(int i = (int)-sideLength; i < Math.ceil(sideLength); i ++){
            for(int j = (int)-sideLength; j < Math.ceil(sideLength); j ++){
                if (checkPointInBounds(new Point(x+i, y + j))){
                    Color oldColor = board[x+i][y+j];
                    board[x+i][y+j] = color;
                    if (!color.equals(oldColor)){
                        changedPoints.add(new Point(x+i, y+j));
                    }
                }
            }
        }

        Point[] coloredPoints = new Point[changedPoints.size()];
        for (int i = 0; i < changedPoints.size(); i ++){
            coloredPoints[i] = changedPoints.get(i);
        }
        return coloredPoints;
    }

    /**
     * Return whether the point falls within the boundary of
     * a whiteboard.
     *
     * @param point the point in question
     * @return true if the point is inside a Whiteboard boundary
     */
    public static boolean checkPointInBounds(Point point){
        return point.getX() < WIDTH && point.getX() >= 0 && point.getY() < HEIGHT && point.getY() >= 0;
    }

    /**
     * Sets the color at the point to color. The point's x and y 
     * coordinates must be within the board's dimensions.
     * @param point: the point to color in
     * @param color: the color the point should now have
     */
    public void setPixel(Point point, Color color){
        int x = point.getX();
        int y = point.getY();

        assert checkPointInBounds(point);

        board[x][y] = color;
    }

    /**
     * Return the color at the point. The point must be within the
     * board's dimensions
     * @param point: the point we want to know the color of
     * @return the color at point
     */
    public Color getPixel(Point point){
        int x = point.getX();
        int y = point.getY();

        assert checkPointInBounds(point);

        return board[x][y];
    }

    /**
     * Create a BufferedImage that can be used to copy the pixel data
     * via the copyPixelData() function. 
     * @return a BufferedImage with the same width and height as the
     * whiteboard, using the predefined type "TYPE_INT_RGB"
     */
    public static BufferedImage makeBuffer(){
        final int TYPE_INT_RGB = 1; //looked up value in documentation
        BufferedImage bi = new BufferedImage(WIDTH, HEIGHT, TYPE_INT_RGB);
        return bi;
    }

    /**
     * Copy the pixel data from the whiteboard into the BufferedImage
     * buffer. The buffer should have been constructed by makeBuffere()
     * or elsewhere with the same parameters
     * @param buffer: the buffer we copy the pixel data into from
     * the whiteboard
     */
    public void copyPixelData(BufferedImage buffer){
        for (int i = 0; i < WIDTH; i ++){
            for (int j = 0; j < HEIGHT; j ++){
                Color c = board[i][j];
                int rgb = ((c.getRed()&0x0ff)<<16)|((c.getGreen()&0x0ff)<<8)|(c.getBlue()&0x0ff);
                buffer.setRGB(i, HEIGHT - j - 1, rgb); //weird indexing because buffer starts from upper left
                //and we start in bottom left...
            }
        }
    }

    /**
     * Equality is determined by if all the colors at all the pixels are the same.
     *
     * @return true if colors are the same
     */
    public boolean equals(Object obj){
        if (obj == null)
            return false;
        if (obj.getClass() != this.getClass())
            return false;
        Whiteboard that = (Whiteboard) obj;
        for (int i = 0; i < WIDTH; i ++){
            for (int j = 0; j < HEIGHT; j ++){
                if (! this.board[i][j].equals(that.board[i][j])){
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Hash code
     *
     * @return integer hash code
     */
    public int hashCode(){
        int sum = 0;
        for (int i = 0; i < WIDTH; i ++){
            for (int j = 0; j < HEIGHT; j ++){
                sum += this.board[i][j].hashCode()*(i*j+i);
            }
        }
        return sum;
    }
}
