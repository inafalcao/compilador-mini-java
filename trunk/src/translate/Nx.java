package translate;

import activationRegister.temp.Label;
import treeIR.Stm;

class Nx extends Exp
{
    Stm stm;
    
    public Nx(Stm s)
    {
        super();
        
        stm = s;
    }

    @Override
	treeIR.Exp unEx()
    {
        throw new Error("unEx chamado para Nx");
    }

    @Override
	Stm unNx()
    {
        return stm;
    }

    @Override
	Stm unCx(Label t, Label f)
    {
        throw new Error("unEx chamado para Nx");
    }
}
