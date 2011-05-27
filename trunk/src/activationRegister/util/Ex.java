package activationRegister.util;

import activationRegister.temp.Label;
import treeIR.CJUMP;
import treeIR.CONST;
import treeIR.Stm;

class Ex extends Exp
{
    treeIR.Exp exp;
    
    Ex(treeIR.Exp e)
    {
        super();
        
        exp = e;
    }

    @Override
	treeIR.Exp unEx()
    {    
        return exp;
    }

    @Override
	Stm unNx()
    {
        return new treeIR.EXPSTM(exp);
    }

    @Override
	Stm unCx(Label t, Label f)
    {
        return new CJUMP(CJUMP.EQ, exp, new CONST(0), f, t);
    }

}
