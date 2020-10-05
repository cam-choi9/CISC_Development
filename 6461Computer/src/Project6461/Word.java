package Project6461;

public class Word {
	protected short opcode;
	protected short reg1;
	protected short reg2;
	protected short address;
	
	public Word(short ir) {
		opcode = getOpcode(ir);
		reg1 = getReg1(ir);
		reg2 = getReg2(ir);
		address = getAddress(ir);
		debugging();
	}
	
	private short getOpcode(short ir) {
		short operationcode = ir;
		operationcode = (short) (operationcode >> 10); //remove everything else from the word to get the opcode
		return operationcode;
	}
	
	private short getReg1(short ir) {
		//TODO calculating wrong values
		short reg1 = ir;
		reg1 = (short) (reg1 << 6);
		reg1 = (short) (reg1 >> 13);
		return reg1;
	}
	
	private short getReg2(short ir) {
		//TODO calculating wrong values?
		short reg2 = ir;
		reg2 = (short) (reg2 << 8);
		reg2 = (short) (reg2 >> 13);
		return reg2;
	}
	
	private short getAddress(short ir) {
		short addr = ir;
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
