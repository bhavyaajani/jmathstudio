package org.JMathStudio.SignalToolkit.GeneralTools;

import org.JMathStudio.DataStructure.Vector.Vector;
import org.JMathStudio.Exceptions.BugEncounterException;

/**
 * This class define various padding operations to pad a discrete real signal.
 * <p>A discrete real signal will be represented by a {@link Vector} object.
 * <pre>Usage:
 * Let 'a' be a valid Vector object.
 * 
 * SignalPadder sp = new SignalPadder();//Create an instance of SignalPadder.
 * 
 * Vector cir_pad = sp.padCircular(a, 3);//Pad signal as represented by input Vector, circularly
 * along its ends with specified amount.
 * 
 * Vector sym_pad = sp.padSymmetric(a, 4);//Pad signal as represented by input Vector, symmetrically
 * along its ends with specified amount.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class SignalPadder {
	
	/**
	 * This method will pad the discrete real signal as represented by the Vector 'signal'
	 * with zeroes and return the resultant padded signal as a Vector.
	 * <p>The argument 'padSize' specify the amount of padding of the signal. The signal is 
	 * padded along both the direction by the specified padding amount.
	 * <p>The argument 'padSize' should not be negative else this method will throw an 
	 * IllegalArgument Exception.
	 * <p>The resultant signal will have the length increased by the amount of 2*(padSize).
	 * @param Vector signal
	 * @param int padSize
	 * @return Vector
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector padZeroes(Vector signal, int padSize) throws IllegalArgumentException
	{
		if(padSize <0)
			throw new IllegalArgumentException();
		else
		{
			try{
			Vector result = new Vector(signal.length() + 2*padSize);
			
			for(int i=0;i<signal.length();i++)
			{
				result.setElement(signal.getElement(i), i+padSize);
			}
			
			return result;
			}catch(ArrayIndexOutOfBoundsException e)
			{
				throw new BugEncounterException();
			}
		}
	}

	/**
	 * This method will pad the discrete real signal as represented by the Vector 'signal'
	 * by replicating the boundary/edge element of the signal and return the resultant padded signal as a Vector.
	 * <p>The argument 'padSize' specify the amount of padding of the signal. The signal is 
	 * padded along both the direction by the specified padding amount.
	 * <p>The argument 'padSize' should not be negative else this method will throw an 
	 * IllegalArgument Exception.
	 * <p>The resultant signal will have the length increased by the amount of 2*(padSize).
	 * @param Vector signal
	 * @param int padSize
	 * @return Vector
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector padReplicate(Vector signal,int padSize) throws IllegalArgumentException
	{
		
		if(padSize <0)
			throw new IllegalArgumentException();
		else
		{
			try{
			Vector result = new Vector(signal.length() + 2*padSize);
			
			int L = signal.length();
			
			for(int i=0;i<result.length();i++)
			{
				float tmp;
					
					if(i<padSize)
						tmp = signal.getElement(0);
					else if(i>=padSize+L)
						tmp = signal.getElement(L-1);
					else
						tmp = signal.getElement(i-padSize);
						
					result.setElement(tmp, i);
				
			}
			
			return result;
			}catch(ArrayIndexOutOfBoundsException e)
			{
				throw new BugEncounterException();
			}
		}
	}
	
	/**
	 * This method will pad the discrete real signal as represented by the Vector 'signal'
	 * by circular replication of the elements of the signal and return the resultant padded signal as a Vector.
	 * <p>The argument 'padSize' specify the amount of padding of the signal. The signal is 
	 * padded along both the direction by the specified padding amount.
	 * <p>The argument 'padSize' should not be negative else this method will throw an 
	 * IllegalArgument Exception.
	 * <p>The resultant signal will have the length increased by the amount of 2*(padSize).
	 * @param Vector signal
	 * @param int padSize
	 * @return Vector
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector padCircular(Vector signal,int padSize) throws IllegalArgumentException
	{
		if(padSize <0)
			throw new IllegalArgumentException();
		else
		{
			try{
			Vector result = new Vector(signal.length() + 2*padSize);
		
			int L = signal.length();
			
			for(int i=0;i<result.length();i++)
			{
				//Do not change this. Difficult to understand formulation.
				//But correct.
				int l = (i+(padSize)*(L-1))%L;
				result.setElement(signal.getElement(l), i);
				
			}
			
			return result;
			}catch(ArrayIndexOutOfBoundsException e)
			{
				throw new BugEncounterException();
			}
		}
	}
	
	/**
	 * This method will pad the discrete real signal as represented by the Vector 'signal'
	 * by symmetric circular replication of the elements of the signal and return the resultant 
	 * padded signal as a Vector.
	 * <p>The argument 'padSize' specify the amount of padding of the signal. The signal is 
	 * padded along both the direction by the specified padding amount.
	 * <p>The argument 'padSize' should not be negative else this method will throw an 
	 * IllegalArgument Exception.
	 * <p>The resultant signal will have the length increased by the amount of 2*(padSize).
	 * @param Vector signal
	 * @param int padSize
	 * @return Vector
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector padSymmetric(Vector signal,int padSize) throws IllegalArgumentException
	{
		if(padSize <0)
			throw new IllegalArgumentException();
		else
		{
			try{
			Vector result = new Vector(signal.length() + 2*padSize);
		
			int L = signal.length();
		
			int twiceL_1 = 2*(L-1);
			
			for(int i=0;i<result.length();i++)
			{
				int l = i-padSize;
				l = Math.abs(l%twiceL_1);
				
				if(l >= L)
					l = twiceL_1 - l;
				
				result.setElement(signal.getElement(l), i);
				
			}
			
			return result;
			}catch(ArrayIndexOutOfBoundsException e)
			{
				throw new BugEncounterException();
			}
		}
	}

}
