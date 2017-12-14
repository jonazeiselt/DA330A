package model;

import other.RotateListener;

/**
 * Notifies a listener at regular intervals when thread is running,
 * with the value of degree.
 *  
 * @author Jonas Eiselt
 * @since 2017-11-12
 */
public class RotateTask implements Runnable
{
	private RotateListener rotateListener;
	
	private volatile boolean isRunning = true;
	private int degree = 0;

	/** 
	 * We can now let the Controller know when a rotation should
	 * be made. 
	 */
	public void setOnRotateUpdate(RotateListener rotateListener) 
	{
		this.rotateListener = rotateListener;		
	}

	@Override
	public void run()
	{
		while (isRunning)
		{
			rotateListener.setOnRotate(degree);
			
			degree++;
			if (degree >= 360)
				degree = 0;
			
			sleep(25);
		}
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
