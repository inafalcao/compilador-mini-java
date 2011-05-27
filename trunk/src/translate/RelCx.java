package translate;

import activationRegister.temp.Label;
import treeIR.CJUMP;
import treeIR.Stm;

class RelCx extends Cx
{
    private Exp left;
    private Exp right;
    private int op;
    
    RelCx(int o, Exp l, Exp r)
    {
        super();
        
        op = o;
        left = l;
        right = r;
    }

    @Override
	Stm unCx(Label t, Label f)
    {
        return new CJUMP(op, left.unEx(), right.unEx(), t, f);
    }

}
