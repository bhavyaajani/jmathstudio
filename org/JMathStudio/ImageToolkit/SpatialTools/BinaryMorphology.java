package org.JMathStudio.ImageToolkit.SpatialTools;

import org.JMathStudio.DataStructure.Structure.StrElement;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.DimensionMismatchException;
import org.JMathStudio.PixelImageToolkit.BinaryPixelImage.BinaryImageMath;
import org.JMathStudio.PixelImageToolkit.BinaryPixelImage.BinaryPixelImage;

/**
 * This class define various Binary Morphological operations applicable over the binary/boolean
 * discrete images. 
 * <p>A Binary/boolean discrete image will be represented by a {@link BinaryPixelImage}.
 * <p>For all the morphological operations defined here, if condition arise to consider the
 * image pixels out side the image bounds, all such outlier pixels are ignored with in the
 * operation.  
 * <pre>Usage:
 * BinaryPixelImage img = BinaryPixelImage.importImage("path");//Import input image as BinaryPixelImage.
 * 
 * BinaryMorphology bm = new BinaryMorphology();//Create an instance of BinaryMorphology.
 * StrElement ele = StrElement.squareStrElement(5);//Select suitable structuring element.
 * 
 * BinaryPixelImage opening = bm.opening(img, ele);//Apply morphological opening operation on the
 * input binary image.
 * 
 * BinaryPixelImage fill = bm.fill(img);//Apply hole filling operation on the input binary
 * image.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */

public final class BinaryMorphology {	
	/**
	 * This method apply the morphological Hit and Miss operation on the
	 * discrete binary image as represented by the {@link BinaryPixelImage} 'img' and
	 * return the resultant binary image as a BinaryPixelImage.
	 * <p>The argument {@link StrElement} 'strEle' specify the structuring element 
	 * to be employed for the given morphological operation.
	 * 
	 * @param BinaryPixelImage
	 *            img
	 * @param StrElement
	 *            strEle
	 * @return BinaryPixelImage
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public BinaryPixelImage hitAndMiss(BinaryPixelImage img, StrElement strEle){

		try{

			int H = img.getHeight();
			int W = img.getWidth();
			int h = strEle.getHeight();
			int w = strEle.getWidth();

			BinaryPixelImage result = (BinaryPixelImage) img.getEquivalentBlankImage();

			//Ensure the structuring element dimensions are odd.
			int cY = (h-1) / 2;
			int cX = (w-1) / 2;
			boolean loop;
			boolean pixel, ele;

			for (int i = 0; i < H; i++)
			{
				for (int j = 0; j < W; j++) 
				{
					loop = true;
					//By default set result as '1'.
					//Check for hit and miss condition for all the neighbouring pixels
					//as per and with the structuring element. If fails set result '0'.
					result.setPixel(true, i,j);

					for (int k = 0; loop && k < h; k++) 
					{
						for (int l = 0; loop && l < w; l++) 
						{
							int Y = i + k - cY;
							int X = j + l - cX;

							if (Y >= 0 && Y < H && X >= 0 && X < W) 
							{
								pixel = img.getPixel(Y,X);
								ele = strEle.isStructure(k,l);

								//Check if the corresponding image pixel and structure element
								//are same. Both 1 (hit) or Both 0(miss).
								//when this is not the case, hit and miss fails and set the
								//result as '0'. Break loop as no further check is necessary.
								if(!(pixel&&ele || !pixel&&!ele))
								{
									result.setPixel(false,i,j);
									loop = false;
								}
							}
						}
					}
				}
			}

			return result;

		}catch(ArrayIndexOutOfBoundsException e){
			throw new BugEncounterException();
		}
	}

	/**
	 * This method apply the morphological Dilation operation on the
	 * discrete binary image as represented by the {@link BinaryPixelImage} 'img' and 
	 * return the resultant dilated image as a {@link BinaryPixelImage}.
	 * <p>The argument {@link StrElement} 'strEle' specify the structuring element 
	 * to be employed for the given morphological operation.
	 *
	 * @param BinaryPixelImage
	 *            img
	 * @param StrElement
	 *            strEle
	 * @return BinaryPixelImage
	 * @see #erasion(BinaryPixelImage, StrElement)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	/**
	 * This method apply the morphological Dilation operation on the
	 * discrete binary image as represented by the {@link BinaryPixelImage} 'img' and 
	 * return the resultant dilated image as a {@link BinaryPixelImage}.
	 * <p>The argument {@link StrElement} 'strEle' specify the structuring element 
	 * to be employed for the given morphological operation.
	 *
	 * @param BinaryPixelImage
	 *            img
	 * @param StrElement
	 *            strEle
	 * @return BinaryPixelImage
	 * @see #erasion(BinaryPixelImage, StrElement)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public BinaryPixelImage dilation(BinaryPixelImage img, StrElement strEle){

		try{

			BinaryPixelImage result = (BinaryPixelImage) img.clone();

			int H = result.getHeight();
			int W = result.getWidth();
			int h = strEle.getHeight();
			int w = strEle.getWidth();

			//h and w should be odd. Ensure same by Structuring element class.
			int cY = (h-1) / 2;
			int cX = (w-1) / 2;

			boolean loop;

			for (int i = 0; i < H; i++) 
			{
				for (int j = 0; j < W; j++) 
				{
					if(!img.getPixel(i, j))
					{
						loop = true;

						for (int k = 0; loop && k < h; k++) 
						{
							for (int l = 0; loop && l < w; l++) 
							{
								if (strEle.isStructure(k,l) == true) 
								{
									int Y = i + k - cY;
									int X = j + l - cX;

									if (Y >= 0 && Y < H && X >= 0 && X < W) 
									{
										if(img.getPixel(Y, X))
										{
											result.setPixel(true, i, j);
											loop = false;
										}
									}
								}
							}
						}
					}
				}
			}

			return result;

		}catch(ArrayIndexOutOfBoundsException e){
			throw new BugEncounterException();
		}
	}

	/**
	 * This method apply the morphological Erasion operation on the
	 * discrete binary image as represented by the {@link BinaryPixelImage} 'img' and 
	 * return the resultant erased image as a {@link BinaryPixelImage}.
	 * <p>The argument {@link StrElement} 'strEle' specify the structuring element 
	 * to be employed for the given morphological operation.
	 *
	 * @param BinaryPixelImage
	 *            img
	 * @param StrElement
	 *            strEle
	 * @return BinaryPixelImage
	 * @see #dilation(BinaryPixelImage, StrElement)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public BinaryPixelImage erasion(BinaryPixelImage img, StrElement strEle){
		try{

			BinaryPixelImage result = (BinaryPixelImage) img.clone();

			int H = result.getHeight();
			int W = result.getWidth();
			int h = strEle.getHeight();
			int w = strEle.getWidth();

			//h and w should be odd. Ensure same by Structuring element class.
			int cY = (h-1) / 2;
			int cX = (w-1) / 2;

			boolean loop;

			for (int i = 0; i < H; i++) 
			{
				for (int j = 0; j < W; j++) 
				{
					if(img.getPixel(i, j))
					{
						loop = true;

						for (int k = 0; loop && k < h; k++) 
						{
							for (int l = 0; loop && l < w; l++) 
							{
								if (strEle.isStructure(k,l) == true) 
								{
									int Y = i + k - cY;
									int X = j + l - cX;

									if (Y >= 0 && Y < H && X >= 0 && X < W) 
									{
										if(!img.getPixel(Y, X))
										{
											result.setPixel(false, i, j);
											loop = false;
										}
									}
								}
							}
						}
					}
				}
			}

			return result;

		}catch(ArrayIndexOutOfBoundsException e){
			throw new BugEncounterException();
		}
	}

	/**
	 * This method apply the morphological Opening operation on the
	 * discrete binary image as represented by the {@link BinaryPixelImage} 'img' and 
	 * return the resultant opened image as a {@link BinaryPixelImage}.
	 * <p>The argument {@link StrElement} 'strEle' specify the structuring element 
	 * to be employed for the given morphological operation.
	 * <p><i>Opening operation is the Erasion of the image followed by a Dilation.</i>
	 * @param BinaryPixelImage
	 *            img
	 * @param StrElement
	 *            strEle
	 * @return BinaryPixelImage
	 * @see #closing(BinaryPixelImage, StrElement)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public BinaryPixelImage opening(BinaryPixelImage img, StrElement strEle){
		return dilation(erasion(img, strEle), strEle);
	}

	/**
	 * This method apply the morphological Closing operation on the
	 * discrete binary image as represented by the {@link BinaryPixelImage} 'img' and 
	 * return the resultant closed image as a {@link BinaryPixelImage}.
	 * <p>The argument {@link StrElement} 'strEle' specify the structuring element 
	 * to be employed for the given morphological operation.
	 * <p><i>Closing operation is the Dilation of the image followed by an Erasion.</i>
	 * @param BinaryPixelImage
	 *            img
	 * @param StrElement
	 *            strEle
	 * @return BinaryPixelImage
	 * @see #opening(BinaryPixelImage, StrElement)
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public BinaryPixelImage closing(BinaryPixelImage img, StrElement strEle){
		return erasion(dilation(img, strEle), strEle);
	}


	/**
	 * This method apply the morphological Bridge operation on the
	 * discrete binary image as represented by the {@link BinaryPixelImage} 'img' and 
	 * return the resultant bridged image as a {@link BinaryPixelImage}.
	 * <p>Bridge operation fills in any fore ground (true) pixels to link up the bridges in the
	 * image.
	 * <i>Bridge operation set the given back ground (false) pixel to fore ground (true) if it has
	 * at least 2 adjoining fore ground pixels.</i>
	 * @param BinaryPixelImage	img
	 * @return BinaryPixelImage
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public BinaryPixelImage bridge(BinaryPixelImage img)
	{
		//Bridge operation: If back ground pixel (false) has at least two adjoining fore ground (true)
		//pixels, set that pixel to fore ground (true).

		try{

			int H = img.getHeight();
			int W = img.getWidth();

			BinaryPixelImage result = (BinaryPixelImage) img.getEquivalentBlankImage();

			//Going to check the number of adjoining pixels are true.
			short nosOfTrues = 0;
			Boolean loop;

			for(int i=0;i<H;i++)
			{
				for(int j=0;j<W;j++)
				{
					//If given image is fore ground (true) nothing to be done, set same to result.
					if(img.getPixel(i,j))
						result.setPixel(true, i,j);
					else
					{
						//At each new pixel set this to 0.
						nosOfTrues = 0;
						loop = true;

						//Loop through all the adjoining connected pixel position.
						//Including the current pixel position. The login will
						//reach at this stage only if current pixel is (false). 
						//So no impact to check for current pixel position.
						for(int l=-1,y = i+l;loop && l<=1;l++,y++)
						{
							for(int k=-1, x = j+k;loop && k<=1;k++,x++)
							{
								if(y>=0 && y<H && x>=0 && x<W)
								{
									//Count the numbers of (true) adjoining pixels.
									if(img.getPixel(y,x))
										nosOfTrues++;
									//If count is '2'. Set result to fore ground (true).
									//Break further check ie loop.
									if(nosOfTrues == 2)
									{
										result.setPixel(true, i,j);
										loop = false;
									}
								}
							}
						}
					}
				}
			}

			return result;

		}catch(ArrayIndexOutOfBoundsException e){
			throw new BugEncounterException();
		}
	}

	/**
	 * This method apply the morphological Clean operation on the
	 * discrete binary image as represented by the {@link BinaryPixelImage} 'img' and 
	 * return the resultant cleaned image as a {@link BinaryPixelImage}.
	 * <p>Clean operation remove any isolated fore ground (true) pixels in the image.
	 * <i>Clean operation set the given fore ground (true) pixel to back ground (false) if it has
	 * no adjoining fore ground pixels.</i>
	 * @param BinaryPixelImage	img
	 * @return BinaryPixelImage
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public BinaryPixelImage clean(BinaryPixelImage img)
	{
		//Clean operation: If fore ground pixel (true) has no adjoining fore ground (true)
		//pixels, set that pixel to back ground (false).
		//Cleans isolated fore ground pixels in the image.

		try{

			int H = img.getHeight();
			int W = img.getWidth();

			//All pixels are back ground (false).
			BinaryPixelImage result = (BinaryPixelImage) img.getEquivalentBlankImage();

			Boolean loop;

			for(int i=0;i<H;i++)
			{
				for(int j=0;j<W;j++)
				{
					//If given pixel is fore ground (true) we need to check if it has at least one
					//adjoining fore ground pixel ie. It is not isolated.
					//For back ground nothing to be done. Set same in result (default).
					if(img.getPixel(i,j))
					{
						//Let us assume that given fore ground pixel is isolated and thus set result to
						//back ground (false).
						result.setPixel(false, i,j);

						loop = true;
						//Loop through all the adjoining connected pixel position
						//except current position. Check if at least one adjoining pixel
						//is fore ground (true) ie given fore ground pixel is not isolated.
						for(int l=-1,y = i+l;loop && l<=1;l++,y++)
						{
							for(int k=-1, x = j+k;loop && k<=1;k++,x++)
							{
								if(y>=0 && y<H && x>=0 && x<W)
								{
									//do not check current position.
									if(l==0 && k==0)
										continue;
									//if at least one adjoining pixel is fore ground(true)
									//set fore ground pixel as fore ground in result ie do not
									//clean up. No need to further check, so break loop.
									if(img.getPixel(y,x))
									{
										result.setPixel(true, i,j);
										loop = false;
									}
								}
							}
						}
					}
				}
			}

			return result;

		}catch(ArrayIndexOutOfBoundsException e){
			throw new BugEncounterException();
		}
	}

	/**
	 * This method apply the morphological Fille operation on the
	 * discrete binary image as represented by the {@link BinaryPixelImage} 'img' and 
	 * return the resultant filled image as a {@link BinaryPixelImage}.
	 * <p>Fill operation fills any isolated back ground (false) pixels in the image.
	 * <i>Fill operation set the given back ground (false) pixel to fore ground (true) if it has
	 * no adjoining back ground pixels.</i>
	 * @param BinaryPixelImage	img
	 * @return BinaryPixelImage
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public BinaryPixelImage fill(BinaryPixelImage img)
	{
		//Fill operation: If back ground pixel (false) has no adjoining back ground (false)
		//pixels, set that pixel to fore ground (true).
		//Fills isolated back ground pixels in the image.

		try{

			int H = img.getHeight();
			int W = img.getWidth();

			//All pixels are back ground (false).
			BinaryPixelImage result = (BinaryPixelImage) img.getEquivalentBlankImage();

			Boolean loop;

			for(int i=0;i<H;i++)
			{
				for(int j=0;j<W;j++)
				{
					//If given pixel is fore ground (true) no need to do anything. Set the same in the
					//result.
					if(img.getPixel(i,j))
						result.setPixel(true, i,j);
					//If given pixel is back ground (false) we need to check if it has at least one
					//adjoining back ground pixel ie. It is not isolated.
					else
					{
						//Let us assume that given back ground pixel is isolated and thus set result to
						//fore ground (true). ie fill it.
						result.setPixel(true,i,j);

						loop = true;
						//Loop through all the adjoining connected pixel position
						//except current position. Check if at least one adjoining pixel
						//is back ground (false) ie given back ground pixel is not isolated.
						for(int l=-1,y = i+l;loop && l<=1;l++,y++)
						{
							for(int k=-1, x = j+k;loop && k<=1;k++,x++)
							{
								//If pixel is within the image bounds.
								if(y >=0 && y <H && x >=0 && x < W)
								{
									//do not check current position.
									if(l==0 && k==0)
										continue;
									//if at least one adjoining pixel is back ground(false)
									//set back ground pixel as back ground in result ie do not
									//fill up. No need to further check, so break loop.
									if(!img.getPixel(y,x))
									{
										result.setPixel(false, i,j);
										loop = false;
									}
								}
							}
						}
					}
				}
			}

			return result;

		}catch(ArrayIndexOutOfBoundsException e){
			throw new BugEncounterException();
		}
	}

	/**
	 * This method apply the morphological Majority operation on the
	 * discrete binary image as represented by the {@link BinaryPixelImage} 'img' and 
	 * return the resultant image as a {@link BinaryPixelImage}.
	 * <p>Majority operation maps a binary pixel in the input image, to majority of the either
	 * fore ground (true) or back ground (false) pixels in its immediate neighbourhood counting
	 * itself, in the resultant image.
	 * @param BinaryPixelImage	img
	 * @return BinaryPixelImage
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public BinaryPixelImage majority(BinaryPixelImage img)
	{
		//Majority operation: This operation assign a pixel to either a fore ground (true) or back ground
		//pixel (false) based on the majority pixels in its 3 by 3 neighbourhood.

		try{

			int H = img.getHeight();
			int W = img.getWidth();

			//All pixels are back ground (false).
			BinaryPixelImage result = (BinaryPixelImage) img.getEquivalentBlankImage();

			Boolean loop;
			short trues = 0;
			short falses = 0;

			for(int i=0;i<H;i++)
			{
				for(int j=0;j<W;j++)
				{
					//Set loop to true.
					loop = true;		
					//Counts of true's and false's in the neighbourhood initialised to 0.
					trues = 0;
					falses = 0;

					//Loop through the neighbourhood for each pixel.
					for(int l=-1,y = i+l;loop && l<=1;l++,y++)
					{
						for(int k=-1, x = j+k;loop && k<=1;k++,x++)
						{
							//If pixel is within the image bounds.
							if(y >=0 && y <H && x >=0 && x < W)
							{
								//Count votes for true's or false's
								if(img.getPixel(y, x))
									trues++;
								else
									falses++;								
							}
						}
					}

					//If true are more, set result to true.
					if(trues>falses)
						result.setPixel(true,i,j);
					//if false are more, set result to false.
					else if(falses > trues)
						result.setPixel(false, i,j);
					//if true and false are same, set result to image pixel.
					//This may happen for boundary pixels, where part of structuring
					//element goes out of image and is not considered.
					//Otherwise with odd structuring element dimension, this case
					//is not possible.
					else
						result.setPixel(img.getPixel(i,j), i,j);
				}
			}

			return result;

		}catch(ArrayIndexOutOfBoundsException e){
			throw new BugEncounterException();
		}
	}

	/**
	 * This method apply the morphological Thinning operation on the
	 * discrete binary image as represented by the {@link BinaryPixelImage} 'img' and 
	 * return the resultant thinned image as a {@link BinaryPixelImage}.
	 * <p>The argument {@link StrElement} 'strEle' specify the structuring element 
	 * to be employed for the given morphological operation.
	 * <p>Morphological thinning operation is defined as follows:
	 * <p><i> I - hit and miss(I, S)
	 * <p>where, 'I' is the image and 'S' is the structuring element.
	 * <p>'-' indicate logical substraction.</i>
	 * @param BinaryPixelImage
	 *            img
	 * @param StrElement
	 *            strEle
	 * @return BinaryPixelImage
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */	
	public BinaryPixelImage thinning(BinaryPixelImage img, StrElement strEle)
	{
		BinaryPixelImage hm = hitAndMiss(img, strEle);

		try {
			return BinaryImageMath.logicalSubstraction(img, hm);
		} catch (DimensionMismatchException e) {
			throw new BugEncounterException();
		}
	}	

	/**
	 * This method apply the morphological Thickning operation on the
	 * discrete binary image as represented by the {@link BinaryPixelImage} 'img' and 
	 * return the resultant image as a {@link BinaryPixelImage}.
	 * <p>The argument {@link StrElement} 'strEle' specify the structuring element 
	 * to be employed for the given morphological operation.
	 * <p>Morphological thickning of the fore ground image is derived from the 
	 * morphological thinning of the back ground image.
	 * @param BinaryPixelImage
	 *            img
	 * @param StrElement
	 *            strEle
	 * @return BinaryPixelImage
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */	
	public BinaryPixelImage thickning(BinaryPixelImage img, StrElement strEle)
	{
		BinaryPixelImage result = thinning(img.getComplementaryImage(), strEle);
		try {
			return BinaryImageMath.logicalOR(result,img);
		} catch (DimensionMismatchException e) {
			throw new BugEncounterException();
		}
		//Even following gives the same result. But as per theory, above algorithm
		//is to be employed. 
		//return hitAndMiss(img.getComplimentImage(), strEle).getComplimentImage();
	}
}
