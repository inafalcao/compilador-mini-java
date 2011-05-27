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

  @Override
public void accept(Visitor v) {
    v.visit(this);
  }

  @Override
public void accept(TypeVisitor v) {
    v.visit(this);
  }
  @Override
public translate.Exp accept(TranslateVisitor v){
	  return v.visit(this);
  	}
}