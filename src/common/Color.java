package common;

/**
 * To represent a color, Color contains three final ints that stand
 * for it's RGB values.
 *
 * Thread safety:
 *      It is completely immutable and threadsafe.
 */
public class Color {
    private final int red;
    private final int green;
    private final int blue;

    /**
     * Default constructor makes the color white
     */
    public Color(){
        this.red = 255;
        this.green = 255;
        this.blue = 255;
    }

    /**
     * Create color with given RGB values.
     *
     * @param r integer from 0 to 255
     * @param g integer from 0 to 255
     * @param b integer from 0 to 255
     */
    public Color(int r, int g, int b){
        assert r >= 0 && r <= 255;
        assert g >= 0 && g <= 255;
        assert b >= 0 && b <= 255;

        this.red = r;
        this.green = g;
        this.blue = b;
    }

    /**
     * @return the amount of red in this color
     */
    public int getRed(){
        return this.red;
    }

    /**
     * @return the amount of green in this color
     */
    public int getGreen(){
        return this.green;
    }

    /**
     * @return the amount of blue in this color
     */
    public int getBlue(){
        return this.blue;
    }

    /**
     * Equality
     *
     * @return true if components are equal
     */
    public boolean equals(Object obj){
        if (obj == null)
            return false;
        if (obj.getClass() != this.getClass())
            return false;
        Color that = (Color) obj;
        return this.red == that.red &&
            this.green == that.green &&
            this.blue == that.blue;
    }

    /**
     * Hash code
     *
     * @return integer hash code
     */
    public int hashCode(){
        return (this.red << 16) + (this.green << 8) + this.blue;
    }

    public String toString(){
        return "Color(" + red + "," + green + "," + blue + ")";
    }
}
