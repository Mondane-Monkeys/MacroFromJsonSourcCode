/**
 * 
 */

/**
 * @author Admin
 *
 */

package MacroFromJson;

import processing.app.ui.Editor;

import java.io.FileReader;
import java.util.Iterator;
import java.util.Map;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class InitMacroList {
	
	/**
	 * Takes json file and parses into a Macros[] array
	 * @param jsonPath
	 * @return 
	 */
	public static Macros[] parseMacro(String jsonPath) {
		Macros[] returnArray = {};
		try {
			// Get json object from file
			FileReader fileRead = new FileReader(jsonPath);
			Object obj = new JSONParser().parse(fileRead);
			JSONObject jo = (JSONObject) obj;
			fileRead.close();

			// Parse json object and concat to returnArray
			returnArray = concatMacroArrays(returnArray, genBoolArray(jo));
			returnArray = concatMacroArrays(returnArray, jsonArrayParser(jo, "macros", false));
			returnArray = concatMacroArrays(returnArray, jsonArrayParser(jo, "replaceMacros", true));
			System.out.println(returnArray.length + " macros found.");
			
		} catch (Exception e) {
			System.out.println("InitMacroList: \n" + e);
		}
		return returnArray;
	}

	private static Macros[] jsonArrayParser(JSONObject jo, String macroType, boolean isReplace) {
		Macros[] returnArray = {};
		JSONArray ja = (JSONArray) jo.get(macroType);
		Iterator itr = ja.iterator();
		returnArray = new Macros[ja.size()];
		int i = 0;
		while (itr.hasNext()) {
			JSONObject thisMacro = (JSONObject) itr.next();
			String key = (String) thisMacro.get("key");
			String code = (String) thisMacro.get("code");
			long carBack = (long) thisMacro.get("carBack");
			if (!isReplace) {
				returnArray[i] = new Macros(key, code, (int) carBack);
			} else if (isReplace) {
				returnArray[i] = new ReplaceMacros(key, code, (int) carBack);
			}
			i++;
		}
		return returnArray;
	}

	private static Macros[] concatMacroArrays(Macros[] array1, Macros[] array2) {
		int arr1Len = array1.length;
		int arr2Len = array2.length;
		Macros[] returnArray = new Macros[arr1Len + arr2Len];
		System.arraycopy(array1, 0, returnArray, 0, arr1Len);
		System.arraycopy(array2, 0, returnArray, arr1Len, arr2Len);
		return returnArray;
	}

	private static Macros[] genBoolArray(JSONObject jo) {
		Macros[] returnArray = {};
		boolean functionMacroToggle = (boolean) jo.get("FunctionMacros");
		boolean codeSkeletonToggle = (boolean) jo.get("CodeSkeletonMacro");
		boolean inputToggle = (boolean) jo.get("input");
		if (codeSkeletonToggle) {
			Macros[] codeSkelArr = {new CodeSkeletonMacro()};
			returnArray = concatMacroArrays(returnArray, codeSkelArr);
		}
		if (functionMacroToggle) {
			Macros[] functMacroArr = {new FunctionMacro()};
			returnArray = concatMacroArrays(returnArray, functMacroArr);
		}
		if (inputToggle) {
			Macros[] inputMacros = {
					new ReplaceMacros("input", "JOptionPane.showInputDialog(\"\")",2, "import javax.swing.JOptionPane;\n"),
			        new ReplaceMacros("output", "JOptionPane.showMessageDialog(null, \"\");",3, "import javax.swing.JOptionPane;\n"),
			        new ReplaceMacros("inputstr", "String str = JOptionPane.showInputDialog(\"\");",3, "import javax.swing.JOptionPane;\n")
			};
			returnArray = concatMacroArrays(returnArray, inputMacros);
		}
		return returnArray;
	}
}