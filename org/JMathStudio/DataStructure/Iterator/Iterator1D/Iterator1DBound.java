package org.JMathStudio.DataStructure.Iterator.Iterator1D;

import org.JMathStudio.Exceptions.IllegalArgumentException;

/**
 * This class define Iterator1DBound which specify the sub index bounds for a 1D Iterator.
 * <p>See {@link Abstract1DIterator} for more information on 1D iterators.
 * <p>1D Iterators operate within the bounds as set by the Iterator1DBound.
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public class Iterator1DBound {

	//This should be immutable class.
	int ox;
	int ex;
	int l;
	
	/**
	 * This will create an Iterator1DBound with origin index at location 'ox' and length 'l' respectively.
	 * <p>Thus the Iterator1DBound shall span the valid indexes from location 'ox' till (ox+l-1).
	 * <p>If the value of argument 'ox' is less than 0 this method will throw an IllegalArgument Exception.
	 * <p>Further, if the value of argument 'l' is less than 1 this method will throw an IllegalArgument
	 * Exception.
	 * @param int ox
	 * @param int l
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Iterator1DBound(int ox,int l) throws IllegalArgumentException{
		if(ox < 0 || l <=0)
			throw new IllegalArgumentException();
		else{
			this.ox = ox;
			this.l = l;
			this.ex = ox + l -1;
		}
	}
	
	/**
	 * This method checks if the index space as define by the Iterator1DBound 'bound' is within or sub index space
	 * of the index space as defined by the given Iterator1DBound.
	 * @param Iterator1DBound bound
	 * @return boolean
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public boolean isSubBounds(Iterator1DBound bound){
		return (bound.ox >= ox && bound.ex <= ex);
	}
	
	/**
	 * This method checks if the index location as specified by the argument 'x' falls within the valid
	 * bounds for given Iterator1DBound.
	 * <p>If the origin of the Iterator1DBound is set at 'ox' with length 'l'; this method checks
	 * for condition,
	 * <p><i>(ox <= x && x < ox+l)</i>
	 * @param int x
	 * @return boolean
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public boolean isWithinBounds(int x){
		return (x >= ox && x <= ex);
	}
	
	/**
	 * This method checks if the index location as specified by the argument 'x' defines the origin/start
	 * of the given Iterator1DBound.
	 * @param int x
	 * @return boolean
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public boolean isAtStart(int x){
		return (x==ox);
	}
	
	/**
	 * This method checks if the index location as specified by the argument 'x' defines the end
	 * of the given Iterator1DBound.
	 * @param int x
	 * @return boolean
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public boolean isAtEnd(int x){
		return (x==ex);
	}
}
