package org.JMathStudio.DataStructure.Vector;

import org.JMathStudio.DataStructure.Generic.Index1D;
import org.JMathStudio.DataStructure.Generic.Index1DList;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.IllegalArgumentException;
import org.JMathStudio.MathToolkit.StatisticalTools.VectorStatistics.VectorStatistics;

/**
 * This class define some of the useful operations for finding the elements within a {@link Vector} with specific
 * traits or values.
 * <p>The location of all the matching elements within the Vector shall be return as an {@link Index1DList}.
 * <pre>Usage:
 * Vector vector = SignalGenerator.squarePulses(0.1f, 127);//Input Vector of interest.
 *		
 * VectorProbe vp = new VectorProbe();//Create an instance of VectorProbe object.
 * Index1DList indexes = vp.findAllElementIndexes(vector, -1);//Find index location of all elements with value '-1'.
 *	
 * vector.setAllElements(0, indexes);//Replace all elements with value '-1' with '0'.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class VectorProbe {

	/**
	 * This method will find index location for all the elements of {@link Vector} 'vector' which has global maximum value
	 * within the Vector and return the index list as {@link Index1DList}.
	 * <p>Thus all the {@link Index1D} within the return list specify location of all those elements 
	 * within the Vector 'vector' having the global maximum value within the Vector.
	 * @param Vector vector
	 * @return Index1DList
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Index1DList findAllMaxElementIndexes(Vector vector)
	{
		try{
			float max = new VectorStatistics().maximum(vector);

			final int L = vector.length();

			float[] buffer = vector.accessVectorBuffer();
			Index1DList list = new Index1DList();

			for(int i=0;i<L;i++)
			{
				if(buffer[i] == max){
					list.add(new Index1D(i));
				}
			}

			return list;
		}catch(IllegalArgumentException e){
			throw new BugEncounterException();
		}
	}
	
	/**
	 * This method will find index location for all the elements of {@link Vector} 'vector' which has global minimum value
	 * within the Vector and return the index list as {@link Index1DList}.
	 * <p>Thus all the {@link Index1D} within the return list specify location of all those elements 
	 * within the Vector 'vector' having the global minimum value within the Vector.
	 * @param Vector vector
	 * @return Index1DList
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Index1DList findAllMinElementIndexes(Vector vector)
	{
		try{
			float min = new VectorStatistics().minimum(vector);

			final int L = vector.length();

			float[] buffer = vector.accessVectorBuffer();
			Index1DList list = new Index1DList();

			for(int i=0;i<L;i++)
			{
				if(buffer[i] == min){
					list.add(new Index1D(i));
				}
			}

			return list;
		}catch(IllegalArgumentException e){
			throw new BugEncounterException();
		}
	}
	
	/**
	 * This method will find index location for all the elements of {@link Vector} 'vector' which has value equal to 
	 * the argument 'element' and return the index list as {@link Index1DList}.
	 * <p>Thus all the {@link Index1D} within the return list specify location of all those elements 
	 * within the Vector 'vector' having value equal to the argument 'element'.
	 * @param Vector vector
	 * @param float element
	 * @return Index1DList
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Index1DList findAllElementIndexes(Vector vector,float element)
	{
		try{
			
			final int L = vector.length();

			float[] buffer = vector.accessVectorBuffer();
			Index1DList list = new Index1DList();

			for(int i=0;i<L;i++)
			{
				if(buffer[i] == element){
					list.add(new Index1D(i));
				}
			}

			return list;
		}catch(IllegalArgumentException e){
			throw new BugEncounterException();
		}
	}
	
	/**
	 * This method will find index location for all the elements of {@link Vector} 'vector' which has value 
	 * more than or equal to argument 'min' but less than or equal to argument 'max' respectively and return 
	 * the index list as {@link Index1DList}.
	 * <p>Thus all the {@link Index1D} within the return list specify location of all those elements 
	 * within the Vector 'vector' having value in the range of [min max].
	 * <p>If argument 'min' is more than argument 'max' this method will throw an {@link IllegalArgumentException}.
	 * 
	 * @param Vector vector
	 * @param float min
	 * @param float max
	 * @return Index1DList
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com) 
	 */
	public Index1DList findAllElementIndexesInRange(Vector vector,float min,float max) throws IllegalArgumentException
	{
		if(min > max)
			throw new IllegalArgumentException();
		
		try{
					
			final int L = vector.length();

			float[] buffer = vector.accessVectorBuffer();
			Index1DList list = new Index1DList();

			for(int i=0;i<L;i++)
			{
				float check = buffer[i];
				if(check >= min && check <= max){
					list.add(new Index1D(i));
				}
			}

			return list;
		}catch(IllegalArgumentException e){
			throw new BugEncounterException();
		}
	}
	
	/**
	 * This method will find index location for all the elements of {@link Vector} 'vector' which has value 
	 * greater than or equal to argument 'threshold' and return the index list as {@link Index1DList}.
	 * <p>Thus all the {@link Index1D} within the return list specify location of all those elements 
	 * within the Vector 'vector' having value greater than or equal to 'threshold'.
	 * 
	 * @param Vector vector
	 * @param float threshold
	 * @return Index1DList
	 * @author Ajani Bhavya - (jmathstudio@gmail.com) 
	 */
	public Index1DList findAllLargerElementIndexes(Vector vector,float threshold) 
	{
		try{
					
			final int L = vector.length();

			float[] buffer = vector.accessVectorBuffer();
			Index1DList list = new Index1DList();

			for(int i=0;i<L;i++)
			{
				float check = buffer[i];
				if(check >= threshold){
					list.add(new Index1D(i));
				}
			}

			return list;
		}catch(IllegalArgumentException e){
			throw new BugEncounterException();
		}
	}
	
	/**
	 * This method will find index location for all the elements of {@link Vector} 'vector' which has value 
	 * less than or equal to argument 'threshold' and return the index list as {@link Index1DList}.
	 * <p>Thus all the {@link Index1D} within the return list specify location of all those elements 
	 * within the Vector 'vector' having value less than or equal to 'threshold'.
	 * 
	 * @param Vector vector
	 * @param float threshold
	 * @return Index1DList
	 * @author Ajani Bhavya - (jmathstudio@gmail.com) 
	 */
	public Index1DList findAllSmallerElementIndexes(Vector vector,float threshold) 
	{
		try{
					
			final int L = vector.length();

			float[] buffer = vector.accessVectorBuffer();
			Index1DList list = new Index1DList();

			for(int i=0;i<L;i++)
			{
				float check = buffer[i];
				if(check <= threshold){
					list.add(new Index1D(i));
				}
			}

			return list;
		}catch(IllegalArgumentException e){
			throw new BugEncounterException();
		}
	}
}
