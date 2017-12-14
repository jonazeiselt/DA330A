package model;

/**
 * Represents a coordinate.
 * 
 * @author Jonas Eiselt
 * @since 2017-11-12
 */
public class Point 
{
	private int x;
	private int y;
	
	public Point(int x, int y) 
	{
		this.x = x;
		this.y = y;
	}
	
	public int getX()
	{
		return x;
	}
	
	public int getY() 
	{
		return y;
	}
}
