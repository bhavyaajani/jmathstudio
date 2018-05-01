package org.JMathStudio.DataStructure.Iterator.Iterator2D;

import org.JMathStudio.DataStructure.Complex;
import org.JMathStudio.DataStructure.Cell.CCell;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.IteratorOutOfBoundsException;

/**
 * This class define a 2D Iterator for iterating over the elements of a {@link CCell} which are
 * defined over a 2D index space.
 * <p>See {@link Abstract2DIterator} for more information on the 2D Iterator.
 * 
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class CCellIterator extends Abstract2DIterator{

	private CCell cell;
	
	/**
	 * This method will create a CCellIterator associated with the given {@link CCell} 'cell'. 
	 * <p>For safety reason avoid creating an instance of {@link CCellIterator} directly but get the
	 * instance from {@link CCell#getAssociatedIterator()}, which maintain a single instance of 
	 * CCellIterator for a given CCell.
	 * @param CCell cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public CCellIterator(CCell cell){
		this.cell = cell;
		f1(cell.getLargestIterableBounds());
	}
	
	/**
	 * This method reset the {@link Iterator2DBound} of the given CCellIterator with Iterator2DBound
	 * 'bound'.
	 * <p>If the Iterator2DBound 'bound' is not completely within the index space of the associated CCell 
	 * this method will throw an IteratorOutOfBounds Exception. See {@link CCell#getLargestIterableBounds()}.
	 * @param Iterator2DBound bound
	 * @throws IteratorOutOfBoundsException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void assignBounds(Iterator2DBound bound) throws IteratorOutOfBoundsException{
		if(!cell.getLargestIterableBounds().isSubBounds(bound))
			throw new IteratorOutOfBoundsException();
		else{
			f1(bound);
		}
	}
	
	/**
	 * This method will set the value of the element within the associated {@link CCell} for the current 
	 * location of iterator with value as given by the argument Complex 'value'.
	 * <p>If the current location of iterator is not within the valid bounds as specified by the set
	 * Iterator2DBound this method will throw an IteratorOutOfBounds Exception. Check {@link #isBound()}
	 * before manipulating the element for current location of the iterator.
	 * @param Complex value
	 * @throws IteratorOutOfBoundsException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void set(Complex value) throws IteratorOutOfBoundsException{
		try{
			if(i3)
				cell.setElement(value, y, x);
			else
				throw new IteratorOutOfBoundsException();
		}catch(ArrayIndexOutOfBoundsException e){
			throw new BugEncounterException();
		}
	}
	
	/**
	 * This method will return the element within the associated {@link CCell} for the current location of 
	 * iterator.
	 * <p>If the current location of iterator is not within the valid bounds as specified by the set
	 * Iterator2DBound this method will throw an IteratorOutOfBounds Exception. Check {@link #isBound()}
	 * before accessing the element for current location of the iterator.
	 * @return Complex value
	 * @throws IteratorOutOfBoundsException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Complex get() throws IteratorOutOfBoundsException{
		try{
			if(i3)
				return cell.getElement(y, x);
			else 
				throw new IteratorOutOfBoundsException();
		}catch(ArrayIndexOutOfBoundsException e){
			throw new BugEncounterException();
		}
	}
	
	/**
	 * This method will return the element within the associated {@link CCell} for the current location of 
	 * iterator and moves the iterator to next valid location.
	 * <p>If the current location of iterator is not within the valid bounds as specified by the set
	 * Iterator2DBound this method will throw an IteratorOutOfBounds Exception. Check {@link #isBound()}
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
	 * This method will return the element within the associated {@link CCell} for the current location of 
	 * iterator and moves the iterator to previous valid location.
	 * <p>If the current location of iterator is not within the valid bounds as specified by the set
	 * Iterator2DBound this method will throw an IteratorOutOfBounds Exception. Check {@link #isBound()}
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
	 * This method will set the value of the element within the associated {@link CCell} for the current 
	 * location of iterator with value as given by the argument Complex 'value' and moves iterator to next
	 * valid location.
	 * <p>If the current location of iterator is not within the valid bounds as specified by the set
	 * Iterator2DBound this method will throw an IteratorOutOfBounds Exception. Check {@link #isBound()}
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
	 * This method will set the value of the element within the associated {@link CCell} for the current 
	 * location of iterator with value as given by the argument Complex 'value' and moves iterator to previous
	 * valid location.
	 * <p>If the current location of iterator is not within the valid bounds as specified by the set
	 * Iterator2DBound this method will throw an IteratorOutOfBounds Exception. Check {@link #isBound()}
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
