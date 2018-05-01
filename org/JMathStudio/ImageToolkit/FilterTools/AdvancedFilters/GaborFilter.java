package org.JMathStudio.ImageToolkit.FilterTools.AdvancedFilters;

import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.ImageToolkit.GeneralTools.Conv2DTools;
import org.JMathStudio.ImageToolkit.Utilities.KernelFactory;
import org.JMathStudio.MathToolkit.MatrixTools.MatrixTools;
import org.JMathStudio.Exceptions.IllegalArgumentException;

/**
 * This class define a spatial Gabor filter which is characterised by a square Gabor filter mask.
 * <p>The 2D Gabor filter mask represent a 2D gabor function which consists of a cosine waveform
 * enveloped by a gaussian waveform.
 * <p>This forms a texture mask which is mostly employed in texture analysis.
 * <pre>Usage:
 * Cell img = Cell.importImageAsCell("path");//Import input image as Cell.
 * 
 * int M = 7;//Parameters for Gabor filter.
 * int lamada = 3;
 * int theta = 0;
 * int std = 2;
 * float gamma = 1;
 * 
 * GaborFilter gf = new GaborFilter(M,lamada,theta,std,gamma);//Create an instance of GaborFilter
 * with given parameters.
 * 
 * Cell result = gf.filter(img);//Apply Gabor filtering on the input image.
 * </pre>
 * 
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class GaborFilter {

	private int i1;
	private int i2;
	private int i5;
	private int i9;
	private float i0;
	private Cell i3;
	
	/**
	 * This will create a Gabor filter based on the parameter as provided. 
	 * This filter is a spatial filter which is characterised by a square Gabor filter mask. Such a filter
	 * mask is in essence a cosine waveform enveloped by a gaussian waveform. Thus the parameters provided
	 * describe the cosine and the gaussian waveform to be in the filter mask and thus define the filter characteristics.
	 * <p>The argument 'M' specify the dimension of the square gabor filter mask. The argument 'M' should be a 
	 * positive odd integer greater than 0 else this method will throw an IllegalArgument Exception.
	 * <p>The argument 'lamada' here specify the spatial wavelength of the cosine waveform in the 
	 * gabor filter mask. The argument 'lamada' should be more than 0 else this method will throw
	 * an IllegalArgument Exception.
	 * <p>The argument 'theta' specify the orientation of the cosine waveform in degrees, to the parallel 
	 * stripes of the gabor filter mask. The argument 'theta' specified in the unit of degrees should be in the range
	 * of [0 180] else this method will throw an IllegalArgument Exception.
	 * <p>The argument 'std' specify the standard deviation of the gaussian waveform in the gabor filter mask. 
	 * The argument 'std' should be more than 0 else this method will throw an IllegalArgument Exception.
	 * <p>The argument 'gamma' specify the spatial aspect ratio or ellipticity for the gabor filter mask.
	 * Default value for ellipticity should be '1'.
	 * @param int M
	 * @param int lamada
	 * @param int theta
	 * @param int std
	 * @param float gamma
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public GaborFilter(int M,int lamada,int theta,int std, float gamma) throws IllegalArgumentException
	{
		if(M <1 || M%2==0 || lamada<1 || theta <0 || theta >180 || std<1)
			throw new IllegalArgumentException();
		else
		{
			this.i1 = M;
			this.i2 = lamada;
			this.i5 = theta;
			this.i9 = std;
			this.i0 = gamma;
			
			f4();
		}
	}
	/**
	 * This method will apply the given Gabor filter on the discrete image as represented by the
	 * {@link Cell} 'image' and return the resultant filtered image as a Cell.
	 * @param Cell image
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell filter(Cell image)
	{		
		return new Conv2DTools().linearConvSame(image, i3);	
	}

	/**
	 * This method will return the dimension of the square gabor filter mask.
	 * @return int
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public int getMaskDimension()
	{
		return this.i1;
	}
	
	/**
	 * This method will reset the dimension of the square gabor filter mask to the argument
	 * 'N' and reconfigure the given filter as per the new specification.
	 * <p>The argument 'N' should be a positive odd integer greater than 0 else this method
	 * will throw an IllegalArgument Exception.
	 * @param int N
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void resetMaskDimension(int N) throws IllegalArgumentException
	{
		if(N<1 || N%2==0)
			throw new IllegalArgumentException();
		else
		{
			this.i1 = N;
			f4();
		}
	}
	
	/**
	 * This method will return the spatial wavelength of the cosine wave in the 
	 * given gabor filter mask.
	 * @return int
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public int getSpatialWavelengthOfMask()
	{
		return this.i2;
	}
	 
	/**
	 * This method will reset the spatial wavelength of the cosine wave in the 
	 * given gabor filter mask to the argument 'lamada' and reconfigure the given
	 * filter as per the new specification.
	 * <p>The argument 'lamada' should be more than 0 else this method will throw
	 * an IllegalArgument Exception.
	 * @param int lamada
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void resetSpatialWavelengthOfMask(int lamada) throws IllegalArgumentException
	{
		if(lamada<1)
			throw new IllegalArgumentException();
		else
		{
			this.i2 = lamada;
			f4();
		}
	}
	
	/**
	 * This method will return the orientation of the cosine wave (in degrees) in the given gabor filter mask.
	 * @return int
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public int getOrientationOfMask()
	{
		return this.i5;
	}
	
	/**
	 * This method will reset the orientation of the cosine wave in the given gabor filter by the degrees as specified
	 * by the argument 'theta' and reconfigure the given filter as per the new specification.
	 * <p>The argument 'theta' specified in the unit of degrees should be in the range
	 * of [0 180] else this method will throw an IllegalArgument Exception.
	 * @param int theta
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void resetOrientationOfMask(int theta) throws IllegalArgumentException
	{
		if(theta <0 || theta >180)
			throw new IllegalArgumentException();
		else
		{
			this.i5 = theta;
			f4();
		}
	}
	
	/**
	 * This method will return the measure of ellipticity of the given gabor filter mask.
	 * @return int
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float getEllipticityOfMask()
	{
		return this.i0;
	}
	
	/**
	 * This method will reset the measure of ellipticity of the given gabor filter mask as per the
	 * argument 'gamma' and reconfigure the given filter as per the new specification.
	 * @param float gamma
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void resetEllipticityOfMask(float gamma)
	{
		this.i0 = gamma;
		f4();
	}
	
	/**
	 * This method will return the standard deviation of the gaussian waveform in the given gabor filter mask.
	 * @return int
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public int getStandardDeviationOfMask()
	{
		return this.i9;
	}
	
	/**
	 * This method will reset the standard deviation of the gaussian waveform in the given gabor filter mask to
	 * the argument 'std' and reconfigure the given filter as per the new specification.
	 * <p>The argument 'std' should be more than 0 else this method will throw an IllegalArgument Exception.
	 * @param int std
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void resetStandardDeviationOfMask(int std) throws IllegalArgumentException
	{
		if(std<1)
			throw new IllegalArgumentException();
		else
		{
			this.i9 = std;
			f4();
		}
	}
	
	/**
	 * This will return the convolution mask associated with the given filter as {@link Cell}.
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell accessConvolutionMask()
	{
		return this.i3;
	}
	
	private void f4()
	{
		try{
			Cell filterMask = KernelFactory.gaborKernel(i1, i1, i2, i5, i9, i0);
			MatrixTools matrix = new MatrixTools();
			this.i3 = matrix.flipRows(matrix.flipColumns(filterMask));
		}catch(IllegalArgumentException e)
		{
			throw new BugEncounterException();
		}
	}

}
