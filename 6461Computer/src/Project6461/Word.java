package Project6461;

public class Word {
	protected short opcode;
	protected short reg1;
	protected short reg2;
	protected short address;
	
	public Word(short mbr) {
		opcode = getOpcode(mbr);
		reg1 = getReg1(mbr);
		reg2 = getReg2(mbr);
		address = getAddress(mbr);
		debugging();
	}
	
	private short getOpcode(short mbr) {
		short operationcode = mbr;
		operationcode = (short) (operationcode >> 10); //remove everything else from the word to get the opcode
		return operationcode;
	}
	
	private short getReg1(short mbr) {
		//TODO calculating wrong values
		short reg1 = mbr;
		reg1 = (short) (reg1 << 6);
		reg1 = (short) (reg1 >> 13);
		return reg1;
	}
	
	private short getReg2(short mbr) {
		//TODO calculating wrong values?
		short reg2 = mbr;
		reg2 = (short) (reg2 << 8);
		reg2 = (short) (reg2 >> 13);
		return reg2;
	}
	
	private short getAddress(short mbr) {
		short addr = mbr;
		addr = (short) (addr << 11);
		addr = (short) (addr >> 11);
		return addr;
	}
	
	private void debugging() {
		System.out.println("opcode=" + opcode);
		System.out.println("Reg1=" + reg1);
		System.out.println("Reg2=" + reg2);
		System.out.println("Addr=" + address);
	}

}
