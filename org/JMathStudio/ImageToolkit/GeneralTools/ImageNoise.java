package org.JMathStudio.ImageToolkit.GeneralTools;

import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.MathToolkit.StatisticalTools.Distribution.ExponentialRV;
import org.JMathStudio.MathToolkit.StatisticalTools.Distribution.GaussianRV;
import org.JMathStudio.MathToolkit.StatisticalTools.Distribution.PoissonRV;
import org.JMathStudio.MathToolkit.StatisticalTools.Distribution.UniformRV;
import org.JMathStudio.PixelImageToolkit.UIntPixelImage.AbstractUIntPixelImage;
import org.JMathStudio.Exceptions.IllegalArgumentException;

/**
 * This class define various independent additive Noise model for adding Noise to a discrete
 * real image.
 * <p>
 * A discrete real image will be represented as a {@link Cell} or {@link AbstractUIntPixelImage} object.
 * <pre>Usage:
 * Cell img = Cell.importImageAsCell("path");//Import input image as Cell.
 * 
 * ImageNoise in = new ImageNoise();//Create an instance of ImageNoise.
 * 
 * Cell noise1 = in.gaussianNoise(img, mean, std);//Add gaussian noise with specified attributes
 * to input image.
 * 
 * Cell noise2 = in.poissonNoise(img, lambda);//Add poisson noise with specified attributes
 * to input image.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class ImageNoise {
	/**
	 * This will add an Uniform Noise in the interval [a,b) to the discrete real
	 * image as represented by the Cell 'cell' and return the noisy image as a
	 * Cell with similar dimensions.
	 * <p>
	 * The argument 'b' should be more than 'a' else this method will throw an
	 * IllegalArgument Exception.
	 * 
	 * @param Cell
	 *            cell
	 * @param float a
	 * @param float b
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell uniformNoise(Cell cell, float a, float b)
			throws IllegalArgumentException {
		UniformRV un = new UniformRV(a, b);

		Cell result = new Cell(cell.getRowCount(), cell.getColCount());

		for (int i = 0; i < result.getRowCount(); i++) {
			for (int j = 0; j < result.getColCount(); j++) {
				result.setElement(cell.getElement(i, j) + un.nextRandomValue(),
						i, j);
			}
		}

		return result;
	}

	/**
	 * This will add a Gaussian Noise with Mean and Standard deviation as given
	 * by the arguments 'mean' and 'std' respectively to the discrete real image
	 * as represented by the Cell 'cell' and return the noisy image as a Cell
	 * with similar dimensions.
	 * <p>
	 * The argument 'std' should be more than 0 else this method will throw an
	 * IllegalArgument Exception.
	 * 
	 * @param Cell
	 *            cell
	 * @param float mean
	 * @param float std
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell gaussianNoise(Cell cell, float mean, float std)
			throws IllegalArgumentException {
		GaussianRV gd = new GaussianRV(mean, std);

		Cell result = new Cell(cell.getRowCount(), cell.getColCount());

		for (int i = 0; i < result.getRowCount(); i++) {
			for (int j = 0; j < result.getColCount(); j++) {
				result.setElement(cell.getElement(i, j) + gd.nextRandomValue(),
						i, j);
			}
		}

		return result;

	}

	/**
	 * This will add a Poisson Noise with mean as given by the argument
	 * 'lambda', which gives the expected number of occurrences in an interval,
	 * to the discrete real image as represented by the Cell 'cell' and return
	 * the noisy image as a Cell with similar dimensions.
	 * <p>
	 * The argument 'lambda' should be more than 0 else this method will throw
	 * an IllegalArgument Exception.
	 * 
	 * @param Cell
	 *            cell
	 * @param float lambda
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell poissonNoise(Cell cell, float lambda)
			throws IllegalArgumentException {
		PoissonRV pn = new PoissonRV(lambda);

		Cell result = new Cell(cell.getRowCount(), cell.getColCount());

		for (int i = 0; i < result.getRowCount(); i++) {
			for (int j = 0; j < result.getColCount(); j++) {
				result.setElement(cell.getElement(i, j) + pn.nextRandomValue(),
						i, j);
			}
		}

		return result;

	}

	/**
	 * This will add an Exponential Noise with rate parameter 'lambda' to the
	 * discrete real image as represented by the Cell 'cell' and return the
	 * noisy image as a Cell with similar dimensions.
	 * <p>
	 * The argument 'lambda' should be more than 0 else this method will throw
	 * an IllegalArgument Exception.
	 * 
	 * @param Cell
	 *            cell
	 * @param float lambda
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Cell exponentialNoise(Cell cell, float lambda)
			throws IllegalArgumentException {
		ExponentialRV ed = new ExponentialRV(lambda);

		Cell result = new Cell(cell.getRowCount(), cell.getColCount());

		for (int i = 0; i < result.getRowCount(); i++) {
			for (int j = 0; j < result.getColCount(); j++) {
				result.setElement(cell.getElement(i, j) + ed.nextRandomValue(),
						i, j);
			}
		}

		return result;
	}

	/**
	 * This will add a Salt and Pepper noise to the UInt PixelImage as
	 * represented by an {@link AbstractUIntPixelImage} and return the noised
	 * UInt PixelImage as an {@link AbstractUIntPixelImage}.
	 * <p>
	 * The argument 'percent' specify the percentage of the total pixel
	 * positions of the given image to be corrupted by the noise. The argument
	 * 'percent' specifying the percentage of pixel positions to be corrupted
	 * should be in the range of (0 100) else this method will throw an
	 * IllegalArgument Exception.
	 * <p>
	 * Salt and Pepper noise will set the specify percentage of random pixel
	 * position intensities to either Maximum (Salt) or Minimum (Pepper)
	 * intensity value for the given UInt PixelImage in equal proportion.
	 * 
	 * @param AbstractUIntPixelImage
	 *            image
	 * @param float percent
	 * @return AbstractUIntPixelImage
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public AbstractUIntPixelImage saltAndPepperNoise(
			AbstractUIntPixelImage image, float percent)
			throws IllegalArgumentException {
		if (percent <= 0 || percent >= 100)
			throw new IllegalArgumentException();
		else {
			int height = image.getHeight();
			int width = image.getWidth();

			int N = (int) (height * width * percent / 100.0f);

			AbstractUIntPixelImage res = (AbstractUIntPixelImage) image.clone();

			for (int i = 0; i < N / 2; i++) {
				int x = (int) (Math.random() * width);
				int y = (int) (Math.random() * height);

				res.setPixel(image.getMaxValidPixel(), y, x);

				x = (int) (Math.random() * width);
				y = (int) (Math.random() * height);

				res.setPixel(image.getMinValidPixel(), y, x);
			}

			return res;
		}

	}
}
