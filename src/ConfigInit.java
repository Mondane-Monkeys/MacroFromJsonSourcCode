/**
 * Part of the MacroFromJson tool for Processing
 *
 * ##copyright##
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General
 * Public License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA  02111-1307  USA
 *
 * @author   ##author##
 * @modified ##date##
 * @version  ##tool.prettyVersion##
 */

package MacroFromJson;

import processing.app.ui.Editor;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.regex.*;

import javax.sound.midi.spi.SoundbankReader;
import javax.xml.transform.Source;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

/**
 * ConfigInit class creates and manages json file. Can create config directory,
 * new template files.json, and .json file backups
 */
public class ConfigInit {
	
	
	public static void saveJsonFile(String fileContents, File source) {
		
		File bak1 = getBakString(source,"1");
		File bak2 = getBakString(source,"2");
		//Move files around
		simpleDeleteFile(bak2);
		moveFile(bak1,bak2);
		moveFile(source,bak1);
		//create new file
		createJson(fileContents, source);
	}
	
	/**
	 * Takes file source and destination, ensures no issues,
	 * Then renames source to be equal to destination
	 * @param source source file to be moved
	 * @param dest destination file name to move to
	 * @return If move successful
	 */
	private static boolean moveFile(File source, File dest) {
		boolean sourceExists;
		boolean destAvailable;
		boolean moveSuccess = false;
		if (!source.exists()) {
			//Handle missingSource issue
			sourceExists = false;
		} else {sourceExists = true;}
		
		if (dest.exists()) {
			//Handle duplicate file issue
			destAvailable = false;
		} else {destAvailable = true;}
		
		if (sourceExists&&destAvailable) {
			//move file
			moveSuccess = source.renameTo(dest);
		}
		return moveSuccess;
	}
	
	private static boolean simpleDeleteFile(File source) {
		boolean deleteSuccess;
		deleteSuccess = source.exists() ? source.delete() : false;
		return deleteSuccess;
	}
	
	private static File getBakString(File source, String append) {
		String fileName = source.getPath();
//		System.out.println("Token: " + fileName);
		String[] tokens = fileName.split("\\.(?=[^\\.]+$)");
//		System.out.println("Token: " + tokens[0]);
//		System.out.println("Token: " + tokens[1]);
		return new File(tokens[0]+append+"."+tokens[1]);
	}
	
	/**
	 * Checks current config directory situation 
	 * And creates a new (file)macros.json / (dir)config as needed. 
	 * 
	 * If macros.json exists, renames it 
	 * And creates a new macros.json file with template macros.
	 */
	public static void generateJsonFile() {
		String relativePath = Const.relativePath;
		String parentPath = Const.parentPath;
		String shortFileName = Const.shortFileName;
		File configDir = new File(parentPath);
		File configFile = new File(relativePath);

		if (configDir.exists()) {
			if (configFile.exists()) {
				bakJson(configFile, shortFileName);
			} else {
				// create macro.json
				createJson(configFile);
			}
		} else {
			// create config folder&macro.json
			createDir(configDir);
			createJson(configFile);
		}
	}

	/**
	 * Tries to create new file and populate with template macros
	 * @param configFile 
	 * 		File location to create new file
	 * @return If creation succeeded
	 */
	private static boolean createJson(File configFile) {
		boolean returnVal = false;
		try {
			returnVal = configFile.createNewFile();
			fillJson(configFile.getPath());
		} catch (Exception e) {
			System.out.println(e);
		}
		if (returnVal) {
			System.out.println(Const.DEFAULT_SUCCESS + configFile.getPath());
		} else {
			System.out.println(Const.DEFAULT_FAILED+configFile.getPath());
		}
		return returnVal;
	}
	
	private static boolean createJson(String contents, File configFile) {
		boolean returnVal = false;
		try {
			returnVal = configFile.createNewFile();
			fillJson(contents, configFile.getPath());
		} catch (Exception e) {
			System.out.println(e);
		}
		if (returnVal) {
			System.out.println(Const.DEFAULT_SUCCESS + configFile.getPath());
		} else {
			System.out.println(Const.DEFAULT_FAILED+configFile.getPath());
		}
		return returnVal;
	}
	
	/**
	 * Renames file and calls createJson() to replace the moved file
	 * @param source 
	 * 		Location of file to be moved and replaced
	 * @param bakFileName
	 * 		Core name of target backup location
	 * @return If bakup succeeded
	 */
	private static boolean bakJson(File source, String bakFileName) {
		try {
			String folderPath = source.getParent();
			File backup = new File(folderPath + "//" + bakFileName + "Bak" + ".json");
			int attempts = 0;
			boolean bakMade = false;
			while (!bakMade && attempts < 10) {
				backup = new File(folderPath + "//" + bakFileName + "Bak" + attempts + ".json");
				attempts++;
				bakMade = source.renameTo(backup);
			}
			if (bakMade) {
				bakMade = createJson(source);
				System.out.println("\nBackup of: " + source.getName() + "\nCreated at: " + backup.getName());
				System.out.println(Const.OPEN_FILE);
			} else if (attempts == 10) {
				System.out.println(Const.BAK_OVERFLOW + Const.OPEN_FILE);
			} else {
				System.out.println(Const.BAK_FAILED);
			}
			return bakMade;
		} catch (Exception e) {
			System.out.println("bakJson: " + e);
		}
		return false;
	}

	/**
	 * Creates config directory
	 * @param configDir 
	 * 		directory location
	 * @return If createDir succeeded
	 */
	private static boolean createDir(File configDir) {
		boolean success = false;
		try {
			if (!configDir.exists()) {
				success = configDir.mkdirs();
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		if (!success) {
			System.out.println(Const.DIR_FAILED);
		}
		return success;
	}
	
	/**
	 * Populates file with template macros
	 * @param path 
	 * 		Location of file to populate
	 */
	private static void fillJson(String path) {
		try {
			PrintWriter pw = new PrintWriter(path);
			pw.write(Const.DEFAULT_JSON());
			pw.flush();
			pw.close();
		} catch (Exception e) {
			System.out.println("fillJson Error: " + e);
		}
	}
	
	/**
	 * Populates file with template macros
	 * @param path 
	 * 		Location of file to populate
	 */
	private static void fillJson(String contents, String path) {
		try {
			PrintWriter pw = new PrintWriter(path);
			pw.write(contents);
			pw.flush();
			pw.close();
		} catch (Exception e) {
			System.out.println("fillJson Error: " + e);
		}
	}
}
