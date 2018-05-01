package org.JMathStudio.DataStructure.Vector;

import org.JMathStudio.DataStructure.Complex;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.DimensionMismatchException;
import org.JMathStudio.Exceptions.DivideByZeroException;
import org.JMathStudio.Exceptions.IllegalArgumentException;

/**
 * This class define basic mathematical operations on a {@link CVector}.
 *  
 * <pre>Usage:
 * Let 'a' & 'b' be CVector objects with same length.
 * 
 * CVector c = CVectorMath.dotProduct(a, b);//Take product of corresponding elements of 
 * input CVectors.
 * 
 * CVector d = CVectorMath.subtract(a, b);//Subtract elements of CVector 'b' from 
 * corresponding element of CVector 'a'.
 * </pre> 
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 * 
 */
public final class CVectorMath {

	//Ensure no instances are made for utility classes.
	private CVectorMath(){}
	
	/**
	 * This method will perform an element by element complex addition operation
	 * between the corresponding complex elements of the {@link CVector}s 'vector1' and
	 * 'vector2' and return the result as a CVector.
	 * <p>
	 * The dimension of both the CVectors should be similar or else this method will
	 * throw an DimensionMismatch Exception.
	 * <p>
	 * Each complex element of the return CVector will be the result of the addition of 
	 * the corresponding elements of the given CVectors respectively.
	 * @param CVector
	 *            vector1
	 * @param CVector
	 *            vector2
	 * @return CVector
	 * @throws DimensionMismatchException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static CVector add(CVector vector1, CVector vector2)
	throws DimensionMismatchException {
		if (vector1.length() != vector2.length()) {
			throw new DimensionMismatchException();
		}

		int L = vector1.length();

		float[] real = new float[L];
		float[] img = new float[L];

		for (int i = 0; i < L; i++) {
			real[i] = vector1.i2[i] + vector2.i2[i];
			img[i] = vector1.i1[i] + vector2.i1[i];
		}

		return new CVector(real,img);
	}

	/**
	 * This method will perform an element by element complex subtraction operation
	 * between the corresponding complex elements of the {@link CVector}s 'vector1' and
	 * 'vector2' and return the result as a CVector.
	 * <p>Here complex elements from 'vector2' are subtracted from the corresponding
	 * complex elements of 'vector1'.
	 * <p>
	 * The dimension of both the CVectors should be similar or else this method will
	 * throw an DimensionMismatch Exception.
	 * <p>
	 * Each complex element of the return CVector will be the result of the subtraction of 
	 * the corresponding elements of the given CVectors respectively.
	 * @param CVector
	 *            vector1
	 * @param CVector
	 *            vector2
	 * @return CVector
	 * @throws DimensionMismatchException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static CVector subtract(CVector vector1, CVector vector2)
	throws DimensionMismatchException {
		if (vector1.length() != vector2.length()) {
			throw new DimensionMismatchException();
		}

		int L = vector1.length();

		float[] real = new float[L];
		float[] img = new float[L];

		for (int i = 0; i < L; i++) {
			real[i] = vector1.i2[i] - vector2.i2[i];
			img[i] = vector1.i1[i] - vector2.i1[i];
		}

		return new CVector(real,img);
	}

	/**
	 * This method will perform an element by element complex product operation
	 * between the corresponding complex elements of a CVectors given by the
	 * argument 'vector1' and 'vector2' and return the result as a CVector.
	 * <p>
	 * The dimension or length of both the CVectors 'vector1' and 'vector2'
	 * should be the same or else this method will throw an DimensionMismatch
	 * Exception.
	 * 
	 * @param CVector
	 *            vector1
	 * @param CVector
	 *            vector2
	 * @return CVector
	 * @throws DimensionMismatchException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static CVector dotProduct(CVector vector1, CVector vector2)
	throws DimensionMismatchException {
		if (vector1.length() != vector2.length()) {
			throw new DimensionMismatchException();
		}

		float[] real = new float[vector1.length()];
		float[] img = new float[vector2.length()];

		for (int i = 0; i < real.length; i++) {
			Complex res = vector1.getElement(i).product(vector2.getElement(i));
			real[i] = res.getRealPart();
			img[i] = res.getImaginaryPart();
		}

		return new CVector(real, img);
	}

	/**
	 * This method will divide the complex elements of CVector 'vector1' by the
	 * corresponding complex elements of CVector 'vector2' and return the result
	 * as a CVector.
	 * <p>
	 * The dimension or length of both the Vectors 'vector1' and 'vector2'
	 * should be the same or else this method will throw an DimensionMismatch
	 * Exception.
	 * <p>
	 * If any of the complex element of CVector 'vector2' is found to be having
	 * zero magnitude this method will throw an DivideByZero Exception.
	 * 
	 * @param CVector
	 *            vector1
	 * @param CVector
	 *            vector2
	 * @return CVector
	 * @throws DimensionMismatchException
	 * @throws DivideByZeroException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static CVector dotDivision(CVector vector1, CVector vector2)
	throws DivideByZeroException, DimensionMismatchException {
		if (vector1.length() != vector2.length()) {
			throw new DimensionMismatchException();
		}

		float[] real = new float[vector1.length()];
		float[] img = new float[vector2.length()];

		for (int i = 0; i < real.length; i++) {
			Complex res = vector1.getElement(i).divide(vector2.getElement(i));
			real[i] = res.getRealPart();
			img[i] = res.getImaginaryPart();
		}

		return new CVector(real, img);
	}

	/**
	 * This method will perform an element by element real product or scale
	 * operation on the complex elements of a CVector 'vector1' with corresponding real 
	 * scalar elements of the Vector 'vector2' and return the result as a CVector.
	 * <p>
	 * The dimension or length of both the CVector 'vector1' and Vector
	 * 'vector2' should be the same or else this method will throw an
	 * DimensionMismatch Exception.
	 * 
	 * @param CVector
	 *            vector1
	 * @param Vector
	 *            vector2
	 * @return CVector
	 * @throws DimensionMismatchException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static CVector dotProduct(CVector vector1, Vector vector2)
	throws DimensionMismatchException {
		if (vector1.length() != vector2.length()) {
			throw new DimensionMismatchException();
		}

		float[] real = new float[vector1.length()];
		float[] img = new float[vector2.length()];
		float value;

		for (int i = 0; i < real.length; i++) {
			value = vector2.getElement(i);

			real[i] = vector1.i2[i]*value;
			img[i] = vector1.i1[i]*value;
		}

		return new CVector(real, img);
	}
	
	/**
	 * This method will perform an element by element real product or scale
	 * operation between the complex elements of a CVector 'vector' and the real
	 * value given by the argument 'value'. The result is returned as a CVector.
	 * 
	 * @param CVector
	 *            vector
	 * @param float value
	 * @return CVector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static CVector scale(CVector vector, float value) {
		
		int l = vector.length();
		
		CVector result = null;
		try {
			result = new CVector(l);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			throw new BugEncounterException();
		}

		for (int i = 0; i < l; i++) {
			result.i2[i] = vector.i2[i]*value;
			result.i1[i] = vector.i1[i]*value;
		}
		
		return result;
	}
}
