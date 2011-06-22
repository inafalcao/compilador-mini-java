package visitor;

import syntaxtree.*;

public class TypeDepthFirstVisitor implements TypeVisitor {

  // MainClass m;
  // ClassDeclList cl;
  @Override
public Type visit(Program n) {
    n.m.accept(this);
    for ( int i = 0; i < n.cl.size(); i++ ) {
        n.cl.elementAt(i).accept(this);
    }
    return null;
  }
  
  // Identifier i1,i2;
  // Statement s;
  @Override
public Type visit(MainClass n) {
    n.i1.accept(this);
    n.i2.accept(this);
    n.s.accept(this);
    return null;
  }
  
  // Identifier i;
  // VarDeclList vl;
  // MethodDeclList ml;
  @Override
public Type visit(ClassDeclSimple n) {
    n.i.accept(this);
    for ( int i = 0; i < n.vl.size(); i++ ) {
        n.vl.elementAt(i).accept(this);
    }
    for ( int i = 0; i < n.ml.size(); i++ ) {
        n.ml.elementAt(i).accept(this);
    }
    return null;
  }
 
  // Identifier i;
  // Identifier j;
  // VarDeclList vl;
  // MethodDeclList ml;
  @Override
public Type visit(ClassDeclExtends n) {
    n.i.accept(this);
    n.j.accept(this);
    for ( int i = 0; i < n.vl.size(); i++ ) {
        n.vl.elementAt(i).accept(this);
    }
    for ( int i = 0; i < n.ml.size(); i++ ) {
        n.ml.elementAt(i).accept(this);
    }
    return null;
  }

  // Type t;
  // Identifier i;
  @Override
public Type visit(VarDecl n) {
    n.t.accept(this);
    n.i.accept(this);
    return null;
  }

  // Type t;
  // Identifier i;
  // FormalList fl;
  // VarDeclList vl;
  // StatementList sl;
  // Exp e;
  @Override
public Type visit(MethodDecl n) {
    n.t.accept(this);
    n.i.accept(this);
    for ( int i = 0; i < n.fl.size(); i++ ) {
        n.fl.elementAt(i).accept(this);
    }
    for ( int i = 0; i < n.vl.size(); i++ ) {
        n.vl.elementAt(i).accept(this);
    }
    for ( int i = 0; i < n.sl.size(); i++ ) {
        n.sl.elementAt(i).accept(this);
    }
    n.e.accept(this);
    return null;
  }

  // Type t;
  // Identifier i;
  @Override
public Type visit(Formal n) {
    n.t.accept(this);
    n.i.accept(this);
    return null;
  }

  @Override
public Type visit(IntArrayType n) {
    return null;
  }

  @Override
public Type visit(BooleanType n) {
    return null;
  }

  @Override
public Type visit(IntegerType n) {
    return null;
  }
  
  @Override
public Type visit(NullType n) {
	return null;
  }
  
  @Override
public Type visit(StringArrayType n) {
		return null;
	  }

  // String s;
  @Override
public Type visit(IdentifierType n) {
    return null;
  }

  // StatementList sl;
  @Override
public Type visit(Block n) {
    for ( int i = 0; i < n.sl.size(); i++ ) {
        n.sl.elementAt(i).accept(this);
    }
    return null;
  }

  // Exp e;
  // Statement s1,s2;
  @Override
public Type visit(If n) {
    n.e.accept(this);
    n.s1.accept(this);
    n.s2.accept(this);
    return null;
  }

  // Exp e;
  // Statement s;
  @Override
public Type visit(While n) {
    n.e.accept(this);
    n.s.accept(this);
    return null;
  }

  // Exp e;
  @Override
public Type visit(Print n) {
    n.e.accept(this);
    return null;
  }
  
  // Identifier i;
  // Exp e;
  @Override
public Type visit(Assign n) {
    n.i.accept(this);
    n.e.accept(this);
    return null;
  }

  // Identifier i;
  // Exp e1,e2;
  @Override
public Type visit(ArrayAssign n) {
    n.i.accept(this);
    n.e1.accept(this);
    n.e2.accept(this);
    return null;
  }

  // Exp e1,e2;
  @Override
public Type visit(And n) {
    n.e1.accept(this);
    n.e2.accept(this);
    return null;
  }

  // Exp e1,e2;
  @Override
public Type visit(LessThan n) {
    n.e1.accept(this);
    n.e2.accept(this);
    return null;
  }

  // Exp e1,e2;
  @Override
public Type visit(Plus n) {
    n.e1.accept(this);
    n.e2.accept(this);
    return null;
  }

  // Exp e1,e2;
  @Override
public Type visit(Minus n) {
    n.e1.accept(this);
    n.e2.accept(this);
    return null;
  }

  // Exp e1,e2;
  @Override
public Type visit(Times n) {
    n.e1.accept(this);
    n.e2.accept(this);
    return null;
  }

  // Exp e1,e2;
  @Override
public Type visit(ArrayLookup n) {
    n.e1.accept(this);
    n.e2.accept(this);
    return null;
  }

  // Exp e;
  @Override
public Type visit(ArrayLength n) {
    n.e.accept(this);
    return null;
  }

  // Exp e;
  // Identifier i;
  // ExpList el;
  @Override
public Type visit(Call n) {
    n.e.accept(this);
    n.i.accept(this);
    for ( int i = 0; i < n.el.size(); i++ ) {
        n.el.elementAt(i).accept(this);
    }
    return null;
  }

  // int i;
  @Override
public Type visit(IntegerLiteral n) {
    return null;
  }

  @Override
public Type visit(True n) {
    return null;
  }

  @Override
public Type visit(False n) {
    return null;
  }

  // String s;
  @Override
public Type visit(IdentifierExp n) {
    return null;
  }

  @Override
public Type visit(This n) {
    return null;
  }

  // Exp e;
  @Override
public Type visit(NewArray n) {
    n.e.accept(this);
    return null;
  }

  // Identifier i;
  @Override
public Type visit(NewObject n) {
    return null;
  }

  // Exp e;
  @Override
public Type visit(Not n) {
    n.e.accept(this);
    return null;
  }

  // String s;
  @Override
public Type visit(Identifier n) {
    return null;
  }
}