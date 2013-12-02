package common;

/**
 * Immutable, thread safe pair of integer (x,y) coordinates.
 */
public class Point{
    private int x;
    private int y;

    /**
     * Construct with the given arguments.
     *
     * @param x x coordinate
     * @param y y coordinate
     */
    public Point(int x, int y){
        this.x = x;
        this.y = y;
    }

    /**
     * Get x coordinate.
     *
     * @return x coordinate
     */
    public int getX(){
        return x;
    }

    /**
     * Get y coordinate.
     *
     * @return y coordinate
     */
    public int getY(){
        return y;
    }
}
