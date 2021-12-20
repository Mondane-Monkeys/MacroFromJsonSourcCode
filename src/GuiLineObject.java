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
import processing.app.syntax.InputHandler.next_char;
import processing.app.ui.Editor;

import javax.management.loading.PrivateClassLoader;
import javax.swing.*;
import javax.swing.Box.Filler;
import javax.xml.transform.Templates;

import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Window;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
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



public class GuiLineObject {
	
	public static Dimension btnIdWidth = new Dimension(65,20);
	public static Dimension endCapWidth = new Dimension(200,25);
	public static Dimension carBackWidth = new Dimension(36,20);
	public static Dimension startWidth = new Dimension(190,25);
	
	
	JTextField keyField;
	JTextArea codeField;
	JTextField carBackField;
	JCheckBox removeKeyField;
	JTextField importField;
	JTextField groupNameField;
	JButton deleteButton;
	int ID;
		
	public GuiLineObject(JTextField keyField,
						JTextArea codeField,
						JTextField carBackField,
						JCheckBox removeKeyField,
						JTextField importField,
						JTextField groupNameField,
						JButton deleteButton,
						int ID) {
		this.keyField = keyField;
		this.codeField = codeField;
		this.carBackField = carBackField;
		this.removeKeyField = removeKeyField;
		this.importField = importField;
		this.groupNameField = groupNameField;
		this.deleteButton = deleteButton;
		this.ID = ID;
	}
	
	public JPanel addLine() {
		JPanel localPanel = new JPanel();
		localPanel.setLayout(new BoxLayout(localPanel, BoxLayout.Y_AXIS));
			JPanel MBNewLine = new JPanel();
			MBNewLine.setLayout(new BorderLayout());
			// groupNameBoxs[i] = new JComboBox<String>(choices);
			JPanel MBStart = new JPanel();
			MBStart.setLayout(new BorderLayout());
			JPanel MBCenter = new JPanel();
			MBCenter.setLayout(new BorderLayout());
			JPanel MBEnd = new JPanel();
			MBEnd.setLayout(new BorderLayout());
			
			//StartSubBox
			JPanel SubMBStart = new JPanel();
			SubMBStart.setLayout(new BorderLayout());

			// LineStart
			MBStart.add(new JScrollPane(keyField), BorderLayout.CENTER);
			
			SubMBStart.add(deleteButton, BorderLayout.CENTER);
			JLabel IdLabel = new JLabel("ID: "+ID);
			IdLabel.setHorizontalAlignment(JLabel.CENTER);
			SubMBStart.add(IdLabel, BorderLayout.NORTH);
			SubMBStart.setPreferredSize(btnIdWidth);
			
			MBStart.add(SubMBStart, BorderLayout.WEST);
			// Center
			keyField.setHorizontalAlignment(JTextField.CENTER);
			carBackField.setHorizontalAlignment(JTextField.CENTER);

//			MBCenter.add(new JScrollPane(codeFields[i]));
			MBCenter.add(codeField, BorderLayout.CENTER);
			// Line end
			carBackField.setPreferredSize(carBackWidth);
//			removeKeyFields[i].setPreferredSize(new Dimension());
//			groupNameField[i]
			//new JScrollPane(codeFields[i])
			MBEnd.add(carBackField, BorderLayout.LINE_START);
			MBEnd.add(removeKeyField, BorderLayout.LINE_END);
			MBEnd.add(groupNameField, BorderLayout.CENTER);
			
			//cosmetic
			MBEnd.setPreferredSize(endCapWidth);
			MBEnd.setBackground(Color.CYAN);
			MBStart.setPreferredSize(startWidth);
//			MBEnd.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			//ErrorHighlighter
			FocusListener highlighter = new FocusListener() {
				@Override
                public void focusGained(FocusEvent e) {
                    //e.getComponent().setBackground(Color.BLUE);
                }
				@Override
				public void focusLost(FocusEvent e) {
					e.getComponent().setBackground(UIManager.getColor("TextField.background"));
				}
			};
			keyField.addFocusListener(highlighter);
			carBackField.addFocusListener(highlighter);
			groupNameField.addFocusListener(highlighter);
			
			MBNewLine.add(MBStart, BorderLayout.LINE_START);
			MBNewLine.add(MBCenter, BorderLayout.CENTER);
			MBNewLine.add(MBEnd, BorderLayout.LINE_END);
			// macroPanel.add(groupNameBoxs[i]);
			MBNewLine.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			localPanel.add(MBNewLine);
		return localPanel;
	}
	
	public static JPanel addHeader() {
		JLabel idLabel = new JLabel("ID");
		JLabel keyLabel = new JLabel("Key");
		JLabel codeLabel = new JLabel("Code");
		JLabel carBackLabel = new JLabel("Cursor");
		JLabel replaceLabel = new JLabel("isReplace");
		JLabel groupLabel = new JLabel("Group");
		
		idLabel.setHorizontalAlignment(JLabel.CENTER);
		keyLabel.setHorizontalAlignment(JLabel.CENTER);
		codeLabel.setHorizontalAlignment(JLabel.CENTER);
		carBackLabel.setHorizontalAlignment(JLabel.CENTER);
		replaceLabel.setHorizontalAlignment(JLabel.CENTER);
		groupLabel.setHorizontalAlignment(JLabel.CENTER);
		
		idLabel.setToolTipText(Const.idToolTip);
		keyLabel.setToolTipText(Const.keyToolTip);
		codeLabel.setToolTipText(Const.codeToolTip);
		carBackLabel.setToolTipText(Const.carbackToolTip);
		replaceLabel.setToolTipText(Const.replaceToolTip);
		groupLabel.setToolTipText(Const.groupToolTip);
		
		idLabel.setBackground(Color.BLACK);
		keyLabel.setBackground(Color.BLUE);
		codeLabel.setBackground(Color.BLACK);
		carBackLabel.setBackground(Color.BLUE);
		replaceLabel.setBackground(Color.BLACK);
		groupLabel.setBackground(Color.BLUE);
		
		
		JPanel localPanel = new JPanel();
		localPanel.setLayout(new BoxLayout(localPanel, BoxLayout.Y_AXIS));
			JPanel MBNewLine = new JPanel();
			MBNewLine.setLayout(new BorderLayout());
			// groupNameBoxs[i] = new JComboBox<String>(choices);
			JPanel MBStart = new JPanel();
			MBStart.setLayout(new BorderLayout());
			JPanel MBCenter = new JPanel();
			MBCenter.setLayout(new BorderLayout());
			JPanel MBEnd = new JPanel();
			MBEnd.setLayout(new BorderLayout());
			
			//StartSubBox
			JPanel SubMBStart = new JPanel();
			SubMBStart.setLayout(new BorderLayout());

			// LineStart
			MBStart.add(keyLabel, BorderLayout.CENTER);
			SubMBStart.add(idLabel, BorderLayout.CENTER);
			SubMBStart.setPreferredSize(new Dimension(65,25));
			
			MBStart.add(SubMBStart, BorderLayout.WEST);
			// Center
			
			

//			MBCenter.add(new JScrollPane(codeFields[i]));
			MBCenter.add(codeLabel, BorderLayout.CENTER);
			// Line end
			carBackLabel.setPreferredSize(new Dimension(36,25));
//			removeKeyFields[i].setPreferredSize(new Dimension());
//			groupNameField[i]
			//new JScrollPane(codeFields[i])
			MBEnd.add(carBackLabel, BorderLayout.LINE_START);
			MBEnd.add(replaceLabel, BorderLayout.LINE_END);
			MBEnd.add(groupLabel, BorderLayout.CENTER);
			
			//cosmetic
			MBEnd.setPreferredSize(new Dimension(200,25));
			MBStart.setPreferredSize(new Dimension(190,25));
//			MBEnd.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			
			MBNewLine.add(MBStart, BorderLayout.LINE_START);
			MBNewLine.add(MBCenter, BorderLayout.CENTER);
			MBNewLine.add(MBEnd, BorderLayout.LINE_END);
			// macroPanel.add(groupNameBoxs[i]);
			MBNewLine.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			localPanel.add(MBNewLine);
			MBNewLine.setBackground(Color.GREEN);
			localPanel.setBackground(Color.PINK);
		return localPanel;
	}
	
	public String validateKey() {
		boolean valid = true;
		String s = "";
		String key = keyField.getText();
		for (char c : key.toCharArray()) {
			if (!(c >= 33 && c <= 126)) {
				valid = false;
				s+=c;
			}
		}
		if (!valid) {
			System.out.println("failed: " + key);
			keyField.setBackground(new Color(255, 251, 187));
			s = "\nString cannot contain [" + s + "]";
		}
		return s;
	}
	
	public String validateCode() {
		return "";
	}
	
	public String validateCarBack() {
		boolean valid = true;
		String s = "";
		String carBack = carBackField.getText();
		
		try {
			int carBackInt = Integer.parseInt(carBack);
			if (carBackInt>codeField.getText().length()+keyField.getText().length()) {
				valid = false;
				s = "\ncarBack should be less than the length of the code and key fields";
			}
		} catch (Exception e) {
			System.out.println("GuiLineObject: "+e);
			s = "\ncarBack must be number characters only";
		}
		
		if (!valid) {
			System.out.println("CarBack failed: " + carBack);
			carBackField.setBackground(new Color(255, 251, 187));
		}
		return s;
	}
	
	public String validateGroup() {
		boolean valid = true;
		String s = "";
		String group = groupNameField.getText();
//		System.out.println(group);
		if (group.length()<1) {
			valid = false;
		}
		if (!valid) {
			System.out.println("Group failed: " + group);
			groupNameField.setBackground(new Color(255, 251, 187));
			s = "\nGroup field must contain at least one character";
		}
		return s;
	}
	
	public JTextField getKey() {
		return keyField;
	}
	public JTextArea getCode() {
		return codeField;
	}
	public JTextField getCarBack() {
		return carBackField;
	}
	public JCheckBox getRemoveKey() {
		return removeKeyField;
	}
	public JTextField getImp() {
		return importField;
	}
	public JTextField getGroupName() {
		return groupNameField;
	}
	public JButton getDelete() {
		return deleteButton;
	}
	
	public String getKeyVal() {
		return keyField.getText();
	}
	public String getCodeVal() {
		return codeField.getText();
	}
	public String getCarBackVal() {
		return carBackField.getText();
	}
	public boolean getRemoveKeyVal() {
		return removeKeyField.isSelected();
	}
	public String getImpVal() {
		return importField.getText();
	}
	public String getGroupNameVal() {
		return groupNameField.getText();
	}
	public int getID() {
		return ID;
	}
}
