package treeIR;

public class CONST extends Exp {

	public int value;
  
	public CONST(int v) {
		value=v;
	}
  
	public ExpList kids() {
		return null;
	}
  
	public Exp build(ExpList kids) {
		return this;
	}

	public String print() {
		return "CONST(" + Integer.toString(value)+" );";
	}
}

