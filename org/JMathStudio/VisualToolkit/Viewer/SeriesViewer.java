/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * NewJFrame1.java
 *
 * Created on 15 Oct, 2010, 10:16:34 PM
 */

package org.JMathStudio.VisualToolkit.Viewer;

import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.DimensionMismatchException;

/**
 * This class define a SeriesViewer which is a Window (Java Frame) to display a series of 
 * {@link BufferedImage}s.
 * <p>SeriesViewer embed a {@link ViewPort} on which images are rendered.
 * <p>Further, SeriesViewer embed a slider which enable user to select the image within the
 * series to be rendered. 
 * <p>See {@link ViewPort} for the list of user interfaces supported.
 * @author Ajani Bhavya - (jmathstudio@gmail.com) 
 */
public final class SeriesViewer extends JFrame implements ChangeListener{

	private static final long serialVersionUID = -5605424101203550581L;
	
	private final String seriesNavigatorLabel = "IMG";
	private final static String DEFAULT_TITLE="JMathStudio";
	
	private BufferedImage[] series;
	private Cell[] data;
	private boolean hasSeries = false;
	private boolean hasData = false;
	
    private ViewPort viewport;
    private javax.swing.JSlider navigator;
    
    /**
	 * This will create a SeriesViewer with width and height as specified by the arguments
	 * 'width' and 'height' respectively.
	 * @param int width
	 * @param int height
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public SeriesViewer(int width,int height)
	{
		super(DEFAULT_TITLE);
		setSize(width,height);
        
		initComponents();
        
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setVisible(true);
	}
	
	/**
	 * This will create a SeriesViewer with default width and height.
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
    public SeriesViewer() 
    {
    	this(ImageViewer.DefaultWidth,ImageViewer.DefaultHeight);
    }

    /**
	 * This method will render a series of {@link BufferedImage}s as given by the BufferedImage array 'imgs'
	 * on the given SeriesViewer.
	 * <p>If the series to be rendered is accompanied by corresponding raw pixel data in the form of {@link Cell} as 
	 * represented by the Cell array 'datas' the <i>Pixel Data Annotation</i> interface will be enabled. 
	 * <p>Each individual Cell of the array 'datas' represents the raw pixel data for the corresponding image within the
	 * BufferedImage series to be rendered.
	 * <p>If the Cell array 'datas' is null, this method will render the image series but will disable the 
	 * <i>Pixel Data Annotation</i> interface.
	 * <p>If Cell array 'datas' is not null, the dimension of both the array should be same else this method 
	 * will throw an DimensionMismatch Exception. 
	 * <p>Further the dimension of the corresponding Cell and the BufferedImage in the respective array should
	 * be similar else this method will throw an DimensionMismatch Exception.
	 * <p>If the BufferedImage array 'imgs' is null or if any of the BufferedImage is null this method will throw an
	 * NullPointer Exception.
	 * <p>By default, the first image of the series is rendered.
	 * <p>Refer {@link ViewPort} for User Interfaces supported on the ViewPort.
	 * <p>The arguments 'imgs' and 'datas' are passed by reference and no deep copy of the same is made.
	 * 
	 * @param BufferedImage[] imgs
	 * @param Cell[] datas
	 * @throws DimensionMismatchException
	 * throws {@link NullPointerException}
	 * @author Ajani Bhavya - (jmathstudio@gmail.com) 
	 */
    public void displaySeries(BufferedImage[] imgs,Cell[] datas) throws DimensionMismatchException
    {
    	hasData = false;
    	hasSeries = false;
    	setNavigatorConfiguration(0, 0, 0);
    	
    	if(imgs == null)
			throw new NullPointerException();
		else
		{
			for(int i=0;i<imgs.length;i++)
			{
				if(imgs[i] == null)
					throw new NullPointerException();
			}
		}
		
		if(datas != null)
		{
			if(datas.length != imgs.length)
				throw new DimensionMismatchException();
			
			for(int i=0;i<imgs.length;i++)
			{
				if(datas[i] == null)
					throw new NullPointerException();
				else
				{
					if(imgs[i].getHeight() != datas[i].getRowCount() | imgs[i].getWidth() != datas[i].getColCount())
						throw new DimensionMismatchException();
				}
			}
			
			hasData = true;
			this.data = datas;
			hasSeries = true;
			this.series = imgs;
			
			setNavigatorConfiguration(0, series.length-1, 0);
			
		}
		else
		{
			hasSeries = true;
			this.series = imgs;
			
			setNavigatorConfiguration(0, series.length-1, 0);
		}
		
    	try {
			showImage(0);
		} catch (IllegalArgumentException e) {
			throw new BugEncounterException();
		}    	
    	
    }
    
    private void showImage(int index) throws IllegalArgumentException, DimensionMismatchException
    {
    	if(hasSeries)
    	{
    		if(index <0 || index >=series.length)
    			throw new IllegalArgumentException();
    		else
    		{
    			if(hasData)
    				viewport.display(series[index], data[index]);
    			else
    				viewport.display(series[index], null);
    		}
    	}
    }
    
   private void initComponents() 
   {
        viewport = new ViewPort(this.getWidth(),this.getHeight());
        getContentPane().add(viewport, java.awt.BorderLayout.CENTER);
 
        navigator = new javax.swing.JSlider();
        navigator.addChangeListener(this);
        setNavigatorConfiguration(0,0,0);
        navigator.setInverted(true);
        navigator.setOrientation(javax.swing.JSlider.VERTICAL);
        navigator.setBorder(javax.swing.BorderFactory.createTitledBorder(seriesNavigatorLabel));
        navigator.setMinimumSize(new java.awt.Dimension(30, 36));
        navigator.setPreferredSize(new java.awt.Dimension(30, 200));
        getContentPane().add(navigator, java.awt.BorderLayout.LINE_END);
        
        pack();
    }

public void stateChanged(ChangeEvent e) {
	
	try {
		showImage(navigator.getValue());
	} catch (IllegalArgumentException e1) {
		throw new BugEncounterException();
	} catch (DimensionMismatchException e1) {
		throw new BugEncounterException();
	}
}

private void setNavigatorConfiguration(int min,int max,int value)
{
	navigator.setMaximum(max);
	navigator.setMinimum(min);
	navigator.setValue(value);
}

}
