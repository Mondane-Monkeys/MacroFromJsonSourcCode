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
import processing.app.ui.Editor;

import javax.management.loading.PrivateClassLoader;
import javax.swing.*;
import javax.swing.Box.Filler;

import java.awt.event.ActionEvent;
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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

/*
 * Creates framework for the gui interface
 */
public class MacroGui {

	/**
	 * Tracks unique macroGroups
	 */
	private MacroGroup[] macroGroups = {};
	
	/**
	 * Tracks all keys
	 */
	private JTextField[] keysFields = { new JTextField("ThisKey\ntt"), new JTextField("ThisKey\ntt"),
			new JTextField("ThisKey\ntt") };
	
	/**
	 * Tracks all code fields
	 */
	private JTextArea[] codeFields = { new JTextArea("ThisCode"), new JTextArea("ThisCode"),
			new JTextArea("ThisCode") };
	/**
	 * Tracks all carBack fields
	 */
	private JTextField[] carBack = { new JTextField("thisCarBack"), new JTextField("thisCarBack"),
			new JTextField("thisCarBack") };
	/**
	 * Tracks all removeKey checkboxes
	 */
	private JCheckBox[] removeKeyFields = { new JCheckBox("thisCheck"), new JCheckBox("thisCheck"),
			new JCheckBox("thisCheck") };
	/**
	 * Tracks all import fields
	 */
	private JTextField[] importField = { new JTextField("imp"), new JTextField("imp"), new JTextField("imp") };
	/**
	 * Tracks all group fields
	 */
	private JTextField[] groupNameField = { new JTextField("group"), new JTextField("group"),
			new JTextField("group") };
	// private JComboBox<String>[] groupNameBoxs = new JComboBox<String>[1];
	

	private JPanel macroBodyJPanel = new JPanel();
	private JPanel macroPanel = new JPanel();
	private int size;

	

	public void createFrame() {
		getFromJson();
		JFrame frame = new JFrame();
		frame.setSize(775, 900);
		fillFrame(frame);
	}

	private void fillFrame(JFrame frame) {
		JPanel buttonPanel = new JPanel();
		// MacroPanel
				// Headers
//				JPanel macroPanel = new JPanel();
				fillGrid();
//				fillTable();
				fillMacroPanel();

				// ButtonPanel
				// OKAY BUTTON
				buttonPanel.add(new JButton(new AbstractAction("OK") {
					@Override
					public void actionPerformed(ActionEvent e) {
						System.out.println("OK Button");
						for (int i = 0; i < keysFields.length; i++) {
//							System.out.print(keysFields[i].getText() + " : ");
//							System.out.print(codeFields[i].getText() + " : ");
//							System.out.print(carBack[i].getText() + " : ");
//							System.out.print(removeKeyFields[i].getText() + "\n");
						}
						ConfigInit.saveJsonFile(writeToJson(),new File(Const.relativePath));
						frame.validate();
					}
				}));
				// New Line BUTTON
				buttonPanel.add(new JButton(new AbstractAction("New Line") {
					@Override
					public void actionPerformed(ActionEvent e) {
						System.out.println("NewLineButton");
						guiAddMacro();
//						fillGrid();
						//macroPanel.add(new JScrollPane(macroBodyJPanel), BorderLayout.CENTER);
						//frame.add(macroPanel, BorderLayout.CENTER);
						//macroBodyJPanel = guiLines();
						frame.validate();
					}
				}));
				// MANAGEGROUPS Button
				buttonPanel.add(new JButton(new AbstractAction("ManageGroups") {
					@Override
					public void actionPerformed(ActionEvent e) {
						System.out.println("Groups Button");
						frame.validate();
					}
				}));
				// Close window button
				buttonPanel.add(new JButton(new AbstractAction("Cancel") {
					@Override
					public void actionPerformed(ActionEvent e) {
						System.out.println("Cancel Button");
						if (guiClose()) {
							frame.dispose();
						}
					}
				}));
				frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				frame.addWindowListener(new WindowAdapter() {

					@Override
					public void windowClosing(WindowEvent we) {
						if (guiClose()) {
							guiReset();
							frame.dispose();
						}
					}

				});
				
				frame.add(macroPanel, BorderLayout.CENTER);
				frame.add(buttonPanel, BorderLayout.PAGE_END);

				frame.setVisible(true);
	}
	
	private void fillMacroPanel() {
		JPanel macroHeader = new JPanel();
		macroHeader.setBackground(new Color(255, 0, 0));
		macroHeader.add(new JButton());
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				System.out.println(codeFields[0].getText());
//				frame.validate();
//			}
//		}));

		macroPanel.setBackground(new Color(0, 0, 255));
		macroPanel.setLayout(new BorderLayout());
		macroPanel.add(macroHeader, BorderLayout.PAGE_START);
		JScrollPane macroScroll = new JScrollPane(macroBodyJPanel);
		macroScroll.getVerticalScrollBar().setUnitIncrement(16);
		macroPanel.add(macroScroll, BorderLayout.CENTER);
	}
	
	
	private void fillGrid() {
		JPanel localPanel = new JPanel();
		localPanel.setLayout(new BoxLayout(localPanel, BoxLayout.Y_AXIS));
		for (int i = 0; i < keysFields.length; i++) {
			JPanel MBNewLine = new JPanel();
			MBNewLine.setLayout(new BorderLayout());
//			// groupNameBoxs[i] = new JComboBox<String>(choices);
//			JPanel MBStart = new JPanel();
//			MBStart.setLayout(new BorderLayout());
//			JPanel MBCenter = new JPanel();
//			MBCenter.setLayout(new BorderLayout());
//			JPanel MBEnd = new JPanel();
//			MBEnd.setLayout(new BorderLayout());
//
//			// LineStart
//			MBStart.add(new JScrollPane(keysFields[i]));
//			// Center
//
////			MBCenter.add(new JScrollPane(codeFields[i]));
//			MBCenter.add(codeFields[i], BorderLayout.CENTER);
//			// Line end
//			carBack[i].setPreferredSize(new Dimension(18,20));
////			removeKeyFields[i].setPreferredSize(new Dimension());
////			groupNameField[i]
//			//new JScrollPane(codeFields[i])
//			MBEnd.add(carBack[i], BorderLayout.LINE_START);
//			MBEnd.add(removeKeyFields[i], BorderLayout.LINE_END);
//			MBEnd.add(groupNameField[i], BorderLayout.CENTER);
//			
//			//cosmetic
//			MBEnd.setPreferredSize(new Dimension(200,25));
//			MBEnd.setBackground(Color.CYAN);
//			MBStart.setPreferredSize(new Dimension(125,25));
////			MBEnd.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
//			
//			MBNewLine.add(MBStart, BorderLayout.LINE_START);
//			MBNewLine.add(MBCenter, BorderLayout.CENTER);
//			MBNewLine.add(MBEnd, BorderLayout.LINE_END);
//			// macroPanel.add(groupNameBoxs[i]);
			MBNewLine=addLine(i);
			MBNewLine.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			localPanel.add(MBNewLine);
		}
		macroBodyJPanel = localPanel;
	}
	
	private void fillTable() {
		JPanel localPanel = new JPanel();
		JTable table;
		String[] columnNames = {"key", "code", "removeKey"};
		Object[][] data = {keysFields, codeFields, removeKeyFields};
		table = new JTable(data, columnNames);
		table.setFillsViewportHeight(true);
		localPanel.add(table);
		macroBodyJPanel=localPanel;		
	}
	
	
	
	private void addLine(JTextField key, JTextArea code, JTextField carBack, JTextField imp, JCheckBox removeKey, JTextField group) {
		JPanel MBNewLine = new JPanel();
		MBNewLine.setLayout(new BorderLayout());
		// groupNameBoxs[i] = new JComboBox<String>(choices);
		JPanel MBStart = new JPanel();
		MBStart.setLayout(new BorderLayout());
		JPanel MBCenter = new JPanel();
		MBCenter.setLayout(new BorderLayout());
		JPanel MBEnd = new JPanel();

		// LineStart
		MBStart.add(key);
		// Center

//		MBCenter.add(new JScrollPane(codeFields[i]));
		MBCenter.add(code, BorderLayout.CENTER);
		// Line end
		MBEnd.add(carBack);
		MBEnd.add(removeKey);
		MBEnd.add(group);
		MBEnd.setPreferredSize(new Dimension(200,25));
		MBEnd.setBackground(Color.CYAN);
		MBStart.setPreferredSize(new Dimension(200,25));
		MBEnd.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		MBNewLine.add(MBStart, BorderLayout.LINE_START);
		MBNewLine.add(MBCenter, BorderLayout.CENTER);
		MBNewLine.add(MBEnd, BorderLayout.LINE_END);
		// macroPanel.add(groupNameBoxs[i]);
		MBNewLine.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		macroBodyJPanel.add(MBNewLine);
	}
	
	private JPanel addLine(int ID) {
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

			// LineStart
			MBStart.add(new JScrollPane(keysFields[ID]));
			// Center
			keysFields[ID].setHorizontalAlignment(JTextField.CENTER);
			carBack[ID].setHorizontalAlignment(JTextField.CENTER);

//			MBCenter.add(new JScrollPane(codeFields[i]));
			MBCenter.add(codeFields[ID], BorderLayout.CENTER);
			// Line end
			carBack[ID].setPreferredSize(new Dimension(36,20));
//			removeKeyFields[i].setPreferredSize(new Dimension());
//			groupNameField[i]
			//new JScrollPane(codeFields[i])
			MBEnd.add(carBack[ID], BorderLayout.LINE_START);
			MBEnd.add(removeKeyFields[ID], BorderLayout.LINE_END);
			MBEnd.add(groupNameField[ID], BorderLayout.CENTER);
			
			//cosmetic
			MBEnd.setPreferredSize(new Dimension(200,25));
			MBEnd.setBackground(Color.CYAN);
			MBStart.setPreferredSize(new Dimension(125,25));
//			MBEnd.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			
			MBNewLine.add(MBStart, BorderLayout.LINE_START);
			MBNewLine.add(MBCenter, BorderLayout.CENTER);
			MBNewLine.add(MBEnd, BorderLayout.LINE_END);
			// macroPanel.add(groupNameBoxs[i]);
			MBNewLine.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
			localPanel.add(MBNewLine);
		return localPanel;
	}
	
	private void guiAddMacro() {
		keysFields = appendJTFieldsArray(keysFields,new JTextField("this is a test"));
		codeFields = appendJTAreaArray(codeFields,new JTextArea());
		carBack = appendJTFieldsArray(carBack,new JTextField());
		importField = appendJTFieldsArray(importField,new JTextField());
		removeKeyFields = appendJTCheckArray(removeKeyFields,new JCheckBox());
		groupNameField = appendJTFieldsArray(groupNameField,new JTextField());
		int lastID = keysFields.length-1;
		addLine(keysFields[lastID], codeFields[lastID], carBack[lastID], importField[lastID], removeKeyFields[lastID], groupNameField[lastID]);
	}
	
	private void guiSave() {

	}

	private boolean guiClose() {
		String ObjButtons[] = { "Yes", "No" };
		int PromptResult = JOptionPane.showOptionDialog(null, "Are you sure you want to exit?",
				"Macro Editor Confirmation", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, ObjButtons,
				ObjButtons[1]);
		if (PromptResult == JOptionPane.YES_OPTION) {
			return true;
		}
		return false;
	}

	private void guiManageGroups() {

	}

	private void guiReset() {

	}

	private void guiRemoveMacro(int ID) {
		
	}
	
	private void getFromJson() {
//		!!!!choices (groups names)!!! - TODO
//		keyField - String - new JTextField("ThisKey\ntt")
//		codeField - String - new JTextArea("ThisCode")
//		carBack - String = new JTextField("thisCarBack")
//		removeKeyFields bool = new JCheckBox("")
//		importField - String = new JTextField("imp")
		
		Macros[] tempMacroList = InitMacroList.parseMacros(Const.relativePath, "", false);
		MacroGroup[] tempMacroGroupList = InitMacroList.parseMacroGroup(Const.relativePath, Const.groupArrName);
		
		keysFields = new JTextField[tempMacroList.length];
		codeFields = new JTextArea[tempMacroList.length];
		carBack = new JTextField[tempMacroList.length];
		removeKeyFields = new JCheckBox[tempMacroList.length];
		importField = new JTextField[tempMacroList.length];
		groupNameField = new JTextField[tempMacroList.length];
		macroGroups = new MacroGroup[0];
		
		for (int i = 0; i < tempMacroGroupList.length; i++) {
			macroGroups = addUniqueMacroGroup(macroGroups, tempMacroGroupList[i]);
		}
		
		for (int i = 0; i < tempMacroList.length; i++) {
			keysFields[i] = new JTextField(tempMacroList[i].getKey());
			codeFields[i] = new JTextArea(tempMacroList[i].getCode());
			carBack[i] = new JTextField("" + tempMacroList[i].getCarBack());
			importField[i] = new JTextField(tempMacroList[i].getImp());
			removeKeyFields[i] = new JCheckBox();
			removeKeyFields[i].setSelected(tempMacroList[i].getRemoveKey());
			groupNameField[i] = new JTextField(tempMacroList[i].getGroupString());
			
		}
	}

	private String writeToJson() {
		String s = "";
		System.out.println("write to json from gui ran");
		updateMacroGroups();
		
		//Generate boolean macros
		s += Const.generateTopLevelStrings("CodeSkeletonMacro", true, true);
		s += Const.generateTopLevelStrings("FunctionMacros", true, false);
		s += Const.generateTopLevelStrings("input", true, false);
		s += Const.generateTopLevelStrings("macroGroups", true);
		
		//Generate macro group controller array
		
		for (int i = 0; i < macroGroups.length; i++) {
			String groupName = macroGroups[i].getName();
			Boolean groupIsActive = macroGroups[i].getIsActive();
			System.out.println(i == keysFields.length - 1);
			s += Const.generateGroupArrString(groupName, groupIsActive, i == macroGroups.length - 1);
		}
		
		//Create macro arrays
		for (int i = 0; i < macroGroups.length; i++) {
			s += Const.generateTopLevelStrings(macroGroups[i].getName(), false);
			boolean first = true;
			for (int j = 0; j < keysFields.length; j++) {
				if (macroGroups[i].getName().equals(groupNameField[j].getText())) {
					String fixedCode = codeFields[j].getText();
					fixedCode = fixedCode.replaceAll("\n","\\\\n");
					
					String fixedImp = importField[j].getText();
					fixedImp = fixedImp.replaceAll("\n","\\\\n");
					
					System.out.println(fixedCode);
					s += Const.generateMacroString(
							keysFields[j].getText(), 
							fixedCode,
							Integer.parseInt(carBack[j].getText()), 
							fixedImp,
							removeKeyFields[j].isSelected(), 
							first);
					first=false;
				}
			}
		}
		s+=Const.closeJson;
		return s;
	}
	
	/**
	 * iterates over macros in window and adds any new group names to the macroGroup array
	 */
	private void updateMacroGroups() {
		for (int i = 0; i < groupNameField.length; i++) {
			String fieldVal = groupNameField[i].getText();
			boolean matchFound = false;
			for (int j = 0; j < macroGroups.length; j++) {
				String groupVal = macroGroups[j].getName();
				if (groupVal.equals(fieldVal)) {
					matchFound = true;
				}
			}
			if (!matchFound) {
				System.out.println("MagicPokemnt: " +fieldVal);
				macroGroups = addUniqueMacroGroup(macroGroups, new MacroGroup(fieldVal,true));
			}
		}
	}
	
	private MacroGroup[] addUniqueMacroGroup(MacroGroup[] array, MacroGroup element) {
		System.out.println("Element:: " + element.getName());
		boolean unique = true;
		for (int i = 0; i < array.length; i++) {
			if (array[i].getName().equals(element.getName())) {
				//add to array
				unique = false;
				break;
			}
		}
		System.out.println(element.getName() + " isUnique: " + unique);
		if (unique) {
			 array = Arrays.copyOf(array, array.length + 1);
			 array[array.length - 1] = element;
		}
		
		//System.out.println("addUArr");
		for (int i = 0; i < array.length; i++) {
			System.out.println("MagicarrValue: " + array[i].getName());
		}
		return array;
	}
	
	private JTextField[] appendJTFieldsArray(JTextField[] array, JTextField element) {
		array = Arrays.copyOf(array, array.length + 1);
		 array[array.length - 1] = element;
		 return array;
	}
	private JTextArea[] appendJTAreaArray(JTextArea[] array, JTextArea element) {
		array = Arrays.copyOf(array, array.length + 1);
		 array[array.length - 1] = element;
		 return array;
	}
	private JCheckBox[] appendJTCheckArray(JCheckBox[] array, JCheckBox element) {
		array = Arrays.copyOf(array, array.length + 1);
		 array[array.length - 1] = element;
		 return array;
	}
}
