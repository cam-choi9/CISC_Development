package Project6461;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

//RUN ComputerMain.java ,  this is NOT the Main file!

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
	protected short mbr = 0; //Memory Buffer Register
	private short ir = 0; //(Current) Instruction Register
	private short mfr = 0; //Machine Fault Register
	private short cc = 0; //Condition Code
	private boolean run = false;
	protected short[] memory = new short[2048];
	private boolean isaConsole = false; //for debugging. If set to true during testing, ISA will list in IDE console
	
	public CPU(ComputerMain gui) { //on creation of the class, imports the GUI class location and updates the GUI.
		this.gui = gui;
		this.start();
		updateGUI();
	}
	
	public String shortToBinaryString(short val, int size) { //Java doesn't provide a short to Binary string method or leading zeroes, just Integers to Binary. This method does that.
		String binary = "";
		String firstConversion = Integer.toBinaryString(val); //performs the initial conversion
		if (firstConversion.length() > 16) { //Deals with situations where the binary string has a ton of extra 1s due to 2's compliment.
			firstConversion = firstConversion.substring(16);
		}
		int leadingzeros = size - firstConversion.length();
		for (int i = 0; i < leadingzeros; i++) { //places leading zeroes on the front of the string.
			binary += "0";
		}
		binary += firstConversion;
		return binary;
	}
	
	public void setRunning(boolean state) {
		run = state;
	} 
	
	public void updateGUI() { //updates the GUI once any actions are performed.	Each line corresponds to a visible register.
		gui.gpr0field.setText(shortToBinaryString(gpr0, 16)); //the second digit is the length of the string
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
	
	public void iPL() {//initial program load from text file. Basically, the ROM loader
		try {
			File initialProgram = new File("program.txt"); //program.txt is the ROM.
			Scanner fileReader = new Scanner(initialProgram);
			int address = 0;
			short content = 0;
			int contentInt = 0;
			boolean first = true;
			String parsed = "";
			String line;
			while (fileReader.hasNextLine()) {
				line = fileReader.nextLine();
				address = Integer.parseInt(line.substring(0, 4), 16); //takes the first four characters of the line and parses them as hexidecimal digits into an integer
				contentInt = Integer.parseInt(line.substring((line.length() - 4)), 16); //takes the last four characters of the line and parses them as hexidecimal digits into an integer
				parsed = Integer.toBinaryString(contentInt);
				if (parsed.length() > 16) { //Java keeps turning the short into a 32 bit value and storing that in the array. This prevents that.
					parsed = parsed.substring(16);
					//System.out.println("try2 " + parsed);
					content = Short.parseShort(parsed, 2);
				}
				else {
					content = (short) contentInt;
				}
				//System.out.println(address + "__" + (Integer.toHexString(content)) + "_" + content);//for debugging.
				memory[address] = content; //loads the line to the specified address location
				if (first == true) { //for the first line of the program, set the PC and MAR to the first line. 
					pc = (short) address;
					first = false;
				}
			}
			fileReader.close();
			updateGUI();
			gui.visualizefield.setText("Program written to Memory");
		}
		catch (FileNotFoundException e) {
			gui.visualizefield.setText("IPL File Not Found");
			e.printStackTrace();
		}
	}
	
	public void runInstructionCycle() { //This is the instruction cycle
		Word executable; //The executable word... defined shortly!
		while (true) {
			if (pc >= 2048) { //this if-branch is for Part III, but it's being placed in here early as a precaution to stop the pc from running past the end of the memory array.
				mfr = 8;
				gui.mfrfield.setText("1000");
				run = false;
				break;
			}
			fetch(); //Performs the fetch method (see method below)
			executable = decode(ir); //Decode the contents of the IR (see method below)
			execute(executable); //Executes the decoded word using the opcode (see method below)
			updateGUI(); //Refresh the gui to reflect everything that happened.
			if (run == false) break; //During Execute Single Instructions or when Run is set to OFF, the loop stops here.
		} 
	}
	
	public void executeSingleInstruction() {//When the Execute Single Instruction button is enabled, this runs instead of the instruction cycle.
		Word executable;
		fetch();
		executable = decode(ir); //Decode the contents of the IR (see method below)
		execute(executable); //Executes the decoded word using the opcode (see method below)
		updateGUI(); //Refresh the gui to reflect everything that happened.
		gui.runToggle.setSelected(false);//ensures the Run button is deselected at the end.
	}
	
	public void fetch(){
		mar = pc; //The CPU sends the contents of the PC to the MAR
		mbr = memory[mar]; //In response to the read command (with address equal to PC), 
		//the memory returns the data stored at the memory location indicated by PC, 
		//and copies it to the MBR.
		ir = mbr; //CPU copies data from the MBR to the Instruction Register
		pc++;//The PC is incremented so that it points to the next instruction.
	}
	
	public Word decode(short ir){ //this is the first part of the control unit. It takes the IR and decodes it into an executable Word
		Word executable = new Word(ir);
		return executable;
	}
	public void execute(Word word){ //this is the second part of the control unit. 
	//It takes the opcode from the decode section and turns it into an executable instruction 
		switch (word.opcode) {
			case 0: hlt();
					break;
			case 1: ldr(word);
					break;
			case 2: str(word);
					break;
			case 3: lda(word);
					break;
			case 41: ldx(word);
					break;
			case 42: lda(word);
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
	
	public short effectiveAddress(Word word) { //Calculates the Effective Address using the parameters in instruction packet on page 7
		short indAddress = 0;
		if (word.idb == false) {//If Indirect Bit IS NOT set
			if (word.ixrN == 0) { //if the IXR number is 0
				return word.address; //return just the Word's base address value
			}
			else { //if IXR number is 1-3
				switch (word.ixrN) { //calculate the IXR plus the base address value
				case 1: indAddress = (short) (ixr1 + word.address);
					break;
				case 2: indAddress = (short) (ixr2 + word.address);
					break;
				case 3: indAddress = (short) (ixr3 + word.address);
					break;
				}
				return indAddress; //...and return that
			}
		}
		else { //IF Indirect Bit IS set
			if (word.ixrN == 0) { //if the IXR number is 0
				return memory[word.address]; //return the contents at memory address that the address value is pointing to
			}
			else { //if IXR number is 1-3
				switch (word.ixrN) { //calculate the IXR plus the base address value
				case 1: indAddress = (short) (ixr1 + word.address);
					break;
				case 2: indAddress = (short) (ixr2 + word.address);
					break;
				case 3: indAddress = (short) (ixr3 + word.address);
					break;
				}
				return memory[indAddress]; //...and return the contents at memory address that the new value is pointing to.
			}
		}
	}
	/*
	 * All of the instruction methods will be placed below here, in numerical order
	 * */
	public void hlt() { // 00 Halt
		if(isaConsole == true) System.out.println("Halted!"); //Debugging tool
		run = false; //stops the fetch cycle loop.
		gui.runToggle.setSelected(false);//if the halt occurred during a running program, this resets the state of the Run button. 
	}
	public void ldr(Word word) { // 01 Load Register from Memory
		if(isaConsole == true) System.out.println("01"); //Debugging tool
		switch (word.gprN) { //uses gpr number in the word to determine which register to use.
		case 0: gpr0 = memory[effectiveAddress(word)];
			break;
		case 1: gpr1 = memory[effectiveAddress(word)];
			break;
		case 2: gpr2 = memory[effectiveAddress(word)];
			break;
		case 3: gpr3 = memory[effectiveAddress(word)];
			break;
		default: gui.visualizefield.setText("LDR failed.");
		}
	}
	public void str(Word word) { //02 Load Register to Memory
		if(isaConsole == true) System.out.println("02"); //Debugging tool
		switch (word.gprN) { //uses gpr number in the word to determine which register to use.
		case 0: memory[effectiveAddress(word)] = gpr0;
				break;
		case 1: memory[effectiveAddress(word)] = gpr1;
				break;
		case 2: memory[effectiveAddress(word)] = gpr2;
				break;
		case 3: memory[effectiveAddress(word)] = gpr3;
				break;
		default: gui.visualizefield.setText("STR failed.");
		}
	}
	public void lda(Word word) { //03 Load Register with Address
		if(isaConsole == true) System.out.println("03"); //Debugging tool
		switch (word.gprN) { //uses gpr number in the word to determine which register to use.
		case 0: gpr0 = effectiveAddress(word);
				break;
		case 1: gpr1 = effectiveAddress(word);
				break;
		case 2: gpr2 = effectiveAddress(word);
				break;
		case 3: gpr3 = effectiveAddress(word);
				break;
		default: gui.visualizefield.setText("LDA failed.");
		}
	}
	public void ldx(Word word) { //41 Load Index Register from Memory
		if(isaConsole == true) System.out.println("41"); ////Debugging tool
		switch (word.ixrN) { //uses ixr number in the word to determine which register to use.
		case 1: ixr1 = memory[effectiveAddress(word)];
			break;
		case 2: ixr2 = memory[effectiveAddress(word)];
			break;
		case 3: ixr3 = memory[effectiveAddress(word)];
			break;
		default: gui.visualizefield.setText("LDR failed.");
		}
	}
	public void stx(Word word) { //42 Store Index Register to Memory
		if(isaConsole == true) System.out.println("42"); //Debugging tool
		switch (word.ixrN) { //uses ixr number in the word to determine which register to use.
		case 1: memory[effectiveAddress(word)] = ixr1;
				break;
		case 2: memory[effectiveAddress(word)] = ixr2;
				break;
		case 3: memory[effectiveAddress(word)] = ixr3;
				break;
		default: gui.visualizefield.setText("STX failed.");
		}
	}
}
