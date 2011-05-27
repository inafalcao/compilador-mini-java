package translate;

import activationRegister.Frame;
import treeIR.Stm;

public class ProcFrag extends Frag
{
    public Stm body;
    public Frame frame;
    
    public ProcFrag(Stm b, Frame f)
    {
        super();
        
        body = b;
        frame = f;
    }

    @Override
	public String toString()
    {
        StringBuffer b = new StringBuffer();
        
        b.append("PROC ");
        b.append(frame.name);
        b.append("\n");
        // como imprimir a IR?
        b.append("\tENDP\n");
        
        return b.toString();
    }
}
