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
     * Two points are equal if their coordinates are equal
     * @param other
     * @return
     */
    @Override
    public boolean equals(Object other){
        if (!(other instanceof Point)){
            return false;
        }
        return this.x == ((Point)(other)).x && this.y == ((Point)(other)).y;
    }
    
    /**
     * Very basic (and bad for collisions) hashcode, but at least it allows us
     * to use hashsets.
     */
    @Override
    public int hashCode(){
        return this.x*10 + this.y;
    }
    
    public String toString(){
        return "x: " + this.x + ", y: " + this.y;
    }
}
