

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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

/**
 *
 * @author dahjon
 */
public class Macros {

    protected String key;
    protected String code;
    protected int carBack;


    public Macros(String key, String code, int carBack) {
        this.key = key;
        this.code = code;
        this.carBack = carBack;
    }

    protected int getNumbersOfLineBreaks(String str){
        int nr=0;
        for (int i = 0; i < str.length(); i++) {
            if(str.charAt(i)=='\n'){
                nr++;
            }
        }
        return nr;
    }

    public static Macros find(Editor editor,String sstr) {
        for (int i = 0; i < MacroFromJson.macroList.length; i++) {
            Macros m = MacroFromJson.macroList[i];
            if (m.stringIsThisMacro(editor, sstr)) {
                return m;
            }

        }
        return null;
    }

	public boolean stringIsThisMacro(Editor editor, String sstr) {
        return key.equals(sstr);
    }

    public void insert(Editor editor, int indent) {
        int nr = getNumbersOfLineBreaks(code.substring(code.length()-carBack));
        String indentStr = new String(new char[indent]).replace('\0', ' ');
        String str = code.replaceAll("\n", "\n" + indentStr);
        editor.insertText(str);
        //System.out.println("nr = " + nr);
        int carPos=editor.getCaretOffset()-carBack-nr*indent;
        editor.getTextArea().setCaretPosition(carPos);
        
    }
}
