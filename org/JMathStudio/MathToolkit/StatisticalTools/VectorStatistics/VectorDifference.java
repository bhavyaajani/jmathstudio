package org.JMathStudio.MathToolkit.StatisticalTools.VectorStatistics;

import org.JMathStudio.DataStructure.Vector.Vector;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.DimensionMismatchException;

/**
 * This class define various operations to measure difference between the two {@link Vector}s.
 * <p>Difference between two Vectors is a function of the difference in their corresponding element value.
 * <pre>Usage:
 * Let 'a' & 'b' be Vector's with same length.
 * 
 * VectorDifference vd = new VectorDifference();//Create an instance of VectorDifference.
 * 
 * double abs_diff = vd.absDiff(a, b);//Compute absolute difference between the input
 * Vector's.
 * 
 * double euclidian = vd.euclidianDiff(a, b);//Compute euclidian difference or L2 norm between
 * the input Vector's.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */

public final class VectorDifference {

	/**
	 * This method computes the sum of difference between the corresponding elements of the two {@link Vector}s 
	 * 'vector1' and 'vector2' and return the same.
	 * <p>The dimensions of both the Vectors 'vector1' and 'vector2' should be similar else this method will throw
	 * a DimensionMismatch Exception.
	 * @param Vector vector1
	 * @param Vector vector2
	 * @return double
	 * @throws DimensionMismatchException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public double diff(Vector vector1, Vector vector2) throws DimensionMismatchException{

		if(vector1.length() != vector2.length())
			throw new DimensionMismatchException();
		else
		{
			try{

				double diff=0;
				int L = vector1.length();

				for(int i=0;i<L;i++)
				{
					diff+= vector1.getElement(i)-vector2.getElement(i);
				}

				return diff;				
			}catch(ArrayIndexOutOfBoundsException e)
			{
				throw new BugEncounterException();
			}
		}
	}

	/**
	 * This method computes the mean of difference between the corresponding elements of the two {@link Vector}s 
	 * 'vector1' and 'vector2' and return the same.
	 * <p>The dimensions of both the Vectors 'vector1' and 'vector2' should be similar else this method will throw
	 * a DimensionMismatch Exception.
	 * @param Vector vector1
	 * @param Vector vector2
	 * @return double
	 * @throws DimensionMismatchException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public double meanDiff(Vector vector1, Vector vector2) throws DimensionMismatchException{

		if(vector1.length() != vector2.length())
			throw new DimensionMismatchException();
		else
		{
			try{

				double diff=0;
				int L = vector1.length();

				for(int i=0;i<L;i++)
				{
					diff+= vector1.getElement(i)-vector2.getElement(i);
				}

				return diff/L;				
			}catch(ArrayIndexOutOfBoundsException e)
			{
				throw new BugEncounterException();
			}
		}
	}

	/**
	 * This method computes the sum of absolute difference between the corresponding elements of the two {@link Vector}s 
	 * 'vector1' and 'vector2' and return the same.
	 * <p>This quantity is also called the L1 norm of difference between the two {@link Vector}s.
	 * <p>The dimensions of both the Vectors 'vector1' and 'vector2' should be similar else this method will throw
	 * a DimensionMismatch Exception.
	 * @param Vector vector1
	 * @param Vector vector2
	 * @return double
	 * @throws DimensionMismatchException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public double absDiff(Vector vector1, Vector vector2) throws DimensionMismatchException{

		if(vector1.length() != vector2.length())
			throw new DimensionMismatchException();
		else
		{
			try{

				double diff=0;
				int L = vector1.length();

				for(int i=0;i<L;i++)
				{
					diff+= Math.abs(vector1.getElement(i)-vector2.getElement(i));
				}

				return diff;				
			}catch(ArrayIndexOutOfBoundsException e)
			{
				throw new BugEncounterException();
			}
		}
	}

	/**
	 * This method computes the mean of absolute difference between the corresponding elements of the two {@link Vector}s 
	 * 'vector1' and 'vector2' and return the same.
	 * <p>The dimensions of both the Vectors 'vector1' and 'vector2' should be similar else this method will throw
	 * a DimensionMismatch Exception.
	 * @param Vector vector1
	 * @param Vector vector2
	 * @return double
	 * @throws DimensionMismatchException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public double meanAbsDiff(Vector vector1, Vector vector2) throws DimensionMismatchException{

		if(vector1.length() != vector2.length())
			throw new DimensionMismatchException();
		else
		{
			try{

				double diff=0;
				int L = vector1.length();

				for(int i=0;i<L;i++)
				{
					diff+= Math.abs(vector1.getElement(i)-vector2.getElement(i));
				}

				return diff/L;				
			}catch(ArrayIndexOutOfBoundsException e)
			{
				throw new BugEncounterException();
			}
		}
	}

	/**
	 * This method computes the euclidian difference or L2 norm between the two {@link Vector}s 'vector1' and 'vector2'
	 * and return the same.
	 * <p>Euclidian difference between the two Vectors is the square root of the sum of square of difference between
	 * their corresponding elements. 
	 * <p>The dimensions of both the Vectors 'vector1' and 'vector2' should be similar else this method will throw
	 * a DimensionMismatch Exception.
	 * @param Vector vector1
	 * @param Vector vector2
	 * @return double
	 * @throws DimensionMismatchException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public double euclidianDiff(Vector vector1, Vector vector2) throws DimensionMismatchException{

		if(vector1.length() != vector2.length())
			throw new DimensionMismatchException();
		else
		{
			try{

				double euclidian=0;
				int L = vector1.length();
				float diff = 0;

				for(int i=0;i<L;i++)
				{
					diff = vector1.getElement(i)-vector2.getElement(i);
					euclidian+=diff*diff;
				}

				return Math.sqrt(euclidian);				
			}catch(ArrayIndexOutOfBoundsException e)
			{
				throw new BugEncounterException();
			}
		}
	}

	/**
	 * This method computes the Root Mean Square (RMS) difference between the two {@link Vector}s 'vector1' and 'vector2'
	 * and return the same.
	 * <p>RMS difference between the two Vectors is the square root of the average of square of difference between
	 * their corresponding elements. 
	 * <p>The dimensions of both the Vectors 'vector1' and 'vector2' should be similar else this method will throw
	 * a DimensionMismatch Exception.
	 * @param Vector vector1
	 * @param Vector vector2
	 * @return double
	 * @throws DimensionMismatchException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public double RMSDiff(Vector vector1, Vector vector2) throws DimensionMismatchException{

		if(vector1.length() != vector2.length())
			throw new DimensionMismatchException();
		else
		{
			try{

				double rms=0;
				int L = vector1.length();
				float diff = 0;

				for(int i=0;i<L;i++)
				{
					diff = vector1.getElement(i)-vector2.getElement(i);
					rms+=diff*diff;
				}

				return Math.sqrt(rms/L);				
			}catch(ArrayIndexOutOfBoundsException e)
			{
				throw new BugEncounterException();
			}
		}
	}
}
