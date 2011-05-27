package translate;

import symbolTable.SymbolTable;
import symbolTable.Class;
import symbolTable.Method;
import symbolTable.Symbol;
import syntaxtree.MethodDecl;
import treeIR.ESEQ;

class MethodDeclHandler
{
    private MethodDeclHandler()
    {
        super();
    }

    static public treeIR.Exp translate(SymbolTable e, Class c, MethodDecl m)
    {
        Symbol name = Symbol.symbol(m.i.s);
        
        Method i = e.getMethod(m);
        
        Exp r = StatementListHandler.translate(i.frame, e, c, i, m.sl);
        Exp v = ExpHandler.translate(i.frame, e, c, i, m.e);
        
        //Label label = new Label(i.decorateName());
        
        //tree.Exp result = new ESEQ(new SEQ(new LABEL(label), r.unNx()),v.unEx());
        
        return new ESEQ(r.unNx(), v.unEx());
    }
}
