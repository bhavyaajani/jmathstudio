package org.JMathStudio.ImageToolkit.FilterTools.AdvancedFilters;

import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.IllegalArgumentException;
import org.JMathStudio.PixelImageToolkit.UIntPixelImage.AbstractUIntPixelImage;

/**
 * This class define a Bilateral filter. A bilateral filter is a non linear edge preserving smoothing
 * filter where each pixel in an image is replaced by the weighted average of its neighbourhood pixels.
 * <p>However the weight assign to the neighbouring pixels depends both upon the intensity difference
 * and the euclidian distance of that pixel from the centre pixel.
 * <p>Thus a bilateral filter essentially filters an image both in the spatial and intensity range domain.
 * <p>A closeness function assign weight as per the nearness in space ie. spatial euclidian distance while
 * a similarity function assign the weight based on the similarity in the pixel intensity ie intensity
 * range distance. The overall weight is than the product of this two weights.
 * <p>This class uses a gaussian function as a closeness and similarity function.  
 * <pre>Usage:
 * AbstractUIntPixelImage img = UInt8PixelImage.importImage("path");//Import input image
 * as UIntPixelImage type.
 * 
 * float sigmaC = 2;//Parameters of required Bilateral filter.
 * float sigmaS = 10;
 * 
 * BilateralFilter bf = new BilateralFilter(sigmaC,sigmaS);//Create an instance of BilateralFilter with
 * given parameters.
 * 
 * AbstractUIntPixelImage result = bf.filter(img);//Apply Bilateral filtering on the input image.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class BilateralFilter {

	private float sigmaC;
	private float sigmaS;

	private float[][] i7;
	private float[] i2;
	
	private int lastDepth=0;

	//private float twoSigmaRSquared;

	/**
	 * This will create a bilateral filter with a gaussian closeness and similarity function.
	 * <p>The argument 'sigmaC' specify the standard deviation of the closeness function which
	 * determine the weights based upon the nearness in space or euclidian distance.
	 * <p>The argument 'sigmaS' specify the standard deviation of the similarity function which
	 * determine the weights as per the similarity in pixel intensities or intensity range.
	 * <p><i>Note: The ideal value for the 'sigmaC' and 'sigmaS' would be less than 5 for acceptable
	 * computational performance.</i>
	 * @param float sigmaC
	 * @param float sigmaS
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public BilateralFilter(float sigmaC, float sigmaS) 
	{
		this.sigmaC = sigmaC;
		this.sigmaS = sigmaS;

		//Precompute the gausian closeness filter kernel for given standard
		//deviation.
		this.f3();


	}

	/**
	 * This method will apply the given bilateral filter on the {@link AbstractUIntPixelImage} 'img' 
	 * and retun the resultant filtered image as an {@link AbstractUIntPixelImage}.
	 * @param AbstractUIntPixelImage img
	 * @return AbstractUIntPixelImage
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public AbstractUIntPixelImage filter(AbstractUIntPixelImage img) 
	{
		try
		{
			//Depth means number of integer pixel ranges.
			int depth = img.getMaxValidPixel()+1;

			if(this.lastDepth != depth)
			{
				//If the input image is of different depth from last image
				//recompue the similarity matrix for the new depth.
				this.f1(depth);
				this.lastDepth = depth;
			}

			int height = img.getHeight();
			int width = img.getWidth();
	
			AbstractUIntPixelImage result = (AbstractUIntPixelImage) img.getEquivalentBlankImage();
			
			int DKernelSize = this.i7.length;
     		int index = (DKernelSize -1)/2;

     		//loop throught all pixel locations of the image.
			for(int i=0;i<height;i++)
			{
				for(int j=0;j<width;j++)
				{
					float pix = img.getPixel(i,j);
					float res =0;
					float totalWeight =0;
					
					//For each pixel, compute the weight for all neihbourhood pixels
					//using the gaussian closeness function.
					//The weights are multiplied by the corresponding weightage as derived
					//through the similarity function.
					for(int k=-index;k<=index;k++)
					{
						for(int l=-index;l<=index;l++)
						{
							int yBound = k+i;
							int xBound = l+j;
							if(yBound > 0 && yBound <height && xBound >0 && xBound <width)
							{
								float curPix = img.getPixel(yBound, xBound);
								//weight = weight due to proximity in space * weight due to similarity in intensity.
								float weight = i7[k+index][l+index]*i2[(int) Math.abs(curPix- pix)];
							
								totalWeight+=weight;
								res+= curPix*weight;
							}
						}
					}
					
					//Normalise result with total weight. This ensure that all the final weights
					//are in the range of [0 1] and no DC is added.
					
					result.setPixel((int)Math.floor(res/totalWeight),i,j);

				}
			}

			return result;
		}
		catch(ArrayIndexOutOfBoundsException e)
		{
			throw new BugEncounterException();
		}
		catch(IllegalArgumentException e)
		{
			throw new BugEncounterException();
		}
		
	}

	/**
	 * This will set the standard deviation of the gaussian similarity function for this
	 * bilateral filter as specified by the argument 'sigmaS'.
	 * @param float sigmaS
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void setSimilarityFunctionSigma(float sigmaS)
	{
		this.sigmaS = sigmaS;
	}
	
	/**
	 * This will set the standard deviation of the gaussian closeness function for this
	 * bilateral filter as specified by the argument 'sigmaC'.
	 * @param float sigmaC
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void setClosenessFunctionSigma(float sigmaC)
	{
		this.sigmaC = sigmaC;
		this.f3();
	}
	
	/**
	 * This method will return the standard deviation of the gaussian similarity function
	 * associated with this bilateral function.
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float getSimilarityFunctionSigma()
	{
		return this.sigmaS;
	}
	
	/**
	 * This method will return the standard deviation of the gaussian closeness function
	 * associated with this bilateral function.
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float getClosenessFunctionSigma()
	{
		return this.sigmaC;
	}
	
	private void f1(int depth)
	{
		//Precompute the similarity weight to be assighn for a given intensity
		//difference for performance reason.
		
		//Possible intensity difference value shall be equal to image depth.
		//ie for UInt 8 image, possible intensity difference ranges from, 0-0 to 255-0
		//that is 0 to 255.
		this.i2= new float[depth];
		
		double twoSigmaRSquared = 2 * this.sigmaS * this.sigmaS;

		for (int i = 0; i < depth; i++) 
		{
			//similarity gaussian function for weight determination based on intensity
			//difference or similarity.
			this.i2[i] = (float) Math.exp(-((i) / twoSigmaRSquared));
		}
	}

	private void f3()
	{
		//Originally as stated in algorithm
		
		//double sigmaMax = Math.max(sigmaC, sigmaS);
		//float kernelRadius = (int)Math.ceil(2 * sigmaMax);
			
		//Mine modified.
		float kernelRadius = (int)Math.ceil(2 * sigmaC);

		// this will always be an odd number, i.e. {1,3,5,7,9,...}
		int kernelSize = (int) (kernelRadius * 2 + 1);
		int center = (kernelSize - 1) / 2;

		this.i7 = new float[kernelSize][kernelSize];
		double twoSigmaDSqr = 2*sigmaC*sigmaC;

		for (int y = 0; y < kernelSize; y++) 
		{
			for (int x = 0; x < kernelSize; x++) 
			{
				float xc = x-center;
				float yc = y-center;
				
				//Closeness gaussian function.
				i7[y][x] = this.f5(twoSigmaDSqr,yc,xc);
			}
		}

	}

	private float f5(double sigma,float y, float x) 
	{
		return (float) Math.exp(-((y * y + x * x) / (sigma)));
	}
}