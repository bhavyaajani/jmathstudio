package org.JMathStudio.ImageToolkit.Utilities;

import org.JMathStudio.DataStructure.Cell.Cell;

/**
 * This class define various standard discrete real images.
 * <p>
 * A discrete real image will be represented by a {@link Cell} object.
 * 
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class ImageGenerator {
	
	//Ensure no instances are made for utility classes.
	private ImageGenerator(){}
	
	/**
	 * This method will create a discrete real image with random pixel values in
	 * the range of [0 1] and return the same as a Cell. The argument 'height'
	 * and 'width' here specify the height and width of the required random
	 * image respectively.
	 * <p>
	 * The argument 'height' and 'width' should be more than 0 else this method
	 * will throw an IllegalArgument Exception.
	 * 
	 * @param int height
	 * @param int width
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Cell random(int height, int width)
			throws IllegalArgumentException {
		if (height < 1 || width < 1) {
			throw new IllegalArgumentException();
		}
		Cell result = new Cell(height, width);

		for (int i = 0; i < result.getRowCount(); i++) {
			for (int j = 0; j < result.getColCount(); j++) {
				result.setElement((float) Math.random(), i, j);
			}
		}

		return result;
	}

	/**
	 * This method will create a discrete real image with all pixel values
	 * having the same uniform value as specified by the argument 'value' and
	 * return the same as a Cell.
	 * <p>
	 * The argument 'height' and 'width' specify the height and width of the
	 * required uniform image respectively.
	 * <p>
	 * The argument 'height' and 'width' should be more than 0 else this method
	 * will throw an IllegalArgument Exception.
	 * 
	 * @param int height
	 * @param int width
	 * @param float value
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Cell uniform(int height, int width, float value)
			throws IllegalArgumentException {
		if (height < 1 || width < 1) {
			throw new IllegalArgumentException();
		}

		if (value == 0)
			return new Cell(height, width);

		Cell result = new Cell(height, width);

		for (int i = 0; i < result.getRowCount(); i++) {
			for (int j = 0; j < result.getColCount(); j++) {
				result.setElement(value, i, j);
			}
		}

		return result;
	}

	/**
	 * This method will create a discrete real image whose raw pixel intensities
	 * has only two values either '1' or '0' following a chess board pattern and
	 * return the same as a Cell.
	 * <p>
	 * The argument 'height' and 'width' specify the height and width of the
	 * required image respectively. The argument 'height' and 'width' should be
	 * more than 0 else this method will throw an IllegalArgument Exception.
	 * <p>
	 * The argument 'size' specify the dimension of each square checker. The
	 * argument 'size' should be more than 0 else this method will throw an
	 * IllegalArgument Exception.
	 * 
	 * @param int height
	 * @param int width
	 * @param int size
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Cell checker(int height, int width, int size)
			throws IllegalArgumentException {
		if (height < 1 || width < 1 || size < 1)
			throw new IllegalArgumentException();

		Cell res = new Cell(height, width);

		for (int i = 0; i < res.getRowCount(); i++) {
			for (int j = 0; j < res.getColCount(); j++) {
				boolean rowCheck = false;
				boolean colCheck = false;

				if (!((i / size) % 2 == 0))
					rowCheck = true;
				if ((j / size) % 2 == 0)
					colCheck = true;

				if (rowCheck & !colCheck || !rowCheck & colCheck)
					res.setElement(1, i, j);
			}
		}

		return res;
	}

}
