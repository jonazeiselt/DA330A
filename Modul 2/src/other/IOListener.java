package other;

public interface IOListener 
{
	public void onReadingCompleted(String message);
	public void onWritingCompleted(String message);
	
	public void onReadingUpdate(String message);
	public void onWritingUpdate(String message);
}
