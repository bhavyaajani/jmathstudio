package org.JMathStudio.ImageToolkit.GeneralTools;

import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.Exceptions.BugEncounterException;

/**
 * This class define various padding operations to pad a discrete real image.
 * <p> A discrete real image will be represented by a {@link Cell} object.
 * <pre>Usage:
 * Cell img = Cell.importImageAsCell("path");//Import input image as Cell.
 * 
 * ImagePadder ip = new ImagePadder();//Create an instance of ImagePadder.
 * 
 * int verPadSize = 3;//Depth of padding along input image boundary.
 * int horPadSize = 3;
 * 
 * Cell padd1 = ip.padCircular(img, verPadSize, horPadSize);//Pad input image
 * for specified depth using circular padding.
 * 
 * Cell padd2 = ip.padSymmetric(img, verPadSize, horPadSize);//Pad input image
 * for specified depth using symmetric padding.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class ImagePadder {

	/**
	 * This method will pad the discrete real image as represented by the Cell
	 * 'img' with zeroes along its horizontal and vertical dimension and return
	 * the resultant padded image as a Cell.
	 * <p>
	 * The arguments 'verpadSize' and 'horPadSize' specify the amount of padding
	 * along the vertical and the horizontal dimension of the image. The image
	 * is padded along both the direction per dimension by the specified padding
	 * amount for that dimension.
	 * <p>
	 * The arguments 'verPadSize' and 'horPadSize' should not be negative else
	 * this method will throw an IllegalArgument Exception.
	 * <p>
	 * The resultant image will have the dimension increased by the amount of
	 * 2*(pad amount along that dimension).
	 * 
	 * @param Cell
	 *            img
	 * @param int verPadSize
	 * @param int horPadSize
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell padZeroes(Cell img, int verPadSize, int horPadSize)
			throws IllegalArgumentException {
		if (horPadSize < 0 | verPadSize < 0)
			throw new IllegalArgumentException();
		else {
			try {
				Cell result = new Cell(img.getRowCount() + 2 * verPadSize, img
						.getColCount()
						+ 2 * horPadSize);

				for (int i = 0; i < img.getRowCount(); i++) {
					for (int j = 0; j < img.getColCount(); j++) {
						result.setElement(img.getElement(i, j), i + verPadSize,
								j + horPadSize);
					}
				}

				return result;
			} catch (ArrayIndexOutOfBoundsException e) {
				throw new BugEncounterException();
			}
		}
	}

	/**
	 * This method will pad the discrete real image as represented by the Cell
	 * 'img' by replicating the boundary/edge element of the image, along its
	 * horizontal and vertical dimension and return the resultant padded image
	 * as a Cell.
	 * <p>
	 * The arguments 'verpadSize' and 'horPadSize' specify the amount of padding
	 * along the vertical and the horizontal dimension of the image. The image
	 * is padded along both the direction per dimension by the specified padding
	 * amount for that dimension.
	 * <p>
	 * The arguments 'verPadSize' and 'horPadSize' should not be negative else
	 * this method will throw an IllegalArgument Exception.
	 * <p>
	 * The resultant image will have the dimension increased by the amount of
	 * 2*(pad amount along that dimension).
	 * 
	 * @param Cell
	 *            img
	 * @param int verPadSize
	 * @param int horPadSize
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell padReplicate(Cell img, int verPadSize, int horPadSize)
			throws IllegalArgumentException {
		if (horPadSize < 0 | verPadSize < 0)
			throw new IllegalArgumentException();
		else {
			try {
				Cell result = new Cell(img.getRowCount() + 2 * verPadSize, img
						.getColCount()
						+ 2 * horPadSize);

				int X = img.getColCount();
				int Y = img.getRowCount();

				for (int i = 0; i < result.getRowCount(); i++) {
					for (int j = 0; j < result.getColCount(); j++) {
						float tmp;

						if (i < verPadSize & j < horPadSize)
							tmp = img.getElement(0, 0);
						else if (i < verPadSize & j >= horPadSize + X)
							tmp = img.getElement(0, X - 1);
						else if (i >= verPadSize + Y & j < horPadSize)
							tmp = img.getElement(Y - 1, 0);
						else if (i >= verPadSize + Y & j >= horPadSize + X)
							tmp = img.getElement(Y - 1, X - 1);
						else if (i < verPadSize)
							tmp = img.getElement(0, j - horPadSize);
						else if (i >= verPadSize + Y)
							tmp = img.getElement(Y - 1, j - horPadSize);
						else if (j < horPadSize)
							tmp = img.getElement(i - verPadSize, 0);
						else if (j >= horPadSize + X)
							tmp = img.getElement(i - verPadSize, X - 1);
						else
							tmp = img
									.getElement(i - verPadSize, j - horPadSize);

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
	 * This method will pad the discrete real image as represented by the Cell
	 * 'img' by circular replication of the elements of the image along its
	 * horizontal and vertical dimension and return the resultant padded image
	 * as a Cell.
	 * <p>
	 * The arguments 'verpadSize' and 'horPadSize' specify the amount of padding
	 * along the vertical and the horizontal dimension of the image. The image
	 * is padded along both the direction per dimension by the specified padding
	 * amount for that dimension.
	 * <p>
	 * The arguments 'verPadSize' and 'horPadSize' should not be negative else
	 * this method will throw an IllegalArgument Exception.
	 * <p>
	 * The resultant image will have the dimension increased by the amount of
	 * 2*(pad amount along that dimension).
	 * 
	 * @param Cell
	 *            img
	 * @param int verPadSize
	 * @param int horPadSize
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell padCircular(Cell img, int verPadSize, int horPadSize)
			throws IllegalArgumentException {
		if (horPadSize < 0 || verPadSize < 0)
			throw new IllegalArgumentException();
		else {
			try {
				Cell result = new Cell(img.getRowCount() + 2 * verPadSize, img
						.getColCount()
						+ 2 * horPadSize);

				int X = img.getColCount();
				int Y = img.getRowCount();

				for (int i = 0; i < result.getRowCount(); i++) {
					for (int j = 0; j < result.getColCount(); j++) {
						// Do not change this. Difficult to understand
						// formulation.
						// But correct.
						int y = (i + (verPadSize) * (Y - 1)) % Y;
						int x = (j + (horPadSize) * (X - 1)) % X;

						result.setElement(img.getElement(y, x), i, j);
					}
				}

				return result;
			} catch (ArrayIndexOutOfBoundsException e) {
				throw new BugEncounterException();
			}
		}
	}

	/**
	 * This method will pad the discrete real image as represented by the Cell
	 * 'img' by symmetric circular replication of the elements of the image
	 * along its horizontal and vertical dimension and return the resultant
	 * padded image as a Cell.
	 * <p>
	 * The arguments 'verpadSize' and 'horPadSize' specify the amount of padding
	 * along the vertical and the horizontal dimension of the image. The image
	 * is padded along both the direction per dimension by the specified padding
	 * amount for that dimension.
	 * <p>
	 * The arguments 'verPadSize' and 'horPadSize' should not be negative else
	 * this method will throw an IllegalArgument Exception.
	 * <p>
	 * The resultant image will have the dimension increased by the amount of
	 * 2*(pad amount along that dimension).
	 * 
	 * @param Cell
	 *            img
	 * @param int verPadSize
	 * @param int horPadSize
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell padSymmetric(Cell img, int verPadSize, int horPadSize)
			throws IllegalArgumentException {
		if (horPadSize < 0 | verPadSize < 0)
			throw new IllegalArgumentException();
		else {
			try {
				Cell result = new Cell(img.getRowCount() + 2 * verPadSize, img
						.getColCount()
						+ 2 * horPadSize);

				int X = img.getColCount();
				int Y = img.getRowCount();

				int twiceX_1 = 2 * (X - 1);
				int twiceY_1 = 2 * (Y - 1);

				for (int i = 0; i < result.getRowCount(); i++) {
					for (int j = 0; j < result.getColCount(); j++) {
						int y = i - verPadSize;
						int x = j - horPadSize;

						y = Math.abs(y % (twiceY_1));
						x = Math.abs(x % (twiceX_1));

						if (y >= Y)
							y = twiceY_1 - y;
						if (x >= X)
							x = twiceX_1 - x;

						result.setElement(img.getElement(y, x), i, j);
					}
				}

				return result;
			} catch (ArrayIndexOutOfBoundsException e) {
				throw new BugEncounterException();
			}
		}
	}

}
