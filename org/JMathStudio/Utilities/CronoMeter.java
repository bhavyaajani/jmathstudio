package org.JMathStudio.Utilities;

/**
 * This class define a Crono Meter which provide useful service in measuring the 
 * elapsed time. This can be used to measure the execution time for an operation, transform etc.
 * <p>The time measured will be in milli-seconds.
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 *
 */
public final class CronoMeter {
	
	private long time;
	private float elapsedTime;
	private boolean isPaused;
	private boolean isAlive;
	
	/**
	 * This will instantiate a Crono Meter with a default state.
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public CronoMeter()
	{
		elapsedTime = 0;
		isAlive = false;
		isPaused = false;
	}
	
	/**
	 * This method will activate a new state for the current Crono Meter by discarding
	 * the old state if any. The new state will clear the elapsed time if any and signal
	 * the timer to run.
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void start()
	{
		time = System.currentTimeMillis();
		elapsedTime = 0;
		isAlive = true;
		isPaused = false;
	}
	
	/**
	 * This method will pause the current active state if any. 
	 * This will stop the timer and record the elapsed time if any.
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void pause()
	{
		if(isAlive & !isPaused)
		{
			elapsedTime += (System.currentTimeMillis()-time);
			isPaused = true;
		}
	}
	
	/**
	 * This method will resume the state from the last paused state if any.
	 * This will initiate the timer again.
	 * <p>Make sure that the current state is active.
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void resume()
	{
		if(isPaused & isAlive)
		{
			time = System.currentTimeMillis();
			isPaused = false;
		}
	}
	
	/**
	 * This method will stop the current active state if any and return the cumulative elapsed
	 * time for the state in milli-seconds.
	 * <p>If the current state is in paused, this method will return the cumulative elapsed time till
	 * the last pause event.
	 * <p>If the current state is not active this method will return '0'.
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float stop()
	{
		if(isAlive & !isPaused)
		{
			isAlive=false;
			return elapsedTime+(System.currentTimeMillis()-time);
		}
		else if(isAlive & isPaused)
			return elapsedTime;
		else 
			return 0;
	}

	private static long log;
	
	/**
	 * This static method along with {@link #end()} provide a simple quick way to measure elapsed 
	 * execution time (in milliseconds).
	 * <pre>
	 * CronoMeter.init(); //Start timer
	 * code ...
	 * code ...
	 * float duration = CronoMeter.end(); //End timer 
	 * </pre>
	 * <p>This method will initiate and start the timer. Use method {@link #end()} to stop timer
	 * and get the elapsed time.
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public static void init()
	{
		log = System.currentTimeMillis();
	}
	
	/**
	 * This static method along with {@link #init()} provide a simple quick way to measure elapsed 
	 * execution time (in milliseconds).
	 * <pre>
	 * CronoMeter.init(); //Start timer
	 * code ...
	 * code ...
	 * float duration = CronoMeter.end(); //End timer 
	 * </pre>
	 * <p>This method will return the elapsed time in milliseconds since {@link #init()} was called.
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public static float end()
	{
		float duration = System.currentTimeMillis() - log;
		return duration;
	}
}
