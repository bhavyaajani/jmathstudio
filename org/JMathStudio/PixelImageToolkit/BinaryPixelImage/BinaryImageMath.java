package org.JMathStudio.PixelImageToolkit.BinaryPixelImage;

import org.JMathStudio.Exceptions.DimensionMismatchException;

/**
 * This class defines bit wise binary operations on a {@link BinaryPixelImage}.
 * 
 * <pre>Usage:
 * Let 'a' & 'b' be BinaryPixelImages with similar dimension.
 * 
 * BinaryPixelImage AND = BinaryImageMath.logicalAND(a, b);//Bit wise AND operation between binary images.
 * 
 * BinaryPixelImage NOR = BinaryImageMath.logicalNOR(a, b);//Bit wise NOR operation between binary images.
 *  
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 * 
 */
public final class BinaryImageMath {

	//Ensure no instances are created for utility class.
	private BinaryImageMath(){}
	
	/**
	 * This method will perform a bit wise logical AND operation between the
	 * corresponding binary pixels of the {@link BinaryPixelImage}s 'a' and 'b' and 
	 * return the result as BinaryPixelImage.
	 * <p>Each pixel (R) of the resultant BinaryPixelImage represent the result of logical
	 * AND operation between the corresponding binary pixels of input images 'A' and 'B'
	 * respectively.
	 * <p><i>R = A & B</i>
	 * <p>The dimensions of input BinaryPixelImages 'a' and 'b' should be same else this 
	 * method will throw an DimensionMismatch Exception.
	 * 
	 * @param BinaryPixelImage a
	 * @param BinaryPixelImage b
	 * @return BinaryPixelImage
	 * @throws DimensionMismatchException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static BinaryPixelImage logicalAND(BinaryPixelImage a,BinaryPixelImage b)
			throws DimensionMismatchException {
		if (!a.hasSameDimensions(b)) {
			throw new DimensionMismatchException();
		}
		
		BinaryPixelImage result = (BinaryPixelImage) a.getEquivalentBlankImage();
		
		boolean[][] rbuf = result.accessPixelDataBuffer();
		boolean[][] abuf = a.accessPixelDataBuffer();
		boolean[][] bbuf = b.accessPixelDataBuffer();
		
		final int h = result.getHeight();
		final int w = result.getWidth();
		
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				rbuf[i][j] = abuf[i][j] & bbuf[i][j];
			}
		}

		return result;
	}

	/**
	 * This method will perform a bit wise logical NAND operation between the
	 * corresponding binary pixels of the {@link BinaryPixelImage}s 'a' and 'b' and 
	 * return the result as BinaryPixelImage.
	 * <p>Each pixel (R) of the resultant BinaryPixelImage represent the result of logical
	 * NAND operation between the corresponding binary pixels of input images 'A' and 'B'
	 * respectively.
	 * <p><i>R = !(A & B)</i>
	 * <p>The dimensions of input BinaryPixelImages 'a' and 'b' should be same else this 
	 * method will throw an DimensionMismatch Exception.
	 * 
	 * @param BinaryPixelImage a
	 * @param BinaryPixelImage b
	 * @return BinaryPixelImage
	 * @throws DimensionMismatchException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static BinaryPixelImage logicalNAND(BinaryPixelImage a,BinaryPixelImage b)
			throws DimensionMismatchException {
		if (!a.hasSameDimensions(b)) {
			throw new DimensionMismatchException();
		}
		
		BinaryPixelImage result = (BinaryPixelImage) a.getEquivalentBlankImage();
		
		boolean[][] rbuf = result.accessPixelDataBuffer();
		boolean[][] abuf = a.accessPixelDataBuffer();
		boolean[][] bbuf = b.accessPixelDataBuffer();
		
		final int h = result.getHeight();
		final int w = result.getWidth();
		
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				rbuf[i][j] = !(abuf[i][j] & bbuf[i][j]);
			}
		}

		return result;
	}

	/**
	 * This method will perform a bit wise logical OR operation between the
	 * corresponding binary pixels of the {@link BinaryPixelImage}s 'a' and 'b' and 
	 * return the result as BinaryPixelImage.
	 * <p>Each pixel (R) of the resultant BinaryPixelImage represent the result of logical
	 * OR operation between the corresponding binary pixels of input images 'A' and 'B'
	 * respectively.
	 * <p><i>R = A | B</i>
	 * <p>The dimensions of input BinaryPixelImages 'a' and 'b' should be same else this 
	 * method will throw an DimensionMismatch Exception.
	 * 
	 * @param BinaryPixelImage a
	 * @param BinaryPixelImage b
	 * @return BinaryPixelImage
	 * @throws DimensionMismatchException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static BinaryPixelImage logicalOR(BinaryPixelImage a,BinaryPixelImage b)
			throws DimensionMismatchException {
		if (!a.hasSameDimensions(b)) {
			throw new DimensionMismatchException();
		}
		
		BinaryPixelImage result = (BinaryPixelImage) a.getEquivalentBlankImage();
		
		boolean[][] rbuf = result.accessPixelDataBuffer();
		boolean[][] abuf = a.accessPixelDataBuffer();
		boolean[][] bbuf = b.accessPixelDataBuffer();
		
		final int h = result.getHeight();
		final int w = result.getWidth();
		
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				rbuf[i][j] = abuf[i][j] | bbuf[i][j];
			}
		}

		return result;
	}

	/**
	 * This method will perform a bit wise logical NOR operation between the
	 * corresponding binary pixels of the {@link BinaryPixelImage}s 'a' and 'b' and 
	 * return the result as BinaryPixelImage.
	 * <p>Each pixel (R) of the resultant BinaryPixelImage represent the result of logical
	 * NOR operation between the corresponding binary pixels of input images 'A' and 'B'
	 * respectively.
	 * <p><i>R = !(A | B)</i>
	 * <p>The dimensions of input BinaryPixelImages 'a' and 'b' should be same else this 
	 * method will throw an DimensionMismatch Exception.
	 * 
	 * @param BinaryPixelImage a
	 * @param BinaryPixelImage b
	 * @return BinaryPixelImage
	 * @throws DimensionMismatchException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static BinaryPixelImage logicalNOR(BinaryPixelImage a,BinaryPixelImage b)
			throws DimensionMismatchException {
		if (!a.hasSameDimensions(b)) {
			throw new DimensionMismatchException();
		}
		
		BinaryPixelImage result = (BinaryPixelImage) a.getEquivalentBlankImage();
		
		boolean[][] rbuf = result.accessPixelDataBuffer();
		boolean[][] abuf = a.accessPixelDataBuffer();
		boolean[][] bbuf = b.accessPixelDataBuffer();
		
		final int h = result.getHeight();
		final int w = result.getWidth();
		
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				rbuf[i][j] = !(abuf[i][j] | bbuf[i][j]);
			}
		}

		return result;
	}

	/**
	 * This method will perform a bit wise logical XOR operation between the
	 * corresponding binary pixels of the {@link BinaryPixelImage}s 'a' and 'b' and 
	 * return the result as BinaryPixelImage.
	 * <p>Each pixel (R) of the resultant BinaryPixelImage represent the result of logical
	 * XOR operation between the corresponding binary pixels of input images 'A' and 'B'
	 * respectively.
	 * <p><i>R = (A & !B) | (!A & B)</i>
	 * <p>The dimensions of input BinaryPixelImages 'a' and 'b' should be same else this 
	 * method will throw an DimensionMismatch Exception.
	 * 
	 * @param BinaryPixelImage a
	 * @param BinaryPixelImage b
	 * @return BinaryPixelImage
	 * @throws DimensionMismatchException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static BinaryPixelImage logicalXOR(BinaryPixelImage a,BinaryPixelImage b)
			throws DimensionMismatchException {
		if (!a.hasSameDimensions(b)) {
			throw new DimensionMismatchException();
		}
		
		BinaryPixelImage result = (BinaryPixelImage) a.getEquivalentBlankImage();
		
		boolean[][] rbuf = result.accessPixelDataBuffer();
		boolean[][] abuf = a.accessPixelDataBuffer();
		boolean[][] bbuf = b.accessPixelDataBuffer();
		
		final int h = result.getHeight();
		final int w = result.getWidth();
		
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				rbuf[i][j] = (abuf[i][j] & !bbuf[i][j])	| (!abuf[i][j] & bbuf[i][j]);
			}
		}

		return result;
	}

	/**
	 * This method will perform a bit wise logical XNOR operation between the
	 * corresponding binary pixels of the {@link BinaryPixelImage}s 'a' and 'b' and 
	 * return the result as BinaryPixelImage.
	 * <p>Each pixel (R) of the resultant BinaryPixelImage represent the result of logical
	 * XNOR operation between the corresponding binary pixels of input images 'A' and 'B'
	 * respectively.
	 * <p><i>R = (!A & !B) | (A & B)</i>
	 * <p>The dimensions of input BinaryPixelImages 'a' and 'b' should be same else this 
	 * method will throw an DimensionMismatch Exception.
	 * 
	 * @param BinaryPixelImage a
	 * @param BinaryPixelImage b
	 * @return BinaryPixelImage
	 * @throws DimensionMismatchException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static BinaryPixelImage logicalXNOR(BinaryPixelImage a,BinaryPixelImage b)
			throws DimensionMismatchException {
		if (!a.hasSameDimensions(b)) {
			throw new DimensionMismatchException();
		}
		
		BinaryPixelImage result = (BinaryPixelImage) a.getEquivalentBlankImage();
		
		boolean[][] rbuf = result.accessPixelDataBuffer();
		boolean[][] abuf = a.accessPixelDataBuffer();
		boolean[][] bbuf = b.accessPixelDataBuffer();
		
		final int h = result.getHeight();
		final int w = result.getWidth();
		
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				rbuf[i][j] = (!abuf[i][j] & !bbuf[i][j]) | (abuf[i][j] & bbuf[i][j]);
			}
		}

		return result;
	}
	
	/**
	 * This method will perform a bit wise logical substraction between the
	 * corresponding binary pixels of the {@link BinaryPixelImage}s 'a' and 'b' and 
	 * return the result as BinaryPixelImage.
	 * <p>Each pixel (R) of the resultant BinaryPixelImage represent the result of logical
	 * substraction between the corresponding binary pixels of input images 'A' and 'B'
	 * respectively.
	 * <p><i>R = (A & !B)</i>
	 * <p>The dimensions of input BinaryPixelImages 'a' and 'b' should be same else this 
	 * method will throw an DimensionMismatch Exception.
	 * 
	 * @param BinaryPixelImage a
	 * @param BinaryPixelImage b
	 * @return BinaryPixelImage
	 * @throws DimensionMismatchException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static BinaryPixelImage logicalSubstraction(BinaryPixelImage a,BinaryPixelImage b)
			throws DimensionMismatchException {
		if (!a.hasSameDimensions(b)) {
			throw new DimensionMismatchException();
		}
		
		BinaryPixelImage result = (BinaryPixelImage) a.getEquivalentBlankImage();
		
		boolean[][] rbuf = result.accessPixelDataBuffer();
		boolean[][] abuf = a.accessPixelDataBuffer();
		boolean[][] bbuf = b.accessPixelDataBuffer();
		
		final int h = result.getHeight();
		final int w = result.getWidth();
		
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				rbuf[i][j] = (abuf[i][j] & !bbuf[i][j]);
			}
		}

		return result;		
	}
}
