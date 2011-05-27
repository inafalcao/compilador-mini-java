package syntaxtree;
import visitor.Visitor;
import visitor.TypeVisitor;

public class IntegerType extends Type {
  @Override
public void accept(Visitor v) {
    v.visit(this);
  }

  @Override
public Type accept(TypeVisitor v) {
    return v.visit(this);
  }
}