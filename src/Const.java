/**
 * Part of the MacroFromJson tool for Processing
 *
 * ##copyright##
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * @author ##author##
 * @modified ##date##
 * @version ##tool.prettyVersion##
 */
package MacroFromJson;
public class Const {

	// String variables to specify the macros.json file
	public static String parentPath = "config//";
	public static String shortFileName = "newMacros";
	public static String fileName = shortFileName + ".json";
	public static String relativePath = parentPath + fileName;
	
	public static String groupArrName = "macroGroups";
	//GUI Constants
	public static String GUI_NAME = "Processing's Macro Editor";
	public static String newMacroGroup = "Default";
	
	//Tool tips
	public static String idToolTip = "<html>id test</html>";
	public static String keyToolTip = "<html>This is the trigger word to call a macro.</html>";
	public static String codeToolTip = "<html>This is the text that will be inserted into your code.</html>";
	public static String carbackToolTip = "<html>Sets the resting place of the cursor after the macro is run.<br>Set to 0 to leave the cursor at the end of the inserted macro.</html>";
	public static String replaceToolTip = "<html>Toggle if the key is removed after the macro runs</html>";
	public static String groupToolTip = "<html>Sets the group for this key.<br>You can enable or disable macro groups without losing the macro.</html>";
	
	
	// Message Constants
	public static final String LOAD_MESSAGE = "MacroFromJson is running.\n";
	public static final String CONFIRM_DEFAULT = "\n\nAre you sure you want to restore default macros?\n"
			+ "Press ctrl+shift+b again to confirm\n" + "Press ctrl+b to cancel";
	public static final String SAVE_MESSAGE = "After making changes to the Json file, press ctrl+b to enable new macros.";
	public static final String UPDATED = "Macro list has been updated";
	public static final String NO_CONFIG = "\nPress ctrl+b to open file location\n"
			+ "Press ctrl+shift+b to generate default jsonFile";
	public static final String DEFAULT_SUCCESS = "New default file created at: ";
	public static final String DEFAULT_FAILED = "Error: default file failed to initialize\n"
			+ "Manually restore macro file to: \n";
	public static final String OPEN_FILE = "Press ctrl+b to open file location";
	public static final String BAK_OVERFLOW = "\nError: too many backups in config folder";
	public static final String BAK_FAILED = "\nError: Backup Failed\n"
			+ "Processing does not have file editing permissions. Please install config file manually";
	public static final String DIR_FAILED = "Error: Could not create config directory";

	//default json constants
	public static String defaultGroup = "default";
	
	public static String DEFAULT_JSON() {
		String s = "{\r\n" + "    \"CodeSkeletonMacro\":true,\r\n" + "    \"FunctionMacros\":true,\r\n";
		s += "    \"input\":true,\r\n" + "    \"macros\": [";
		s += generateMacroString("for", "(int i=0; i < 10; i++){\\n   \\n}\\n", 14, "", "false", false);
		s += generateMacroString("if", "(){\\n   \\n}\\n", 9, "", "false", false);
		s += generateMacroString("while", "(){\\n   \\n}\\n", 9, "", "false", false);
		s += generateMacroString("do", "{\\n   \\n}while();\\n", 3, "", "false", false);
		s += generateMacroString("switch", "(){\\n   case 1:\\n   break;\\n   default:\\n}\\n", 38, "", "false", true);
		s += "\n    ],\r\n" + "    \"replaceMacros\": [";
		s += generateMacroString("setup", "void setup(){\\n   \\n}\\n", 3, "", "true", false);
		s += generateMacroString("draw", "void draw(){\\n   \\n}\\n", 3, "", "true", false);
		s += generateMacroString("swing", "", 0, "import javax.swing.*;\\n", "true", true);
		s += "\n    ]\r\n" + "}";
		return s;
	}
	
	public static String generateGroupArrString(String name, boolean isActive, boolean last) {
		String s = "";
		s += "\n        {";
		s += "\n            \"name\":\"" + name + "\",";
		s += "\n            \"isActive\":" + String.valueOf(isActive);
		s += "\n        }";
		s += last ? "" : ",";
		return s;
	}
	
	public static String generateTopLevelStrings(String name, boolean state, boolean first) {
		String s = first ? "{" : "";
		return s+"\n    \""+name+"\":"+String.valueOf(state)+",";
	}
	
	public static String generateTopLevelStrings(String name, boolean first) {
		String s = first ? "" : "\n    ],";
		return s+"\n    \""+name+"\": [";
	}

	/**
	 * 
	 * @param key
	 * @param code
	 * @param carback
	 * @param imp
	 * @param removeKey
	 * @param last
	 * @return
	 * @deprecated
	 */
	public static String generateMacroString(String key, String code, int carback, String imp, String removeKey, boolean last) {
		String s = "";
		s += "\n        {";
		s += "\n            \"key\":\"" + key + "\",";
		s += "\n            \"code\":\"" + code + "\",";
		s += "\n            \"carBack\":" + carback;
		s += "\n            \"import\":\"" + imp + "\",";
		s += "\n            \"removeKey\":" + removeKey;
		s += "\n        }";
		s += last ? "" : ",";
		return s;
	}
	
	public static String generateMacroString(String key, String code, int carback, String imp, boolean removeKey, boolean first) {
		String s = "";
		s += first ? "" : ",";
		s += "\n        {";
		s += "\n            \"key\":\"" + key + "\",";
		s += "\n            \"code\":\"" + code + "\",";
		s += "\n            \"carBack\":" + carback + ",";
		s += "\n            \"import\":\"" + imp + "\",";
		s += "\n            \"removeKey\":" + String.valueOf(removeKey);
		s += "\n        }";
		return s;
	}
	
	public static String closeJson = "\n    ]\n}";
	
	
	public static Macros[] defaultArray = {
			new Macros("for", "(int i=0; i < 10; i++){\n   \n}\n",14,"",false,defaultGroup), 
	        new Macros("if", "(){\n   \n}\n",9,"",false,defaultGroup),
	        new Macros("while", "(){\n   \n}\n",9,"",false,defaultGroup),
	        new Macros("do", "{\n   \n}while();\n",3,"",false,defaultGroup),
	        new Macros("switch", "(){\n   case 1:\n   break;\n   default:\n}\n",38,"",false,defaultGroup),
	        new Macros("setup", "void setup(){\n   \n}\n",3,"",true,defaultGroup),
	        new Macros("draw", "void draw(){\n   \n}\n",3,"",true,defaultGroup),
	        new Macros("swing", "import javax.swing.*;\n",0,"",true,defaultGroup),
	        new Macros("input", "JOptionPane.showInputDialog(\"\")",2, "import javax.swing.JOptionPane;\n",true,defaultGroup),
	        new Macros("output", "JOptionPane.showMessageDialog(null, \"\");",3, "import javax.swing.JOptionPane;\n",true,defaultGroup),
	        new Macros("inputstr", "String str = JOptionPane.showInputDialog(\"\");",3, "import javax.swing.JOptionPane;\n",true,defaultGroup),
	        new CodeSkeletonMacro(),
	        new FunctionMacro()
	};
	
//	private static String generateReplaceMacroString(String key, String code, int carback, String imp, boolean last) {
//		String s = "";
//		s += "\n        {";
//		s += "\n            \"key\":\"" + key + "\",";
//		s += "\n            \"code\": \"" + code + "\",";
//		s += "\n            \"carBack\":" + carback + ",";
//		s += "\n            \"import\": \"" + imp + "\"";
//		s += "\n        }";
//		if (!last) {
//			s += ",";
//		}
//		return s;
//	}

}
