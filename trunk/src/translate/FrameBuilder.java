package translate;

import activationRegister.Access;
import activationRegister.Frame;
import symbolTable.*;
import symbolTable.Class;
import syntaxtree.ClassDeclExtends;
import syntaxtree.ClassDeclSimple;
import syntaxtree.MethodDecl;
import syntaxtree.Program;
import syntaxtree.VarDecl;
import visitor.VisitorAdapter;
import activationRegister.temp.Label;
import util.List;

class FrameBuilder extends VisitorAdapter
{
    private SymbolTable env;
    private Frame parent;
    private Class cinfo;
    private Method minfo;
    
    private FrameBuilder(Frame p, SymbolTable e)
    {
        super();
        
        env = e;
        parent = p;
    }
    
    static void translate(Frame p, SymbolTable e, Program pp)
    {
        FrameBuilder b = new FrameBuilder(p, e);
        
        pp.accept(b);
    }
    
    @Override
	public void visit(Program node)
    {
    	for(int i=0;i< node.cl.size();i++){
            node.cl.elementAt(i).accept(this);
    	}
    }
    
    @Override
	public void visit(ClassDeclSimple node)
    {       
    	cinfo = env.getClass(Symbol.symbol(node.i.s));
    	for(int i=0;i< node.ml.size();i++){
            node.ml.elementAt(i).accept(this);
    	}
    }
    
    @Override
	public void visit(ClassDeclExtends node)
    {
    	cinfo = env.getClass(Symbol.symbol(node.i.s));
    	for(int i=0;i< node.ml.size();i++){
            node.ml.elementAt(i).accept(this);
    	}
    }
    
    @Override
	public void visit(MethodDecl node)
    {
        // criando o frame
        List<Boolean> head = null, tail = null, nn;
        
        for(int i=0;i< node.fl.size();i++)
        {
            nn = new List<Boolean>(false, null);
            
            if ( head == null )
                head = tail = nn;
            else
                tail = tail.tail = nn;
        }
        
        // colocando o parametro 'this'
        head = new List<Boolean>(false, head);
        
        minfo = env.getMethod(node);
        
        Label methodName = new Label( minfo.toString() );
        
        Frame methodFrame = parent.newFrame(methodName, head);
                
        
        // facilitando a vida de muitas partes do compilador
        minfo.frame = methodFrame;
        minfo.thisPtr = methodFrame.formals.head;
        
        List<Access> f = methodFrame.formals.tail;
        
        for(int i=0;i< node.fl.size();i++)
        {
            minfo.getAccesses().put(Symbol.symbol(node.fl.elementAt(i).i.toString()),f.head);
        }
        
        // criando espaco para as variaveis locais
        for(int i=0;i< node.vl.size();i++){
            node.vl.elementAt(i).accept(this);
        }
    }
    
    @Override
	public void visit(VarDecl node)
    {
    	minfo.getAccesses().put(Symbol.symbol(node.i.s),minfo.frame.allocLocal(false));
    }
}
