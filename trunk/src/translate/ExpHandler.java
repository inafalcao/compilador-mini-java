package translate;

import java.util.Vector;

import activationRegister.Frame;
import symbolTable.SymbolTable;
import symbolTable.Class;
import symbolTable.Method;
import symbolTable.Symbol;
import symbolTable.VarInfo;
import syntaxtree.And;
import syntaxtree.Type;
import syntaxtree.ArrayLength;
import syntaxtree.ArrayLookup;
import syntaxtree.Call;
import syntaxtree.False;
import syntaxtree.IdentifierExp;
import syntaxtree.IdentifierType;
import syntaxtree.IntegerLiteral;
import syntaxtree.LessThan;
import syntaxtree.Minus;
import syntaxtree.NewArray;
import syntaxtree.NewObject;
import syntaxtree.Not;
import syntaxtree.Plus;
import syntaxtree.This;
import syntaxtree.Times;
import syntaxtree.True;
import visitor.VisitorAdapter;
import activationRegister.temp.*;
import treeIR.*;
import typeCheking.CheckType;
import util.List;

class ExpHandler extends VisitorAdapter
{
    private Exp result;
    private typeCheking.CheckType checkTypeVisitor;
    private SymbolTable env;
    private Class cinfo;
    private Method minfo;
    private Frame frame;
    
    private treeIR.Exp getVariable(Symbol name)
    {
    	
        if ( minfo != null )
        {
        	if(minfo.getAccesses().containsKey(name))
        		return minfo.getAccesses().get(name).exp(new TEMP(frame.FP()));
        }
        
        // se esta aqui, a variavel eh um atributo de classe
        treeIR.Exp t = minfo.thisPtr.exp(new TEMP(frame.FP()));
        
        int offset = cinfo.getAttributeOffset(name);
        
        treeIR.Exp addr = new BINOP(BINOP.PLUS, t, new BINOP(BINOP.LSHIFT, new CONST(offset), new CONST(2)));
        
        treeIR.Exp fetch = new MEM(addr);
        
        return fetch;
    }
    
    private treeIR.Exp getMethod(treeIR.Exp obj, Call node, Class c, Method m, List<treeIR.Exp> args)
    {
        Temp thisPtr = new Temp();
        
        treeIR.Exp vtable = new MEM(new BINOP(BINOP.PLUS, new TEMP(thisPtr), new CONST(0)));
        
        int index = c.getMethodOffset(m);
        
        treeIR.Exp methodOffset = new BINOP(BINOP.LSHIFT, new CONST(index), new CONST(2));
        
        MEM methodAddr = new MEM(new BINOP(BINOP.PLUS, vtable, methodOffset));
        
        args = new List<treeIR.Exp>( new TEMP(thisPtr), args); 
        
        Temp rv = new Temp();
        
        List<treeIR.Exp> params = new List<treeIR.Exp>(new TEMP(thisPtr),
                new List<treeIR.Exp>(new CONST(node.line), null));
        
        treeIR.Stm as = new EXPSTM(frame.externalCall("assertPtr", params));
        
        return new ESEQ(
                new SEQ(new MOVE(new TEMP(thisPtr), obj),
                        new SEQ( as,
                                new SEQ( new EXPSTM( 
                                        new CALL(methodAddr, args) ) , 
                                        new MOVE( new TEMP(rv), 
                                                new TEMP(frame.RV()))))), 
               new TEMP(rv) );
    }
    
    private ExpHandler(Frame f, SymbolTable e, Class c, Method m)
    {
        super();
        
        frame = f;
        env = e;
        cinfo = c;
        minfo = m;
        
        result = null;
    }
    
    static Exp translate(Frame f, SymbolTable e, Class c, Method m, syntaxtree.Exp node)
    {
        ExpHandler h = new ExpHandler(f, e, c, m);
        
        node.accept(h);
        
        return h.result;
    }
    
    /*-----*/
    /* AND */
    /*-----*/
    @Override
	public void visit(And node)
    {
        Temp res = new Temp();
        
        Exp lhs = ExpHandler.translate(frame, env, cinfo, minfo, node.e1);
        Exp rhs = ExpHandler.translate(frame, env, cinfo, minfo, node.e2);
        
        Label f = new Label();        
        Label secondPart = new Label();
        Label join = new Label();
        Label t = new Label();
        
        treeIR.Exp total = new ESEQ(
                new SEQ(new CJUMP(CJUMP.NE, lhs.unEx(), new CONST(0), secondPart, f),
                        new SEQ(new LABEL(secondPart),
                                new SEQ(rhs.unCx(t, f),
                                        new SEQ(new LABEL(f),
                                                new SEQ(new MOVE(new TEMP(res), new CONST(0)),
                                                        new SEQ(new JUMP(join),
                                                                new SEQ(new LABEL(t),
                                                                        new SEQ(new MOVE(new TEMP(res), new CONST(1)),
                                                                                new LABEL(join))))))))), new TEMP(res));
        
        result = new Ex(total);
    }
    
    /*-------*/
    /* EQUAL */
    /*-------
    public void visit(Equal node)
    {
        Exp lhs = ExpHandler.translate(frame, env, cinfo, minfo, node.lhs);
        Exp rhs = ExpHandler.translate(frame, env, cinfo, minfo, node.rhs);
        
        int op = CJUMP.EQ;
        
        result = new RelCx(op, lhs, rhs);        
    }*/
    
    /*-----------*/
    /* LESS THAN */
    /*-----------*/
    @Override
	public void visit(LessThan node)
    {
        Exp lhs = ExpHandler.translate(frame, env, cinfo, minfo, node.e1);
        Exp rhs = ExpHandler.translate(frame, env, cinfo, minfo, node.e2);
        
        int op = CJUMP.LT;
        
        result = new RelCx(op, lhs, rhs);        
    }
    
    /*--------------------*/
    /* PLUS, MINUS, TIMES */
    /*--------------------*/
    @Override
	public void visit(Plus node)
    {
        Exp lhs = ExpHandler.translate(frame, env, cinfo, minfo, node.e1);
        Exp rhs = ExpHandler.translate(frame, env, cinfo, minfo, node.e2);
        
        treeIR.Exp cmp = new BINOP(BINOP.PLUS, lhs.unEx(), rhs.unEx());
        
        result = new Ex(cmp);
    }
    
    @Override
	public void visit(Minus node)
    {
        Exp lhs = ExpHandler.translate(frame, env, cinfo, minfo, node.e1);
        Exp rhs = ExpHandler.translate(frame, env, cinfo, minfo, node.e2);
        
        treeIR.Exp cmp = new BINOP(BINOP.MINUS, lhs.unEx(), rhs.unEx());
        
        result = new Ex(cmp);
    }
    
    @Override
	public void visit(Times node)
    {
        Exp lhs = ExpHandler.translate(frame, env, cinfo, minfo, node.e1);
        Exp rhs = ExpHandler.translate(frame, env, cinfo, minfo, node.e2);
        
        treeIR.Exp cmp = new BINOP(BINOP.MUL, lhs.unEx(), rhs.unEx());
        
        result = new Ex(cmp);
    }
    
    /*-----------------*/
    /* INTEGER LITERAL */
    /*-----------------*/
    @Override
	public void visit(IntegerLiteral node)
    {
        result = new Ex(new CONST(node.i));
    }
    
    /*-------------------*/
    /* TRUE, FALSE, THIS */
    /*-------------------*/
    @Override
	public void visit(True node)
    {
        result = new Ex(new CONST(1));
    }
    
    @Override
	public void visit(False node)
    {
        result = new Ex(new CONST(0));
    }
    
    @Override
	public void visit(This node)
    {
        result = new Ex(minfo.thisPtr.exp(new TEMP(frame.FP())));
    }
    
    /*-----------------------*/
    /* NEW OBJECT, NEW ARRAY */
    /*-----------------------*/
    @Override
	public void visit(NewObject node)
    {
        Symbol s = Symbol.symbol(node.i.s);
        
        Class c = env.getClass(s);
        
        // tamanho do objeto: numero de atributos + 1 palavras
        int tamanho = (c.attributesOrder.size() + 1) * frame.wordsize();
        
        Label vtableName = new Label("vtable_" + c.getName());
        
        List<treeIR.Exp> params = new List<treeIR.Exp>(new CONST(tamanho), 
                new List<treeIR.Exp>(new NAME(vtableName),null));
        
        treeIR.Exp e = frame.externalCall("newObject", params);
        
        result = new Ex( e );
    }
    
    @Override
	public void visit(NewArray node)
    {
        treeIR.Exp size = ExpHandler.translate(frame, env, cinfo, minfo, node.e).unEx();
        
        List<treeIR.Exp> params = new List<treeIR.Exp>(size, null);
        
        Temp t = new Temp();
        
        treeIR.Exp e = new ESEQ(new MOVE(new TEMP(t), frame.externalCall("newArray", params)),
                new TEMP(t));
        
        result = new Ex( e );
    }
    
    /*------------*/
    /* IDENTIFIER */
    /*------------*/
    @Override
	public void visit(IdentifierExp node)
    {
        Symbol name = Symbol.symbol(node.s);
        
        treeIR.Exp fetch = getVariable(name);
        
        result = new Ex(fetch);
    }
    
    /*----------------------------*/
    /* ARRAY LOOKUP, ARRAY LENGTH */
    /*----------------------------*/
    @Override
	public void visit(ArrayLength node)
    {
        treeIR.Exp array = ExpHandler.translate(frame, env, cinfo, minfo, node.e).unEx();
        
        Temp arr = new Temp();
        
        Temp size = new Temp();
        
        List<treeIR.Exp> params = new List<treeIR.Exp>(new TEMP(arr),
                new List<treeIR.Exp>(new CONST(node.line), null));
        
        
        treeIR.Stm move = new MOVE(new TEMP(arr), array);
        treeIR.Stm as = new EXPSTM(frame.externalCall("assertPtr", params));
        treeIR.Stm s = new MOVE(new TEMP(size),
                new BINOP(BINOP.PLUS, new TEMP(arr), new CONST(0)));
        
        treeIR.Stm aux = new SEQ(move, new SEQ(as,s));
        
        treeIR.Exp fetchSize = new ESEQ(aux, new MEM(new TEMP(size)));
        
        result = new Ex(fetchSize);
    }
    
    @Override
	public void visit(ArrayLookup node)
    {
        treeIR.Exp array = ExpHandler.translate(frame, env, cinfo, minfo, node.e1).unEx();
        treeIR.Exp index = ExpHandler.translate(frame, env, cinfo, minfo, node.e2).unEx();
        
        Temp arrayTemp = new Temp();
        Temp indexTemp = new Temp();
        
        List<treeIR.Exp> params = new List<treeIR.Exp>(new TEMP(arrayTemp),
                new List<treeIR.Exp>(new TEMP(indexTemp),
                        new List<treeIR.Exp>(new CONST(node.line),null)));
        
        List<treeIR.Exp> asParams = new List<treeIR.Exp>(new TEMP(arrayTemp),
                new List<treeIR.Exp>(new CONST(node.line), null));
        
        treeIR.Stm as = new EXPSTM(frame.externalCall("assertPtr", asParams));
        
        // tudo isso para fazer verificacao de limites...
        SEQ s = new SEQ(new MOVE(new TEMP(arrayTemp), array), 
                new SEQ(as,
                        new SEQ(new MOVE(new TEMP(indexTemp),index),
                                new EXPSTM(frame.externalCall("boundCheck",params)))));
        
        // faz o 'fetch' da posicao
        ESEQ fetch = new ESEQ(s, 
                new MEM(new BINOP(BINOP.PLUS, 
                        new TEMP(arrayTemp),
                        new BINOP(BINOP.LSHIFT, 
                                new BINOP(BINOP.PLUS, new TEMP(indexTemp), new CONST(1)),
                                new CONST(2)))));
        
        result = new Ex(fetch);
    }
    
    /*-----*/
    /* NOT */
    /*-----*/
    @Override
	public void visit(Not node)
    {
        Temp r = new Temp();
        Label t = new Label();
        Label f = new Label();
        Label join = new Label();
        
        SEQ s = new SEQ( ExpHandler.translate(frame, env, cinfo, minfo, node.e ).unCx(t,f),
                new SEQ(new LABEL(t),
                        new SEQ(new MOVE(new TEMP(r), new CONST(0)), 
                                new SEQ(new JUMP(join),
                                        new SEQ(new LABEL(f),
                                                new SEQ(new MOVE(new TEMP(r), new CONST(1)),
                                                        new LABEL(join)))))));
        
        ESEQ res = new ESEQ(s, new TEMP(r));
        
        result = new Ex(res);
    }
    
    /*------*/
    /* CALL */
    /*------*/
    @Override
	public void visit(Call node)
    {
        treeIR.Exp thisPtr = ExpHandler.translate(frame, env, cinfo, minfo, node.e).unEx();
        
        IdentifierType type = (IdentifierType)cinfo.getVariables().get(node.e);
        
        Class ci = env.getClass(Symbol.symbol(type.s));
        
        Vector<Method> meths = env.getMethodVectorClass(type,Symbol.symbol(node.i.s) );
        Method meth,mi;
        Type t1;
        boolean exists = true;
		for(int i=0;i<meths.size();i++){
			exists = true;
			meth = meths.elementAt(i);
			for(int j=0; j<node.el.size(); j++){
				t1= node.el.elementAt(j).accept(checkTypeVisitor);
				if((node.el.size()!=meth.getPramsType().size())||(t1==null)){ 
					exists = false;
					break;
				}
				if(!t1.equals(meth.getPramsType().elementAt(j).t)){
					exists = false;
					break;
				}
			}
			if(exists){
				mi = meth;
				break;
			}
		}
        
        List<treeIR.Exp> params = ExpListHandler.translate(frame, env, cinfo, minfo, node.el);
        
        result = new Ex(getMethod(thisPtr, node, ci, mi, params));
    }
}
