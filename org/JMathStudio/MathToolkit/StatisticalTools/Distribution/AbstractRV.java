package org.JMathStudio.MathToolkit.StatisticalTools.Distribution;

/**
 * This class define an Abstract Random Variable following a specific
 * statistical distribution.
 * 
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 * 
 */
public abstract class AbstractRV {
	/**
	 * This method will return a random value taken by the given random variable
	 * with the given statistical distribution.
	 * 
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public abstract float nextRandomValue();

}
