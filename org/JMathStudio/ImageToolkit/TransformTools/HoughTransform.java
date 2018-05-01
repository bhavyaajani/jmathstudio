package org.JMathStudio.ImageToolkit.TransformTools;

import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.PixelImageToolkit.BinaryPixelImage.BinaryPixelImage;

/**
 * This class define Hough transform operations on a discrete binary real image.
 * <p>
 * A discrete binary real image will be represented by a {@link BinaryPixelImage}
 * object.
 * <pre>Usage:
 * BinaryPixelImage img = BinaryPixelImage.importImage("path");//Import input image as
 * BinaryPixelImage.
 * 
 * HoughTransform ht = new HoughTransform();//Create an instance of HoughTransform.
 * 
 * Cell hough = ht.houghCircle(img, radius);//Search for circle of given radius within input
 * binary image.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 * 
 */
public final class HoughTransform {

	/**
	 * This method apply the Line Hough transform on the discrete binary real
	 * image as represented by the BinaryPixelImage 'img' for detecting the
	 * presence of set of lines as specified and return the result as a Cell.
	 * <p>
	 * The BinaryPixelImage 'img' here represents the discrete binary image,
	 * such that all true's corresponds to the 1's or foreground and all false's
	 * corresponds to the 0's or background.
	 * 
	 * <p>
	 * In this geometry each line is define by two parameters - angle with
	 * positive x-axis(@) and the translation from origin(l) for that given
	 * angle. Thus a line is define as all those point with co-ordinates (y,x)
	 * satisfying the following condition,
	 * <p>
	 * <i>l = y*Cos(@) + x*Sin(@),
	 * <p>
	 * (y,x) gives coordinate of the pixel with entire image centred at the
	 * origin.
	 * <p>
	 *'l' specify the translations from -(n-1)/2 to (n-1)/2 for 'n' number of
	 * translations.
	 * <p>
	 * '@' specify the 'm' equally spaced angles in the range of [0 PI).</i>
	 * 
	 * <p>
	 * The argument 'noTranslations' and 'angles' together define those set of
	 * lines whose presence in the given binary image will be identified by this
	 * Line Hough Transform.
	 * 
	 * <p>
	 * The argument 'angles' specify that many number of equally spaced angles
	 * in the range of [0 PI) starting from 0 angle. i.e. if 'angles' = 4, set
	 * of angles will be [0 45 90 135] degrees. The argument 'angles' should be
	 * more than 0 else this method will throw an IllegalArgument Exception.
	 * 
	 * <p>
	 * The arguments 'noTranslations' specify a set of lines containing this
	 * many number of line having translation from origin -(noTranslation-1)/2
	 * to (noTranslation-1)/2 including 0th origin position for each angle. So
	 * that we have 'noTranslation' number of lines in the set which are centred
	 * at origin. The argument 'noTranslations' should not be less than 3 else
	 * this method will throw an IllegalArgument Exception.
	 * 
	 * <p>
	 * Preferably argument 'noTranslations' should be odd. If argument
	 * 'noTranslations' is even this operation will be computed with
	 * 'noTranslations'-1 odd number of translations.
	 * 
	 * <p>
	 * Thus arguments 'noTranslations' and 'angles' fully specify all the
	 * required lines whose presence will be determined by this operations. For
	 * each angles position, 'nosTranslations' number of lines as described
	 * above will be identified. Thus total number of lines defined are
	 * 'noTranslations' * 'angles'.
	 * 
	 * <p>
	 * The result of the operation will be return as a Cell where row index
	 * corresponds to the equally spaced angles starting from 0 in increasing
	 * order as define by the argument 'angles' and column index corresponds to
	 * the translation spanning from -(noTranslations-1)/2 to
	 * (noTranslations-1)/2 in the increasing order. Each element in the return
	 * Cell gives the count of foreground pixel positions in the original binary
	 * image which falls on the line having angle and translation as defined by
	 * the corresponding row and column index respectively and gives objective
	 * probability of the presence of the that line in the given binary image.
	 * 
	 * @param BinaryPixelImage
	 *            img
	 * @param int noTranslations
	 * @param int angles
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell houghLine(BinaryPixelImage img, int noTranslations, int angles)
			throws IllegalArgumentException {
		noTranslations = (noTranslations - 1) / 2;

		if (noTranslations < 1 || angles < 1) {
			throw new IllegalArgumentException();
		}

		Cell result = new Cell(angles, noTranslations * 2 + 1);

		float cY = (img.getHeight() - 1.0f) / 2.0f;
		float cX = (img.getWidth() - 1.0f) / 2.0f;

		for (int a = 0; a < angles; a++) {
			// theta-> [0 to pi]'angles' number of equispaced angles.
			// Translations->-(nosTranslations-1)/2 to +(nosTranslations-1)/2.

			float theta = (float) (Math.PI * a / angles);
			float cosTheta = (float) Math.cos(theta);
			float sinTheta = (float) Math.sin(theta);

			for (int i = 0; i < img.getHeight(); i++) {
				float y = (i - cY) * cosTheta;

				for (int j = 0; j < img.getWidth(); j++) {
					if (img.getPixel(i, j) == true) {

						float dist = (float) (y + (j - cX) * sinTheta);
						int index = Math.round(dist);

						if (index >= -noTranslations && index <= noTranslations) {
							float tmp = result.getElement(a, noTranslations
									+ index) + 1;
							result.setElement(tmp, a, noTranslations + index);
						}
					}
				}

			}
		}

		return result;
	}

	/**
	 * This method apply the Circle Hough transform on the discrete binary real
	 * image as represented by the BinaryPixelImage 'img' for the detection of
	 * the presence of a Circle with specified radius and return the result as a
	 * Cell.
	 * <p>
	 * The BinaryPixelImage 'img' here represents the discrete binary image,
	 * such that all true's corresponds to the 1's or foreground and all false's
	 * corresponds to the 0's or background.
	 * 
	 * <p>
	 * The argument 'radius' specify the radius of the circle in interest. The
	 * argument 'radius' should be more than 0 and less than the maximum
	 * dimension of the given image else this method will throw an
	 * IllegalArgument Exception.
	 * 
	 * <p>
	 * For each pixel position in the original binary image, this operator will
	 * count the number of foreground pixel positions which falls on the
	 * circumference of the hypothetical circle with specified radius if it is
	 * centred at the given pixel position in question. Thus the count gives
	 * objective probability of the presence of the circle in interest at the
	 * given pixel position.
	 * 
	 * <p>
	 * This count for all pixel positions is return as a Cell. The dimension of
	 * the returned Cell will be similar to the dimension of the original
	 * BinaryPixelImage and value at each position gives objective evidence for
	 * the presence of a circle centred at the corresponding pixel position in
	 * the original image.
	 * 
	 * @param BinaryPixelImage
	 *            img
	 * @param int radius
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell houghCircle(BinaryPixelImage img, int radius)
			throws IllegalArgumentException {
		if (radius > img.getHeight() || radius > img.getWidth()) {
			throw new IllegalArgumentException();
		}

		if (radius <= 0) {
			throw new IllegalArgumentException();
		}

		int height = img.getHeight();
		int width = img.getWidth();

		Cell result = new Cell(height, width);

		// Algorithm:
		// Traverse through all the pixel positions in the image.
		// For each pixel position, count the number of 'trues' or foreground
		// pixel
		// in the image falling on the hypothetical circle of radius in question
		// centred around the given pixel position. The count thus provide
		// objective
		// evidence of the present of the circle with given radius centred at
		// that
		// pixel location.
		for (int y = 0; y < result.getRowCount(); y++) {
			for (int x = 0; x < result.getColCount(); x++) {
				// Traverse along the horizontal X direction along the pixel
				// position
				// (y,x) from x-radius to x+radius, with position 'x' at centre.
				for (int xr = x - radius; xr <= x + radius; xr++) {
					if (xr >= 0 && xr < width) {
						// For each X location locate the Y positions which
						// together
						// (Y,X) falls on the circle with given radius and
						// centred at
						// (y,x). Y = sqrt(radius^2 - X^2).
						int Y = (int) Math.round(Math.sqrt(radius * radius
								- (xr - x) * (xr - x)));

						// Well get two Y positions Y1 and Y2 for positions
						// above and below,
						// so that (Y1,X) & (Y2,X) falls on the circle centred
						// at (y,x).
						int y1 = y + Y;
						int y2 = y - Y;

						if (y1 >= 0 && y1 < height) {
							if (img.getPixel(y1, xr) == true) {
								float tmp = result.getElement(y, x) + 1;
								result.setElement(tmp, y, x);
							}
						}
						if (y2 >= 0 && y2 < height) {
							if (img.getPixel(y2, xr) == true) {
								float tmp = result.getElement(y, x) + 1;
								result.setElement(tmp, y, x);
							}
						}
					}
				}
			}
		}

		return result;
	}

}
