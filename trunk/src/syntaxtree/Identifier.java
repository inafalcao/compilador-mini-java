package syntaxtree;
import visitor.Visitor;
import visitor.TypeVisitor;

public class Identifier {
	public int beginLine;
	
	public String s;

	public Identifier(String as) { 
		s=as;
	}

	public void accept(Visitor v) {
		v.visit(this);
	}

	public Type accept(TypeVisitor v) {
		return v.visit(this);
	}

	@Override
	public String toString(){
		return s;
	}
}