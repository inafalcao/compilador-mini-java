package translate;

import activationRegister.Frame;
import symbolTable.SymbolTable;
import symbolTable.Class;
import symbolTable.Method;
import util.List;

class ExpListHandler
{
    private ExpListHandler()
    {
        super();
    }

    static List<treeIR.Exp> translate(Frame f, SymbolTable e, Class c, Method m, List<syntaxtree.Exp> le)
    {
        List<treeIR.Exp> result = null, tail = null, nn;
        
        for ( ; le != null; le = le.tail )
        {
            treeIR.Exp aux = ExpHandler.translate(f, e, c, m, le.head).unEx();
            
            nn = new List<treeIR.Exp>(aux, null);
            
            if ( result == null)
                result = tail = nn;
            else
                tail = tail.tail = nn;
        }
        return result;
    }
}
