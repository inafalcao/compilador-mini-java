package activationRegister;

import instructionSelection.Assem.InstrList;

import symbolTable.Symbol;
import activationRegister.temp.Label;
import activationRegister.temp.Temp;
import activationRegister.util.BoolList;
import treeIR.Exp;
import treeIR.ExpList;
import treeIR.StmList;

public abstract class Frame {
	public Label name;
	public AccessList formals;
	public abstract Frame newFrame(Symbol name, BoolList args);
	public abstract int wordSize();
	public abstract Access allocLocal(boolean escape);
	public abstract Temp FP();
	public abstract Temp RV();
	public abstract Exp externalCall(String func, ExpList args);
	public abstract String tempMap(Temp temp);
	public abstract StmList procEntryExit1(StmList body);
	public abstract InstrList procEntryExit2(InstrList body);
	public abstract InstrList procEntryExit3(InstrList body);
	public abstract InstrList codegen(StmList s);
}

