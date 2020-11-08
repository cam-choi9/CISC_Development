package Project6461;

import java.lang.Math;

//Run ComputerMain.java , this is NOT the Main file!

/*
 * Word.java
 * During the decode portion of CPU.java's instruction cycle, a 16-bit string is used to create a Word object.
 * Word objects take the 16-bit value and parse it into smaller values that can be easily referenced  
 * by CPU.java's Instruction Set Architecture.
 * 
 * Part 1 Written by Michael Ashery, reviewed by Jaeseock Choi and Daniel Brewer.
 * Part 2 Modifications for new ISA by Michael Ashery, reviewed by Jaeseock Choi.
 * */

public class Word {
	protected short opcode; //Operation Code, derived from the first six bits of the word
	protected short gprN; //GPR number, derived from bits 7 and 8 of the word.
	protected short ixrN; //IXR number, derived from bits 9 and 10 of the word.
	protected boolean idb; //Indirect bit, derived from bit 11 of the word.
	protected short address; //Memory address, derived from bits 12 through 16 of the word. Also used for immed
	protected short rx; //for logical/arithmeitc operations. Also R for Register shift/Rotations
	protected short ry;
	protected short immed;
	protected short r;
	protected boolean al, lr;
	protected short count;
	protected short devID;
	private boolean debug = true; //set to true to enable console debugging
	
	public Word(short ir) { //when the word is first created, all possible values for operations are calculated.
		opcode = getOpcode(ir);
		gprN = getGprN(ir);
		ixrN = getIxrN(ir);
		idb = getIndirectBit(ir);
		rx = gprN;
		ry = ixrN;
		address = getAddress(ir);
		immed = address; //yes, we technically don't need to do this, but it makes the code in CPU easier to understand.
		r = gprN;
		al = getAL(ir);
		lr = getLR(ir);
		count = count(ir);
		devID = address;
		if (debug == true) {
			System.out.println("IR passed " + ir + " in binary " + Integer.toBinaryString(ir));
			debugging();
		}
	}
	
	private short getOpcode(short ir) { //performs bit shifting to isolate and find the OpCode
		short operationcode = ir;
		String parsed = "";
		if (ir < 0) {
			parsed = Integer.toBinaryString(ir);
			//System.out.println("try " + parsed);
			parsed = parsed.substring(16, 22);
			//System.out.println("try2 " + parsed);
			operationcode = Short.parseShort(parsed, 2);
		}
		else {
			operationcode = (short) (operationcode >>> 10);//remove everything else from the word to get the opcode
		} 
		return operationcode;
	}
	private short getGprN(short ir) { //performs bit shifting to isolate and find gpr value
		short gprN = ir;
		gprN = (short) (gprN & 0b0000001100000000);
		gprN = (short) (gprN >>> 8);
		
		return gprN;
	}
	private short getIxrN(short ir) { //performs bit shifting to isolate and find ixr value
		short ixrN = ir;
		ixrN = (short) (ixrN & 0b0000000011000000);
		ixrN = (short) (ixrN >>> 6);
		return ixrN;
	}
	private boolean getIndirectBit(short ir) { //determines if the indirect bit is set
		short idbN = ir;
		if ((idbN & 0b0000000000100000) == 0b0000000000100000) {return true;}
		else return false;
	}
	private short getAddress(short ir) { //performs bit shifting to isolate and find address
		short addr = ir;
		addr = (short) (addr & 0b0000000000011111); //isolate last five bits.
		return addr;
	}
	private boolean getAL(short ir) { //determines if the AL bit is set
		short idbN = ir;
		if ((idbN & 0b0000000010000000) == 0b0000000010000000) {return true;}
		else return false;
	}
	private boolean getLR(short ir) { //determines if the LR bit is set
		short idbN = ir;
		if ((idbN & 0b0000000001000000) == 0b0000000001000000) {return true;}
		else return false;
	}
	private short count(short ir) {
		short countVal = ir;
		countVal = (short) (countVal & 0b0000000000001111);//isolate last four bits.
		return countVal;
	}
	private void debugging() { //Troubleshooting tool for checking the contents of the word. Disabled otherwise
		
		System.out.println("opcode=" + opcode);
		System.out.println("gprN, Rx, r=" + gprN);
		System.out.println("ixrN, Ry=" + ixrN);
		System.out.println("idb=" + idb);
		System.out.println("Addr, Immed, devID=" + address);
		System.out.println("Count=" + count);
	}
}
