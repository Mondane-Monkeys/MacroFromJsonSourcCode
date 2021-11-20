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


	public static Macros[] parseMacro(String jsonPath) {
		Macros[] returnArray = {};
//		ConfigInit.updatePath();
		try {
			//Get json object from file
			FileReader fileRead = new FileReader(jsonPath);
			Object obj = new JSONParser().parse(fileRead);
			JSONObject jo = (JSONObject) obj;
			
			//Parse json object and concat to returnArray
			returnArray = concatMacroArrays(returnArray, genBoolArray(jo));
			returnArray = concatMacroArrays(returnArray, jsonArrayParser(jo, "macros"));
			returnArray = concatMacroArrays(returnArray, jsonArrayParser(jo, "replaceMacros"));
			System.out.println(returnArray.length + " macros found.");
			fileRead.close();
		} catch (Exception e) {
			System.out.println("InitMacroList: \n" + e);
		}
		return returnArray;
	}

	private static Macros[] jsonArrayParser(JSONObject jo, String macroType) {
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
			returnArray[i] = new Macros(key, code, (int) carBack);
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
		if (codeSkeletonToggle) {
			Macros[] codeSkelArr = {new CodeSkeletonMacro()};
			returnArray = concatMacroArrays(returnArray, codeSkelArr);
		}
		if (functionMacroToggle) {
			Macros[] functMacroArr = {new FunctionMacro()};
			returnArray = concatMacroArrays(returnArray, functMacroArr);
		}
		return returnArray;
	}
}