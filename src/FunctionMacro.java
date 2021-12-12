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

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import processing.app.ui.Editor;

/**
 * @author dahjon - http://jonathan.dahlberg.media/ecc/
 * FunctionMacro Class takes a standard data type + sometext and adds function syntax to editor
 */
public class FunctionMacro extends Macros {

	public FunctionMacro() {

		super("(void|int|float|double|long|String|char|byte|PVector)\\s+[0-9a-zA-ZåäöÅÄÖ]+\\(", "){\n   \n}\n", 3, "", false, Const.defaultGroup);
	}

	public boolean stringIsThisMacro(Editor editor, String sstr) {
		System.out.println("hereFM");
		String etxt = editor.getText();
		etxt = etxt.substring(0, editor.getCaretOffset());
		int previousLineBreak = etxt.lastIndexOf('\n');
		int nextLineBreak = editor.getText().indexOf('\n', editor.getCaretOffset());
		if (nextLineBreak == -1) {
			nextLineBreak = editor.getText().length();
		}
		if (caretIsOnLastNonspace(nextLineBreak, editor, etxt)
		) {
			String row = etxt.substring(previousLineBreak + 1);
			Pattern p = Pattern.compile(key);
			Matcher m = p.matcher(new StringBuilder(row));
			return m.find();

		} else {
			return false;
		}
	}

	@Override
	public void insert(Editor editor, int indent) {
		char caretChar = editor.getText().charAt(editor.getCaretOffset() - 1);
		if (caretChar == ')') {
			code = code.substring(1);
			super.insert(editor, 0);
			code = ")" + code;
		} else {
			super.insert(editor, 0);
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
}
