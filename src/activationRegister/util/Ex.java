package activationRegister.util;

import activationRegister.temp.Label;
import treeIR.Stm;

/**
 * 
 * */
public class Ex extends Exp {
	
	/***/
	private treeIR.Exp exp;
	
	/**
	 * 
	 * */
	public Ex(treeIR.Exp exp){
		this.exp = exp;
	}

	/**
	 * 
	 * */
	public Stm unCx(Label t, Label f) {
		return null;
	}

	/**
	 * 
	 * */
	public treeIR.Exp unEx() {
		return exp;
	}

	/**
	 * 
	 * */
	public Stm unNx() {
		return new treeIR.EXPSTM(exp);
	}
	
}
