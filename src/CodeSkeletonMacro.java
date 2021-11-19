/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MacroFromJson;

import processing.app.ui.Editor;

/**
 *
 * @author dahjon
 */
public class CodeSkeletonMacro extends Macros {

	public static final String CODE_SKELETON = "void setup() {\n" + "   size(500, 500);\n" + "   \n" + "}\n" + "\n"
			+ "void draw() {\n" + "   \n" + "}\n";

	public static final int CARETPOS = 37;

	public CodeSkeletonMacro() {
		super("", "", 0);
	}

	@Override
	public boolean stringIsThisMacro(Editor editor, String sstr) {
		return editor.getText().trim().length() == 0;
	}

	@Override
	public void insert(Editor editor, int indent) {
		editor.setText(CODE_SKELETON);
		editor.getTextArea().setCaretPosition(CARETPOS);
		// System.out.println("editor.getCaretOffset() = " + editor.getCaretOffset());
	}
}
