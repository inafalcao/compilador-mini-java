package treeIR;

import activationRegister.temp.Label;

public class LABEL extends Stm { 

	public Label label;
 
	public LABEL(Label l) {
		label=l;
	}
  
	public ExpList kids() {
		return null;
	}
  
	public Stm build(ExpList kids) {
		return this;
	}

	public String print() {
		return "LABEL("+ label.print()+" );";
	}
}

