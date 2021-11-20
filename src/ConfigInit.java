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

//MacroFromJson.relativePath
//MacroFromJson.fileName
//MacroFromJson.filePath

//Checks if macro file exists. If it does, backup and reset. Else create new.
//Open file explorer

public class ConfigInit {

	private static String bakFileName = "bakMacros";

	public static void Test() {
		File configFile = new File("config//CustomMacros.json");
		File testFile1 = new File("config//thisTest.json");
		// createJson(testFile1);
		System.out.println(bakJson(configFile, "CustomMacros"));
		// createJson(configFile);
		// File testFile = new File("config//Test//Test.json");
		try {
			// testFile.createNewFile();
		} catch (Exception e) {
			System.out.println("Test: " + e);
		}
	}

	public static void generateJsonFile() {
		String relativePath = MacroFromJson.relativePath;
		String parentPath = MacroFromJson.parentPath;
		String shortFileName = MacroFromJson.shortFileName;
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

	private static boolean createJson(File configFile) {
		boolean returnVal = false;
		try {
			returnVal = configFile.createNewFile();
			fillJson(configFile.getPath());
		} catch (Exception e) {
			System.out.println(e);
		}
		if (returnVal) {
			System.out.println("New default file created at: " + configFile.getPath());
		} else {
			System.out.println("Error: default file failed to initialize");
			System.out.println("Manually restore macro file to: " + configFile.getPath());
		}
		return returnVal;
	}

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
				System.out.println("\nBackup of: " + source.getName() + "\nCreated at: " + backup.getName());
				System.out.println("Press ctrl+b to open file location\n");
				createJson(source);
			} else if (attempts == 10) {
				System.out.println("\nError: too many backups in config folder");
				System.out.println("Press ctrl+b to open file location");
			} else {
				System.out.println("\nError: Backup Failed");
				System.out.println(
						"Processing does not have file editing permissions. Please install config file manually");
			}
			return bakMade;
		} catch (Exception e) {
			System.out.println("bakJson: " + e);
		}
		return false;
	}

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
			System.out.println("Error: Could not create config directory");
		}
		return success;
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
		try {
			String backslashQuote = "\\" + "\"";
			String defaultJson = "{\r\n" + "    \"CodeSkeletonMacro\":true,\r\n" + "    \"FunctionMacros\":true,\r\n"
					+ "    \"input\":true,\r\n" + "    \"macros\": [";
			defaultJson += generateMacroString("for", "(int i=0; i < 10; i++){\\n   \\n}\\n", 14, false);
			defaultJson += generateMacroString("if", "(){\\n   \\n}\\n", 9, false);
			defaultJson += generateMacroString("while", "(){\\n   \\n}\\n", 9, false);
			defaultJson += generateMacroString("do", "{\\n   \\n}while();\\n", 3, false);
			defaultJson += generateMacroString("switch", "(){\\n   case 1:\\n   break;\\n   default:\\n}\\n", 38, true);
			defaultJson += "\n    ],\r\n" + "    \"replaceMacros\": [";
			defaultJson += generateReplaceMacroString("setup", "void setup(){\\n   \\n}\\n", 3, "", false);
			defaultJson += generateReplaceMacroString("draw", "void draw(){\\n   \\n}\\n", 3, "", false);
			defaultJson += generateReplaceMacroString("swing", "import javax.swing.*;\\n", 0, "", true);
			defaultJson += "\n    ]\r\n" + "}";
			PrintWriter pw = new PrintWriter(path);
			pw.write(defaultJson);
			pw.flush();
			pw.close();
		} catch (Exception e) {
			System.out.println("fillJson Error: " + e);
		}

	}

	private static String generateMacroString(String key, String code, int carback, boolean last) {
		String s = "";
		s += "\n        {";
		s += "\n            \"key\":\"" + key + "\",";
		s += "\n            \"code\": \"" + code + "\",";
		s += "\n            \"carBack\":" + carback;
		s += "\n        }";
		if (!last) {
			s += ",";
		}
		return s;
	}

	private static String generateReplaceMacroString(String key, String code, int carback, String imp, boolean last) {
		String s = "";
		s += "\n        {";
		s += "\n            \"key\":\"" + key + "\",";
		s += "\n            \"code\": \"" + code + "\",";
		s += "\n            \"carBack\":" + carback + ",";
		s += "\n            \"import\": \"" + imp + "\"";
		s += "\n        }";
		if (!last) {
			s += ",";
		}
		return s;
	}
}
