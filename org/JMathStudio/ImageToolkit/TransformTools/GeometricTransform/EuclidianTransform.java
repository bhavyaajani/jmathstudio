package org.JMathStudio.ImageToolkit.TransformTools.GeometricTransform;

import org.JMathStudio.DataStructure.Cell.Cell;

/**
 * This class define a set of Euclidian transforms on a discrete real image.
 * <p>
 * An euclidian transform is a geometric transform useful in manipulating the
 * kernel or grid of an image.
 * <p>
 * All the transforms define in this class follow the geometry where origin is
 * at the centre of the image and 'X' and 'Y' axis align with the image width
 * and height respectively.
 * <p>
 * A discrete real image will be represented by a {@link Cell} object.
 * <pre>Usage:
 * Cell img = Cell.importImageAsCell("path");//Import input image as Cell.
 * 
 * EuclidianTransform et = new EuclidianTransform();//Create an instance of EuclidianTransform.
 * 
 * Cell translate = et.translate(img, 20, 20, true);//Apply required euclidian translation on the
 * input image while maintaining the original dimensions of image.
 * 
 * Cell rotate = et.rotate(img, 45, false);//Apply required rotation on the input image and
 * preserve entire original image.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 * 
 */
public final class EuclidianTransform {

	/**
	 * This method define the Translation transform on the discrete real image
	 * as represented by the Cell 'cell' and return the resultant translated
	 * image as a Cell.
	 * <p>
	 * The argument 'ty' and 'tx' respectively specify the translation along Y
	 * and X axis. Rows and Columns of the Cell points in the direction of Y
	 * axis and X axis respectively with entire Cell located in the first
	 * quadrant.
	 * 
	 * <p>
	 * This operation shifts the Cell 'cell' elements along both the principal
	 * axis by the amount as specified by the arguments 'ty' and 'tx'. The sign
	 * of the arguments 'ty' and 'tx' will decide the directions along which
	 * Cell will be shifted.
	 * <p>
	 * The absolute value of the argument 'ty' and 'tx' should not exceed the
	 * image height and width respectively else this method will throw an
	 * IllegalArgument Exception.
	 * <p>
	 * If the argument 'keepDimension' is 'true' the resultant translated image
	 * will have the dimension similar to the image as represented by 'cell'.
	 * The portion of the translated image which falls outside the image
	 * dimension after translation shall be discarded.
	 * <p>
	 * If the argument 'keepDimension' is false the resultant translated image
	 * will be of dimension so as to encompass the entire translated image after
	 * translation.
	 * 
	 * @param Cell
	 *            cell
	 * @param int ty
	 * @param int tx
	 * @param boolean keepDimension
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell translate(Cell cell, int ty, int tx, boolean keepDimension)
			throws IllegalArgumentException {
		if (Math.abs(ty) > cell.getRowCount()
				| Math.abs(tx) > cell.getColCount()) {
			throw new IllegalArgumentException();
		}
		// image y axis index is reverse of geometric y axis.
		ty = -ty;

		Cell result;
		int height = cell.getRowCount();
		int width = cell.getColCount();

		// Original centre same as centre of image.
		// (cy,cx)
		float cy = (height - 1.0f) / 2.0f;
		float cx = (width - 1.0f) / 2.0f;

		// If the kernel dimension is to be similar to the input image.
		// Discard the outlier positions.
		if (keepDimension) {
			result = new Cell(height, width);
		}
		// Encompass the entire translated image within the expanded
		// kernel.
		else {
			result = new Cell(height + Math.abs(ty), width + Math.abs(tx));
		}

		// New centre of geometry same as centre of final size image.
		// (cY,cX)
		float cY = (result.getRowCount() - 1.0f) / 2.0f;
		float cX = (result.getColCount() - 1.0f) / 2.0f;

		// Offset depends upon the difference in the old and new centre
		// of geometry + translations.
		// Translations is 1/2 of the original as another half is provided
		// by the change in the centre of geomtry term, ie (cY -cy).
		float yOffset = ty / 2.0f + cY - cy;
		float xOffset = tx / 2.0f + cX - cx;

		for (int i = 0; i < height; i++) {
			int Y = (int) (i + yOffset);

			for (int j = 0; j < width; j++) {
				int X = (int) (j + xOffset);

				// Check needed for the same when kernel dimension is to be
				// similar
				// to the input image and need to discard the outlier positions.
				if (X >= 0 & X < result.getColCount() & Y >= 0
						& Y < result.getRowCount()) {
					result.setElement(cell.getElement(i, j), Y, X);
				}

			}
		}

		return result;
	}

	/**
	 * This method define the Shear transform on the discrete real image as
	 * represented by the Cell 'cell' and return the resultant sheared image as
	 * Cell.
	 * <p>
	 * The arguments 'SY' and 'SX' specify the shear along the Y and X axis
	 * respectively and should be in the range of (-1,1) else this method will
	 * throw an IllegalArgument Exception.
	 * <p>
	 * The Cell 'cell' is centred at the origin of the 2D geometry, with Cell
	 * rows and columns falling in the direction of Y axis and X axis
	 * respectively, before shear is applied.
	 * <p>
	 * If the argument 'keepDimension' is 'true' the resultant sheared image
	 * will have the dimension similar to the image as represented by 'cell'.
	 * The portion of the sheared image which falls outside the image dimension
	 * after shear shall be discarded.
	 * <p>
	 * If the argument 'keepDimension' is false the resultant sheared image will
	 * be of dimension so as to encompass the entire sheared image after shear.
	 * 
	 * @param Cell
	 *            cell
	 * @param float SY
	 * @param float SX
	 * @param boolean keepDimension
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell shear(Cell cell, float SY, float SX, boolean keepDimension)
			throws IllegalArgumentException {
		// Shear factor should be normalised within (-1 1)
		if (Math.abs(SY) >= 1 || Math.abs(SX) >= 1)
			throw new IllegalArgumentException();
		else {
			// If both shear is 0, return original image clone.
			if (SX == 0 && SY == 0)
				return cell.clone();

			int height = cell.getRowCount();
			int width = cell.getColCount();

			// Centre of original geometry i.e input image geometry.
			float cy = (height - 1) / 2.0f;
			float cx = (width - 1) / 2.0f;

			// Obtain the bounds of the resultant image after forward transform.

			float maxX;
			float maxY;
			float minX;
			float minY;

			// Height and width of the resultant image. By default that of
			// original image.
			int fullHeight = height;
			int fullWidth = width;

			if (keepDimension) {
				// Keep the resultant image dimension as that of the original
				// image.
				// Bounds will be such that the centre of geometry coincide with
				// centre of the image.
				maxX = cx;
				maxY = cy;
				minX = -cx;
				minY = -cy;

			} else {
				// If the resultant image has to have the full coverage or bound
				// after transform
				// compute the bounds.
				maxX = -Float.MAX_VALUE;
				maxY = -Float.MAX_VALUE;
				
				minX = Float.MAX_VALUE;
				minY = Float.MAX_VALUE;

				// Apply forward transform to compute the bounds.
				for (int i = 0; i < height; i++) {
					float yp = (i - cy);
					float ys = yp * SX;

					for (int j = 0; j < width; j++) {
						float xp = j - cx;
						float X = (float) (xp + ys);
						float Y = (float) (yp + xp * SY);

						if (X < minX)
							minX = X;
						if (Y < minY)
							minY = Y;
						if (X > maxX)
							maxX = X;
						if (Y > maxY)
							maxY = Y;
					}
				}

				// maxY = (float) Math.ceil(maxY);
				// minY = (float) Math.floor(minY);
				// maxX = (float) Math.ceil(maxX);
				// minX = (float) Math.floor(minX);

				// From the computed bound get the resultant image dimensions.
				// Transform the geometrical co-ordinates for resultant image to
				// index space starting from '0'.
				fullHeight = (int) Math.ceil(maxY - minY + 0.5f);
				fullWidth = (int) Math.ceil(maxX - minX + 0.5f);

			}

			Cell result = new Cell(fullHeight, fullWidth);

			// Centre of geometry of the resultant image.
			float cY = (fullHeight - 1) / 2.0f;
			float cX = (fullWidth - 1) / 2.0f;

			// Now we have resultatn image with centre at cY and cX.
			// For each co-ordinate in the resultant image kernel, reverse
			// transform the
			// same to the co-ordinate in the original image geometry.
			// Interpolate the value at that position and put the same in the
			// resultant image.

			// Constant for reverse mapping.
			// Based on the formula for reverse mapping the co-ordinates for
			// shear transform.
			float reverseMappingConstant = 1 - SX * SY;

			for (int i = 0; i < fullHeight; i++) {
				// Transform the index space of resultant image to geometrical
				// co-ordinates
				// with origin at the centre.
				float yp = (i - cY);

				// Apply reverse mapping formula.
				float ys = SX * yp;
				for (int j = 0; j < fullWidth; j++) {
					float xp = j - cX;

					// X and Y are reverse mapped co-ordinates in to original
					// image geometry.
					float X = (xp - ys) / reverseMappingConstant;
					float Y = (yp - SY * xp) / reverseMappingConstant;

					// X and Y should be within the bounds of the original image
					// geometry.
					if (X >= -cx & X <= cx & Y >= -cy & Y <= cy) {
						// Bring indexing to start from '0' for original image
						// indes space.
						X = (X + cx);
						Y = (Y + cy);

						int yl = (int) Math.floor(Y);
						int yh = (int) Math.ceil(Y);
						int xl = (int) Math.floor(X);
						int xh = (int) Math.ceil(X);

						// bilinear interpolation from original image to
						// resultant image.
						if (xl == xh & yl == yh) {
							result.setElement(cell.getElement(yl, xl), i, j);
						} else if (yl == yh) {
							int y = yl;
							float value = cell.getElement(y, xh) * (X - xl)
									+ cell.getElement(y, xl) * (xh - X);
							result.setElement(value, i, j);
						} else if (xl == xh) {
							int x = xl;
							float value = cell.getElement(yl, x) * (yh - Y)
									+ cell.getElement(yh, x) * (Y - yl);
							result.setElement(value, i, j);
						} else {

							float ll = cell.getElement(yl, xl);
							float lh = cell.getElement(yl, xh);
							float hl = cell.getElement(yh, xl);
							float hh = cell.getElement(yh, xh);

							float value = ll * (yh - Y) * (xh - X) + lh
									* (yh - Y) * (X - xl) + hl * (Y - yl)
									* (xh - X) + hh * (Y - yl) * (X - xl);

							result.setElement(value, i, j);

						}
					}
				}
			}

			return result;
		}

	}

	/**
	 * This method define the Rotation transform on the discrete real image as
	 * represented by the Cell 'cell' and return the resultant rotated image as
	 * a Cell.
	 * <p>
	 * The Cell 'cell' is rotated in the counter clockwise direction by the
	 * angle in degrees as specified by the argument 'theta'. The argument
	 * 'theta' should be in the range of [0,360] else this method will throw an
	 * IllegalArgument Exception.
	 * <p>
	 * The Cell 'cell' is centred at the origin of the 2D geometry before
	 * rotation is applied. Thus this method will perform a rotation along the
	 * origin.
	 * <p>
	 * If the argument 'keepDimension' is 'true' the resultant rotated image
	 * will have the dimension similar to the image as represented by 'cell'.
	 * The portion of the rotated image which falls outside the image dimension
	 * after rotation shall be discarded.
	 * <p>
	 * If the argument 'keepDimension' is false the resultant rotated image will
	 * be of dimension so as to encompass the entire rotated image after
	 * rotation.
	 * 
	 * @param Cell
	 *            cell
	 * @param float theta
	 * @param boolean keepDimension
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell rotate(Cell cell, float theta, boolean keepDimension)
			throws IllegalArgumentException {
		if (theta < 0 | theta > 360) {
			throw new IllegalArgumentException();
		}

		// For angles 0, 90, 180, 270 & 360 get result by directly setting the
		// pixels
		// to the new position.
		// Here in these cases the dimension of the final image will be same as
		// that of the input
		// image.
		if (theta == 0 || theta == 360) {
			// If angle is '0' degree, return the same image as no rotation.
			return cell.clone();
		}
		// Case when angle is 90 degree.
		else if (theta == 90) {
			int width = cell.getColCount() - 1;

			Cell result = new Cell(cell.getColCount(), cell.getRowCount());

			for (int i = 0; i < cell.getRowCount(); i++) {
				for (int j = 0; j < cell.getColCount(); j++) {
					result.setElement(cell.getElement(i, j), width - j, i);
				}
			}

			return result;
		}
		// Case when angle is 270 degree.
		else if (theta == 270) {
			int height = cell.getRowCount() - 1;

			Cell result = new Cell(cell.getColCount(), cell.getRowCount());

			for (int i = 0; i < cell.getRowCount(); i++) {
				for (int j = 0; j < cell.getColCount(); j++) {
					result.setElement(cell.getElement(i, j), j, height - i);
				}
			}

			return result;
		}
		// Case when angle is 180 degree.
		else if (theta == 180) {
			int height = cell.getRowCount() - 1;
			int width = cell.getColCount() - 1;

			Cell result = new Cell(cell.getRowCount(), cell.getColCount());

			for (int i = 0; i < cell.getRowCount(); i++) {
				for (int j = 0; j < cell.getColCount(); j++) {
					result.setElement(cell.getElement(i, j), height - i, width
							- j);
				}
			}

			return result;
		}
		// Case when angle is any other angle.
		else {
			// Convert angle to radian. Also (360-angle) to provide
			// anti-clockwise
			// rotation.
			float angle = (float) (2 * Math.PI * (360 - theta) / 360);
			float cosTheta = (float) Math.cos(angle);
			float sinTheta = (float) Math.sin(angle);

			int height = cell.getRowCount();
			int width = cell.getColCount();

			// Height and width of the resultant image.
			// Default is same as original image dimensions.
			int fullHeight = height;
			int fullWidth = width;

			// Centre of geometry for original image.
			float cy = (height - 1) / 2.0f;
			float cx = (width - 1) / 2.0f;

			if (!keepDimension) {
				// If to retain full dimensions

				// Bounds for resultant image with full extended dimensions
				// after
				// rotation.
				float maxX = -Float.MAX_VALUE;
				float maxY = -Float.MAX_VALUE;
				
				float minX = Float.MAX_VALUE;
				float minY = Float.MAX_VALUE;

				// Take four corner of the rectangle image in consideration.
				// Rotate the same to new position. Based on this new 'y' and
				// 'x' position estimate the bounds.

				// The four image corners have y and x co-ordinate when image
				// centre
				// is at origin as follows,
				float yMin = -cy;
				float yMax = cy;
				float xMin = -cx;
				float xMax = cx;

				// Estimate the new position of this four corner points after
				// rotation.
				// Apply forward rotation transform to this points.

				float xa = xMin * cosTheta - yMin * sinTheta;
				float ya = yMin * cosTheta + xMin * sinTheta;

				float xb = xMax * cosTheta - yMin * sinTheta;
				float yb = yMin * cosTheta + xMax * sinTheta;

				float xc = xMin * cosTheta - yMax * sinTheta;
				float yc = yMax * cosTheta + xMin * sinTheta;

				float xd = xMax * cosTheta - yMax * sinTheta;
				float yd = yMax * cosTheta + xMax * sinTheta;

				// From this new positions, estimate the minimum and maximum
				// 'y' and 'x' co-ordinate position. This shall be the
				// resultant image bounds.

				minX = (xa < xb) ? xa : xb;
				minX = (xc < minX) ? xc : minX;
				minX = (xd < minX) ? xd : minX;

				maxX = (xa > xb) ? xa : xb;
				maxX = (xc > maxX) ? xc : maxX;
				maxX = (xd > maxX) ? xd : maxX;

				minY = (ya < yb) ? ya : yb;
				minY = (yc < minY) ? yc : minY;
				minY = (yd < minY) ? yd : minY;

				maxY = (ya > yb) ? ya : yb;
				maxY = (yc > maxY) ? yc : maxY;
				maxY = (yd > maxY) ? yd : maxY;

				// Estimate that height and width of the resultant image from
				// bounds so as to
				// encompass the entire resultant image.
				fullHeight = (int) Math.ceil(maxY - minY + 0.5f);
				fullWidth = (int) Math.ceil(maxX - minX + 0.5f);

			}

			Cell result = new Cell(fullHeight, fullWidth);

			// Centre of geometry for resultant image.
			float cY = (result.getRowCount() - 1) / 2.0f;
			float cX = (result.getColCount() - 1) / 2.0f;

			for (int i = 0; i < fullHeight; i++) {
				// Convert index space to co-ordinate space for resultant image.
				float yp = (i - cY);
				float ySin = yp * sinTheta;
				float yCos = yp * cosTheta;
				for (int j = 0; j < fullWidth; j++) {
					// Convert index space to co-ordinate space for resultant
					// image.
					float xp = (j - cX);

					// Apply reverse rotation transform, for each pixel position
					// in resultant
					// image space estimate the co-ordinate in original image
					// space.
					float X = (float) (xp * cosTheta + ySin);
					float Y = (float) (yCos - xp * sinTheta);

					// Estimated co-ordinate in original image space should be
					// within the original
					// image space bounds.
					if (X >= -cx & X <= cx & Y >= -cy & Y <= cy) {
						// Transform estimated co-ordinate in original image
						// space to index space
						// for original image.
						X = (X + cx);
						Y = (Y + cy);

						// Get surrounding locations in the original image
						// space.
						int yl = (int) Math.floor(Y);
						int yh = (int) Math.ceil(Y);
						int xl = (int) Math.floor(X);
						int xh = (int) Math.ceil(X);

						// bilinear interpolation from original image and get
						// value for corresponding
						// pixel position in resultant image.
						if (xl == xh & yl == yh) {
							result.setElement(cell.getElement(yl, xl), i, j);
						} else if (yl == yh) {
							int y = yl;
							float value = cell.getElement(y, xh) * (X - xl)
									+ cell.getElement(y, xl) * (xh - X);
							result.setElement(value, i, j);
						} else if (xl == xh) {
							int x = xl;
							float value = cell.getElement(yl, x) * (yh - Y)
									+ cell.getElement(yh, x) * (Y - yl);
							result.setElement(value, i, j);
						} else {

							float ll = cell.getElement(yl, xl);
							float lh = cell.getElement(yl, xh);
							float hl = cell.getElement(yh, xl);
							float hh = cell.getElement(yh, xh);

							float value = ll * (yh - Y) * (xh - X) + lh
									* (yh - Y) * (X - xl) + hl * (Y - yl)
									* (xh - X) + hh * (Y - yl) * (X - xl);

							result.setElement(value, i, j);

						}
					}
				}
			}

			return result;

		}

	}

	/**
	 * This method define the Scale transform on the discrete real image as
	 * represented by the Cell 'cell' and return the resultant scaled image as
	 * Cell.
	 * <p>
	 * The arguments 'sy' and 'sx' specify the scale along the Y and X axis
	 * respectively and should be more than '0' else this method will throw an
	 * IllegalArgument Exception.
	 * <p>
	 * The value of the arguments 'sy' and/or 'sx' if more than '1', the
	 * resultant image will be scaled up by that factor in the corresponding
	 * dimension. Else if the value of the arguments 'sy' and/or 'sx' is less
	 * than '1' but more than '0', the resultant image will be scaled down by
	 * that factor in the corresponding dimension.
	 * <p>
	 * The Cell 'cell' is centred at the origin of the 2D geometry, with Cell
	 * rows and columns falling in the direction of Y axis and X axis
	 * respectively, before scale is applied.
	 * <p>
	 * If the argument 'keepDimension' is 'true' the resultant scaled image will
	 * have the dimension similar to the original image as represented by
	 * 'cell'. The portion of the scaled image if it falls outside the resultant
	 * image dimension after scale shall be discarded.
	 * <p>
	 * If the argument 'keepDimension' is false the resultant scaled image will
	 * be of dimension so as to encompass the entire scaled image. That is,
	 * height and width of the resultant image will be scaled by the factor of
	 * 'sy' and 'sx' respectively.
	 * 
	 * @param Cell
	 *            cell
	 * @param float sy
	 * @param float sx
	 * @param boolean keepDimension
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell scale(Cell cell, float sy, float sx, boolean keepDimension)
			throws IllegalArgumentException {
		if (sy <= 0 || sx <= 0)
			throw new IllegalArgumentException();
		else {
			// If both scale factor are '1' return the clone of the image.
			if (sy == 1 && sx == 1)
				return cell.clone();

			int height = cell.getRowCount();
			int width = cell.getColCount();

			// Height and Width of the resultant image.
			// Default is same as original image dimensions.
			int fullHeight = height;
			int fullWidth = width;

			// Centre of geometry for original image.
			float cy = (height - 1) / 2.0f;
			float cx = (width - 1) / 2.0f;

			if (!keepDimension) {
				// If to retain full dimensions.

				// Full image dimensions are scaled by the corresponding scale
				// factor.
				fullHeight = (int) Math.ceil(height * sy);
				fullWidth = (int) Math.ceil(width * sx);
			}

			Cell result = new Cell(fullHeight, fullWidth);

			// Centre of geometry for resultant image.
			float cY = (result.getRowCount() - 1) / 2.0f;
			float cX = (result.getColCount() - 1) / 2.0f;

			for (int i = 0; i < fullHeight; i++) {
				// Apply reverse scale transform; for each pixel position in
				// resultant
				// image space estimate the co-ordinate in original image space.

				// Convert index space to co-ordinate space for resultant image.
				float yp = (i - cY);

				// Reverse Y mapping.
				float ys = yp / sy;

				for (int j = 0; j < fullWidth; j++) {
					// Apply reverse scale transform; for each pixel position in
					// resultant
					// image space estimate the co-ordinate in original image
					// space.

					// Convert index space to co-ordinate space for resultant
					// image.
					float xp = (j - cX);

					// Reverse X mapping.
					float X = xp / sx;

					// Dummy assignment. Necessary for logic to work.
					// Don't modify has impact.
					float Y = ys;

					// Mapped co-ordinate in original image space should be
					// within the original
					// image space bounds.
					if (X >= -cx & X <= cx & Y >= -cy & Y <= cy) {
						// Transform estimated co-ordinate in original image
						// space to index space
						// for original image.
						X = (X + cx);
						Y = (Y + cy);

						// Get surrounding locations in the original image
						// space.
						int yl = (int) Math.floor(Y);
						int yh = (int) Math.ceil(Y);
						int xl = (int) Math.floor(X);
						int xh = (int) Math.ceil(X);

						// bilinear interpolation from original image and get
						// value for corresponding
						// pixel position in resultant image.
						if (xl == xh & yl == yh) {
							result.setElement(cell.getElement(yl, xl), i, j);
						} else if (yl == yh) {
							int y = yl;
							float value = cell.getElement(y, xh) * (X - xl)
									+ cell.getElement(y, xl) * (xh - X);
							result.setElement(value, i, j);
						} else if (xl == xh) {
							int x = xl;
							float value = cell.getElement(yl, x) * (yh - Y)
									+ cell.getElement(yh, x) * (Y - yl);
							result.setElement(value, i, j);
						} else {

							float ll = cell.getElement(yl, xl);
							float lh = cell.getElement(yl, xh);
							float hl = cell.getElement(yh, xl);
							float hh = cell.getElement(yh, xh);

							float value = ll * (yh - Y) * (xh - X) + lh
									* (yh - Y) * (X - xl) + hl * (Y - yl)
									* (xh - X) + hh * (Y - yl) * (X - xl);

							result.setElement(value, i, j);

						}
					}
				}
			}

			return result;

		}
	}
}

// Shear with0ut reverse mapping algorithm.
// public Cell shear(Cell cell, float SY, float SX,boolean keepDimension)
// throws org.JILab.Exceptions.IllegalArgumentException {
// if (Math.abs(SY) > 1 | Math.abs(SX) > 1)
// throw new org.JILab.Exceptions.IllegalArgumentException();
//
// int height = cell.getRowCount();
// int width = cell.getColCount();
//
// //Centre of original geometry i.e input image geometry.
// float cy = (height - 1) / 2.0f;
// float cx = (width - 1) / 2.0f;
//
// //Bounds on the final geometric coordinate of pixel locations i.e kernel.
// //coordinates are with respect to the centre of geometry of the
// //resultant image.
// float maxX;
// float maxY;
// float minX;
// float minY;
//
// Cell result;
//
// if(keepDimension)
// {
// //If keep the same dimension as input image.
// result = new Cell(height,width);
// //Input and final geometry is same. Bounds will be such that the
// //centre of geometry coincide with centre of the image.
// maxX = cx;
// maxY = cy;
// minX = -cx;
// minY = -cy;
// }
// else
// {
// //If the final resultant image is to encompass the entire input image
// //kernel after shear, compute the bounds of the final geometry in this case.
// //This implies get the min and max coordinate values for the resultant
// //pixel positions after shear.
// maxX = -Float.MAX_VALUE;
// maxY = -Float.MAX_VALUE;
// minX = Float.MAX_VALUE;
// minY = Float.MAX_VALUE;
//
// for (int i = 0; i < height; i++)
// {
// float yp = (i-cy);
// float ys = yp*SX;
//
// for (int j = 0; j < width; j++)
// {
// float xp = j-cx;
// float X = (float) (xp + ys);
// float Y = (float) (yp + xp*SY);
//
// if(X<minX)
// minX = X;
// if(Y<minY)
// minY = Y;
// if(X>maxX)
// maxX = X;
// if(Y>maxY)
// maxY = Y;
// }
// }
//
// //maxY = (float) Math.ceil(maxY);
// //minY = (float) Math.floor(minY);
// //maxX = (float) Math.ceil(maxX);
// //minX = (float) Math.floor(minX);
//
// //Compute the extended dimension such that it encompass all the pixels
// //of the input image after shear.
// int extHeight = (int) Math.ceil(maxY - minY + 1);
// int extWidth = (int) Math.ceil(maxX - minX + 1);
//
// result = new Cell(extHeight, extWidth);
// }
//
// //Centre of geometry of the final resultant image.
//
// float cY = (result.getRowCount()-1)/2.0f;
// float cX = (result.getColCount()-1)/2.0f;
//	
// for (int i = 0; i < height; i++)
// {
// //Shear is computed based on the input image geometry.
// //ie origin is at (cy,cx).
// float yp = (i-cy);
// float ys = yp*SX;
// for (int j = 0; j < width; j++)
// {
// float xp = j-cx;
// float X = (float) (xp + ys);
// float Y = (float) (yp + xp*SY);
//
// //The resultant pixel positions after shear should be within
// //the bounds of the final geometry.
// //Origin here is at the centre of the final resultant image.
// if (X >= -cX & X <= cX & Y >= -cY & Y <= cY)
// {
// //Bring indexing to start from '0'.
// X = (X - minX);
// Y = (Y - minY);
//
// int yl = (int) Math.floor(Y);
// int yh = (int) Math.ceil(Y);
// int xl = (int) Math.floor(X);
// int xh = (int) Math.ceil(X);
//
// float value = cell.getElement(i, j);
// // bilinear interpolation
// if (xl == xh & yl == yh) {
// result.setElement(value, yl, xl);
// } else if (yl == yh) {
// int y = yl;
// result.setElement(value * (X - xl), y, xh);
// float tmp = result.getElement(y, xl) + value * (xh - X);
// result.setElement(tmp, y, xl);
// } else if (xl == xh) {
// int x = xl;
// float tmp1 = result.getElement(yl, x) + value
// * (yh - Y);
// result.setElement(tmp1, yl, x);
//
// tmp1 = result.getElement(yh, x) + value * (Y - yl);
// result.setElement(tmp1, yh, x);
// } else {
// float Yl = value * (yh - Y);
// float Yh = value * (Y - yl);
//
// float tmp = result.getElement(yl, xl) + Yl * (xh - X);
// result.setElement(tmp, yl, xl);
//
// tmp = result.getElement(yl, xh) + Yl * (X - xl);
// result.setElement(tmp, yl, xh);
//
// tmp = result.getElement(yh, xl) + Yh * (xh - X);
// result.setElement(tmp, yh, xl);
//
// tmp = result.getElement(yh, xh) + Yh * (X - xl);
// result.setElement(tmp, yh, xh);
//
// }
// }
// }
// }
//	
// return result;
// }

// Rotation without reverse mapping

// public Cell rotate(Cell cell, float theta,boolean keepDimension) throws
// org.JILab.Exceptions.IllegalArgumentException {
// if (theta < 0 | theta > 360) {
// throw new org.JILab.Exceptions.IllegalArgumentException();
// }
//
// //For angles 0, 90, 180, 270 & 360 get result by directly setting the pixels
// //to the new position.
// //Here in these cases the dimension of the final image will be same as that
// of the input
// //image.
// if(theta == 0 || theta == 360)
// {
// //If angle is '0' degree, return the same image as no rotation.
// return cell.clone();
// }
// //Case when angle is 90 degree.
// else if(theta == 90)
// {
// int width = cell.getColCount()-1;
//
// Cell result = new Cell(cell.getColCount(),cell.getRowCount());
//
// for(int i=0;i<cell.getRowCount();i++)
// {
// for(int j=0;j<cell.getColCount();j++)
// {
// result.setElement(cell.getElement(i,j), width-j,i);
// }
// }
//
// return result;
// }
// //Case when angle is 270 degree.
// else if(theta == 270)
// {
// int height = cell.getRowCount()-1;
//
// Cell result = new Cell(cell.getColCount(),cell.getRowCount());
//
// for(int i=0;i<cell.getRowCount();i++)
// {
// for(int j=0;j<cell.getColCount();j++)
// {
// result.setElement(cell.getElement(i,j), j,height-i);
// }
// }
//
// return result;
// }
// //Case when angle is 180 degree.
// else if(theta == 180)
// {
// int height = cell.getRowCount()-1;
// int width = cell.getColCount()-1;
//
// Cell result = new Cell(cell.getRowCount(),cell.getColCount());
//
// for(int i=0;i<cell.getRowCount();i++)
// {
// for(int j=0;j<cell.getColCount();j++)
// {
// result.setElement(cell.getElement(i,j), height-i,width-j);
// }
// }
//
// return result;
// }
// //Case when angle is any other angle.
// else
// {
// //Convert angle to radian. Also (360-angle) to provide anti-clockwise
// //rotation.
// float angle = (float) (2 * Math.PI * (360 - theta) / 360);
// float cosTheta = (float) Math.cos(angle);
// float sinTheta = (float) Math.sin(angle);
//		
// int height = cell.getRowCount();
// int width = cell.getColCount();
//		
// //Centre of original geometry i.e. centre of input image.
// float cy = (height - 1) / 2.0f;
// float cx = (width - 1) / 2.0f;
//		
// //Bounds on the final geometric coordinate of pixel locations i.e kernel.
// //coordinates are with respect to the centre of geometry of the
// //resultant image.
// float maxX;
// float maxY;
// float minX;
// float minY;
//
// Cell result;
//
// if(keepDimension)
// {
// //If keep the same dimension as input image.
// result = new Cell(height,width);
// //Input and final geometry is same. Bounds will be such that the
// //centre of geometry coincide with centre of the image.
// maxX = cx;
// maxY = cy;
// minX = -cx;
// minY = -cy;
// }
// else
// {
// //If the final resultant image is to encompass the entire input image
// //kernel after rotation, compute the bounds of the final geometry in this
// case.
// //This implies get the min and max coordinate values for the resultant
// //pixel positions after rotation.
// maxX = -Float.MAX_VALUE;
// maxY = -Float.MAX_VALUE;
// minX = Float.MAX_VALUE;
// minY = Float.MAX_VALUE;
//
// for (int i = 0; i < height; i++)
// {
// float y = (i - cy);
// float ySin = y*sinTheta;
// float yCos = y*cosTheta;
// for (int j = 0; j < width; j++)
// {
// float x = (j - cx);
//
// float X = (float) (x*cosTheta - ySin);
// float Y = (float) (yCos + x*sinTheta);
//
// if(X<minX)
// minX = X;
// if(Y<minY)
// minY = Y;
// if(X>maxX)
// maxX = X;
// if(Y>maxY)
// maxY = Y;
//
// }
// }
//
// //maxY = (float) Math.ceil(maxY);
// //minY = (float) Math.floor(minY);
// //maxX = (float) Math.ceil(maxX);
// //minX = (float) Math.floor(minX);
//
// //Compute the extended dimension such that it encompass all the pixels
// //of the input image after rotation.
// int extHeight = (int) Math.ceil(maxY - minY + 1);
// int extWidth = (int) Math.ceil(maxX - minX + 1);
//
// result = new Cell(extHeight, extWidth);
// }
//
// //Centre of geometry of the final resultant image.
//
// float cY = (result.getRowCount()-1)/2.0f;
// float cX = (result.getColCount()-1)/2.0f;
//	
// for (int i = 0; i < height; i++)
// {
// //Rotation is computed based on the input image geometry.
// //ie origin is at (cy,cx).
// float y = (i - cy);
// float ySin = y*sinTheta;
// float yCos = y*cosTheta;
// for (int j = 0; j < width; j++)
// {
// float x = (j - cx);
// float X = (float) (x*cosTheta - ySin);
// float Y = (float) (yCos + x*sinTheta);
//
// //The resultant pixel positions after rotation should be within
// //the bounds of the final geometry.
// //Origin here is at the centre of the final resultant image.
// if (X >= -cX & X <= cX & Y >= -cY & Y <= cY)
// {
// //Bring indexing to start from '0'.
// X = (X - minX);
// Y = (Y - minY);
//					
// int yl = (int) Math.floor(Y);
// int yh = (int) Math.ceil(Y);
// int xl = (int) Math.floor(X);
// int xh = (int) Math.ceil(X);
//
// float value = cell.getElement(i, j);
// // biliner interpolation
// if (xl == xh && yl == yh) {
// result.setElement(value, yl, xl);
// } else if (yl == yh) {
// int yres = yl;
// result.setElement(value * (X - xl), yres, xh);
// float tmp = result.getElement(yres, xl) + value * (xh - X);
// result.setElement(tmp, yres, xl);
// } else if (xl == xh) {
// int xres = xl;
// float tmp1 = result.getElement(yl, xres) + value
// * (yh - Y);
// result.setElement(tmp1, yl, xres);
//
// tmp1 = result.getElement(yh, xres) + value * (Y - yl);
// result.setElement(tmp1, yh, xres);
// } else {
// float Yl = value * (yh - Y);
// float Yh = value * (Y - yl);
//
// float tmp = result.getElement(yl, xl) + Yl * (xh - X);
// result.setElement(tmp, yl, xl);
//
// tmp = result.getElement(yl, xh) + Yl * (X - xl);
// result.setElement(tmp, yl, xh);
//
// tmp = result.getElement(yh, xl) + Yh * (xh - X);
// result.setElement(tmp, yh, xl);
//
// tmp = result.getElement(yh, xh) + Yh * (X - xl);
// result.setElement(tmp, yh, xh);
//
// }
// }
// }
// }
//
// return result;
// }
// }