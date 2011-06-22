package jouette;

import activationRegister.temp.Temp;
import treeIR.Exp;
import treeIR.TEMP;
import activationRegister.Access;

class InReg extends Access
{
    public Temp temp;
    
    public InReg(Temp t)
    {
        temp =t;
    }

    public Exp exp(Exp framePtr)
    {
        return new TEMP(temp);
    }
    
    public String toString() {
        return temp.toString();
    }


}
