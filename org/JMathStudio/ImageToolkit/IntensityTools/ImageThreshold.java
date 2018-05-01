package org.JMathStudio.ImageToolkit.IntensityTools;

import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.IllegalArgumentException;
import org.JMathStudio.PixelImageToolkit.UIntPixelImage.AbstractUIntPixelImage;

/**
 * This class define various threshold operations to threshold the pixel intensities of an image
 * represented either by a {@link Cell} or an {@link AbstractUIntPixelImage} respectively.
 * <pre>Usage:
 * Cell img = Cell.importImageAsCell("path");//Import input image as Cell.
 * 
 * ImageThreshold it = new ImageThreshold();//Create an instance of ImageThreshold.
 * 
 * Cell result1 = it.thresholdAbove(img, k, L);//Set pixels of input image with intensity above
 * 'k' to value 'L'.
 * 
 * Cell result2 = it.thresholdBetween(img, k1, k2, L);//Set pixels of input image with intensity
 * between 'k1' & 'k2' to value 'L'.
 * <pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class ImageThreshold {

	/**
	 * This method will threshold the pixel intensities of the UInt PixelImage as represented
	 * by an {@link AbstractUIntPixelImage} above the threshold 'T' and return the resultant
	 * UInt PixelImage as an {@link AbstractUIntPixelImage}.
	 * <p>
	 * This operation will set all the image pixel with intensities more than the threshold 'T' to 
	 * zero.
	 * <p>
	 * The threshold 'T' should be with in the valid pixel intensity range for the given UInt
	 * PixelImage else this method will throw an IllegalArgument Exception.
	 * 
	 * @param AbstractUIntPixelImage
	 *            img
	 * @param int T
	 * @return AbstractUIntPixelImage
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public AbstractUIntPixelImage thresholdAbove(AbstractUIntPixelImage img, int T) throws IllegalArgumentException{

		if(T <img.getMinValidPixel() || T>img.getMaxValidPixel())
			throw new IllegalArgumentException();
		else
		{
			try{
				//Assumption that blank image has all 0's.
				AbstractUIntPixelImage result = (AbstractUIntPixelImage) img.getEquivalentBlankImage();

				int height = result.getHeight();
				int width = result.getWidth();

				for(int i=0;i<height;i++){
					for(int j=0;j<width;j++){
						//If pixel is more than Threshold 'T' it should be thresholded to '0'.
						//So do not include that pixel in result. For all other pixels copy
						//them into result.
						if(img.getPixel(i, j)<=T)
							result.setPixel(img.getPixel(i, j), i, j);
					}
				}
				return result;
			}catch (IllegalArgumentException e) {
				throw new BugEncounterException();
			}
		}
	}

	/**
	 * This method will threshold the pixel intensities of the UInt PixelImage as represented
	 * by an {@link AbstractUIntPixelImage} below the threshold 'T' and return the resultant
	 * UInt PixelImage as an {@link AbstractUIntPixelImage}.
	 * <p>
	 * This operation will set all the image pixel with intensities less than the threshold 'T' to 
	 * zero.
	 * <p>
	 * The threshold 'T' should be with in the valid pixel intensity range for the given UInt
	 * PixelImage else this method will throw an IllegalArgument Exception.
	 * 
	 * @param AbstractUIntPixelImage
	 *            img
	 * @param int T
	 * @return AbstractUIntPixelImage
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public AbstractUIntPixelImage thresholdBelow(AbstractUIntPixelImage img, int T) throws IllegalArgumentException{

		if(T <img.getMinValidPixel() || T>img.getMaxValidPixel())
			throw new IllegalArgumentException();
		else
		{
			try{
				//Assumption that blank image has all 0's.
				AbstractUIntPixelImage result = (AbstractUIntPixelImage) img.getEquivalentBlankImage();

				int height = result.getHeight();
				int width = result.getWidth();

				for(int i=0;i<height;i++){
					for(int j=0;j<width;j++){
						//If pixel is less than Threshold 'T' it should be thresholded to '0'.
						//So do not include that pixel in result. For all other pixels copy
						//them into result.
						if(img.getPixel(i, j)>=T)
							result.setPixel(img.getPixel(i, j), i, j);
					}
				}
				return result;
			}catch (IllegalArgumentException e) {
				throw new BugEncounterException();
			}
		}
	}

	/**
	 * This method will threshold the pixel intensities of the UInt PixelImage as represented
	 * by an {@link AbstractUIntPixelImage} between the thresholds 'T1' & 'T2' and return the 
	 * resultant UInt PixelImage as an {@link AbstractUIntPixelImage}.
	 * <p>
	 * This operation will set all the image pixel with intensities less than the threshold 'T2'
	 * but more than 'T1' to zero.
	 * <p>
	 * The argument 'T1' & 'T2' should be within the valid pixel intensity range for the given UInt
	 * PixelImage 'img' else this method will throw an IllegalArgument Exception. 
	 * <p>Further, upper threshold value 'T2' should be more than the lower threshold value 'T1' 
	 * else this method will throw an IllegalArgument Exception. 
	 * 
	 * @param AbstractUIntPixelImage
	 *            img
	 * @param int T1
	 * @param int T2
	 * @return AbstractUIntPixelImage
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public AbstractUIntPixelImage thresholdBetween(AbstractUIntPixelImage img, int T1,int T2) throws IllegalArgumentException{

		if(T1 >= T2 || T1 <img.getMinValidPixel() || T1>img.getMaxValidPixel() || T2 <img.getMinValidPixel()
		|| T2 >img.getMaxValidPixel())
			throw new IllegalArgumentException();
		else
		{
			try{
				//Assumption that blank image has all 0's.
				AbstractUIntPixelImage result = (AbstractUIntPixelImage) img.getEquivalentBlankImage();

				int height = result.getHeight();
				int width = result.getWidth();
				int pixel=0;

				for(int i=0;i<height;i++){
					for(int j=0;j<width;j++){
						pixel = img.getPixel(i, j);
						//If pixel is between thresholds 'T1' and 'T2' it should be thresholded to '0'.
						//So do not include that pixel in result. For all other pixels copy
						//them into result.
						if(pixel>=T2 || pixel <=T1)
							result.setPixel(pixel, i, j);
					}
				}
				return result;
			}catch (IllegalArgumentException e) {
				throw new BugEncounterException();
			}
		}
	}

	/**
	 * This method will threshold the pixel intensities of the UInt PixelImage as represented
	 * by an {@link AbstractUIntPixelImage} beyond the thresholds 'T1' & 'T2' and return the 
	 * resultant UInt PixelImage as an {@link AbstractUIntPixelImage}.
	 * <p>
	 * This operation will set all the image pixel with intensities more than upper threshold 'T2'
	 * or less than lower threshold 'T1' to zero.
	 * <p>
	 * The argument 'T1' & 'T2' should be within the valid pixel intensity range for the given UInt
	 * PixelImage 'img' else this method will throw an IllegalArgument Exception. 
	 * <p>Further, upper threshold value 'T2' should be more than the lower threshold value 'T1' 
	 * else this method will throw an IllegalArgument Exception. 
	 * 
	 * @param AbstractUIntPixelImage
	 *            img
	 * @param int T1
	 * @param int T2
	 * @return AbstractUIntPixelImage
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public AbstractUIntPixelImage thresholdBeyond(AbstractUIntPixelImage img, int T1,int T2) throws IllegalArgumentException{

		if(T1 >= T2 || T1 <img.getMinValidPixel() || T1>img.getMaxValidPixel() || T2 <img.getMinValidPixel() 
		|| T2 >img.getMaxValidPixel())
			throw new IllegalArgumentException();
		else
		{
			try{
				//Assumption that blank image has all 0's.
				AbstractUIntPixelImage result = (AbstractUIntPixelImage) img.getEquivalentBlankImage();

				int height = result.getHeight();
				int width = result.getWidth();
				int pixel=0;

				for(int i=0;i<height;i++){
					for(int j=0;j<width;j++){
						pixel = img.getPixel(i, j);
						//If pixel is between thresholds 'T1' and 'T2' it should be thresholded to '0'.
						//So do not include that pixel in result. For all other pixels copy
						//them into result.
						if(pixel>=T1 && pixel<=T2)
							result.setPixel(pixel, i, j);
					}
				}
				return result;
			}catch (IllegalArgumentException e) {
				throw new BugEncounterException();
			}
		}
	}

	/**
	 * This method will replace only those elements of the {@link Cell} 'cell' whose value is more than the 
	 * threshold 'T' with a new value as given by the argument 'set' in the corresponding resultant Cell and 
	 * return the same.
	 *  
	 * @param Cell cell
	 * @param float T
	 * @param float set
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell thresholdAbove(Cell cell, float T,float set) {
		int rc = cell.getRowCount();
		int cc = cell.getColCount();

		Cell result = new Cell(rc,cc);

		for (int i = 0; i < rc; i++) {
			for (int j = 0; j < cc; j++) {
				if (cell.getElement(i, j) > T) {
					result.setElement(set, i, j);
				} else {
					result.setElement(cell.getElement(i, j), i, j);
				}
			}
		}

		return result;
	}

	/**
	 * This method will replace only those elements of the {@link Cell} 'cell' whose value is less than the 
	 * threshold 'T' with a new value as given by the argument 'set' in the corresponding resultant Cell and 
	 * return the same.
	 *  
	 * @param Cell cell
	 * @param float T
	 * @param float set
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell thresholdBelow(Cell cell, float T,float set) {
		int rc = cell.getRowCount();
		int cc = cell.getColCount();

		Cell result = new Cell(rc,cc);

		for (int i = 0; i < rc; i++) {
			for (int j = 0; j < cc; j++) {
				if (cell.getElement(i, j) < T) {
					result.setElement(set, i, j);
				} else {
					result.setElement(cell.getElement(i, j), i, j);
				}
			}
		}

		return result;
	}

	/**
	 * This method will replace only those elements of the {@link Cell} 'cell' whose value is more than 
	 * lower threshold 'T1' but less than upper threshold 'T2' with a new value as given by the argument 
	 * 'set' in the corresponding resultant Cell and return the same.
	 * <p>Upper threshold value 'T2' should be more than the lower threshold value 'T1' else this method 
	 * will throw an IllegalArgument Exception. 
	 *
	 * @param Cell cell
	 * @param float T1
	 * @param float T2
	 * @param float set
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell thresholdBetween(Cell cell, float T1,float T2,float set) throws IllegalArgumentException {

		if(T1 >= T2)
			throw new IllegalArgumentException();
		else
		{
			int rc = cell.getRowCount();
			int cc = cell.getColCount();

			Cell result = new Cell(rc,cc);
			float ele=0;

			for (int i = 0; i < rc; i++) {
				for (int j = 0; j < cc; j++) {
					ele = cell.getElement(i, j);
					if (ele > T1 && ele <T2) {
						result.setElement(set, i, j);
					} else {
						result.setElement(ele, i, j);
					}
				}
			}

			return result;
		}
	}

	/**
	 * This method will replace only those elements of the {@link Cell} 'cell' whose value is either less than 
	 * lower threshold 'T1' or more than upper threshold 'T2' with a new value as given by the argument 
	 * 'set' in the corresponding resultant Cell and return the same.
	 * <p>Upper threshold value 'T2' should be more than the lower threshold value 'T1' else this method 
	 * will throw an IllegalArgument Exception. 
	 *
	 * @param Cell cell
	 * @param float T1
	 * @param float T2
	 * @param float set
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell thresholdBeyond(Cell cell, float T1,float T2,float set) throws IllegalArgumentException {

		if(T1 >= T2)
			throw new IllegalArgumentException();
		else
		{
			int rc = cell.getRowCount();
			int cc = cell.getColCount();

			Cell result = new Cell(rc,cc);
			float ele=0;

			for (int i = 0; i < rc; i++) {
				for (int j = 0; j < cc; j++) {
					ele = cell.getElement(i, j);
					if (ele < T1 || ele >T2) {
						result.setElement(set, i, j);
					} else {
						result.setElement(ele, i, j);
					}
				}
			}

			return result;
		}
	}


	/**
	 * This method will apply a Soft threshold on the pixel intensities of the discrete real image
	 * as represented by the Cell 'Cell' with the threshold as given by the argument 'T'. 
	 * The resultant thresholded image will be return as a Cell.
	 * <p>
	 * The argument 'T' should be more than or equal to 0 else this method will
	 * throw an IllegalArgument Exception.
	 * 
	 * @param Cell
	 *            cell
	 * @param float T
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell softThreshold(Cell cell, float T)throws IllegalArgumentException {
		if (T < 0)
			throw new IllegalArgumentException();
		else
		{
			int rc = cell.getRowCount();
			int cc = cell.getColCount();

			Cell result = new Cell(rc,cc);
			float pixel=0;
			float dummy=0;

			for (int i = 0; i < rc; i++) {
				for (int j = 0; j < cc; j++) {
					pixel = cell.getElement(i, j);

					if (pixel < 0)
						dummy = -Math.max(0, Math.abs(pixel)-T);
					else
						dummy = Math.max(0, Math.abs(pixel)-T);

					result.setElement(dummy, i, j);
				}
			}

			return result;
		}
	}

}
