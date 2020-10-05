package Project6461;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.Integer.*;

public class CPU extends Thread {
	private ComputerMain gui; //used by the CPU to reference the GUI where necessary
	protected short gpr0 = 0; //gpr0-3 are general-purpose registers
	protected short gpr1 = 0; 
	protected short gpr2 = 0;
	protected short gpr3 = 0;
	protected short ixr1 = 0; //ixr 1-3 are Index registers
	protected short ixr2 = 0;
	protected short ixr3 = 0;
	protected short pc = 0; //program counter
	protected short mar = 0; //Memory Address Register
	protected short mbr = 0; //MBR
	private short ir = 0; //(Current) Instruction Register
	private short mfr = 0; //Machine Fault Register
	private short cc = 0; //Condition Code
	private boolean run = false;
	protected short[] memory = new short[2048];
	
	public CPU(ComputerMain gui) { //on creation of the class, imports the GUI class location and assigns the values to the appropriate locations.
		this.gui = gui;
		this.start();
		updateGUI();
	}
	
	public String shortToBinaryString(short val, int size) { //for registers of other lengths
		String binary = "";
		String firstConversion = Integer.toBinaryString(val);
		int leadingzeros = size - firstConversion.length();
		for (int i = 0; i < leadingzeros; i++) {
			binary += "0";
		}
		binary += firstConversion;
		return binary;
	}
	
	public void setRunning(boolean state) {
		run = state;
	} 
	
	public void updateGUI() { //updates the GUI once any actions are performed.	
		gui.gpr0field.setText(shortToBinaryString(gpr0, 16));
		gui.gpr1field.setText(shortToBinaryString(gpr1, 16));
		gui.gpr2field.setText(shortToBinaryString(gpr2, 16));
		gui.gpr3field.setText(shortToBinaryString(gpr3, 16));
		gui.ixr1field.setText(shortToBinaryString(ixr1, 16));
		gui.ixr2field.setText(shortToBinaryString(ixr2, 16));
		gui.ixr3field.setText(shortToBinaryString(ixr3, 16));
		gui.pcfield.setText(shortToBinaryString(pc, 12));
		gui.marfield.setText(shortToBinaryString(mar, 12));
		gui.mbrfield.setText(shortToBinaryString(mbr, 16));
		gui.irfield.setText(shortToBinaryString(ir, 16));
		gui.mfrfield.setText(shortToBinaryString(mfr, 4));
		gui.ccfield.setText(shortToBinaryString(cc, 4));
	}
	
	public void iPL() {//initial program load from text file
		try {
			File initialProgram = new File("program.txt");
			Scanner fileReader = new Scanner(initialProgram);
			int address = 0;
			short content = 0;
			String line;
			while (fileReader.hasNextLine()) {
				line = fileReader.nextLine();
				address = Integer.parseInt(line.substring(0, 4), 16); //takes the first four characters of the line and parses them as hexidecimal digits into an integer
				System.out.println(address);
				content = (short) Integer.parseInt(line.substring((line.length() - 3), (line.length() - 0)), 16); //takes the last four characters of the line and parses them as hexidecimal digits into an integer
				System.out.println(content);
				memory[address] = content; //loads the line to the specified address location
			}
			gui.visualizefield.setText("Program written to Memory");
		}
		catch (FileNotFoundException e) {
			gui.visualizefield.setText("IPL File Not Found");
			e.printStackTrace();
		}
	}
	
	public void runInstructionCycle() {
		short opcode;
		while (true) {
			fetch();
			opcode = decode(mbr);
			execute(opcode);
			updateGUI();
			if (run == false) break;
			if (pc == 2048) { //this is for Part III, but it's being placed in here early to stop the pc from running out of control
				mfr = 8;
				gui.mfrfield.setText("1000");
				run = false;
				break;
			}
		} 
	}
	
	public void fetch(){
		mar = pc; //The CPU sends the contents of the PC to the MAR
		mbr = memory[mar]; //In response to the read command (with address equal to PC), 
		//the memory returns the data stored at the memory location indicated by PC, 
		//and copies it to the MBR.
		ir = mbr; //CPU copies data from the MBR to the Instruction Register
		pc++;//The PC is incremented so that it points to the next instruction.
	}
	
	public short decode(short mbr){ //this is the first part of the control unit. It takes the value in the MBR and finds the opcode
		short operationcode = mbr;
		operationcode = (short) (operationcode >> 10); //remove everything else from the word to get the opcode
		return operationcode;
	}
	public void execute(short opcode){ //this is the second part of the control unit. 
	//It takes the opcode from the decode section and turns it into an executable instruction 
		switch (opcode) {
			case 0: hlt();
					break;
			case 1: ldr();
					break;
			case 2: str();
					break;
			case 3: lda();
					break;
			case 41: ldx();
					break;
			case 42: lda();
					break;
			default: gui.visualizefield.setText("Failed to get opcode.");
		}
	}
	
	public void storeMemAtMARtoMBR() { //GUI button, stores memory at MAR address to MBR
		mbr = memory[mar];
		updateGUI(); //...and updates the gui to reflect that
	}
	
	public void storeMBRtoMemAtMAR() { //GUI button, stores MBR content to Memory at the address in MAR
		memory[mar] = mbr; 
		//you can't see the contents of the memory, so there's no GUI update here.
	}
	
	/*
	 * All of the instruction methods will be placed below here, in numerical order
	 * */
	public void hlt() { // 00 Halt
		System.out.println("Halted!");
		//run = false;
	}
	public void ldr() { // 01 Load Register from Memory
		System.out.println("01");
	}
	public void str() { //02 Load Register to Memory
		System.out.println("02");
	}
	public void lda() { //03 Load Register with Address
		System.out.println("03");
	}
	public void ldx() { //41 Load Index Register from Memory
		System.out.println("41");
	}
	public void stx() { //42 Store Index Register to Memory
		System.out.println("42");
	}

}
