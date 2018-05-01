package org.JMathStudio.DataStructure.Vector;

import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.DimensionMismatchException;
import org.JMathStudio.Exceptions.IllegalArgumentException;

/**
 * This class define several useful operations and manipulations applicable over
 * a {@link CVector}.
 * <pre>Usage:
 * Let 'a' be CVector object.
 * 
 * CVectorTools tool = new CVectorTools();//Create an instance of CVectorTools.
 * 
 * CVector flip = tool.flip(a);//Flip order of elements of input CVector.
 * 
 * CVector resize = tool.resize(a, length);//Resize dimension of the input CVector.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class CVectorTools {

	/**
	 * This method will resize the length or size of the CVector 'vector' as
	 * given by the argument 'N' by appropriately padding zeroes if 'vector'
	 * size is to be increased or by discarding appropriate number of end
	 * elements if 'vector' size is to be shorten.
	 * <p>
	 * The argument 'N' should be more than 0 else this method will throw an
	 * IllegalArgument Exception.
	 * 
	 * @param CVector
	 *            vector.
	 * @param int N.
	 * @return CVector
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public CVector resize(CVector vector, int N)
	throws IllegalArgumentException {
		if (N < 1)
			throw new IllegalArgumentException();

		float[] real = new float[N];
		float[] img = new float[N];

		int index = vector.length() < N ? vector.length() : N;

		for (int i = 0; i < index; i++){
			real[i] = vector.i2[i];
			img[i] = vector.i1[i];
		}

		try {
			return new CVector(real,img);
		} catch (DimensionMismatchException e) {
			throw new BugEncounterException();
		}
	}

	/**
	 * This method will Wrap the CVector 'vector' around its centre and return
	 * the wrapped CVector.
	 * <p>
	 * Wrap operation will cause a circular shift of the given CVector so that
	 * the first element of the CVector shifts to centre and extreme right
	 * elements wrap around to left of the CVector.
	 * <p>
	 * The length of the return Wrap CVector will be similar to the original
	 * CVector.
	 * 
	 * @param CVector
	 *            vector
	 * @return CVector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public CVector wrapCVector(CVector vector) {
		try {
			int l = vector.length();

			float[] real = new float[l];
			float[] img = new float[l];

			int shift = (l - 1) / 2;

			for (int i = 0; i < l; i++) {
				int x = (i + shift) % l;
				real[x] = vector.i2[i];
				img[x] = vector.i1[i];
			}

			return new CVector(real,img);

		} catch (ArrayIndexOutOfBoundsException e) {
			throw new BugEncounterException();
		} catch (DimensionMismatchException e) {
			throw new BugEncounterException();
		}
	}

	/**
	 * This method will flip the order of elements index position within the
	 * CVector 'vector' and return the resultant CVector.
	 * <p>
	 * Flip operation makes forward elements tailing elements and vice versa.
	 * 
	 * @param CVector
	 *            vector
	 * @return CVector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public CVector flip(CVector vector) {
		try{
			float[] real = vector.accessRealPart().accessVectorBuffer();
			float[] img = vector.accessImaginaryPart().accessVectorBuffer();

			int N = vector.length();
			CVector result = new CVector(N);

			float[] f_real = result.accessRealPart().accessVectorBuffer();
			float[] f_img = result.accessImaginaryPart().accessVectorBuffer();

			N = N-1;

			for (int i = 0; i <= N; i++) {
				f_real[i] = real[N-i];
				f_img[i] = img[N-i];
			}

			return result;
		}catch(IllegalArgumentException e){
			throw new BugEncounterException();
		}
	}

}
