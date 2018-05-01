package org.JMathStudio.DataStructure.Iterator.Iterator2D;

import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.IllegalArgumentException;
import org.JMathStudio.Exceptions.IteratorOutOfBoundsException;
import org.JMathStudio.PixelImageToolkit.UIntPixelImage.AbstractUIntPixelImage;

/**
 * This class define a 2D Iterator for iterating over the elements of an UIntPixelImages as represented by
 * an {@link AbstractUIntPixelImage}, which are defined over a 2D index space.
 * <p>See {@link Abstract2DIterator} for more information on the 2D Iterator.
 * 
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class UIntPixelImageIterator extends Abstract2DIterator{

	private AbstractUIntPixelImage image;
	
	/**
	 * This method will create a UIntPixelImageIterator associated with the given {@link AbstractUIntPixelImage} 'image'. 
	 * <p>For safety reason avoid creating an instance of {@link UIntPixelImageIterator} directly but get the
	 * instance from {@link AbstractUIntPixelImage#getAssociatedIterator()}, which maintain a single instance of 
	 * UIntPixelImageIterator for a given AbstractUIntPixelImage.
	 * @param AbstractUIntPixelImage image
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public UIntPixelImageIterator(AbstractUIntPixelImage image){
		this.image = image;
		f1(image.getLargestIterableBounds());
	}
	
	/**
	 * This method reset the {@link Iterator2DBound} of the given UIntPixelImageIterator with Iterator2DBound
	 * 'bound'.
	 * <p>If the Iterator2DBound 'bound' is not completely within the index space of the associated AbstractUIntPixelImage 
	 * this method will throw an IteratorOutOfBounds Exception. See {@link AbstractUIntPixelImage#getLargestIterableBounds()}.
	 * @param Iterator2DBound bound
	 * @throws IteratorOutOfBoundsException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void assignBounds(Iterator2DBound bound) throws IteratorOutOfBoundsException{
		if(!image.getLargestIterableBounds().isSubBounds(bound))
			throw new IteratorOutOfBoundsException();
		else{
			f1(bound);
		}
	}
	
	/**
	 * This method will set the value of the element within the associated {@link AbstractUIntPixelImage} 
	 * for the current location of iterator with value as given by the argument 'value'.
	 * <p>If the current location of iterator is not within the valid bounds as specified by the set
	 * Iterator2DBound this method will throw an IteratorOutOfBounds Exception. Check {@link #isBound()}
	 * before manipulating the element for current location of the iterator.
	 * <p>If the argument 'value' is not within the valid range for the associated AbstractUIntPixelImage this
	 * method will throw an IllegalArgument Exception.
	 * @param int value
	 * @throws IteratorOutOfBoundsException
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void set(int value) throws IteratorOutOfBoundsException, IllegalArgumentException{
		try{
			if(i3)
				image.setPixel(value, y, x);
			else
				throw new IteratorOutOfBoundsException();
		}catch(ArrayIndexOutOfBoundsException e){
			throw new BugEncounterException();
		}
	}
	
	/**
	 * This method will return the element within the associated {@link AbstractUIntPixelImage} for the current 
	 * location of iterator.
	 * <p>If the current location of iterator is not within the valid bounds as specified by the set
	 * Iterator2DBound this method will throw an IteratorOutOfBounds Exception. Check {@link #isBound()}
	 * before accessing the element for current location of the iterator.
	 * @return int value
	 * @throws IteratorOutOfBoundsException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public int get() throws IteratorOutOfBoundsException{
		try{
			if(i3)
				return image.getPixel(y, x);
			else 
				throw new IteratorOutOfBoundsException();
		}catch(ArrayIndexOutOfBoundsException e){
			throw new BugEncounterException();
		}
	}
	
	/**
	 * This method will return the element within the associated {@link AbstractUIntPixelImage} for the current 
	 * location of iterator and moves the iterator to next valid location.
	 * <p>If the current location of iterator is not within the valid bounds as specified by the set
	 * Iterator2DBound this method will throw an IteratorOutOfBounds Exception. Check {@link #isBound()}
	 * before accessing the element for current location of the iterator.
	 * @return int value
	 * @throws IteratorOutOfBoundsException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public int getAndNext() throws IteratorOutOfBoundsException{
		int value = get();
		next();
		return value;
	}
	
	/**
	 * This method will return the element within the associated {@link AbstractUIntPixelImage} for the current 
	 * location of iterator and moves the iterator to previous valid location.
	 * <p>If the current location of iterator is not within the valid bounds as specified by the set
	 * Iterator2DBound this method will throw an IteratorOutOfBounds Exception. Check {@link #isBound()}
	 * before accessing the element for current location of the iterator.
	 * @return int value
	 * @throws IteratorOutOfBoundsException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public int getAndPrevious() throws IteratorOutOfBoundsException{
		int value = get();
		previous();
		return value;
	}
	
	/**
	 * This method will set the value of the element within the associated {@link AbstractUIntPixelImage} for the 
	 * current location of iterator with value as given by the argument int 'value' and moves iterator to next
	 * valid location.
	 * <p>If the current location of iterator is not within the valid bounds as specified by the set
	 * Iterator2DBound this method will throw an IteratorOutOfBounds Exception. Check {@link #isBound()}
	 * before manipulating the element for current location of the iterator.
	 * <p>If the argument 'value' is not within the valid range for the associated AbstractUIntPixelImage this
	 * method will throw an IllegalArgument Exception.
	 * @param int value
	 * @throws IteratorOutOfBoundsException
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void setAndNext(int value) throws IteratorOutOfBoundsException, IllegalArgumentException{
		set(value);
		next();
	}
	
	/**
	 * This method will set the value of the element within the associated {@link AbstractUIntPixelImage} for the 
	 * current location of iterator with value as given by the argument int 'value' and moves iterator to previous
	 * valid location.
	 * <p>If the current location of iterator is not within the valid bounds as specified by the set
	 * Iterator2DBound this method will throw an IteratorOutOfBounds Exception. Check {@link #isBound()}
	 * before manipulating the element for current location of the iterator.
	 * <p>If the argument 'value' is not within the valid range for the associated AbstractUIntPixelImage this
	 * method will throw an IllegalArgument Exception.
	 * @param int value
	 * @throws IteratorOutOfBoundsException
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void setAndPrevious(int value) throws IteratorOutOfBoundsException, IllegalArgumentException{
		set(value);
		previous();
	}
}
