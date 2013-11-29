package common;

/**
 * To represent a color, Color contains three final ints that stand
 * for it's RGB values.
 * It is completely immutable and threadsafe.
 *
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
	
	public Color(int r, int g, int b){
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
}
