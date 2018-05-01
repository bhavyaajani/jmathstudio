package org.JMathStudio.SignalToolkit.FilterTools;

import org.JMathStudio.DataStructure.Vector.Vector;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.SignalToolkit.GeneralTools.Conv1DTools;
import org.JMathStudio.SignalToolkit.Utilities.WindowFactory;
import org.JMathStudio.Exceptions.IllegalArgumentException;

/**
 * This class creates various Digital Impulse Filters.
 * @see ImpFilter
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class ImpFilterMaker 
{
	//Ensure no instances are made of Utility class.
	private ImpFilterMaker(){}
	
	/**
	 * This method will create a Digital Impulse filter characterised by a Gaussian
	 * impulse response and return the same as an {@link ImpFilter} object.
	 * <p>The argument 'sigma' here specify the standard deviation of the gaussian impulse.
	 * The value of the argument 'sigma' should be more than or equal to '0.5' else this method will throw an IllegalArgument Exception.
	 * <p>The argument 'N' specify the length of the gaussian impulse and should be an odd integer
	 * greater than or equal to '3' else this method will throw an IllegalArgument Exception.
	 * <p>The gaussian impulse of the return Digital Impulse filter will be symmetric and centred on the
	 * origin. 
	 * @param int N
	 * @param int sigma
	 * @return {@link ImpFilter}
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static ImpFilter gaussianFilter(int N,float sigma) throws IllegalArgumentException
	{
		if(N<3 || N%2==0)
			throw new IllegalArgumentException();
		if(sigma<0.5f)
			throw new IllegalArgumentException();
		
		float[] gaussian = WindowFactory.gaussian(N,sigma,(N-1)/2).accessVectorBuffer();
		
		float sum=0;
		for(int i=0;i<gaussian.length;i++)
			sum+=gaussian[i];
				
		if(sum ==0)
			throw new BugEncounterException();
		
		for(int i=0;i<gaussian.length;i++)
			gaussian[i] = gaussian[i]/sum;
		
		return new ImpFilter(new Vector(gaussian),(N-1)/2);
	}

	
	
	/**
	 * This method will create a Digital Impulse Filter characterised by a Sinc impulse response
	 * and return the same as an {@link ImpFilter} object. The return filter is a low pass Sinc filter.
	 * <p>The argument 'N' specify the roll off for the filter and should be even number larger
	 * than 1 else this method will throw an IllegalArgument Exception. The length of the resultant impulse
	 * shall be 'N+1'.
	 * <p>The argument 'fc' specify the normalised upper cut off frequency and
	 * should be in the range of [0,0.5] else this method will throw an IllegalArgument Exception.
	 * <p>The argument 'windowed' specify whether a smoothing Hanning window is to be applied on the 
	 * Sinc impulse.
	 * <p>The main central lobe of the Sinc impulse of the resultant Digital Impulse Filter will be 
	 * located at the origin.
	 * @param int N
	 * @param float fc
	 * @param boolean windowed
	 * @return {@link ImpFilter}
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static ImpFilter sincFilter(int N, float fc,boolean windowed) throws IllegalArgumentException
	{
		if(N<2 || N%2 !=0)
			throw new IllegalArgumentException();
		
		if(fc<0 || fc>0.5f)
			throw new IllegalArgumentException();
		
			float PI = (float) Math.PI;
			float[] h = new float[N+1];//0 to M
			float K = 2*PI*fc;
			float sum=0;
			
			for(int i=0;i<h.length;i++)
			{
				if(i!=N/2)
					h[i] = (float) (Math.sin(2*PI*fc*(i-N/2.0f))/(i-N/2.0f));
				else
					h[i] = K;
				
				if(windowed)
				{
					float WPoint = (float) (0.42f - 0.5*Math.cos(2*PI*i/(float)N) + 0.08*Math.cos(4*PI*i/(float)N));
					h[i]=h[i]*WPoint;
				}
				sum +=h[i];
			}
			
			for(int i=0;i<h.length;i++)
			{
				h[i] = h[i]/sum;
			}
			
			return new ImpFilter(new Vector(h),(h.length-1)/2);
			
	}

	/**
	 * This will create a Digital Impulse Filter characterised by a symmetrical impulse which consist of
	 * binomial coefficients of polynomial of given order and return the same as a {@link ImpFilter} object.
	 * <p>The argument 'order' specifying the order of the required binomial filter should
	 * be more than '0' else this method will throw an IllegalArgument Exception.
	 * <p>The given binomial filter is characterised by a symmetrical impulse response consisting
	 * of binomial coefficients of polynomial of that order.
	 * <p>This is a Low pass filter.
	 * @param int order
	 * @return {@link ImpFilter}
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static ImpFilter binomialFilter(int order) throws IllegalArgumentException
	{
		//Binomial filter with order 0 is unit filter and negative is not possible.
		if(order < 1)
			throw new IllegalArgumentException();
		
		//Binomial filter impulse response of order 'N' is obtain by convolving this basic binomial
		//filter response with itself N-1 times.
		Vector unitMask = new Vector(new float[]{1,1});
		
		//Store 1D binomial iir response.
		Vector impulse = new Vector(new float[]{1,1});

		//If order is more than '1' need to do N-1 recurssive convolution on basic
		//binomial filter response to obtain required order binomial iir response.
		if(order >1)
		{
			Conv1DTools conv = new Conv1DTools();

			for(int i=2;i<=order;i++)
			{
				impulse = conv.linearConv(impulse, unitMask);
			}
		}
		
		//Find sum to normalise the filter so that sum of all elements of iir filter
		//is '1' as required for all low pass filters.
		float sum=0;
		
		//Sum of obtained 1D binomial filter iir.
		for(int i=0;i<impulse.length();i++)
			sum+=impulse.getElement(i);
		
		if(sum == 0)
			throw new BugEncounterException();
		else
		{
			for(int i=0;i<impulse.length();i++)
			{
				impulse.setElement(impulse.getElement(i)/sum, i);
			}
		}
		
		return new ImpFilter(impulse,impulse.length()/2);
		
	}


}
