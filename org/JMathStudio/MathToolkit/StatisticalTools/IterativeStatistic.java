package org.JMathStudio.MathToolkit.StatisticalTools;

/**
 * This class iteratively estimate the Statistical parameters for a set of elements when the
 * elements are drawn one by one.
 * <p>This class is useful in estimating the descriptive statistics for a set of elements when
 * all the elements are not available at one instant but are drawn one after another. 
 * <p>Following descriptive statistics are estimated by this class:
 * <p><i>Mean,Variance and Standard deviation.</i>
 * <p>The statistics computed are for the first 'N' elements available so far.
 * <p>If an additional element 'N+1' is available the class will update the descriptive statistics
 * for 'N+1' elements. 
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class IterativeStatistic {

	private float i3;
	private float i0;
	private int i1;
	
	/**
	 * This will instantiate a new Iterative Statistic object with initial mean, variance,
	 * standard deviation and count of elements equal to '0'.
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public IterativeStatistic(){
		this.i3 = 0;
		this.i0 = 0;
		this.i1 = 0;
	}
	
	/**
	 * This method will update the estimated statistics for the 'N' elements encountered so far
	 * with an additional element as specified by the argument 'x'.
	 * <p>The updated statistics will now reflect statistics for these 'N+1' elements.  
	 * @param float x
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void update(float x){
		
		float lastMean = i3;
		int N = i1+1;
		
		i3 = (i1*i3 + x)/(N);
		
		float deltaMean = i3 - lastMean;
		i0 =  (i1*i0)/N + i1*deltaMean*deltaMean;
		
		i1 = N;
	}
	
	/**
	 * This will return the estimated Mean of the elements encountered so far.
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float getMean(){
		return this.i3;
	}
	/**
	 * This will return the estimated Variance of the elements encountered so far.
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float getVariance(){
		return this.i0;
	}
	/**
	 * This will return the estimated Standard deviation of the elements encountered so far.
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float getStandardDeviation(){
		return (float)Math.sqrt(this.i0);
	}
	/**
	 * This will return the number of elements encountered so far.
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public int getSampleCount(){
		return this.i1;
	}
	/**
	 * This method will clean all computed statistics and reset the state of the object
	 * to its initial default state.
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void reset(){
		this.i3 = 0;
		this.i0 = 0;
		this.i1 = 0;
	}
}
