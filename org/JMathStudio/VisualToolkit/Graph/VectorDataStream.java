package org.JMathStudio.VisualToolkit.Graph;

/**
 * The class implementing this interface act as a source for the dynamic data
 * for the associated {@link VectorRunningGraph}.
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public interface VectorDataStream {

	/**
	 * This method will return the next data as float for the associated {@link VectorRunningGraph}.
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public abstract float getNextData();
	
	/**
	 * This method will specify if the next data is available for the associated {@link VectorRunningGraph}.
	 * If so, the running graph will be updated with the next data as captured from method {@link #getNextData()}. 
	 * @return boolean
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public abstract boolean hasNextData();
	
}
