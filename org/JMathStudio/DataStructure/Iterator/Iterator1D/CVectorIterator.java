package org.JMathStudio.DataStructure.Iterator.Iterator1D;

import org.JMathStudio.DataStructure.Complex;
import org.JMathStudio.DataStructure.Vector.CVector;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.IteratorOutOfBoundsException;

/**
 * This class define a 1D Iterator for iterating over the elements of a {@link CVector} which are
 * defined over a 1D index space.
 * <p>See {@link Abstract1DIterator} for more information on the 1D Iterator.
 * 
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class CVectorIterator extends Abstract1DIterator{

	private CVector vector;
	
	/**
	 * This method will create a CVectorIterator associated with the given {@link CVector} 'vector'. 
	 * <p>For safety reason avoid creating an instance of {@link CVectorIterator} directly but get the
	 * instance from {@link CVector#getAssociatedIterator()}, which maintains a single instance of 
	 * CVectorIterator for a given CVector.
	 * @param CVector vector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public CVectorIterator(CVector vector){
		this.vector = vector;
		f1(vector.getLargestIterableBounds());
	}
	
	/**
	 * This method reset the {@link Iterator1DBound} of the given CVectorIterator with Iterator1DBound
	 * 'bound'.
	 * <p>If the Iterator1DBound 'bound' is not completely within the index space of the associated CVector 
	 * this method will throw an IteratorOutOfBounds Exception. See {@link CVector#getLargestIterableBounds()}.
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
	 * This method will set the value of the element within the associated {@link CVector} for the current 
	 * index location of iterator with value as given by the argument Complex 'value'.
	 * <p>If the current index location of the iterator is not within the valid bounds as specified by the set
	 * Iterator1DBound this method will throw an IteratorOutOfBounds Exception. Check {@link #isBound()}
	 * before manipulating the element for current index location of the iterator.
	 * @param Complex value
	 * @throws IteratorOutOfBoundsException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void set(Complex value) throws IteratorOutOfBoundsException{
		try{
			if(i2)
				vector.setElement(value, i4);
			else
				throw new IteratorOutOfBoundsException();
		}catch(ArrayIndexOutOfBoundsException e){
			throw new BugEncounterException();
		}
	}
	
	/**
	 * This method will return the element within the associated {@link CVector} for the current index location of 
	 * iterator.
	 * <p>If the current index location of the iterator is not within the valid bounds as specified by the set
	 * Iterator1DBound this method will throw an IteratorOutOfBounds Exception. Check {@link #isBound()}
	 * before accessing the element for current location of the iterator.
	 * @return Complex value
	 * @throws IteratorOutOfBoundsException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Complex get() throws IteratorOutOfBoundsException{
		try{
			if(i2)
				return vector.getElement(i4);
			else 
				throw new IteratorOutOfBoundsException();
		}catch(ArrayIndexOutOfBoundsException e){
			throw new BugEncounterException();
		}
	}
	
	/**
	 * This method will return the element within the associated {@link CVector} for the current index location of 
	 * iterator and moves the iterator to next valid location.
	 * <p>If the current index location of iterator is not within the valid bounds as specified by the set
	 * Iterator1DBound this method will throw an IteratorOutOfBounds Exception. Check {@link #isBound()}
	 * before accessing the element for current location of the iterator.
	 * @return Complex value
	 * @throws IteratorOutOfBoundsException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Complex getAndNext() throws IteratorOutOfBoundsException{
		Complex value = get();
		next();
		return value;
	}
	
	/**
	 * This method will return the element within the associated {@link CVector} for the current index location of 
	 * iterator and moves the iterator to previous valid location.
	 * <p>If the current index location of iterator is not within the valid bounds as specified by the set
	 * Iterator1DBound this method will throw an IteratorOutOfBounds Exception. Check {@link #isBound()}
	 * before accessing the element for current location of the iterator.
	 * @return Complex value
	 * @throws IteratorOutOfBoundsException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Complex getAndPrevious() throws IteratorOutOfBoundsException{
		Complex value = get();
		previous();
		return value;
	}
	
	/**
	 * This method will set the value of the element within the associated {@link CVector} for the current 
	 * index location of iterator with value as given by the argument Complex 'value' and moves iterator to next
	 * valid location.
	 * <p>If the current index location of iterator is not within the valid bounds as specified by the set
	 * Iterator1DBound this method will throw an IteratorOutOfBounds Exception. Check {@link #isBound()}
	 * before manipulating the element for current location of the iterator.
	 * @param Complex value
	 * @throws IteratorOutOfBoundsException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void setAndNext(Complex value) throws IteratorOutOfBoundsException{
		set(value);
		next();
	}
	
	/**
	 * This method will set the value of the element within the associated {@link CVector} for the current 
	 * location of iterator with value as given by the argument Complex 'value' and moves iterator to previous
	 * valid location.
	 * <p>If the current index location of iterator is not within the valid bounds as specified by the set
	 * Iterator1DBound this method will throw an IteratorOutOfBounds Exception. Check {@link #isBound()}
	 * before manipulating the element for current location of the iterator.
	 * @param Complex value
	 * @throws IteratorOutOfBoundsException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void setAndPrevious(Complex value) throws IteratorOutOfBoundsException{
		set(value);
		previous();
	}
}
