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
 * ReplaceMacros class holds methods needed remove keyword and add a code
 * snippet to editor
 */
public class ReplaceMacros extends Macros {

	String imp = "";

	public ReplaceMacros(String key, String code, int carBack) {
		super(key, code, carBack);
	}

	public ReplaceMacros(String key, String code, int carBack, String imp) {
		super(key, code, carBack);
		this.imp = imp;
	}

	@Override
	public void insert(Editor editor, int indent) {
		String indentStr = new String(new char[indent]).replace('\0', ' ');
		String str = code.replaceAll("\n", "\n" + indentStr);
		String etxt = editor.getText();
		int cur = editor.getCaretOffset();
		etxt = etxt.substring(0, cur - key.length()) + str + etxt.substring(cur);
		editor.setText(etxt);
		int implen = 0;
		if (imp.length() > 0) {
			if (editor.getText().indexOf(imp) < 0) {
				editor.setText(imp + etxt);
				implen = imp.length();
			}
		}
		int carPos = cur + str.length() + implen - key.length() - carBack;
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

}
