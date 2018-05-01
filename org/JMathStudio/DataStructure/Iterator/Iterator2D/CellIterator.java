package org.JMathStudio.DataStructure.Iterator.Iterator2D;

import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.IteratorOutOfBoundsException;

/**
 * This class define a 2D Iterator for iterating over the elements of a {@link Cell} which are
 * defined over a 2D index space.
 * <p>See {@link Abstract2DIterator} for more information on the 2D Iterator.
 * 
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class CellIterator extends Abstract2DIterator{

	private Cell cell;
		
	/**
	 * This method will create a CellIterator associated with the given {@link Cell} 'cell'. 
	 * <p>For safety reason avoid creating an instance of {@link CellIterator} directly but get the
	 * instance from {@link Cell#getAssociatedIterator()}, which maintain a single instance of 
	 * CellIterator for a given Cell.
	 * @param Cell cell
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public CellIterator(Cell cell){
		this.cell = cell;
		f1(cell.getLargestIterableBounds());
	}
	
	/**
	 * This method reset the {@link Iterator2DBound} of the given CellIterator with Iterator2DBound
	 * 'bound'.
	 * <p>If the Iterator2DBound 'bound' is not completely within the index space of the associated Cell 
	 * this method will throw an IteratorOutOfBounds Exception. See {@link Cell#getLargestIterableBounds()}.
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
	 * This method will set the value of the element within the associated {@link Cell} for the current 
	 * location of iterator with value as given by the argument 'value'.
	 * <p>If the current location of iterator is not within the valid bounds as specified by the set
	 * Iterator2DBound this method will throw an IteratorOutOfBounds Exception. Check {@link #isBound()}
	 * before manipulating the element for current location of the iterator.
	 * @param float value
	 * @throws IteratorOutOfBoundsException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void set(float value) throws IteratorOutOfBoundsException{
		try{
			if(i3)
				cell.accessCellBuffer()[y][x] = value;
			else
				throw new IteratorOutOfBoundsException();
		}catch(ArrayIndexOutOfBoundsException e){
			throw new BugEncounterException();
		}
	}
	
	/**
	 * This method will return the element within the associated {@link Cell} for the current location of 
	 * iterator.
	 * <p>If the current location of iterator is not within the valid bounds as specified by the set
	 * Iterator2DBound this method will throw an IteratorOutOfBounds Exception. Check {@link #isBound()}
	 * before accessing the element for current location of the iterator.
	 * @return float value
	 * @throws IteratorOutOfBoundsException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public float get() throws IteratorOutOfBoundsException{
		try{
			if(i3)
				return cell.accessCellBuffer()[y][x];
			else throw new IteratorOutOfBoundsException();
		}catch(ArrayIndexOutOfBoundsException e){
			throw new BugEncounterException();
		}
	}
	
	/**
	 * This method will return the element within the associated {@link Cell} for the current location of 
	 * iterator and moves the iterator to next valid location.
	 * <p>If the current location of iterator is not within the valid bounds as specified by the set
	 * Iterator2DBound this method will throw an IteratorOutOfBounds Exception. Check {@link #isBound()}
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
	 * This method will return the element within the associated {@link Cell} for the current location of 
	 * iterator and moves the iterator to previous valid location.
	 * <p>If the current location of iterator is not within the valid bounds as specified by the set
	 * Iterator2DBound this method will throw an IteratorOutOfBounds Exception. Check {@link #isBound()}
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
	 * This method will set the value of the element within the associated {@link Cell} for the current 
	 * location of iterator with value as given by the argument float 'value' and moves iterator to next
	 * valid location.
	 * <p>If the current location of iterator is not within the valid bounds as specified by the set
	 * Iterator2DBound this method will throw an IteratorOutOfBounds Exception. Check {@link #isBound()}
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
	 * This method will set the value of the element within the associated {@link Cell} for the current 
	 * location of iterator with value as given by the argument float 'value' and moves iterator to previous
	 * valid location.
	 * <p>If the current location of iterator is not within the valid bounds as specified by the set
	 * Iterator2DBound this method will throw an IteratorOutOfBounds Exception. Check {@link #isBound()}
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
