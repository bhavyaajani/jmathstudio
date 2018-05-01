package org.JMathStudio.DataStructure.Vector;

import java.util.Arrays;

import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.IllegalArgumentException;

/**
 * This class define several useful operations and manipulations applicable over
 * a {@link Vector}.
 * <pre>Usage
 * Let 'a' be a valid Vector object.
 * 
 * VectorTools tool = new VectorTools();//Create an instance of VectorTools.
 * 
 * VectorStack blocks = tool.block(a, size);//Get equal size segments of input Vector.
 * 
 * Vector sub_vector = tool.subVector(a, start, length);//Get a part or section of input Vector.
 * 
 * Vector shift = tool.linearShift(a, shift);//Shift linearly elements of input Vector.
 * 
 * Vector flip = tool.flip(a);//Flip input Vector.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class VectorTools {

	/**
	 * This method will resize the length or size of the Vector 'vector' as
	 * given by the argument 'size' by appropriately padding zeroes if 'vector'
	 * size is to be increased or by discarding appropriate number of end
	 * elements if 'vector' size is to be shorten.
	 * <p>
	 * The argument 'size' should be more than 0 else this method will throw an
	 * IllegalArgument Exception.
	 * 
	 * @param Vector
	 *            vector.
	 * @param int size.
	 * @return Vector
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector resize(Vector vector, int size)
			throws IllegalArgumentException {
		if (size < 1) {
			throw new IllegalArgumentException();
		}
		float[] result = new float[size];

		for (int i = 0; i < size; i++) {
			if (i < vector.length())
				result[i] = vector.getElement(i);
			else
				break;

		}

		return new Vector(result);
	}

	/**
	 * This method return the subVector or part of the Vector 'vector' which
	 * starts from the index position given by the argument 'start' in mother
	 * Vector and is of length given by the argument 'length'.
	 * <p>
	 * Thus this subVector includes elements starting from the index position
	 * given by argument 'start' till index position given by 'start+length-1'
	 * of mother Vector.
	 * <p>
	 * The argument 'start' should not be negative and argument 'length' should
	 * be more than 0 also the argument 'start' and 'length' should be so chosen
	 * that the subVector should not extend beyond the mother Vector or else
	 * this method will throw an IllegalArgument Exception.
	 * <p>
	 * Condition : 0<start+length<=vector.length(); should be satisfied.
	 * 
	 * @param Vector
	 *            vector
	 * @param int start
	 * @param int length
	 * @return Vector
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector subVector(Vector vector, int start, int length)
			throws IllegalArgumentException {
		if (start < 0 || length < 1) {
			throw new IllegalArgumentException();
		}
		if (start + length > vector.length()) {
			throw new IllegalArgumentException();
		}

		float[] result = new float[length];

		System.arraycopy(vector.accessVectorBuffer(), start, result, 0, length);

		return new Vector(result);
	}

	/**
	 * This method will concate the Vector 'vector2' with Vector 'vector1' and
	 * return the resultant Vector.
	 * <p>
	 * The length of the resultant Vector will be the sum of length of Vectors
	 * 'vector1' and 'vector2'.
	 * 
	 * @param Vector
	 *            vector1
	 * @param Vector
	 *            vector2
	 * @return Vector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector concate(Vector vector1, Vector vector2) {
		float[] tmp1 = vector1.accessVectorBuffer();
		float[] tmp2 = vector2.accessVectorBuffer();

		float[] result = new float[tmp1.length + tmp2.length];

		System.arraycopy(tmp1, 0, result, 0, tmp1.length);
		System.arraycopy(tmp2, 0, result, tmp1.length, tmp2.length);

		return new Vector(result);
	}

	/**
	 * This method checks whether the given Vector 'vector' has dyadic length,
	 * ie. if the length of the Vector is power of 2.
	 * <p>
	 * If length of Vector 'vector' is dyadic this method will return true else
	 * it will return false.
	 * 
	 * @param Vector
	 *            vector
	 * @return boolean
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public boolean isDyadic(Vector vector) {
		int N = vector.length();
		int index = 1;
		for (int i = 0;; i++) {
			if (N == index) {
				return true;
			}

			if (N < index) {
				return false;
			}

			index = 2 * index;

		}

	}

	/**
	 * This method will resize the Vector 'vector' to a nearest dyadic length
	 * i.e. length of power of 2, by padding appropriate number of tailing
	 * zeroes.
	 * <p>
	 * If the Vector 'vector' is already a dyadic length Vector, the same Vector
	 * will be return.
	 * 
	 * @param Vector
	 *            vector
	 * @return Vector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector makeDyadic(Vector vector) {
		int N = vector.length();
		int index = 1;
		for (int i = 0;; i++) {
			if (N == index) {
				return vector;
			}

			if (N < index) {
				try {
					return resize(vector, index);
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
			}

			index = 2 * index;

		}
	}

	/**
	 * This method will block or segments the Vector 'vector' into the block of
	 * length as given by the argument 'M' and return all the subVectors or
	 * blocks as a VectorStack.
	 * <p>
	 * The return VectorStack will contain blocks from left to right in
	 * increasing order of index. If the length of the Vector 'vector' is not a
	 * multiple of 'M', this method will pad appropriate number of tailing zeros
	 * before blocking the Vector.
	 * <p>
	 * The value of argument 'M' should be more than 0 or else this method will
	 * throw an IllegalArgument Exception.
	 * 
	 * @param Vector
	 *            vector
	 * @param int M
	 * @return VectorStack
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public VectorStack block(Vector vector, int M)
			throws IllegalArgumentException {
		if (M <= 0) {
			throw new IllegalArgumentException();
		}

		int Padd = vector.length();

		if (vector.length() % M != 0)
			Padd = Padd + M - vector.length() % M;

		Vector tmp = resize(vector, Padd);

		int nosvectors = tmp.length() / M;

		VectorStack result = new VectorStack();

		for (int i = 0; i < nosvectors; i++)
			result.addVector(subVector(tmp, i * M, M));

		return result;
	}

	/**
	 * This method will circularly shift the elements of the Vector 'vector' by
	 * the index positions as given by the argument 'space' and return the
	 * circularly shifted Vector.
	 * <p>
	 * If the argument 'space' is negative, shift will be operated from right to
	 * left with shifted forward elements of Vector becoming tailing elements of
	 * the Vector. If argument 'space' is positive, shift will be operated from
	 * left to right with shifted tailing elements of Vector becoming the
	 * forward elements of the Vector.
	 * 
	 * @param Vector
	 *            vector
	 * @param int space
	 * @return Vector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector circularShift(Vector vector, int space) {
		if (space < 0)
			return new Vector(f1(vector.accessVectorBuffer(), -space));
		else
			return new Vector(f4(vector.accessVectorBuffer(), space));
	}

	private float[] f1(float[] vector, int space) {
		if (space == 0) {
			return vector;
		}
		int shift = space % vector.length;

		float[] result = new float[vector.length];

		for (int i = 0; i < result.length; i++) {
			int index = i + shift;

			if (index >= 0 && index < vector.length) {
				result[i] = vector[index];
			} else {
				result[i] = vector[index - vector.length];
			}
		}

		return result;
	}

	private float[] f4(float[] vector, int space) {
		if (space == 0) {
			return vector;
		}

		int shift = space % vector.length;

		float[] result = new float[vector.length];

		for (int i = 0; i < result.length; i++) {
			int index = i + shift;

			if (index >= 0 && index < result.length) {
				result[index] = vector[i];
			} else {
				result[index - result.length] = vector[i];
			}
		}

		return result;
	}

	/**
	 * This method will flip the order of elements index position within the
	 * Vector 'vector' and return the resultant Vector.
	 * <p>
	 * Flip operation makes forward elements tailing elements and vice versa.
	 * 
	 * @param Vector
	 *            vector
	 * @return Vector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector flip(Vector vector) {
		float[] tmp = vector.accessVectorBuffer();

		float[] result = new float[tmp.length];

		for (int i = 0; i < result.length; i++) {
			result[i] = tmp[tmp.length - 1 - i];
		}

		return new Vector(result);
	}

	/**
	 * This method will shift the elements of the Vector 'vector' linearly by
	 * the index positions as given by the argument 'space' and return the
	 * shifted Vector.
	 * <p>
	 * If the argument 'space' is negative, elements of Vector will be shifted
	 * towards right by the amount equal to 'space', forward elements of the
	 * Vector falling outside will be discarded and same number of zeros will be
	 * inserted at the tailing end.
	 * <p>
	 * If the argument 'space' is positive, elements of Vector will be shifted
	 * towards left by the amount equal to 'space', tailing elements of the
	 * Vector falling outside will be discarded and same number of zeros will be
	 * inserted at the forward end.
	 * 
	 * @param Vector
	 *            vector
	 * @param int space
	 * @return Vector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector linearShift(Vector vector, int space) {
		float[] tmp = vector.accessVectorBuffer();

		if (space >= 0) {
			return new Vector(f6(tmp, space));
		} else {
			return new Vector(f0(tmp, -space));
		}
	}

	private float[] f0(float[] vector, int space) {
		float[] result = new float[vector.length];

		for (int i = space; i < vector.length; i++) {
			result[i - space] = vector[i];
		}
		return result;
	}

	private float[] f6(float[] vector, int space) {
		float[] result = new float[vector.length];

		for (int i = space; i < result.length; i++) {
			result[i] = vector[i - space];
		}
		return result;
	}

	/**
	 * This method will shrink the Vector 'vector' by the factor as given by the
	 * argument 'n' and return the shrunk version of the Vector as a Vector.
	 * <p>
	 * The value of argument 'n' should not be less than 1 else this method will
	 * throw an IllegalArgument Exception.
	 * <p>
	 * If the length of the Vector 'vector' is less than or equal to the
	 * argument 'n', the return Vector will contain only the first element of
	 * the Vector 'vector'.
	 * <p>
	 * This method will reduce the size of the given Vector by the factor of 'n'
	 * and return the intermediate elements of the original Vector. The length
	 * of the return Vector will be floor(L/n), where 'L' would be the original
	 * length of the Vector.
	 * 
	 * @param Vector
	 *            vector
	 * @param int n
	 * @return Vector
	 * @throws IllegalArgumentException
	 * @see {@link #expand(Vector, int)}
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector shrink(Vector vector, int n)
			throws IllegalArgumentException {
		if (n < 1) {
			throw new IllegalArgumentException();
		}
		float[] buf = vector.accessVectorBuffer();
		
		if (vector.length() <= n) {
			return new Vector(new float[] { buf[0] });
		} else {
			float result[] = new float[vector.length() / n];

			for (int i = 0; i < result.length; i++) {
				result[i] = buf[i * n];
			}

			return new Vector(result);
		}
	}

	/**
	 * This method will expand the Vector 'vector' by the factor as given by the
	 * argument 'n' by inserting intermediate zeroes and will return the
	 * expanded Vector.
	 * <p>
	 * The value of argument 'n' should not be less than 1 else this method will
	 * throw an IllegalArgument Exception.
	 * <p>
	 * This method will expand the Vector 'vector' by the factor of 'n' by
	 * inserting appropriate number of intermediate zeros. The length of the
	 * return vector will be L*n, where 'L' would be the length of the original
	 * Vector.
	 * 
	 * @param Vector
	 *            vector
	 * @param int n
	 * @return Vector
	 * @throws IllegalArgumentException
	 * @see {@link #shrink(Vector, int)}
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector expand(Vector vector, int n)
			throws IllegalArgumentException {
		if (n < 1) {
			throw new IllegalArgumentException();
		}
		float[] result = new float[vector.length() * n];
		float[] buf = vector.accessVectorBuffer();
		
		for (int i = 0; i < vector.length(); i++) {
			result[n * i] = buf[i];
		}

		return new Vector(result);
	}

	/**
	 * This method will Wrap the Vector 'vector' around its centre and return
	 * the wrapped Vector.
	 * <p>
	 * Wrap operation will cause a circular shift of the given Vector so that
	 * the first element of the Vector shifts to centre and extreme right
	 * elements wrap around to left of the Vector.
	 * <p>
	 * The length of the return Wrap Vector will be similar to the original
	 * Vector.
	 * 
	 * @param Vector
	 *            vector
	 * @return Vector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector wrapVector(Vector vector) {
		int l = vector.length();

		Vector res = new Vector(l);
		int shift = (l - 1) / 2;

		for (int i = 0; i < res.length(); i++) {
			int x = (i + shift) % l;
			res.setElement(vector.getElement(i), x);
		}

		return res;
	}

	/**
	 * This method will insert the Vector 'insert' within the Vector 'vector' starting from the index position as specified by the 
	 * argument 'l'.
	 * <p>The argument 'l' specify the starting index position for inserting the Vector 'insert' and should not be a negative number
	 * else this method will throw an IllegalArgument Exception.
	 * <p>The insert operation will overwrite the portion of the Vector 'vector' starting from the index position 'l' with portion length
	 * equal to the length of the Vector 'insert' with the corresponding content of the Vector 'insert'.
	 * <p>The length of the Vector 'insert' and the insert offset 'l' should be such that the Vector 'insert' remains completely within 
	 * the Vector 'vector' after insertion else this method will throw an IllegalArgument Exception.
	 * <p>If the length of the Vector 'vector' is LV and that of Vector 'insert' is LI, ensure that following
	 * condition is meet with starting index position for insert 'l',
	 * <p><i> l + LI <= LV.</i> 
	 * 
	 * @param Vector vector
	 * @param Vector insert
	 * @param int l 
	 * @return Vector
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector insert(Vector vector,Vector insert,int l) throws IllegalArgumentException
	{
		if(l < 0 )
			throw new IllegalArgumentException();
		
		int LV = vector.length();
		
		int LI = insert.length();
		
		int L = l + LI;
		
		if(L > LV)
			throw new IllegalArgumentException();
		else
		{
			try{
			Vector res = new Vector(LV);
			
			for(int i=0;i<l;i++)
			{
				res.setElement(vector.getElement(i), i);
			}
			
			for(int i=l;i<LI;i++)
			{
				res.setElement(insert.getElement(i), i);
			}
			
			for(int i=l+LI;i<LV;i++)
			{
				res.setElement(vector.getElement(i), i);
			}
			
			return res;
			}catch(ArrayIndexOutOfBoundsException e){
				throw new BugEncounterException();
			}
		}
	}
	
	/**
	 * This method will sort the elements of the input Vector 'vector' in ascending order and
	 * return the sorted Vector.
	 * 
	 * @param Vector
	 *            vector
	 * @return Vector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector sortAscending(Vector vector) {
		Vector clone = vector.clone();
		Arrays.sort(clone.accessVectorBuffer());

		return clone;
	}

	/**
	 * This method will sort the elements of input Vector 'vector' in descending order
	 * and return the sorted Vector.
	 * 
	 * @param Vector
	 *            vector
	 * @return Vector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector sortDescending(Vector vector) {
		Vector clone = vector.clone();
		Arrays.sort(clone.accessVectorBuffer());

		return flip(clone);
	}
}