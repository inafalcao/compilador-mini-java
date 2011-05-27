package translate;

import activationRegister.Frame;
import symbolTable.SymbolTable;
import symbolTable.Class;
import symbolTable.Method;
import symbolTable.Symbol;
import symbolTable.VarInfo;
import syntaxtree.ArrayAssign;
import syntaxtree.Assign;
import syntaxtree.Block;
import syntaxtree.If;
import syntaxtree.Print;
import syntaxtree.Statement;
import visitor.VisitorAdapter;
import syntaxtree.While;
import activationRegister.temp.Label;
import activationRegister.temp.Temp;
import treeIR.BINOP;
import treeIR.CONST;
import treeIR.EXPSTM;
import treeIR.JUMP;
import treeIR.LABEL;
import treeIR.MEM;
import treeIR.MOVE;
import treeIR.SEQ;
import treeIR.Stm;
import treeIR.TEMP;
import util.List;

class StatementHandler extends VisitorAdapter
{
    private Exp result;
    
    private Class cinfo;
    
    private Method minfo;
    
    private SymbolTable env;
    
    private Frame frame;
    
    private treeIR.Exp getVariable(Symbol name)
    {
        VarInfo v = minfo.localsTable.get(name);
        
        if ( v != null )
            return v.access.exp(new TEMP(frame.FP()));
        
        v = minfo.formalsTable.get(name);
        
        if ( v != null )
            return v.access.exp(new TEMP(frame.FP()));
        
        // obtendo atributo de classe
        int idx = cinfo.getAttributeOffset(name);
        
        MEM node = new MEM( 
                new BINOP(BINOP.PLUS,
                        minfo.thisPtr.exp(new TEMP(frame.FP())),
                        new BINOP(BINOP.LSHIFT, new CONST(idx), new CONST(2))));
        
        return node;
    }
    
    private StatementHandler(Frame f, SymbolTable e, Class c, Method m)
    {
        super();
        
        frame = f;
        env = e;
        cinfo = c;
        minfo = m;
    }

    static Exp translate(Frame f, SymbolTable e, Class c, Method m, Statement stm)
    {
        StatementHandler h = new StatementHandler(f, e, c, m);
        
        stm.accept(h);
        
        return h.result;
    }
    
    /*----*/
    /* IF */
    /*----*/
    @Override
	public void visit(If node)
    {
        Label t = new Label();
        Label f = new Label();
        Label j = new Label();
        
        Exp cond = ExpHandler.translate(frame, env, cinfo, minfo, node.e);
        Exp th = StatementHandler.translate(frame, env, cinfo, minfo, node.s1);
        Exp el = node.s2 == null ? null : 
            StatementHandler.translate(frame, env, cinfo, minfo, node.s2);
        
        if ( el == null )
        {
            Stm r = new SEQ(cond.unCx(t,j),
                    new SEQ(new LABEL(t),
                            new SEQ(th.unNx(),
                                    new LABEL(j))));
            
            result = new Nx(r);
        }
        else
        {
            Stm r = new SEQ(cond.unCx(t,f),
                    new SEQ(new LABEL(t),
                            new SEQ(new SEQ(th.unNx(),new JUMP(j)),
                    new SEQ(new LABEL(f), 
                            new SEQ(el.unNx(),
                    new LABEL(j))))));
            
            result = new Nx(r);
        }
    }
    
    /*-------*/
    /* WHILE */
    /*-------*/
    @Override
	public void visit(While node)
    {
        Label test = new Label();
        Label done = new Label();
        Label body = new Label();
        
        Exp cond = ExpHandler.translate(frame, env, cinfo, minfo, node.e );
        Exp b = StatementHandler.translate(frame, env, cinfo, minfo, node.s);
        
        Stm r = new SEQ(new LABEL(test),
                  new SEQ(cond.unCx(body, done),
                          new SEQ(new LABEL(body),
                                  new SEQ(b.unNx(),
                                          new SEQ(new JUMP(test),
                                                  new LABEL(done))))));
        
        result = new Nx(r);
    }
    
    /*-------*/
    /* PRINT */
    /*-------*/
    @Override
	public void visit(Print node)
    {
        treeIR.Exp arg = ExpHandler.translate(frame, env, cinfo, minfo, node.e).unEx();
        
        List<treeIR.Exp> param = new List<treeIR.Exp>(arg, null);
        
        treeIR.Exp call = frame.externalCall("printInt", param);
        
        result = new Nx(new EXPSTM(call));
    }
    
    /*-------*/
    /* BLOCK */
    /*-------*/
    @Override
	public void visit(Block node)
    {
        result = StatementListHandler.translate(frame, env, cinfo, minfo, node.sl);
    }
    
    /*----------------------*/
    /* ASSIGN, ARRAY ASSIGN */
    /*----------------------*/
    @Override
	public void visit(Assign node)
    {        
        Symbol name = Symbol.symbol(node.i.s);
        
        treeIR.Exp var = getVariable(name);
        
        Exp e = ExpHandler.translate(frame, env, cinfo, minfo, node.e);
        
        Stm s = new MOVE(var, e.unEx());
        
        result = new Nx(s);
    }
    
    @Override
	public void visit(ArrayAssign node)
    {
        Symbol name = Symbol.symbol(node.i.s);
        
        treeIR.Exp var = getVariable(name);
        
        Temp index = new Temp();
        
        Temp arr = new Temp();
        
        treeIR.Exp idx = ExpHandler.translate(frame, env, cinfo, minfo, node.e1).unEx();
        treeIR.Exp val = ExpHandler.translate(frame, env, cinfo, minfo, node.e2).unEx();
        
        List<treeIR.Exp> params = new List<treeIR.Exp>(new TEMP(arr),
                new List<treeIR.Exp>(new TEMP(index),
                        new List<treeIR.Exp>(new CONST(node.line),null)));
        
        treeIR.Exp arrayIndex = new BINOP(BINOP.LSHIFT, new BINOP(BINOP.PLUS, idx, new CONST(1)), new CONST(2));
        
        treeIR.Exp access = new MEM(new BINOP(BINOP.PLUS, var, arrayIndex));
        
        List<treeIR.Exp> asParams = new List<treeIR.Exp>(new TEMP(arr),
                new List<treeIR.Exp>(new CONST(node.line), null));
        
        Stm as = new EXPSTM(frame.externalCall("assertPtr", asParams));
        
        Stm s = new SEQ( new MOVE(new TEMP(arr), var),
                        new SEQ(as,
                                new SEQ( new MOVE(new TEMP(index), idx),
                                        new SEQ( new EXPSTM(frame.externalCall("boundCheck", params)),
                                                new MOVE(access, val)))));
        
        result = new Nx(s);
    }
}
