package treeIR;

import activationRegister.temp.Label;

public class LABEL extends Stm { 

	public Label label;
 
	public LABEL(Label l) {
		label=l;
	}
  
	@Override
	public ExpList kids() {
		return null;
	}
  
	@Override
	public Stm build(ExpList kids) {
		return this;
	}

	@Override
	public String print() {
		return "LABEL("+ label.print()+" );";
	}
}

