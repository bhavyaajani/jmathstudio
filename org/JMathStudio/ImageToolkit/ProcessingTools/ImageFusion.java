package org.JMathStudio.ImageToolkit.ProcessingTools;

import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.DimensionMismatchException;
import org.JMathStudio.Exceptions.IllegalArgumentException;
import org.JMathStudio.ImageToolkit.IntensityTools.ImageMerge;
import org.JMathStudio.ImageToolkit.SpatialTools.ImageGradient;
import org.JMathStudio.PixelImageToolkit.UIntPixelImage.AbstractUIntPixelImage;
import org.JMathStudio.ToolBoxes.WaveletToolBox.DWT2D;
import org.JMathStudio.ToolBoxes.WaveletToolBox.DWT2DCoeff;
import org.JMathStudio.ToolBoxes.WaveletToolBox.Wavelet;

/**
 * This class define various Image Fusion operations.
 * 
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 * 
 */

public final class ImageFusion {

	/**
	 * This method define a discrete wavelet transform based image fusion
	 * operation to fuse two discrete real images as represented by the
	 * {@link Cell}'s 'cell1','cell2' and return the resultant fused image as a
	 * Cell.
	 * <p>
	 * The Cells 'cell1' and 'cell2' representing the input images should have a
	 * similar dimensions else this method will throw a DimensionMismatch
	 * Exception.
	 * <p>
	 * Further, dimensions i.e. row and column count of the Cells 'cell1',
	 * 'cell2' should be more than '1' else this method will throw an
	 * IllegalArgument Exception.
	 * <p>
	 * The algorithm employed here for image fusion is as follows: <i>
	 * <p>
	 * 1> 2D DWT decomposition of both the input images.
	 * <p>
	 * 2> composition of 2D DWT coefficient matrix by selection of either of the
	 * corresponding 2D DWT sub-band (high frequency) detail coefficients of the
	 * input images depending upon the maximum gradient criteria(sobel operator
	 * is used to measure the gradients).
	 * <p>
	 * 3> fusion of Approximate coefficients(low frequency) of both the input
	 * images as per the weightage factors assign.
	 * <p>
	 * 4> Reconstruction of fused image by taking Inverse DWT of the 2D DWT
	 * coefficient matrix as obtained from steps 2 and 3. </i>
	 * <p>
	 * The argument 'k1' and 'k2' respectively specify the contribution of the
	 * input images in the final fused image. This define the weightage factor
	 * for the fusion of the Approximate coefficients. The value of the argument
	 * 'k1' and 'k2' should be in the range of [0,1], such that <i>k1 + k2 =
	 * 1</i> else this method will throw an IllegalArgument Exception.
	 * <p>
	 * The argument 'wavelet' here specify the Analysing and Synthesising
	 * Wavelet to be employed for this operation.
	 * <p>
	 * The argument 'level' specify the level of Wavelet decomposition to be
	 * carried out for given image fusion operation and it should be more than 0
	 * else this method will throw an IllegalArgument Exception.
	 * <p>
	 * 
	 * @param Cell
	 *            cell1
	 * @param float k1
	 * @param Cell
	 *            cell2
	 * @param float k2
	 * @param Wavelet
	 *            wavelet
	 * @param int level
	 * @return Cell
	 * @throws DimensionMismatchException
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell DWTFusion(Cell cell1, float k1, Cell cell2, float k2,
			Wavelet wavelet, int level) throws DimensionMismatchException,
			IllegalArgumentException {
		if (cell1.getRowCount() != cell2.getRowCount()
				|| cell1.getColCount() != cell2.getColCount())
			throw new DimensionMismatchException();

		if (k1 + k2 != 1 || k1 < 0 || k2 < 0 || k1 > 1 || k2 > 1)
			throw new IllegalArgumentException();

		DWT2D dwt2d = new DWT2D();

		DWT2DCoeff coeff1 = dwt2d.dwt(cell1, level, wavelet);
		DWT2DCoeff coeff2 = dwt2d.dwt(cell2, level, wavelet);

		Cell ll1 = coeff1.accessApproximate(level);
		Cell ll2 = coeff2.accessApproximate(level);

		Cell ll = new ImageMerge().combine(ll1, k1, ll2, k2);

		for (int i = 1; i <= level; i++) {
			Cell hh1 = coeff1.accessDiagonal(i);
			Cell hl1 = coeff1.accessHorizontal(i);
			Cell lh1 = coeff1.accessVertical(i);

			Cell hh2 = coeff2.accessDiagonal(i);
			Cell hl2 = coeff2.accessHorizontal(i);
			Cell lh2 = coeff2.accessVertical(i);

			Cell hh = f2(hh1, hh2);
			Cell hl = f2(hl1, hl2);
			Cell lh = f2(lh1, lh2);

			try {
				coeff1.assignDiagonal(hh, i);
				coeff1.assignHorizontal(hl, i);
				coeff1.assignVertical(lh, i);
			} catch (DimensionMismatchException e) {
				throw new BugEncounterException();
			}

		}

		coeff1.assignApproximate(ll, level);

		return dwt2d.idwt(coeff1, coeff1.accessAssociatedWavelet());

	}

	/**
	 * The method define an Image fusion operation which fuse the UInt
	 * PixelImages as represented by the {@link AbstractUIntPixelImage}'s 'img1'
	 * and 'img2' and return the resultant fused image as an UInt PixelImage.
	 * <p>
	 * The algorithm employed here fuse the images by merging the corresponding
	 * pixel intensities of the images as per the weight specified by the
	 * argument 'alpha' which is the fusion factor.
	 * <p>
	 * The fusion factor 'alpha' which specify the weight of the first image
	 * 'img1' in the resultant fused image should be within the range of [0 1]
	 * else this method will throw an IllegalArgument Exception.
	 * <p>
	 * Thus the operation define here is as follow:
	 * <p>
	 * <i>r(y,x) = img1(y,x)*alpha + (1 - alpha)*img2(y,x),
	 * <p>
	 * where, r(y,x) is the resultant image pixel intensity,
	 * <p>
	 * img1(y,x) and img2(y,x) are the pixel intensities of the given images
	 * respectively,
	 * <p>'alpha' is the fusion factor. </i>
	 * <p>
	 * The UIntmPixelImage's 'img1' and 'img2' to be fused should be of similar
	 * dimensions and depth else this method will throw a DimensionMismatch
	 * Exception and IllegalArgument Exception respectively.
	 * 
	 * @param AbstractUIntPixelImage
	 *            img1
	 * @param AbstractUIntPixelImage
	 *            img2
	 * @param float alpha
	 * @return AbstractUIntPixelImage
	 * @throws IllegalArgumentException
	 * @throws DimensionMismatchException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public AbstractUIntPixelImage linearFusion(AbstractUIntPixelImage img1,
			AbstractUIntPixelImage img2, float alpha)
			throws IllegalArgumentException, DimensionMismatchException {
		if (alpha < 0 || alpha > 1)
			throw new IllegalArgumentException();
		else if (img1.getDepth() != img2.getDepth())
			throw new IllegalArgumentException();
		else if (img1.getHeight() != img2.getHeight()
				|| img1.getWidth() != img2.getWidth())
			throw new DimensionMismatchException();
		else {
			try {

				int height = img1.getHeight();
				int width = img2.getWidth();

				AbstractUIntPixelImage result = (AbstractUIntPixelImage) img1
						.getEquivalentBlankImage();
				int pixel;

				for (int i = 0; i < height; i++) {
					for (int j = 0; j < width; j++) {
						pixel = Math.round(img2.getPixel(i, j) + alpha
								* (img1.getPixel(i, j) - img2.getPixel(i, j)));
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
	}

	private Cell f2(Cell hA, Cell hB)
			throws IllegalArgumentException {
		if (hA.getRowCount() != hB.getRowCount()
				|| hA.getColCount() != hB.getColCount())
			throw new IllegalArgumentException();

		ImageGradient sobel = ImageGradient.sobel();
		Cell GhA = sobel.getGradientMagnitude(hA);
		Cell GhB = sobel.getGradientMagnitude(hB);

		Cell result = new Cell(hA.getRowCount(), hB.getColCount());

		for (int i = 0; i < result.getRowCount(); i++) {
			for (int j = 0; j < result.getColCount(); j++) {
				if (GhA.getElement(i, j) >= GhB.getElement(i, j))
					result.setElement(hA.getElement(i, j), i, j);
				else
					result.setElement(hB.getElement(i, j), i, j);
			}
		}

		return result;

	}

}
