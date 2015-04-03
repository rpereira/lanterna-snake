
/**
 *
 * A generic class to represent an 2D object's position on the grid.
 */
public class Position
{
	/*
	 * X-Axis coordinate.
	 */
	private final int x;

	/*
	 * Y-Axis coordinate.
	 */
	private final int y;

	/**
	 * Instantiate a new Position object of coordinates (x, y). Both coordinates
	 * should be grater than zero and not grater than terminal size.
	 *
	 * @param x the X-axis coordinate of this object
	 * @param y the Y-axis coordinate of this object
	 */
	public Position(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	/**
	 * @return this X-axis coordinate
	 */
	public int getX()
	{
		return x;
	}

	/**
	 * @return this Y-axis coordinate
	 */
	public int getY()
	{
		return y;
	}

	/**
	 * Takes a value of type Object as parameter and yields a boolean result.
	 * The implementation of this method uses instanceof and a cast. It first
	 * tests whether the other object is also of type Position. If it is, it
	 * compares the coordinates of the two positions and returns the result.
	 * Otherwise the result is false.
	 *
	 * @param other the other object.
	 * @return true if the coordinates are the same; otherwise returns false.
	 */
	@Override public boolean equals(Object other)
	{
	    boolean result = false;

	    if(other instanceof Position)
	    {
	        Position that = (Position) other;
	        result = (getX() == that.getX() && getY() == that.getY());
	    }

	    return result;
	}
}
