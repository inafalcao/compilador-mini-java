package translate;

import activationRegister.temp.Label;
import activationRegister.temp.Temp;
import treeIR.CONST;
import treeIR.ESEQ;
import treeIR.LABEL;
import treeIR.MOVE;
import treeIR.SEQ;
import treeIR.Stm;
import treeIR.TEMP;

public abstract class Cx extends Exp
{
    @Override
	treeIR.Exp unEx()
    {
        Label t = new Label();
        Label f = new Label();
        Temp r = new Temp();
        
        MOVE move1 = new MOVE( new TEMP(r),new CONST(1) );
        MOVE move0 = new MOVE( new TEMP(r),new CONST(0) );
        SEQ tail = new SEQ( move0, new LABEL(t) );
        SEQ mid = new SEQ(new LABEL(f), tail);
        SEQ before = new SEQ(unCx(t,f), mid);
        SEQ retval = new SEQ(move1, before);
        
        return new ESEQ(retval, new TEMP(r));
    }

    // TODO: Sera que esta certo???
    @Override
	Stm unNx()
    {
        Label t = new Label();
        Label f = new Label();
        /*---------------------------------------
        Temp r = new Temp();
        
        MOVE move1 = new MOVE( new TEMP(r),new CONST(1) );
        MOVE move0 = new MOVE( new TEMP(r),new CONST(0) );
        SEQ tail = new SEQ( move0, new LABEL(t) );
        SEQ mid = new SEQ(new LABEL(f), tail);
        SEQ before = new SEQ(unCx(t,f), mid);
        SEQ retval = new SEQ(move1, before);
        ---------------------------------------*/
        
        return unCx(t,f);
    }

    @Override
	abstract Stm unCx(Label t, Label f);
}