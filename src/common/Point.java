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


    /**
     * Equality
     *
     * @return true if x and y coords equal
     */
    public boolean equals(Object obj){
        if (obj == null)
            return false;
        if (obj.getClass() != this.getClass())
            return false;
        Point that = (Point) obj;
        return this.x == that.x &&
            this.y == that.y;
    }


    /**
     * Hash code
     *
     * @return integer hash code
     */
    public int hashCode(){
        return this.x*123 + this.y*6789;
    }
}
