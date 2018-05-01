package org.JMathStudio.ImageToolkit.Utilities;

import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.DataStructure.Cell.CellMath;
import org.JMathStudio.Exceptions.IllegalArgumentException;
import org.JMathStudio.MathToolkit.Utilities.MathUtils;

/**
 * This class define various standard 2D discrete spatial kernels.
 * <p>
 * A discrete spatial kernel will be represented by a {@link Cell} object.
 * 
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 * 
 */
public final class KernelFactory {

	//Ensure no instances are made for utility classes.
	private KernelFactory(){}
	
	/**
	 * This method creates a Legendre Kernel with required specifications and
	 * return the same as a Cell.
	 * <p>
	 * The argument 'M' and 'N' specify the height and width of the required
	 * kernel respectively. The argument 'M' and 'N' should be more than 1 else
	 * this method will throw an IllegalArgument Exception.
	 * <p>
	 * The argument 'Lx' and 'Ly' respectively specify the order of the required
	 * spatial kernel along X and Y axis respectively. Thus the order of the
	 * created k will be
	 * <i>'Lx + Ly'</i>.
	 * <p>
	 * The argument 'Lx' and 'Ly' should not be negative else this method will
	 * throw an IllegalArgument Exception.
	 * <p>
	 * The legendre kernels of same dimensions but different order forms an
	 * orthonormal basis.
	 * 
	 * @param int M
	 * @param int N
	 * @param int Lx
	 * @param int Ly
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Cell legendreKernel(int M, int N, int Lx, int Ly)throws IllegalArgumentException {
		if (M < 2 || N < 2 || Lx < 0 || Ly < 0) {
			throw new IllegalArgumentException();
		}

		Cell basis = new Cell(M, N);
		float[] legPolyY = new float[basis.getRowCount()];

		float[] legPolyX = new float[basis.getColCount()];

		float slopeY = (float) (2.0 / (basis.getRowCount() - 1));
		for (int i = 0; i < basis.getRowCount(); i++) {
			float y = i * slopeY - 1;
			legPolyY[i] = MathUtils.legPoly(Ly, y);
		}

		float slopeX = (float) (2.0 / (basis.getColCount() - 1));
		for (int j = 0; j < basis.getColCount(); j++) {
			float x = j * slopeX - 1;
			legPolyX[j] = MathUtils.legPoly(Lx, x);
		}

		float normalization = 0;
		for (int i = 0; i < basis.getRowCount(); i++) {
			for (int j = 0; j < basis.getColCount(); j++) {
				basis.setElement(legPolyX[j] * legPolyY[i], i, j);
				normalization = normalization + basis.getElement(i, j)
						* basis.getElement(i, j);
			}
		}

		normalization = (float) Math.sqrt(normalization);

		return CellMath.linear(1.0f / normalization, 0, basis);

	}

	/**
	 * This method creates a Gaussian Kernel and return the same as a Cell.
	 * <p>
	 * The argument 'M' and 'N' specify the height and width of the required
	 * kernel respectively. The argument 'M' and 'N' should be more than 0 else
	 * this method will throw an IllegalArgument Exception.
	 * <p>
	 * The argument 'std' specify the standard deviation for the required
	 * gaussian kernel and should be more or equal to 0 else this method will
	 * throw an IllegalArgument Exception.
	 * <p>
	 * The hill or lobe of the gaussian kernel will be located at the centre of the resultant Cell.
	 * <p>The resultant gaussian kernel is based on a non-normalised gaussian function define
	 * over a 2D support with origin located at the centre of the support. The maximum amplitude value
	 * at the centre of the gaussian lobe will be '1'.
	 * <p>If one want to derive a normalised gaussian kernel one has to normalise the given
	 * kernel with appropriate normalisation factor. 
	 * 
	 * @param int M
	 * @param int N
	 * @param float std
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Cell gaussianKernel(int M, int N, float std)throws IllegalArgumentException {
		if (M < 1 || N < 1 || std <= 0) {
			throw new IllegalArgumentException();
		}

		Cell result = new Cell(M, N);
		double den = 2.0*std*std;
		float cY = (result.getRowCount()-1)/2.0f;
		float cX = (result.getColCount()-1)/2.0f;

		for (int i = 0; i < result.getRowCount(); i++) {
			for (int j = 0; j < result.getColCount(); j++) {
				float Y = (float) (i - cY);
				float X = (float) (j - cX);

				double tmp = (X * X + Y * Y) / (den);
				//result.setElement((float) (Math.exp(-tmp) / (2 * Math.PI * std * std)),i, j);
				result.setElement((float) (Math.exp(-tmp)),i, j);

			}
		}

		return result;
	}

	/**
	 * This method creates a Gabor Kernel with required specifications and
	 * return the same as a Cell.
	 * <p><i>
	 * A Gabor Kernel is defined as the impulse response of the 2D Gabor
	 * filter which is the scalar product of the 2D Harmonic Cosine function and
	 * a Gaussian function.</i>
	 * <p>
	 * Thus this method has arguments related to the definition/specification
	 * for the Cosine and Gaussian part of the resultant Kernel.
	 * <p>
	 * The argument 'M' and 'N' specify respectively the height and the width of
	 * the required kernel. The argument 'M' and 'N' should not be less than 1
	 * else this method will throw an IllegalArgument Exception.
	 * <p>
	 * The argument 'lamada' specify the spatial wavelength of the cosine
	 * function and should not be less than 1 else this method will throw an
	 * IllegalArgument Exception.
	 * <p>
	 * The argument 'theta' specify the orientation in degrees of the cosine
	 * function with respect to the normal to the parallel stripes of a gabor
	 * kernel and it should be in the range of [0 180] degree else this method
	 * will throw an IllegalArgument Exception.
	 * <p>
	 * The argument 'std' specify the spatial standard deviation of the gaussian
	 * function and it should not be less than 1 else this method will throw an
	 * IllegalArgument Exception.
	 * <p>
	 * The argument 'gamma' specify the spatial aspect ratio or ellipticity of
	 * the resultant gabor kernel.
	 * 
	 * @param int M
	 * @param int N
	 * @param int lamada
	 * @param int theta
	 * @param int std
	 * @param float gamma
	 * @return Cell
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Cell gaborKernel(int M, int N, int lamada, int theta, int std,
			float gamma) throws IllegalArgumentException {
		if (M < 1 || N < 1 || lamada < 1 || theta < 0 || theta > 180 || std < 1) {
			throw new IllegalArgumentException();
		}

		float angle = (float) ((theta / 180.0f) * Math.PI);
		
		Cell result = new Cell(M, N);

		float cY = (result.getRowCount()-1)/2.0f;
		float cX = (result.getColCount()-1)/2.0f;
		float den = 2*std*std;
		float gammaPower2 = gamma*gamma;
		float costheta = (float) Math.cos(angle);
		float sintheta = (float) Math.sin(angle);
		float pi = (float) (2*Math.PI);
		
		for (int i = 0; i < result.getRowCount(); i++) 
		{
			for (int j = 0; j < result.getColCount(); j++) 
			{
				float Y = i - cY;
				float X = j - cX;

				float x = (float) (Y * costheta + X * sintheta);
				float y = (float) (X * costheta - Y * sintheta);

				float cos = (float) Math.cos(pi*(x/lamada));
				float parameter = (x * x + gammaPower2 * y * y)/ (den);

				result.setElement((float) Math.exp(-parameter) * cos, i, j);
			}
		}

		return result;

	}
}
