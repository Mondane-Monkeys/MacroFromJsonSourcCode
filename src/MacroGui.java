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
import javax.swing.plaf.basic.BasicInternalFrameTitlePane.SystemMenuBar;
import javax.xml.transform.Templates;

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
import java.lang.Math;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

/*
 * Creates framework for the gui interface
 */
public class MacroGui {

	//NOT USED
	private MacroGroup[] macroGroups = {};
	private JTextField[] keysFields = {};
	private JTextArea[] codeFields = {};
	private JTextField[] carBack = {};
	private JCheckBox[] removeKeyFields = {};
	private JTextField[] importField = {};
	private JTextField[] groupNameField = {};
	private JButton[] deletButtonArr = {};
	//
	
	private GuiLineObject[] macroLines = {};
	private JFrame frame = new JFrame();
	private JPanel macroBodyJPanel = new JPanel();
	private JPanel macroPanel = new JPanel();
	private int macroCount = 0;
	private int size;

	

	public void createFrame() {
		getFromJson();
		frame.setSize(840, 900);
		fillFrame(frame);
	}

	private void fillFrame(JFrame frame) {
		JPanel buttonPanel = new JPanel();
		// MacroPanel
				// Headers
				fillGrid();

				// ButtonPanel
				// OKAY BUTTON
				buttonPanel.add(new JButton(new AbstractAction("Save") {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (validateFields()) {
						ConfigInit.saveJsonFile(writeToJson(),new File(Const.relativePath));
						MacroFromJson.macroList = InitMacroList.parseMacros(Const.relativePath, "", true);
						}
						frame.validate();
					}
				}));
				// New Line BUTTON
				buttonPanel.add(new JButton(new AbstractAction("New Line") {
					@Override
					public void actionPerformed(ActionEvent e) {
						System.out.println("NewLineButton");
						guiAddMacro();
						frame.validate();
					}
				}));
				//Future feature:
					//Change groupsfield to a dropdown. 
					//Selection should be from macroGroups[]
					//This button should open a new GroupGui (extends jframe)
					//Should then update macroGroups
//				// MANAGEGROUPS Button
//				buttonPanel.add(new JButton(new AbstractAction("ManageGroups") {
//					@Override
//					public void actionPerformed(ActionEvent e) {
//						System.out.println("Groups Button");
//						GroupGui groupEdit = new GroupGui(macroGroups);
//						frame.validate();
//					}
//				}));
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
				
				frame.add(buttonPanel, BorderLayout.PAGE_END);
				frame.setVisible(true);
	}
	
	private void fillGrid() {
		frame.remove(macroPanel);
		JPanel localPanel = new JPanel();
		localPanel.setLayout(new BoxLayout(localPanel, BoxLayout.Y_AXIS));
		localPanel.add(GuiLineObject.addHeader());
		for (int i = 0; i < macroLines.length; i++) {
			JPanel MBNewLine = new JPanel();
			//MBNewLine.setLayout(new BorderLayout());
			MBNewLine=macroLines[i].addLine();
			MBNewLine.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
			localPanel.add(MBNewLine);
		}
		macroBodyJPanel = localPanel;
		JPanel macroHeader = new JPanel();
		macroHeader.setBackground(new Color(255, 0, 0));
		macroPanel = new JPanel();
		macroPanel.setBackground(new Color(0, 0, 255));
		macroPanel.setLayout(new BorderLayout());
		macroPanel.add(macroHeader, BorderLayout.PAGE_START);
		JScrollPane macroScroll = new JScrollPane(macroBodyJPanel);
		macroScroll.getVerticalScrollBar().setUnitIncrement(16);
		macroPanel.add(macroScroll, BorderLayout.CENTER);
		
		frame.add(macroPanel, BorderLayout.CENTER);
		frame.validate();
		frame.setVisible(true);
	}

	private void guiAddMacro() {
		JTextField tempKeyField = new JTextField("");
		JTextArea tempCodeFields = new JTextArea("");
		JTextField tempCarBack = new JTextField("0");
		JTextField tempImportField = new JTextField("");
		JCheckBox tempRemoveKeyFields = new JCheckBox();
		tempRemoveKeyFields.setSelected(false);
		JTextField tempGroupNameField = new JTextField(Const.newMacroGroup);
		JButton tempDeletButton = generateDeleteBtn(macroLines,macroCount);
		
		GuiLineObject newGuiLine= new GuiLineObject(tempKeyField,tempCodeFields,tempCarBack,tempRemoveKeyFields,tempImportField,tempGroupNameField,tempDeletButton, macroCount); 
		macroCount++;
		macroLines = appendMacroLines(macroLines, newGuiLine);
		JPanel tempPanel = macroLines[macroLines.length-1].addLine();
		tempPanel.setBackground(Color.YELLOW);
		macroBodyJPanel.add(tempPanel);
		
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
//		choices (groups names)
//		keyField - String - new JTextField("ThisKey\ntt")
//		codeField - String - new JTextArea("ThisCode")
//		carBack - String = new JTextField("thisCarBack")
//		removeKeyFields bool = new JCheckBox("")
//		importField - String = new JTextField("imp")
		
		Macros[] tempMacroList = InitMacroList.parseMacros(Const.relativePath, "", false);
		MacroGroup[] tempMacroGroupList = InitMacroList.parseMacroGroup(Const.relativePath, Const.groupArrName);
		
		//reset arrays
		macroLines = new GuiLineObject[tempMacroList.length];
		macroGroups = new MacroGroup[0];
		
		keysFields = new JTextField[tempMacroList.length];
		codeFields = new JTextArea[tempMacroList.length];
		carBack = new JTextField[tempMacroList.length];
		removeKeyFields = new JCheckBox[tempMacroList.length];
		importField = new JTextField[tempMacroList.length];
		groupNameField = new JTextField[tempMacroList.length];
		
		
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
			//deletButtonArr = addJBtnArray(deletButtonArr);
			
			JTextField tempKeyField = new JTextField(tempMacroList[i].getKey());
			JTextArea tempCodeFields = new JTextArea(tempMacroList[i].getCode());
			JTextField tempCarBack = new JTextField("" + tempMacroList[i].getCarBack());
			JTextField tempImportField = new JTextField(tempMacroList[i].getImp());
			JCheckBox tempRemoveKeyFields = new JCheckBox();
			tempRemoveKeyFields.setSelected(tempMacroList[i].getRemoveKey());
			JTextField tempGroupNameField = new JTextField(tempMacroList[i].getGroupString());
			JButton tempDeletButton = generateDeleteBtn(macroLines, macroCount);
			
			macroLines[i]= new GuiLineObject(tempKeyField,tempCodeFields,tempCarBack,tempRemoveKeyFields,tempImportField,tempGroupNameField,tempDeletButton, macroCount); 
			macroCount++;
		}
	}

	private String writeToJson() {
		String s = "";
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
			s += Const.generateGroupArrString(groupName, groupIsActive, i == macroGroups.length - 1);
		}
		
		//Create macro arrays
		for (int i = 0; i < macroGroups.length; i++) {
			s += Const.generateTopLevelStrings(macroGroups[i].getName(), false);
			boolean first = true;
			for (int j = 0; j < macroLines.length; j++) {
				if (macroGroups[i].getName().equals(macroLines[j].getGroupNameVal())) {
					String fixedCode = macroLines[j].getCodeVal();
					fixedCode = fixedCode.replaceAll("\n","\\\\n");
					String fixedImp = macroLines[j].getImpVal();
					fixedImp = fixedImp.replaceAll("\n","\\\\n");
					s += Const.generateMacroString(
							macroLines[j].getKeyVal(), 
							fixedCode,
							Integer.parseInt(macroLines[j].getCarBackVal()), 
							fixedImp,
							macroLines[j].getRemoveKeyVal(), 
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
		macroGroups = new MacroGroup[0];
		for (int i = 0; i < macroLines.length; i++) {
			String fieldVal = macroLines[i].getGroupNameVal();
			boolean matchFound = false;
			for (int j = 0; j < macroGroups.length; j++) {
				String groupVal = macroGroups[j].getName();
				if (groupVal.equals(fieldVal)) {
					matchFound = true;
					break;
				}
			}
			if (!matchFound) {
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
		if (unique) {
			 array = Arrays.copyOf(array, array.length + 1);
			 array[array.length - 1] = element;
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
	
	private GuiLineObject[] appendMacroLines(GuiLineObject[] array, GuiLineObject element) {
		array = Arrays.copyOf(array, array.length + 1);
		array[array.length - 1] = element;
		return array;
	}
	
	private JButton generateDeleteBtn(GuiLineObject[] array) {
		int id = array.length-1;
		JButton thisBtn = new JButton(new AbstractAction("Delete") {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("delete " + id);
				macroLines = deleteLine(macroLines,id);
				fillGrid();
			}
		});
		
		return thisBtn;
	}
	
	private JButton generateDeleteBtn(GuiLineObject[] array, int id) {
		JButton thisBtn = new JButton(new AbstractAction("Delete") {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("delete " + id);
				macroLines = deleteLine(macroLines, id);
				fillGrid();
			}
		});
		
		return thisBtn;
	}
	
	private GuiLineObject[] deleteLine(GuiLineObject[] array,int ID) {
		//get array position
		int index = -1;
		for (int i = 0; i < array.length; i++) {
			if (array[i].getID() == ID) {
				index = i;
				break;
			}
		}
		GuiLineObject[] tempArr = new GuiLineObject[array.length-1];
		for (int i = 0; i < array.length; i++) {
			if (i<index) {
				tempArr[i] = array[i];
			}
			if (i>index) {
				tempArr[i-1] = array[i];
			}
		}
		return tempArr;
	}
	
	private int stringToHeight(String str) {
		String[] lines = str.split("\r\n|\r|\n");
		int lineCount = lines.length;
		int height = lineCount>1 ? lineCount*18+12 : 60;
		return height;
	}
	
	private boolean validateFields() {
		//validate keys: no space characters? onlyallowed characters
		String s = "";
		for (int i = 0; i < macroLines.length; i++) {
			String thisMacroError = "";
			thisMacroError += macroLines[i].validateKey();
			thisMacroError += macroLines[i].validateCode();
			thisMacroError += macroLines[i].validateCarBack();
			thisMacroError += macroLines[i].validateGroup();
			if (thisMacroError.length()>10) {
				s+=thisMacroError.replaceAll("\n", "\n#"+i+": ");
			}
		}
		System.out.println(s.replaceAll("\n", "T"));
		if (s.length()>10) {
			return guiErrors(s);
		}
		return true;
	}
	
	private boolean guiErrors(String error) {
		String ObjButtons[] = { "Ignore errors", "Go back" };
		JTextArea textArea = new JTextArea("Formatting errors found:\n"+error);
		JScrollPane scrollPane = new JScrollPane(textArea);  
		scrollPane.setMaximumSize(new Dimension( 500, 500 ));
		int PromptResult = JOptionPane.showOptionDialog(null, scrollPane,
				"Macro Editor Confirmation", JOptionPane.DEFAULT_OPTION, JOptionPane.WARNING_MESSAGE, null, ObjButtons,
				ObjButtons[1]);
		if (PromptResult == JOptionPane.YES_OPTION) {
			return true;
		}
		return false;
	}
}
