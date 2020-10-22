package Project6461;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

//RUN ComputerMain.java ,  this is NOT the Main file!

/*
 * CPU.java is the "brains" of the simulator, and performs all of the actual calculations and functions.
 * It also contains the values for the registers which are pushed to the gui after update events.
 * The Initial Program Load method can be found here, along with the Instruction Cycle and
 * Instruction Set Architecture. During the instruction cycle, it takes content from the Instruction Register (ir)
 * and converts them into Word objects. These Word objects are then placed into the execute method which
 * uses them to execute the appropriate instruction.
 * Because ComputerMain.java's GUI and CPU.java must run concurrently, this object runs in a separate thread.
 * 
 * Part 1 written by Michael Ashery, reviewed by Jaeseok Choi and Daniel Brewer.
 * Part 2 Cache by Jaeseok Choi
 * Part 2 ISA by Michael Ashery
 * */

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
	protected short[] memory = new short[2048]; // # of words = 2048 => main memory size = # of words (2048) * block size (2 bytes) = 4096 bytes = 2^12 bytes => physical address = 12 bits
    protected short[] cache = new short[16]; // # of cache lines = 16 => cache size = # of cache lines (16) * block size (2 bytes) = 32 bytes = 2^5 bytes => cache address = 5 bits
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
		//clear all registers and memory
		gpr0 = 0;
		gpr1 = 0; 
		gpr2 = 0;
		gpr3 = 0;
		ixr1 = 0;
		ixr2 = 0;
		ixr3 = 0;
		pc = 0; 
		mar = 0;
		mbr = 0;
		ir = 0; 
		mfr = 0;
		cc = 0; 
		memory = new short[2048];
		cache = new short[16];
		//read from the text file
		try {
			File initialProgram = new File("program.txt"); //program.txt is the ROM.
			Scanner fileReader = new Scanner(initialProgram);
			int address = 0;
			short content = 0;
			int contentInt = 0;
			boolean first = true;
			int cacheLineNumber = 0;
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
                cache[cacheLineNumber] = content; // loads the line to replace the first block in cache memory
                cacheLineNumber++; // move index to point the next block to be out
                if (cacheLineNumber == cache.length) { // if the pointer reaches the end of the cache memory, 
                    cacheLineNumber = 0; // sets it back to the head of the cache memory => first-in-first-out                                     
                }
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
		// cache_search(); // search cache memory first => If 'hit', execute the instruction. Else, access main memory.
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
	
	public void cache_search() { // search through the cache memory and execute the instruction if found in the cache memory
        for (int i=0; i<cache.length; i++) {                
            if (cache[i] == ir) {     // "hit" case
                // execute 
                // no need to access main memory
            }
        }
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
		case 4: amr(word);
				break;
		case 5: smr(word);
				break;
		case 6: air(word);
				break;
		case 7: sir(word);
				break;
		case 10: jz(word);
				break;
		case 11: jne(word);
				break;
		case 12: jcc(cc, word);
				break;
		case 13: jma(word);
				break;
		case 20: mlt(word);
				break;
		case 21: dvd(word);
				break;
		case 22: trr(word);
				break;
		case 23: and(word);
				break;
		case 24: orr(word);
				break;
		case 25: not(word);
				break;
		case 31: src(word);
				break;
		case 32: rrc(word);
				break;
		case 41: ldx(word);
				break;
		case 42: lda(word);
				break;
		case 61: in(word);
				break;
		case 62: out(word);
				break;
		case 63: chk(word);
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
				if(isaConsole == true) System.out.println("EAcase1");
				return word.address; //return just the Word's base address value
			}
			else { //if IXR number is 1-3
				if(isaConsole == true) System.out.println("EAcase2");
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
				if(isaConsole == true) System.out.println("EAcase3");
				return memory[word.address]; //return the contents at memory address that the address value is pointing to
			}
			else { //if IXR number is 1-3
				if(isaConsole == true) System.out.println("EAcase4");
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
		pc--; //cancels out the PC increment in the fetch method.
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
	public void amr(Word word) {//TODO 04 Add Memory to Register
		if(isaConsole == true) System.out.println("04"); //Debugging tool
		switch (word.gprN) { //uses gpr number in the word to determine which register to use.
		case 0: gpr0 += effectiveAddress(word);
				break;
		case 1: gpr1 += effectiveAddress(word);
				break;
		case 2: gpr2 += effectiveAddress(word);
				break;
		case 3: gpr3 += effectiveAddress(word);
				break;
		default: gui.visualizefield.setText("AMR failed.");
		}
	}
	public void smr(Word word) {//TODO 05 Subtract Memory from Register
		if(isaConsole == true) System.out.println("05"); //Debugging tool
		switch (word.gprN) { //uses gpr number in the word to determine which register to use.
		case 0: gpr0 -= effectiveAddress(word);
				break;
		case 1: gpr1 -= effectiveAddress(word);
				break;
		case 2: gpr2 -= effectiveAddress(word);
				break;
		case 3: gpr3 -= effectiveAddress(word);
				break;
		default: gui.visualizefield.setText("AMR failed.");
		}
	}
	public void air(Word word) {//TODO 06 Add Immediate to Register
		if(isaConsole == true) System.out.println("06"); //Debugging tool
	}
	public void sir(Word word) {//TODO 07 Subtract Immediate from Register
		if(isaConsole == true) System.out.println("07"); //Debugging tool
	}
	public void jz(Word word) { //10 Jump if Zero
		if(isaConsole == true) System.out.println("10"); //Debugging tool
		switch (word.gprN) { //uses gpr number in the word to determine which register to use.
		case 0: if(gpr0 == 0) pc = effectiveAddress(word);
				break;
		case 1: if(gpr1 == 0) pc = effectiveAddress(word);
				break;
		case 2: if(gpr2 == 0) pc = effectiveAddress(word);
				break;
		case 3: if(gpr3 == 0) pc = effectiveAddress(word);
				break;
		default: gui.visualizefield.setText("JZ failed.");
		//note, PC+1 happens automatically as part of fetch method. This overrides that if triggered.
		}
	}
	public void jne(Word word) { //11 Jump if Not Zero
		if(isaConsole == true) System.out.println("11"); //Debugging tool
		switch (word.gprN) { //uses gpr number in the word to determine which register to use.
		case 0: if(gpr0 != 0) pc = effectiveAddress(word);
				break;
		case 1: if(gpr1 != 0) pc = effectiveAddress(word);
				break;
		case 2: if(gpr2 != 0) pc = effectiveAddress(word);
				break;
		case 3: if(gpr3 != 0) pc = effectiveAddress(word);
				break;
		default: gui.visualizefield.setText("JZ failed.");
		//note, PC+1 happens automatically as part of fetch method. This overrides that if triggered.
		}
	}
	public void jcc(short condition, Word word) { //12 Jump if Condition Code
		if(isaConsole == true) System.out.println("12"); //Debugging tool
		short compare;
		switch (word.gprN) { //uses gpr field as a stand-in for determining the cc bit to check
		case 0: compare = 0b0000000000000001; //check first bit using mask
				if((cc & compare) == compare) pc = effectiveAddress(word);
				break;
		case 1: compare = 0b0000000000000010; //check second bit using mask
				if((cc & compare) == compare) pc = effectiveAddress(word);
				break;
		case 2: compare = 0b0000000000000100; //check third bit using mask
				if((cc & compare) == compare) pc = effectiveAddress(word);
				break;
		case 3: compare = 0b0000000000001000; //check fourth bit using mask
				if((cc & compare) == compare) pc = effectiveAddress(word);
				break;
		default: gui.visualizefield.setText("JCC failed.");
		//note, PC+1 happens automatically as part of fetch method. This overrides that if triggered.
		}
	}
	public void jma(Word word) { //13 Unconditional Jump to Address
		if(isaConsole == true) System.out.println("12"); //Debugging tool
		pc = effectiveAddress(word);
	}
	public void jsr(Word word) {//TODO 14 Jump and Save Return Address
		if(isaConsole == true) System.out.println("14"); //Debugging tool
	}
	public void rfs(Word word) {//TODO 15 Return from Subroutine with Return Code as Immed portion stored in the instruction's address field.
		if(isaConsole == true) System.out.println("15"); //Debugging tool
	}
	public void sob(Word word) {//16 Subtract One and Branch
		switch (word.gprN) { //uses gpr number in the word to determine which register to use.
		case 0: gpr0--;
				if (gpr0 > 0) pc = effectiveAddress(word); 
				break;
		case 1: gpr1--;
				if (gpr1 > 0) pc = effectiveAddress(word);
				break;
		case 2: gpr2--;
				if (gpr2 > 0) pc = effectiveAddress(word);
				break;
		case 3: gpr3--;
				if (gpr3 > 0) pc = effectiveAddress(word);
				break;
		default: gui.visualizefield.setText("JZ failed.");
		//note, PC+1 happens automatically as part of fetch method. This overrides that if triggered.
		}
	}
	public void jge(Word word) {//17 Jump Greater Than or Equal To
		switch (word.gprN) { //uses gpr number in the word to determine which register to use.
		case 0: if (gpr0 > 0) pc = effectiveAddress(word); 
				break;
		case 1: if (gpr1 > 0) pc = effectiveAddress(word);
				break;
		case 2: if (gpr2 > 0) pc = effectiveAddress(word);
				break;
		case 3: if (gpr3 > 0) pc = effectiveAddress(word);
				break;
		default: gui.visualizefield.setText("JZ failed.");
		//note, PC+1 happens automatically as part of fetch method. This overrides that if triggered.
		}
	}
	public void mlt(Word word) {//TODO 20 Multiply Register by Register
		if(isaConsole == true) System.out.println("20"); //Debugging tool
	}
	public void dvd(Word word) {//TODO 21 Divide Register by Register
		if(isaConsole == true) System.out.println("21"); //Debugging tool
	}
	public void trr(Word word) {//TODO 22 Test Equality of two Registers
		if(isaConsole == true) System.out.println("22"); //Debugging tool
	}
	public void and(Word word) {//TODO 23 Logical AND Register by Register
		if(isaConsole == true) System.out.println("23"); //Debugging tool
	}
	public void orr(Word word) {//TODO 24 Logical OR Register by Register
		if(isaConsole == true) System.out.println("24"); //Debugging tool
	}
	public void not(Word word) {//TODO 25 Logical NOT Register by Register
		if(isaConsole == true) System.out.println("25"); //Debugging tool
	}
	public void src(Word word) {//TODO 31 Shift Register by Count
		if(isaConsole == true) System.out.println("31"); //Debugging tool
	}
	public void rrc(Word word) {//TODO 32 Rotate Register by Count
		if(isaConsole == true) System.out.println("32"); //Debugging tool
	}
	public void ldx(Word word) { //41 Load Index Register from Memory
		if(isaConsole == true) {System.out.println("41"); ////Debugging tool
		System.out.println("ldxEfAd is =" + effectiveAddress(word));}
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
		if(isaConsole == true) {System.out.println("42"); //Debugging tool
		System.out.println("stxEfAd is =" + effectiveAddress(word));}
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
	public void in(Word word) {//TODO 61 Input character to Register from Device
		if(isaConsole == true) System.out.println("61"); //Debugging tool
	}
	public void out(Word word) {//TODO 62 Output character to Device from Register
		if(isaConsole == true) System.out.println("62"); //Debugging tool
	}
	public void chk(Word word) {//TODO 63 Check Device Status to Register
		if(isaConsole == true) System.out.println("63"); //Debugging tool
	}
}
