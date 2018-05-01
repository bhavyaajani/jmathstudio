package org.JMathStudio.DataStructure.Iterator.Iterator1D;

import org.JMathStudio.DataStructure.Vector.Vector;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.IteratorOutOfBoundsException;

/**
 * This class define a 1D Iterator for iterating over the elements of a {@link Vector} which are
 * defined over a 1D index space.
 * <p>See {@link Abstract1DIterator} for more information on the 1D Iterator.
 * 
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class VectorIterator extends Abstract1DIterator{

	private Vector vector;
		
	/**
	 * This method will create a VectorIterator associated with the given {@link Vector} 'vector'. 
	 * <p>For safety reason avoid creating an instance of {@link VectorIterator} directly but get the
	 * instance from {@link Vector#getAssociatedIterator()}, which maintain a single instance of 
	 * VectorIterator for a given Vector.
	 * @param Vector vector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public VectorIterator(Vector vector){
		this.vector = vector;
		f1(vector.getLargestIterableBounds());
	}
	
	/**
	 * This method reset the {@link Iterator1DBound} of the given VectorIterator with Iterator1DBound
	 * 'bound'.
	 * <p>If the Iterator1DBound 'bound' is not completely within the index space of the associated Vector 
	 * this method will throw an IteratorOutOfBounds Exception. See {@link Vector#getLargestIterableBounds()}.
	 * @param Iterator1DBound bound
	 * @throws IteratorOutOfBoundsException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void assignBounds(Iterator1DBound bound) throws IteratorOutOfBoundsException{
		if(!vector.getLargestIterableBounds().isSubBounds(bound))
			throw new IteratorOutOfBoundsException();
		else{
			f1(bound);
		}
	}
	
	/**
	 * This method will set the value of the element within the associated {@link Vector} for the current 
	 * index location of iterator with value as given by the argument 'value'.
	 * <p>If the current index location of the iterator is not within the valid bounds as specified by the set
	 * Iterator1DBound this method will throw an IteratorOutOfBounds Exception. Check {@link #isBound()}
	 * before manipulating the element for current location of the iterator.
	 * @param float value
	 * @throws IteratorOutOfBoundsException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void set(float value) throws IteratorOutOfBoundsException{
		try{
			if(i2)
				vector.accessVectorBuffer()[i4] = value;
			else
				throw new IteratorOutOfBoundsException();
		}catch(ArrayIndexOutOfBoundsException e){
			throw new BugEncounterException();
		}
	}
	
	/**
	 * This method will return the element within the associated {@link Vector} for the current index location
	 * of iterator.
	 * <p>If the current index location of the iterator is not within the valid bounds as specified by the set
	 * Iterator1DBound this method will throw an IteratorOutOfBounds Exception. Check {@link #isBound()}
	 * before accessing the element for current location of the iterator.
	 * @return float value
	 * @throws IteratorOutOfBoundsException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float get() throws IteratorOutOfBoundsException{
		try{
			if(i2)
				return vector.accessVectorBuffer()[i4];
			else throw new IteratorOutOfBoundsException();
		}catch(ArrayIndexOutOfBoundsException e){
			throw new BugEncounterException();
		}
	}
	
	/**
	 * This method will return the element within the associated {@link Vector} for the current index 
	 * location of the iterator and moves the iterator to next valid location.
	 * <p>If the current index location of the iterator is not within the valid bounds as specified by the set
	 * Iterator1DBound this method will throw an IteratorOutOfBounds Exception. Check {@link #isBound()}
	 * before accessing the element for current location of the iterator.
	 * @return float value
	 * @throws IteratorOutOfBoundsException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float getAndNext() throws IteratorOutOfBoundsException{
		float value = get();
		next();
		return value;
	}
	
	/**
	 * This method will return the element within the associated {@link Vector} for the current index location
	 * of iterator and moves the iterator to previous valid location.
	 * <p>If the current index location of iterator is not within the valid bounds as specified by the set
	 * Iterator1DBound this method will throw an IteratorOutOfBounds Exception. Check {@link #isBound()}
	 * before accessing the element for current location of the iterator.
	 * @return float value
	 * @throws IteratorOutOfBoundsException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float getAndPrevious() throws IteratorOutOfBoundsException{
		float value = get();
		previous();
		return value;
	}
	
	/**
	 * This method will set the value of the element within the associated {@link Vector} for the current 
	 * index location of iterator with value as given by the argument float 'value' and moves iterator to next
	 * valid location.
	 * <p>If the current index location of iterator is not within the valid bounds as specified by the set
	 * Iterator1DBound this method will throw an IteratorOutOfBounds Exception. Check {@link #isBound()}
	 * before manipulating the element for current location of the iterator.
	 * @param float value
	 * @throws IteratorOutOfBoundsException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void setAndNext(float value) throws IteratorOutOfBoundsException{
		set(value);
		next();
	}
	
	/**
	 * This method will set the value of the element within the associated {@link Vector} for the current 
	 * index location of iterator with value as given by the argument float 'value' and moves iterator to previous
	 * valid location.
	 * <p>If the current index location of the iterator is not within the valid bounds as specified by the set
	 * Iterator1DBound this method will throw an IteratorOutOfBounds Exception. Check {@link #isBound()}
	 * before manipulating the element for current location of the iterator.
	 * @param float value
	 * @throws IteratorOutOfBoundsException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void setAndPrevious(float value) throws IteratorOutOfBoundsException{
		set(value);
		previous();
	}	
}
