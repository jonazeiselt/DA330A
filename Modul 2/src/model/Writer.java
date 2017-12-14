package model;

import java.util.Random;

import other.ConcurrentMode;
import other.IOListener;

/**
 * Writes a character to a buffer and returns the character
 * to a listener. The writing can be done asynchronously or 
 * synchronously.
 *  
 * @author Jonas Eiselt
 * @since 2017-11-19
 */
public class Writer implements Runnable
{
	private CharacterBuffer buffer;
	private String stringToTransfer;
	private ConcurrentMode concurrentMode;

	private IOListener ioListener;
	private volatile boolean isRunning = true;

	public Writer(CharacterBuffer buffer, String stringToTransfer) 
	{
		this.buffer = buffer;
		this.stringToTransfer = stringToTransfer;
	}

	public void setConcurrentMode(ConcurrentMode concurrentMode) 
	{
		this.concurrentMode = concurrentMode;
	}

	public void setOnWritingCompleted(IOListener ioListener) 
	{
		this.ioListener = ioListener;
	}

	@Override
	public void run()
	{
		StringBuilder transmittedString = new StringBuilder();
		for (int i = 0; i < stringToTransfer.length(); i++) 
		{	
			if (!isRunning)
				break;

			char newChar = stringToTransfer.charAt(i);

			if (concurrentMode == ConcurrentMode.SYNCHRONOUS)
				buffer.write(newChar);		
			else 
				buffer.asyncWrite(newChar);

			transmittedString.append(newChar);
			ioListener.onWritingUpdate("Writing '" + newChar + "'");

			Random rand = new Random();
			sleep(rand.nextInt(75));
		}
		
		if (isRunning) 
		{
			buffer.hasCharacter(false);
			ioListener.onWritingCompleted(transmittedString.toString());
		}
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
