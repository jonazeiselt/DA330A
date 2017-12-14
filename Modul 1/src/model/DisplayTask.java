package model;

import java.util.Random;

import other.DisplayListener;

/**
 * Notifies a listener at regular intervals when thread is running,
 * with a string value and a random generated Point (see Point in 
 * package model).
 *  
 * @author Jonas Eiselt
 * @since 2017-11-12
 */
public class DisplayTask implements Runnable 
{
	private String taskName;
	private DisplayListener displayListener;

	private volatile boolean isRunning = true;

	public DisplayTask(String taskName) 
	{
		this.taskName = taskName;
	}
	
	/** 
	 * We can now let the Controller know when an display update
	 * should be made. 
	 */
	public void setOnDisplayUpdate(DisplayListener displayListener) 
	{
		this.displayListener = displayListener;
	}
		
	@Override
	public void run()
	{
		while (isRunning)
		{
			Point randomPoint = generateRandomPoint();
			displayListener.setOnDisplay(taskName, randomPoint);
			
			sleep(500);
		}
	}

	private Point generateRandomPoint() 
	{
		Random random = new Random();
		int x = random.nextInt(130); // experimental value
		int y = random.nextInt(280); // experimental value

		return new Point(x,y);
	}

	public void terminate() 
	{
		isRunning = false;
	}
	
	/** Push the thread into a sleep-mode. */
	public void sleep(long milliseconds)
	{
		try 
		{
			Thread.sleep(milliseconds);
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
}
