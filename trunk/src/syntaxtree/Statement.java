package syntaxtree;
import visitor.TranslateVisitor;
import visitor.Visitor;
import visitor.TypeVisitor;

public abstract class Statement {
  public int line=0;
  public abstract void accept(Visitor v);
  public abstract Type accept(TypeVisitor v);
  public abstract translate.Exp accept(TranslateVisitor v);//usada em Translate.java
}