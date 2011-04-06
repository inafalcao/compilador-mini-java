package symbolTable;

import java.util.ArrayList;

import syntaxtree.And;
import syntaxtree.ArrayAssign;
import syntaxtree.ArrayLength;
import syntaxtree.ArrayLookup;
import syntaxtree.Assign;
import syntaxtree.Block;
import syntaxtree.BooleanType;
import syntaxtree.Call;
import syntaxtree.ClassDeclExtends;
import syntaxtree.ClassDeclSimple;
import syntaxtree.False;
import syntaxtree.Formal;
import syntaxtree.Identifier;
import syntaxtree.IdentifierExp;
import syntaxtree.IdentifierType;
import syntaxtree.If;
import syntaxtree.IntArrayType;
import syntaxtree.IntegerLiteral;
import syntaxtree.IntegerType;
import syntaxtree.LessThan;
import syntaxtree.MainClass;
import syntaxtree.MethodDecl;
import syntaxtree.Minus;
import syntaxtree.NewArray;
import syntaxtree.NewObject;
import syntaxtree.Not;
import syntaxtree.Plus;
import syntaxtree.Print;
import syntaxtree.Program;
import syntaxtree.This;
import syntaxtree.Times;
import syntaxtree.True;
import syntaxtree.Type;
import syntaxtree.VarDecl;
import syntaxtree.While;
import visitor.Visitor;


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
		table.beginScope(clas);
		table.endScope();
	}

	//this methods are to visit each node and 
	public void visit(ClassDeclSimple n) {
		Symbol clas = Symbol.symbol(n.i.s);
		table.addClass(clas);
		table.beginScope(clas);
		for(int i = 0; i < n.vl.size(); i++){
			n.vl.elementAt(i).accept(this);
		}
		for(int j = 0; j < n.ml.size(); j++){
			n.ml.elementAt(j).accept(this);
		}
		table.endScope();
	}

	
	public void visit(ClassDeclExtends n) {
		Symbol clas = Symbol.symbol(n.i.s);
		table.addClass(clas);
		table.beginScope(clas);
		for(int i = 0; i < n.vl.size(); i++){
			n.vl.elementAt(i).accept(this);
		}
		for(int j = 0; j < n.ml.size(); j++){
			n.ml.elementAt(j).accept(this);
		}
		table.endScope();
	}

	public void visit(VarDecl n) {
		table.addVariable(Symbol.symbol(n.i.s), n.t);
	}

	public void visit(MethodDecl n) {
		Type returnType = n.t;
		Symbol name = Symbol.symbol(n.i.s);
		ArrayList<String> arguments = new ArrayList<String>();
		for(int i = 0; i < n.fl.size(); i++){
			arguments.add(n.fl.elementAt(i).t.toString());
			arguments.add(n.fl.elementAt(i).i.s);
		}
		table.addMethod(returnType, name, arguments);
		table.beginScope(name);
		for(int i = 0; i< n.vl.size(); i++){
			n.vl.elementAt(i).accept(this);
		}
		table.endScope();
	}

	public void visit(Formal n) {}

	public void visit(IntArrayType n) {}

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
