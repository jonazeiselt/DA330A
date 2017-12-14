package model;

/**
 * Representation of a single character with data members, such as 
 * read and write. The class provides both an asynchronous and a 
 * synchronous way of reading and writing.
 * 
 * @author Jonas Eiselt
 * @since 2017-11-19
 */
public class CharacterBuffer 
{
	private volatile Character buffer;
	private volatile boolean hasCharacter = true;

	public Character asyncRead() 
	{
		Character tmp = buffer;
		buffer = null;
		
		return tmp;
	}
	
	/** Thread-safe method */
	public synchronized Character read() 
	{	
		while (isEmpty()) 
		{
			try {
				this.wait();
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		Character tmp = buffer;
		buffer = null;
		this.notify();

		return tmp;
	}
	
	public void asyncWrite(Character newChar) 
	{
		buffer = newChar;
	}
	
	/** Thread-safe method */
	public synchronized void write(Character newChar) 
	{
		while (!isEmpty()) 
		{
			try {
				this.wait();
			} 
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		buffer = newChar;
		this.notify();		
	}
	
	public void hasCharacter(boolean hasCharacter) 
	{
		this.hasCharacter = hasCharacter;
	}
	
	public boolean hasCharacter() 
	{
		return hasCharacter;
	}
	
	public boolean isEmpty() 
	{
		return buffer == null;
	}
}
