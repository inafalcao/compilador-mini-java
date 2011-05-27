package translate;

import activationRegister.Frame;
import symbolTable.SymbolTable;
import symbolTable.Class;
import symbolTable.Method;
import symbolTable.Symbol;
import syntaxtree.ClassDeclExtends;
import syntaxtree.ClassDeclSimple;
import syntaxtree.MainClass;
import syntaxtree.Program;
import visitor.VisitorAdapter;
import activationRegister.temp.Label;
import treeIR.CONST;
import treeIR.ESEQ;
import treeIR.Stm;
import util.List;

class IRBuilder extends VisitorAdapter
{
    private SymbolTable env;
    private Frame frame;
    
    private Frag result;
    private Frag tail;
    
    private Class cinfo;
    private Method minfo;
    
    private IRBuilder(SymbolTable e, Frame f)
    {
        super();
        
        env = e;
        frame = f;
        
        result = tail = null;
    }

    private void addFrag(Frag f)
    {
        if ( result == null )
            result = tail = f;
        else
            tail = tail.next = f;
    }
    
    static Frag build(SymbolTable e, Program p, Frame f)
    {
        IRBuilder b = new IRBuilder(e, f);
        
        Label l = new Label("_minijava_main_1");
        b.frame = f.newFrame(l, null);
        
        p.accept(b);
        
        return b.result;
    }
    
    @Override
	public void visit(Program p)
    {
        p.m.accept(this);
        
        for(int i=0; i< p.cl.size();i++)
            p.cl.elementAt(i).accept(this);
    }
    
    @Override
	public void visit(MainClass node)
    {
        Stm s = StatementHandler.translate(frame, env, null, null, node.s).unNx();
        
        //Label l = new Label("_minijava_main$1");
        List<treeIR.Exp> param = new List<treeIR.Exp>(new CONST(0),null);
        treeIR.Exp r = new ESEQ( s, frame.externalCall("minijavaExit", param));
        
        s = frame.procEntryExit1(r);
        
        ProcFrag f = new ProcFrag(s, frame);
        
        addFrag(f);
    }
    
    @Override
	public void visit(ClassDeclSimple node)
    {
        cinfo = env.getClass(Symbol.symbol(node.i.s));
        for(int i=0;i<node.ml.size();i++)
        {
            minfo = env.getMethod(node.ml.elementAt(i));
            treeIR.Exp body = MethodDeclHandler.translate(env, cinfo, node.ml.elementAt(i));
            
            Stm b = minfo.frame.procEntryExit1(body);
            ProcFrag f = new ProcFrag(b, minfo.frame);
            addFrag(f);
        }
    }
    
    @Override
	public void visit(ClassDeclExtends node)
    {
        cinfo = env.getClass(Symbol.symbol(node.i.s));
        for(int i=0;i<node.ml.size();i++)
        {
        	minfo = env.getMethod(node.ml.elementAt(i));
        	treeIR.Exp body = MethodDeclHandler.translate(env, cinfo, node.ml.elementAt(i));
            
            Stm b = minfo.frame.procEntryExit1(body);
            ProcFrag f = new ProcFrag(b, minfo.frame);
            addFrag(f);
        }
    }
}
