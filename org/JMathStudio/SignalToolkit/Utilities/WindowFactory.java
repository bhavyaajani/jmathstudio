package org.JMathStudio.SignalToolkit.Utilities;

import org.JMathStudio.DataStructure.Vector.Vector;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.IllegalArgumentException;

/**
 * This class define various standard discrete Window.
 * <p>A window signal will be represented by a {@link Vector} object.
 * <pre>Usage:
 * 
 * Vector barlett = WindowFactory.bartlett(128);//Create a Barlett window of given length.
 * 
 * Vector hamming = WindowFactory.hamming(128);//Create a Hamming window of given length.
 * 
 * Vector kaiser = WindowFactory.kaiser(128, 10);//Create a Kaiser window of given length
 * and roll off.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class WindowFactory
{
	//Ensure no instances are made of Utility class.
	private WindowFactory(){}
	
	/**
	 * This method will create a Bartlett window of length as given by the argument 'N' and return
	 * the same as a Vector.
	 * <p>The argument 'N' which specify the length of the window should be more than 0 else this 
	 * method will throw an IllegalArgument Exception.
	 * <p>The return Bartlett window will be normalised in amplitude, with peak amplitude located at
	 * the centre of the window.
	 * @param int N
	 * @return Vector
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Vector bartlett(int N) throws IllegalArgumentException
	{
		if(N<=0)
			throw new IllegalArgumentException();
		else
		{
			try{
				
				Vector result = new Vector(N);
				float L = N-1;
				int n = (N-1)/2;
				for(int i=0;i<=n;i++)
					result.setElement(2*i/L, i);
				for(int i=n+1;i<N;i++)
					result.setElement(2 -(2*i/L), i);
				
				return result;
			}
			catch(ArrayIndexOutOfBoundsException e)
			{
				throw new BugEncounterException();
			}
		}
	}
	/**
	 * This method will create a Hanning window of length as given by the argument 'N' and return
	 * the same as a Vector.
	 * <p>The argument 'N' which along with length also specify the roll off, should be more than 0
	 * else this method will throw an IllegalArgument Exception.
	 * <p>The return Hanning window will be normalised in amplitude and main lobe will be located at
	 * the centre.
	 * @param int N
	 * @return Vector
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Vector hanning(int N) throws IllegalArgumentException
	{
		if(N <1)
		{
			throw new IllegalArgumentException();
		}

		float[] result = new float[N];
		
		for(int i=0;i<result.length;i++)
		{
			result[i] = (float) (0.5*(1 - Math.cos(2*Math.PI*i/N)));
		}
		return new Vector(result);
	}
	
	/**
	 * This method will create a Hamming window of length as given by the argument 'N' and return
	 * the same as a Vector.
	 * <p>The argument 'N' which along with length also specify the roll off, should be more than 0
	 * else this method will throw an IllegalArgument Exception.
	 * <p>The return Hamming window will be normalised in amplitude and main lobe will be located at
	 * the centre.
	 * @param int N
	 * @return Vector
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Vector hamming(int N) throws IllegalArgumentException
	{
		if(N <1)
		{
			throw new IllegalArgumentException();
		}

		float[] result = new float[N];
		double arg = 2*Math.PI/N;
		
		for(int i=0;i<result.length;i++)
		{
			result[i] = (float) (0.54 - 0.46*Math.cos(arg*i));
		}
		return new Vector(result);

	}
	
	/**
	 * This method will create a Bohman window of length as given by the argument 'N' and return
	 * the same as a Vector.
	 * <p>The argument 'N' which along with length also specify the roll off, should be more than 0
	 * else this method will throw an IllegalArgument Exception.
	 * <p>The return Bohman window will be normalised in amplitude and main lobe will be located at
	 * the centre.
	 * @param int N
	 * @return Vector
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Vector bohman(int N) throws IllegalArgumentException
	{
		if(N <1)
		{
			throw new IllegalArgumentException();
		}

		float[] result = new float[N];
		
		for(int i=0;i<result.length;i++)
		{
			float parameter = (float) (2.0*(Math.abs(i - N/2.0)/N));
			
			result[i] = (float) ((1-parameter)*(Math.cos(Math.PI*parameter)) + (Math.sin(Math.PI*parameter))/Math.PI);
		}
		return new Vector(result);

	}
	
	/**
	 * This method will create a Welch window of length as given by the argument 'N' and return
	 * the same as a Vector.
	 * <p>The argument 'N' which along with length also specify the roll off, should be more than 0
	 * else this method will throw an IllegalArgument Exception.
	 * <p>The return Welch window will be normalised in amplitude and main lobe will be located at
	 * the centre.
	 * @param int N
	 * @return Vector
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Vector welch(int N) throws IllegalArgumentException
	{
		if(N <1)
		{
			throw new IllegalArgumentException();
		}

		float[] result = new float[N];
		
		for(int i=0;i<result.length;i++)
		{
			float parameter = (float) (2.0*(i - N/2.0)/N);
			
			result[i] = (float) (1-parameter*parameter);
		}
		return new Vector(result);

	}
	
	/**
	 * This method will create a BlackMan window of length as given by the argument 'N' and return
	 * the same as a Vector.
	 * <p>The argument 'N' which along with length also specify the roll off, should be more than 0
	 * else this method will throw an IllegalArgument Exception.
	 * <p>The return BlackMan window will be normalised in amplitude and main lobe will be located at
	 * the centre.
	 * @param int N
	 * @return Vector
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Vector blackMan(int N) throws IllegalArgumentException
	{
		if(N <1)
		{
			throw new IllegalArgumentException();
		}

		float[] result = new float[N];
		double arg = 2*Math.PI/N;
		
		for(int i=0;i<result.length;i++)
		{
			float W = (float) (arg*i);
			
			result[i] = (float) (0.42 - 0.50*Math.cos(W) + 0.08*Math.cos(2.0*W));
		}
		return new Vector(result);
	}
	
	/**
	 * This method will create a Gaussian window defining a gaussian curve of length as given by the argument 'N' and return
	 * the same as a Vector.
	 * <p>The argument 'N' should be more than 0 else this method will throw an IllegalArgument
	 * Exception.
	 * <p>The width of the gaussian lobe will be twice that of the argument 'stddev', which specify the
	 * standard deviation of the gaussian curve. The argument 'stddev' should be more than 0 else this
	 * method will throw an IllegalArgument Exception.
	 * <p>The Gaussian lobe will be centred at the index position as given by the argument 'mean', which
	 * specify the mean of the gaussian curve.
	 * <p>The return Gaussian window is based on a non-normalised gaussian function define over a 1D
	 * support. The maximum amplitude value at the centre of the gaussian lobe will be '1'.
	 * <p>If one want to derive a normalised gaussian kernel one has to normalised the given gaussian window
	 * with appropriate normalisation factor.
	 * @param int N
	 * @param float stddev
	 * @param float mean
	 * @return Vector
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Vector gaussian(int N, float stddev,float mean) throws IllegalArgumentException
	{
		if(N < 1)
		{
			throw new IllegalArgumentException();
		}
		if(stddev <= 0)
		{
			throw new IllegalArgumentException();
		}
		
		float[] result = new float[N];
		float norm = 2.0f*stddev*stddev;
		
		for(int i=0;i<result.length;i++)
		{
//			float m = (N-1.0f)/2.0f;
			float index = (i-mean)*(i-mean)/(norm);
			result[i] = (float) Math.exp(-index);
		}
		
		return new Vector(result);
	}

	/**
	 * This method will create an Exponential window of length as given by the argument 'N' and
	 * return the same as a Vector.
	 * <p>The input argument 'N' should be more than 0 else this method will throw an IllegalArgument
	 * Exception.
	 * <p>The argument 'rollOff' specify the roll off for the exponential window and should be in the
	 * range of [0,1] else this method will throw an IllegalArgument Exception.
	 * <p>The return Exponential window will be normalised in Amplitude with the main lobe located
	 * at the centre.
	 * @param int N
	 * @param float rollOff
	 * @return Vector
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Vector exponential(int N,float rollOff) throws IllegalArgumentException
	{
		if(N<1 || rollOff<0 || rollOff>1)
			throw new IllegalArgumentException();
		
		float[] result = new float[N];
		int centre = (N-1)/2;
		
		for(int i=0;i<result.length;i++)
		{
			int index = Math.abs(i-centre);
			result[i] = (float) Math.exp(-rollOff*index);
		}
		
		return new Vector(result);
	}
	
	/**
	 * This method will create a Kaiser window of length as given by the argument 'N' and return
	 * the same as a Vector.
	 * <p>The argument 'N' should be more than 0 else this method will throw an IllegalArgument
	 * Exception.
	 * <p>The argument 'alpha' specify the roll off for the window and should be more than 1 else
	 * this method will throw an IllegalArgument Exception.
	 * <p>The return Kaiser window will be normalised in Amplitude with main lobe located at the
	 * centre.
	 * @param int N
	 * @param float alpha
	 * @return Vector
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Vector kaiser(int N,float alpha) throws IllegalArgumentException
	{
		if(N <1 || alpha <1)
			throw new IllegalArgumentException();
		
		float[] w = new float[N];
		int m = (N-1)/2;
		
		// zero order Bessel function of the first kind
	    final float eps = 1.0e-6f;   // accuracy parameter
	    float fact = 1.0f;
	    float x2 = 0.5f * alpha;
	    float p = x2;
	    float t = p * p;
	    float I0alpha = 1.0f + t;
	    for (int k = 2; t > eps; k++) 
	    {
	      p *= x2;
	      fact *= k;
	      t = (p / fact)*(p / fact);
	      I0alpha += t;
	    }
	    	    
		for (int n = -m; n <= m; n++)
		{
			float para = ((float)n*n)/((float)m*m);
			para =  (float) (alpha*Math.sqrt(1 - para));
			
			fact = 1.0f;
		    x2 = 0.5f * para;
		    p = x2;
		    t = p * p;
		    float s = 1.0f + t;
		    for (int k = 2; t > eps; k++) 
		    {
		      p *= x2;
		      fact *= k;
		      t = (p / fact)*(p / fact);
		      s += t;
		    }
	        w[m + n] = s / I0alpha;
		}
		return new Vector(w);
	}
	
}
