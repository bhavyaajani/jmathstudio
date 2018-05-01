package org.JMathStudio.SignalToolkit.ProcessingTools.Cepstrum;

import org.JMathStudio.DataStructure.Vector.Vector;

/**
 * This class act as a wrapper class encapsulating the Complex Cepstrum.
 * <p>
 * A complex cepstrum of a discrete real signal is also a discrete real signal
 * and hence is represented by a {@link Vector}.
 * <p>
 * The class further encapsulate the linear phase term that went into the
 * computation of the given complex cepstrum, so that the inverse can be
 * reproduced with minimum phase distortion.
 * <p>
 * Thus the class act as a container, encapsulating both the complex cepstrum
 * and the associated linear phase term together.
 * 
 * @see ComplexCepstrum
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class CCepstrum {

	private Vector cceps;
	private int i5;

	/**
	 * This will instantiate a complex cepstrum with cepstrum as represented by
	 * {@link Vector} 'cceps' and the associated linear phase term 'nd'.
	 * <p>The argument 'cceps' is passed by reference and no deep copy of the same
	 * is made.
	 * 
	 * @param Vector
	 *            cceps
	 * @param int nd
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	protected CCepstrum(Vector cceps, int nd) {
		this.cceps = cceps;
		this.i5 = nd;
	}

	/**
	 * This method will return the complex cepstrum encapsulated by this class
	 * as a {@link Vector}
	 * 
	 * @return Vector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector accessComplexCepstrum() {
		return this.cceps;
	}

	/**
	 * This method will return the linear phase term associated with the given
	 * complex cepstrum. The same is required during the computation of the
	 * inverse to minimise the phase distortion.
	 * 
	 * @return int
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public int getLinearPhaseTerm() {
		return this.i5;
	}

}
