package Project6461;

import java.lang.Math;

//Run ComputerMain.java , this is NOT the Main file!

public class Word {
	protected short opcode; //Operation Code, derived from the first six bits of the word
	protected short gprN; //GPR number, derived from bits 7 and 8 of the word.
	protected short ixrN; //IXR number, derived from bits 9 and 10 of the word.
	protected boolean idb; //Indirect bit, derived from bit 11 of the word.
	protected short address; //Memory address, derived from bits 12 through 16 of the word.
	
	public Word(short ir) { //when the word is first created, all possible values for operations are calculated.
		opcode = getOpcode(ir);
		gprN = getGprN(ir);
		ixrN = getIxrN(ir);
		address = getAddress(ir);
		//debugging();
	}
	
	private short getOpcode(short ir) { //performs bit shifting to isolate and find the OpCode
		short operationcode = ir;
		operationcode = (short) (operationcode >> 10); //remove everything else from the word to get the opcode
		return operationcode;
	}
	
	private short getGprN(short ir) { //performs bit shifting to isolate and find gpr value
		short gprN = ir;
		gprN = (short) (gprN << 6); //remove leading bits.
		gprN = (short) Math.abs((gprN >> 14)); //Remove trailing bits. Java's casting leaves this in negative for some reason. Taking the absolute value fixes it.
		return gprN;
	}
	
	private short getIxrN(short ir) { //performs bit shifting to isolate and find ixr value
		short ixrN = ir;
		ixrN = (short) (ixrN << 8); //remove leading bits.
		ixrN = (short) Math.abs((ixrN >> 14)); //Remove trailing bits. Java's casting leaves this in negative for some reason. Taking the absolute value fixes it.
		return ixrN;
	}
	
	private short getAddress(short ir) { //performs bit shifting to isolate and find address
		short addr = ir;
		addr = (short) (addr << 11); //remove leading bits
		addr = (short) Math.abs((addr >> 11)); //Shift back to starting position. Java's casting leaves this in negative for some reason. Taking the absolute value fixes it.
		return addr;
	}
	
	private void debugging() { //Troubleshooting tool for checking the contents of the word. Disabled otherwise
		System.out.println("opcode=" + opcode);
		System.out.println("gprN=" + gprN);
		System.out.println("ixrN=" + ixrN);
		System.out.println("Addr=" + address);
	}
}
