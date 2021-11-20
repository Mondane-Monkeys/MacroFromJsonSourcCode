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

public class MacroFromJson implements Tool, KeyListener {

	Base base;
	Editor editor;
	
	//String variables to specify the macros.json file
	public static String parentPath = "config//";
	public static String shortFileName = "macros";
	public static String fileName = shortFileName + ".json";
	public static String relativePath = parentPath + fileName;
	
	//Array to store all macros. Populated by initMacroList class
	public static Macros[] macroList;
	
	//State variables used for ctrl+b interface
	private static boolean openExplorer = true;
	private static boolean setDefault = false;
	
	// In Processing 3, the "Base" object is passed instead of an "Editor"
	public void init(Base base) {
		// Store a reference to the Processing application itself
		this.base = base;
	}

	public void run() {
		// Run this Tool on the currently active Editor window
		System.out.println("MacroFromJson is running.");
		System.out.println();
		editor = base.getActiveEditor();
		editor.getTextArea().addKeyListener(this);
		
		checkConfig();//Checks macros.json exists or warns user to create
		
		try {
			macroList = InitMacroList.parseMacro(relativePath);
		} catch (Exception e) {
			System.out.println("Init Failed:");
			System.out.println(e);
		}
	}

	public String getMenuTitle() {
		return "MacroFromJson";
	}

	
	/**
	 * Captures user command inputs
	 * 
	 * On ctrl+Space checks text before the caret to find a match in macroList
	 * and runs Macros.instert on that instance
	 * 
	 * On first ctrl+b opens file location of macros.json 
	 * On second ctrl+b re-initializes macroList to catch user changes
	 * 
	 * On first ctrl+shift+b, warns user of file modification
	 * On second, runs ConfigInit.generateJsonFile()
	 */
	@Override
	public void keyPressed(KeyEvent ke) {
		//Runs when ctrl+space are pressed. 
		if (ke.getKeyCode() == KeyEvent.VK_SPACE && ke.getModifiersEx() == InputEvent.CTRL_DOWN_MASK) {
			ke.consume();
			String txt = getTextBeforeCaret();
			int indent = getSpacesBeforeText(txt.length());
			Macros m = find(editor, txt);
			if (m != null) {
				m.insert(editor, indent);
			}
		}
		//Runs when ctrl+shift+b are pressed. Used to create new macro file.
		if (ke.getKeyCode() == KeyEvent.VK_B && ke.isControlDown() && ke.isShiftDown()) {
			if (setDefault) {
				ConfigInit.generateJsonFile();
				setDefault = false;
			} else {
			System.out.println("Are you sure you want to restore default macros?");
			System.out.println("Press ctrl+shift+b again to confirm");
			System.out.println("Press ctrl+b to cancel");
			setDefault = true;
			}
		}
		//Runs when ctrl+b are pressed. Used to open macro file and re-initialize the macroList
		if (ke.getKeyCode() == KeyEvent.VK_B && ke.getModifiersEx() == InputEvent.CTRL_DOWN_MASK) {
			setDefault = false;
			try {
				if(openExplorer) {
				File myPath = new File(parentPath);
				Desktop desktop = Desktop.getDesktop();
				desktop.open(myPath);
				System.out.println("After making changes to the Json file, press ctrl+b to enable new macros.");
				openExplorer = false;
				} else {
					macroList = InitMacroList.parseMacro(relativePath);
					System.out.println("Macro list has been updated");
					openExplorer = true;
				}
			} catch (Exception e) {
				System.out.println("ctrl+b error: " + e);
			}
		}
	}

	/**
	 * Used to calculate needed indentation for the macro that will be inserted
	 * 
	 * @param textLen
	 * @return indentation count on line
	 */
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

	/**
	 * Gets chracters of the word immediately behind caret until last space character
	 * @return word before the caret
	 */
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

	/**
	 * Ensures either the macros.json file exists
	 * Or prompts user to create new file.
	 */
	private static void checkConfig() {
		File myFile = new File(relativePath);
		String myPath = relativePath;
		try {
			myPath = myFile.getCanonicalPath();
			if (myFile.exists()) {
				System.out.println("Config file found at:");
				System.out.println(myPath);
			} else {
				throw new FileNotFoundException("(The system cannot find the file specified)");
			}
		} catch (Exception e) {
			System.out.println("Config file, " + fileName + ", missing at: ");
			System.out.println(myPath);
			System.out.println("Press ctrl+b to open file location");
			System.out.println("Press ctrl+shift+b to generate default jsonFile");
			System.out.println();
			System.out.println(e);
		}
	}
}