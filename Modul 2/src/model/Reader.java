package model;

import java.util.Random;

import other.ConcurrentMode;
import other.IOListener;

/**
 * Reads a character from a buffer and returns the character
 * to a listener. The reading can be read asynchronously or 
 * synchronously.
 *  
 * @author Jonas Eiselt
 * @since 2017-11-19
 */
public class Reader implements Runnable
{
	private CharacterBuffer buffer;
	private ConcurrentMode concurrentMode;

	private IOListener ioListener;
	private volatile boolean isRunning = true;

	public Reader(CharacterBuffer buffer) 
	{
		this.buffer = buffer;
	}

	public void setConcurrentMode(ConcurrentMode concurrentMode) 
	{
		this.concurrentMode = concurrentMode;
	}

	public void setOnReadingCompleted(IOListener ioListener) 
	{
		this.ioListener = ioListener;
	}

	@Override
	public void run() 
	{
		StringBuilder receivedString = new StringBuilder();
		while ((buffer.hasCharacter() || !buffer.isEmpty()) && isRunning) 
		{
			if (!buffer.isEmpty() && isRunning) 
			{
				char c;
				if (concurrentMode == ConcurrentMode.SYNCHRONOUS)
					c = buffer.read();
				else 
					c = buffer.asyncRead();

				ioListener.onReadingUpdate("Reading '" + c + "'");
				receivedString.append(c);

				Random rand = new Random();
				sleep(rand.nextInt(100));
			}
		}
		ioListener.onReadingCompleted(receivedString.toString());
	}

	/** Push the thread into a sleep-mode. */
	private void sleep(long milliseconds) 
	{
		try {
			Thread.sleep(milliseconds);
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void terminate() 
	{
		isRunning = false;
	}
}
