package other;

public enum ConcurrentMode 
{
	SYNCHRONOUS 
	{
		@Override 
		public String toString() { 
			return "synchronous"; 
		}
	}, 
	ASYNCHRONOUS
	{
		@Override 
		public String toString() { 
			return "asynchronous"; 
		}
	}
}
