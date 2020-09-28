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
import java.awt.Font;
import javax.swing.JToggleButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ComputerMain extends JFrame {

	private static CPU cpu;
	private JPanel contentPane;
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
	
	public String getInput() { //converts the ticked checkboxes to a binary number string.
		String userInput = "";
		if (operation0.isSelected() == true) {userInput += "1";}
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
		
		//System.out.println(userInput); //for debugging purposes
		return userInput;
	}

	/**
	 Everything below here is JFrame GUI components; buttons, labels, etc.
	 */
	public ComputerMain() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 950, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		SpringLayout sl_contentPane = new SpringLayout();
		contentPane.setLayout(sl_contentPane);
		
		JLabel lblGpr_0 = new JLabel("GPR 0");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblGpr_0, 10, SpringLayout.NORTH, contentPane);
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
		
		gpr0field = new JTextField();
		sl_contentPane.putConstraint(SpringLayout.NORTH, gpr0field, 0, SpringLayout.NORTH, lblGpr_0);
		sl_contentPane.putConstraint(SpringLayout.WEST, gpr0field, 6, SpringLayout.EAST, lblGpr_0);
		sl_contentPane.putConstraint(SpringLayout.EAST, gpr0field, 206, SpringLayout.EAST, lblGpr_0);
		contentPane.add(gpr0field);
		gpr0field.setColumns(10);
		
		gpr1field = new JTextField();
		sl_contentPane.putConstraint(SpringLayout.NORTH, gpr1field, -3, SpringLayout.NORTH, lblGpr_1);
		sl_contentPane.putConstraint(SpringLayout.WEST, gpr1field, 6, SpringLayout.EAST, lblGpr_1);
		sl_contentPane.putConstraint(SpringLayout.EAST, gpr1field, 206, SpringLayout.EAST, lblGpr_1);
		gpr1field.setColumns(10);
		contentPane.add(gpr1field);
		
		gpr2field = new JTextField();
		sl_contentPane.putConstraint(SpringLayout.NORTH, gpr2field, 0, SpringLayout.NORTH, lblGpr_2);
		sl_contentPane.putConstraint(SpringLayout.WEST, gpr2field, -200, SpringLayout.EAST, gpr0field);
		sl_contentPane.putConstraint(SpringLayout.EAST, gpr2field, 0, SpringLayout.EAST, gpr0field);
		gpr2field.setColumns(10);
		contentPane.add(gpr2field);
		
		gpr3field = new JTextField();
		sl_contentPane.putConstraint(SpringLayout.NORTH, gpr3field, 0, SpringLayout.NORTH, lblGpr_3);
		sl_contentPane.putConstraint(SpringLayout.WEST, gpr3field, 6, SpringLayout.EAST, lblGpr_3);
		sl_contentPane.putConstraint(SpringLayout.EAST, gpr3field, 0, SpringLayout.EAST, gpr0field);
		gpr3field.setColumns(10);
		contentPane.add(gpr3field);
		
		ixr1field = new JTextField();
		sl_contentPane.putConstraint(SpringLayout.NORTH, ixr1field, 0, SpringLayout.NORTH, lblixr_1);
		sl_contentPane.putConstraint(SpringLayout.WEST, ixr1field, 0, SpringLayout.WEST, gpr0field);
		sl_contentPane.putConstraint(SpringLayout.EAST, ixr1field, 200, SpringLayout.WEST, gpr0field);
		ixr1field.setColumns(10);
		contentPane.add(ixr1field);
		
		ixr2field = new JTextField();
		sl_contentPane.putConstraint(SpringLayout.NORTH, ixr2field, 0, SpringLayout.NORTH, lblixr_2);
		sl_contentPane.putConstraint(SpringLayout.WEST, ixr2field, 0, SpringLayout.WEST, gpr0field);
		sl_contentPane.putConstraint(SpringLayout.EAST, ixr2field, 209, SpringLayout.EAST, lblixr_2);
		ixr2field.setColumns(10);
		contentPane.add(ixr2field);
		
		ixr3field = new JTextField();
		sl_contentPane.putConstraint(SpringLayout.NORTH, ixr3field, 0, SpringLayout.NORTH, lblixr_3);
		sl_contentPane.putConstraint(SpringLayout.WEST, ixr3field, -200, SpringLayout.EAST, gpr0field);
		sl_contentPane.putConstraint(SpringLayout.EAST, ixr3field, 0, SpringLayout.EAST, gpr0field);
		ixr3field.setColumns(10);
		contentPane.add(ixr3field);
		
		JButton gpr0load = new JButton("Load");
		gpr0load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String inputBinary = getInput();
				gpr0field.setText(inputBinary);
				//cpu.gpr0 = inputBinary;
			}
		});
		sl_contentPane.putConstraint(SpringLayout.NORTH, gpr0load, 0, SpringLayout.NORTH, lblGpr_0);
		sl_contentPane.putConstraint(SpringLayout.WEST, gpr0load, 6, SpringLayout.EAST, gpr0field);
		contentPane.add(gpr0load);
		
		JButton gpr1load = new JButton("Load");
		gpr1load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String inputBinary = getInput();
				gpr1field.setText(inputBinary);
			}
		});
		sl_contentPane.putConstraint(SpringLayout.NORTH, gpr1load, 0, SpringLayout.NORTH, gpr1field);
		sl_contentPane.putConstraint(SpringLayout.WEST, gpr1load, 6, SpringLayout.EAST, gpr1field);
		contentPane.add(gpr1load);
		
		JButton gpr2load = new JButton("Load");
		gpr2load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String inputBinary = getInput();
				gpr2field.setText(inputBinary);
			}
		});
		sl_contentPane.putConstraint(SpringLayout.NORTH, gpr2load, 0, SpringLayout.NORTH, lblGpr_2);
		sl_contentPane.putConstraint(SpringLayout.WEST, gpr2load, 6, SpringLayout.EAST, gpr2field);
		contentPane.add(gpr2load);
		
		JButton gpr3load = new JButton("Load");
		gpr3load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String inputBinary = getInput();
				gpr3field.setText(inputBinary);
			}
		});
		sl_contentPane.putConstraint(SpringLayout.NORTH, gpr3load, 0, SpringLayout.NORTH, lblGpr_3);
		sl_contentPane.putConstraint(SpringLayout.WEST, gpr3load, 6, SpringLayout.EAST, gpr3field);
		contentPane.add(gpr3load);
		
		JButton ixr1load = new JButton("Load");
		ixr1load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String inputBinary = getInput();
				ixr1field.setText(inputBinary);
			}
		});
		sl_contentPane.putConstraint(SpringLayout.NORTH, ixr1load, 0, SpringLayout.NORTH, lblixr_1);
		sl_contentPane.putConstraint(SpringLayout.WEST, ixr1load, 6, SpringLayout.EAST, ixr1field);
		contentPane.add(ixr1load);
		
		JButton ixr2load = new JButton("Load");
		ixr2load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String inputBinary = getInput();
				ixr2field.setText(inputBinary);
			}
		});
		sl_contentPane.putConstraint(SpringLayout.NORTH, ixr2load, 0, SpringLayout.NORTH, lblixr_2);
		sl_contentPane.putConstraint(SpringLayout.WEST, ixr2load, 6, SpringLayout.EAST, ixr2field);
		contentPane.add(ixr2load);
		
		JButton ixr3load = new JButton("Load");
		ixr3load.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String inputBinary = getInput();
				ixr3field.setText(inputBinary);
			}
		});
		sl_contentPane.putConstraint(SpringLayout.NORTH, ixr3load, 0, SpringLayout.NORTH, lblixr_3);
		sl_contentPane.putConstraint(SpringLayout.WEST, ixr3load, 6, SpringLayout.EAST, ixr3field);
		contentPane.add(ixr3load);
		
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
		
		pcfield = new JTextField();
		sl_contentPane.putConstraint(SpringLayout.NORTH, pcfield, 0, SpringLayout.NORTH, lblGpr_0);
		sl_contentPane.putConstraint(SpringLayout.WEST, pcfield, 17, SpringLayout.EAST, lblPC);
		sl_contentPane.putConstraint(SpringLayout.EAST, pcfield, 217, SpringLayout.EAST, lblPC);
		pcfield.setColumns(10);
		contentPane.add(pcfield);
		
		mbrfield = new JTextField();
		sl_contentPane.putConstraint(SpringLayout.NORTH, mbrfield, 0, SpringLayout.NORTH, lblGpr_2);
		sl_contentPane.putConstraint(SpringLayout.WEST, mbrfield, 0, SpringLayout.WEST, pcfield);
		sl_contentPane.putConstraint(SpringLayout.EAST, mbrfield, 200, SpringLayout.WEST, pcfield);
		mbrfield.setColumns(10);
		contentPane.add(mbrfield);
		
		irfield = new JTextField();
		sl_contentPane.putConstraint(SpringLayout.NORTH, irfield, 0, SpringLayout.NORTH, lblGpr_3);
		sl_contentPane.putConstraint(SpringLayout.WEST, irfield, 0, SpringLayout.WEST, pcfield);
		sl_contentPane.putConstraint(SpringLayout.EAST, irfield, 0, SpringLayout.EAST, pcfield);
		irfield.setColumns(10);
		contentPane.add(irfield);
		
		marfield = new JTextField();
		sl_contentPane.putConstraint(SpringLayout.NORTH, marfield, 0, SpringLayout.NORTH, lblGpr_1);
		sl_contentPane.putConstraint(SpringLayout.WEST, marfield, 0, SpringLayout.WEST, pcfield);
		sl_contentPane.putConstraint(SpringLayout.EAST, marfield, 0, SpringLayout.EAST, pcfield);
		marfield.setColumns(10);
		contentPane.add(marfield);
		
		JButton pcload = new JButton("Load");
		pcload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String inputBinary = getInput();
				pcfield.setText(inputBinary);
			}
		});
		sl_contentPane.putConstraint(SpringLayout.NORTH, pcload, 0, SpringLayout.NORTH, lblGpr_0);
		sl_contentPane.putConstraint(SpringLayout.WEST, pcload, 6, SpringLayout.EAST, pcfield);
		contentPane.add(pcload);
		
		JButton marload = new JButton("Load");
		marload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String inputBinary = getInput();
				marfield.setText(inputBinary);
			}
		});
		sl_contentPane.putConstraint(SpringLayout.NORTH, marload, 0, SpringLayout.NORTH, lblGpr_1);
		sl_contentPane.putConstraint(SpringLayout.WEST, marload, 6, SpringLayout.EAST, marfield);
		contentPane.add(marload);
		
		JButton mbrload = new JButton("Load");
		mbrload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String inputBinary = getInput();
				mbrfield.setText(inputBinary);
			}
		});
		sl_contentPane.putConstraint(SpringLayout.NORTH, mbrload, 0, SpringLayout.NORTH, lblGpr_2);
		sl_contentPane.putConstraint(SpringLayout.WEST, mbrload, 6, SpringLayout.EAST, mbrfield);
		contentPane.add(mbrload);
		
		ccfield = new JTextField();
		sl_contentPane.putConstraint(SpringLayout.NORTH, ccfield, -3, SpringLayout.NORTH, lblixr_2);
		ccfield.setColumns(10);
		contentPane.add(ccfield);
		
		mfrfield = new JTextField();
		sl_contentPane.putConstraint(SpringLayout.WEST, ccfield, 0, SpringLayout.WEST, mfrfield);
		sl_contentPane.putConstraint(SpringLayout.NORTH, mfrfield, 0, SpringLayout.NORTH, lblixr_1);
		sl_contentPane.putConstraint(SpringLayout.EAST, mfrfield, 0, SpringLayout.EAST, pcfield);
		mfrfield.setColumns(10);
		contentPane.add(mfrfield);
		
		JLabel lblMFR = new JLabel("MFR");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblMFR, 0, SpringLayout.NORTH, lblixr_1);
		sl_contentPane.putConstraint(SpringLayout.EAST, lblMFR, -17, SpringLayout.WEST, mfrfield);
		contentPane.add(lblMFR);
		
		JLabel lblCC = new JLabel("Condition Code");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblCC, 0, SpringLayout.NORTH, lblixr_2);
		sl_contentPane.putConstraint(SpringLayout.EAST, lblCC, 0, SpringLayout.EAST, lblMFR);
		contentPane.add(lblCC);
		
		//below here are all the check boxes.
		
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
		
		JLabel lblInstructions = new JLabel("Input below. Tick for 1, untick for 0.");
		sl_contentPane.putConstraint(SpringLayout.WEST, lblInstructions, 0, SpringLayout.WEST, operation5);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, lblInstructions, -17, SpringLayout.NORTH, operation5);
		lblInstructions.setFont(new Font("Tahoma", Font.PLAIN, 16));
		contentPane.add(lblInstructions);
		
		JToggleButton runToggle = new JToggleButton("RUN");
		sl_contentPane.putConstraint(SpringLayout.NORTH, runToggle, -17, SpringLayout.NORTH, lblOperation);
		runToggle.setFont(new Font("Tahoma", Font.BOLD, 24));
		contentPane.add(runToggle);
		
		JButton btnStore = new JButton("STORE");
		sl_contentPane.putConstraint(SpringLayout.WEST, btnStore, 0, SpringLayout.WEST, pcload);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, btnStore, 0, SpringLayout.SOUTH, lblInstructions);
		contentPane.add(btnStore);
		
		JButton btnLoad = new JButton("LOAD");
		sl_contentPane.putConstraint(SpringLayout.EAST, runToggle, 0, SpringLayout.EAST, btnLoad);
		sl_contentPane.putConstraint(SpringLayout.WEST, btnLoad, 6, SpringLayout.EAST, btnStore);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, btnLoad, 0, SpringLayout.SOUTH, lblInstructions);
		contentPane.add(btnLoad);
		
		JButton btnIPL = new JButton("Initial Program Load");
		sl_contentPane.putConstraint(SpringLayout.NORTH, btnIPL, 6, SpringLayout.SOUTH, btnStore);
		sl_contentPane.putConstraint(SpringLayout.WEST, btnIPL, 0, SpringLayout.WEST, pcload);
		contentPane.add(btnIPL);
	}
}
