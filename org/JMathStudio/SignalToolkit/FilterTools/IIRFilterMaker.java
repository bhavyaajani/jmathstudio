package org.JMathStudio.SignalToolkit.FilterTools;

import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.IllegalArgumentException;

/**
 * This class creates various useful IIR Filters.
 * <p>The IIR Filter will be represented by an IIRFilter object.
 * @see IIRFilter
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class IIRFilterMaker {

	/**
	 * This enumeration list out supported Filter type.
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	private static enum FilterType {LOWPASS,HIGHPASS,BANDPASS}

	//Ensure no instances are made of Utility class.
	private IIRFilterMaker(){}
    
  /**
   * This method will build a Low Pass ButterWorth filter with given specification.
   * <p>The argument 'order' indicate the order of the ButterWorth filter and
   * should be a positive integer.
   * <p>The argument 'fc' indicate the 3 db higher cut-off frequency for the given low pass
   * filter. The argument 'fc' should be a normalised frequency in the range of [0,0.5]. 
   * <p>If any of the input parameter is not valid this method will throw an IllegalArgument
   * Exception.
   * <p>The required filter is return as an IIRFilter.
   * 
   * @param int order
   * @param float fc
   * @return IIRFilter
   * @throws IllegalArgumentException
   * @see IIRFilter
   * @author  Ajani Bhavya - (jmathstudio@gmail.com)
   */
  public final static IIRFilter butterWorthLowPass(int order,float fc) throws IllegalArgumentException
  {
  	if(order<1 || fc<0 || fc>0.5f)
  		throw new IllegalArgumentException();
  	
     return f5(FilterType.LOWPASS, order, 0, fc);
     }
  
  /**
   * This method will build a High Pass ButterWorth filter with given specification.
   * <p>The argument 'order' indicate the order of the ButterWorth filter and
   * should be a positive integer.
   * <p>The argument 'fc' indicate the 3 db lower cut-off frequency for the given high pass
   * filter. The argument 'fc' should be a normalised frequency in the range of [0,0.5]. 
   * <p>If any of the input parameter is not valid this method will throw an IllegalArgument
   * Exception.
   * <p>The required filter is return as an IIRFilter.
   * 
   * @param int order
   * @param float fc
   * @return IIRFilter
   * @throws IllegalArgumentException
   * @see IIRFilter
   * @author  Ajani Bhavya - (jmathstudio@gmail.com)
   */
  public final static IIRFilter butterWorthHighPass(int order,float fc) throws IllegalArgumentException
  {
  	if(order<1 || fc<0 || fc>0.5f)
  		throw new IllegalArgumentException();
  	
     return f5(FilterType.HIGHPASS, order, fc, 0.5f);
         
  }
  
  /**
   * This method will build a Band Pass ButterWorth filter with given specification.
   * <p>The argument 'order' indicate the order of the ButterWorth filter and should 
   * be a positive integer.
   * <p>The argument 'fl' indicate the lower 3 db cut-off frequency for the given band pass
   * filter. 
   * <p>The argument 'fh' indicate the upper 3 db cut-off frequency for the given band pass
   * filter.
   * <p>The arguments 'fl' and 'fh' should be a normalised frequency in the range of [0,0.5].
   * Further the argument 'fl' should be less than 'fh'. 
   * <p>If any of the input parameter is not valid this method will throw an IllegalArgument
   * Exception.
   * <p>The required filter is return as an IIRFilter.
   * 
   * @param int order
   * @param float fl
   * @param float fh
   * @return IIRFilter
   * @throws IllegalArgumentException
   * @see IIRFilter
   * @author  Ajani Bhavya - (jmathstudio@gmail.com)
   */
  public final static IIRFilter butterWorthBandPass(int order,float fl,float fh) throws IllegalArgumentException
  {
  	if(order<1 || fl<0 || fl>0.5f || fh<0 || fh>0.5)
  		throw new IllegalArgumentException();
  	
  	if(fl>=fh)
  		throw new IllegalArgumentException();
  	
      if (f0(order)) 
      {
          order = order+1;
      }     
     
      return f5(FilterType.BANDPASS, order, fl, fh);
     }
    
  /**
   * This method will build a Low Pass Chebyshev Type -I filter with given specification.
   * <p>The argument 'order' indicate the order of the Chebyshev filter and
   * should be a positive integer.
   * <p>The argument 'ripple' specify the ripple factor desired for given filter.
   * The argument 'ripple' should be a positive value greater than '0'.
   * <p>The argument 'fc' indicate the higher 3 db cut-off frequency for the given low pass
   * filter. The argument 'fc' should be a normalised frequency in the range of [0,0.5]. 
   * <p>If any of the input parameter is not valid this method will throw an IllegalArgument
   * Exception.
   * The required filter is return as an IIRFilter.
   * 
   * @param int order
   * @param float ripple
   * @param float fc
   * @return IIRFilter
   * @throws IllegalArgumentException
   * @see IIRFilter
   * @author  Ajani Bhavya - (jmathstudio@gmail.com)
   */
  public final static IIRFilter chebyshevLowPass(int order,float ripple,float fc) throws IllegalArgumentException
  {
  	if(order<1 || fc<0 || fc>0.5f || ripple<=0)
  		throw new IllegalArgumentException();
  	
      return f7(FilterType.LOWPASS, order, ripple, 0, fc);
     }
  
  /**
   * This method will build a High Pass Chebyshev Type -I filter with given specification.
   * <p>The argument 'order' indicate the order of the Chebyshev filter and
   * should be a positive integer.
   * <p>The argument 'ripple' specify the ripple factor desired for given filter.
   * The argument 'ripple' should be a positive value greater than '0'.
   * <p>The argument 'fc' indicate the lower 3 db cut-off frequency for the given high pass
   * filter. The argument 'fc' should be a normalised frequency in the range of [0,0.5]. 
   * <p>If any of the input parameter is not valid this method will throw an IllegalArgument
   * Exception.
   * The required filter is return as an IIRFilter.
   * 
   * @param int order
   * @param float ripple
   * @param float fc
   * @return IIRFilter
   * @throws IllegalArgumentException
   * @see IIRFilter
   * @author  Ajani Bhavya - (jmathstudio@gmail.com)
   */
  public final static IIRFilter chebyshevHighPass(int order,float ripple,float fc) throws IllegalArgumentException
  {
  	if(order<1 || fc<0 || fc>0.5f || ripple<=0)
  		throw new IllegalArgumentException();
  	
      return f7(FilterType.HIGHPASS, order, ripple, fc, 0.5f);
  }
  
  /**
   * This method will build a Band Pass Chebyshev Type -I filter with given specification.
   * <p>The argument 'order' indicate the order of the Chebyshev filter and
   * should be a positive integer.
   * <p>The argument 'ripple' specify the ripple factor desired for given filter.
   * The argument 'ripple' should be a positive value greater than '0'.
   * 
   * <p>The argument 'fl' indicate the lower 3 db cut-off frequency for the given band pass
   * filter. 
   * <p>The argument 'fh' indicate the upper 3 db cut-off frequency for the given band pass
   * filter.
   * <p>The arguments 'fl' and 'fh' should be a normalised frequency in the range of [0,0.5].
   * Further the argument 'fl' should be less than 'fh'. 
   * <p>If any of the input parameter is not valid this method will throw an IllegalArgument
   * Exception.    
   * The required filter is return as an IIRFilter.
   * 
   * @param int order
   * @param float ripple
   * @param float fl
   * @param float fh
   * @return IIRFilter
   * @throws IllegalArgumentException
   * @see IIRFilter
   * @author  Ajani Bhavya - (jmathstudio@gmail.com)
   */
  public final static IIRFilter chebyshevBandPass(int order,float ripple,float fl,float fh) throws IllegalArgumentException
  {
  	if(order<1 || fl<0 || fl>0.5f || fh<0 || fh>0.5 || ripple<=0)
  		throw new IllegalArgumentException();
  	
  	if(fl>=fh)
  		throw new IllegalArgumentException();
  	
      if (f0(order)) 
      {
          order = order + 1;
      }     
     
      return f7(FilterType.BANDPASS, order, ripple, fl, fh);
  }
  
  /**
   * This method will build a EWMA or Exponentially Weightage Moving Average
   * IIR Filter.
   * <p>The argument 'alpha' indicate the smoothing factor and should be in the
   * range of (0,1] else this method will throw an IllegalArgument Exception. 
   * <p>EWMA IIR filter is characterised by following difference equation,
   * <p><i>Y(n) = alpha * X(n) + (1 - alpha)*Y(n-1).</i>
   * @param float alpha
   * @return IIRFilter
   * @throws IllegalArgumentException
   * @see {@link IIRFilter}
   * @author Ajani Bhavya - (jmathstudio@gmail.com)
   */
  public final static IIRFilter EWMAFilter(float alpha) throws IllegalArgumentException
  {
	  if(alpha <=0 || alpha >1)
		  throw new IllegalArgumentException();
	  
	  float[] numCoeff = new float[]{alpha};
	  float[] denCoeff = new float[]{(alpha -1)};
	  
	  return new IIRFilter(numCoeff,denCoeff);
  }

  private static double f2(double x) {
    return x * x;
  }

  private static boolean f0(int n) {
    // returns true if n odd
    return (n % 2) != 0;
  }

  private static IIRFilter f5(FilterType type,int order,float fl,float fh)
  {
	  	double[] numeratorCoeff = new double[order + 1];
	    double[] denominatorCoeff = new double[order + 1];
	    double[] newA = new double[order + 1];
	    double[] newB = new double[order + 1];
	    
	    
	 // determines poles and zeros of IIR filter
	    // based on bilinear transform method
	    double[] pReal  = new double[order + 1];
	    double[] pImag  = new double[order + 1];
	    double[] z      = new double[order + 1];
	  
	    for(int k = 1; k <= order; k++) 
	    {
	      pReal[k] = 0.0;
	      pImag[k] = 0.0;
	    }
	    // Butterworth, Chebyshev parameters
	    int n = order;
	    
	    if (type == FilterType.BANDPASS) 
	    	n = n/2;
	    
	    int ir = n % 2;
	    int n1 = n + ir;
	    int n2 = (3*n + ir)/2 - 1;
	    double f1;
	    
	    if(type == FilterType.LOWPASS)
	    	f1 = fh;
	    else if(type == FilterType.HIGHPASS)
	    	f1 = 0.5 - fl;
	    else if(type == FilterType.BANDPASS)
	    	f1 = fh - fl;
	    else
	    	f1 = 0.0;
	    
	    double tanw1 = Math.tan(Math.PI*f1);
	    double tansqw1 = f2(tanw1);
	    // Real and Imaginary parts of low-pass poles
	    double t, r = 1.0, i = 1.0;
	    for (int k = n1; k <= n2; k++) 
	    {
	      t = 0.5*(2*k + 1 - ir)*Math.PI/(double)n;
	      
	       	  double b3 = 1.0 - 2.0*tanw1*Math.cos(t) + tansqw1;
	          r = (1.0 - tansqw1)/b3;
	          i = 2.0*tanw1*Math.sin(t)/b3;
	           
	      int m = 2*(n2 - k) + 1;
	      pReal[m + ir]     = r;
	      pImag[m + ir]     = Math.abs(i);
	      pReal[m + ir + 1] = r;
	      pImag[m + ir + 1] = - Math.abs(i);
	    }
	    if (f0(n)) 
	    {
	      r = (1.0 - tansqw1)/(1.0 + 2.0*tanw1+tansqw1);
	      pReal[1] = r;
	      pImag[1] = 0.0;
	    }
	    
	    if(type == FilterType.LOWPASS)
	    {
	    	for (int m = 1; m <= n; m++)
	            z[m]= -1.0;
	    }
	    else if(type == FilterType.HIGHPASS)
	    {
	    	// low-pass to high-pass transformation
	        for (int m = 1; m <= n; m++) {
	          pReal[m] = -pReal[m];
	          z[m]     = 1.0;
	        }
	    }
	    else if(type == FilterType.BANDPASS)
	    {
	    	// low-pass to bandpass transformation
	        for (int m = 1; m <= n; m++) {
	          z[m]  =  1.0;
	          z[m+n]= -1.0;
	        }
	        double f4 = Math.PI*fl;
	        double f5 = Math.PI*fh;
	        /*
	        check this bit ... needs value for gp to adjust critical freqs
	        if (prototype == BUTTERWORTH) {
	          f4 = f4/Math.exp(0.5*Math.log(gp)/n);
	          f5 = fN - (fN - f5)/Math.exp(0.5*Math.log(gp)/n);
	        }
	        */
	        double aa = Math.cos(f4 + f5)/Math.cos(f5 - f4);
	        double aR, aI, h1, h2, p1R, p2R, p1I, p2I;
	        for (int m1 = 0; m1 <= (order - 1)/2; m1++) {
	          int m = 1 + 2*m1;
	          aR = pReal[m];
	          aI = pImag[m];
	          if (Math.abs(aI) < 0.0001) {
	            h1 = 0.5*aa*(1.0 + aR);
	            h2 = f2(h1) - aR;
	            if (h2 > 0.0) {
	              p1R = h1 + Math.sqrt(h2);
	              p2R = h1 - Math.sqrt(h2);
	              p1I = 0.0;
	              p2I = 0.0;
	            }
	            else {
	              p1R = h1;
	              p2R = h1;
	              p1I = Math.sqrt(Math.abs(h2));
	              p2I = -p1I;
	            }
	          }
	          else {
	            double fR = aa*0.5*(1.0 + aR);
	            double fI = aa*0.5*aI;
	            double gR = f2(fR) - f2(fI) - aR;
	            double gI = 2*fR*fI - aI;
	            double sR = Math.sqrt(0.5*Math.abs(gR + Math.sqrt(f2(gR) + f2(gI))));
	            double sI = gI/(2.0*sR);
	            p1R = fR + sR;
	            p1I = fI + sI;
	            p2R = fR - sR;
	            p2I = fI - sI;
	          }
	          pReal[m]   = p1R;
	          pReal[m+1] = p2R;
	          pImag[m]   = p1I;
	          pImag[m+1] = p2I;
	        } // end of m1 for-loop
	        if (f0(n)) {
	          pReal[2] = pReal[n+1];
	          pImag[2] = pImag[n+1];
	        }
	        for (int k = n; k >= 1; k--) {
	          int m = 2*k - 1;
	          pReal[m]   =   pReal[k];
	          pReal[m+1] =   pReal[k];
	          pImag[m]   =   Math.abs(pImag[k]);
	          pImag[m+1] = - Math.abs(pImag[k]);
	        }
	      }

	    
	    
	    // compute filter coefficients from pole/zero values
	    numeratorCoeff[0]= 1.0;
	    denominatorCoeff[0]= 1.0;
	    for (int i1 = 1; i1 <= order; i1++)
	    {
	      numeratorCoeff[i1] = 0.0;
	      denominatorCoeff[i1] = 0.0;
	    }
	    int k = 0;
	    n = order;
	    int pairs = n/2;
	    if (f0(order)) {
	     // first subfilter is first order
	      numeratorCoeff[1] = - z[1];
	      denominatorCoeff[1] = - pReal[1];
	      k = 1;
	    }
	    for (int p = 1; p <= pairs; p++) {
	      int m = 2*p - 1 + k;
	      double alpha1 = - (z[m] + z[m+1]);
	      double alpha2 = z[m]*z[m+1];
	      double beta1  = - 2.0*pReal[m];
	      double beta2  = f2(pReal[m]) + f2(pImag[m]);
	      newA[1] = numeratorCoeff[1] + alpha1*numeratorCoeff[0];
	      newB[1] = denominatorCoeff[1] + beta1 *denominatorCoeff[0];
	      for (int i1 = 2; i1 <= n; i1++) {
	        newA[i1] = numeratorCoeff[i1] + alpha1*numeratorCoeff[i1-1] + alpha2*numeratorCoeff[i1-2];
	        newB[i1] = denominatorCoeff[i1] + beta1 *denominatorCoeff[i1-1] + beta2 *denominatorCoeff[i1-2];
	      }
	      for (int i1 = 1; i1 <= n; i1++) {
	        numeratorCoeff[i1] = newA[i1];
	        denominatorCoeff[i1] = newB[i1];
	      }
	    }
	    
	    
	    
	 // filter gain at uniform frequency intervals
		int freqPoints = 5;
	    float[] g = new float[freqPoints+1];
	    double theta, s, c, sac, sas, sbc, sbs;
	    float gMax = -100.0f;
	    float sc = 10.0f/(float)Math.log(10.0f);
	    t = Math.PI / freqPoints;
	    for (int i1 = 0; i1 <= freqPoints; i1++) {
	      theta = i1*t;
	      if (i1 == 0) theta = Math.PI*0.0001;
	      if (i1 == freqPoints) theta = Math.PI*0.9999;
	      sac = 0.0f;
	      sas = 0.0f;
	      sbc = 0.0f;
	      sbs = 0.0f;
	      for (int k1 = 0; k1 <= order; k1++) {
	        c = Math.cos(k1*theta);
	        s = Math.sin(k1*theta);
	        sac += c*numeratorCoeff[k1];
	        sas += s*numeratorCoeff[k1];
	        sbc += c*denominatorCoeff[k1];
	        sbs += s*denominatorCoeff[k1];
	      }
	      g[i1] = sc*(float)Math.log((f2(sac) + f2(sas))/(f2(sbc) + f2(sbs)));
	      gMax = Math.max(gMax, g[i1]);
	    }
	    // normalise to 0 dB maximum gain
	    for (int i1=0; i1<=freqPoints; i1++) 
	    	g[i1] -= gMax;
	    // normalise numerator (a) coefficients
	    float normFactor = (float)Math.pow(10.0, -0.05*gMax);
	    for (int i1=0; i1<=order; i1++) 
	    	numeratorCoeff[i1] *= normFactor;
	    
	    float[] num = new float[numeratorCoeff.length];
	    float[] den = new float[denominatorCoeff.length];
	    
		 for(int l=0;l<num.length;l++)
		 {
			  num[l] = (float) numeratorCoeff[l];
			  den[l] = (float) denominatorCoeff[l];
		 }
		 
	    try {
			return new IIRFilter(num,den);
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}

  }
  
  private static IIRFilter f7(FilterType type,int order,float ripple,float fl,float fh) 
  {
    double[] numeratorCoeff = new double[order + 1];
    double[]denominatorCoeff = new double[order + 1];
    double[] newA = new double[order + 1];
    double[] newB = new double[order + 1];
    
    
 // determines poles and zeros of IIR filter
    // based on bilinear transform method
    double[] pReal  = new double[order + 1];
    double[] pImag  = new double[order + 1];
    double[] z      = new double[order + 1];
    double ln10 = Math.log(10.0);
    for(int k = 1; k <= order; k++) 
    {
      pReal[k] = 0.0;
      pImag[k] = 0.0;
    }
    // Butterworth, Chebyshev parameters
    int n = order;
    
    if (type == FilterType.BANDPASS) 
    	n = n/2;
    
    int ir = n % 2;
    int n1 = n + ir;
    int n2 = (3*n + ir)/2 - 1;
    double f1;
    
    if(type == FilterType.LOWPASS)
    	f1 = fh;
    else if(type == FilterType.HIGHPASS)
    	f1 = 0.5 - fl;
    else if(type == FilterType.BANDPASS)
    	f1 = fh - fl;
    else
    	f1 = 0.0;
    
    double tanw1 = Math.tan(Math.PI*f1);
    // Real and Imaginary parts of low-pass poles
    double t, a = 1.0, r = 1.0, i = 1.0;
    for (int k = n1; k <= n2; k++) {
      t = 0.5*(2*k + 1 - ir)*Math.PI/(double)n;
      
       	  double d = 1.0 - Math.exp(-0.05*ripple*ln10);
          double e = 1.0 / Math.sqrt(1.0 / f2(1.0 - d) - 1.0);
          double x = Math.pow(Math.sqrt(e*e + 1.0) + e, 1.0/(double)n);
          a = 0.5*(x - 1.0/x);
          double b = 0.5*(x + 1.0/x);
          double c3 = a*tanw1*Math.cos(t);
          double c4 = b*tanw1*Math.sin(t);
          double c5 = f2(1.0 - c3) + f2(c4);
          r = 2.0*(1.0 - c3)/c5 - 1.0;
          i = 2.0*c4/c5;
      
      
      int m = 2*(n2 - k) + 1;
      pReal[m + ir]     = r;
      pImag[m + ir]     = Math.abs(i);
      pReal[m + ir + 1] = r;
      pImag[m + ir + 1] = - Math.abs(i);
    }
    if (f0(n)) 
    {
      r = 2.0/(1.0 + a*tanw1) - 1.0;
      pReal[1] = r;
      pImag[1] = 0.0;
    }
    
    if(type == FilterType.LOWPASS)
    {
    	for (int m = 1; m <= n; m++)
            z[m]= -1.0;
    }
    else if(type == FilterType.HIGHPASS)
    {
    	// low-pass to high-pass transformation
        for (int m = 1; m <= n; m++) {
          pReal[m] = -pReal[m];
          z[m]     = 1.0;
        }
    }
    else if(type == FilterType.BANDPASS)
    {
    	// low-pass to bandpass transformation
        for (int m = 1; m <= n; m++) {
          z[m]  =  1.0;
          z[m+n]= -1.0;
        }
        double f4 = Math.PI*fl;
        double f5 = Math.PI*fh;
        /*
        check this bit ... needs value for gp to adjust critical freqs
        if (prototype == BUTTERWORTH) {
          f4 = f4/Math.exp(0.5*Math.log(gp)/n);
          f5 = fN - (fN - f5)/Math.exp(0.5*Math.log(gp)/n);
        }
        */
        double aa = Math.cos(f4 + f5)/Math.cos(f5 - f4);
        double aR, aI, h1, h2, p1R, p2R, p1I, p2I;
        for (int m1 = 0; m1 <= (order - 1)/2; m1++) {
          int m = 1 + 2*m1;
          aR = pReal[m];
          aI = pImag[m];
          if (Math.abs(aI) < 0.0001) {
            h1 = 0.5*aa*(1.0 + aR);
            h2 = f2(h1) - aR;
            if (h2 > 0.0) {
              p1R = h1 + Math.sqrt(h2);
              p2R = h1 - Math.sqrt(h2);
              p1I = 0.0;
              p2I = 0.0;
            }
            else {
              p1R = h1;
              p2R = h1;
              p1I = Math.sqrt(Math.abs(h2));
              p2I = -p1I;
            }
          }
          else {
            double fR = aa*0.5*(1.0 + aR);
            double fI = aa*0.5*aI;
            double gR = f2(fR) - f2(fI) - aR;
            double gI = 2*fR*fI - aI;
            double sR = Math.sqrt(0.5*Math.abs(gR + Math.sqrt(f2(gR) + f2(gI))));
            double sI = gI/(2.0*sR);
            p1R = fR + sR;
            p1I = fI + sI;
            p2R = fR - sR;
            p2I = fI - sI;
          }
          pReal[m]   = p1R;
          pReal[m+1] = p2R;
          pImag[m]   = p1I;
          pImag[m+1] = p2I;
        } // end of m1 for-loop
        if (f0(n)) {
          pReal[2] = pReal[n+1];
          pImag[2] = pImag[n+1];
        }
        for (int k = n; k >= 1; k--) {
          int m = 2*k - 1;
          pReal[m]   =   pReal[k];
          pReal[m+1] =   pReal[k];
          pImag[m]   =   Math.abs(pImag[k]);
          pImag[m+1] = - Math.abs(pImag[k]);
        }
      }

    
    
    // compute filter coefficients from pole/zero values
    numeratorCoeff[0]= 1.0;
    denominatorCoeff[0]= 1.0;
    for (int i1 = 1; i1 <= order; i1++)
    {
      numeratorCoeff[i1] = 0.0;
      denominatorCoeff[i1] = 0.0;
    }
    int k = 0;
    n = order;
    int pairs = n/2;
    if (f0(order)) {
     // first subfilter is first order
      numeratorCoeff[1] = - z[1];
      denominatorCoeff[1] = - pReal[1];
      k = 1;
    }
    for (int p = 1; p <= pairs; p++) {
      int m = 2*p - 1 + k;
      double alpha1 = - (z[m] + z[m+1]);
      double alpha2 = z[m]*z[m+1];
      double beta1  = - 2.0*pReal[m];
      double beta2  = f2(pReal[m]) + f2(pImag[m]);
      newA[1] = numeratorCoeff[1] + alpha1*numeratorCoeff[0];
      newB[1] = denominatorCoeff[1] + beta1 *denominatorCoeff[0];
      for (int i1 = 2; i1 <= n; i1++) {
        newA[i1] = numeratorCoeff[i1] + alpha1*numeratorCoeff[i1-1] + alpha2*numeratorCoeff[i1-2];
        newB[i1] = denominatorCoeff[i1] + beta1 *denominatorCoeff[i1-1] + beta2 *denominatorCoeff[i1-2];
      }
      for (int i1 = 1; i1 <= n; i1++) {
        numeratorCoeff[i1] = newA[i1];
        denominatorCoeff[i1] = newB[i1];
      }
    }
    
    
    
 // filter gain at uniform frequency intervals
	int freqPoints = 5;
    float[] g = new float[freqPoints+1];
    double theta, s, c, sac, sas, sbc, sbs;
    float gMax = -100.0f;
    float sc = 10.0f/(float)Math.log(10.0f);
    t = Math.PI / freqPoints;
    for (int i1 = 0; i1 <= freqPoints; i1++) {
      theta = i1*t;
      if (i1 == 0) theta = Math.PI*0.0001;
      if (i1 == freqPoints) theta = Math.PI*0.9999;
      sac = 0.0f;
      sas = 0.0f;
      sbc = 0.0f;
      sbs = 0.0f;
      for (int k1 = 0; k1 <= order; k1++) {
        c = Math.cos(k1*theta);
        s = Math.sin(k1*theta);
        sac += c*numeratorCoeff[k1];
        sas += s*numeratorCoeff[k1];
        sbc += c*denominatorCoeff[k1];
        sbs += s*denominatorCoeff[k1];
      }
      g[i1] = sc*(float)Math.log((f2(sac) + f2(sas))/(f2(sbc) + f2(sbs)));
      gMax = Math.max(gMax, g[i1]);
    }
    // normalise to 0 dB maximum gain
    for (int i1=0; i1<=freqPoints; i1++) 
    	g[i1] -= gMax;
    // normalise numerator (a) coefficients
    float normFactor = (float)Math.pow(10.0, -0.05*gMax);
    for (int i1=0; i1<=order; i1++) 
    	numeratorCoeff[i1] *= normFactor;
  
    float[] num = new float[numeratorCoeff.length];
    float[] den = new float[denominatorCoeff.length];
    
	 for(int l=0;l<num.length;l++)
	 {
		  num[l] = (float) numeratorCoeff[l];
		  den[l] = (float) denominatorCoeff[l];
	 }
	 
    try {
		return new IIRFilter(num,den);
	} catch (IllegalArgumentException e) {
		throw new BugEncounterException();
	}
 
  }
}
