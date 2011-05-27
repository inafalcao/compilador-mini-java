package translate;

import symbolTable.SymbolTable;
import symbolTable.Class;
import symbolTable.Method;
import symbolTable.Symbol;
import syntaxtree.*;
import visitor.VisitorAdapter;

class VTableBuilder extends VisitorAdapter
{
    private Frag result;
    private Frag tail;
    private SymbolTable env;
        
    private void addFrag(Frag f)
    {
        if ( result == null )
            result = tail = f;
        else
            tail = tail.next = f;
    }
    
    private VTableBuilder(SymbolTable e)
    {
        super();
        
        env = e;
        
        result = tail = null;
    }

    public static Frag build(SymbolTable e, Program p)
    {
        VTableBuilder b =  new VTableBuilder(e);
        
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
    
    private void BuildVTable(Class info)
    {
        String name = info.getName().toString();
        String[] indexes = new String[info.getMethods().size()];
        
        for ( int i = 0; i < info.getMethods().size(); i++ )
        {
            Method m = info.getMethods().elementAt(i);
            
            indexes[i] = m.toString();
        }
        
        VtableFrag frag = new VtableFrag(name, indexes);
        
        info.vtable = frag.name;
        
        this.addFrag(frag);
    }
        
    @Override
	public void visit(ClassDeclSimple node)
    {
        Symbol name = Symbol.symbol(node.i.s);
        
        Class info = env.getClass(name);
        
        this.BuildVTable(info);
    }
    
    @Override
	public void visit(ClassDeclExtends node)
    {
        Symbol name = Symbol.symbol(node.i.s);
        
        Class info = env.getClass(name);
        
        this.BuildVTable(info);
    }
}
