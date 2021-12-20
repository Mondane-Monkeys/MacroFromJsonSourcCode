 /* Part of the MacroFromJson tool for Processing
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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General
 * Public License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307 USA
 *
 * @author ##author##
 * @modified ##date##
 * @version ##tool.prettyVersion##
 */

package MacroFromJson;

import processing.app.contrib.ContributionListing;
import processing.app.ui.Editor;

import javax.management.loading.PrivateClassLoader;
import javax.swing.*;
import javax.swing.Box.Filler;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.SystemMenuBar;
import javax.xml.transform.Templates;

import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Window;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.security.PublicKey;
import java.util.Iterator;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;
import java.lang.Math;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

public class GroupGui extends JDialog{
	
	MacroGroup[] groupList;
	
	GroupGui(MacroGroup[] groupList) {
		this.groupList = groupList;
		
		setSize(150,150);
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		
		
		for (int i = 0; i < groupList.length; i++) {
			add(newLine(groupList[i]));
//			add(new JButton("Test "+i));
		}
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setVisible(true);
	}

	private JPanel newLine(MacroGroup group) {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		
		JTextField nameField = new JTextField(group.name);
		JButton thisBtn = new JButton(new AbstractAction("Delete") {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("delete " + group.getName());
			}
		});
		
		panel.add(thisBtn, BorderLayout.NORTH);
		panel.add(nameField, BorderLayout.CENTER);
		return panel;
	}
	
	
	public MacroGroup[] getList() {
		return groupList;
	}
	
}
