package typeCheking;


import symbolTable.*;
import syntaxtree.*;
import error.Error;
import visitor.TypeVisitor;


public class CheckType implements TypeVisitor {

	private SymbolTable table;
	
	public CheckType(SymbolTable t){
		table = t;
	}
	
	public Type visit(Program n) {
		n.m.accept(this);
		for(int i = 0; i < n.cl.size(); i++){
			n.cl.elementAt(i).accept(this);
		}
		return null;
	}

	public Type visit(MainClass n) {
		table.beginScope(Symbol.symbol(n.i1.s));
		table.beginScope(Symbol.symbol("main"));
		n.s.accept(this);
		table.endScope();
		table.endScope();
		return null;
	}

	public Type visit(ClassDeclSimple n) {
		table.beginScope(Symbol.symbol(n.i.s));
		for(int i = 0; i < n.vl.size(); i++){
			n.vl.elementAt(i).accept(this);
		}
		for(int i = 0; i < n.ml.size(); i++){
			n.ml.elementAt(i).accept(this);
		}
		table.endScope();
		return null;
	}

	public Type visit(ClassDeclExtends n) {
		table.beginScope(Symbol.symbol(n.i.s));
		for(int i = 0; i < n.vl.size(); i++){
			n.vl.elementAt(i).accept(this);
		}
		for(int i = 0; i < n.ml.size(); i++){
			n.ml.elementAt(i).accept(this);
		}
		table.endScope();		
		return null;
	}

	public Type visit(VarDecl n) {
		Symbol s = Symbol.symbol(n.i.s);
		Type type = n.t;
		if(!type.equals(new IntArrayType()) && !type.equals(new IntegerType()) && !type.equals(new BooleanType())){
			if(!table.hasType(Symbol.symbol(((IdentifierType)type).s))){
				Error.getInstance().addErro("Tipo da variavel "+s.toString()+" nao declarado", n.i.beginLine);
			}
		}
		return null;
	}

	public Type visit(MethodDecl n) {
		table.beginScope(Symbol.symbol(n.i.s));
		for(int i = 0; i < n.fl.size(); i++){
			n.fl.elementAt(i).accept(this);
		}
		for(int i = 0; i < n.vl.size(); i++){
			n.vl.elementAt(i).accept(this);
		}
		//checa retorno
		Type type = n.e.accept(this);
		if (type == null){
			Error.getInstance().addErro("Expressão invalida", n.e.beginLine);
		}else{
			if(!n.t.equals(type)) 
				Error.getInstance().addErro(n.i.s + ": tipo errado de retorno",n.i.beginLine);
		}
		
		for(int i = 0; i < n.sl.size(); i++){
			n.sl.elementAt(i).accept(this);
		}
		table.endScope();
		return null;
	}

	public Type visit(Formal n) {
		Symbol s = Symbol.symbol(n.i.s);
		Type type = n.t;
		
		if(!type.equals(new IntArrayType()) && !type.getClass().equals((new IntegerType()).getClass()) && !type.equals(new BooleanType())){
			if(!table.hasType(Symbol.symbol(((IdentifierType)type).s))){
				Error.getInstance().addErro("Tipo da variavel "+s.toString()+" nao declarado", n.i.beginLine);
			}
		}
		return null;
	}

	public Type visit(IntArrayType n) {
		return  n;
	}
	
	public Type visit(NullType n) {
		return n;
	}
	
	public Type visit(StringArrayType n) {
		return n;
	}

	public Type visit(BooleanType n) {
		return n;
	}

	public Type visit(IntegerType n) {
		return n;
	}

	public Type visit(IdentifierType n) {
		Symbol s = Symbol.symbol(n.s);
		Type type = table.lookUp(s);
		return type;
	}

	public Type visit(Block n) {
		for(int i = 0; i < n.sl.size(); i++){
			n.sl.elementAt(i).accept(this);
		}
		return null;
	}

	public Type visit(If n) {
		Type t = n.e.accept(this);
		if (t == null){
			Error.getInstance().addErro("Expressão invalida, retorno booleano esperado", n.e.beginLine);
			return null;
		}
		if(!(t.equals(new BooleanType())))
			Error.getInstance().addErro("Expressão invalida, retorno booleano esperado", n.e.beginLine);
		n.s1.accept(this);
		n.s2.accept(this);
		return null;
	}

	public Type visit(While n) {
		Type t = n.e.accept(this);
		if (t == null){
			Error.getInstance().addErro("Expressão invalida, retorno booleano esperado", n.e.beginLine);
			return null;
		}
		if(!(t.equals(new BooleanType())))
			Error.getInstance().addErro("Expressão invalida, retorno booleano esperado", n.e.beginLine);
		n.s.accept(this);
		return null;

	}

	public Type visit(Print n){
		Type t = n.e.accept(this);
		if (t == null){
			Error.getInstance().addErro("Expressão invalida, retorno inteiro ou boleano esperado", n.e.beginLine);
			return null;
		}
		if(!(t.equals(new BooleanType())) && !(t.equals(new IntegerType())))
			Error.getInstance().addErro("Expressão invalida, retorno inteiro ou boleano esperado", n.e.beginLine);
		return null;
	}

	public Type visit(Assign n) {
		Type typeLeft = table.lookUp(Symbol.symbol(n.i.s));
		if(typeLeft==null){
			Error.getInstance().addErro("Variavel " +n.i.s +" nao declarada", n.i.beginLine);
			//Error.getInstance().print();
			//System.out.println("SAINDO DO PROGRAMA");
			//System.exit(1);
			return null;
		}
		Type typeRight = n.e.accept(this);
		if (typeRight == null){
			Error.getInstance().addErro("Expressão invalida, retorno de valor esperado", n.e.beginLine);
			return null;
		}
		if(!typeLeft.equals(typeRight)){
			Error.getInstance().addErro("Atribuicao invalida, tipos conflitantes", n.i.beginLine);
		}	
		return null;
	}

	public Type visit(ArrayAssign n) {
		Type typeId, typeExp, typeRight;		
		typeId = n.i.accept(this);
		if (typeId == null){
			Error.getInstance().addErro("Expressão invalida, array esperado", n.i.beginLine);
			return null;
		}
		if(!(typeId.equals(new IntArrayType()))){
			Error.getInstance().addErro("Expressão invalida, array esperado", n.i.beginLine);
			return null;
		}
		typeExp = n.e1.accept(this);
		if (typeExp == null){
			Error.getInstance().addErro("Expressão invalida, valor inteiro esperado", n.e1.beginLine);
			return null;
		}
		
		if(!typeExp.equals(new IntegerType()))
			Error.getInstance().addErro("Expressão invalida, valor inteiro esperado", n.e1.beginLine);	
		typeRight = n.e2.accept(this);
		
		if (typeRight == null){
			Error.getInstance().addErro("Expressão invalida, valor inteiro esperado", n.e1.beginLine);
			return null;
		}
		
		if(!typeRight.equals(new IntegerType()))
			Error.getInstance().addErro("Atribuicao invalida, tipos conflitantes", n.i.beginLine);	
		return null;
	}

	public Type visit(And n) {
		Type type = n.e1.accept(this);
		if(type==null){ 
			Error.getInstance().addErro("Expressão invalida, booleano esperado", n.e1.beginLine);
			return null;
		}
		
		if(!type.equals(new BooleanType()))
			Error.getInstance().addErro("Expressão invalida, booleano esperado", n.e1.beginLine);
		type = n.e2.accept(this);
		if(type==null){
			Error.getInstance().addErro("Expressão invalida, booleano esperado", n.e1.beginLine);
			return null;
		}
		if(!type.equals(new BooleanType()))
			Error.getInstance().addErro("Expressão invalida, booleano esperado", n.e2.beginLine);
		return new BooleanType();
	}

	public Type visit(LessThan n) {
		Type type = n.e1.accept(this);
		if(type==null){ 
			Error.getInstance().addErro("Expressão invalida, inteiro esperado", n.e1.beginLine);
			return null;
		}
		if(!type.equals(new IntegerType()))
			Error.getInstance().addErro("Expressão invalida, inteiro esperado", n.e1.beginLine);
			
		type = n.e2.accept(this);
		if(type==null){
			Error.getInstance().addErro("Expressão invalida, inteiro esperado", n.e1.beginLine);	
			return null;
		}
		if(!type.equals( new IntegerType()))
			Error.getInstance().addErro("Expressão invalida, inteiro esperado", n.e2.beginLine);
		return new BooleanType();
	}

	public Type visit(Plus n) {
		Type type = n.e1.accept(this);
		if(type==null){ 
			Error.getInstance().addErro("Expressão invalida, inteiro esperado", n.e1.beginLine);
			return null;
		}
		if(!type.equals(new IntegerType()))
			Error.getInstance().addErro("Expressão invalida, inteiro esperado", n.e1.beginLine);
			
		type = n.e2.accept(this);
		if(type==null){ 
			Error.getInstance().addErro("Expressão invalida, inteiro esperado", n.e1.beginLine);	
			return null;
		}
		if(!type.equals( new IntegerType()))
			Error.getInstance().addErro("Expressão invalida, inteiro esperado", n.e2.beginLine);
		return new IntegerType();
	}

	public Type visit(Minus n) {
		Type type = n.e1.accept(this);
		if(type==null){
			Error.getInstance().addErro("Expressão invalida, inteiro esperado", n.e1.beginLine);
			return null;
		}
		if(!type.equals(new IntegerType()))
			Error.getInstance().addErro("Expressão invalida, inteiro esperado", n.e1.beginLine);
			
		type = n.e2.accept(this);
		if(type==null){
			Error.getInstance().addErro("Expressão invalida, inteiro esperado", n.e1.beginLine);
			return null;
		}		
		if(!type.equals( new IntegerType()))
			Error.getInstance().addErro("Expressão invalida, inteiro esperado", n.e2.beginLine);
		return new IntegerType();
	}

	public Type visit(Times n) {
		Type type = n.e1.accept(this);
		if(type==null) {
			Error.getInstance().addErro("Expressão invalida, inteiro esperado", n.e1.beginLine);
			return null;
		}
		if(!type.equals(new IntegerType()))
			Error.getInstance().addErro("Expressão invalida, inteiro esperado", n.e1.beginLine);
			
		type = n.e2.accept(this);
		if(type==null){
			Error.getInstance().addErro("Expressão invalida, inteiro esperado", n.e1.beginLine);	
			return null;
		}
		if(!type.equals( new IntegerType()))
			Error.getInstance().addErro("Expressão invalida, inteiro esperado", n.e2.beginLine);
		return new IntegerType();
    }

	public Type visit(ArrayLookup n) {
		Type typeLeft,typeRight;
		typeLeft = n.e1.accept(this);
		if(typeLeft==null){
			Error.getInstance().addErro("Expressão invalida, array esperado", n.e1.beginLine);
			return null;
		}			
		if(!typeLeft.equals(new IntArrayType()))
			Error.getInstance().addErro("Tipo Array esperado", n.e1.beginLine);
		
		typeRight = n.e2.accept(this);
		if(!typeRight.equals(new IntegerType()))
			Error.getInstance().addErro("Apenas inteiros podem indexar vetores", n.e2.beginLine);	
		return new IntegerType();
	}

	public Type visit(ArrayLength n) {
		Type type;
		type = n.e.accept(this);
		if(type==null){
			Error.getInstance().addErro("Expressão invalida, array esperado", n.e.beginLine);
			return null;
		}
		if(!type.equals(new IntArrayType()))
			Error.getInstance().addErro("Tipo Array esperado", n.e.beginLine);	
		return new IntegerType();
	}

	public Type visit(Call n) {
		Type type;
		type = n.e.accept(this);
		if(type.equals(new IntArrayType()) || type.equals(new IntegerType()) || type.equals(new BooleanType()) ){
			Error.getInstance().addErro("Expressão invalida, identificador esperado", n.i.beginLine);
			Error.getInstance().print();
			System.err.println("EXECUCAO DO COMPILADOR INTERROMPIDA");
			System.exit(1);
			return null;
		}		
		Method meth = table.getMethod(Symbol.symbol(type.toString()), Symbol.symbol(n.i.s));
		if(meth == null){
			Error.getInstance().addErro("Metodo não declarado : " + n.i.s, n.i.beginLine);
			//Error.getInstance().print();
			//System.err.println("EXECUCAO DO COMPILADOR INTERROMPIDA");
			//System.exit(1);
			return null;
		}
		if(n.el.size()<meth.getPramsType().length){
			Error.getInstance().addErro("Faltam parametros para o metodo : " + n.i.s, n.i.beginLine);
			return meth.getReturnType();
		}else if(n.el.size()>meth.getPramsType().length) {
			Error.getInstance().addErro("Excesso de parametros para o metodo : " + n.i.s, n.i.beginLine);
			return meth.getReturnType();
		}	
		for(int i=0; i<meth.getPramsType().length; i++){
			if(!n.el.elementAt(i).accept(this).equals(meth.getPramsType()[i])){
				Error.getInstance().addErro("Paramentro com conflito de tipos, tipo esperado " + meth.getPramsType()[i].toString(), n.i.beginLine);
			}
		}	
		return meth.getReturnType();
	}

	public Type visit(IntegerLiteral n) {
		return new IntegerType();
	}

	public Type visit(True n) {
		return new BooleanType();
	}

	public Type visit(False n) {
		return new BooleanType();
	}

	public Type visit(IdentifierExp n) {
		Symbol s = Symbol.symbol(n.s);
		Type type = table.lookUp(s);
		if(type == null){
			//if(table.isClass(s))return new IdentifierType(n.s);
			Error.getInstance().addErro("Tipo nao declarado : " + n.s, n.beginLine);
			return type;
			//Error.getInstance().print();
			//System.err.println("EXECUCAO DO COMPILAR INTERROMPIDA");
			//System.exit(1);
		}
		return type;
	}

	public Type visit(This n) {
		if (table.getCurrentClass() == null) return null;
		return table.getCurrentClass().getType();

	}

	public Type visit(NewArray n) {
		Type type = n.e.accept(this);
		if(type==null){
			Error.getInstance().addErro("Esperado tipo inteiro para essa expressão", n.beginLine);
			return null;
		}
		if(!type.equals(new IntegerType()))
			Error.getInstance().addErro("Esperado tipo inteiro para essa expressão", n.beginLine);
		
		return new IntArrayType();
	}

	public Type visit(NewObject n) {
		Type type = n.i.accept(this);
		if(type==null){
			Error.getInstance().addErro("Classe "+n.i.s+" nao declarada", n.i.beginLine);
			return type;
			//Error.getInstance().print();
			//System.err.println("EXECUCAO DO COMPILAR INTERROMPIDA");
			//System.exit(1);
		}		
		if(type.equals(new IntArrayType()) || type.equals(new IntegerType()) || type.equals(new BooleanType()) ){
			Error.getInstance().addErro("Expressao invalida, identificador de classe esperado", n.i.beginLine);
			//Error.getInstance().print();
			//System.err.println("EXECUCAO DO COMPILAR INTERROMPIDA");
			//System.exit(1);
		}	
		return type;
	}

	public Type visit(Not n) {
		Type type = n.e.accept(this);
		if(type==null){
			Error.getInstance().addErro("Expressão invalida, booleano esperado", n.e.beginLine);
			return null;
		}
		if(!type.equals(new BooleanType()))
			Error.getInstance().addErro("Expressão invalida, booleano esperado", n.e.beginLine);
		return new BooleanType();
	}

	public Type visit(Identifier n) {
		Symbol s = Symbol.symbol(n.s);
		Type type = table.lookUp(s);
		if(type == null)
			return new IdentifierType(n.s);
		else return type;
	}

}
