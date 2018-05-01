package org.JMathStudio.VisualToolkit.Viewer;

import java.awt.image.BufferedImage;

import javax.swing.JFrame;

import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.Exceptions.DimensionMismatchException;

//Assumption that ViewPort completely fills up this parent component content pane.
//Dimensions of Viewport will be some what less than ViewFrame.
/**
 * This class define an ImageViewer which is a Window (Java Frame) to display a 
 * {@link BufferedImage}.
 * <p>ImageViewer embed a {@link ViewPort} on which subsequent image is rendered.
 * @author Ajani Bhavya - (jmathstudio@gmail.com) 
 */
@SuppressWarnings("serial")
public final class ImageViewer extends JFrame {
	
	public final static int DefaultHeight = 300;
	public final static int DefaultWidth = 300;
	private final static String DEFAULT_TITLE="JMathStudio";
	
	private ViewPort port;
	
	/**
	 * This will create an ImageViewer with default width and height.
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public ImageViewer()
	{
		this(DefaultWidth,DefaultHeight);
	}
	/**
	 * This will create an ImageViewer with width and height as specified by the arguments
	 * 'width' and 'height' respectively.
	 * @param int width
	 * @param int height
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public ImageViewer(int width,int height)
	{
		super(DEFAULT_TITLE);
		setSize(width, height);
		port = new ViewPort(width,height);
		getContentPane().add(port);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		pack();
		setVisible(true);
	}

	/**
	 * This method will render the {@link BufferedImage} 'img' on the {@link ViewPort} embedded within this
	 * ImageViewer.
	 * <p>If the BufferedImage to be rendered is accompanied by its raw pixel data in the form of a valid 
	 * {@link Cell} 'data' the<i>Pixel Data Annotation </i> interface will be enabled.
	 * <p>The Cell 'data' provides the raw pixel data for the {@link BufferedImage} 'img'. The dimension
	 * of the Cell 'data' should be similar to that of the BufferedImage 'img' else this method will
	 * throw an DimensionMismatch Exception.
	 * <p>If the Cell 'data' is null this method will render the BufferedImage 'img' on the view port
	 * but will disable the <i>Pixel Data Annotation</i> interface.
	 * <p>If the BufferedImage 'img' is null this method will throw a NullPointer Exception.
	 * <p>Refer {@link ViewPort} for User Interfaces supported on the ViewPort.
	 * <p>The arguments 'img' and 'data' are passed by reference and no deep copy of the same is made.
	 * 
	 * @param BufferedImage img
	 * @param Cell data
	 * @throws DimensionMismatchException
	 * @throws {@link NullPointerException}
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void display(BufferedImage img,Cell data) throws DimensionMismatchException
	{
		port.display(img, data);
	}
	
	
}
