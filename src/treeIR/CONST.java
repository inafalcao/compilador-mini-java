package treeIR;

public class CONST extends Exp {

	public long value;
  
	public CONST(long v) {
		value=v;
	}
  
	@Override
	public ExpList kids() {
		return null;
	}
  
	@Override
	public Exp build(ExpList kids) {
		return this;
	}

	@Override
	public String print() {
		return "CONST(" + Long.toString(value)+" )";
	}
}

