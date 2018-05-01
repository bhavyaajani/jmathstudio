package org.JMathStudio.ImageToolkit.IntensityTools;

import org.JMathStudio.DataStructure.Vector.Vector;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.IllegalArgumentException;
import org.JMathStudio.MathToolkit.StatisticalTools.Histogram;
import org.JMathStudio.MathToolkit.StatisticalTools.VectorStatistics.VectorStatistics;
import org.JMathStudio.PixelImageToolkit.UIntPixelImage.AbstractUIntPixelImage;

/**
 * This class define various Gray level transforms to manipulate the gray scale pixel 
 * intensities of various UInt PixelImages.
 * <p>
 * UInt Image with different depth or gray scale will be represented by an
 * {@link AbstractUIntPixelImage}.
 * <pre>Usage:
 * AbstractUIntPixelImage img = UInt8PixelImage.importImage("path");//Import external image
 * as UInt PixelImage type.
 * 
 * GrayLevelTransform glt = new GrayLevelTransform();//Create an instance of GrayLevelTransform.
 * 
 * AbstractUIntPixelImage result1 = glt.histEqualization(img);//Apply histogram equalization
 * operation on input image.
 * 
 * AbstractUIntPixelImage result2 = glt.autoContrast(img);//Apply auto-contrast operation on
 * input image.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 * 
 */
public final class GrayLevelTransform {

	/**
	 * This method apply the Histogram matching operation on the target UInt
	 * PixelImage as represented by an {@link AbstractUIntPixelImage} 'target' with
	 * reference UInt PixelImage as given by an {@link AbstractUIntPixelImage}
	 * 'reference' and return the result as an {@link AbstractUIntPixelImage}.
	 * 
	 * <p>Histogram matching operation try to match the histogram of a target image
	 * with that of the reference image. It computes the histogram matching function
	 * 'M' based upon the cumulative histogram of the target and reference images. 
	 * Once histogram matching function 'M' is defined, it applies the same on the
	 * pixel intensities of the target image. 
	 * 
	 * <p>The {@link AbstractUIntPixelImage} 'target' and 'reference' should be of same
	 * depth else this method will throw an IllegalArgument Exception. 
	 * <p>Both target and reference images need not be of same dimensions. This operation
	 * normalizes the difference in dimensions i.e. pixel counts while performing
	 * histogram matching.
	 * 
	 * @param AbstractUIntPixelImage target
	 * @param AbstractUIntPixelImage reference
	 * @return AbstractUIntPixelImage
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public AbstractUIntPixelImage histoMatching(AbstractUIntPixelImage target, AbstractUIntPixelImage reference)
		throws IllegalArgumentException{
		
		if(target.getDepth() != reference.getDepth())
			throw new IllegalArgumentException();
		else
		{
			AbstractUIntPixelImage result = (AbstractUIntPixelImage) target.getEquivalentBlankImage();
			int levels = target.getMaxValidPixel()+1;
			
			Histogram histo = new Histogram();
			
			Vector targetCHist = histo.cumulativeHistogram(target);
			Vector refCHist = histo.cumulativeHistogram(reference);
			
			int[] map = new int[levels];
			float L1,L2;
			int lastLevel=0;
			
			//As target and reference image size can be different, we need to factor in the difference in the
			//number of pixels in both the image.
			//Normalise cumulative histogram of reference image with ratio of total pixels in both the image.
			float factor = (float)(target.getHeight()*target.getWidth())/(float)(reference.getHeight()*reference.getWidth());
			
			for(int l=0;l<levels;l++){
				refCHist.setElement(refCHist.getElement(l)*factor, l);
			}
			
			for(int i=0;i<levels;i++){
				L1 = targetCHist.getElement(i);
				map[i]=lastLevel;
				for(int k=lastLevel;k<levels;k++){
					L2 = refCHist.getElement(k);
					
					if(L1 > L2){
						map[i] = k+1;
						
					}
					else{
						lastLevel = k;
						break;
					}
				}
			}
			int h = result.getHeight();
			int w = result.getWidth();
			
			for(int i=0;i<h;i++){
				for(int j=0;j<w;j++){
					result.setPixel(map[target.getPixel(i, j)], i, j);
				}
			}
			
			return result;
		}
	}
	/**
	 * This method apply the Histogram equalisation operation on the UInt
	 * PixelImage as represented by an {@link AbstractUIntPixelImage} 'img' and
	 * return the resultant UInt PixelImage as an {@link AbstractUIntPixelImage}
	 * .
	 * 
	 * @param AbstractUIntPixelImage
	 *            img
	 * @return AbstractUIntPixelImage
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public AbstractUIntPixelImage histEqualization(AbstractUIntPixelImage img) {

		try {
			int Max = img.getMaxValidPixel();

			float[] hist = new float[Max + 1];

			for (int i = 0; i < img.getHeight(); i++) {
				for (int j = 0; j < img.getWidth(); j++) {
					hist[img.getPixel(i, j)]++;
				}
			}

			Vector cdf = new VectorStatistics().cdf(new Vector(hist));

			AbstractUIntPixelImage result = (AbstractUIntPixelImage) img
					.getEquivalentBlankImage();

			float minCdf = 0;

			for (int i = 0; i < cdf.length(); i++) {
				if (cdf.getElement(i) > 0) {
					minCdf = cdf.getElement(i);
					break;
				}
			}

			float area = img.getHeight() * img.getWidth();
			float norm = Max/(area - minCdf);
			
			for (int i = 0; i < result.getHeight(); i++) 
			{
				for (int j = 0; j < result.getWidth(); j++) 
				{
					float tmp = (cdf.getElement(img.getPixel(i, j)) - minCdf);
							
					result.setPixel((int) Math.floor(tmp * norm), i, j);
				}
			}

			return result;
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new BugEncounterException();
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}

	}

	/**
	 * This method will perform an Auto contrast operation on the UInt
	 * PixelImage as represented by an {@link AbstractUIntPixelImage} 'img' and
	 * return the enhanced contrast UInt PixelImage as an
	 * {@link AbstractUIntPixelImage}.
	 * <p>
	 * Auto contrast operation linearly maps the pixel intensities of the given
	 * UInt PixelImage to the full intensity range for that UInt PixelImage.
	 * Thus minimum and maximum pixel intensity of the given UInt PixelImage
	 * will be mapped to lowest and highest valid pixel intensity respectively for
	 * the given UInt PixelImage.
	 * <p>
	 * The contrast enhanced UInt PixelImage will be return as an
	 * {@link AbstractUIntPixelImage}.
	 * 
	 * @param AbstractUIntPixelImage
	 *            img
	 * @return AbstractUIntPixelImage
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */

	public AbstractUIntPixelImage autoContrast(AbstractUIntPixelImage img) {
		int min = Integer.MAX_VALUE;
		int max = Integer.MIN_VALUE;

		for (int i = 0; i < img.getHeight(); i++) 
		{
			for (int j = 0; j < img.getWidth(); j++) 
			{
				int pix = img.getPixel(i, j);
				
				if(pix<min)
					min = pix;
				if(pix>max)
					max = pix;
			}
		}
		AbstractUIntPixelImage result = (AbstractUIntPixelImage) img
				.getEquivalentBlankImage();

		float slope = ((float) img.getMaxValidPixel()) / (float) (max - min);

		try {
			for (int i = 0; i < result.getHeight(); i++) {
				for (int j = 0; j < result.getWidth(); j++) {
					int tmp = (int) (slope * (img.getPixel(i, j) - min));
					result.setPixel(tmp, i, j);
				}
			}
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}

		return result;

	}

	/**
	 * This method will apply Histogram stretching operation on the UInt PixelImage as represented
	 * by {@link AbstractUIntPixelImage} 'img' with lower and upper intensity threshold as given
	 * by the arguments 'lt' & 'ut' respectively and return the resultant image as {@link AbstractUIntPixelImage}.
	 * <p>The lower threshold 'lt' should be less than upper threshold 'ut' and both 'lt' & 'ut'
	 * should be within the valid intensity range of the {@link AbstractUIntPixelImage} 'img' else
	 * this method will throw an IllegalArgument Exception.
	 * @param AbstractUIntPixelImage img
	 * @param int lt
	 * @param int ut
	 * @return AbstractUIntPixelImage
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public AbstractUIntPixelImage histoStretching(AbstractUIntPixelImage img, int lt, int ut) throws IllegalArgumentException
	{
		int maxPix = img.getMaxValidPixel();
		int minPix = img.getMinValidPixel();
		boolean check = img.validatePixel(lt) && img.validatePixel(ut);
		
		if(lt >= ut || !check){
			throw new IllegalArgumentException();
		}
		else
		{
			int h = img.getHeight();
			int w = img.getWidth();
			
			AbstractUIntPixelImage res = (AbstractUIntPixelImage) img.getEquivalentBlankImage();
			
			float scale = (maxPix-minPix)/((float)(ut-lt));
			int in;
			int op;
			
			for(int i=0;i<h;i++)
			{
				for(int j=0;j<w;j++)
				{
					in = img.getPixel(i, j);
					op = Math.round((in - lt)*scale);//+ minPix -> 0
					
					if(op>maxPix)
						op = maxPix;
					else if(op < minPix)
						op = minPix;
					
					res.setPixel(op, i, j);					
				}
			}
			
			return res;
		}
	}
	
	/**
	 * This method will apply a Solarisation operation on the UInt PixelImage as represented by {@link AbstractUIntPixelImage} 
	 * 'img', with intensity threshold as given by the argument 't' and return the resultant image as 
	 * {@link AbstractUIntPixelImage}.
	 * <p>The solarisation is achieved by inverting a subset of pixel intensities within the image. If the argument
	 * 'foward' is set true, the image pixel intensities falling within the range [t MAX-PIXEL] will be inverted.
	 * Else if the argument 'forward' is set false, the image pixel intensities falling within the range [MIN-PIXEL t] 
	 * will be inverted. 
	 * <p>The threshold 't' should be within the valid intensity range of the {@link AbstractUIntPixelImage} 'img' else
	 * this method will throw an IllegalArgument Exception.
	 *  
	 * @param AbstractUIntPixelImage img
	 * @param int t
	 * @param boolean forward
	 * @return AbstractUIntPixelImage
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public AbstractUIntPixelImage solarisation(AbstractUIntPixelImage img, int t, boolean forward) throws IllegalArgumentException
	{
		if(!img.validatePixel(t))
			throw new IllegalArgumentException();
		
		final int maxPix = img.getMaxValidPixel();
		final int h = img.getHeight();
		final int w = img.getWidth();
		
		AbstractUIntPixelImage result = (AbstractUIntPixelImage) img.getEquivalentBlankImage();
		int pixel;
		
		if(forward){
			
			for(int i=0;i<h;i++)
			{
				for(int j=0;j<w;j++)
				{
					pixel = img.getPixel(i, j);
					if(pixel >= t)
						pixel = maxPix - pixel;
					result.setPixel(pixel, i, j);
				}
			}
		}
		else{
			for(int i=0;i<h;i++)
			{
				for(int j=0;j<w;j++)
				{
					pixel = img.getPixel(i, j);
					if(pixel <= t)
						pixel = maxPix - pixel;
					result.setPixel(pixel, i, j);
				}
			}
		}
		
		return result;
	}
	
	// /**
	// * This method will perform an Auto Contrast operation on the
	// UInt16PixelImage
	// * 'img' and return the enhanced contrast UInt16PixelImage.
	// * <p>Auto Contrast operation linearly maps the pixel intensities of the
	// given
	// * UInt16PixelImage to the full range of the UInt16 PixelImage i.e. [0
	// 65535].
	// * Thus minimum and maximum pixel intensity of the given UInt16 PixelImage
	// will be linearly mapped to
	// * '0' and '65535' respectively in the return UInt16PixelImage there by
	// enhancing
	// * the contrast in it.
	// * @param UInt16PixelImage img
	// * @return UInt16PixelImage
	// * @see UInt16PixelImage
	// * @author Ajani Bhavya - (jmathstudio@gmail.com)
	// */
	// public UInt16PixelImage autoContrast(UInt16PixelImage img)
	// {
	// int min = Integer.MAX_VALUE;
	// int max = Integer.MIN_VALUE;
	//		
	// for(int i=0;i<img.getHeight();i++)
	// {
	// for(int j=0;j<img.getWidth();j++)
	// {
	// int pix = img.getPixel(i,j);
	//				
	// min = pix < min ? pix:min;
	// max = pix > max ? pix:max;
	// }
	// }
	//		
	// UInt16PixelImage res = new
	// UInt16PixelImage(img.getHeight(),img.getWidth());
	//		
	// float _16Max = UInt16PixelImage.MAX;
	// float slope = _16Max/(max - min);
	//		
	// try{
	// for(int i=0;i<res.getHeight();i++)
	// {
	// for(int j=0;j<res.getWidth();j++)
	// {
	// int tmp = (int) (slope*(img.getPixel(i,j)-min));
	// res.setPixel(tmp, i,j);
	// }
	// }
	// }catch(IllegalArgumentException e)
	// {
	// e.printStackTrace();
	// throw new RuntimeException();
	// }
	//		
	// return res;
	//			
	// }

	// /**
	// * This method will apply a threshold operation on the Pixel Intensities
	// of the
	// * UInt16PixelImage and return the result as an UInt16PixelImage.
	// * <p>
	// * The argument 'threshold' specify the threshold value for thresholding
	// the pixel
	// * intensities of the UInt16PixelImage 'img'. The value of argument
	// 'threshold' should
	// * be in the UInt16PixelImage intensity range i.e. [0 65535] else this
	// method will throw
	// * an IllegalArgument Exception.
	// * <p>This operation will make all those Pixel Intensities of the
	// UInt16PixelImage which
	// * is equal to or less than the argument 'threshold' zero.
	// * <p>The resultant thresholded Image will be return as an
	// UInt16PixelImage.
	// * @param UInt16PixelImage img
	// * @param int threshold
	// * @return UInt16PixelImage
	// * @throws IllegalArgumentException
	// * @see UInt16PixelImage
	// * @author Ajani Bhavya - (jmathstudio@gmail.com)
	// */
	//
	// public UIntPixelImage threshold(UIntPixelImage img,int threshold) throws
	// IllegalArgumentException
	// {
	// if(threshold <img.getMinPixel() | threshold > img.getMaxPixel())
	// throw new IllegalArgumentException();
	//		
	// UInt16PixelImage result = new
	// UInt16PixelImage(img.getHeight(),img.getWidth());
	//				
	// try
	// {
	// for(int i=0;i<result.getHeight();i++)
	// {
	// for(int j=0;j<result.getWidth();j++)
	// {
	// if(img.getPixel(i,j) <= threshold)
	// result.setPixel(0,i,j);
	//				
	// else
	// result.setPixel(img.getPixel(i,j),i,j);
	// }
	// }
	// }
	// catch(IllegalArgumentException e)
	// {
	// e.printStackTrace();
	// throw new RuntimeException();
	// }
	//		
	// return result;
	// }

	// /**
	// * This method will return the Negative of the UInt16PixelImage 'img' as
	// an
	// * UInt16PixelImage.
	// * <p>
	// * Negative UInt16PixelImage will have flip pixel intensities of the
	// original UInt16PixelImage
	// * such that the highest pixel intensity of 65535 is mapped to 0 and vice
	// versa.
	// * <p>This operation define on UInt16PixelImage with pixel intensity range
	// of [0 65535] is,
	// * Y = 65535 - I.
	// * where, Y is the new pixel intensity.
	// * I is the original pixel intensity.
	// * <p>
	// * This operation interchanges the bright and dark areas of the
	// UInt16PixelImage.
	// *
	// * @param UInt16PixelImage
	// * img
	// * @return {@link UInt16PixelImage}
	// * @see UInt16PixelImage
	// * @author Ajani Bhavya - (jmathstudio@gmail.com)
	// */
	// public UInt16PixelImage negative(UInt16PixelImage img) {
	//		
	// UInt16PixelImage res = new
	// UInt16PixelImage(img.getHeight(),img.getWidth());
	// int _16Max = UInt16PixelImage.MAX;
	//		
	// try
	// {
	// for (int i = 0; i < res.getHeight(); i++)
	// {
	// for (int j = 0; j < res.getWidth(); j++)
	// {
	// res.setPixel(_16Max - img.getPixel(i,j),i,j);
	// }
	// }
	// }catch(IllegalArgumentException e)
	// {
	// e.printStackTrace();
	// throw new RuntimeException();
	// }
	//
	// return res;
	// }

	// /**
	// * This method apply the Histogram Equalisation operation on the
	// UInt8PixelImage
	// * 'img' and return the resultant Image as UInt8PixelImage.
	// *
	// * @param UInt8PixelImage img
	// * @return UInt8PixelImage
	// * @see UInt8PixelImage
	// * @author Ajani Bhavya - (jmathstudio@gmail.com)
	// */
	// public UInt8PixelImage histEqualization(UInt8PixelImage img){
	//		
	// float[] hist = new float[256];
	//
	// for (int i = 0; i < img.getHeight(); i++) {
	// for (int j = 0; j < img.getWidth(); j++) {
	// hist[img.getPixel(i, j)]++;
	// }
	// }
	//
	// Vector cdf = new VectorStatistics().cdf(new Vector(hist));
	//
	// UInt8PixelImage result = new UInt8PixelImage(img.getHeight(),
	// img.getWidth());
	//
	// float minCdf = 0;
	//
	// for (int i = 0; i < cdf.length(); i++) {
	// if (cdf.getElement(i) > 0) {
	// minCdf = cdf.getElement(i);
	// break;
	// }
	// }
	//
	// float area = img.getHeight() * img.getWidth();
	//
	// try
	// {
	// for (int i = 0; i < result.getHeight(); i++) {
	// for (int j = 0; j < result.getWidth(); j++) {
	// float tmp = (cdf.getElement(img.getPixel(i, j)) - minCdf)
	// / (float) (area - minCdf);
	//
	// result.setPixel((short) Math.floor(tmp * 255), i, j);
	// }
	// }
	// }
	// catch(IllegalArgumentException e)
	// {
	// e.printStackTrace();
	// throw new RuntimeException();
	// }
	//
	// return result;
	//
	// }

	// /**
	// * This method will compute the Histogram of the Pixel Intensities of the
	// * UInt16PixelImage 'img' and return the same as a Vector.
	// * <p>
	// * As the range of pixel intensities for UInt16PixelImage is [0 65535] i.e
	// it has
	// * 65536 different intensity values, the return Vector will be of length
	// 65536 with
	// * each index element of the vector representing the count for that pixel
	// intensity
	// * value respectively.
	// *
	// * @param UInt16PixelImage img
	// * @return Vector
	// * @see UInt16PixelImage
	// * @see Vector
	// * @author Ajani Bhavya - (jmathstudio@gmail.com)
	// */
	// public Vector histogram(UInt16PixelImage img){
	//		
	// float[] hist = new float[UInt16PixelImage.MAX+1];
	//
	// for (int i = 0; i < img.getHeight(); i++) {
	// for (int j = 0; j < img.getWidth(); j++) {
	// hist[img.getPixel(i, j)]++;
	// }
	// }
	//
	// return new Vector(hist);
	// }

}
