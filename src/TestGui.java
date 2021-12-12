package MacroFromJson;

import processing.app.contrib.ContributionListing;
import processing.app.ui.Editor;
//import processing.data.Table;

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

public class TestGui {
	
	private JPanel macroBodyJPanel = new JPanel();
	
	private int size;
	
	private static JTable table;

	public static void createFrame() {
		JFrame frame = new JFrame();
		frame.setSize(500, 500);
		JPanel macroPanel = new JPanel();
		SimpleTableDemo(macroPanel);
		frame.setLayout(new BorderLayout());
		// Close window button
		frame.add(new JButton(new AbstractAction("Print") {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println(table.getValueAt(1,1));
			}
		}), BorderLayout.LINE_END);
		frame.add(macroPanel, BorderLayout.CENTER);
		frame.setVisible(true);
		
		
	}
	
	
	public static void SimpleTableDemo(JPanel panel) {
		
		String[] columnNames = {"First Name",
			"Last Name",
			"Sport",
			"# of Years",
			"Vegetarian"};
			
		Object[][] data = {
			{"Kathy", "Smith",
			"Snowboarding", new Integer(5), new Boolean(false)},
			{"John", "Doe",
			"Rowing", new Integer(3), new Boolean(true)},
			{"Sue", "Black",
			"Knitting", new Integer(2), new Boolean(false)},
			{"Jane", "White",
			"Speed reading", new Integer(20), new Boolean(true)},
			{"Joe", "Brown",
			"Pool", new Integer(10), new Boolean(false)}
			};
			
		table = new JTable(data, columnNames);
		table.setPreferredScrollableViewportSize(new Dimension(500, 70));
		table.setFillsViewportHeight(true);
		//Create the scroll pane and add the table to it.
		JScrollPane scrollPane = new JScrollPane(table);
		
		//Add the scroll pane to this panel		
		panel.add(scrollPane);
			
	}
}
