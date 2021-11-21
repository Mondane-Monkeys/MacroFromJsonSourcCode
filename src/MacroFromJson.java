/**
 * Allows users to use keywords that can autocomplete to code snippets.
 * Additionally, it can create a template json file to get users started.
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
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Desktop;
import processing.app.Base;
import processing.app.tools.Tool;
import processing.app.ui.Editor;

/**
 *  MacroFromJson is the main class. Primary function flow goes through run() and keyPressed()
 */
public class MacroFromJson implements Tool, KeyListener {

	Base base;
	Editor editor;

	// Array to store all macros. Populated by initMacroList class
	public static Macros[] macroList;

	// State variables used for ctrl+b interface
	private static boolean openExplorer = true;
	private static boolean setDefault = false;

	// In Processing 3, the "Base" object is passed instead of an "Editor"
	public void init(Base base) {
		// Store a reference to the Processing application itself
		this.base = base;
	}

	/**
	 * Runs once for setup
	 */
	public void run() {
		// Run this Tool on the currently active Editor window
		System.out.println(Const.LOAD_MESSAGE);
		editor = base.getActiveEditor();
		editor.getTextArea().addKeyListener(this);

		// Initialize macroList
		macroList = InitMacroList.parseMacros(Const.relativePath, "");
	}

	/**
	 * Used by Processing to fill Tools dropdown menu
	 */
	public String getMenuTitle() {
		return "MacroFromJson";
	}

	/**
	 * Primary source of functionality: Captures user command inputs
	 * 
	 * On ctrl+Space checks text before the caret to find a match in macroList and
	 * runs Macros.instert on that instance
	 * 
	 * On first ctrl+b opens file location of macros.json On second ctrl+b
	 * re-initializes macroList to catch user changes
	 * 
	 * On first ctrl+shift+b, warns user of file modification On second, runs
	 * ConfigInit.generateJsonFile()
	 */
	@Override
	public void keyPressed(KeyEvent ke) {
		// Runs when ctrl+space are pressed.
		// Used to trigger macro behaviour
		if (ke.getKeyCode() == KeyEvent.VK_SPACE && ke.getModifiersEx() == InputEvent.CTRL_DOWN_MASK) {
			ke.consume();
			String txt = getTextBeforeCaret();
			int indent = getSpacesBeforeText(txt.length());
			Macros m = find(editor, txt);
			if (m != null) {
				m.insert(editor, indent);
			}
		}
		// Runs when ctrl+shift+b are pressed.
		// Used to create new macro file.
		if (ke.getKeyCode() == KeyEvent.VK_B && ke.isControlDown() && ke.isShiftDown()) {
			if (setDefault) {
				ConfigInit.generateJsonFile();
				macroList = InitMacroList.parseMacros(Const.relativePath, "");
				setDefault = false;
			} else {
				System.out.println(Const.CONFIRM_DEFAULT);
				
				setDefault = true;
			}
		}
		// Runs when ctrl+b are pressed.
		// Used to open macro file and re-initialize the macroList
		if (ke.getKeyCode() == KeyEvent.VK_B && ke.getModifiersEx() == InputEvent.CTRL_DOWN_MASK) {
			setDefault = false;
			try {
				// First triggered
				// Opens file location for editing
				if (openExplorer) {
					File myPath = new File(Const.parentPath);
					Desktop desktop = Desktop.getDesktop();
					desktop.open(myPath);
					System.out.println(Const.SAVE_MESSAGE);
					macroList = InitMacroList.parseMacros(Const.relativePath, "Before change: ");
					openExplorer = false;
					
					// Second trigger
					// updates macroList with any changes
				} else {
					macroList = InitMacroList.parseMacros(Const.relativePath, "After change: ");
					System.out.println(Const.UPDATED);
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
	 * Gets chracters of the word immediately behind caret until last space
	 * character
	 * 
	 * @return word before the caret
	 */
	private String getTextBeforeCaret() {
		int start = editor.getCaretOffset() - 1;
		if (start >= 0) {
			int i = start;
			String edtext = editor.getText();
			char c = edtext.charAt(start);
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

	/**
	 * Returns macro object from ConcatJson.macrosJsonList where key=sstr else null
	 * 
	 * @param editor
	 * @param sstr   search term
	 * @return macro matching search term
	 */
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
}