package org.JMathStudio.ImageToolkit.ClusterTools;

import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.MathToolkit.StatisticalTools.Histogram;
import org.JMathStudio.PixelImageToolkit.BinaryPixelImage.BinaryPixelImage;
import org.JMathStudio.PixelImageToolkit.UIntPixelImage.AbstractUIntPixelImage;

/**
 * This class define various Image/Pixel Classification/Clustering operations.
 * <pre>Usage:
 * AbstractUIntPixelImage img = UInt8PixelImage.importImage("path");//Import input image as
 * UIntPixelImage type.
 * 
 * ImageClassifier ic = new ImageClassifier();//Create an instance of ImageClassifier.
 * 
 * BinaryPixelImage result = ic.fisherBinaryClassification(img);//Apply fisher binary classification on
 * the input UIntPixelImage and get resultant 2 class classification as BinaryPixelImage.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 * 
 */
public final class ImageClassifier {

	/**
	 * This method define a Nearest Neighbour classification operation for clustering
	 * the pixel intensities of a discrete real image as represented by the {@link Cell}
	 * 'cell' and return the resultant image as a Cell.
	 * <p>
	 * The 1d float array 'seed' specify the reference intensity values per class for
	 * clustering. Each of the pixel intensity value of the image will be assign one
	 * of the reference 'seed' intensity value (class) with which it has minimum euclidian
	 * distance.
	 *   
	 * @param Cell
	 *            cell
	 * @param float[] seed
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell nearestNeighborClassification(Cell cell, float[] seed) 
	{
		Cell result = new Cell(cell.getRowCount(), cell.getColCount());

		float[] distance = new float[seed.length];

		for (int i = 0; i < result.getRowCount(); i++) 
		{
			for (int j = 0; j < result.getColCount(); j++) 
			{
				for (int k = 0; k < distance.length; k++)
				{
					distance[k] = Math.abs(cell.getElement(i, j) - seed[k]);
				}

				int index = f1(distance);
				result.setElement(seed[index], i, j);

			}
		}

		return result;

	}

	private int f1(float[] array) 
	{
		float min = Float.MAX_VALUE;
		int index = -1;

		for (int i = 0; i < array.length; i++) 
		{
			index = array[i] <= min ? i : index;
			min = array[i] <= min ? array[i] : min;
		}

		return index;
	}
	
	/**
	 * This method define the Fisher binary (2 class) classification operation applicable over the
	 * UInt PixelImage as represented by an {@link AbstractUIntPixelImage} 'img' and return 
	 * the classified image as a {@link BinaryPixelImage}.
	 *<p>
	 * The result of this image classification operation is return as a {@link BinaryPixelImage} which
	 * contains 'true' and 'false' for corresponding pixel position in the original image, representing
	 * either of the two class to which given pixel is assign.
	 * @param AbstractUIntPixelImage img
	 * @return BinaryPixelImage
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	
	public BinaryPixelImage fisherBinaryClassification(AbstractUIntPixelImage img) {
		
		
		float[] hist = new Histogram().histogram(img).accessVectorBuffer();

		int max = Integer.MIN_VALUE;
		int min = Integer.MAX_VALUE;

		for (int i = 0; i < img.getHeight(); i++) 
		{
			for (int j = 0; j < img.getWidth(); j++) 
			{
				int ele = img.getPixel(i, j);

				max = ele > max ? ele : max;
				min = ele < min ? ele : min;
			}
		}
		
		float[] cdf3 = new float[hist.length];
		float[] cdf2 = new float[hist.length];
		float[] cdf = new float[hist.length];
				
		cdf3[min] = hist[min]*min*min;
		cdf2[min] = hist[min]*min;
		cdf[min] = hist[min];
		
		for(int i= min + 1;i<=max;i++)
		{
			cdf3[i] = cdf3[i-1] + hist[i]*i*i;
			cdf2[i] = cdf2[i-1] + hist[i]*i;
			cdf[i] = cdf[i-1] + hist[i];
			
		}
		
		float[] mean1 = new float[hist.length];
		float[] mean2 = new float[hist.length];
		float[] std1 = new float[hist.length];
		float[] std2 = new float[hist.length];
		
		float[] fisher = new float[hist.length];
		
		int maxIndex = -1;
		float maxFisher = -Float.MAX_VALUE;
		
		for (int i = min; i <= max; i++) 
		{
			float upperCDF3 = cdf3[max] - cdf3[i];
			float upperCDF2 = cdf2[max] - cdf2[i];
			float upperCDF = cdf[max] - cdf[i];
			
			mean1[i] = cdf2[i]/cdf[i];
			
			if(i==max)
				mean2[i]=max;
			else
				mean2[i] = (upperCDF2)/(upperCDF);
			
			std1[i] = (cdf3[i] - 2*mean1[i]*cdf2[i] + mean1[i]*mean1[i]*cdf[i])/cdf[i];

			if(i==max)
				std2[i]=min;
			else
				std2[i]= (upperCDF3 - 2*mean2[i]*upperCDF2 + mean2[i]*mean2[i]*upperCDF)
				 / (upperCDF);
			
			float deltaMean = (mean1[i]-mean2[i]);
			
			fisher[i] = (deltaMean*deltaMean)/(std1[i]+std2[i]);
			
			if(fisher[i]>maxFisher)
			{
				maxIndex=i;
				maxFisher=fisher[i];
			}
				
		}

		if(maxIndex <0)
			throw new BugEncounterException();
		else
			return BinaryPixelImage.toBinaryPixelImage(img.toCell(), maxIndex);

	}
	
	
}
	
	
//		public BinaryPixelImage fisherClassifier(UIntPixelImageInterface img) {
//		
//		float[] hist = new IntensityTools().histogram(img).getVector();
//
//		float max = -Float.MAX_VALUE;
//		float min = Float.MAX_VALUE;
//
//		for (int i = 0; i < img.getHeight(); i++) {
//			for (int j = 0; j < img.getWidth(); j++) {
//				float ele = img.getPixel(i, j);
//
//				max = ele > max ? ele : max;
//				min = ele < min ? ele : min;
//			}
//		}
//
//		float[] mean1 = new float[hist.length];
//		float[] mean2 = new float[hist.length];
//
//		for (float i = min; i <= max; i++) {
//			float tmp = 0;
//			float sum = 1;
//
//			for (float j = 0; j <= i; j++) {
//				tmp = tmp + hist[(int) j] * j;
//				sum = sum + hist[(int) j];
//			}
//
//			tmp = tmp / sum;
//			mean1[(int) i] = tmp;
//
//			tmp = 0;
//			sum = 1;
//
//			for (float j = i + 1; j <= img.getMaxPixel(); j++) 
//			{
//				tmp = tmp + hist[(int) j] * j;
//				sum = sum + hist[(int) j];
//			}
//
//			if(i==max)
//				mean2[(int) i] = max;
//			else
//			{
//			tmp = tmp / sum;
//			mean2[(int) i] = tmp;
//			}
//
//		}
//
//		float[] std1 = new float[hist.length];
//		float[] std2 = new float[hist.length];
//
//		for (float i = min; i <= max; i++) {
//			float tmp = 0;
//			float sum = 1;
//
//			for (float j = 0; j <= i; j++) 
//			{
//				tmp = (float) (tmp + hist[(int) j]
//						* Math.pow(j - mean1[(int) i], 2));
//				sum = sum + hist[(int) j];
//			}
//
//			tmp = tmp / sum;
//			std1[(int) i] = tmp;
//
//			tmp = 0;
//			sum = 1;
//
//			for (float j = i + 1; j <= img.getMaxPixel(); j++) {
//				tmp = (float) (tmp + hist[(int) j]
//						* Math.pow(j - mean2[(int) i], 2));
//				sum = sum + hist[(int) j];
//			}
//
//			if(i==max)
//				std2[(int) i] = min;
//			else
//			{
//			tmp = tmp / sum;
//			std2[(int) i] = tmp;
//			}
//			//System.out.println(i+" "+std1[(int) i]+" "+std2[(int) i]);
//		}
//
//		float[] fisher = new float[hist.length];
//
//		for (float i = min; i <= max; i++) 
//		{
//			fisher[(int) i] = (mean1[(int) i] - mean2[(int) i])
//					* (mean1[(int) i] - mean2[(int) i]);
//			fisher[(int) i] = fisher[(int) i] / (std1[(int) i] + std2[(int) i]);
//			//System.out.println(i+" "+fisher[(int) i]);
//		}
//
//		float threshold = getMaximumIndex(fisher);
//		
//		AbstractPixelImage image;
//		
//		if(img.getDepth() == UInt8PixelImage.DEPTH)
//			image = (UInt8PixelImage) img;
//		else
//			image = (UInt16PixelImage) img;
//		
//		//System.out.println(max+" "+min+" "+threshold+" "+fisher[(int) threshold]);
//		return BinaryPixelImage.toBinaryPixelImage(image.toCell(), threshold);
//
//	}

