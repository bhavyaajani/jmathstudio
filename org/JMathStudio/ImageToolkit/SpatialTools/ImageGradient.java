package org.JMathStudio.ImageToolkit.SpatialTools;

import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.IllegalCellFormatException;
import org.JMathStudio.ImageToolkit.GeneralTools.Conv2DTools;
import org.JMathStudio.MathToolkit.Utilities.MathUtils;

/**
 * This class define an Image Gradient operator for deriving a 2D gradient of a discrete
 * real image.
 * <p>Gradient operator derive gradient or change in the pixel intensities in the image for every
 * pixel position based on the gradient mask. Gradient mask specify how the change in the pixel
 * intensity is to be computed for every pixel position. The resultant gradient image is obtained
 * by taking convolution of the image with the specified gradient mask.
 * <p>The gradient operator define here supports gradient calculation for two principal orthogonal
 * direction, horizontal and vertical only. This is based on the horizontal and vertical gradient mask.
 * Thus the resultant gradient image will have a gradient vector derived for each pixel position in the image
 * such that the magnitude of the gradient vector indicate the intensity of change and its direction
 * specify the direction with maximum change in pixel intensity.
 * <p>The magnitude of all such gradient vectors will give a gradient magnitude image and direction 
 * of all such gradient vector will give a gradient direction image.
 * <p>A discrete real image will be represented by a {@link Cell}.
 * <p>Also horizontal and vertical gradient mask will be represented by a {@link Cell}.
 * <p>This operation is widely used for edge detection in the image.
 * <pre>Usage:
 * Cell img = Cell.importImageAsCell("path");//Import input image as Cell.
 * 
 * ImageGradient sobel = ImageGradient.sobel();//Create an instance of sobel ImageGradient.
 * 
 * Cell magnitude = sobel.getGradientMagnitude(img);//Apply image gradient on input image and get
 * gradient magnitude at all pixels.
 * 
 * Cell direction = sobel.getGradientDirection(img);//Apply image gradient on input image and get
 * gradient direction at all pixels.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class ImageGradient {
	
	private Cell i9;
	private Cell i2;
	private Conv2DTools conv = new Conv2DTools();
	
	/**
	 * This will create an Image Gradient operator with the horizontal and vertical gradient mask
	 * as specified by the {@link Cell}'s 'horGradCell' and 'verGradCell' respectively.
	 * <p>Horizontal and vertical gradient mask specify the convolution kernel for deriving the
	 * horizontal and vertical gradient for each pixel position. This derived gradients form the horizontal
	 * and the vertical component of the gradient vector for each pixel position.
	 * <p>The arguments 'horGradCell' and 'verGradCell' are passed by reference and no deep copy of the same
	 * is made.
	 * 
	 * @param Cell horGradCell
	 * @param Cell verGradCell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public ImageGradient(Cell horGradCell,Cell verGradCell) 
	{
			this.i9 = horGradCell;
			this.i2 = verGradCell;
	}
	
	/**
	 * This method will return the gradient magnitude image of the discrete real image
	 * as represented by the {@link Cell} 'image' for the given Image Gradient operator.
	 * <p>Gradient magnitude image contains the magnitude of the gradient vector derived
	 * for each pixel position in the image by this gradient operator.
	 * <p>The resultant gradient magnitude image will be return as a {@link Cell} with
	 * dimension similar to that of the original image.
	 * @param Cell image
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell getGradientMagnitude(Cell image)
	{
		Cell gx = conv.linearConvSame(image, i9);
		Cell gy = conv.linearConvSame(image, i2);

		Cell result = new Cell(gx.getRowCount(), gx.getColCount());

		for (int i = 0; i < result.getRowCount(); i++) {
			for (int j = 0; j < result.getColCount(); j++) {
				float X = gx.getElement(i, j);
				float Y = gy.getElement(i, j);
				float tmp = (float) Math.sqrt((X * X + Y * Y));
				result.setElement(tmp, i, j);
			}
		}

		return result;
	}
	
	/**
	 * This method will return the gradient direction image of the discrete real image
	 * as represented by the {@link Cell} 'image' for the given Image Gradient operator.
	 * <p>Gradient direction image contains the direction of the gradient vector in radians, derived
	 * for each pixel position in the image by this gradient operator.
	 * <p>The resultant gradient direction image will be return as a {@link Cell} with
	 * dimension similar to that of the original image.
	 * @param Cell image
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell getGradientDirection(Cell image)
	{
		Cell gx = conv.linearConvSame(image, i9);
		Cell gy = conv.linearConvSame(image, i2);

		Cell result = new Cell(gx.getRowCount(), gx.getColCount());
		float constant = (float) (Math.PI/2);
		
		for (int i = 0; i < result.getRowCount(); i++) {
			for (int j = 0; j < result.getColCount(); j++) {
				if (gx.getElement(i, j) == 0) {
					result.setElement(constant, i, j);
				} else {
					result.setElement((float) Math.atan(gy.getElement(i, j)
							/ gx.getElement(i, j)), i, j);
				}
			}
		}

		return result;

	}
	
	/**
	 * This method will compute the gradient of the discrete real image as represented by the 
	 * {@link Cell} 'image' along horizontal direction for given Image Gradient operator.
	 * <p>Horizontal gradient image contains the horizontal 'x' component of the gradient vector 
	 * derived for each pixel position in the image by this gradient operator.
	 * <p>The resultant gradient image will be return as a {@link Cell} with dimension similar 
	 * to that of the original image.
	 * @param Cell image
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell getHorizontalGradient(Cell image)
	{
		return conv.linearConvSame(image, i9);
	}
	
	/**
	 * This method will compute the gradient of the discrete real image as represented by the 
	 * {@link Cell} 'image' along vertical direction for given Image Gradient operator.
	 * <p>Vertical gradient image contains the vertical 'y' component of the gradient vector 
	 * derived for each pixel position in the image by this gradient operator.
	 * <p>The resultant gradient image will be return as a {@link Cell} with dimension similar 
	 * to that of the original image.
	 * @param Cell image
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell getVerticalGradient(Cell image)
	{
		return conv.linearConvSame(image, i2);
	}
	
	/**
	 * This will return the horizontal gradient mask associated with this image gradient
	 * operator as a {@link Cell}.
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell accessHorizontalGradientMask()
	{
		return this.i9;
	}
	
	/**
	 * This will return the vertical gradient mask associated with this image gradient
	 * operator as a {@link Cell}.
	 * @return Cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell accessVerticalGradientMask()
	{
		return this.i2;
	}

	/**
	 * This will create and return a Sobel Image Gradient operator as an {@link ImageGradient}
	 * object.
	 * @return {@link ImageGradient}
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static ImageGradient sobel()
	{
		try{
		Cell hor = new Cell(new float[][] { { 1, 0, -1 }, { 2, 0, -2 },
				{ 1, 0, -1 } });
		Cell ver = new Cell(new float[][] { { 1, 2, 1 }, { 0, 0, 0 },
				{ -1, -2, -1 } });
		
		return new ImageGradient(hor,ver);
		}catch (IllegalCellFormatException e) {
			throw new BugEncounterException();
		}
	}
	
	/**
	 * This will create and return a Scharr Image Gradient operator as an {@link ImageGradient}
	 * object.
	 * @return {@link ImageGradient}
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static ImageGradient scharr()
	{
		try{
		Cell hor = new Cell(new float[][] { { 3, 0, -3 }, { 10, 0, -10 },
				{ 3, 0, -3 } });
		Cell ver = new Cell(new float[][] { { 3, 10, 3 }, { 0, 0, 0 },
				{ -3, -10, -3 } });
		
		return new ImageGradient(hor,ver);
		}catch (IllegalCellFormatException e) {
			throw new BugEncounterException();
		}
	}
	
	/**
	 * This will create and return a Prewitt Image Gradient operator as an {@link ImageGradient}
	 * object.
	 * @return {@link ImageGradient}
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static ImageGradient prewitt()
	{
		try{
		Cell hor = new Cell(new float[][] { { 1, 0, -1 }, { 1, 0, -1 },
				{ 1, 0, -1 } });
		Cell ver = new Cell(new float[][] { { 1, 1, 1 }, { 0, 0, 0 },
				{ -1, -1, -1 } });
		
		return new ImageGradient(hor,ver);
		}catch (IllegalCellFormatException e) {
			throw new BugEncounterException();
		}
	}
	
	/**
	 * This will create and return a Frei-Chen Image Gradient operator as an {@link ImageGradient}
	 * object.
	 * @return {@link ImageGradient}
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static ImageGradient frei_chen()
	{
		try{
			float sqrtOf2 = (float) MathUtils.SQRT_OF_TWO;
		Cell hor = new Cell(new float[][] { { 1, 0, -1 }, { sqrtOf2, 0, -sqrtOf2 },
				{ 1, 0, -1 } });
		Cell ver = new Cell(new float[][] { { 1, sqrtOf2, 1 }, { 0, 0, 0 },
				{ -1, -sqrtOf2, -1 } });
		
		return new ImageGradient(hor,ver);
		}catch (IllegalCellFormatException e) {
			throw new BugEncounterException();
		}
	}

	/**
	 * This will create and return a Roberts Image Gradient operator as an {@link ImageGradient}
	 * object.
	 * @return {@link ImageGradient}
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static ImageGradient roberts()
	{
		try{
		Cell hor = new Cell(new float[][] { { 1, 0, 0 },{0,0,0}, {0, 0, -1 } });
		Cell ver = new Cell(new float[][] { { 0, 0, -1 }, {0, 0, 0}, { 1, 0, 0 } });
		
		return new ImageGradient(hor,ver);
		}catch (IllegalCellFormatException e) {
			throw new BugEncounterException();
		}
	}


}
