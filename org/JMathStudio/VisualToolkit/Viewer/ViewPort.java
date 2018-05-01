package org.JMathStudio.VisualToolkit.Viewer;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;

import javax.swing.event.MouseInputListener;

import org.JMathStudio.DataStructure.Cell.Cell;
import org.JMathStudio.Exceptions.BugEncounterException;
import org.JMathStudio.Exceptions.DimensionMismatchException;
import org.JMathStudio.Exceptions.IllegalArgumentException;


/**
 * This class define a ViewPort or a canvas for rendering a
 * {@link BufferedImage} and supports following user interfaces,
 * <p>
 * 1. Pixel Data Annotation
 * <p>
 * <i>This interface enable user to annotate pixel data and location information
 * at a specified position on the rendered image by clicking left mouse button
 * at a desired position on the ViewPort. This interface will only be enable if
 * corresponding pixel data for the rendered image is available. </i>
 * <p>
 * 2. Zoom
 * <p>
 * <i>This interface enables the user to view a desired sub image of the
 * rendered image by zooming into it. This class supports a rectangular ROI for
 * selecting the desired sub image from view port. To select a sub image press
 * the left mouse button, holding it drag it so as to form a Rectangle
 * describing the ROI. The sub image as described by the ROI will be rendered on
 * the ViewPort.
 * <p>
 * User can click right mouse button to come back to the original rendered
 * image. </i>
 * 
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
@SuppressWarnings("serial")
final class ViewPort extends Canvas implements MouseInputListener,
		MouseWheelListener {

	private BufferedImage setImage;
	private BufferedImage displayImage;
	private BufferedImage offscreen;
	private Graphics2D offHandle;

	private Cell setData;

	private boolean isDataAccessable = false;
	private boolean isPixelDataAnnotEnable = true;
	private String annotPixelData = " ";

	private float startX, startY, endX, endY;
	private ROI roi;

	private boolean isZoomEnable = true;
	private boolean isZoomImage = false;

	private int mousePressed = -1;

	/**
	 * This will initiate a ViewPort with preferred width and height as
	 * specified by the arguments 'width' and 'height' respectively.
	 * <p>
	 * This method will also populate the ViewPort with a blank image.
	 * <p>
	 * The <i>Pixel Data Annotation </i>and<i> Zoom </i> interface are enabled
	 * for this ViewPort by default.
	 * 
	 * @param int width
	 * @param int height
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public ViewPort(int width, int height) {
		this.setPreferredSize(new Dimension(width, height));
		// this.setSize(width,height);
		this.setBackground(Color.BLACK);

		addMouseListener(this);
		addMouseMotionListener(this);
		addMouseWheelListener(this);

		roi = new ROI();

		BufferedImage dummy = new BufferedImage(width, height,
				BufferedImage.TYPE_BYTE_GRAY);
		Cell dummy_data = new Cell(height, width);

		try {
			display(dummy, dummy_data);
		} catch (DimensionMismatchException e) {
			throw new BugEncounterException();
		}

	}

	/**
	 * This method will render the {@link BufferedImage} 'img' on the given view
	 * port.
	 * <p>
	 * If the BufferedImage to be rendered is accompanied by its raw pixel data
	 * in the form of {@link Cell} 'data' the<i>Pixel Data Annotation </i>
	 * interface will be enabled.
	 * <p>
	 * Here the Cell 'data' provides the raw pixel data for the
	 * {@link BufferedImage} 'img'. The dimension of the Cell 'data' should be
	 * similar to that of the BufferedImage 'img' else this method will throw an
	 * DimensionMismatch Exception.
	 * <p>
	 * If the Cell 'data' is null this method will render the BufferedImage
	 * 'img' on this view port but will disable the <i>Pixel Data
	 * Annotation</i>interface.
	 * <p>
	 * If the BufferedImage 'img' is null this method will throw a NullPointer
	 * Exception.
	 * <p>The arguments 'img' and 'data' are passed by reference and no deep copy of the 
	 * same is made.
	 * 
	 * @param BufferedImage
	 *            img
	 * @param Cell
	 *            data
	 * @throws DimensionMismatchException
	 * @throws {@link NullPointerException}
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void display(BufferedImage img, Cell data)
			throws DimensionMismatchException {
		if (img == null)
			throw new NullPointerException();
		else if (data == null) {
			setImage = img;
			setData = null;
			isDataAccessable = false;
			paintBufferedImage(img);
		} else if (img.getHeight() != data.getRowCount()
				|| img.getWidth() != data.getColCount()) {
			throw new DimensionMismatchException();
		} else {
			setImage = img;
			setData = data;
			isDataAccessable = true;
			paintBufferedImage(img);
		}
	}

	/**
	 * This method will refresh the view port and revert back to the original
	 * settings for the current displayed image.
	 * 
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void refresh() {
		isZoomImage = false;
		paintBufferedImage(setImage);
	}

	private void comeOutOfROI() {
		isZoomImage = false;
		paintBufferedImage(setImage);
	}

	/**
	 * This will enable/disable the <i>Zoom<i> interface for this view port.
	 * 
	 * @param boolean set
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void setZoomable(boolean set) {
		this.isZoomEnable = set;
	}

	/**
	 * This will enable/disable the <i>Pixel Data Annotation</i> interface for
	 * this view port.
	 * <p>
	 * This property will be enabled if and only if the pixel data is available
	 * for the given rendered image.
	 * <p>
	 * Refer {@link #display(BufferedImage, Cell)}.
	 * 
	 * @param boolean set
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void setPixelDataAnnotation(boolean set) {
		isPixelDataAnnotEnable = set;
		this.annotPixelData = " ";
	}

	/**
	 * This method control the rendering operation for this class.
	 * 
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public void paint(Graphics g) {
		Graphics2D handle = (Graphics2D) g;

		offscreen = new BufferedImage(this.getWidth(), this.getHeight(),
				BufferedImage.TYPE_INT_RGB);

		double scaleh = offscreen.getHeight()
				/ (double) displayImage.getHeight();
		double scalew = offscreen.getWidth() / (double) displayImage.getWidth();

		offHandle = (Graphics2D) offscreen.getGraphics();

		offHandle.setColor(Color.orange);

		if (scalew != 0 || scaleh != 0)
			offHandle.scale(scalew, scaleh);

		offHandle.drawImage(displayImage, 0, 0, this);

		if (scalew != 0 || scaleh != 0)
			offHandle.scale(1.0f / scalew, 1.0f / scaleh);

		if (isZoomEnable & roi.hasROI)
			offHandle.drawRect(roi.getX(), roi.getY(), roi.getROIWidth(), roi
					.getROIHeight());

		int fontSize = (getHeight() > 20) ? getHeight() / 20 : 4;
		offHandle.setFont(new Font(" ", Font.PLAIN, fontSize));

		if (isToShowData())
			offHandle.drawString(annotPixelData, 0, fontSize);

		handle.drawImage(offscreen, 0, 0, this);
	}

	public void update(Graphics g) {
		paint(g);
	}

	// x and y should be normalised, i.e [0 1)
	private void showDataForLocation(float x, float y)
			throws IllegalArgumentException {
		if (check(x) & check(y)) {
			if (isToShowData()) {
				short X = (short) (x * setData.getColCount());
				short Y = (short) (y * setData.getRowCount());

				annotPixelData = "I: "
						+ Float.toString(setData.getElement(Y, X));
				annotPixelData = annotPixelData + " Y: " + Short.toString(Y);
				annotPixelData = annotPixelData + " X: " + Short.toString(X);

				repaint();
			}
		} else
			throw new IllegalArgumentException();

	}

	// Start and End points are normalised [0 1)
	// Should only be called if DataAccess is enable and data is available.
	private void showSubImage(float startX, float startY, float endX, float endY)
			throws IllegalArgumentException {
		if (check(startX) & check(startY) & check(endX) & check(endY)) {
			if (startX == endX | startY == endY)
				return;

			if (startX > endX) {
				float tmp = startX;
				startX = endX;
				endX = tmp;
			}

			if (startY > endY) {
				float tmp = startY;
				startY = endY;
				endY = tmp;
			}

			int height = (int) (displayImage.getHeight() * (endY - startY));
			int width = (int) (displayImage.getWidth() * (endX - startX));
			int X = (int) (startX * displayImage.getWidth());
			int Y = (int) (startY * displayImage.getHeight());

			if (height > 0 & width > 0) {
				// System.out.println(X+" "+Y+" "+(X+width)+" "+(Y+height));
				try {
					BufferedImage roiImage = displayImage.getSubimage(X, Y,
							width, height);

					if (roiImage != null) {
						isZoomImage = true;// because showing ROI image not
											// original image.
						paintBufferedImage(roiImage);
					}
				} catch (RasterFormatException e) {
					e.printStackTrace();
					throw new IllegalArgumentException();
				}
			}
		}

	}

	private boolean isToShowData() {
		return (isDataAccessable & isPixelDataAnnotEnable & !isZoomImage);
	}

	private boolean check(float x) {
		if (x < 0 || x >= 1)
			return false;
		else
			return true;
	}

	private void cleanDataAnnotation(){
		this.annotPixelData = "";
	}
	
	private void paintBufferedImage(BufferedImage img) {
		displayImage = img;
		repaint();
	}

	public void mouseClicked(MouseEvent e) {

		if (e.getButton() == MouseEvent.BUTTON1 & isToShowData()) {
			float x = (float) e.getX() / getWidth();
			float y = (float) e.getY() / getHeight();

			try {
				showDataForLocation(x, y);
			} catch (IllegalArgumentException e1) {
				throw new BugEncounterException();
			}
		} else if (e.getButton() == MouseEvent.BUTTON3) {
			cleanDataAnnotation();
			comeOutOfROI();
		}

	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mousePressed(MouseEvent e) {

		mousePressed = e.getButton();

		if (isZoomEnable & e.getButton() == MouseEvent.BUTTON1) {
			roi = new ROI();
			roi.setOriginPoint(e.getY(), e.getX());
		}
	}

	public void mouseReleased(MouseEvent e) {

		if (isZoomEnable & e.getButton() == MouseEvent.BUTTON1) {
			if(roi.updateROI(e.getY(), e.getX())){
			try {
				float height = getHeight();
				float width = getWidth();
				startX = roi.getX()/width;
				startY = roi.getY()/height;
				endX = (roi.getX()+roi.getROIWidth())/width;
				endY = (roi.getY()+roi.getROIHeight())/height;
				showSubImage(startX, startY, endX, endY);
			} catch (IllegalArgumentException e1) {
				throw new BugEncounterException();
			}
			}
			else{
				this.repaint();
			}
			roi.clean();
		}
	}

	public void mouseDragged(MouseEvent e) {

		if (roi != null & isZoomEnable & mousePressed == MouseEvent.BUTTON1) {
			roi.updateROI(e.getY(), e.getX());
			this.repaint();		
		}
	}

	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseWheelMoved(MouseWheelEvent e) {

	}

	private class ROI {
		private int OX, OY, width, height;
		public boolean hasROI;

		public ROI() {
			clean();
		}

		public void setOriginPoint(int y, int x) {
			this.OX = x;
			this.OY = y;
		}

		// Checks the current validity of the ROI with current end point. 
		// Also ROI can expand from original point towards right and down
		// only.
		public boolean updateROI(int y, int x) 
		{
			// Check for expansion in right and bottom direction only.
			if (x < OX && y < OY){
				width=height=0;
				hasROI =  false;
			}
			else if(x < OX){
				width = 0;
				height = y - OY;
				hasROI = false;
			}
			else if(y < OY){
				height = 0;
				width = x - OX;
				hasROI = false;
			}
			else if (x >= getWidth() && y >= getHeight()){
				width = getWidth()-1 - OX;
				height = getHeight()-1 - OY;
				hasROI = true;
			}
			else if(x >= getWidth()){
				width = getWidth()-1 - OX;
				height = y - OY;
				hasROI = true;
			}
			else if(y >= getHeight()){
				width = x - OX;
				height = getHeight()-1 - OY;
				hasROI = true;
			}
			else {
				width = x - OX;
				height = y - OY;
				hasROI = true;
			}
			
			if(width <= 0 || height <= 0){
				hasROI = false;
			}
			
			return hasROI;
		}

		public int getX() {
			return this.OX;
		}

		public int getY() {
			return this.OY;
		}

		public int getROIWidth() {
			return this.width;
		}

		public int getROIHeight() {
			return this.height;
		}

		public void clean() {
			this.OX = 0;
			this.OY = 0;
			this.height = 0;
			this.width = 0;
			hasROI = false;
		}
	}

}
