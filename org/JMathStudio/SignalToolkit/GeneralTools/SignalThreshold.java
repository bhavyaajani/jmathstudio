package org.JMathStudio.SignalToolkit.GeneralTools;

import org.JMathStudio.DataStructure.Vector.Vector;
import org.JMathStudio.Exceptions.IllegalArgumentException;

/**
 * This class define various threshold operations to threshold sample values of a discrete real signal
 * represented by a {@link Vector}.
 * <pre>Usage:
 * Let 'a' be a valid Vector object.
 * 
 * SignalThreshold st = new SignalThreshold();//Create an instance of SignalThreshold.
 * 
 * Vector thr1 = st.thresholdAbove(a, 10, 1);//Set elements of input Vector with value greater
 * than threshold '10' to new value of '1'.
 * 
 * Vector thr2 = st.thresholdBetween(a, 10, 100, 0);//Set elements of input Vector with value
 * greater than '10' but less than '100' to new value of '0'.
 * </pre> 
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class SignalThreshold {

	/**
	 * This method will replace only those elements of the {@link Vector} 'vector' whose value is more than the 
	 * threshold 'T' with a new value as given by the argument 'set' in the corresponding resultant Vector and 
	 * return the same.
	 *  
	 * @param Vector vector
	 * @param float T
	 * @param float set
	 * @return Vector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector thresholdAbove(Vector vector, float T,float set) {
		int l = vector.length();

		Vector result = new Vector(l);

		for (int i = 0; i < l; i++) {
			if (vector.getElement(i) > T) {
				result.setElement(set, i);
			} else {
				result.setElement(vector.getElement(i), i);
			}
		}


		return result;
	}

	/**
	 * This method will replace only those elements of the {@link Vector} 'vector' whose value is less than the 
	 * threshold 'T' with a new value as given by the argument 'set' in the corresponding resultant Vector and 
	 * return the same.
	 *  
	 * @param Vector vector
	 * @param float T
	 * @param float set
	 * @return Vector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector thresholdBelow(Vector vector, float T,float set) {
		int l = vector.length();

		Vector result = new Vector(l);

		for (int i = 0; i < l; i++) {
			if (vector.getElement(i) < T) {
				result.setElement(set, i);
			} else {
				result.setElement(vector.getElement(i), i);
			}
		}


		return result;
	}

	/**
	 * This method will replace only those elements of the {@link Vector} 'vector' whose value is more than 
	 * lower threshold 'T1' but less than upper threshold 'T2' with a new value as given by the argument 
	 * 'set' in the corresponding resultant Vector and return the same.
	 * <p>Upper threshold value 'T2' should be more than the lower threshold value 'T1' else this method 
	 * will throw an IllegalArgument Exception. 
	 *
	 * @param Vector vector
	 * @param float T1
	 * @param float T2
	 * @param float set
	 * @return Vector
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector thresholdBetween(Vector vector, float T1,float T2,float set) throws IllegalArgumentException {

		if(T1 >= T2)
			throw new IllegalArgumentException();
		else
		{
			int l = vector.length();

			Vector result = new Vector(l);
			float ele=0;

			for (int i = 0; i < l; i++) {
				ele = vector.getElement(i);
				if (ele > T1 && ele <T2) {
					result.setElement(set, i);
				} else {
					result.setElement(ele, i);
				}
			}
			return result;
		}
	}

	/**
	 * This method will replace only those elements of the {@link Vector} 'vector' whose value is either less than 
	 * lower threshold 'T1' or more than upper threshold 'T2' with a new value as given by the argument 
	 * 'set' in the corresponding resultant Vector and return the same.
	 * <p>Upper threshold value 'T2' should be more than the lower threshold value 'T1' else this method 
	 * will throw an IllegalArgument Exception. 
	 *
	 * @param Vector vector
	 * @param float T1
	 * @param float T2
	 * @param float set
	 * @return Vector
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector thresholdBeyond(Vector vector, float T1,float T2,float set) throws IllegalArgumentException {

		if(T1 >= T2)
			throw new IllegalArgumentException();
		else
		{
			int l = vector.length();

			Vector result = new Vector(l);
			float ele=0;

			for (int i = 0; i < l; i++) {
				ele = vector.getElement(i);
				if (ele < T1 || ele >T2) {
					result.setElement(set, i);
				} else {
					result.setElement(ele, i);
				}
			}
			return result;
		}
	}

}
