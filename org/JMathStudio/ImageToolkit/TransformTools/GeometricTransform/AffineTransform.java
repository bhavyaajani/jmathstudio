package org.JMathStudio.ImageToolkit.TransformTools.GeometricTransform;

import org.JMathStudio.DataStructure.Cell.Cell;

/**
 * This class define an Affine transform.
 * <p>
 * Affine transform is a geometric transform characterised by a 3 X 3
 * transformational matrix. Such a transform maps the pixel co-ordinate of an
 * input image to a new co-ordinate as per the transformational matrix.
 * <p>
 * Such a transform is useful in geometric manipulation of the image kernel or
 * grid.
 * <p>
 * The transform follows the geometry where origin is at the centre of the image
 * and 'X' and 'Y' axis align with the image width and height respectively.
 * <pre>Usage:
 * Cell img = Cell.importImageAsCell("path");//Import input image as Cell.
 * 
 * AffineTransform at = new AffineTransform(1,0.3f,0,0.3f,1,0);//Create an instance of 
 * AffineTransform with specified affine parameters.
 * 
 * Cell result = at.transform(img);//Apply given affine transform on the input image.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class AffineTransform {

	private float i5;
	private float i0;
	private float i3;
	private float i8;
	private float i4;
	private float i2;

	/**
	 * This will create an Affine transform with the transformational matrix as
	 * specified by the given arguments.
	 * <p>
	 * The arguments specified here fully define the transform and maps the
	 * image co-ordinate to a new co-ordinate by following process:
	 * <p>
	 * <b>
	 * 
	 * <p>
	 * [ x'] = [ m00x + m01y + m02 ]
	 * <p>
	 * [ y'] = [ m10x + m11y + m12 ]
	 * 
	 *</b>
	 *<p>
	 * where (y,x) is the old pixel position and (y',x') is the new pixel
	 * position after transform is applied.
	 * 
	 * @param float m00
	 * @param float m01
	 * @param float m02
	 * @param float m10
	 * @param float m11
	 * @param float m12
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public AffineTransform(float m00, float m01, float m02, float m10, float m11,
			float m12) {
		this.i5 = m00;
		this.i0 = m01;
		this.i3 = m02;
		this.i8 = m10;
		this.i4 = m11;
		this.i2 = -m12;

	}

	/**
	 * This will apply the given affine transform on the discrete real image as
	 * represented by {@link Cell} 'cell' and return the resultant image as a
	 * Cell.
	 * <p>
	 * This transform shall map the pixel co-ordinate of the input image to a
	 * new co-ordinate as per the transformational matrix. The co-ordinate space
	 * is defined with origin at the centre of the input image.
	 * <p>
	 * The return image is of dimension similar to that of the input image and
	 * pixels falling outside the resultant image after transform is applied are
	 * discarded.
	 * 
	 * @param Cell
	 *            cell
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell transform(Cell cell) {
		int height = cell.getRowCount();
		int width = cell.getColCount();

		// Centre of original geometry i.e input image geometry.
		float cy = (height - 1) / 2.0f;
		float cx = (width - 1) / 2.0f;

		Cell result = new Cell(height, width);

		// Input and final geometry is same. Bounds will be such that the
		// centre of geometry coincide with centre of the image.

		float constant = i8 * i0 - i5 * i4;

		for (int i = 0; i < height; i++) {
			// float ytmp = (i-cy)* m00;
			// float xtmp = ytmp*m01;

			for (int j = 0; j < width; j++) {
				float xp = (j - cx);
				float yp = (i - cy);

				float X = (yp - i2) * i0 - (xp - i3) * i4;
				X = X / constant;

				float Y = (yp - i2) * i5 - (xp - i3) * i8;
				Y = -Y / constant;

				// The resultant pixel positions after shear should be within
				// the bounds of the final geometry.
				// Origin here is at the centre of the final resultant image.
				if (X >= -cx && X <= cx && Y >= -cy && Y <= cy) {
					// Bring indexing to start from '0'.
					X = (X + cx);
					Y = (Y + cy);

					int yl = (int) Math.floor(Y);
					int yh = (int) Math.ceil(Y);
					int xl = (int) Math.floor(X);
					int xh = (int) Math.ceil(X);

					// bilinear interpolation
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

						float value = ll * (yh - Y) * (xh - X) + lh * (yh - Y)
								* (X - xl) + hl * (Y - yl) * (xh - X) + hh
								* (Y - yl) * (X - xl);

						result.setElement(value, i, j);

					}
				}
			}
		}

		return result;

	}

	// Affine with full dimension : not working properly with translation.

	// public Cell transform1(Cell cell,boolean keepDimension)
	// {
	// int height = cell.getRowCount();
	// int width = cell.getColCount();
	//
	// //Centre of original geometry i.e input image geometry.
	// float cy = (height - 1) / 2.0f;
	// float cx = (width - 1) / 2.0f;
	//		
	// Cell result;
	// int fullHeight,fullWidth;
	//		
	// if(keepDimension)
	// {
	// float maxY = -Float.MAX_VALUE;
	// float maxX = -Float.MAX_VALUE;
	// float minY = Float.MAX_VALUE;
	// float minX = Float.MAX_VALUE;
	//		
	// for (int i = 0; i < height; i++)
	// {
	// float yp = i-cy;
	// //Ignore translation offset m12 and m02 during estimating the fully
	// extended
	// //bound of the image after this transform is applied.
	// //This shall get canceled during calculating the same,i.e maxX - minX
	// cancels
	// //common offset m02 and so on for 'Y' also.
	// float ytmp = m11*yp;
	// float xtmp = m01*yp;
	//			
	// for (int j = 0; j < width; j++)
	// {
	// float xp = j-cx;
	//					
	// float Y = ytmp + m10*xp;
	// float X = xtmp + m00*xp;
	//				
	// if(Y > maxY)
	// maxY = Y;
	// else if(Y < minY)
	// minY = Y;
	//				
	// if(X > maxX)
	// maxX = X;
	// else if(X < minX)
	// minX = X;
	// }
	// }
	//		
	// //As the translation offset was not considered dueing calculating
	// //the extended bound, add the same to get the full height and width
	// //of the fully extended image after this transform is applied.
	// fullHeight = (int) (Math.ceil(maxY - minY +0.5f)+ Math.abs(m12));
	// fullWidth = (int) (Math.ceil(maxX - minX +0.5f) + Math.abs(m02));
	//		
	// System.out.println(m12 + " " + m02+" "+fullHeight +" "+fullWidth);
	// result = new Cell(fullHeight,fullWidth);
	// }
	// else
	// {
	// fullHeight = height;
	// fullWidth = width;
	// result = new Cell(fullHeight,fullWidth);
	// }
	// //Input and final geometry is same. Bounds will be such that the
	// //centre of geometry coincide with centre of the image.
	//		
	// float newCy = (fullHeight-1 + m12)/2.0f;
	// float newCx = (fullWidth-1 + m02)/2.0f;
	//		
	// float constant = m10*m01 - m00*m11;
	//		
	// for (int i = 0; i < fullHeight; i++)
	// {
	// for (int j = 0; j < fullWidth; j++)
	// {
	// float xp = j-newCx;
	// float yp = i-newCy;
	//				
	// float X = (yp)*m01 - (xp)*m11;
	// X = X/constant;
	//				
	// float Y = (yp)*m00 - (xp)*m10;
	// Y = -Y/constant;
	//				
	//				
	// //The resultant pixel positions after shear should be within
	// //the bounds of the final geometry.
	// //Origin here is at the centre of the final resultant image.
	// if (X >= -cx && X <= cx && Y >= -cy && Y <= cy)
	// {
	// //Bring indexing to start from '0'.
	// X = (X + cx);
	// Y = (Y + cy);
	//
	// int yl = (int) Math.floor(Y);
	// int yh = (int) Math.ceil(Y);
	// int xl = (int) Math.floor(X);
	// int xh = (int) Math.ceil(X);
	//
	// // bilinear interpolation
	// if (xl == xh & yl == yh) {
	// result.setElement(cell.getElement(yl, xl), i,j);
	// } else if (yl == yh) {
	// int y = yl;
	// float value = cell.getElement(y, xh)*(X-xl) +
	// cell.getElement(y,xl)*(xh-X);
	// result.setElement(value, i,j);
	// } else if (xl == xh) {
	// int x = xl;
	// float value = cell.getElement(yl, x)*(yh-Y) + cell.getElement(yh,
	// x)*(Y-yl);
	// result.setElement(value, i,j);
	// } else {
	//						
	// float ll = cell.getElement(yl,xl);
	// float lh = cell.getElement(yl,xh);
	// float hl = cell.getElement(yh,xl);
	// float hh = cell.getElement(yh,xh);
	//						
	//						
	// float value = ll*(yh-Y)*(xh-X) + lh*(yh-Y)*(X-xl) + hl*(Y-yl)*(xh-X) +
	// hh*(Y-yl)*(X-xl);
	//						
	// result.setElement(value,i,j);
	//
	// }
	// }
	// }
	// }
	//
	// return result;
	//
	//		
	// }

}
