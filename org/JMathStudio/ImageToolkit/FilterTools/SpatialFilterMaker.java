package org.JMathStudio.ImageToolkit.FilterTools;

import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.DataStructure.Vector.Vector;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.IllegalCellFormatException;
import org.JMathStudio.ImageToolkit.FilterTools.AdvancedFilters.GaussianIsotropicFilter;
import org.JMathStudio.ImageToolkit.Utilities.KernelFactory;
import org.JMathStudio.SignalToolkit.GeneralTools.Conv1DTools;
import org.JMathStudio.Exceptions.IllegalArgumentException;

/**
 * This class create various useful spatial filters for filtering discrete image.
 * <p>A spatial filter will be represented by a {@link SpatialFilter} object.
 * <pre>Usage:
 * int order = 7;
 * SpatialFilter binomial = SpatialFilterMaker.binomialFilter(order);//Create a new instance 
 * of Binomial 2D filter of required order.
 * 
 * int dimension = 5;//Parameters for Gaussian mask.
 * float std = 1;
 * SpatialFilter gaussian = SpatialFilterMaker.gaussianFilter(dimension, std);//Create an 
 * instance of Gaussian 2D filter with given parameters.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 *
 */
public final class SpatialFilterMaker {
	
	//Ensure no instances are made for utility classes.
	private SpatialFilterMaker(){}
	
	/**
	 * This method will create a Spatial moving average (MA)filter which replaces the pixel values
	 * in the image by the average of the surrounding square neighbourhood. The spatial filter will be return
	 * as a {@link SpatialFilter}.
	 * <p>The argument 'order' specify the order of the moving average filter. This specify the
	 * dimension of the square neighbourhood for computing the average pixel value.
	 * <p>The argument 'order' should be a positive odd number greater than '0' else this
	 * method will throw an IllegalArgument Exception. 
	 * <p>This is a low pass filter.
	 * @param int order
	 * @return SpatialFilter
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static SpatialFilter MA(int order) throws IllegalArgumentException
	{
		if(order <1 || order%2==0)
			throw new IllegalArgumentException();
		
		Cell mask = new Cell(order,order);
		
		float element = 1.0f/(order*order);
		
		for(int i=0;i<mask.getRowCount();i++)
		{
			for(int j=0;j<mask.getColCount();j++)
			{
				mask.setElement(element, i,j);
			}
		}
		
		return new SpatialFilter(mask);
		
	}
	/**
	 * This will create a Laplacian spatial filter and return the same as a {@link SpatialFilter}.
	 * <p>Laplacian spatial filter is characterised by a laplacian filter mask which has horizontal,
	 * vertical and diagonal elements.
	 * <p>This is a high pass filter.
	 * @return {@link SpatialFilter}
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static SpatialFilter laplacianFilter()
	{
		float norm = -1.0f/9;
		float[][] mask = new float[][]{{ norm, norm, norm }, { norm, 8.0f/9, norm },{ norm, norm, norm} };
		
		try{
		return new SpatialFilter(new Cell(mask));
		}catch(IllegalCellFormatException e)
		{
			throw new BugEncounterException();
		}
	}
	
	/**
	 * This will create a high boost filter and return the same as a {@link SpatialFilter}.
	 * <p>A high boost filter enhances the high frequency features without eliminating low
	 * frequency components.
	 * <p>The argument 'order' specify the order of this high boost filter.
	 * <p>A high boost filter mask of order 'c' is characterised as,
	 * <p><i> high boost mask = c*all pass mask + high pass mask.</i>
	 * <p>Here a laplacian mask is employed as a high pass mask.
	 * @param int order
	 * @return SpatialFilter
	 * @throws IllegalArgumentException 
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static SpatialFilter highBoost(int order) throws IllegalArgumentException
	{
		if(order <1)
			throw new IllegalArgumentException();
		
		float norm = -1.0f/9;
		float[][] mask = new float[][]{{ norm, norm, norm }, { norm, (8.0f/9)+(order), norm },{ norm, norm, norm} };
		
		try{
		return new SpatialFilter(new Cell(mask));
		}catch(IllegalCellFormatException e)
		{
			throw new BugEncounterException();
		}

	}

	/**
	 * This will create a Binomial spatial filter of order as specified by the argument 'order' and
	 * return the same as a {@link SpatialFilter}.
	 * <p>The argument 'order' specifying the order of the required binomial spatial filter should
	 * be more than '0' else this method will throw an IllegalArgument Exception.
	 * <p>The return binomial spatial filter is normalised, i.e sum of all the coefficients of it filter
	 * mask is '1'.
	 * <p>This is a Low pass filter.
	 * @param int order
	 * @return {@link SpatialFilter}
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static SpatialFilter binomialFilter(int order) throws IllegalArgumentException
	{
		//Binomial filter with order 0 is unit filter and negative is not possible.
		if(order < 1)
			throw new IllegalArgumentException();
		
		//Binomial filter mask of order 'N' is obtain by convolving this basic binomial
		//filter mask with itself N-1 times.
		Vector unitMask = new Vector(new float[]{1,1});
		
		//Store 1D binomial filter mask.
		Vector _1Dmask = new Vector(new float[]{1,1});

		//If order is more tha '1' need to do N-1 recurssive convolution on basic
		//binomial mask to obtain required order binomial mask.
		if(order >1)
		{
			Conv1DTools conv = new Conv1DTools();

			for(int i=2;i<=order;i++)
			{
				_1Dmask = conv.linearConv(_1Dmask, unitMask);
			}
		}
		
		//Find sum to normalise the filter mask so that sum of all elements of filter mask
		//is '1' as required for all low pass filters.
		float sum_1DMask=0;
		
		//Sum of obtained 1D binomial filter mask.
		for(int i=0;i<_1Dmask.length();i++)
			sum_1DMask+=_1Dmask.getElement(i);
		
		float normFactor;
		
		//The sum of all elements in the 2D binomial mask is square of the sum of all
		//elements in the 1D binomial mask.
		if(sum_1DMask == 0)
			throw new BugEncounterException();
		else
			normFactor = sum_1DMask*sum_1DMask;
		
		
		//Obtain equivalent 2D binomial mask from the 1D mask by using the principle of
		//separatibility. Binomial filter mask is separable i.e. it can be obtain by
		//multiplying the 1D mask along row and columns. 
		//Apply a norm factor to keep sum of all elements of the resultant filter mask
		//'1' as this is a low pass filter.
		
		Cell _2DMask = new Cell(_1Dmask.length() , _1Dmask.length());
		
		for(int i=0;i<_2DMask.getRowCount();i++)
		{
			for(int j=0;j<_2DMask.getColCount();j++)
			{
				float value = (_1Dmask.getElement(i)*_1Dmask.getElement(j))/normFactor;
				_2DMask.setElement(value, i,j);
			}
		}
		
		return new SpatialFilter(_2DMask);
		
	}
	
	/**
	 * This method will create a Spatial Gaussian Filter and return the same as a 
	 * {@link SpatialFilter}.
	 * <p>A gaussian spatial filter is characterised by a square gaussian filter mask.
	 * <p>The argument 'M' specify the dimension of the square gaussian filter mask of 
	 * the gaussian filter. The argument 'M' should be a positive odd integer greater
	 * than or equal to '3' else this method will throw an IllegalArgument Exception.
	 * <p>The argument 'std' specify the standard deviation for the gaussian filter mask
	 * of the gaussian filter. The argument 'std' should be more than zero
	 * else this method will throw an IllegalArgument Exception.
	 * <p>This return spatial gaussian filter will be normalised so that sum of all the filter
	 * mask coefficients is '1'. 
	 * <p>This is a low pass filter.
	 * <p>See fast implementation of the 2D Gaussian filter {@link GaussianIsotropicFilter}.
	 * @param int M
	 * @param float std
	 * @return {@link SpatialFilter}
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static SpatialFilter gaussianFilter(int M, float std)throws IllegalArgumentException {
		
		if(M<3 || M%2 == 0)
			throw new IllegalArgumentException();
		
		//This kernel is non-normalised gaussuan kernel. i.e. normalisation factor
		//sqrt(2*PI)*std is not multiplied.
		//Because we are going to divide the whole kernel by its total sum so as to have
		//sum of all elements of this low pass filter mask '1', normalisation of this
		//kernel is not necessary. So no normalisation factor is multiplied here.
		
		Cell filterMask = KernelFactory.gaussianKernel(M, M, std);
		
		float sum=0;
		
		for(int i=0;i<filterMask.getRowCount();i++)
		{
			for(int j=0;j<filterMask.getColCount();j++)
			{
				sum+=filterMask.getElement(i,j);
			}
		}
		if(sum==0)
			throw new BugEncounterException();
		
		float norm;
		
		for(int i=0;i<filterMask.getRowCount();i++)
		{
			for(int j=0;j<filterMask.getColCount();j++)
			{
				norm = filterMask.getElement(i,j)/sum;
				filterMask.setElement(norm,i,j);
			}
		}
		return new SpatialFilter(filterMask);
	}

}
