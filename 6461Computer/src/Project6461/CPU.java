package Project6461;


import java.lang.Integer.*;

public class CPU extends Thread {
	private ComputerMain gui; //used by the CPU to reference the GUI where necessary
	private int gpr0 = 0; //gpr0-3 are general-purpose registers
	private short gpr1 = 0; 
	private short gpr2 = 0;
	private short gpr3 = 0;
	private short ixr1 = 0; //ixr 1-3 are IXR registers
	private short ixr2 = 0;
	private short ixr3 = 0;
	private short pc = 0; //program counter
	private short mar = 0; //Memory Address Register
	private short mbr = 0; //MBR
	private short ir = 0; //Instruction Register
	
	public CPU(ComputerMain gui) { //on creation of the class, imports the GUI class location and assigns the values to the appropriate locations.
		this.gui = gui;
		this.start();
	}
	
	public void run() {
		updateGUI();
		
	}
	
	public void updateGUI() { //updates the GUI once any actions are performed.
		gui.gpr0field.setText(Integer.toBinaryString(gpr0));
	}

}
