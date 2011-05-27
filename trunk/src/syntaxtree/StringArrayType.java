package syntaxtree;
import visitor.Visitor;
import visitor.TypeVisitor;

public class StringArrayType extends Type {

  public StringArrayType() {
  }

  @Override
public void accept(Visitor v) {
    v.visit(this);
  }

  @Override
public Type accept(TypeVisitor v) {
    return v.visit(this);
  }
}