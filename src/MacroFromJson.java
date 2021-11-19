/**
 * you can put a one sentence description of your tool here.
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

import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Desktop;
import processing.app.Base;
import processing.app.tools.Tool;
import processing.app.ui.Editor;

// when creating a tool, the name of the main class which implements Tool must
// be the same as the value defined for project.name in your build.properties

public class MacroFromJson implements Tool, KeyListener {

	Base base;
	Editor editor;
	public static String filePath = "config//";
	public static String fileName = "CustomMacros.json";
	public static String RelativePath = filePath + fileName;
	public static Macros[] macroList;
	// In Processing 3, the "Base" object is passed instead of an "Editor"

	public void init(Base base) {
		// Store a reference to the Processing application itself
		this.base = base;
	}

	public void run() {
		// Run this Tool on the currently active Editor window
		System.out.println("MacroFromJson is running.");
		System.out.println();
		checkConfig();
		editor = base.getActiveEditor();
		editor.getTextArea().addKeyListener(this);
		try {
			macroList = InitMacroList.parseMacro();
		} catch (Exception e) {
			System.out.println("InitFailed");
			System.out.println(e);
		}
	}

	public String getMenuTitle() {
		return "MacroFromJson";
	}

	@Override
	public void keyPressed(KeyEvent ke) {
//		System.out.println("this: " + ke.isShiftDown() +" " + ke.isControlDown() + " " + (ke.getKeyCode()));
		if (ke.getKeyCode() == KeyEvent.VK_SPACE && ke.getModifiersEx() == InputEvent.CTRL_DOWN_MASK) {
			ke.consume();
			String txt = getTextBeforeCaret();
			int indent = getSpacesBeforeText(txt.length());
			Macros m = find(editor, txt);
			if (m != null) {
				m.insert(editor, indent);
			}
		}
		if (ke.getKeyCode() == KeyEvent.VK_B && ke.isControlDown() && ke.isShiftDown()) {
			System.out.println("hi1");
//			ConfigInit.generateJsonFile();
			ConfigInit.Test();
		} else if (ke.getKeyCode() == KeyEvent.VK_B && ke.getModifiersEx() == InputEvent.CTRL_DOWN_MASK) {
			try {
				File myPath = new File(RelativePath);
				;
				Desktop desktop = Desktop.getDesktop();
				desktop.open(myPath);
				System.out.println("hi");
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		
	}

	private int getSpacesBeforeText(int textLen) {
		int start = editor.getCaretOffset() - 1 - textLen;
		int i = start;
		String edtext = editor.getText();
		if (start >= 0) {
			char c = edtext.charAt(start);
			while (c == ' ' && i >= 0) {
				i--;
				if (i >= 0) {
					c = editor.getText().charAt(i);
				}
			}
		}

		return start - i;
	}

	private String getTextBeforeCaret() {
		int start = editor.getCaretOffset() - 1;
		if (start >= 0) {
			int i = start;
			String edtext = editor.getText();
			char c = edtext.charAt(start);
//            while ((Character.isLetterOrDigit(c)||c == '/')&& i >= 0) {
			while ((Character.isLetterOrDigit(c) || (c >= 33 && c <= 126)) && i >= 0) {
				i--;
				if (i >= 0) {
					c = editor.getText().charAt(i);
				}
			}
			i++;
			return editor.getText().substring(i, start + 1);
		} else {
			return "";
		}
	}

	@Override
	public void keyTyped(KeyEvent ke) {
	}

	@Override
	public void keyReleased(KeyEvent ke) {
	}

//returns macro object from ConcatJson.macrosJsonList where key=sstr else null
	public static Macros find(Editor editor, String sstr) {
		for (int i = 0; i < macroList.length; i++) {
			if (macroList[i] != null) {
				Macros m = macroList[i];
				if (m.stringIsThisMacro(editor, sstr)) {
					return m;
				}
			}
		}
		return null;
	}

	private static void checkConfig() {
		File myFile = new File(RelativePath);
		String myPath = RelativePath;
		try {
			myPath = myFile.getCanonicalPath();
			if (myFile.exists()) {
				System.out.println("Config file found at:");
				System.out.println(myPath);
			} else {

				throw new FileNotFoundException("error");
			}
//			Desktop desktop = Desktop.getDesktop();
//			desktop.open(myFile);

		} catch (Exception e) {
			System.out.println("Config file, " + fileName + ", missing at: ");
			System.out.println(myPath);
			System.out.println("Press ctrl+m to open file location");
			System.out.println("Press ctrl+shift+m to generate default jsonFile");
			System.out.println();
			System.out.println(e);
		}
	}
}