package org.JMathStudio.ImageToolkit.IntensityTools;

import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.IllegalArgumentException;
import org.JMathStudio.MathToolkit.StatisticalTools.CellStatistics.CellStatistics;
import org.JMathStudio.PixelImageToolkit.UIntPixelImage.AbstractUIntPixelImage;

/**
 * This class define various spatial point operators which manipulate or work with the 
 * individual pixel intensities of a discrete image.
 * <p>Based upon the operator a discrete image will be represented either as a {@link Cell}
 * or an {@link AbstractUIntPixelImage}.
 * <pre>Usage:
 * Cell img1 = Cell.importImageAsCell("path1");//Import input image as Cell.
 * 
 * PointOperator po = new PointOperator();//Create an instance of PointOperator.
 * 
 * Cell result1 = po.setRange(img1, min, max);//Linearly set pixel intensity range of the input
 * image as specified.
 * 
 * Cell result2 = po.contrastAndBrightness(img1, mean, std);//Re-adjust contrast and brightness
 * for input image with specific mean and standard deviation of the intensity range.
 * 
 * AbstractUIntPixelImage img2 = UInt8PixelImage.importImage("path2");//Import input image
 * as UInt PixelImage.
 * 
 * AbstractUIntPixelImage result3 = po.negative(img2);//Compute Negative of the input image.
 * 
 * AbstractUIntPixelImage result4 = po.powerLaw(img2, gamma);//Apply gamma correction on input
 * image for specific 'gamma' value.
 * </pre>	
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 * 
 */
public final class PointOperator {

	/**
	 * This method will apply a shift scale operator on the pixel intensities
	 * of the discrete real image as represented by the Cell 'cell' and return
	 * the resultant image as a Cell.
	 * <p>
	 * The argument 'shift' and 'scale' specify respectively the shift and the
	 * scale parameter for this operation.
	 * <p>
	 * This operation first scale the pixel intensities of the given image by
	 * the 'scale' factor and subsequently add the shift factor 'shift' to it.
	 * <p>
	 * This operation can be summarised as following,
	 * <p>
	 * <i> Y = scale*X + shift,
	 * <p>
	 * where, 'Y' is the output intensity for the given input intensity 'X'.
	 * <p>
	 * 'scale' and 'shift' are the scale and shift factor respectively.</i>
	 * 
	 * @param Cell
	 *            cell
	 * @param float shift
	 * @param float scale
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell shiftScale(Cell cell, float shift, float scale) {
		Cell result = new Cell(cell.getRowCount(), cell.getColCount());

		for (int i = 0; i < result.getRowCount(); i++) {
			for (int j = 0; j < result.getColCount(); j++) {
				result.setElement(scale * cell.getElement(i, j) + shift, i, j);
			}
		}

		return cell;
	}

	/**
	 * This method will linearly map the pixel intensity range of the discrete
	 * real image as represented by the Cell 'cell' to the new intensity range
	 * as specified by the argument 'min' and 'max' and return the resultant
	 * image as a Cell.
	 * <p>
	 * The argument 'min' and 'max' specify the minimum and maximum limit of the
	 * new Intensity range respectively. The argument 'max' should be more than
	 * the argument 'min' else this method will throw an IllegalArgument
	 * Exception.
	 * <p>
	 * All the pixel intensities of the original image will be linearly mapped
	 * to the new range [min max].
	 * <p>
	 * This operation bounds the pixel intensities of an Image in the desired
	 * range.
	 * 
	 * @param Cell
	 *            cell
	 * @param float min
	 * @param float max
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell setRange(Cell cell, float min, float max)
			throws IllegalArgumentException {
		if (max <= min) {
			throw new IllegalArgumentException();
		}

		float[] minmax = f0(cell);

		Cell result = new Cell(cell.getRowCount(), cell.getColCount());

		if (minmax[1] == minmax[0]) {
			for (int i = 0; i < cell.getRowCount(); i++) {
				for (int j = 0; j < cell.getColCount(); j++) {
					result.setElement(max, i, j);
				}
			}
			return result;
		}

		float slope = (max - min) / (minmax[1] - minmax[0]);

		for (int i = 0; i < cell.getRowCount(); i++) {
			for (int j = 0; j < cell.getColCount(); j++) {
				float tmp = slope * (cell.getElement(i, j) - minmax[0]) + min;
				result.setElement(tmp, i, j);
			}
		}

		return result;
	}

	/**
	 * This method will modify the pixel intensities of the discrete real image
	 * as represented by the Cell 'cell', as per the Ramp transfer function as
	 * specified and return the resultant modified image as a Cell.
	 * <p>
	 * The argument 'slope' and 'intercept' respectively specify the slope and
	 * the y-axis intercept for the Ramp transfer function which acts as an
	 * intensity transfer function for this method.
	 * <p>
	 * For input intensity 'X', resultant intensity 'Y' is given by following
	 * formula, <i>Y =X*slope + intercept</i>
	 * 
	 * @param Cell
	 *            cell
	 * @param float slope
	 * @param float intercept
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell ramp(Cell cell, float slope, float intercept) {
		Cell result = new Cell(cell.getRowCount(), cell.getColCount());

		for (int i = 0; i < cell.getRowCount(); i++) {
			for (int j = 0; j < cell.getColCount(); j++) {
				result.setElement(slope * cell.getElement(i, j) + intercept, i,
						j);
			}
		}

		return result;
	}

	/**
	 * This method will apply the Power law operator (Gamma correction) on the
	 * pixel intensities of the discrete real image as represented by the Cell
	 * 'cell' and return the modified resultant image as a Cell.
	 * <p>
	 * The argument 'gamma' specify the variable of the Power law operator. A
	 * Power law operator has the basic form of,
	 * <p>
	 * <i>Y = I^r, </i>
	 * <p>
	 * where 'Y' is the output pixel intensity, 'I' is the input pixel intensity
	 * and 'r' is the gamma value.
	 * <p>
	 * Power law operator define here is applied on the normalised pixel
	 * intensities in order to keep the intensity range of the resultant image
	 * same as that of the input image.
	 * <p>
	 * This transform is carried out in following steps, <tt>
	 * <p>1. Normalised the pixel intensities of input image to the range of [0 1].
	 * <p>2. Raise the normalised pixel intensities to the power of 'gamma'.
	 * <p>3. Reset the gamma applied normalised pixel intensities linearly to the original pixel intensity range. 
	 * </tt>
	 * <p>
	 * Thus the pixel intensity range of the resultant image will be same as
	 * that of the original image, but pixel intensity distribution or histogram
	 * will be modified as per the power law transform.
	 * <p>
	 * The argument 'gamma' should not be less than 0 else this method will
	 * throw an IllegalArgument Exception.
	 * 
	 * @param Cell
	 *            cell
	 * @param float gamma
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell powerLaw(Cell cell, float gamma)throws IllegalArgumentException {
		if (gamma < 0) {
			throw new IllegalArgumentException();
		}

		float[] minmax = f0(cell);

		if (minmax[0] == minmax[1]) {
			return cell.clone();
		}

		Cell result = new Cell(cell.getRowCount(), cell.getColCount());
		float diff = minmax[1] - minmax[0];
		float Y,tmp;
		
		for (int i = 0; i < cell.getRowCount(); i++) 
		{
			for (int j = 0; j < cell.getColCount(); j++) 
			{
				tmp = (cell.getElement(i, j) - minmax[0])/(diff);

				Y = (float) Math.pow(tmp, gamma);

				result.setElement(Y * (diff) + minmax[0], i,j);
			}
		}

		return result;
	}

	/**
	 * This method will apply the Negative operator on the pixel intensities of
	 * the discrete real image as represented by Cell 'cell' and return the
	 * resultant negative image as a Cell.
	 * <p>
	 * Negative operator flips the intensity range of the original image such
	 * that the maximum intensity of the image is mapped to its minimum
	 * intensity and vice versa.
	 * <p>
	 * All the intermediate pixel intensities are also suitably modified using a
	 * linear mapping.
	 * <p>
	 * This operator interchanges the bright and dark areas in the original
	 * Image.
	 * 
	 * @param Cell
	 *            cell
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell negative(Cell cell) {
		float[] minmax = f0(cell);

		if (minmax[0] == minmax[1]) {
			return cell.clone();
		}

		Cell result = new Cell(cell.getRowCount(), cell.getColCount());
		float diff = minmax[1] - minmax[0];
		float tmp;
		
		for (int i = 0; i < cell.getRowCount(); i++)
		{
			for (int j = 0; j < cell.getColCount(); j++) 
			{
				tmp = (cell.getElement(i, j) - minmax[0])/(diff);

				result.setElement(tmp * (-diff) + minmax[1], i,	j);
			}
		}

		return result;
	}

	/**
	 * This method apply a contrast stretching on the pixel intensities of the
	 * discrete real image as represented by the Cell 'cell' and return the
	 * modified image as Cell.
	 * <p>
	 * The argument 'T1' and 'T2' specify the lower and the upper normalised
	 * threshold pixel intensity respectively for the contrast stretching
	 * operation else this method will throw an IllegalArgument Exception.
	 * <p>
	 * The arguments 'T1' and 'T2' should be in the range of [0,1], where '0'
	 * corresponds to the minimum pixel intensity value and '1' corresponds to
	 * the maximum pixel intensity value of the original image respectively.
	 * <p>
	 * The argument 'T2' should be more than 'T1' else this method will throw an
	 * IllegalArgument Exception.
	 * 
	 * @param Cell
	 *            cell
	 * @param float T1
	 * @param float T2
	 * @return Cell cell
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell contrastStretching(Cell cell, float T1, float T2)
			throws IllegalArgumentException {
		if (T1 > 1 || T2 > 1 || T1 < 0 || T2 < 0) {
			throw new IllegalArgumentException();
		}

		if (T2 <= T1) {
			throw new IllegalArgumentException();
		}

		float[] minmax = f0(cell);

		if (minmax[0] == minmax[1]) {
			return cell.clone();
		}

		Cell result = new Cell(cell.getRowCount(), cell.getColCount());
		float diff = minmax[1] - minmax[0];
		float tmp;
		float range = T2-T1;
		
		for (int i = 0; i < cell.getRowCount(); i++) {
			for (int j = 0; j < cell.getColCount(); j++) {
				// Normalized pixel intensity between 0 and 1.
				tmp = (cell.getElement(i, j) - minmax[0])/(diff);

				if (tmp <= T1) {
					tmp = 0;
				} else if (tmp >= T2) {
					tmp = 1;
				} else {
					tmp = (tmp - T1) / (range);
				}
				// Get back unnormalized intensity, based on the minimum and the
				// maximum
				// of Original image intensity.
				result.setElement(tmp * (diff) + minmax[0], i,j);
			}
		}

		return result;
	}

	/**
	 * This method will transform the pixel intensities of the discrete real
	 * image as represented by the {@link Cell} 'cell' to a new mean level and a
	 * new standard deviation as given by the arguments 'mean' and 'std'
	 * respectively. The resultant image will be return as a Cell.
	 * <p>
	 * The value of the argument 'std' should be more than '0' else this method
	 * will throw an IllegalArgument Exception.
	 * <p>
	 * This method redefine the contrast and the brightness level of the image.
	 * Here, brightness is defined as the average pixel intensity of the image
	 * and contrast is defined as the amount of variation in the pixel
	 * intensities of the image. Thus the arguments 'mean' and 'std' here
	 * redefine the new mean brightness and the new contrast level for the
	 * image.
	 * 
	 * @param Cell
	 *            cell
	 * @param float mean
	 * @param float std
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell contrastAndBrightness(Cell cell, float mean, float std)
			throws IllegalArgumentException {
		if (std <= 0)
			throw new IllegalArgumentException();
		else {
			try {

				CellStatistics stat = new CellStatistics();

				float cell_mean = stat.mean(cell);
				float cell_std = 0;
				float tmp;
				// Compute cell's standard deviation. Faster here.
				for (int i = 0; i < cell.getRowCount(); i++) {
					for (int j = 0; j < cell.getColCount(); j++) {
						tmp = (cell.getElement(i, j) - cell_mean);
						cell_std += (tmp * tmp);
					}
				}

				float nosElements = cell.getRowCount() * cell.getColCount();
				// Cell's standard deviation is here.
				cell_std = (float) Math.sqrt(cell_std / nosElements);

				Cell result = new Cell(cell.getRowCount(), cell.getColCount());

				// Take ratio of old standard deviation to new one. Use this to
				// reset the standard deviation of the resultant cell to new
				// standard deviation.
				float stdRatio = cell_std / std;

				for (int i = 0; i < cell.getRowCount(); i++) {
					for (int j = 0; j < cell.getColCount(); j++) {
						// Remove mean, reset to new standard deviation and add
						// new mean
						// to reset the new mean.
						tmp = ((cell.getElement(i, j) - cell_mean) / stdRatio)
								+ mean;
						result.setElement(tmp, i, j);
					}
				}

				return result;

			} catch (ArrayIndexOutOfBoundsException e) {
				throw new BugEncounterException();
			}

		}
	}

	
	/**
	 * This method will apply the Power law operator (Gamma correction) on the
	 * pixel intensities of the UInt PixelImage as represented by an
	 * {@link AbstractUIntPixelImage} 'img' and return the resultant UInt PixelImage
	 * as {@link AbstractUIntPixelImage}.
	 * <p>
	 * The argument 'gamma' specify the variable of the power law operator. A
	 * power law operator has the basic form of,
	 * <p>
	 * <i>Y = I^r, </i>
	 * <p>
	 * where 'Y' is the output pixel intensity, 'I' is the input pixel intensity
	 * and 'r' is the gamma value.
	 * <p>
	 * Power law operator define here is applied on the normalised pixel
	 * intensities in order to keep the Gray Scale intensity range or depth of
	 * the resultant image same as that of the input image.
	 * <p>
	 * This transform is carried out in following steps,
	 * <tt>1 Normalised the pixel intensities of input image to the range of [0 1].
	 * <p>2 Raise the normalised pixel intensities to the power of 'gamma'.
	 * <p>3 Reset the gamma applied normalised pixel intensities linearly to the original pixel intensity range. </tt>
	 * <p>
	 * Thus the pixel intensity range of the resultant image will be same as
	 * that of the original image, but pixel intensity distribution or histogram
	 * will be modified as per the power law transform.
	 * <p>
	 * The argument 'gamma' should not be less than 0 else this method will
	 * throw an IllegalArgument Exception.
	 * 
	 * @param AbstractUIntPixelImage
	 *            img
	 * @param float gamma
	 * @return AbstractUIntPixelImage
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public AbstractUIntPixelImage powerLaw(AbstractUIntPixelImage img,
			float gamma) {
		try {
			AbstractUIntPixelImage result = (AbstractUIntPixelImage) img
					.getEquivalentBlankImage();

			int pixel;
			float MAX = img.getMaxValidPixel();

			for (int i = 0; i < result.getHeight(); i++) {
				for (int j = 0; j < result.getWidth(); j++) {
					pixel = (int) (MAX * Math.pow((img.getPixel(i, j) / MAX),
							gamma));
					result.setPixel(pixel, i, j);
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
	 * This method will apply the Natural log operator on the pixel intensities
	 * of the UInt PixelImage as represented by an {@link AbstractUIntPixelImage} 'img'
	 * and return the resultant UInt PixelImage as an {@link AbstractUIntPixelImage}.
	 * <p>
	 * A Log operator has the basic form of,
	 * <p>
	 * <i>Y = log(1+I), </i>
	 * <p>
	 * where 'Y' is the output pixel intensity and 'I' is the input pixel
	 * intensity.
	 * <p>
	 * The gray scale intensity range or depth of the resultant image will be
	 * same as that of the original image, but pixel intensity distribution or
	 * histogram will be modified as per the Log Transform.
	 * 
	 * @param AbstractUIntPixelImage
	 *            img
	 * @return AbstractUIntPixelImage
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public AbstractUIntPixelImage logTransform(AbstractUIntPixelImage img) {
		try {
			AbstractUIntPixelImage result = (AbstractUIntPixelImage) img
					.getEquivalentBlankImage();

			int pixel;

			for (int i = 0; i < result.getHeight(); i++) {
				for (int j = 0; j < result.getWidth(); j++) {
					pixel = (int) Math.log(1 + img.getPixel(i, j));
					result.setPixel(pixel, i, j);
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
	 * This method will apply the Negative operator on the UInt PixelImage as
	 * represented by the {@link AbstractUIntPixelImage} 'img' and return the result as
	 * {@link AbstractUIntPixelImage}.
	 * <p>
	 * Negative of an UInt PixelImage will have flip pixel intensities for the
	 * original UInt PixelImage such that the highest valid pixel intensity of
	 * the UInt PixelImage is mapped to lowest valid pixel intensity of that
	 * UInt PixelImage and vice versa.
	 * <p>
	 * This operator define on UInt PixelImage with pixel intensity range of
	 * [low high] has form, Y = high - I. where, Y is the new pixel intensity. I is
	 * the original pixel intensity.
	 * <p>
	 * This operator interchanges the bright and dark areas of the UInt
	 * PixelImage.
	 * 
	 * @param AbstractUIntPixelImage
	 *            img
	 * @return {@link AbstractUIntPixelImage}
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public AbstractUIntPixelImage negative(AbstractUIntPixelImage img) {

		try {
			AbstractUIntPixelImage result = (AbstractUIntPixelImage) img
					.getEquivalentBlankImage();

			int MAX = img.getMaxValidPixel();

			for (int i = 0; i < result.getHeight(); i++) {
				for (int j = 0; j < result.getWidth(); j++) {
					result.setPixel(MAX - img.getPixel(i, j), i, j);
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
	 * This method define a Sigmoidal operator which define a sigmoid curve to map the 
	 * pixel intensities of the UInt PixelImage as represented by an {@link AbstractUIntPixelImage}
	 * 'img' and return the resultant UInt PixelImage as an {@link AbstractUIntPixelImage}.
	 * <p>
	 * The argument 'alpha' and 'beta' are the parameters of the sigmoid curve
	 * and define the required sigmoidal mapping.
	 * <p>
	 * The argument 'alpha' control the slope and direction of the sigmoid curve
	 * and should not be '0' else this method will throw an IllegalArgument
	 * Exception. Sigmoid curve with negative 'alpha' will be flip of the
	 * sigmoid curve with the same positive 'alpha' thus negating the resultant
	 * image.
	 * <p>
	 * The argument 'beta' specify the intensity around which the required
	 * sigmoid curve is centred. Argument 'beta' can take any valid value in the
	 * integer range.
	 * <p>
	 * This sigmoidal intensity transform operation can be illustrated as,
	 * <p>
	 * <i>OP = 1/(1 + exp(-(IP -beta)/alpha)),
	 * <p>
	 * where, OP is the mapped pixel intensity for input pixel intensity IP.
	 * <p>
	 * 'alpha' and 'beta' are the sigmoid curve parameter as explained.
	 * <p>
	 * 'exp' is the exponential function.</i>
	 * <p>
	 * The dynamic range of pixel intensities of the resultant image will be
	 * approximately [beta -3*alpha beta + 3*alpha] as intensity values lower
	 * than 'beta - 3*alpha' and intensity values more than 'beta + 3*alpha'
	 * become progressively mapped to the minimum and maximum pixel intensity
	 * range for the resultant UInt PixelImage.
	 * 
	 * @param AbstractUIntPixelImage
	 *            img
	 * @param float alpha
	 * @param int beta
	 * @return AbstractUIntPixelImage
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public AbstractUIntPixelImage sigmoidMap(AbstractUIntPixelImage img,
			float alpha, int beta)
			throws IllegalArgumentException {
		if (alpha == 0)
			throw new IllegalArgumentException();

		else {
			try {
				int MAX = img.getMaxValidPixel();

				AbstractUIntPixelImage result = (AbstractUIntPixelImage) img
						.getEquivalentBlankImage();

				for (int i = 0; i < result.getHeight(); i++) {
					for (int j = 0; j < result.getWidth(); j++) {
						int Iip = img.getPixel(i, j);
						double map = 1.0 / (1.0 + Math.exp(-(Iip - beta)
								/ alpha));
						int Iop = (int) (map * MAX);

						result.setPixel(Iop, i, j);
					}
				}

				return result;
			} catch (IllegalArgumentException e) {
				throw new BugEncounterException();
			}
		}
	}
	
	private float[] f0(Cell cell) {
		float max = -Float.MAX_VALUE;
		float min = Float.MAX_VALUE;

		for (int i = 0; i < cell.getRowCount(); i++)
		{
			for (int j = 0; j < cell.getColCount(); j++) 
			{
				float value = cell.getElement(i, j);
				
				if (value > max)
					max = value;
				if (value < min)
					min = value;
			}
		}

		return new float[] { min, max };
	}

}
