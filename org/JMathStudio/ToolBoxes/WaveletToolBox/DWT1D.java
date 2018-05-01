package org.JMathStudio.ToolBoxes.WaveletToolBox;

import org.JMathStudio.DataStructure.Vector.Vector;
import org.JMathStudio.DataStructure.Vector.VectorMath;
import org.JMathStudio.DataStructure.Vector.VectorStack;
import org.JMathStudio.DataStructure.Vector.VectorTools;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.DimensionMismatchException;
import org.JMathStudio.Exceptions.IllegalArgumentException;

/**
 * This class define 1D discrete Wavelet transform (DWT) operation on a discrete real signal.
 * <p>A discrete real signal will be represented by a {@link Vector} object.
 * <p>1D DWT consists of following two operations,
 * <p><i>Analytical or Decomposition</i>
 * <p>This operation decomposes a discrete real signal in to a set of wavelet coefficients.
 * <p><i>Synthesis or Reconstruction</i>
 * <p>This operation synthesis or reconstructs a discrete real signal from wavelet coefficients.
 * <p>1D {@link Wavelet} coefficients will be represented by a {@link DWT1DCoeff} object.
 * <pre>Usage:
 * Let 'a' be a valid Vector object.
 * 
 * DWT1D dwt = new DWT1D();//Create an instance of DWT1D.
 * Wavelet wavelet = WaveletFactory.getDB2Wavelet();//Create required Wavelet for decomposition.
 * 
 * DWT1DCoeff coeff = dwt.dwt(a, 3, wavelet);//Compute DWT coefficients for signal as represented
 * by input Vector 'a' for given levels.
 * 
 * Vector recover = dwt.idwt(coeff, coeff.accessAssociatedWavelet());//Recover original signal from
 * its DWT coefficients.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class DWT1D 
{
	/**
	 * This method will perform a 1D DWT Analytical or Decomposition operation on a discrete real signal
	 * as represented by the {@link Vector} 'vector' with the analysing {@link Wavelet} as specified 
	 * by the argument 'wavelet' and level of decomposition as given by the argument 'level'.
	 * <p>The set of wavelet decomposition coefficient for all levels will be return as a {@link DWT1DCoeff}.
	 * <p>The argument 'level' should be more than 0 and length of Vector 'vector' should be more than
	 * '1' else this method will throw an IllegalArgument Exception. 
	 * <p>The argument 'wavelet' is retain as reference within the return {@link DWT1DCoeff}.
	 * 
	 * @param Vector vector
	 * @param int level
	 * @param Wavelet wavelet
	 * @return DWT1DCoeff
	 * @throws IllegalArgumentException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public DWT1DCoeff dwt(Vector vector,int level,Wavelet wavelet) throws IllegalArgumentException
	{
		if(level < 1)
		{
			throw new IllegalArgumentException();
		}
		if(vector.length() <2)
			throw new IllegalArgumentException();

		VectorStack[] decomposition = new VectorStack[level];
		Vector currentApproximate = vector;

		boolean[] evenOdd = new boolean[level];

		for(int i=0;i<level;i++)
		{
			if(currentApproximate.length()%2 == 0)
			{
				evenOdd[i] = true;
			}
			else
			{
				evenOdd[i] = false;
			}
			decomposition[i] = f4(currentApproximate, wavelet);
			currentApproximate = decomposition[i].accessVector(0);
		}

		DWT1DCoeff coeff = new DWT1DCoeff(decomposition,evenOdd);
		coeff.assignAssociatedWavelet(wavelet);

		return coeff;

	}

	/**
	 * This method will perform a 1D DWT Synthesis or Reconstruction operation using a set of 
	 * DWT coefficient as represented by the argument {@link DWT1DCoeff} 'coeff' with the synthesising
	 * {@link Wavelet} as specified by the argument 'wavelet'.
	 * <p>The reconstructed signal will be return as a Vector.
	 *     
	 * @param DWT1DCoeff coeff
	 * @param Wavelet wavelet
	 * @return Vector
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public Vector idwt(DWT1DCoeff coeff,Wavelet wavelet) 
	{
		VectorStack[] decomposition= coeff.f2();
		int level = decomposition.length;
		boolean[] evenOdd = coeff.f0();

		Vector currentApproximate = decomposition[level-1].accessVector(0);

		for(int i = level-1;i>=0;i--)
		{
			decomposition[i].replace(currentApproximate,0);
			try {
				currentApproximate = f2(decomposition[i], wavelet, evenOdd[i]);
			} catch (IllegalArgumentException e)
			{
				throw new BugEncounterException();
			}

		}

		return currentApproximate;
	}

	private VectorStack f4(Vector input,Wavelet wave) 
	{
		Vector low = new Vector(wave.accessLD());
		Vector high = new Vector(wave.accessHD());

		Vector l,h;

		l = f6(f0(input, low,false));
		h = f6(f0(input, high,false));

		VectorStack decomposition = new VectorStack();

		decomposition.addVector(l);
		decomposition.addVector(h);

		return decomposition;
	}

	private Vector f2(VectorStack input,Wavelet wave,boolean evenOdd) throws IllegalArgumentException
	{
		if(input.size() != 2)
		{
			throw new IllegalArgumentException();
		}

		Vector low = new Vector(wave.accessLR());
		Vector high = new Vector(wave.accessHR());

		Vector l,h;

		try 
		{
			l = f3(input.accessVector(0));
			l = f0(l, low,true);
			h = f3(input.accessVector(1));
			h = f0(h,high,true);

			VectorTools tools = new VectorTools();

			Vector tmp = VectorMath.add(l,h);
			//result starts from index n-1 and result length is less by 2*n+3
			//here n is number of coefficient in filters, for even original result.

			int length;

			if(evenOdd)
				length = tmp.length() - (2*low.length())+3;
			else
				length = tmp.length() - (2*low.length())+2;

			return tools.subVector(tmp,low.length()-1,length);

		}
		catch (IllegalArgumentException e) 
		{
			throw new BugEncounterException();
		} catch (DimensionMismatchException e) {
			throw new BugEncounterException();
		}

	}

	private Vector f6(Vector signal) 
	{
		if(signal.length()<=2)
		{
			return new Vector(new float[]{signal.getElement(0)});
		}
		else
		{
			Vector result = new Vector(signal.length()/2);

			for(int i=0;i<result.length();i++)
			{
				result.setElement(signal.getElement(i*2+1), i);
			}

			return result;
		}
	}

	private Vector f3(Vector signal) 
	{
		Vector result = new Vector(signal.length()*2);

		for(int i=0;i<signal.length();i++)
		{
			result.setElement(signal.getElement(i), 2*i+1);
		}

		return result;		
	}

	//Convolve linearly with circular extension padding.
	//Add phase term with polarity as specified. This should be opposite for
	//forward and backward convolution to cancel out phase term from result.
	//Note: Phase term is removed to keep coefficients at the centre.
	//With Phase term removal, the result is not exact. The boundary values are
	//slightly different. However, coefficients are not shifted.
	//Without phase term removal, result is exact but coefficients are slightly
	//shifted.
	private Vector f0(Vector signal,Vector impulse,boolean backward)
	{
		int l1 = signal.length();
		int l2 = impulse.length();

		int N = impulse.length()/4;

		if(backward)
			N = -N;

		Vector result = new Vector(l1+l2-1);

		for(int x=0;x<result.length();x++)
		{
			float tmp=0;
			int i1 = x+N;
			int i2 = 0;			 

			while(i2<l2)
			{
				tmp+=signal.getElementWithPadding(i1)*impulse.getElement(i2);
				i1--;
				i2++;
			}

			result.setElement(tmp, x);

		}

		return result;

	}
}
