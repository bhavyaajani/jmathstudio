package org.JMathStudio.DataStructure;

import org.JMathStudio.Exceptions.DivideByZeroException;

/**
 * This class define a Complex number. A complex number is represented as a pair
 * of scalar (float) elements, representing the real and imaginary part of the
 * given complex number.
 * <p>
 * Further, this class supports useful operations on the complex number.
 * <pre>Usage:
 * Complex a = new Complex(1,1);
 * Complex b = new Complex(2,2);
 *
 * Complex c = a.add(b);
 * Complex d = a.getConjugate();
 *
 * float real = a.getRealPart();
 * float img = a.getImaginaryPart();
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class Complex {
	private float i2;
	private float i1;

	/**
	 * This will create a new complex number whose real and imaginary part is
	 * given by the argument 'real' and 'img' respectively.
	 * 
	 * @param float real
	 * @param float img
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Complex(float real, float img) {
		this.i2 = real;
		this.i1 = img;
	}

	/**
	 * This will create a complex number with both real and imaginary part as 0.
	 * 
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Complex() {
		this(0, 0);
	}

	/**
	 * This method will return the real part of the given complex number.
	 * 
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float getRealPart() {
		return i2;
	}

	/**
	 * This method will return the imaginary part of the given complex number.
	 * 
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float getImaginaryPart() {
		return i1;
	}

	/**
	 * This method will return the absolute value or magnitude of the given
	 * complex number.
	 * <p>
	 * Absolute value or magnitude of a complex number is defined as -
	 * sqrt(real^2 + imaginary^2).
	 * 
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float getMagnitude() {
		return (float) Math.sqrt(i2 * i2 + i1 * i1);
	}

	/**
	 * This method will return the phase angle of the given complex number.
	 * <p>
	 * The phase angle of a complex number is in radian and lie between [-PI
	 * PI].
	 * <p>
	 * The phase angle here is computed using method
	 * {@link Math#atan2(double, double)}.
	 * 
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float getAngle() {
		return (float) Math.atan2(i1, i2);
	}

	/**
	 * This method will return the result of addition of the complex number
	 * given by argument 'complex' with this complex number. The result is
	 * returned as a complex number.
	 * 
	 * @param Complex
	 *            complex
	 * @return Complex
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Complex add(Complex complex) {
		float r = this.i2 + complex.i2;
		float i = this.i1 + complex.i1;

		return new Complex(r, i);
	}

	/**
	 * This method will return the result of subtraction of the complex number
	 * given by argument 'complex' from this complex number. The result is
	 * returned as a complex number.
	 * 
	 * @param Complex
	 *            complex
	 * @return Complex
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Complex substract(Complex complex) {
		float r = this.i2 - complex.i2;
		float i = this.i1 - complex.i1;

		return new Complex(r, i);
	}

	/**
	 * This method will return the result of multiplication of the complex
	 * number given by argument 'complex' with this complex number. The result
	 * is returned as a complex number.
	 * 
	 * @param Complex
	 *            complex
	 * @return Complex
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Complex product(Complex complex) {
		float r = this.i2 * complex.i2 - this.i1 * complex.i1;
		float i = this.i2 * complex.i1 + this.i1 * complex.i2;

		return new Complex(r, i);
	}

	/**
	 * This method will return the result of division of this complex number by
	 * the complex number given by argument 'complex'. The result is returned as
	 * a complex number.
	 * <p>
	 * If the complex number 'complex' has zero magnitude, this method will
	 * throw a DivideByZeroException.
	 * 
	 * @param Complex
	 *            complex
	 * @return Complex
	 * @throws DivideByZeroException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Complex divide(Complex complex) throws DivideByZeroException {
		if (complex.i2 == 0 && complex.i1 == 0) {
			throw new DivideByZeroException();
		}

		Complex tmp = this.product(complex.getConjugate());
		float dividend = complex.i2 * complex.i2 + complex.i1
				* complex.i1;
		dividend = (float) (1.0 / dividend);
		tmp = tmp.product(dividend);

		return tmp;
	}

	/**
	 * This method will return the complex conjugate pair of this complex
	 * number.
	 * 
	 * @return Complex
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Complex getConjugate() {
		return new Complex(this.i2, -this.i1);
	}

	/**
	 * This method will return the product of this complex number with the real
	 * number given by argument 'value'. In this operation, both the real and
	 * imaginary part of this complex number are scaled by the real value given
	 * by the argument 'value'. The result is returned as a complex number.
	 * 
	 * @param float value
	 * @return Complex
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Complex product(float value) {
		float r = this.i2 * value;
		float i = this.i1 * value;

		return new Complex(r, i);

	}

	/**
	 * This method will return the Norm of this complex number.
	 * <p>
	 * The Norm of a complex number is defined as - real^2 + imaginary^2.
	 * 
	 * @return float
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float getNorm() {
		return (this.i2 * this.i2 + this.i1 * this.i1);
	}

	/**
	 * This method will convert the complex number defined in the polar
	 * coordinates with magnitude and phase as specified by the arguments
	 * 'magnitude' and 'phase' respectively to an equivalent complex number
	 * defined in the rectangular coordinate space and return the same.
	 * <p>
	 * The argument 'magnitude' should be a positive real scalar and argument
	 * 'phase' should be in the units of radian else this method will throw an
	 * IllegalArgumentException.
	 * 
	 * @param float magnitude
	 * @param float phase
	 * @return Complex
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Complex polarToRectangular(float magnitude, float phase)
			throws IllegalArgumentException {
		if (magnitude < 0) {
			throw new IllegalArgumentException();
		}

		float real = (float) (magnitude * Math.cos(phase));
		float img = (float) (magnitude * Math.sin(phase));

		return new Complex(real, img);
	}

	/**
	 * This method will return the reciprocal complex number for this complex
	 * number.
	 * <p>
	 * If the current complex number is zero ie. 0 + i0, this method will throw
	 * a DivideByZero Exception as reciprocal of zero complex does not exist.
	 * 
	 * @return Complex
	 * @throws DivideByZeroException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Complex getReciprocal() throws DivideByZeroException {
		float scale = getNorm();

		if (scale == 0)
			throw new DivideByZeroException();
		else
			return new Complex(this.i2 / scale, -this.i1 / scale);
	}

	/**
	 * This method return the complex exponential of this complex number.
	 * 
	 * @return Complex
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Complex exp() {
		double exp = Math.exp(i2);
		float r = (float) (exp * Math.cos(i1));
		float i = (float) (exp * Math.sin(i1));

		return new Complex(r, i);
	}

	/**
	 * This method return the complex cosine of this complex number.
	 * 
	 * @return Complex
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Complex cos() {
		float r = (float) (Math.cos(i2) * Math.cosh(i1));
		float i = (float) (-Math.sin(i2) * Math.sinh(i1));
		return new Complex(r, i);
	}

	/**
	 * This method return the complex sine of this complex number.
	 * 
	 * @return Complex
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Complex sin() {
		float r = (float) (Math.sin(i2) * Math.cosh(i1));
		float i = (float) (Math.cos(i2) * Math.sinh(i1));
		return new Complex(r, i);
	}

	/**
	 * This method return the complex tangent of this complex number.
	 * <p>
	 * For some complex number tangent is not defined. For such a case this
	 * method will throw a DivideByZero Exception.
	 * 
	 * @return Complex
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 * @throws DivideByZeroException
	 */
	public Complex tan() throws DivideByZeroException {
		return sin().divide(cos());
	}

}
