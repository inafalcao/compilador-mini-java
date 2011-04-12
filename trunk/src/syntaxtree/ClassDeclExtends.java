package syntaxtree;
import visitor.TranslateVisitor;
import visitor.Visitor;
import visitor.TypeVisitor;

public class ClassDeclExtends extends ClassDecl {
  public Identifier i;
  public Identifier j;
  public VarDeclList vl;  
  public MethodDeclList ml;
 
  public ClassDeclExtends(Identifier ai, Identifier aj, 
                  VarDeclList avl, MethodDeclList aml) {
    i=ai; j=aj; vl=avl; ml=aml;
  }

  public void accept(Visitor v) {
    v.visit(this);
  }

  public void accept(TypeVisitor v) {
    v.visit(this);
  }
  public activationRegister.util.Exp accept(TranslateVisitor v){
	  return v.visit(this);
  	}
}