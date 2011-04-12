package treeIR;

abstract public class Stm {

	abstract public ExpList kids();

	abstract public Stm build(ExpList kids);
	
	abstract public String print();
}

