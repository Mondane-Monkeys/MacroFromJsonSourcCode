/**
 * 
 */

/**
 * @author Admin
 *
 */

package MacroFromJson;

import processing.app.ui.Editor;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Iterator;
import java.util.Map;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

//MacroFromJson.RelativePath
//MacroFromJson.fileName
//MacroFromJson.filePath

//Checks if macro file exists. If it does, backup and reset. Else create new.
//Open file explorer

public class ConfigInit {

	public static void generateJsonFile() {
		String relativePath = MacroFromJson.RelativePath;
		String filePath = MacroFromJson.filePath;

		if (checkFolderExists(filePath)) {
			if (checkFileExists(relativePath)) {
				// backup (count files in dir, backup+count) delete and create
				bakJson(relativePath);
				dumpJson(relativePath);
				createJson(relativePath);
			} else {
				// create macro.json
				createJson(relativePath);
			}
		} else {
			// create config folder&macro.json
			createConfig(filePath);
			createJson(relativePath);
		}

	}

	private static boolean checkFolderExists(String path) {
		File myFile = new File(path);
		return myFile.exists();
	}

	private static boolean checkFileExists(String path) {
		File myFile = new File(path);
		return myFile.exists();
	}

	private static void createJson(String path) {
		File myFile = new File(path);
		myFile.createNewFile();
		fillJson(path);
	}

	private static void bakJson(String filePath, String fileName) {
		File myFile = new File(filePath + fileName);
		File myFolder = new File(filePath);
		if (!isJson(myFile)) {
			System.out.println("Error: \n" + "ConfigInit.bakJson: Is not Json!!");
		}
		File[] filesInConfig = myFolder.listFiles;
		int fileCount = filesInConfig.length;
		myFile.renameTo(filePaht + fileCount + fileName + ".bak");
	}

	private static void createConfig(String path) {
		File myFile = new File(path);
		myFile.createNewFile();
		createJson(path);
	}

	private static void isJson(file jsonFile) {
		String extension = "";
		try {
			if (jsonFile != null && jsonFile.exists()) {
				String name = file.getName();
				extension = name.substring(name.lastIndexOf("."));
			}
		} catch (Exception e) {
			System.out.println("ConfigInit.isJson: \n" + e);
		}
		System.out.println("ConfigInit.isJson extension = " + extension);
		return (extension == ".json");
	}

	private static void fillJson(String path) {
		// creating JSONObject
		JSONObject jo = new JSONObject();

		// putting data to JSONObject
		jo.put("CodeSkeletonMacro", true);
		jo.put("FunctionMacros", true);

		// for macros, first create JSONArray
		JSONArray ja = new JSONArray();

		m = new LinkedHashMap(2);
		m.put("key", "home");
		m.put("code", "212 555-1234");
		m.put("carBack", "0");
		// adding map to list
		ja.add(m);

		// putting macros to JSONObject
		jo.put("macros", ja);

		// for replaceMacros, first create JSONArray
		ja = new JSONArray();

		m = new LinkedHashMap(2);
		m.put("key", "home");
		m.put("code", "212 555-1234");
		m.put("carBack", "0");
		// adding map to list
		ja.add(m);

		// putting macros to JSONObject
		jo.put("repalceMacros", ja);
	}
}
