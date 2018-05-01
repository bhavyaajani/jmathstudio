package org.JMathStudio.ImageToolkit.SpatialTools;

import org.JMathStudio.DataStructure.Structure.StrElement;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.IllegalArgumentException;
import org.JMathStudio.PixelImageToolkit.BinaryPixelImage.BinaryPixelImage;

/**
 * This class define various Binary Geodesic Morphological operations applicable over the binary/boolean
 * discrete images as represenetd by a{@link BinaryPixelImage}.
 * <p>Geodesic morphological operations are essentially regular morphological operations but touch only
 * those regions of input image as defined by the geodesic mask image.
 * <p>For all the morphological operations defined here, if condition arise to consider the
 * image pixels out side the image bounds, all such outlier pixels are ignored with in the
 * operation.  
 * <pre>Usage:
 * BinaryPixelImage img = BinaryPixelImage.importImage("path");//Import input image as BinaryPixelImage.
 *		
 * //Let BinaryPixelImage 'mask' represent the required geodesic mask and has similar dimensions as that of 
 * //the input image 'img'.
 *		
 * GeodesicBinaryMorphology gbm = new GeodesicBinaryMorphology();//Create an instance of GeodesicBinaryMorphology.
 * StrElement ele = StrElement.squareStrElement(5);//Select suitable structuring element.
 *		 
 * BinaryPixelImage opening = gbm.geodesicOpening(img, mask, ele);//Apply geodesic morphological opening operation on the input binary image with given mask and structuring element.
 *		
 * BinaryPixelImage erasion = gbm.geodesicErasion(img, mask, ele);//Apply geodesic morphological erasion operation on the input binary image with given mask and structuring element.
 *	
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class GeodesicBinaryMorphology {

	/**
	 * This method apply a geodesic morphological erasion operation on the discrete binary image as represented
	 * by the {@link BinaryPixelImage} 'img' with geodesic mask as given by {@link BinaryPixelImage} 'mask' and
	 * return the resultant erased image as a {@link BinaryPixelImage}.
	 * <p>The argument {@link StrElement} 'strEle' specify the structuring element to be employed for the given 
	 * morphological operation.
	 * <p>Geodesic erasion operation impacts only those regions of 'img' as defined by the foreground within the
	 * 'mask' image.
	 * <p>If the dimensions of images 'img' and 'mask' are not same this method shall throw an IllegalArgument Exception.
	 * 
	 * @param BinaryPixelImage
	 *            img
	 * @param BinaryPixelImage
	 *            mask
	 * @param StrElement
	 *            strEle
	 * @return BinaryPixelImage
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public BinaryPixelImage geodesicErasion(BinaryPixelImage img,
			BinaryPixelImage mask, StrElement strEle)
					throws IllegalArgumentException {
		if (!img.hasSameDimensions(mask)) {
			throw new IllegalArgumentException();
		}else {
			try {

				int H = img.getHeight();
				int W = img.getWidth();

				//				for(int i=0;i<H;i++)
				//				{
				//					for(int j=0;j<W;j++)
				//					{
				//						if(img.getPixel(i, j) && !mask.getPixel(i, j))
				//							throw new IllegalArgumentException();
				//					}
				//				}

				BinaryPixelImage result = (BinaryPixelImage) img.clone();

				int h = strEle.getHeight();
				int w = strEle.getWidth();

				// h and w should be odd. Ensure same by Structuring element
				// class.
				int cY = (h - 1) / 2;
				int cX = (w - 1) / 2;

				boolean loop;

				for (int i = 0; i < H; i++) {
					for (int j = 0; j < W; j++) {
						if (img.getPixel(i, j)) {							
							loop = true;

							for (int k = 0; loop && k < h; k++) {
								for (int l = 0; loop && l < w; l++) {
									if (strEle.isStructure(k, l) == true) {
										int Y = i + k - cY;
										int X = j + l - cX;

										if (Y >= 0 && Y < H && X >= 0 && X < W) {
											if (mask.getPixel(Y, X) && !img.getPixel(Y, X)) {
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

			} catch (ArrayIndexOutOfBoundsException e) {
				throw new BugEncounterException();
			}
		}
	}

	/**
	 * This method apply a geodesic morphological dilation operation on the discrete binary image as represented
	 * by the {@link BinaryPixelImage} 'img' with geodesic mask as given by {@link BinaryPixelImage} 'mask' and
	 * return the resultant erased image as a {@link BinaryPixelImage}.
	 * <p>The argument {@link StrElement} 'strEle' specify the structuring element to be employed for the given 
	 * morphological operation.
	 * <p>Geodesic dilation operation impacts only those regions of 'img' as defined by the foreground within the
	 * 'mask' image.
	 * <p>If the dimensions of images 'img' and 'mask' are not same this method shall throw an IllegalArgument Exception.
	 * 
	 * @param BinaryPixelImage
	 *            img
	 * @param BinaryPixelImage
	 *            mask
	 * @param StrElement
	 *            strEle
	 * @return BinaryPixelImage
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public BinaryPixelImage geodesicDilation(BinaryPixelImage img,BinaryPixelImage mask, StrElement strEle)
			throws IllegalArgumentException {
		if (!img.hasSameDimensions(mask)) {
			throw new IllegalArgumentException();
		}else {
			try {

				int H = img.getHeight();
				int W = img.getWidth();

				//				for(int i=0;i<H;i++)
				//				{
				//					for(int j=0;j<W;j++)
				//					{
				//						if(img.getPixel(i, j) && !mask.getPixel(i, j))
				//							throw new IllegalArgumentException();
				//					}
				//				}

				BinaryPixelImage result = (BinaryPixelImage) img.clone();

				int h = strEle.getHeight();
				int w = strEle.getWidth();

				// h and w should be odd. Ensure same by Structuring element
				// class.
				int cY = (h - 1) / 2;
				int cX = (w - 1) / 2;

				boolean loop;

				for (int i = 0; i < H; i++) {
					for (int j = 0; j < W; j++) {
						if (mask.getPixel(i, j) && !img.getPixel(i, j)) {							
							loop = true;
							for (int k = 0; loop && k < h; k++) {
								for (int l = 0; loop && l < w; l++) {
									if (strEle.isStructure(k, l) == true) {
										int Y = i + k - cY;
										int X = j + l - cX;

										if (Y >= 0 && Y < H && X >= 0 && X < W) {
											if (mask.getPixel(Y, X) && img.getPixel(Y, X)) {
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

			} catch (ArrayIndexOutOfBoundsException e) {
				throw new BugEncounterException();
			}
		}
	}


	/**
	 * This method apply a geodesic morphological opening operation on the discrete binary image as represented
	 * by the {@link BinaryPixelImage} 'img' with geodesic mask as given by {@link BinaryPixelImage} 'mask' and
	 * return the resultant erased image as a {@link BinaryPixelImage}.
	 * <p>The argument {@link StrElement} 'strEle' specify the structuring element to be employed for the given 
	 * morphological operation.
	 * <p>Geodesic opening operation impacts only those regions of 'img' as defined by the foreground within the
	 * 'mask' image.
	 * <p>If the dimensions of images 'img' and 'mask' are not same this method shall throw an IllegalArgument Exception.
	 * <p><i>Geodesic Opening operation is the Geodesic Erasion of the image followed by a Geodesic Dilation.</i>
	 * 
	 * @param BinaryPixelImage
	 *            img
	 * @param BinaryPixelImage
	 *            mask
	 * @param StrElement
	 *            strEle
	 * @return BinaryPixelImage
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public BinaryPixelImage geodesicOpening(BinaryPixelImage img, BinaryPixelImage mask, StrElement strEle) throws IllegalArgumentException{
		return geodesicDilation(geodesicErasion(img, mask, strEle),mask,strEle);
	}

	/**
	 * This method apply a geodesic morphological closing operation on the discrete binary image as represented
	 * by the {@link BinaryPixelImage} 'img' with geodesic mask as given by {@link BinaryPixelImage} 'mask' and
	 * return the resultant erased image as a {@link BinaryPixelImage}.
	 * <p>The argument {@link StrElement} 'strEle' specify the structuring element to be employed for the given 
	 * morphological operation.
	 * <p>Geodesic closing operation impacts only those regions of 'img' as defined by the foreground within the
	 * 'mask' image.
	 * <p>If the dimensions of images 'img' and 'mask' are not same this method shall throw an IllegalArgument Exception.
	 * <p><i>Geodesic Closing operation is the Geodesic Dilation of the image followed by a Geodesic Erasion.</i>
	 * 
	 * @param BinaryPixelImage
	 *            img
	 * @param BinaryPixelImage
	 *            mask
	 * @param StrElement
	 *            strEle
	 * @return BinaryPixelImage
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public BinaryPixelImage geodesicClosing(BinaryPixelImage img, BinaryPixelImage mask, StrElement strEle) throws IllegalArgumentException{
		return geodesicErasion(geodesicDilation(img, mask, strEle),mask,strEle);
	}
}
