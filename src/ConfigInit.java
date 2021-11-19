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
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.LinkedHashMap;
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

	public static void Test() {
		System.out.println(MacroFromJson.RelativePath);
		System.out.println(MacroFromJson.filePath + ": " + checkFileExists(MacroFromJson.filePath));
		System.out.println(MacroFromJson.RelativePath+": "+checkFileExists(MacroFromJson.RelativePath));
		
		createJson("config//thisTest.json");
		System.out.println(bakJson(MacroFromJson.filePath, MacroFromJson.fileName));
	}
	public static void generateJsonFile() {
		String relativePath = MacroFromJson.RelativePath;
		String filePath = MacroFromJson.filePath;
		String fileName = MacroFromJson.fileName;

		if (checkFileExists(filePath)) {
			if (checkFileExists(relativePath)) {
				// backup (count files in dir, backup+count) delete and create
				bakJson(filePath, fileName);
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

//	private static boolean checkFolderExists(String path) {
//		File myFile = new File(path);
//		return myFile.exists();
//	}

	private static boolean checkFileExists(String path) {
		File myFile = new File(path);
		return myFile.exists();
	}

	private static void createJson(String path) {
		try {
			File myFile = new File(path);
			myFile.createNewFile();
			fillJson(path);
		} catch (Exception e) {
			System.out.println(e);
		}
		
	}

	private static boolean bakJson(String filePath, String fileName) {
		System.out.println("bakJson");
		System.out.println(filePath);
		System.out.println(fileName);
		File myFile = new File(filePath + fileName);
		File myFolder = new File(filePath);
		if (!isJson(myFile)) {
			System.out.println("Error: \n" + "ConfigInit.bakJson: Is not Json!!");
		} else {System.out.println("here");}
		
		File[] filesInConfig = myFolder.listFiles();
		int fileCount = filesInConfig.length;
		File destFile = new File(filePath + fileCount + fileName + ".bak");
		File test = new File ("test");
		File test2 = new File("config//thisTest.json");
		return test2.renameTo(test);
	}

	private static void createConfig(String path) {
		try {
			File myFile = new File(path);
			myFile.createNewFile();
			createJson(path);
		} catch (Exception e) {
			System.out.println(e);
		}
		
	}

	private static boolean isJson(File jsonFile) {
		String extension = "";
		try {
			if (jsonFile != null && jsonFile.exists()) {
				String name = jsonFile.getName();
				extension = name.substring(name.lastIndexOf("."));
			}
		} catch (Exception e) {
			System.out.println("ConfigInit.isJson: \n" + e);
		}
		System.out.println("ConfigInit.isJson extension = |" + extension + "|");
		System.out.println(extension.equals(".json"));
		boolean isSame = (extension.equals(".json"));
		return isSame;
	}

	private static void fillJson(String path) {
		// creating JSONObject
		JSONObject jo = new JSONObject();

		// putting data to JSONObject
		jo.put("CodeSkeletonMacro", true);
		jo.put("FunctionMacros", true);

		// for macros, first create JSONArray
		JSONArray ja = new JSONArray();
		
		Map m = new LinkedHashMap(2);
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
