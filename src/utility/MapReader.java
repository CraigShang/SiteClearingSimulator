package utility;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * utility class to read the map
 * 
 * @author Peizheng Shang
 *
 */
public class MapReader {

	public final static String defaultMapLocation = "testData/sitemap.txt";

	/**
	 * read the test data file and covert it to a two-d array
	 * 
	 * @return a two-d array of site map
	 */
	public static char[][] readSite() {
		char[][] site = null;
		try {
			// read each line of map into an array list
			ArrayList<String> lines = new ArrayList<>();
			BufferedReader reader = new BufferedReader(new FileReader(defaultMapLocation));
			while (true) {
				String line = reader.readLine();
				if (line == null) {
					break;
				}
				lines.add(line);
			}
			reader.close();
			// validate data file
			boolean validData = lines.size() > 0;
			for (int i = 0; i < lines.size() - 1; i++) {
				// if the file is empty or any line has a different length than others,
				// validation fails
				if (lines.get(i).length() == 0 || lines.get(i).length() != lines.get(i + 1).length()) {
					validData = false;
					break;
				}
				for (int j = 0; j < lines.get(i).length(); j++) {
					// for each line, if there are other chars beside the allowed ones, validation
					// fails
					if (lines.get(i).charAt(j) != 'o' && lines.get(i).charAt(j) != 'r' && lines.get(i).charAt(j) != 't'
							&& lines.get(i).charAt(j) != 'T') {
						validData = false;
						break;
					}
				}
			}
			if (!validData) {
				throw new Exception();
			}
			// set each char to a 2d char array
			site = new char[lines.size()][lines.get(0).length()];
			for (int i = 0; i < lines.size(); i++) {
				for (int j = 0; j < lines.get(0).length(); j++) {
					site[i][j] = lines.get(i).charAt(j);
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("Error: Unable to locate site map.");
		} catch (Exception e) {
			System.out.println("Error: Empty or corruptted site map.");
		}
		return site;
	}
}
