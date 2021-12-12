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
 *
 * @author dahjon
 * @modifiedBy ##author##
 * @modified ##date##
 * @version  ##tool.prettyVersion##
 */

package MacroFromJson;

import processing.app.ui.Editor;

import java.io.FileReader;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Macros class holds methods needed to take a keyword and add code snippet to
 * editor
 */
public class Macros {

	protected String key;
	protected String code;
	protected int carBack;
	protected String imp;
	protected boolean removeKey;
	protected String groupString;

	public Macros(String key, String code, int carBack, String imp, boolean removeKey, String groupString) {
		this.key = key;
		this.code = code;
		this.carBack = carBack;
		this.imp = imp;
		this.removeKey = removeKey;
		this.groupString = groupString;
	}

	protected int getNumbersOfLineBreaks(String str) {
		int nr = 0;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == '\n') {
				nr++;
			}
		}
		return nr;
	}

	public static Macros find(Editor editor, String sstr) {
		for (int i = 0; i < MacroFromJson.macroList.length; i++) {
			Macros m = MacroFromJson.macroList[i];
			if (m.stringIsThisMacro(editor, sstr)) {
				return m;
			}

		}
		return null;
	}

//	public boolean stringIsThisMacro(Editor editor, String sstr) {
//		return key.equals(sstr);
//	}

	public boolean stringIsThisMacro(Editor editor, String sstr) {
		System.out.println(key);
		String etxt = editor.getText();
		etxt = etxt.substring(0, editor.getCaretOffset());
		int previousLineBreak = etxt.lastIndexOf('\n');
		int nextLineBreak = editor.getText().indexOf('\n', editor.getCaretOffset());
		if (nextLineBreak == -1) {
			nextLineBreak = editor.getText().length();
		}
		if (caretIsOnLastNonspace(nextLineBreak, editor, etxt)) {
			String row = etxt.substring(previousLineBreak + 1);
			Pattern p = Pattern.compile(key);
			Matcher m = p.matcher(new StringBuilder(row));
			return m.find();

		} else {
			return key.equals(sstr);
		}
	}

	public static boolean caretIsOnLastNonspace(int nextLineBreak, Editor editor, String etxt) {
		int caretOffset = editor.getCaretOffset() - 1;
		nextLineBreak--;
		while (nextLineBreak >= 0 && editor.getText().charAt(nextLineBreak) == ' ') {
			nextLineBreak--;
		}
		return nextLineBreak == caretOffset;
	}

	public void insert(Editor editor, int indent) {
		int subS = code.length() - carBack;
		if (subS < 0) {
			subS = 0;
		}
		int nr = getNumbersOfLineBreaks(code.substring(0, subS));
		String indentStr = new String(new char[indent]).replace('\0', ' ');
		String str = code.replaceAll("\n", "\n" + indentStr);
		String etxt = editor.getText();
		int cur = editor.getCaretOffset();
		int curOffset = 0;
		if (removeKey) {
			curOffset = key.length();
		}
		etxt = etxt.substring(0, cur - curOffset) + str + etxt.substring(cur);
		editor.setText(etxt);
		int importLength = 0;
		if (imp.length() > 0) {
			if (editor.getText().indexOf(imp) < 0) {
				editor.setText(imp + etxt);
				importLength = imp.length();
			}
		}
		// int carPos = cur + str.length() + importLength - curOffset - carBack;
		String cat = "cat|dog";
//		System.out.print("key: " + key + " = " + cat + " : ");
//		System.out.println(key.equals(cat));
		int carPos = cur + code.length() + importLength + curOffset + nr * indent - carBack;
		editor.getTextArea().setCaretPosition(carPos);
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public int getCarBack() {
		return carBack;
	}

	public void setCarBack(int carBack) {
		this.carBack = carBack;;
	}

	public String getImp() {
		return imp;
	}

	public void setImp(String imp) {
		this.code = imp;
	}
	
	public boolean getRemoveKey() {
		return removeKey;
	}

	public void setRemoveKey(boolean removeKey) {
		this.removeKey = removeKey;
	}

	public String getGroupString() {
		return groupString;
	}

	public void setGroupString(String groupString) {
		this.groupString = groupString;
	}
}
