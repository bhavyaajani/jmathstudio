package org.JMathStudio.Interface.FileInterface;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.JMathStudio.DataStructure.Cell.Cell;

/**
 * This class provide interface to Read and Write Raw data from/to an external
 * file in ASCII format.
 * <pre>Usage:
 * Cell img = Cell.importImageAsCell("image_path");//Import external image as Cell.
 * 
 * DataFileInterface.writeData("write_path", img, Delimiter.SPACE, false);//Write input
 * Cell data to an external data file with specified delimiter.
 * 
 * Cell in = DataFileInterface.readData("read_path", Delimiter.SPACE, false);//Read Cell data
 * from external data file with specified delimiter.
 * </pre>
 * @author Ajani Bhavya - (jmathstudio@gmail.com)
 */
public final class DataFileInterface {
	
	//Ensure no instances are made for utility classes.
	private DataFileInterface(){}
	
	/**
	 * This enumeration list out supported delimiter types.
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 *
	 */
	public static enum Delimiter {COMMA,SPACE,TAB,COLON,SEMICOLON};
	
//	public final static String COMMA = ",";
//	public final static String SPACE = " ";
//	public final static String TAB = "	";
//	public final static String COLON = ":";
//	public final static String SEMICOLON = ";";

	
	/**
	 * This method will scan the external ASCII file given by the argument
	 * 'path' and will retrieve the raw data and return the same as a Cell.
	 * <p>
	 * Method will make use of the delimiter as specified by the argument
	 * 'delimiter' to segregate the data while scanning for the data. All the
	 * supported delimiters types are listed in Enum {@link Delimiter}.
	 * <p>
	 * The number of rows and columns in the returned Cell will be equal to the
	 * rows and column of data in the given file. The returned Cell will be a
	 * transposed version of the read data, if the argument 'transpose' is set
	 * true. In case of more than 1 column of data stored in the external file,
	 * the length of each column should be the same if transpose property is to
	 * be enabled.
	 * <p>
	 * This method will throw an IO Exception, if either given file is not
	 * found, or is not a file with read permit or if any problem is encountered
	 * while scanning the file.
	 * 
	 * @param String
	 *            path
	 * @param Delimiter
	 *            delimiter
	 * @param boolean transpose
	 * @return Cell
	 * @throws IOException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static Cell readData(String path, Delimiter delimiter, boolean transpose)
			throws IOException {
		
		File file = new File(path);
		String _delimiter = delimiter.toString();
		
		if (file.createNewFile()) {
			file.delete();
			throw new FileNotFoundException();
		} else if (!file.isFile() || !file.canRead()) {
			throw new IOException();
		}

		Scanner scan = new Scanner(file);
		ArrayList<Float[]> array = new ArrayList<Float[]>();

		try {
			while (scan.hasNextLine()) {
				String line = scan.nextLine();
				String[] word = line.split(_delimiter);
				Float[] data = new Float[word.length];
				for (int i = 0; i < data.length; i++)
					data[i] = Float.valueOf(word[i].trim());

				array.add(data);
			}

			if (array.isEmpty())
				throw new IOException();

			float[][] result = new float[array.size()][];

			for (int i = 0; i < result.length; i++) {
				Float[] row = array.get(i);
				result[i] = new float[row.length];

				for (int j = 0; j < row.length; j++) {
					result[i][j] = row[j];
				}
			}

			if (transpose) {
				float[][] transposed = new float[result[0].length][result.length];

				for (int i = 0; i < transposed.length; i++) {
					for (int j = 0; j < transposed[0].length; j++) {
						transposed[i][j] = result[j][i];
					}
				}

				scan.close();

				return new Cell(transposed);
			} else {
				scan.close();

				return new Cell(result);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new IOException();
		}

	}

	/**
	 * This method will write the given data represented by the Cell 'Cell', to
	 * an external ASCII text file as given by the argument 'path'.
	 * <p>
	 * If given file is not present a new file will be created. If given file
	 * exists this method will flush the contain of the file before writing the
	 * data. If the file given by the path is not a valid file or without a
	 * write permit, this method will throw an IOException.
	 * <p>
	 * This method will make use of the delimiter as specified by the argument
	 * 'delimiter' to segregate the data. All the supported delimiter types are listed
	 * in Enum {@link Delimiter}.
	 * <p>
	 * The written data will be in the same Cell structure/format as that of the
	 * Cell 'Cell', i.e. number of rows and columns will be similar to that of
	 * the input Cell. If the argument 'transpose' is set true, the given data
	 * will be written as a transposed version of the 'Cell' in output file.
	 * 
	 * @param String
	 *            path
	 * @param Cell
	 *            cell
	 * @param Delimiter
	 *            delimiter
	 * @param boolean transpose
	 * @throws IOException
	 * @author Ajani Bhavya - (jmathstudio@gmail.com)
	 */
	public final static void writeData(String path, Cell cell, Delimiter delimiter,
			boolean transpose) throws IOException {
		float[][] data = cell.accessCellBuffer();

		File file = new File(path);
		file.createNewFile();
		
		if (!file.isFile() || !file.canWrite()) {
			throw new IOException();
		}

		FileWriter write = new FileWriter(file);

		float[][] buffer;
		String _delimiter = delimiter.toString();
		
		if (transpose) {
			buffer = new float[data[0].length][data.length];

			for (int i = 0; i < buffer.length; i++) {
				for (int j = 0; j < buffer[0].length; j++) {
					buffer[i][j] = data[j][i];
				}
			}

		} else {
			buffer = data;
		}

		for (int i = 0; i < buffer.length; i++) {
			for (int j = 0; j < buffer[0].length; j++) {
				if (j != buffer[0].length - 1) {
					write.write(String.valueOf(buffer[i][j]));
					write.write(_delimiter);
				} else {
					write.write(String.valueOf(buffer[i][j]));
					write.write("\n");
				}
			}

		}

		write.close();
	}

}
