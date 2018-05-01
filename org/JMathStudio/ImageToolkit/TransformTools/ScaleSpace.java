package org.JMathStudio.ImageToolkit.TransformTools;

import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.DataStructure.Cell.CellStack;
import org.JMathStudio.DataStructure.Vector.Vector;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.DimensionMismatchException;
import org.JMathStudio.Exceptions.IllegalArgumentException;
import org.JMathStudio.ImageToolkit.FilterTools.Separable2DFilter;
import org.JMathStudio.ImageToolkit.GeneralTools.ImageUtilites;
import org.JMathStudio.MathToolkit.MatrixTools.MatrixTools;
import org.JMathStudio.SignalToolkit.Utilities.WindowFactory;

/**
 * This class supports framework for scale space representation of a discrete real image.
 * <p>Scale space framework is a formal way for handling image structures at different scales, 
 * by representing an image as a one-parameter family of smoothed images, the scale-space representation,
 * parametrized by the size of the smoothing kernel used for suppressing fine-scale structures.
 * <p>A discrete real image will be represented by a {@link Cell}.
 * <pre>Usage:
 * Cell img = Cell.importImageAsCell("path");//Import input image as Cell.
 * 
 * ScaleSpace ss = new ScaleSpace();//Create an instance of ScaleSpace.
 * 
 * CellStack scales = ss.gaussianScaleSpace(img, sigma, levels);//Compute different gaussian scales
 * of the input image.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class ScaleSpace {
	
	private ImageUtilites iu = new ImageUtilites();
	
	/**
	 * This method computes the linear Gaussian scale-space representation of a discrete
	 * real image as represented by {@link Cell} 'image' for different scales. Scale-space
	 * representation of the 'image' (also termed as gaussian pyramid) at different scales
	 * is returned as a {@link CellStack}.
	 * <p>The argument 'sigma' specify the standard deviation of the gaussian filter to be 
	 * employed for the gaussian scale-space. This argument can also be used to control the
	 * cut-off frequency for the gaussian low pass filter.
	 * <p>The value of argument 'sigma' should be more than '0' else this method will throw an
	 * IllegalArgument Exception.
	 * <p>The argument 'levels' here specify the number of decomposition level for the given 
	 * gaussian scale-space. In a gaussian scale space, image at a given level 'N' is the smoothen 
	 * version (gaussian filtered) of the image at level 'N-1' and subsequently down sampled by
	 * the factor of '2'. Thus image in the gaussian scale-space at level 'N' would be,
	 * <p><i>G(N) = down sampled ( gaussian filtered(G(N-1)) ).
	 * </i>
	 * <p>As at each level the image is down sampled by the factor of '2' the argument 'levels' should
	 * be such that the dimension of the input image is not less than the value <i>2^(levels-1)</i> else 
	 * this method will throw an IllegalArgument Exception. Further the argument 'levels' should be 
	 * more than '0' else this method will throw an IllegalArgument Exception.
	 * <p>The calculated gaussian pyramid of the input image, that is the images at different scale/level
	 * in increasing order are returned as a {@link CellStack}. The image at the index position '0' is 
	 * the image at '0' level, that is the original image, and the subsequent images at higher index position
	 * are the images at that level of scale-space.
	 * @param Cell image
	 * @param float sigma
	 * @param int levels
	 * @return CellStack
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public CellStack gaussianScaleSpace(Cell image,float sigma,int levels) throws IllegalArgumentException
	{
		if(sigma<=0 || levels <1)
			throw new IllegalArgumentException();
		else
		{
			int height = image.getRowCount();
			int width = image.getColCount();
			int minRequiredSize = (int) Math.pow(2, levels-1);
		
			//A image is to be down sampled by '2' at each level ensure
			//that given image dimension are sufficient large for the stated
			//number of levels.
			if(height < minRequiredSize || width < minRequiredSize)
				throw new IllegalArgumentException();
			
			//stack containing gaussian pyramidical decomposition of the
			//given image, in increasing order of level.
			Cell[] result = new Cell[levels];
			
			//0th Level, i.e. original image.
			result[0] = image.clone();
			
			//Note: For a gaussian kernel with a standard deviation 'sigma', length of the kernel
			//should be ideally an odd number more than 7*sigma. 
			int N = f2((int)Math.ceil(7*sigma));
			
			//To centre the gaussian kernel.
			float mean = (N-1.0f)/2.0f;
			
			//Get the required gaussian kernel.
			Vector gauss = WindowFactory.gaussian(N, sigma, mean);
			float sum =0;
			
			//Make sum of all elements of the gaussian kernel '1'.
			for(int i=0;i<gauss.length();i++)
				sum+=gauss.getElement(i);
			
			if(sum == 0)
				throw new BugEncounterException();
			else
			{
				for(int i=0;i<gauss.length();i++)
					gauss.setElement(gauss.getElement(i)/sum, i);
			}
			
			//Use of separable 2D filter for faster filtering.
			//create a 2d gaussian kernel with given standard deviation
			//as separable filter using the obtained normalised 1d kernel.
			Separable2DFilter gaussian = new Separable2DFilter(gauss,gauss);
			
			for(int i=1;i<levels;i++)
			{
				Cell lastScale = result[i-1];
				//Image at a given level is obtain by smoothning the image from last
				//level by a gaussian filter.
				Cell thisScale = gaussian.filter(lastScale);
				//Down sample the image at given level by factor of '2'.
				thisScale = iu.downSample2D(thisScale,thisScale.getRowCount()/2,thisScale.getColCount()/2);
	
				result[i] = thisScale;
				
			}
			
			return new CellStack(result);
		}
	}
	
	/**
	 * This method computes the linear Laplacian scale-space representation of a discrete
	 * real image as represented by {@link Cell} 'image' for different scales. Scale-space
	 * representation of the 'image' (also termed as laplacian pyramid) at different scales
	 * is returned as a {@link CellStack}.
	 * <p>The argument 'sigma' specify the standard deviation of the gaussian filter to be 
	 * employed for computing the laplacian scale-space. This argument can also be used to control the
	 * cut-off frequency for the high pass filter.
	 * <p>The value of argument 'sigma' should be more than '0' else this method will throw an
	 * IllegalArgument Exception.
	 * <p>The argument 'levels' here specify the number of decomposition level for the given 
	 * laplacian scale-space. A laplacian scale space is computed by taking the difference 
	 * between the subsequent levels of the gaussian scale space.
	 * <p>Thus if G(N) is the image in the gaussian scale space at level 'N', let G'(N+1) be the
	 * gaussian filtered G(N) before it is down sampled. Then the corresponding image in the laplacian
	 * scale space at level 'N' would be,
	 * <p><i> L(N) = G(N) - G'(N+1),
	 * <p>where G'(N+1) = gaussian filtered( G(N) ) and
	 * <p>G(N) = down sampled( G'(N-1) ).
	 * </i>
	 * <p>As at each level the image is down sampled by the factor of '2' the argument 'levels' should
	 * be such that the dimension of the input image is not less than the value <i>2^(levels-1)</i> else 
	 * this method will throw an IllegalArgument Exception. Further the argument 'levels' should be 
	 * more than '0' else this method will throw an IllegalArgument Exception.
	 * <p>The calculated laplacian pyramid of the input image, that is the images at different scale/level
	 * in increasing order are returned as a {@link CellStack}. The image at the subsequent index position
	 * in the stack is the image at that level of scale-space.
	 * @param Cell image
	 * @param float sigma
	 * @param int levels
	 * @return CellStack
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public CellStack laplacianScaleSpace(Cell image,float sigma,int levels) throws IllegalArgumentException
	{
		if(sigma<=0 || levels <1)
			throw new IllegalArgumentException();
		else
		{
			int height = image.getRowCount();
			int width = image.getColCount();
			int minRequiredSize = (int) Math.pow(2, levels-1);
		
			//A image is to be down sampled by '2' at each level ensure
			//that given image dimension are sufficient large for the stated
			//number of levels.
			if(height < minRequiredSize || width < minRequiredSize)
				throw new IllegalArgumentException();
			
			try{
			//stack containing laplacian pyramidical decomposition of the
			//given image, in increasing order of level.
			Cell[] result = new Cell[levels];
			
			//Create gaussian filter, to be used in computing laplacian pyramid.
			
			//Note: For a gaussian kernel with a standard deviation 'sigma', length of the kernel
			//should be ideally an odd number more than 7*sigma. 
			int N = f2((int)Math.ceil(7*sigma));
			
			//To centre the gaussian kernel.
			float mean = (N-1.0f)/2.0f;
			
			//Get the required gaussian kernel.
			Vector gauss = WindowFactory.gaussian(N, sigma, mean);
			float sum =0;
			
			//Make sum of all elements of the gaussian kernel '1'.
			for(int i=0;i<gauss.length();i++)
				sum+=gauss.getElement(i);
			
			if(sum == 0)
				throw new BugEncounterException();
			else
			{
				for(int i=0;i<gauss.length();i++)
					gauss.setElement(gauss.getElement(i)/sum, i);
			}
			
			//Use of separable 2D filter for faster filtering.
			//create a 2d gaussian kernel with given standard deviation
			//as separable filter using the obtained normalised 1d kernel.
			Separable2DFilter gaussian = new Separable2DFilter(gauss,gauss);
			
			MatrixTools mtools = new MatrixTools();
			
			//Let G(N) be gaussian filtered image at Nth level. Now level starts from '0'
			//so G(0) will be original image.
			Cell lastScale = image.clone();
			//Now Laplacian at each level 'N' is the difference of G(N) - G(N+1)
			Cell laplacian;
			int lastLevel = levels-1;
			
			for(int i=0;i<levels;i++)
			{
				//G(N+1) = gaussian_filter(G(N)).
				Cell thisScale = gaussian.filter(lastScale);
				
				//Laplacian = G(N) - G(N+1);
				laplacian = mtools.subtract(lastScale, thisScale);
				
				//G(N+1) = down_sample(G(N+1)).
				//Next level is always down sampled by '2'.
				
				//Down sample and store G(N+1) as last level till next level exist.
				//Don't change this condition: Useful when level is high enough
				//to vanish the last level, if further down sampling is carried out giving
				//exception.
				if(i < lastLevel)
					//Also for next level of N, G(N+1) becomes last level.
					lastScale = iu.downSample2D(thisScale, thisScale.getRowCount()/2, thisScale.getColCount()/2);
	
				//Laplacian at level 'N'.
				result[i] = laplacian;
			}
			
			return new CellStack(result);
			
			}
			catch(IllegalArgumentException e) {
				throw new BugEncounterException();
			}
			catch(DimensionMismatchException e){
				throw new BugEncounterException();
			}
		}
	}
	
	private int f2(int value)
	{
		if(value%2 == 0)
			return value+1;
		else 
			return value;			
	}

}
