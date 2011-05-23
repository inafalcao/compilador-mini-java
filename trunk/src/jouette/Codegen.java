package jouette;
import activationRegister.Frame;
import instructionSelection.Assem.*;
import treeIR.*;

public class Codegen {
	Frame frame;
	//como que vai ficar esse frame?
	
	public Codegen(Frame f){
		frame = f;
	}
	
	private InstrList ilist = null, last  = null; 
	
	private void emit(Instr inst){
		if(last != null)
			last = last.tail = new InstrList(inst, null);
		else
			last = ilist = new InstrList(inst, null);
	}
	
	public void munchStm(Stm s) throws Exception {
        if(s instanceof treeIR.MOVE) munchMove(((treeIR.MOVE)s).dst, ((treeIR.MOVE)s).src);
        else if(s instanceof treeIR.SEQ) munchSeq((treeIR.SEQ)s);
        else if(s instanceof treeIR.CJUMP) munchCJump((treeIR.CJUMP)s);
        else if(s instanceof treeIR.JUMP) munchJump((treeIR.JUMP)s);
        else if(s instanceof treeIR.EXPSTM) munchExp(((treeIR.EXPSTM)s).exp);
        
        else munchLabel((treeIR.LABEL)s);
}

	
	
	InstrList codegen(treeIR.Stm s){
		InstrList l;
		munchStm(s);
		l = ilist;
		ilist = last = null;
		return l;
	}

}
