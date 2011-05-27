package activationRegister.util;

import activationRegister.temp.Label;

public abstract class Exp
{
    Exp()
    {
        super();
    }

    abstract treeIR.Exp unEx();
    
    abstract treeIR.Stm unNx();
    
    abstract treeIR.Stm unCx(Label t, Label f);
}
