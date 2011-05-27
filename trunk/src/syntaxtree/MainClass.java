package syntaxtree;
import visitor.TranslateVisitor;
import visitor.Visitor;
import visitor.TypeVisitor;

public class MainClass {
  public Identifier i1,i2;
  public Statement s;

  public MainClass(Identifier ai1, Identifier ai2, Statement as) {
    i1=ai1; i2=ai2; s=as;
  }

  public void accept(Visitor v) {
    v.visit(this);
  }

  public void accept(TypeVisitor v) {
    v.visit(this);
  }
  //criada para ser usada no Translate.java
  public translate.Exp accept(TranslateVisitor v){
	  return v.visit(this);
  }
}
