package syntaxtree;
import visitor.TranslateVisitor;
import visitor.Visitor;
import visitor.TypeVisitor;

public class True extends Exp {
  @Override
public void accept(Visitor v) {
    v.visit(this);
  }

  @Override
public Type accept(TypeVisitor v) {
    return v.visit(this);
  }
  
  @Override
public translate.Exp accept(TranslateVisitor v){
	  return v.visit(this);
  	}
}