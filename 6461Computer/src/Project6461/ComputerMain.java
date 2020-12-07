package Project6461;
import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.SpringLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JScrollPane;
import java.awt.Font;
import javax.swing.JToggleButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import javax.swing.JTextArea;

//RUN THIS FILE! THIS IS WHERE MAIN IS!

/*ComputerMain.java
 * This class handles the GUI. It contains all the variables and functions for the buttons, text fields, toggles, checkboxes, and labels.
 * It also contains functions for converting the input binary value checkboxes to a short for transfer over to the CPU class.
 * While it does contain a function for binary-to-hex conversion as part of an ease-of-use tool, 
 * there are no critical calculations or processing that happen in this class. 
 * Instead, everything is designed to hand values over to CPU.java, which does all of the actual computing.
 * A CPU object is created automatically when the simulator starts, with a reference to this UI object so the two can pass data back and forth.
 * 
 * Part 1 Written by Michael Ashery, reviewed by Jaeseock Choi and Daniel Brewer.
 * Part 2 GUI updates for keyboard and printer written by Michael Ashery, reviewed by Jaeseock Choi and Daniel Brewer.
 * */

public class ComputerMain extends JFrame {

	private static CPU cpu;
	private JPanel contentPane;
	private JLabel lblstopping;
	private JLabel printerlbl;
	private JLabel kblbl;
	//these text fields are where the value of each register is displayed.
	protected JTextField gpr0field;
	protected JTextField gpr1field;
	protected JTextField gpr2field;
	protected JTextField gpr3field;
	protected JTextField ixr1field;
	protected JTextField ixr2field;
	protected JTextField ixr3field;
	protected JTextField pcfield;
	protected JTextField mbrfield;
	protected JTextField irfield;
	protected JTextField marfield;
	protected JTextField ccfield;
	protected JTextField mfrfield;
	protected JTextField keyboard;
	//these variables are for the check boxes for user input
	private JCheckBox operation0;
	private JCheckBox operation1;
	private JCheckBox operation2;
	private JCheckBox operation3;
	private JCheckBox operation4;
	private JCheckBox operation5;
	private JCheckBox gpr0;
	private JCheckBox gpr1;
	private JCheckBox ixr0;
	private JCheckBox ixr1;
	private JCheckBox i0;
	private JCheckBox address0;
	private JCheckBox address1;
	private JCheckBox address2;
	private JCheckBox address3;
	private JCheckBox address4;
	//These are the toggle switches for the Run, keyboard Enter, and Execute Single Instruction buttons. Unlike the other buttons, this has an ON/OFF state
	protected JToggleButton runToggle; 
	protected JToggleButton tglExeSingleInstruction;
	protected JButton kbEnter;
	
	private JButton btnStore; //these buttons are declared down here because they were created after the Run Toggle.
	private JButton btnLoad;
	private JButton btnIPL;
	
	//text fields for visualizing the input check boxes as Hexidecimal or Binary.
	JButton btnVisualizeInput;
	protected JTextField visualizefield;
	protected JTextField hexField;
	
	//The console printer
	protected JTextArea printer;
	
	protected boolean inReady; //Used for the keyboard to determine CHK status.
	private JTextField enterHexFld;
	private JTextField outMemfld;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ComputerMain gui = new ComputerMain(); //the GUI is launched first. The Graphic User Interface is really just there to pull values from the CPU class and present them to the user, or allow the user to input values and send them to the CPU class.
					gui.setVisible(true);
					cpu = new CPU(gui); //once the GUI is launched it starts up a class for the CPU which runs in a separate thread. If you are looking for most of the actions discussed in the project rubric, they take place in that CPU class.
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public String getInput() { //converts the ticked checkboxes to a binary number String.
		String userInput = "";
		if (operation0.isSelected() == true) {userInput += "1";} //reads the checkboxes left to right.
		else {userInput += "0";}
		
		if (operation1.isSelected() == true) {userInput += "1";}
		else {userInput += "0";}

		if (operation2.isSelected() == true) {userInput += "1";}
		else {userInput += "0";}
		
		if (operation3.isSelected() == true) {userInput += "1";}
		else {userInput += "0";}
		
		if (operation4.isSelected() == true) {userInput += "1";}
		else {userInput += "0";}
		
		if (operation5.isSelected() == true) {userInput += "1";}
		else {userInput += "0";}
		
		if (gpr0.isSelected() == true) {userInput += "1";}
		else {userInput += "0";}
		
		if (gpr1.isSelected() == true) {userInput += "1";}
		else {userInput += "0";}
		
		if (ixr0.isSelected() == true) {userInput += "1";}
		else {userInput += "0";}
		
		if (ixr1.isSelected() == true) {userInput += "1";}
		else {userInput += "0";}
		
		if (i0.isSelected() == true) {userInput += "1";}
		else {userInput += "0";}
		
		if (address0.isSelected() == true) {userInput += "1";}
		else {userInput += "0";}
		
		if (address1.isSelected() == true) {userInput += "1";}
		else {userInput += "0";}
		
		if (address2.isSelected() == true) {userInput += "1";}
		else {userInput += "0";}
		
		if (address3.isSelected() == true) {userInput += "1";}
		else {userInput += "0";}
		
		if (address4.isSelected() == true) {userInput += "1";}
		else {userInput += "0";}
		return userInput;
	}
	
	protected short binaryStrToShort(String binary) { //converts the binary string created by getInput to a Short so it can be sent to the CPU for storage.
		int intermediate = Integer.parseInt(binary, 2);
		short result = (short) intermediate;
		return result;
	}

	/**
	 Everything below here is JFrame GUI components; buttons, labels, etc.
	 */
	public ComputerMain() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //this group of items sets up the GUI window.
		setBounds(100, 100, 1052, 559);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		SpringLayout sl_contentPane = new SpringLayout();
		contentPane.setLayout(sl_contentPane);
		
		/*
		 * NOTE TO READER
		 * JSwing automatically adds objects as they are created in the design panel to the bottom of this section of code.
		 * Each time it does, it uses the positions of previously-created objects to establish the position on the GUI of new objects. 
		 * If you try and move objects around to group them by object type, the code stops working, because the objects are out of order.
		 * Most objects should be self explanatory by looking at the object type and variable name. 
		 * Additional comments are written to help clarify blocks of objects where possible.
		 * For things like Clicks and other ActionEvents, the entire method is explained in detail the first time it is introduced. 
		 * It is not duplicated for subsequent methods with similar functions.
		 */
		
		//Text labels to go along with the GPR and IXR text boxes. The other stuff under each class declaration is JSwing calculating where to position the text box on the GUI.
		
		JLabel lblGpr_0 = new JLabel("GPR 0");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblGpr_0, 10, SpringLayout.NORTH, contentPane); //As mentioned before, these lines help JSwing position new objects by comparing their position to other objects.
		sl_contentPane.putConstraint(SpringLayout.WEST, lblGpr_0, 10, SpringLayout.WEST, contentPane);
		contentPane.add(lblGpr_0);
		
		JLabel lblGpr_1 = new JLabel("GPR 1");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblGpr_1, 20, SpringLayout.SOUTH, lblGpr_0);
		sl_contentPane.putConstraint(SpringLayout.EAST, lblGpr_1, 0, SpringLayout.EAST, lblGpr_0);
		contentPane.add(lblGpr_1);
		
		JLabel lblGpr_2 = new JLabel("GPR 2");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblGpr_2, 18, SpringLayout.SOUTH, lblGpr_1);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblGpr_2, 0, SpringLayout.WEST, lblGpr_0);
		contentPane.add(lblGpr_2);
		
		JLabel lblGpr_3 = new JLabel("GPR 3");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblGpr_3, 16, SpringLayout.SOUTH, lblGpr_2);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblGpr_3, 0, SpringLayout.WEST, lblGpr_0);
		contentPane.add(lblGpr_3);
		
		JLabel lblixr_1 = new JLabel("IXR 1");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblixr_1, 46, SpringLayout.SOUTH, lblGpr_3);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblixr_1, 0, SpringLayout.WEST, lblGpr_0);
		contentPane.add(lblixr_1);
		
		JLabel lblixr_2 = new JLabel("IXR 2");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblixr_2, 26, SpringLayout.SOUTH, lblixr_1);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblixr_2, 0, SpringLayout.WEST, lblGpr_0);
		contentPane.add(lblixr_2);
		
		JLabel lblixr_3 = new JLabel("IXR 3");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblixr_3, 25, SpringLayout.SOUTH, lblixr_2);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblixr_3, 0, SpringLayout.WEST, lblGpr_0);
		contentPane.add(lblixr_3);
		
		//Output text fields for the GPR and IXR. Text fields are accessed by other classes. The other stuff under each class declaration is JSwing calculating where to position the text box on the GUI.
		
		gpr0field = new JTextField();
		gpr0field.setEditable(false);
		sl_contentPane.putConstraint(SpringLayout.NORTH, gpr0field, 0, SpringLayout.NORTH, lblGpr_0);
		sl_contentPane.putConstraint(SpringLayout.WEST, gpr0field, 6, SpringLayout.EAST, lblGpr_0);
		sl_contentPane.putConstraint(SpringLayout.EAST, gpr0field, 206, SpringLayout.EAST, lblGpr_0);
		contentPane.add(gpr0field);
		gpr0field.setColumns(10);
		
		gpr1field = new JTextField();
		gpr1field.setEditable(false);
		sl_contentPane.putConstraint(SpringLayout.NORTH, gpr1field, -3, SpringLayout.NORTH, lblGpr_1);
		sl_contentPane.putConstraint(SpringLayout.WEST, gpr1field, 6, SpringLayout.EAST, lblGpr_1);
		sl_contentPane.putConstraint(SpringLayout.EAST, gpr1field, 206, SpringLayout.EAST, lblGpr_1);
		gpr1field.setColumns(10);
		contentPane.add(gpr1field);
		
		gpr2field = new JTextField();
		gpr2field.setEditable(false);
		sl_contentPane.putConstraint(SpringLayout.NORTH, gpr2field, 0, SpringLayout.NORTH, lblGpr_2);
		sl_contentPane.putConstraint(SpringLayout.WEST, gpr2field, -200, SpringLayout.EAST, gpr0field);
		sl_contentPane.putConstraint(SpringLayout.EAST, gpr2field, 0, SpringLayout.EAST, gpr0field);
		gpr2field.setColumns(10);
		contentPane.add(gpr2field);
		
		gpr3field = new JTextField();
		gpr3field.setEditable(false);
		sl_contentPane.putConstraint(SpringLayout.NORTH, gpr3field, 0, SpringLayout.NORTH, lblGpr_3);
		sl_contentPane.putConstraint(SpringLayout.WEST, gpr3field, 6, SpringLayout.EAST, lblGpr_3);
		sl_contentPane.putConstraint(SpringLayout.EAST, gpr3field, 0, SpringLayout.EAST, gpr0field);
		gpr3field.setColumns(10);
		contentPane.add(gpr3field);
		
		ixr1field = new JTextField();
		ixr1field.setEditable(false);
		sl_contentPane.putConstraint(SpringLayout.NORTH, ixr1field, 0, SpringLayout.NORTH, lblixr_1);
		sl_contentPane.putConstraint(SpringLayout.WEST, ixr1field, 0, SpringLayout.WEST, gpr0field);
		sl_contentPane.putConstraint(SpringLayout.EAST, ixr1field, 200, SpringLayout.WEST, gpr0field);
		ixr1field.setColumns(10);
		contentPane.add(ixr1field);
		
		ixr2field = new JTextField();
		ixr2field.setEditable(false);
		sl_contentPane.putConstraint(SpringLayout.NORTH, ixr2field, 0, SpringLayout.NORTH, lblixr_2);
		sl_contentPane.putConstraint(SpringLayout.WEST, ixr2field, 0, SpringLayout.WEST, gpr0field);
		sl_contentPane.putConstraint(SpringLayout.EAST, ixr2field, 209, SpringLayout.EAST, lblixr_2);
		ixr2field.setColumns(10);
		contentPane.add(ixr2field);
		
		ixr3field = new JTextField();
		ixr3field.setEditable(false);
		sl_contentPane.putConstraint(SpringLayout.NORTH, ixr3field, 0, SpringLayout.NORTH, lblixr_3);
		sl_contentPane.putConstraint(SpringLayout.WEST, ixr3field, -200, SpringLayout.EAST, gpr0field);
		sl_contentPane.putConstraint(SpringLayout.EAST, ixr3field, 0, SpringLayout.EAST, gpr0field);
		ixr3field.setColumns(10);
		contentPane.add(ixr3field);
		
		//load buttons
		
		JButton gpr0load = new JButton("Load");
		gpr0load.addActionListener(new ActionListener() { //all LOAD buttons function the same way
			public void actionPerformed(ActionEvent e) { //when a load button is pressed, three things will happen:
				String inputBinary = getInput(); //1) it will convert the input boxes to a binary String
				cpu.gpr0 = binaryStrToShort(inputBinary); //2) it will convert the string to a Short and pass along that value to the CPU class
				cpu.updateGUI(); //3) it will update the GUI
			} //all the other load buttons function the same way.
		});
		sl_contentPane.putConstraint(SpringLayout.NORTH, gpr0load, 0, SpringLayout.NORTH, lblGpr_0);
		sl_contentPane.putConstraint(SpringLayout.WEST, gpr0load, 6, SpringLayout.EAST, gpr0field);
		contentPane.add(gpr0load);
		
		JButton gpr1load = new JButton("Load");
		gpr1load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String inputBinary = getInput();
				cpu.gpr1 = binaryStrToShort(inputBinary);
				cpu.updateGUI();
			}
		});
		sl_contentPane.putConstraint(SpringLayout.NORTH, gpr1load, 0, SpringLayout.NORTH, gpr1field);
		sl_contentPane.putConstraint(SpringLayout.WEST, gpr1load, 6, SpringLayout.EAST, gpr1field);
		contentPane.add(gpr1load);
		
		JButton gpr2load = new JButton("Load");
		gpr2load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String inputBinary = getInput();
				cpu.gpr2 = binaryStrToShort(inputBinary);
				cpu.updateGUI();
			}
		});
		sl_contentPane.putConstraint(SpringLayout.NORTH, gpr2load, 0, SpringLayout.NORTH, lblGpr_2);
		sl_contentPane.putConstraint(SpringLayout.WEST, gpr2load, 6, SpringLayout.EAST, gpr2field);
		contentPane.add(gpr2load);
		
		JButton gpr3load = new JButton("Load");
		gpr3load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String inputBinary = getInput();
				cpu.gpr3 = binaryStrToShort(inputBinary);
				cpu.updateGUI();
			}
		});
		sl_contentPane.putConstraint(SpringLayout.NORTH, gpr3load, 0, SpringLayout.NORTH, lblGpr_3);
		sl_contentPane.putConstraint(SpringLayout.WEST, gpr3load, 6, SpringLayout.EAST, gpr3field);
		contentPane.add(gpr3load);
		
		JButton ixr1load = new JButton("Load");
		ixr1load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String inputBinary = getInput();
				cpu.ixr1 = binaryStrToShort(inputBinary);
				cpu.updateGUI();
			}
		});
		sl_contentPane.putConstraint(SpringLayout.NORTH, ixr1load, 0, SpringLayout.NORTH, lblixr_1);
		sl_contentPane.putConstraint(SpringLayout.WEST, ixr1load, 6, SpringLayout.EAST, ixr1field);
		contentPane.add(ixr1load);
		
		JButton ixr2load = new JButton("Load");
		ixr2load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String inputBinary = getInput();
				cpu.ixr2 = binaryStrToShort(inputBinary);
				cpu.updateGUI();
			}
		});
		sl_contentPane.putConstraint(SpringLayout.NORTH, ixr2load, 0, SpringLayout.NORTH, lblixr_2);
		sl_contentPane.putConstraint(SpringLayout.WEST, ixr2load, 6, SpringLayout.EAST, ixr2field);
		contentPane.add(ixr2load);
		
		JButton ixr3load = new JButton("Load");
		ixr3load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String inputBinary = getInput();
				cpu.ixr3 = binaryStrToShort(inputBinary);
				cpu.updateGUI();
			}
		});
		sl_contentPane.putConstraint(SpringLayout.NORTH, ixr3load, 0, SpringLayout.NORTH, lblixr_3);
		sl_contentPane.putConstraint(SpringLayout.WEST, ixr3load, 6, SpringLayout.EAST, ixr3field);
		contentPane.add(ixr3load);
		
		//text labels for PC, MAR, MBR, IR
		
		JLabel lblPC = new JLabel("PC");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblPC, 0, SpringLayout.NORTH, lblGpr_0);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblPC, 90, SpringLayout.EAST, gpr0load);
		contentPane.add(lblPC);
		
		JLabel lblMAR = new JLabel("MAR");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblMAR, 0, SpringLayout.NORTH, lblGpr_1);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblMAR, 0, SpringLayout.WEST, lblPC);
		contentPane.add(lblMAR);
		
		JLabel lblMBR = new JLabel("MBR");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblMBR, 0, SpringLayout.NORTH, lblGpr_2);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblMBR, 0, SpringLayout.WEST, lblPC);
		contentPane.add(lblMBR);
		
		JLabel lblIR = new JLabel("IR");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblIR, 0, SpringLayout.NORTH, lblGpr_3);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblIR, 0, SpringLayout.WEST, lblPC);
		contentPane.add(lblIR);
		
		//output text fields for the PC, MBR, IR, MAR
		
		pcfield = new JTextField();
		pcfield.setEditable(false);
		sl_contentPane.putConstraint(SpringLayout.NORTH, pcfield, 0, SpringLayout.NORTH, lblGpr_0);
		sl_contentPane.putConstraint(SpringLayout.WEST, pcfield, 17, SpringLayout.EAST, lblPC);
		sl_contentPane.putConstraint(SpringLayout.EAST, pcfield, 217, SpringLayout.EAST, lblPC);
		pcfield.setColumns(10);
		contentPane.add(pcfield);
		
		mbrfield = new JTextField();
		mbrfield.setEditable(false);
		sl_contentPane.putConstraint(SpringLayout.NORTH, mbrfield, 0, SpringLayout.NORTH, lblGpr_2);
		sl_contentPane.putConstraint(SpringLayout.WEST, mbrfield, 0, SpringLayout.WEST, pcfield);
		sl_contentPane.putConstraint(SpringLayout.EAST, mbrfield, 200, SpringLayout.WEST, pcfield);
		mbrfield.setColumns(10);
		contentPane.add(mbrfield);
		
		irfield = new JTextField();
		irfield.setEditable(false);
		sl_contentPane.putConstraint(SpringLayout.NORTH, irfield, 0, SpringLayout.NORTH, lblGpr_3);
		sl_contentPane.putConstraint(SpringLayout.WEST, irfield, 0, SpringLayout.WEST, pcfield);
		sl_contentPane.putConstraint(SpringLayout.EAST, irfield, 0, SpringLayout.EAST, pcfield);
		irfield.setColumns(10);
		contentPane.add(irfield);
		
		marfield = new JTextField();
		marfield.setEditable(false);
		sl_contentPane.putConstraint(SpringLayout.NORTH, marfield, 0, SpringLayout.NORTH, lblGpr_1);
		sl_contentPane.putConstraint(SpringLayout.WEST, marfield, 0, SpringLayout.WEST, pcfield);
		sl_contentPane.putConstraint(SpringLayout.EAST, marfield, 0, SpringLayout.EAST, pcfield);
		marfield.setColumns(10);
		contentPane.add(marfield);
		
		//These buttons load the content from the Input to the PC, MAR, and MBR. Function the same way as the buttons above.
		
		JButton pcload = new JButton("Load");
		pcload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String inputBinary = getInput();
				cpu.pc = binaryStrToShort(inputBinary);
				cpu.updateGUI();
			}
		});
		sl_contentPane.putConstraint(SpringLayout.NORTH, pcload, 0, SpringLayout.NORTH, lblGpr_0);
		sl_contentPane.putConstraint(SpringLayout.WEST, pcload, 6, SpringLayout.EAST, pcfield);
		contentPane.add(pcload);
		
		JButton marload = new JButton("Load");
		marload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String inputBinary = getInput();
				cpu.mar = binaryStrToShort(inputBinary);
				cpu.updateGUI();
			}
		});
		sl_contentPane.putConstraint(SpringLayout.NORTH, marload, 0, SpringLayout.NORTH, lblGpr_1);
		sl_contentPane.putConstraint(SpringLayout.WEST, marload, 6, SpringLayout.EAST, marfield);
		contentPane.add(marload);
		
		JButton mbrload = new JButton("Load");
		mbrload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String inputBinary = getInput();
				cpu.mbr = binaryStrToShort(inputBinary);
				cpu.updateGUI();
			}
		});
		sl_contentPane.putConstraint(SpringLayout.NORTH, mbrload, 0, SpringLayout.NORTH, lblGpr_2);
		sl_contentPane.putConstraint(SpringLayout.WEST, mbrload, 6, SpringLayout.EAST, mbrfield);
		contentPane.add(mbrload);
		
		//output text fields for Condition Code and MFR
		
		ccfield = new JTextField();
		ccfield.setEditable(false);
		sl_contentPane.putConstraint(SpringLayout.NORTH, ccfield, -3, SpringLayout.NORTH, lblixr_2);
		ccfield.setColumns(10);
		contentPane.add(ccfield);
		
		mfrfield = new JTextField();
		mfrfield.setEditable(false);
		sl_contentPane.putConstraint(SpringLayout.WEST, ccfield, 0, SpringLayout.WEST, mfrfield);
		sl_contentPane.putConstraint(SpringLayout.NORTH, mfrfield, 0, SpringLayout.NORTH, lblixr_1);
		sl_contentPane.putConstraint(SpringLayout.EAST, mfrfield, 0, SpringLayout.EAST, pcfield);
		mfrfield.setColumns(10);
		contentPane.add(mfrfield);
		
		//These JLabels are more text labels
		
		JLabel lblMFR = new JLabel("MFR");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblMFR, 0, SpringLayout.NORTH, lblixr_1);
		sl_contentPane.putConstraint(SpringLayout.EAST, lblMFR, -17, SpringLayout.WEST, mfrfield);
		contentPane.add(lblMFR);
		
		JLabel lblCC = new JLabel("Condition Code");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblCC, 0, SpringLayout.NORTH, lblixr_2);
		sl_contentPane.putConstraint(SpringLayout.EAST, lblCC, 0, SpringLayout.EAST, lblMFR);
		contentPane.add(lblCC);
		
		//The JCheckBox objects below here are all the check boxes used for User Input. Their position corresponds to the variable name.
		
		operation0 = new JCheckBox("");
		sl_contentPane.putConstraint(SpringLayout.NORTH, operation0, 65, SpringLayout.SOUTH, ixr3field);
		sl_contentPane.putConstraint(SpringLayout.WEST, operation0, 0, SpringLayout.WEST, gpr0field);
		contentPane.add(operation0);
		
		operation1 = new JCheckBox("");
		sl_contentPane.putConstraint(SpringLayout.NORTH, operation1, 0, SpringLayout.NORTH, operation0);
		sl_contentPane.putConstraint(SpringLayout.WEST, operation1, 6, SpringLayout.EAST, operation0);
		contentPane.add(operation1);
		
		operation2 = new JCheckBox("");
		sl_contentPane.putConstraint(SpringLayout.NORTH, operation2, 0, SpringLayout.NORTH, operation0);
		sl_contentPane.putConstraint(SpringLayout.WEST, operation2, 6, SpringLayout.EAST, operation1);
		contentPane.add(operation2);
		
		operation3 = new JCheckBox("");
		sl_contentPane.putConstraint(SpringLayout.NORTH, operation3, 0, SpringLayout.NORTH, operation0);
		sl_contentPane.putConstraint(SpringLayout.WEST, operation3, 6, SpringLayout.EAST, operation2);
		contentPane.add(operation3);
		
		operation4 = new JCheckBox("");
		sl_contentPane.putConstraint(SpringLayout.NORTH, operation4, 0, SpringLayout.NORTH, operation0);
		sl_contentPane.putConstraint(SpringLayout.WEST, operation4, 6, SpringLayout.EAST, operation3);
		contentPane.add(operation4);
		
		operation5 = new JCheckBox("");
		sl_contentPane.putConstraint(SpringLayout.NORTH, operation5, 0, SpringLayout.NORTH, operation0);
		sl_contentPane.putConstraint(SpringLayout.WEST, operation5, 6, SpringLayout.EAST, operation4);
		contentPane.add(operation5);
		
		gpr0 = new JCheckBox("");
		sl_contentPane.putConstraint(SpringLayout.NORTH, gpr0, 0, SpringLayout.NORTH, operation0);
		sl_contentPane.putConstraint(SpringLayout.WEST, gpr0, 31, SpringLayout.EAST, operation5);
		contentPane.add(gpr0);
		
		gpr1 = new JCheckBox("");
		sl_contentPane.putConstraint(SpringLayout.NORTH, gpr1, 0, SpringLayout.NORTH, operation0);
		sl_contentPane.putConstraint(SpringLayout.WEST, gpr1, 6, SpringLayout.EAST, gpr0);
		contentPane.add(gpr1);
		
		ixr0 = new JCheckBox("");
		sl_contentPane.putConstraint(SpringLayout.NORTH, ixr0, 0, SpringLayout.NORTH, operation0);
		sl_contentPane.putConstraint(SpringLayout.WEST, ixr0, 27, SpringLayout.EAST, gpr1);
		contentPane.add(ixr0);
		
		ixr1 = new JCheckBox("");
		sl_contentPane.putConstraint(SpringLayout.NORTH, ixr1, 0, SpringLayout.NORTH, operation0);
		sl_contentPane.putConstraint(SpringLayout.WEST, ixr1, 6, SpringLayout.EAST, ixr0);
		contentPane.add(ixr1);
		
		i0 = new JCheckBox("");
		sl_contentPane.putConstraint(SpringLayout.NORTH, i0, 0, SpringLayout.NORTH, operation0);
		sl_contentPane.putConstraint(SpringLayout.EAST, i0, 0, SpringLayout.EAST, lblPC);
		contentPane.add(i0);
		
		address0 = new JCheckBox("");
		sl_contentPane.putConstraint(SpringLayout.SOUTH, address0, 0, SpringLayout.SOUTH, operation0);
		contentPane.add(address0);
		
		address1 = new JCheckBox("");
		sl_contentPane.putConstraint(SpringLayout.EAST, address0, -6, SpringLayout.WEST, address1);
		sl_contentPane.putConstraint(SpringLayout.NORTH, address1, 0, SpringLayout.NORTH, operation0);
		contentPane.add(address1);
		
		address2 = new JCheckBox("");
		sl_contentPane.putConstraint(SpringLayout.EAST, address1, -6, SpringLayout.WEST, address2);
		sl_contentPane.putConstraint(SpringLayout.NORTH, address2, 0, SpringLayout.NORTH, operation0);
		contentPane.add(address2);
		
		address3 = new JCheckBox("");
		sl_contentPane.putConstraint(SpringLayout.EAST, address2, -6, SpringLayout.WEST, address3);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, address3, 0, SpringLayout.SOUTH, operation0);
		contentPane.add(address3);
		
		address4 = new JCheckBox("");
		sl_contentPane.putConstraint(SpringLayout.WEST, address4, 578, SpringLayout.WEST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, address3, -6, SpringLayout.WEST, address4);
		sl_contentPane.putConstraint(SpringLayout.NORTH, address4, 0, SpringLayout.NORTH, operation0);
		contentPane.add(address4);
		
		//JLabels below here are all text labels, and show the text stated in the argument.
		
		JLabel lblOperation = new JLabel("Operation");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblOperation, 17, SpringLayout.SOUTH, operation2);
		sl_contentPane.putConstraint(SpringLayout.EAST, lblOperation, 0, SpringLayout.EAST, operation3);
		contentPane.add(lblOperation);
		
		JLabel lblGPR = new JLabel("GPR");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblGPR, 0, SpringLayout.NORTH, lblOperation);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblGPR, 96, SpringLayout.EAST, lblOperation);
		contentPane.add(lblGPR);
		
		JLabel lblIxr = new JLabel("IXR");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblIxr, 0, SpringLayout.NORTH, lblOperation);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblIxr, 56, SpringLayout.EAST, lblGPR);
		contentPane.add(lblIxr);
		
		JLabel lblI = new JLabel("I");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblI, 0, SpringLayout.NORTH, lblOperation);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblI, 0, SpringLayout.WEST, lblPC);
		sl_contentPane.putConstraint(SpringLayout.EAST, lblI, 64, SpringLayout.EAST, lblIxr);
		contentPane.add(lblI);
		
		JLabel lblAddress = new JLabel("Address");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblAddress, 0, SpringLayout.NORTH, lblOperation);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblAddress, 107, SpringLayout.EAST, lblI);
		contentPane.add(lblAddress);
		
		JLabel lblInstructions = new JLabel("Input below. Tick for 1, untick for 0."); //an instruction label for the inputs
		sl_contentPane.putConstraint(SpringLayout.WEST, lblInstructions, 0, SpringLayout.WEST, operation5);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, lblInstructions, -17, SpringLayout.NORTH, operation5);
		lblInstructions.setFont(new Font("Tahoma", Font.PLAIN, 16));
		contentPane.add(lblInstructions);
		
		runToggle = new JToggleButton("RUN"); //RUN toggle button
		sl_contentPane.putConstraint(SpringLayout.EAST, runToggle, 112, SpringLayout.WEST, pcload);
		runToggle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(tglExeSingleInstruction.isSelected()) { //if the Execute Single Instruction toggle is activated, the Execute Single Instruction method is run instead.
					cpu.executeSingleInstruction();
				}
				else { //otherwise, we run the whole fetch/decode/execute cycle in automatic mode.
					if(runToggle.isSelected()) { //if RUN is clicked on to start the loop...
						cpu.setRunning(true); //sets the instruction cycle to loop
						lblstopping.setText("RUNNING"); //sets the state label in the bottom right corner to RUNNING
						gpr0load.setEnabled(false); //all of the other buttons are disabled to prevent things from being changed in the middle of the cycle.
						gpr1load.setEnabled(false);
						gpr2load.setEnabled(false);
						gpr3load.setEnabled(false);
						ixr1load.setEnabled(false);
						ixr2load.setEnabled(false);
						ixr3load.setEnabled(false);
						pcload.setEnabled(false);
						marload.setEnabled(false);
						mbrload.setEnabled(false);
						btnStore.setEnabled(false);
						btnLoad.setEnabled(false);
						btnIPL.setEnabled(false);
						tglExeSingleInstruction.setEnabled(false);
						btnVisualizeInput.setEnabled(false);
						runToggle.setText("STOP"); //The Run button label is changed to STOP for user clarity.
						final class CpuRunnable implements Runnable{ //runinstructionCyle MUST be run as a separate thread, otherwise GUI will lock up in Automatic mode.
							private CPU myCpu;
							public CpuRunnable(CPU cpu) {
								myCpu = cpu;
							}
							@Override
							public void run() {
								myCpu.runInstructionCycle();
							}
						}
						Runnable r = new CpuRunnable(cpu);
						Thread t = new Thread(r);
						t.start();
					}
					if(!runToggle.isSelected()) { //if RUN (now labled as 'stop') is clicked again to stop the loop...				
						cpu.setRunning(false); //breaks the instruction cycle method after the current cycle completes.
						lblstopping.setText("WAITING - Automatic"); //sets the state label in the bottom right corner to WAITING
						gpr0load.setEnabled(true); //other buttons are reenabled.
						gpr1load.setEnabled(true);
						gpr2load.setEnabled(true);
						gpr3load.setEnabled(true);
						ixr1load.setEnabled(true);
						ixr2load.setEnabled(true);
						ixr3load.setEnabled(true);
						pcload.setEnabled(true);
						marload.setEnabled(true);
						mbrload.setEnabled(true);
						btnStore.setEnabled(true);
						btnLoad.setEnabled(true);
						btnIPL.setEnabled(true);
						tglExeSingleInstruction.setEnabled(true);
						btnVisualizeInput.setEnabled(true);
						runToggle.setText("RUN"); //RUN button label changed back to "RUN"
					}
				}
			}
		});
		sl_contentPane.putConstraint(SpringLayout.WEST, runToggle, 0, SpringLayout.WEST, pcload);
		runToggle.setFont(new Font("Tahoma", Font.BOLD, 24));
		contentPane.add(runToggle);
		
		btnStore = new JButton("STORE MBR at MAR address"); //Store MBR contents at MAR address in memory
		btnStore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cpu.storeMBRtoMemAtMAR(); //Runs the StoreMBRtoMemAtMAR method in the CPU class, which does exactly as the name implies.
			}
		});
		sl_contentPane.putConstraint(SpringLayout.WEST, btnStore, 0, SpringLayout.WEST, pcload);
		contentPane.add(btnStore);
		
		btnLoad = new JButton("LOAD MAR address contents to MBR"); //Load MAR address contents to MBR
		btnLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { //when clicked
				cpu.storeMemAtMARtoMBR(); //Runs the StoreMemAtMARtoMBR method, which does exactly as the name implies.
			}
		});
		sl_contentPane.putConstraint(SpringLayout.NORTH, btnLoad, 6, SpringLayout.SOUTH, btnStore);
		sl_contentPane.putConstraint(SpringLayout.WEST, btnLoad, 0, SpringLayout.WEST, pcload);
		contentPane.add(btnLoad);
		
		btnIPL = new JButton("Initial Program Load"); //Initial Program Load button
		sl_contentPane.putConstraint(SpringLayout.NORTH, runToggle, 30, SpringLayout.SOUTH, btnIPL);
		btnIPL.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { //when clicked
				cpu.iPL(); //performs the IPL method from the CPU class, which loads the text file to memory.
			}
		});
		sl_contentPane.putConstraint(SpringLayout.SOUTH, btnIPL, -109, SpringLayout.SOUTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, btnStore, -36, SpringLayout.NORTH, btnIPL);
		sl_contentPane.putConstraint(SpringLayout.WEST, btnIPL, 0, SpringLayout.WEST, pcload);
		contentPane.add(btnIPL);
		
		tglExeSingleInstruction = new JToggleButton("Execute Single Instruction");
		tglExeSingleInstruction.setHorizontalAlignment(SwingConstants.LEFT);
		sl_contentPane.putConstraint(SpringLayout.WEST, tglExeSingleInstruction, 0, SpringLayout.WEST, pcload);
		sl_contentPane.putConstraint(SpringLayout.EAST, tglExeSingleInstruction, 186, SpringLayout.WEST, pcload);
		tglExeSingleInstruction.addActionListener(new ActionListener() {
			public synchronized void actionPerformed(ActionEvent e) {
				if(tglExeSingleInstruction.isSelected()) {
					lblstopping.setText("WAITING - Single Instruction");
					cpu.automatic = false;
				}
				if(!tglExeSingleInstruction.isSelected()) {
					lblstopping.setText("WAITING - Automatic");
					cpu.automatic = true;
				}
			}
		});
		sl_contentPane.putConstraint(SpringLayout.NORTH, tglExeSingleInstruction, 6, SpringLayout.SOUTH, btnIPL);
		contentPane.add(tglExeSingleInstruction);
		
		visualizefield = new JTextField(); //text field to the left of the visualize input button
		visualizefield.setEditable(false);
		visualizefield.setText("Preview input here");
		sl_contentPane.putConstraint(SpringLayout.NORTH, visualizefield, 22, SpringLayout.SOUTH, lblGPR);
		sl_contentPane.putConstraint(SpringLayout.WEST, visualizefield, -104, SpringLayout.WEST, operation4);
		visualizefield.setColumns(10);
		contentPane.add(visualizefield);
		
		btnVisualizeInput = new JButton("Visualize Input as Binary/Hex"); //visualize input button
		btnVisualizeInput.addActionListener(new ActionListener() { //when the button is pressed, it translates the checkboxes to binary and prints it out in the visualizefield
			public void actionPerformed(ActionEvent e) {
				String inputBinary = getInput();
				visualizefield.setText(inputBinary);
				String dispHex = "";
				for (int i = 0; i < 16; i+=4) {
					//System.out.println(inputBinary.substring(i, i+4));
					int digit = Integer.parseInt(inputBinary.substring(i, i+4), 2);
					//System.out.println(digit);
					dispHex += Integer.toHexString(digit);
				}
				hexField.setText(dispHex);
				//System.out.println(binaryStrToShort(inputBinary)); //for debugging
			}
		});
		sl_contentPane.putConstraint(SpringLayout.EAST, visualizefield, -6, SpringLayout.WEST, btnVisualizeInput);
		sl_contentPane.putConstraint(SpringLayout.NORTH, btnVisualizeInput, 21, SpringLayout.SOUTH, lblIxr);
		sl_contentPane.putConstraint(SpringLayout.EAST, btnVisualizeInput, 0, SpringLayout.EAST, lblIR);
		contentPane.add(btnVisualizeInput);
		
		lblstopping = new JLabel("WAITING - Automatic");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblstopping, 30, SpringLayout.SOUTH, btnIPL);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblstopping, -39, SpringLayout.EAST, btnStore);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, lblstopping, -37, SpringLayout.SOUTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, lblstopping, 92, SpringLayout.EAST, btnLoad);
		contentPane.add(lblstopping);
		
		hexField = new JTextField();
		hexField.setEditable(false); //user is not allowed to edit this field
		sl_contentPane.putConstraint(SpringLayout.NORTH, hexField, 0, SpringLayout.NORTH, visualizefield);
		sl_contentPane.putConstraint(SpringLayout.WEST, hexField, 6, SpringLayout.EAST, btnVisualizeInput);
		contentPane.add(hexField);
		hexField.setColumns(10);
		
		keyboard = new JTextField();
		sl_contentPane.putConstraint(SpringLayout.EAST, keyboard, -43, SpringLayout.EAST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.NORTH, keyboard, 0, SpringLayout.NORTH, lblGpr_0);
		keyboard.setColumns(10);
		contentPane.add(keyboard);
		
		printerlbl = new JLabel("Printer/Output");
		sl_contentPane.putConstraint(SpringLayout.NORTH, printerlbl, 0, SpringLayout.NORTH, lblGpr_2);
		sl_contentPane.putConstraint(SpringLayout.EAST, printerlbl, 0, SpringLayout.EAST, btnStore);
		contentPane.add(printerlbl);
		
		kblbl = new JLabel("Keyboard/Input");
		sl_contentPane.putConstraint(SpringLayout.EAST, kblbl, -219, SpringLayout.EAST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, keyboard, 7, SpringLayout.EAST, kblbl);
		sl_contentPane.putConstraint(SpringLayout.NORTH, kblbl, 4, SpringLayout.NORTH, gpr0load);
		contentPane.add(kblbl);
		
		kbEnter = new JButton("ENTER"); //for sending keyboard strings to the inputBuffer
		sl_contentPane.putConstraint(SpringLayout.NORTH, kbEnter, -4, SpringLayout.NORTH, lblGpr_1);
		sl_contentPane.putConstraint(SpringLayout.EAST, kbEnter, -43, SpringLayout.EAST, contentPane);
		kbEnter.addActionListener(new ActionListener() {
			public synchronized void actionPerformed(ActionEvent e) {
				if (keyboard.getText() == "") {;} //If the keyboard is empty when
				else if(cpu.inputBuffer.peekFirst() != null) {;} //Prevents new strings from being entered while the input buffer is still being processed.
				else {
				for (int i = 0; i < keyboard.getText().length(); i++) {
					cpu.inputBuffer.add(keyboard.getText().charAt(i));
				}
				inReady = true;
				//if you wanted to add a separator character or wanted to denote End-of-Line, it would happen here. Not required or implemented, just making a note.
				keyboard.setText("");} //clears the keyboard after the value is entered
			}
		});
		contentPane.add(kbEnter);
		
		printer = new JTextArea(); //console printer
		printer.setEditable(false); //user is not allowed to edit this field
		JScrollPane scrolling = new JScrollPane(printer);
		
		sl_contentPane.putConstraint(SpringLayout.NORTH, scrolling, 16, SpringLayout.SOUTH, printerlbl);
		sl_contentPane.putConstraint(SpringLayout.WEST, scrolling, 100, SpringLayout.EAST, ccfield);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, scrolling, 3, SpringLayout.SOUTH, ixr2field);
		sl_contentPane.putConstraint(SpringLayout.EAST, scrolling, -5, SpringLayout.EAST, contentPane);
		contentPane.add(scrolling);
		
		JButton btnNewButton = new JButton("MemAtHexAddr");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index = Integer.parseInt(enterHexFld.getText(),16);
				outMemfld.setText(Integer.toHexString(cpu.memory[index]));
			}
		});
		sl_contentPane.putConstraint(SpringLayout.NORTH, btnNewButton, 13, SpringLayout.NORTH, runToggle);
		sl_contentPane.putConstraint(SpringLayout.WEST, btnNewButton, 0, SpringLayout.WEST, gpr0);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, btnNewButton, -43, SpringLayout.SOUTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, btnNewButton, -35, SpringLayout.EAST, lblPC);
		contentPane.add(btnNewButton);
		
		enterHexFld = new JTextField();
		sl_contentPane.putConstraint(SpringLayout.NORTH, enterHexFld, 0, SpringLayout.NORTH, btnNewButton);
		sl_contentPane.putConstraint(SpringLayout.WEST, enterHexFld, -142, SpringLayout.EAST, visualizefield);
		sl_contentPane.putConstraint(SpringLayout.EAST, enterHexFld, 0, SpringLayout.EAST, visualizefield);
		enterHexFld.setText("Input Hex here");
		enterHexFld.setColumns(10);
		contentPane.add(enterHexFld);
		
		outMemfld = new JTextField();
		sl_contentPane.putConstraint(SpringLayout.NORTH, outMemfld, 0, SpringLayout.NORTH, btnNewButton);
		sl_contentPane.putConstraint(SpringLayout.WEST, outMemfld, 6, SpringLayout.EAST, btnNewButton);
		contentPane.add(outMemfld);
		outMemfld.setEditable(false);
		outMemfld.setColumns(10);
		
		JButton scannerBtn = new JButton("Scan In File"); //this button engages the file scanner
		sl_contentPane.putConstraint(SpringLayout.NORTH, scannerBtn, -4, SpringLayout.NORTH, lblGpr_1);
		sl_contentPane.putConstraint(SpringLayout.WEST, scannerBtn, 0, SpringLayout.WEST, printerlbl);
		scannerBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(cpu.inputBuffer.peekFirst() != null) {;} //Prevents new strings from being entered while the input buffer is still being processed.
				else {
					cpu.scanner();
				}
				inReady = false;
			}
		});
		contentPane.add(scannerBtn);
	}
}
