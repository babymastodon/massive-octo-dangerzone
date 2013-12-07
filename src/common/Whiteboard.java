package common;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.List;

public class Whiteboard {
    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    private final Color[][] board = new Color[WIDTH][HEIGHT];

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
     * Copy the pixels in the given whiteboard into this whiteboard.
     * The other whiteboard is not locked during this process
     * @param other: the other whiteboard we are copying from
     */
    public void copy(Whiteboard other){
        for (int i = 0; i < WIDTH; i ++){
            for (int j = 0; j < HEIGHT; j ++){
                Point p = new Point(i, j);
                Color c = other.getPixel(p);
                this.setPixel(p, c);
            }
        }
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
    public List<Point> drawLine(Point point1, Point point2, Color color){
        assert checkPointInBounds(point1);
        assert checkPointInBounds(point2);

        if (point1.equals(point2)){
            //same point
            board[point1.getX()][point1.getY()] = color;
            Point[] changedPoints = {point1};
            return Arrays.asList(changedPoints);
        }

        if (point1.getX() == point2.getX()){
            //vertical line
            Point[] changedPoints = new Point[Math.abs(point1.getY() - point2.getY()) + 1];
            int x = point1.getX();
            for (int i = Math.min(point1.getY(), point2.getY()); i <= Math.max(point1.getY(), point2.getY()) ; i ++){
                board[x][i] = color;
                changedPoints[i] = new Point(x, i);
            }
            return Arrays.asList(changedPoints);
        }

        double slope = (double)(point2.getY() - point1.getY())/(point2.getX() - point1.getX());
        Point[] changedPoints = new Point[Math.abs(point1.getY() - point2.getY()) + Math.abs(point1.getX() - point2.getX()) + 1];

        for (int i = Math.min(point1.getX(), point2.getX()); i <= Math.max(point1.getX(), point2.getX()) ; i ++){
            int y = (int)(slope*i) + Math.min(point1.getX(), point2.getX());
            board[i][y] = color;
            changedPoints[i] = new Point(i, y);
        }

        return Arrays.asList(changedPoints);
    }

    private boolean checkPointInBounds(Point point){
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
    public BufferedImage makeBuffer(){
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
     * Equality
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
