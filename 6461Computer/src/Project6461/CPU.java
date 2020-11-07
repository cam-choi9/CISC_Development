package Project6461;

import java.util.Scanner;
import java.util.Deque;
import java.util.LinkedList;
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
	private short replacement = 0; //Index of the cache line that needs to be replaced when the cache memory is full and needs to be updated  
	private boolean run = false;
	protected boolean automatic = true; //the automatic variable is used in the CHK instruction.
	protected short[] memory = new short[2048]; // # of words = 2048 => physical address = 11 bits
	/*	Cache Implementation
     *  1. # of cache lines is 16
     *  2. Each cache line holds both "memory block (16 bits)" & "memory address (11 tag bits)" => short cannot contain 27 bits, thus uses int[] for cache
     *  3. Use a FIFO algorithm to replace the oldest cache line
     */
	protected int[] cache = new int[16];     
	private boolean isaConsole = true; //for debugging. If set to true during testing, ISA will list in IDE console
	protected String printOut = ""; //This is what appears on the printer.
	protected Deque<Character> inputBuffer = new LinkedList<Character>();
	
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
		gui.printer.setText(printOut);
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
		printOut = "";
		mfr = 0;
		cc = 0; 
		replacement = 0;
		memory = new short[2048];
		cache = new int[16];
		gui.printer.setText("");
		int lineNo = 1; //for troubleshooting.
		//read from the text file
		try {
			File initialProgram = new File("program.txt"); //program.txt is the ROM.
			Scanner fileReader = new Scanner(initialProgram);
			int address = 0;
			short content = 0;
			int contentInt = 0;
			int cacheLine = 0; // memory address (11 bits) + data in that address (16 bits)
			boolean first = true;			
			String parsed = "";
			String line;
			while (fileReader.hasNextLine()) {
				line = fileReader.nextLine();
				if (line == ""|| line == " "|| line == "  ") {;} //If the line is completely empty, do nothing. This lets us ignore empty lines in text files, or lines that are just spaces masquerating as empty lines.
				else if (line.charAt(0) == '#') {;}//If the first character in the line is # do nothing. This lets us add comments to the code.
				else {
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
//					System.out.println(address + "__" + (Integer.toHexString(content)) + "_" + content);//for debugging.
					memory[address] = content; //loads the line to the specified address location                         
					cacheLine = address; // each cache line holds both the address & the data => cacheLine = address(11 bits) + content(16 bits)
					String s = "";
					cacheLine <<= 16;	// shifts 16 bits to the left since word has 16 bits
					if (contentInt < 0) {
						s = Integer.toBinaryString(ir);
						s = s.substring(16, 32);
						contentInt = Integer.parseInt(s, 2);
					}
					cacheLine += contentInt;	// adds content to the cache line
					cache[replacement++] = cacheLine; // loads the content to replace the first block in cache memory & moves index to point the next block to be out                
					if (replacement == cache.length) { // if the pointer reaches the end of the cache memory, 
						replacement = 0; // sets it back to the head of the cache memory => first-in-first-out
//						System.out.println("cache full!");
//						for (int i=0; i<cache.length; i++) { //Debugging => check if the cache holds the address & the content correctly        	
//							s = Integer.toBinaryString(cache[i]);
//							s = ("000000000000000000000000000" + s).substring(s.length());//							
//							String a = s.substring(0, 11);
//							String w = s.substring(11, s.length());            
//							System.out.println((short) (Integer.parseInt(a,2)) + "__" + (Integer.toHexString(Integer.parseInt(w,2))) + "_" + (short) (Integer.parseInt(w,2)));//for debugging.				
//						}
					}
//					
					if (first == true) { //for the first line of the program, set the PC and MAR to the first line. 
						pc = (short) address;
						first = false;
					}
                }
				lineNo++;
			}
			fileReader.close();
			updateGUI();
			gui.visualizefield.setText("Program written to Memory");
		}
		catch (StringIndexOutOfBoundsException e) {
			gui.visualizefield.setText("File Format Error - EL");
			gui.hexField.setText("" + lineNo);
			System.out.println("Problem line " + lineNo);
		}
		catch (NumberFormatException e) {
			gui.visualizefield.setText("File Format Error - SPC");
			gui.hexField.setText("" + lineNo);
			System.out.println("Problem line " + lineNo);
			e.printStackTrace();
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
			
			if (fetch()) {}  //Performs the fetch method & checks the cache memory first(see method below)
			else {
				executable = decode(ir); //Decode the contents of the IR (see method below)
				execute(executable); //Executes the decoded word using the opcode (see method below)
				cacheUpdate(); //Updates the cache with the new content that was used		
			}					
			updateGUI(); //Refresh the gui to reflect everything that happened.			
    		gui.runToggle.setSelected(false);//ensures the Run button is deselected at the end.		  		
			if (run == false) break; //During Execute Single Instructions or when Run is set to OFF, the loop stops here.
		} 
	}
	
	public void executeSingleInstruction() {//When the Execute Single Instruction button is enabled, this runs instead of the instruction cycle.
		Word executable;
		if (fetch()) {} //Performs the fetch method & checks the cache memory first(see method below)
		else {
			executable = decode(ir); //Decode the contents of the IR (see method below)
			execute(executable); //Executes the decoded word using the opcode (see method below)
			cacheUpdate(); //Updates cache memory (see method below)		
		}		
		updateGUI(); //Refresh the gui to reflect everything that happened.
		gui.runToggle.setSelected(false);//ensures the Run button is deselected at the end.
	}
	
	public boolean fetch(){
		mar = pc; //The CPU sends the contents of the PC to the MAR
		if (cacheSearch()) {
			if(isaConsole == true) System.out.println("Running Instruction at " + Integer.toHexString(pc)); //Debugging tool
			pc++;//The PC is incremented so that it points to the next instruction.
			return true;
		} //If the instruction is found in the cache memory, no need to access the main memory (see the method below) 
		else {
			mbr = memory[mar]; //In response to the read command (with address equal to PC), 
			//the memory returns the data stored at the memory location indicated by PC, 
			//and copies it to the MBR.
			ir = mbr; //CPU copies data from the MBR to the Instruction Register
			if(isaConsole == true) System.out.println("Running Instruction at " + Integer.toHexString(pc)); //Debugging tool
			pc++;//The PC is incremented so that it points to the next instruction.
		}				
		return false;
	}
	/*  
	 * Searches through the cache memory first => if cache contains the memory address required by pc, decode and execute the instruction ("Hit" case)
	 */
	public boolean cacheSearch() { 
		System.out.println("Begin Cache Search");
		short address = 0; //Tag bits holding the address of the cache line 
		short word = 0; //Data bits holding the word(content) of the cache line 
		Word executable; //If the instruction is stored in the cache memory, which is a hit case, it will execute within the function before accessing the main memory
		String s = ""; //Empty string that will hold the cache line - 27 bits of address + data
		for (int i=0; i<cache.length; i++) { //Iterate through the cache memory        	
			s = Integer.toBinaryString(cache[i]); //Convert integer into a binary string
			s = ("000000000000000000000000000" + s).substring(s.length()); //Get exact 27 bits (address(11bits) + word(16bits)) with leading zeros 
//            System.out.println("binary string: " + s); //Debug            
			String a = s.substring(0, 11); //String 'a' contains the first 11 bits of the binary string, which is the address bits
			String w = s.substring(11, s.length()); //String 'w' contains the rest bits of the binary string, which is the word bits 
			address = (short) (Integer.parseInt(a,2)); //Convert the address string to a short
			word = (short) (Integer.parseInt(w,2)); //Convert the word string to a short
//            System.out.println("cache line address: " + address); //Debug
//            System.out.println("cache line content: " + word); //Debug            
//            System.out.println("pc: "+ pc); //Debug
			if (address == mar) { //If the address of the cache line matches with the address stored inside the Memory Address Register, => "hit" case
				System.out.println("cache HIT! "); //Debug
				executable = decode(word); //Decodes the data stored in the address
				execute(executable); //Executes the instruction        		
				return true; //Return true if "hit" case => indicating that there is no need to access the main memory
			}            
		}        
		System.out.println("data not found in the cache");
		return false; //Return false if "miss" case => indicating that the CPU needs to access the main memory 
    }
	
	/*
	 * If the content is not in cache memory (miss case), 
	 * update the cache with a new content stored in the main memory
	 * using a FIFO algorithm => the oldest cache line will be replaced
	 */
	public void cacheUpdate() { 
		int address = (int) mar; //The current memory address
		int content = (int) ir; //The current instruction holds the data in the memory address
		String s = "";
		int cacheLine = address; // each cache line holds both the address & the data => cacheLine = address(11 bits) + content(16 bits)
//		System.out.println("address to be updated: " + address);
//		System.out.println("content to be updated: " + content);		
		
		if (content < 0) { // if IR is a negative integer, converts it to a 
			s = Integer.toBinaryString(ir);
			s = s.substring(16, 32);
			content = Integer.parseInt(s, 2);
		}
		
//		System.out.println("content updated: " + content);
//		System.out.println("cache line address: " + cacheLine);
		cacheLine <<= 16;	// shifts 16 bits to the left since word has 16 bits
		cacheLine += content;	// adds content to the cache line
//		System.out.println("cache line +content: " + cacheLine);
//		System.out.println("replacement before: " + replacement);		
		s = Integer.toBinaryString(cacheLine);
		s = ("000000000000000000000000000" + s).substring(s.length()); //Get exact 27 bits (address(11bits) + word(16bits)) with leading zeros 
//        System.out.println("binary string: " + s); // debug
        
		String a = s.substring(0, 11); //String 'a' contains the first 11 bits of the binary string, which is the address bits
		String w = s.substring(11, s.length()); //String 'w' contains the rest bits of the binary string, which is the word bits           
		address = (short) (Integer.parseInt(a,2)); //Convert the address string to a short      
		content = (short) (Integer.parseInt(w,2)); //Convert the word string to a short
//		System.out.println("updated address: " + address); //Debug
//		System.out.println("updated content: " + content); //Debug
		cache[replacement++] = cacheLine; // loads the content to replace the first block in cache memory & moves index to point the next block to be out
		if (replacement == cache.length) { // if the pointer reaches the end of the cache memory, 
			replacement = 0; // sets it back to the head of the cache memory => first-in-first-out                                     
		}				
//		System.out.println("replacement after: " + replacement);//Debug		
		
		System.out.println("cache updated!"); //Debug
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
		case 8: add(word); //This is an extension. Adds two GPRs and places result in another GPR.
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
		case 26: tir(word);
				break;
		case 30: trap(); //nonfunctional, waiting for part 3
				break;
		case 31: src(word);
				break;
		case 32: rrc(word);
				break;
		//33-37 reserved for floating point
		case 41: ldx(word);
				break;
		case 42: stx(word);
				break;
		case 46: aix(word);//This is an extension
				break;
		//50-51 reserved for floating point
		case 60: scrl(); //This is an extension. Scrolls printer.
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
				eaHexval(word.address);
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
				eaHexval(indAddress);
				return indAddress; //...and return that
			}
		}
		else { //IF Indirect Bit IS set
			if (word.ixrN == 0) { //if the IXR number is 0
				if(isaConsole == true) System.out.println("EAcase3");
				eaHexval(memory[word.address]);
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
				eaHexval(memory[indAddress]);
				return memory[indAddress]; //...and return the contents at memory address that the new value is pointing to.
			}
		}
		 //Debugging tool
	}
	public void eaHexval (short nothex) {//another debugging tool for seeing the Hex values of EAs being retunred.
		if(isaConsole == true) {
			String hexval = Integer.toHexString(nothex);
			System.out.println("EA is " + hexval);
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
		if(isaConsole == true) System.out.println("01 LDR"); //Debugging tool
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
		if(isaConsole == true) System.out.println("02 STR"); //Debugging tool
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
		if(isaConsole == true) System.out.println("03 LDA"); //Debugging tool
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
	public void amr(Word word) {// 04 Add Memory to Register
		if(isaConsole == true) System.out.println("04 AMR"); //Debugging tool
		else {
			switch (word.gprN) { //uses gpr number in the word to determine which register to use.
			case 0: 
				if ((gpr0 + effectiveAddress(word)) > 32767) { //Java stores shorts as signed values, so this is the upper limit.
					cc = 8; //set overflow
					hlt();
					break;
				}
				else if ((gpr0 + effectiveAddress(word)) < -32768) { //Java stores shorts as signed values, so this is the lower limit.
					cc = 4; //set underflow
					hlt();
					break;
				}
				else {
				gpr0 += effectiveAddress(word);
				break;
				}
			case 1: 
				if ((gpr1 + effectiveAddress(word)) > 32767) { //Java stores shorts as signed values, so this is the upper limit.
					cc = 8; //set overflow
					hlt();
					break;
				}
				else if ((gpr1 + effectiveAddress(word)) < -32768) { //Java stores shorts as signed values, so this is the lower limit.
					cc = 4; //set underflow
					hlt();
					break;
				}
				else {
					gpr1 += effectiveAddress(word);
					break;
				}
			case 2: 
				if ((gpr2 + effectiveAddress(word)) > 32767) { //Java stores shorts as signed values, so this is the upper limit.
					cc = 8; //set overflow
					hlt();
					break;
				}
				else if ((gpr2 + effectiveAddress(word)) < -32768) { //Java stores shorts as signed values, so this is the lower limit.
					cc = 4; //set underflow
					hlt();
					break;
				}
				else {
					gpr2 += effectiveAddress(word);
					break;
				}
			case 3: 
				if ((gpr3 + effectiveAddress(word)) > 32767) { //Java stores shorts as signed values, so this is the upper limit.
					cc = 8; //set overflow
					hlt();
					break;
				}
				else if ((gpr3 + effectiveAddress(word)) < -32768) { //Java stores shorts as signed values, so this is the lower limit.
					cc = 4; //set underflow
					hlt();
					break;
				}
				else {
					gpr3 += effectiveAddress(word);
					break;
				}
			default: gui.visualizefield.setText("AMR failed.");
			}
		}
	}
	public void smr(Word word) {// 05 Subtract Memory from Register
		if(isaConsole == true) System.out.println("05 SMR"); //Debugging tool
		switch (word.gprN) { //uses gpr number in the word to determine which register to use.
		case 0: 
			if ((gpr0 - effectiveAddress(word)) > 32767) { //Java stores shorts as signed values, so this is the upper limit.
				cc = 8; //set overflow
				hlt();
				break;
			}
			else if ((gpr0 - effectiveAddress(word)) < -32768) { //Java stores shorts as signed values, so this is the lower limit.
				cc = 4; //set underflow
				hlt();
				break;
			}
			else {
			gpr0 -= effectiveAddress(word);
			break;
			}
		case 1: 
			if ((gpr1 - effectiveAddress(word)) > 32767) { //Java stores shorts as signed values, so this is the upper limit.
				cc = 8; //set overflow
				hlt();
				break;
			}
			else if ((gpr1 - effectiveAddress(word)) < -32768) { //Java stores shorts as signed values, so this is the lower limit.
				cc = 4; //set underflow
				hlt();
				break;
			}
			else {
				gpr1 -= effectiveAddress(word);
				break;
			}
		case 2: 
			if ((gpr2 - effectiveAddress(word)) > 32767) { //Java stores shorts as signed values, so this is the upper limit.
				cc = 8; //set overflow
				hlt();
				break;
			}
			else if ((gpr2 - effectiveAddress(word)) < -32768) { //Java stores shorts as signed values, so this is the lower limit.
				cc = 4; //set underflow
				hlt();
				break;
			}
			else {
				gpr2 -= effectiveAddress(word);
				break;
			}
		case 3: 
			if ((gpr3 - effectiveAddress(word)) > 32767) { //Java stores shorts as signed values, so this is the upper limit.
				cc = 8; //set overflow
				hlt();
				break;
			}
			else if ((gpr3 - effectiveAddress(word)) < -32768) { //Java stores shorts as signed values, so this is the lower limit.
				cc = 4; //set underflow
				hlt();
				break;
			}
			else {
				gpr3 -= effectiveAddress(word);
				break;
			}
		default: gui.visualizefield.setText("SMR failed.");
		}
	}
	public void air(Word word) {// 06 Add Immediate to Register
		if(isaConsole == true) System.out.println("06 AIR"); //Debugging tool
		switch (word.gprN) { //uses gpr number in the word to determine which register to use.
		case 0: 
			if ((gpr0 + word.immed) > 32767) { //Java stores shorts as signed values, so this is the upper limit.
				cc = 8; //set overflow
				hlt();
				break;
			}
			else if ((gpr0 + word.immed) < -32768) { //Java stores shorts as signed values, so this is the lower limit.
				cc = 4; //set underflow
				hlt();
				break;
			}
			else {
				gpr0 += word.immed;
				break;
			}
		case 1: 
			if ((gpr1 + word.immed) > 32767) { //Java stores shorts as signed values, so this is the upper limit.
				cc = 8; //set overflow
				hlt();
				break;
			}
			else if ((gpr1 + word.immed) < -32768) { //Java stores shorts as signed values, so this is the lower limit.
				cc = 4; //set underflow
				hlt();
				break;
			}
			else {
				gpr1 += word.immed;
				break;
			}
		case 2:  
			if ((gpr2 + word.immed) > 32767) { //Java stores shorts as signed values, so this is the upper limit.
				cc = 8; //set overflow
				hlt();
				break;
			}
			else if ((gpr2 + word.immed) < -32768) { //Java stores shorts as signed values, so this is the lower limit.
				cc = 4; //set underflow
				hlt();
				break;
			}
			else {
				gpr2 += word.immed;
				break;
			}
		case 3: 
			if ((gpr3 + word.immed) > 32767) { //Java stores shorts as signed values, so this is the upper limit.
				cc = 8; //set overflow
				hlt();
				break;
			}
			else if ((gpr3 + word.immed) < -32768) { //Java stores shorts as signed values, so this is the lower limit.
				cc = 4; //set underflow
				hlt();
				break;
			}
			else {
				gpr3 += word.immed;
				break;
			}
		default: gui.visualizefield.setText("AIR failed.");
		}
	}
	public void sir(Word word) {// 07 Subtract Immediate from Register
		if(isaConsole == true) System.out.println("07 SIR"); //Debugging tool
		switch (word.gprN) { //uses gpr number in the word to determine which register to use.
		case 0: 
			if ((gpr0 - word.immed) > 32767) { //Java stores shorts as signed values, so this is the upper limit.
				cc = 8; //set overflow
				hlt();
				break;
			}
			else if ((gpr0 - word.immed) < -32768) { //Java stores shorts as signed values, so this is the lower limit.
				cc = 4; //set underflow
				hlt();
				break;
			}
			else {
				gpr0 -= word.immed;
				break;
			}
		case 1: 
			if ((gpr1 - word.immed) > 32767) { //Java stores shorts as signed values, so this is the upper limit.
				cc = 8; //set overflow
				hlt();
				break;
			}
			else if ((gpr1 - word.immed) < -32768) { //Java stores shorts as signed values, so this is the lower limit.
				cc = 4; //set underflow
				hlt();
				break;
			}
			else {
				gpr1 -= word.immed;
				break;
			}
		case 2:  
			if ((gpr2 - word.immed) > 32767) { //Java stores shorts as signed values, so this is the upper limit.
				cc = 8; //set overflow
				hlt();
				break;
			}
			else if ((gpr2 - word.immed) < -32768) { //Java stores shorts as signed values, so this is the lower limit.
				cc = 4; //set underflow
				hlt();
				break;
			}
			else {
				gpr2 -= word.immed;
				break;
			}
		case 3: 
			if ((gpr3 - word.immed) > 32767) { //Java stores shorts as signed values, so this is the upper limit.
				cc = 8; //set overflow
				hlt();
				break;
			}
			else if ((gpr3 - word.immed) < -32768) { //Java stores shorts as signed values, so this is the lower limit.
				cc = 4; //set underflow
				hlt();
				break;
			}
			else {
				gpr3 -= word.immed;
				break;
			}
		default: gui.visualizefield.setText("SIR failed.");
		}
	}
	public void add(Word word) { //THIS IS AN EXTENSION. 8 Adds two values and places in gpr0
		if(isaConsole == true) System.out.println("8 ADD"); //Debugging tool
		short crx = 0, cry = 0;
		switch (word.rx) {
		case 0: crx = gpr0;
				break;
		case 1: crx = gpr1;
				break;
		case 2: crx = gpr2;
				break;
		case 3: crx = gpr3;
				break;
		}
		switch (word.ry) {
		case 0: cry = gpr0;
				break;
		case 1: cry = gpr1;
				break;
		case 2: cry = gpr2;
				break;
		case 3: cry = gpr3;
				break;
		}
		short sum = (short) (crx + cry);
		if (sum > 32767) { //Java stores shorts as signed values, so this is the upper limit.
			cc = 8; //set overflow
			hlt();
		}
		else if (sum < -32768) { //Java stores shorts as signed values, so this is the lower limit.
			cc = 4; //set underflow
			hlt();
		}
		else {
			gpr0 = sum;
		}
		//note, PC+1 happens automatically as part of fetch method. This overrides that if triggered.
		
	}
	public void jz(Word word) { //10 Jump if Zero
		if(isaConsole == true) System.out.println("10 JZ"); //Debugging tool
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
		if(isaConsole == true) System.out.println("11 JNE"); //Debugging tool
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
		if(isaConsole == true) System.out.println("12 JCC"); //Debugging tool
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
		if(isaConsole == true) System.out.println("13 JMA"); //Debugging tool
		pc = effectiveAddress(word);
	}
	public void jsr(Word word) {// 14 Jump and Save Return Address
		if(isaConsole == true) System.out.println("14 JSR"); //Debugging tool
		gpr3 = (short) (pc + 1);
		pc = effectiveAddress(word);
		//gpr0 should be set to pointer before this is started
	}
	public void rfs(Word word) {// 15 Return from Subroutine with Return Code as Immed portion stored in the instruction's address field.
		if(isaConsole == true) System.out.println("15 RFS"); //Debugging tool
		gpr0 = word.immed;
		pc = gpr3;
	}
	public void sob(Word word) {//16 Subtract One and Branch
		if(isaConsole == true) System.out.println("16 SOB"); //Debugging tool
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
		default: gui.visualizefield.setText("SOB failed.");
		//note, PC+1 happens automatically as part of fetch method. This overrides that if triggered.
		}
	}
	public void jge(Word word) {//17 Jump Greater Than or Equal To
		if(isaConsole == true) System.out.println("17 JGE"); //Debugging tool
		switch (word.gprN) { //uses gpr number in the word to determine which register to use.
		case 0: if (gpr0 > 0) pc = effectiveAddress(word); 
				break;
		case 1: if (gpr1 > 0) pc = effectiveAddress(word);
				break;
		case 2: if (gpr2 > 0) pc = effectiveAddress(word);
				break;
		case 3: if (gpr3 > 0) pc = effectiveAddress(word);
				break;
		default: gui.visualizefield.setText("JGE failed.");
		//note, PC+1 happens automatically as part of fetch method. This overrides that if triggered.
		}
	}
	public void mlt(Word word) {// 20 Multiply Register by Register
		if(isaConsole == true) System.out.println("20 MLT"); //Debugging tool
			int product;
			short crx = 0, cry = 0;
			switch (word.rx) {
			case 0: crx = gpr0;
					break;
			case 1: crx = gpr1;
					break;
			case 2: crx = gpr2;
					break;
			case 3: crx = gpr3;
					break;
			}
			switch (word.ry) {
			case 0: cry = gpr0;
					break;
			case 1: cry = gpr1;
					break;
			case 2: cry = gpr2;
					break;
			case 3: cry = gpr3;
					break;
			}
			short highorder, loworder;
			switch (word.rx) { //uses rx number in the word to determine which register to use.
			case 0: product = crx * cry;
					if (product > 32767) { //Java stores shorts as signed values, so this is the upper limit.
						cc = 8; //set overflow
						hlt();
						break;
					}
					else if (product < -32768) { //Java stores shorts as signed values, so this is the lower limit.
						cc = 4; //set underflow
						hlt();
						break;
					}
					else {
						highorder = (short) (product >> 16);
						loworder = (short) ((product << 16) >> 16);
						gpr0 = highorder;
						gpr1 = loworder;
						break;
					}
			case 2: product = crx * cry;
				if (product > 32767) { //Java stores shorts as signed values, so this is the upper limit.
					cc = 8; //set overflow
					hlt();
					break;
				}
				else if (product < -32768) { //Java stores shorts as signed values, so this is the lower limit.
					cc = 4; //set underflow
					hlt();
					break;
				}
				else {
					highorder = (short) (product >> 16);
					loworder = (short) ((product << 16) >> 16);
					gpr2 = highorder;
					gpr3 = loworder;
					break;
				}
			case 1:
			case 3: gui.visualizefield.setText("MLT Invalid Reg");
					break;
			default: gui.visualizefield.setText("MLT failed.");
		}	
	}
	public void dvd(Word word) {// 21 Divide Register by Register
		if(isaConsole == true) System.out.println("21 DVD"); //Debugging tool
		short crx = 0, cry = 0;
		switch (word.rx) {
		case 0: crx = gpr0;
				break;
		case 1: crx = gpr1;
				break;
		case 2: crx = gpr2;
				break;
		case 3: crx = gpr3;
				break;
		}
		switch (word.ry) {
		case 0: cry = gpr0;
				break;
		case 1: cry = gpr1;
				break;
		case 2: cry = gpr2;
				break;
		case 3: cry = gpr3;
				break;
		}
		if (word.ry == 0) {
			cc = 2; //set divzero flag
			hlt();
		}
		else {
			short quotient, remainder;
			switch (word.rx) { //uses rx number in the word to determine which register to use.
			case 0: quotient = (short) (crx / cry);
					remainder = (short) (crx % cry);
					gpr0 = quotient;
					gpr1 = remainder;
					break;
			case 2: quotient = (short) (crx / cry);
					remainder = (short) (crx % cry);
					gpr2 = quotient;
					gpr3 = remainder;
					break;
			case 1:
			case 3: gui.visualizefield.setText("DVD Invalid Reg");
					break;
			default: gui.visualizefield.setText("DVD failed.");
			}
		}
	}
	public void trr(Word word) {// 22 Test Equality of two Registers
		if(isaConsole == true) System.out.println("22 TRR"); //Debugging tool
		short crx = 0, cry = 0;
		switch (word.rx) {
		case 0: crx = gpr0;
				break;
		case 1: crx = gpr1;
				break;
		case 2: crx = gpr2;
				break;
		case 3: crx = gpr3;
				break;
		}
		switch (word.ry) {
		case 0: cry = gpr0;
				break;
		case 1: cry = gpr1;
				break;
		case 2: cry = gpr2;
				break;
		case 3: cry = gpr3;
				break;
		}
		if (crx == cry) cc = 1;
		else cc = 0;
	}
	public void and(Word word) {// 23 Logical AND Register by Register
		if(isaConsole == true) System.out.println("23 AND"); //Debugging tool
		short crx = 0, cry = 0, result = 0;
		switch (word.rx) {
		case 0: crx = gpr0;
				break;
		case 1: crx = gpr1;
				break;
		case 2: crx = gpr2;
				break;
		case 3: crx = gpr3;
				break;
		}
		switch (word.ry) {
		case 0: cry = gpr0;
				break;
		case 1: cry = gpr1;
				break;
		case 2: cry = gpr2;
				break;
		case 3: cry = gpr3;
				break;
		}
		result = (short) (crx & cry); //perform bitwise AND on the contents of the two registers
		switch (word.rx) {
		case 0: gpr0 = result;
				break;
		case 1: gpr1 = result;
				break;
		case 2: gpr2 = result;
				break;
		case 3: gpr3 = result;
				break;
		}
	}
	public void orr(Word word) {// 24 Logical OR Register by Register
		if(isaConsole == true) System.out.println("24 ORR"); //Debugging tool
		short crx = 0, cry = 0, result = 0;
		switch (word.rx) {
		case 0: crx = gpr0;
				break;
		case 1: crx = gpr1;
				break;
		case 2: crx = gpr2;
				break;
		case 3: crx = gpr3;
				break;
		}
		switch (word.ry) {
		case 0: cry = gpr0;
				break;
		case 1: cry = gpr1;
				break;
		case 2: cry = gpr2;
				break;
		case 3: cry = gpr3;
				break;
		}
		result = (short) (crx | cry); //perform bitwise OR on the contents of the two registers
		switch (word.rx) {
		case 0: gpr0 = result;
				break;
		case 1: gpr1 = result;
				break;
		case 2: gpr2 = result;
				break;
		case 3: gpr3 = result;
				break;
		}
	}
	public void not(Word word) {//25 Logical NOT Register by Register
		if(isaConsole == true) System.out.println("25 NOT"); //Debugging tool
		short crx =0;
		switch (word.rx) {
		case 0: crx = gpr0;
				break;
		case 1: crx = gpr1;
				break;
		case 2: crx = gpr2;
				break;
		case 3: crx = gpr3;
				break;
		}
		crx = (short) (~crx);
		switch (word.rx) {
		case 0: gpr0 = crx;
				break;
		case 1: gpr1 = crx;
				break;
		case 2: gpr2 = crx;
				break;
		case 3: gpr3 = crx;
				break;
		}
	}
	public void tir(Word word) {// 26 Test Equality of two Registers. THIS IS AN EXTENSION.
		if(isaConsole == true) System.out.println("26 TIR"); //Debugging tool
		short crx = 0, cry = 0;
		switch (word.rx) {
		case 0: crx = gpr0;
				break;
		case 1: crx = gpr1;
				break;
		case 2: crx = gpr2;
				break;
		case 3: crx = gpr3;
				break;
		}
		switch (word.ry) {
		case 0: cry = gpr0;
				break;
		case 1: cry = gpr1;
				break;
		case 2: cry = gpr2;
				break;
		case 3: cry = gpr3;
				break;
		}
		if (crx < cry) cc = 1;
		else cc = 0;
	}
	
	public void trap() {//30 Trap code
		if(isaConsole == true) System.out.println("30 TRAP"); //Debugging tool
		//add content in Part III
	}
	public void src(Word word) {//31 Shift Register by Count
		if(isaConsole == true) System.out.println("31 SRC"); //Debugging tool
		short result = 0;
		boolean crash = false;
		int usablecount = (int) word.count;
		switch (word.r) {
		case 0: result = gpr0;
				break;
		case 1: result = gpr1;
				break;
		case 2: result = gpr2;
				break;
		case 3: result = gpr3;
				break;
		}
		if (word.al == true) { //logical shift
			if (word.lr == true) { //LR True is left
				result = (short) (result << usablecount); // Java doesn't have a <<< because << removes the leftmost bit anyway.
			}
			else { //LR False is right
				result = (short) (result >>> usablecount);
			}
		}
		else { //arithmetic shift
			if (word.lr == true) { //LR True is left
				result = (short) (result << usablecount);
			}
			else { //LR False is right
				result = (short) (result >> usablecount);
				if (result < -32768) { //Java stores shorts as signed values, so this is the lower limit.
					cc = 4; //set underflow
					hlt();
					crash = true;
					gui.visualizefield.setText("SRC Underflow");
				}
			}
		}
		if (crash == false) {//this check occurs to ensure that the switch doesn't happen in the event of a underflow halt.
			switch (word.r) {
			case 0: gpr0 = result;
					break;
			case 1: gpr1 = result;
					break;
			case 2: gpr2 = result;
					break;
			case 3: gpr3 = result;
					break;
			}
		}
	}
	public void rrc(Word word) {// 32 Rotate Register by Count
		if(isaConsole == true) System.out.println("32 RRC"); //Debugging tool
		if (word.count == 0) ; //if Count is 0, don't move anything.
		else {
			short result = 0;
			String resultstring = "", tempstring = "";
			switch (word.r) {
			case 0: result = gpr0;
					break;
			case 1: result = gpr1;
					break;
			case 2: result = gpr2;
					break;
			case 3: result = gpr3;
					break;
			}
			tempstring = shortToBinaryString(result, 16);
			// read into new string in requested order
			if (word.lr == true) { //LR True is left
				for (int i = word.count; i < 16 ; i++) {
					resultstring += tempstring.charAt(i);
				}
				for (int i = 0; i < word.count ; i++) {
					resultstring += tempstring.charAt(i);
				}
			}
			else { //LR false is right
				for (int i = (16 - word.count); i < 16 ; i++) { //read rightmost first
					resultstring += tempstring.charAt(i);
				}
				for (int i = 0; i < (16 - word.count) ; i++) { //read from left until countpoint
					resultstring += tempstring.charAt(i);
				}
			}
			result = gui.binaryStrToShort(resultstring);
			switch (word.r) {
			case 0: gpr0 = result;
					break;
			case 1: gpr1 = result;
					break;
			case 2: gpr2 = result;
					break;
			case 3: gpr3 = result;
					break;
			}
		}
	}
	public void ldx(Word word) { //41 Load Index Register from Memory
		if(isaConsole == true) {System.out.println("41 LDX");} ////Debugging tool
		switch (word.ixrN) { //uses ixr number in the word to determine which register to use.
		case 1: ixr1 = 0;
				ixr1 = memory[effectiveAddress(word)];
				break;
		case 2: ixr2 = 0;
				ixr2 = memory[effectiveAddress(word)];
				break;
		case 3: ixr3 = 0;
				ixr3 = memory[effectiveAddress(word)];
				break;
		default: gui.visualizefield.setText("LDR failed.");
		}
	}
	public void stx(Word word) { //42 Store Index Register to Memory
		if(isaConsole == true) {System.out.println("42 STX");} //Debugging tool
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
	public void aix(Word word) { //THIS IS AN EXTENSION I CREATED. 46 adds Immediate to Index Register
		if(isaConsole == true) {System.out.println("46 AIX");} //Debugging tool
		switch (word.ixrN) { //uses ixr number in the word to determine which register to use.
		case 1: ixr1 += word.immed;
			break;
		case 2: ixr2 += word.immed;
			break;
		case 3: ixr3 += word.immed;
			break;
		default: gui.visualizefield.setText("LDR failed.");
		}
	}
	public void scrl() {//THIS IS AN EXTENSION I CREATED. 60 Scrolls the printer to next line.
		if(isaConsole == true) System.out.println("60 SCRL"); //Debugging tool
		printOut += "\n";
	}
	public void in(Word word) {// 61 Input character to Register from Device
		if(isaConsole == true) System.out.println("61 IN"); //Debugging tool
		if (word.devID == 0) {
			char firstCharacter = inputBuffer.remove(); //pulls only the first character of the string in the keyboard field
			switch (word.r) { //uses gpr number in the word to determine which register to use.
			case 0: gpr0 = (short) firstCharacter;
					break;
			case 1: gpr1 = (short) firstCharacter;
					break;
			case 2: gpr2 = (short) firstCharacter;
					break;
			case 3: gpr3 = (short) firstCharacter;
					break;
			}
		}
		else {
			gui.printer.setText("Wrong DevID in pgm");
		}
	}
	public void out(Word word) {// 62 Output character to Device from Register
		if(isaConsole == true) System.out.println("62 OUT"); //Debugging tool
		short ascii = 0;
		char charValue;
		if(word.devID != 1) {
			gui.printer.setText("Wrong DevID in pgm");
		} 
		else { //take whatever's currently on the printer, add another character to it, and return it.
			switch (word.r) { //uses gpr number in the word to determine which register to use.
			case 0: ascii = gpr0;
					break;
			case 1: ascii = gpr1;
					break;
			case 2: ascii = gpr2;
					break;
			case 3: ascii = gpr3;
					break;
			}
			charValue = (char) ascii;
			if(isaConsole == true) {
				System.out.println("ascii short= " + ascii);
				System.out.println("convert chara= " + charValue);
			} //Debugging tool
			printOut += charValue;
			gui.printer.setText(printOut);//for debugging. printOut is updated to the GUI along with everything else at the end of the Instruction Cycle
		}
	}
	public synchronized void chk(Word word) {//TODO 63 Check Device Status to Register
		if(isaConsole == true) System.out.println("63 CHK"); //Debugging tool
		if (word.devID == 0) {//this is for reading from the kb on the UI.
			if (automatic == true) {  
			//In automatic mode, hitting a loop will lock up the GUI because Java Swing is single-threaded.
			//This try-catch block suspends the thread in Automatic Mode while the user enters an input.
			//This is a fault of Java Swing and there's nothing we can do about it.
			//But if you are worried about authenticity, run the program in Single-Instruction mode.
			//That will disable this block. You will notice that because it has time to wait for 
			//user input between instructions, the program runs just fine.
				try {
					if(isaConsole == true) System.out.println("Waiting!");
					wait();}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (gui.inReady == true) {
				switch(word.r) {
				case 0: gpr0 = 1;
						break;
				case 1: gpr1 = 1;
						break;
				case 2: gpr2 = 1;
						break;
				case 3: gpr3 = 1;
						break;
				}
				System.out.println("Check Passed");
				gui.inReady = false;
			}
			else {
				switch(word.r) {
				case 0: gpr0 = 0;
						break;
				case 1: gpr1 = 0;
						break;
				case 2: gpr2 = 0;
						break;
				case 3: gpr3 = 0;
						break;
				}
				System.out.println("Check Failed");
			}
		}
		else {printOut += "WRONG INPUT ID";}
	}
}
