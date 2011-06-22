package activationRegister;

import treeIR.Exp;


public abstract class Access {

	public abstract Exp exp(Exp FP);

}

/*
 * The Access class describes formals and locals that may be in the frame or in registers.
 * This is an abstract data type, so its implementation as a pair of subclasses is visible only inside the
 * Frame module.
 */
		