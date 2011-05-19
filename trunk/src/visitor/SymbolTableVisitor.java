package visitor;

import error.Error;
import symbolTable.Symbol;
import symbolTable.SymbolTable;
import symbolTable.Method;
import java.util.Vector;
import syntaxtree.*;


public class SymbolTableVisitor implements Visitor {
	

	private SymbolTable table;
	
	public SymbolTable buildSymbolTable(Program program){
		table = new SymbolTable();
		visit(program);
		return table;
	}

	/*
	 * here implements each visit in class Visitor.java
	 */	
	public void visit(Program n) {
		n.m.accept(this);
		for(int i =0; i< n.cl.size(); i++){
			n.cl.elementAt(i).accept(this);
		}
	}

	public void visit(MainClass n) {
		Symbol clas = Symbol.symbol(n.i1.s);
		table.addClass(clas);
		table.beginScopeClass(clas);
		Symbol name = Symbol.symbol("main");
		Method meth = table.addMethod(new NullType(), new Vector<Formal>(), name);
		table.beginScopeMethod(meth);
		table.addVariable(Symbol.symbol(n.i2.s), new StringArrayType());
		table.endScopeMethod();
		table.endScopeClass();
	}

	//this methods are to visit each node and 
	public void visit(ClassDeclSimple n) {
		// se já existir a classe, lança excecao
		if(Symbol.symbolCheck(n.i.s)){ 
			Error.getInstance().addErro("Redeclaração da classe "+ n.i.s+" omitida.",n.i.beginLine);
			return;
		}
		Symbol clas = Symbol.symbol(n.i.s);
		table.addClass(clas);
		table.beginScopeClass(clas);
		for(int i = 0; i < n.vl.size(); i++){
			n.vl.elementAt(i).accept(this);
		}
		for(int j = 0; j < n.ml.size(); j++){
			n.ml.elementAt(j).accept(this);
		}
		table.endScopeClass();
	}
	
	public void visit(ClassDeclExtends n) {
		Symbol clas = Symbol.symbol(n.i.s);
		Symbol ext = Symbol.symbol(n.j.s);
		table.addClass(clas,ext);
		table.beginScopeClass(clas);
		for(int i = 0; i < n.vl.size(); i++){
			n.vl.elementAt(i).accept(this);
		}
		for(int j = 0; j < n.ml.size(); j++){
			n.ml.elementAt(j).accept(this);
		}
		table.endScopeClass();
	}

	public void visit(VarDecl n) {
		table.addVariable(Symbol.symbol(n.i.s), n.t);
	}

	public void visit(MethodDecl n) {
		Type returnType = n.t;
		Symbol name = Symbol.symbol(n.i.s);
		Method meth = table.addMethod(returnType, n.fl.lista(),name);
		if(meth == null) return;
		table.beginScopeMethod(meth);
		for(int i = 0; i < n.fl.size(); i++){
			n.fl.elementAt(i).accept(this);
		}
		for(int i = 0; i< n.vl.size(); i++){
			n.vl.elementAt(i).accept(this);
		}
		table.endScopeMethod();
	}

	public void visit(Formal n) {
		table.addVariable(Symbol.symbol(n.i.s), n.t);
	}

	public void visit(IntArrayType n) {}
	
	public void visit(NullType n) {}
	
	public void visit(StringArrayType n) {}

	public void visit(BooleanType n) {}

	public void visit(IntegerType n) {}

	public void visit(IdentifierType n) {}

	public void visit(Block n) {}

	public void visit(If n) {}

	public void visit(While n) {}

	public void visit(Print n) {}

	public void visit(Assign n) {}

	public void visit(ArrayAssign n) {}

	public void visit(And n) {}

	public void visit(LessThan n) {}

	public void visit(Plus n) {	}

	public void visit(Minus n) {}

	public void visit(Times n) {}

	public void visit(ArrayLookup n) {}

	public void visit(ArrayLength n) {}

	public void visit(Call n) {}

	public void visit(IntegerLiteral n) {}

	public void visit(True n) {}

	public void visit(False n) {}

	public void visit(IdentifierExp n) {}

	public void visit(This n) {	}

	public void visit(NewArray n) {	}

	public void visit(NewObject n) {}

	public void visit(Not n) {}

	public void visit(Identifier n) {}

}
