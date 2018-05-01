package org.JMathStudio.DataStructure.Vector;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.JMathStudio.DataStructure.Complex;
import org.JMathStudio.DataStructure.Generic.Index1D;
import org.JMathStudio.DataStructure.Iterator.Iterator1D.CVectorIterator;
import org.JMathStudio.DataStructure.Iterator.Iterator1D.Iterator1DBound;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.DimensionMismatchException;
import org.JMathStudio.Exceptions.IllegalArgumentException;
import org.JMathStudio.VisualToolkit.Graph.VectorGraph;

/**
 * This class define a 1D data structure 'CVector' which stores 1D Complex
 * elements.
 * <p>
 * Internally, CVector or a complex vector is represented by two 1D float
 * arrays, such that either of them stores the real and imaginary component for
 * the corresponding complex element in the CVector.
 * <p>
 * Length - The number of elements with in the CVector. The indexing of the
 * elements starts from 0 till one less than the length of this CVector.
 * <p>
 * With in the toolkit a CVector is used to represent the following :
 * <p>
 * 1 1D Discrete Complex Signal. A discrete complex signal is the ordered
 * sequence of complex elements as given by the CVector.
 * <p>
 * <i>By default, elements of the CVector (here discrete complex signal) are
 * equi-sampled and ordered in the increasing time position with first element
 * located at the origin. </i>
 * <p>
 * 2 1D ordered sequence of complex data.
 * <pre>Usage:
 * float[] real = new float[128];
 * float[] imag = new float[128];
 * CVector a = new CVector(real,imag);//Create an instance of CVector from input arrays.
 * 
 * a.setElement(real, imag, index);//Set a new Complex element in the CVector.
 * 
 * a.accessImaginaryPart();//Access array representing imaginary component of CVector.
 * 
 * CVector conj = a.getConjugate();//Get conjugate of the given CVector.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class CVector implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -9169432179568166986L;
	//As CVector is serializable, all fields must be either serializable or transient.
	float[] i2;
	float[] i1;
	private transient CVectorIterator iterator = null;
	
	/**
	 * This will create a CVector with complex elements whose Real and Imaginary
	 * part is as given by the Vector 'real' and 'imag' respectively.
	 * <p>
	 * The corresponding elements of the Vector 'real' and 'imag' represents the
	 * real and imaginary part of the complex elements.
	 * <p>
	 * The length of both the Vector should be same else this method will throw
	 * a DimensionMismatch Exception.
	 * <p>The arguments 'real' and 'imag' are passed as reference and no deep copy of
	 * the objects are made.
	 * 
	 * @param Vector
	 *            real
	 * @param Vector
	 *            imag
	 * @throws DimensionMismatchException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public CVector(Vector real, Vector imag) throws DimensionMismatchException {

		if (real.length() != imag.length())
			throw new DimensionMismatchException();

		this.i2 = real.accessVectorBuffer();
		this.i1 = imag.accessVectorBuffer();
	}

	/**
	 * This will create a CVector with complex elements whose Real and Imaginary
	 * part is as given by the float 1D array 'real' and 'imag' respectively.
	 * <p>
	 * The corresponding elements of the float array 'real' and 'imag'
	 * represents the real and imaginary part of the complex elements.
	 * <p>
	 * The length of both the array should be same else this method will throw a
	 * DimensionMismatch Exception.
	 * <p>The arguments 'real' and 'imag' are passed as reference and no deep copy of
	 * the array are made.
	 * 
	 * @param float[] real
	 * @param float[] imag
	 * @throws DimensionMismatchException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public CVector(float[] real, float[] imag)
			throws DimensionMismatchException {

		if (real.length != imag.length)
			throw new DimensionMismatchException();

		this.i2 = real;
		this.i1 = imag;
	}

	/**
	 * This will create a CVector with the capacity to store the number of
	 * complex elements as given by the argument 'length'. The argument 'length'
	 * should be more than 0 else this method will throw an IllegalArgument
	 * Exception.
	 * 
	 * @param int length
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public CVector(int length)
			throws IllegalArgumentException {

		if (length < 1)
			throw new IllegalArgumentException();

		i2 = new float[length];
		i1 = new float[length];
	}

	/**
	 * This method will convert the given CVector to 1D complex array and return
	 * the same.
	 * 
	 * @return Complex[]
	 * @see Complex
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Complex[] toComplexArray() {
		Complex[] array = new Complex[i2.length];

		for (int i = 0; i < array.length; i++) {
			array[i] = new Complex(i2[i], i1[i]);
		}

		return array;
	}

	/**
	 * This method will return the length or size of the given CVector.
	 * 
	 * @return int
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public int length() {
		return this.i2.length;
	}

	/**
	 * This method will return the complex element present at the index location
	 * as given by the argument 'index' from this CVector.
	 * <p>
	 * The argument 'index' should be in the range of [0 length-1], else this
	 * method will throw an ArrayIndexOutOfBound Exception.
	 * 
	 * @param int index
	 * @return Complex
	 * @see Complex
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Complex getElement(int index) {
		return new Complex(i2[index], i1[index]);
	}

	/**
	 * This method will return the Complex element present at the index location
	 * as given by the argument {@link Index1D} 'index' from this CVector.
	 * <p>
	 * The argument 'index' should give a valid index position within the CVector in the range of 
	 * [0 length-1] else this method will throw an ArrayIndexOutOfBound Exception.
	 * 
	 * @param Index1D index
	 * @return Complex
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Complex getElement(Index1D index) {
		return new Complex(i2[index.getX()], i1[index.getX()]);
	}
	
	/**
	 * This method will replace the complex element present at the index
	 * location as given by the argument 'index' in this CVector with the
	 * complex number as given by the argument 'element'.
	 * <p>
	 * The argument 'index' should be in the range of [0 length-1], else this
	 * method will throw an ArrayIndexOutOfBound Exception.
	 * 
	 * @param Complex
	 *            element
	 * @param int index
	 * @see Complex
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void setElement(Complex element, int index) {
		this.i2[index] = element.getRealPart();
		this.i1[index] = element.getImaginaryPart();
	}

	/**
	 * This method will replace the complex element present at the index
	 * location as given by the argument 'index' in this CVector with the
	 * complex number whose Real and Imaginary part is as given by the arguments
	 * 'real' and 'imag' respectively.
	 * <p>
	 * The argument 'index' should be in the range of [0 length-1], else this
	 * method will throw an ArrayIndexOutOfBound Exception.
	 * 
	 * @param float real
	 * @param float imag
	 * @param int index
	 * @see Complex
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void setElement(float real, float imag, int index) {
		this.i2[index] = real;
		this.i1[index] = imag;
	}

	/**
	 * This method will replace the complex element present at the index
	 * location as given by the argument {@link Index1D} 'index' in this CVector with the
	 * complex number as given by the argument {@link Complex} 'element'.
	 * <p>
	 * The argument 'index' should specify a valid index position within the CVector in the 
	 * range of [0 length-1] else this method will throw an ArrayIndexOutOfBound Exception.
	 *
	 * @param Complex
	 *            element
	 * @param Index1D index
	 * @see Complex
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void setElement(Complex element, Index1D index) {
		this.i2[index.getX()] = element.getRealPart();
		this.i1[index.getX()] = element.getImaginaryPart();
	}

	/**
	 * This method will replace the complex element present at the index
	 * location as given by the argument {@link Index1D} 'index' in this CVector with the
	 * complex number whose Real and Imaginary part is as given by the arguments
	 * 'real' and 'imag' respectively.
	 * <p>
	 * The argument 'index' should give a valid index position within the CVector in the range of 
	 * [0 length-1] else this method will throw an ArrayIndexOutOfBound Exception.
	 * 
	 * @param float real
	 * @param float imag
	 * @param Index1D index
	 * @see Complex
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void setElement(float real, float imag, Index1D index) {
		this.i2[index.getX()] = real;
		this.i1[index.getX()] = imag;
	}
	
	/**
	 * This method will plot the real vector and imaginary vector of the given
	 * complex vector or CVector 'vector' on a two separate {@link VectorGraph}.
	 * <p>
	 * See {@link VectorGraph} for further information on graph utility.
	 * 
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void plot() {

		new Vector(i2).plot("Real Part", "Value");
		new Vector(i1).plot("Imaginary Part", "Value");
	}

	/**
	 * This method will return the Magnitude of the complex elements of the
	 * given CVector as a Vector. Each element in return Vector will be the
	 * magnitude value for the corresponding complex element in CVector
	 * 
	 * @return Vector
	 * @see Complex
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector getMagnitude() {
		float[] result = new float[i2.length];

		for (int i = 0; i < result.length; i++) {
			result[i] = (float) Math.sqrt(i2[i] * i2[i] + i1[i] * i1[i]);
		}

		return new Vector(result);
	}

	/**
	 * This method will return the Real part of the complex elements of the
	 * given CVector as a Vector. Each element in return Vector will be the real
	 * part of the corresponding complex element in CVector.
	 * 
	 * @return Vector
	 * @see Complex
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector accessRealPart() {
		return new Vector(i2);
	}

	/**
	 * This method will return the Imaginary part of the complex elements of the
	 * given CVector as a Vector. Each element in return Vector will be the
	 * imaginary part of the corresponding complex element in CVector.
	 * 
	 * @return Vector
	 * @see Complex
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector accessImaginaryPart() {
		return new Vector(i1);
	}

	/**
	 * This method will return the Phase Angles of the complex elements of the
	 * given CVector as a Vector. Each element in return Vector will be the
	 * phase angle for the corresponding complex element in CVector.
	 * 
	 * @return Vector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector getAngle() {
		float[] result = new float[i2.length];

		for (int i = 0; i < result.length; i++) {
			result[i] = (float) Math.atan2(i1[i], i2[i]);
		}

		return new Vector(result);
	}

	/**
	 * This method will return the conjugate CVector of the given CVector.
	 * <p>
	 * Corresponding complex elements of the return CVector will be complex
	 * conjugate of the elements of this CVector.
	 * <p>
	 * The return CVector will be of the same length as that of this CVector.
	 * 
	 * @return CVector
	 * @see Complex
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public CVector getConjugate() {
		float[] cReal = new float[i2.length];
		float[] cImg = new float[i2.length];

		for (int i = 0; i < cReal.length; i++) {
			cReal[i] = i2[i];
			cImg[i] = -i1[i];
		}

		try {
			return new CVector(cReal, cImg);
		} catch (DimensionMismatchException e) {
			throw new BugEncounterException();
		}
	}

	/**
	 * This method will return the clone copy of the given CVector.
	 * <p>
	 * A clone of the CVector is the CVector with similar length and same
	 * corresponding complex elements as that of original CVector.
	 * 
	 * @return CVector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */

	public CVector clone() {
		try {
			CVector clone = new CVector(this.length());

			for (int i = 0; i < clone.length(); i++)
				clone.setElement(i2[i], i1[i], i);
			return clone;
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}
	}

	/**
	 * This method will save the current state of this {@link CVector} object to
	 * an external file with file name as specified by the argument 'filename'
	 * and path as specified by the argument 'dirPath'.
	 * <p>
	 * The argument 'filename' should not contain '.' charactor and any
	 * extension. Further an extension '.cvector' will be added to the saved
	 * state file by this method.
	 * <p>
	 * The current state of this {@link CVector} object stored in the external
	 * file can be loaded into the framework at later stage using the
	 * {@link #retrieveState(String)} method.
	 * <p>
	 * If the argument 'dirPath' does not specify a valid directory path or if
	 * the method encounters any IO issues during the state save this method
	 * will throw an IO Exception.
	 * 
	 * @param String
	 *            dirPath
	 * @param String
	 *            filename
	 * @throws IOException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void saveState(String dirPath, String filename) throws IOException {
		String filePath = dirPath + filename + ".cvector";

		FileOutputStream fs = new FileOutputStream(filePath);
		ObjectOutputStream os = new ObjectOutputStream(fs);

		os.writeObject(this);
		os.close();
		fs.close();

	}

	/**
	 * This method load the saved state of the {@link CVector} object saved in
	 * the external file as specified by the argument 'filePath' and return the
	 * same as a {@link CVector}.
	 * <p>
	 * The argument 'filePath' should provide the full path to an external file
	 * which contain the saved state of a {@link CVector} object. Further the
	 * extension of the file should be '.cvector'.
	 * <p>
	 * If the extension of the external file containing the saved state of a
	 * {@link CVector} object is not '.cvector' or is not a valid save state
	 * file of a {@link CVector} object this method will throw an Illegal
	 * Argument Exception.
	 * <p>
	 * If the argument 'filePath' does not point to a file or if this method
	 * encounters any IO issues an IO Exception will be thrown.
	 * 
	 * @param String
	 *            filePath
	 * @return CVector
	 * @throws IOException
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static CVector retrieveState(String filePath) throws IOException,
			IllegalArgumentException {
		String format = filePath.substring(filePath.indexOf('.') + 1);

		if (!format.equalsIgnoreCase("cvector"))
			throw new IllegalArgumentException();

		FileInputStream fs = new FileInputStream(filePath);
		ObjectInputStream os = new ObjectInputStream(fs);

		CVector cvec = null;

		try {
			cvec = (CVector) os.readObject();
		} catch (ClassCastException e) {
			throw new IllegalArgumentException();
		} catch (ClassNotFoundException e) {
			throw new IllegalArgumentException();
		}

		os.close();
		fs.close();

		return cvec;
	}

	/**
	 * This method will check if the index as specified by the argument 'index'
	 * falls within the valid bounds of this CVector.
	 * 
	 * @param int index
	 * @return boolean
	 */
	public boolean isWithinBounds(int index) {
		if (index >= 0 && index < i2.length)
			return true;
		else
			return false;
	}
	
	/**
	 * This method check whether the CVector 'vector' has length similar to that
	 * of this CVector.
	 * @param CVector vector
	 * @return boolean
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public boolean hasSameLength(CVector vector){
		
		return (vector.length() == this.length());
	}
	
	/**
	 * This method will create and return a CVector which has complex elements
	 * define in rectangular co-ordinates space having Magnitude and Phase equal 
	 * as given by the corresponding elements of Vector 'magnitude' and 'phase' 
	 * (in unit of radians) respectively in polar co-ordinates space.
	 * <p>
	 * The dimension or length of both the Vectors - 'magnitude' and 'phase'
	 * should be the same else this method will throw an DimensionMismatch
	 * Exception.
	 * <p>
	 * If any of the element of Vector 'magnitude' is negative this method will throw an
	 * IllegalArgumentException.
	 * @param Vector
	 *            magnitude
	 * @param Vector
	 *            phase
	 * @return CVector
	 * @throws DimensionMismatchException
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com) 
	 */
	public final static CVector polarToRectangular(Vector magnitude, Vector phase)
			throws DimensionMismatchException, IllegalArgumentException {
		if (magnitude.length() != phase.length()) {
			throw new DimensionMismatchException();
		}

		float[] real = new float[magnitude.length()];
		float[] img = new float[phase.length()];
		float M,P;
		
		for (int i = 0; i < real.length; i++) {
			M = magnitude.getElement(i);
			if(M < 0)
				throw new IllegalArgumentException();
			P = phase.getElement(i);
			
			real[i] = (float) (M * Math.cos(P));
			img[i] = (float) (M * Math.sin(P));
		}

		return new CVector(real, img);
	}
	
	/**
	 * This will return an instance of {@link CVectorIterator } associated with the given CVector.
	 * <p>Each CVector object maintains a single internal instance of CVectorIterator object. Getting
	 * CVectorIterator through this method avoid creation of multiple instances of CVectorIterator associated
	 * with the given CVector.
	 * @return CVectorIterator
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public CVectorIterator getAssociatedIterator(){
		if(iterator == null)
			iterator = new CVectorIterator(this);
		return iterator;
	}
	
	/**
	 * This method return the largest possible iterable bounds for the given CVector as {@link Iterator1DBound}.
	 * @return Iterator1DBound
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Iterator1DBound getLargestIterableBounds(){
		try {
			return new Iterator1DBound(0,length());
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}
	}
}
