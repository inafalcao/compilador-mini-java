package syntaxtree;
import visitor.TranslateVisitor;
import visitor.Visitor;
import visitor.TypeVisitor;

public abstract class ClassDecl {
  public abstract void accept(Visitor v);
  public abstract void accept(TypeVisitor v);
  public abstract activationRegister.util.Exp accept(TranslateVisitor v);//criada para ser usada no Translate.java
}