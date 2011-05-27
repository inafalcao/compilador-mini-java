package translate;

import symbolTable.SymbolTable;
import symbolTable.Class;
import symbolTable.Method;
import syntaxtree.Statement;
import treeIR.CONST;
import treeIR.EXPSTM;
import treeIR.SEQ;
import treeIR.Stm;
import util.List;
import activationRegister.Frame;

class StatementListHandler
{   
    private StatementListHandler()
    {
        super();
    }

    static List<Statement> getPrev(List<Statement> ls, List<Statement> actual)
    {
        for( ;ls != null; ls = ls.tail )
            if ( ls.tail == actual )
                return ls;
                
        return null;
    }
    
    static Exp translate(Frame f, SymbolTable e, Class c, Method m, List<Statement> ls)
    {
        Stm r = null;
        
        if ( ls == null)
            return new Nx(new EXPSTM(new CONST(0)));
        
        List<Statement> it = getPrev(ls, null);
        
        for( ; it != null; it = getPrev(ls, it) )
        {
            Stm s = StatementHandler.translate(f, e, c, m, it.head ).unNx();
            
            if ( r == null )
                r = s;
            else
                r = new SEQ(s, r);
        }
        
        return new Nx(r);
    }
}
