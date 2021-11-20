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
	
	private static String bakFileName = "bakMacros";

	public static void Test() {
		File configFile = new File("config//CustomMacros.json");
		File testFile1 = new File("config//thisTest.json");
		//createJson(testFile1);
		System.out.println(bakJson(configFile, "CustomMacros"));
		//createJson(configFile);
		//File testFile = new File("config//Test//Test.json");
		try {
			//testFile.createNewFile();
		} catch(Exception e) {
			System.out.println("Test: " + e);
		}
	}
	
	public static void generateJsonFile() {
		String relativePath = MacroFromJson.RelativePath;
		String filePath = MacroFromJson.filePath;
		String fileName = MacroFromJson.fileName;
		File configFolder = new File(filePath);
		File configFile = new File(relativePath);

		if (configFolder.exists()) {
			if (configFile.exists()) {
				bakJson(configFile, "CustomMacros");
				createJson(configFile);
			} else {
				// create macro.json
				createJson(configFile);
			}
		} else {
			// create config folder&macro.json
			createConfig(configFile);
			createJson(configFile);
		}
	}

	private static boolean createJson(File ConfigFile) {
		boolean returnVal = false;
		try {
			returnVal = ConfigFile.createNewFile();
			fillJson(ConfigFile.getPath());
		} catch (Exception e) {
			System.out.println(e);
		}
		return returnVal;
	}
	
	private static boolean bakJson(File source, String bakFileName) {
		try {
		String folderPath = source.getParent();
		File backup;
		int attempts = 0;
		boolean bakMade = false;
		while (!bakMade && attempts < 10) {
			backup = new File(folderPath + "//" + bakFileName + "Bak" + attempts + ".json");
			attempts++;
			bakMade = source.renameTo(backup);
			if(bakMade) {
				System.out.println("\nBackup of: " + source.getName() + "\nCreated at: " + backup.getName()+ "\n");
				createJson(source);
			}
		}
		return bakMade;
		} catch (Exception e) {
			System.out.println("bakJson: " + e);
		}
		return false;
	}

	private static void createConfig(File ConfigFile) {
		try {
			ConfigFile.createNewFile();
			createJson(ConfigFile);
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
		try {
			String backslashQuote = "\\" + "\"";
			String defaultJson = "{\r\n"
					+ "    \"CodeSkeletonMacro\":true,\r\n"
					+ "    \"FunctionMacros\":true,\r\n"
					+ "    \"input\":true,\r\n"
					+ "    \"macros\": [";
			defaultJson+=generateMacroString("for", "(int i=0; i < 10; i++){\\n   \\n}\\n",14,false);
			defaultJson+=generateMacroString("if", "(){\\n   \\n}\\n",9,false);
			defaultJson+=generateMacroString("while", "(){\\n   \\n}\\n",9,false);
			defaultJson+=generateMacroString("do", "{\\n   \\n}while();\\n",3,false);
			defaultJson+=generateMacroString("switch", "(){\\n   case 1:\\n   break;\\n   default:\\n}\\n",38,true);
			defaultJson+="\n    ],\r\n"
					+ "    \"replaceMacros\": [";
			defaultJson+=generateReplaceMacroString("setup", "void setup(){\\n   \\n}\\n",3,"",false);
			defaultJson+=generateReplaceMacroString("draw", "void draw(){\\n   \\n}\\n",3,"",false);
			defaultJson+=generateReplaceMacroString("swing", "import javax.swing.*;\\n",0,"",false);
			defaultJson+="\n    ]\r\n"
					+ "}";
	        //pw.write("this is\n a test");
			PrintWriter pw = new PrintWriter(path);
			pw.write(defaultJson);
			pw.flush();
	        pw.close();
		} catch (Exception e) {
			System.out.println("fillJson Error: " + e);
		}
		
	}
	private static String generateMacroString(String key, String code, int carback, boolean last) {
		String s ="";
		s+="\n        {";
		s+="\n            \"key\":\"" + key + "\",";
		s+="\n            \"code\": \"" + code + "\",";
		s+="\n            \"carBack\":"+carback;
		s+="\n        }";
		if (!last) {
			s+=",";
		}
		return s;
	}
	
	private static String generateReplaceMacroString(String key, String code, int carback, String imp, boolean last) {
		String s ="";
		s+="\n        {";
		s+="\n            \"key\":\"" + key + "\",";
		s+="\n            \"code\": \"" + code + "\",";
		s+="\n            \"carBack\":"+carback + ",";
		s+="\n            \"import\": \"" + imp+"\"";
		s+="\n        }";
		if (!last) {
			s+=",";
		}
		return s;
	}
}
