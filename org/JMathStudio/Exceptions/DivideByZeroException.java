package org.JMathStudio.Exceptions;

public class DivideByZeroException extends Exception
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DivideByZeroException()
	{
		super();
	}
	
	public DivideByZeroException(String message)
	{
		super(message);
	}

}
