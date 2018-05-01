package org.JMathStudio.ImageToolkit.IntensityTools;

import org.JMathStudio.DataStructure.Vector.Vector;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.IllegalArgumentException;
import org.JMathStudio.MathToolkit.StatisticalTools.Histogram;
import org.JMathStudio.MathToolkit.Utilities.MathUtils;
import org.JMathStudio.PixelImageToolkit.BinaryPixelImage.BinaryPixelImage;
import org.JMathStudio.PixelImageToolkit.UIntPixelImage.AbstractUIntPixelImage;

/**
 * This class define various algorithms to estimate threshold value for binarization of a
 * gray scale image.
 * <p>A gray scale image will be represented by an {@link AbstractUIntPixelImage}.
 * <pre>
 * AbstractUIntPixelImage img = UInt8PixelImage.importImage("path");//Import external image
 * as UInt PixelImage type.
 * 
 * ImageBinarization ib = new ImageBinarization();//Create an instance of ImageBinarization.
 * 
 * BinaryPixelImage binary1 = ib.maximumEntropy(img);//Convert input image to binary image
 * using maximum entropy algorithm.
 * 
 * BinaryPixelImage binary2 = ib.otsu(img);//Convert input image to binary image using Otsu's
 * algorithm.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class ImageBinarization {

	private Histogram histogram;

	/**
	 * Default constructor of the class.
	 */
	public ImageBinarization(){
		histogram = new Histogram();
	}

	/**
	 * This method estimate the threshold value for binarization of the gray scale image 'img'
	 * based upon the Otsu algorithm and return the same.
	 * <p>The return threshold value can be used to threshold the image 'img', such that pixels
	 * with value above the threshold represent the foreground with rest of the pixels being the
	 * background.
	 * @param AbstractUIntPixelImage img
	 * @return int
	 * @see BinaryPixelImage#toBinaryPixelImage(AbstractUIntPixelImage, int)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public int otsu(AbstractUIntPixelImage img){

		//Compute histogram for the given UInt PixelImage.
		Vector hist = histogram.histogram(img);
		int height = img.getHeight();
		int width = img.getWidth();

		//This many pixels in the image.
		int totalPixels = height*width;
		//Sum of all the pixel intensities in the image.
		double totalSum=0;

		for(int i=0;i<height;i++)
		{
			for(int j=0;j<width;j++)
			{
				totalSum+=img.getPixel(i, j);
			}
		}
		//This many different intensity levels in the image.
		int intensityLevel = hist.length();

		//Compute statistics for iteration.
		float sum=0;
		float count=0;
		float profile;

		//Using Otsu formula measure inter-class variance for each intensity level 'a'.
		//For each intensity level 'a' measure statistics for two class:
		//	Class 1: All pixels with intensities equal to or less than level 'a'.
		//	Class 2: All pixels with intensities more than level 'a'.
		float variance=-1;//inter-class variance for each intensity level.
		float lastVariance=-1;
		float meanDiff;

		int threshold=-1;//Threshold with maximum inter-class variance.

		//'p1' and 'p2' are the probability of occurrence of pixels within one of the two
		//class with class mean 'u1' and 'u2' respectively.
		float p1=0,p2,u1,u2;

		float imageMean= (float) (totalSum/totalPixels);

		for(int a=0;a<intensityLevel;a++){
			//Count of pixels with current intensity 'a'.
			profile = hist.getElement(a);
			//Cumulative count of all pixels with intensity less than or equal to
			//current intensity 'a'.
			count+=profile;
			//Cumulative sum of all pixels intensities with intensity less than or equal
			//to current intensity 'a'.
			sum+=a*profile;

			if(count == 0){
				u1=0;
				u2=imageMean;
				meanDiff = u2;
			}
			else{
				//Cumulative mean is cumulative sum/cumulative count.
				u1 = sum/count;
				u2 = (float) ((totalSum - sum)/(totalPixels - count));
				meanDiff = u2-u1;
			}
			//Cumulative probability for pixels below or equal to level 'a' is total number of 
			//pixels with intensity less than or equal to that intensity 'a' divided by total 
			//number of pixels.
			//This is also probability for class 1.
			p1 = count/totalPixels;
			
			//Probability for other class 2 would be 1 - p1.
			//Cumulative probability of pixels above level 'a'.
			p2 = 1 - p1;

			if(p1 > 1)
				throw new BugEncounterException();
			
			//Calculate inter-class variace for current level 'a'.
			variance = (p1*p2)*(meanDiff*meanDiff);
			if(variance > lastVariance){
				threshold = a;
			}

			lastVariance = variance;
		}

		return threshold;
	}

	/**
	 * This method estimate the threshold value for binarization of the gray scale image 'img'
	 * based upon the Maximum entropy algorithm and return the same.
	 * <p>The return threshold value can be used to threshold the image 'img', such that pixels
	 * with value above the threshold represent the foreground with rest of the pixels being the
	 * background.
	 * @param AbstractUIntPixelImage img
	 * @return int
	 * @see BinaryPixelImage#toBinaryPixelImage(AbstractUIntPixelImage, int)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public int maximumEntropy(AbstractUIntPixelImage img){
		try{
			//Compute histogram for the given UInt PixelImage.
			Vector hist = histogram.histogram(img);

			int height = img.getHeight();
			int width = img.getWidth();

			//This many pixels in the image.
			int totalPixels = height*width;
			//This many different intensity levels in the image.
			int intensityLevel = hist.length();

			float profile;
			//Probability of occurrence of pixels with a given intensity
			//within the image.
			float p;
			//Cumulative Entropy at every intensity level in the image.
			//Sum of entropy for all the pixels with intensity less than or equal
			//to that level.
			float[] CS = new float[intensityLevel];
			float S=0;

			//Total entropy of the image.
			float totalEntropy=0;
			//Log base 2 to calculate entropy.
			short base = 2;

			for(int a=0;a<intensityLevel;a++){
				//Count of pixels with current intensity 'a'.
				profile = hist.getElement(a);

				//Compute probability for this level of intensity.
				p = profile/totalPixels;
				if(p > 1)
					throw new BugEncounterException();
				//Calculate entropy for this level of intensity.
				//S = -p*log2(p)
				if(p > 0)
					S = (float) (-p*MathUtils.log(p, base));
				else
					S = 0;
				//Add to total entropy.
				totalEntropy+=S;
				//Total entropy till this level is cumulative entropy for this level.
				CS[a] = totalEntropy;
			}

			int threshold=-1;
			float S1;
			float entropyDiff;

			//Apply Maximum entropy segmentation on the image. Select a threshold 'T' which divides
			//image pixels in 2 class.
			//Class 1: Pixels with intensity less than or equal to 'T'.
			//Class 2: Pixels with intensity more than 'T'.

			//Estimate Total Entropy S1 & S2 for this 2 class. Select threshold 'T' at which S2-S1
			//change sign.
			//S2-S1 will start from -max to 0 to +max.
			
			for(int a=0;a<intensityLevel;a++){
				S1 = CS[a];
				//Let S2 and S1 be the entropy for 2 class.
				//S2+S1 = S (Total entropy).
				//We are interested in difference of entropy between two class.
				//So, S2-S1 = S - 2*S1.
				//Now as entropy 'S' is measured in negative -p*log2(p), entropy diff
				//has to be negative i.e 2*S1 - S.
				entropyDiff = 2*S1-totalEntropy;
				
				//Entropy difference S2-S1 changes sign from negative to positive.
				if(entropyDiff > 0){
					threshold = a;
					break;
				}
			}
			return threshold;
		}catch(IllegalArgumentException e){
			throw new BugEncounterException();
		}
	}

	/**
	 * This method estimate the threshold value for binarization of the gray scale image 'img'
	 * based upon the Iso-data algorithm and return the same.
	 * <p>The return threshold value can be used to threshold the image 'img', such that pixels
	 * with value above the threshold represent the foreground with rest of the pixels being the
	 * background.
	 * @param AbstractUIntPixelImage img
	 * @return int
	 * @see BinaryPixelImage#toBinaryPixelImage(AbstractUIntPixelImage, int)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public int isoData(AbstractUIntPixelImage img){
		// Histogram of the given UInt PixelImage.
		Vector hist = histogram.histogram(img);

		int sum = 0;
		int count = 0;

		for (int i = 0; i < hist.length(); i++) {
			// Compute the sum of all the pixel intensities of the image
			// from histogram.
			sum += hist.getElement(i) * i;
			// Compute the number of pixels within the image from its
			// histogram.
			count += hist.getElement(i);
		}

		// Initial seed threshold is the mean of the image, ie sum/count.
		int Threshold = Math.round((float) (sum) / count);

		// MAT is the mean of the pixel intensities within the image with
		// intensity above the given threshold intensity.
		int MAT = 0; // Mean above threshold.
		// N_MAT is the count of the pixel intensities with in the image
		// with
		// intensity above the given threshold intensity.
		int N_MAT = 0;

		// MBT is the mean of the pixel intensities within the image with
		// intensity below or equal to the given threshold intensity.
		int MBT = 0;// Mean below threshold.
		// N_MBT is the count of the pixel intensities with in the image
		// with
		// intensity below or equal to the given threshold intensity.
		int N_MBT = 0;

		// Check condition when iteration is to be stopped. This happens
		// when the
		// threshold does not change any more, ie new threshold == old
		// threshold.
		boolean stop = false;
		// Threshold computed at the end of each iteration.
		int newThreshold;

		do {
			// Initialize all with '0' for each new iteration.
			MAT = 0;
			N_MAT = 0;

			MBT = 0;
			N_MBT = 0;

			for (int i = 0; i <= Threshold; i++) {
				// Compute the sum of all the pixel intensities within the
				// image with
				// intensity below or equal to the given threshold from the
				// image histogram.
				MBT += hist.getElement(i) * i;
				// Compute the count of all the pixel intensities within the
				// image with
				// intensity below or equal to the given threshold from the
				// image histogram.
				N_MBT += hist.getElement(i);
			}

			// MAT will be the difference from the total sum of all the
			// pixel intensities
			// within the image and the sum obtained for MBT.
			MAT = sum - MBT;
			// Same applies for N_MAT.
			N_MAT = count - N_MBT;

			// If the image happen to be constant image with all similar
			// pixel intensities
			// count for MBT or MAT may come out to be '0'. In such case
			// respective mean
			// should be set to '0'.
			if (N_MBT == 0) {
				MBT = 0;
			} else {
				// Compute MBT from sum and count for pixel intensities
				// below or equal to
				// the given threshold.
				MBT = MBT / N_MBT;
			}
			// Same as defined for MBT applies for MAT.
			if (N_MAT == 0) {
				MAT = 0;
			} else {
				MAT = MAT / N_MAT;
			}

			// New threshold is the mean of MAT and MBT.
			newThreshold = Math.round((MAT + MBT) / 2.0f);
			// If the threshold does not change from last iteration, we have
			// the solution.
			// Stop iteration.
			stop = (newThreshold == Threshold) ? true : false;

			Threshold = newThreshold;

		} while (!stop);

		// Return computed Threshold value to binarize the image.
		return Threshold;
	}	
	
	/**
	 * This method estimate the threshold value for binarization of the gray scale image 'img'
	 * which is the mean value of the pixels and return the same.
	 * <p>The return threshold value can be used to threshold the image 'img', such that pixels
	 * with value above the threshold represent the foreground with rest of the pixels being the
	 * background.
	 * @param AbstractUIntPixelImage img
	 * @return int
	 * @see BinaryPixelImage#toBinaryPixelImage(AbstractUIntPixelImage, int)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public int mean(AbstractUIntPixelImage img){
		
		int h = img.getHeight();
		int w = img.getWidth();
		
		double sum = 0;
		for(int i=0;i<h;i++)
		{
			for(int j=0;j<w;j++)
			{
				sum+=img.getPixel(i, j);
			}
		}
		
		return (int) Math.round(sum/(h*w));
	}
}
