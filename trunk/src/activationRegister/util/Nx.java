package activationRegister.util;

import activationRegister.temp.Label;
import treeIR.CONST;
import treeIR.ESEQ;
import treeIR.Stm;

public class Nx extends Exp {

	/***/
	private Stm stm;
	
	/**
	 * 
	 * */
	public Nx(Stm stm){
		this.stm = stm;
	}
	
	/**
	 * 
	 * */
	public Stm unNx(){
		return stm;
	}
	
	/***
	 * 
	 * */
	public treeIR.Exp unEx(){
		return new ESEQ(stm, new CONST(0));
	}


	/**
	 * 
	 * */
	public Stm unCx(Label t, Label f) {
		return null;
	}

}
