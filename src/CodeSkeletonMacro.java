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

/**
 * @author dahjon - http://jonathan.dahlberg.media/ecc/
 * CodeSkeletonMacro Class takes an empty editor and adds setup and draw.
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
	}
}
