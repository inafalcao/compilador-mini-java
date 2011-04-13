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
				table.addError("Tipo da variavel "+s.toString()+" nao declarado");
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
			table.addError("Expressão invalida");
		}else{
			if(!n.t.equals(type)) 
				table.addError(n.i.s + ": tipo errado de retorno");
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
				table.addError("Tipo da variavel "+s.toString()+" nao declarado");
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
			table.addError("Expressão invalida, retorno booleano esperado");
			return null;
		}
		if(!(t.equals(new BooleanType())))
			table.addError("Expressão invalida, retorno booleano esperado");
		n.s1.accept(this);
		n.s2.accept(this);
		return null;
	}

	public Type visit(While n) {
		Type t = n.e.accept(this);
		if (t == null){
			table.addError("Expressão invalida, retorno booleano esperado");
			return null;
		}
		if(!(t.equals(new BooleanType())))
			table.addError("Expressão invalida, retorno booleano esperado");
		n.s.accept(this);
		return null;

	}

	public Type visit(Print n){
		Type t = n.e.accept(this);
		if (t == null){
			table.addError("Expressão invalida, retorno inteiro ou boleano esperado");
			return null;
		}
		if(!(t.equals(new BooleanType())) && !(t.equals(new IntegerType())))
			table.addError("Expressão invalida, retorno inteiro ou boleano esperado");
		return null;
	}

	public Type visit(Assign n) {
		Type typeLeft = table.lookUp(Symbol.symbol(n.i.s));
		if(typeLeft==null){
			table.addError("Variavel " +n.i.s +" nao declarada");
		}
		Type typeRight = n.e.accept(this);
		if (typeRight == null){
			table.addError("Expressão invalida, retorno de valor esperado");
		}
		
		if(typeLeft==null||typeRight==null) return null;
		if(!typeLeft.equals(typeRight)){
			table.addError("Atribuicao invalida, tipos conflitantes");
		}	
		return null;
	}

	public Type visit(ArrayAssign n) {
		Type typeId, typeExp, typeRight;		
		typeId = n.i.accept(this);
		if (typeId == null){
			table.addError("Expressão invalida, array esperado");
			return null;
		}
		if(!(typeId.equals(new IntArrayType()))){
			table.addError("Expressão invalida, array esperado");
			return null;
		}
		typeExp = n.e1.accept(this);
		if (typeExp == null){
			table.addError("Expressão invalida, valor inteiro esperado");
			return null;
		}
		
		if(!typeExp.equals(new IntegerType()))
			table.addError("Expressão invalida, valor inteiro esperado");	
		typeRight = n.e2.accept(this);
		
		if (typeRight == null){
			table.addError("Expressão invalida, valor inteiro esperado");
			return null;
		}
		
		if(!typeRight.equals(new IntegerType()))
			table.addError("Atribuicao invalida, tipos conflitantes");	
		return null;
	}

	public Type visit(And n) {
		Type type = n.e1.accept(this);
		if(type==null){ 
			table.addError("Expressão invalida, booleano esperado");
			return null;
		}
		
		if(!type.equals(new BooleanType()))
			table.addError("Expressão invalida, booleano esperado");
		type = n.e2.accept(this);
		if(type==null){
			table.addError("Expressão invalida, booleano esperado");
			return null;
		}
		if(!type.equals(new BooleanType()))
			table.addError("Expressão invalida, booleano esperado");
		return new BooleanType();
	}

	public Type visit(LessThan n) {
		Type type = n.e1.accept(this);
		if(type==null){ 
			table.addError("Expressão invalida, inteiro esperado");
			return null;
		}
		if(!type.equals(new IntegerType()))
			table.addError("Expressão invalida, inteiro esperado");
			
		type = n.e2.accept(this);
		if(type==null){
			table.addError("Expressão invalida, inteiro esperado");	
			return null;
		}
		if(!type.equals( new IntegerType()))
			table.addError("Expressão invalida, inteiro esperado");
		return new BooleanType();
	}

	public Type visit(Plus n) {
		Type type = n.e1.accept(this);
		if(type==null){ 
			table.addError("Expressão invalida, inteiro esperado");
			return null;
		}
		if(!type.equals(new IntegerType()))
			table.addError("Expressão invalida, inteiro esperado");
			
		type = n.e2.accept(this);
		if(type==null){ 
			table.addError("Expressão invalida, inteiro esperado");	
			return null;
		}
		if(!type.equals( new IntegerType()))
			table.addError("Expressão invalida, inteiro esperado");
		return new IntegerType();
	}

	public Type visit(Minus n) {
		Type type = n.e1.accept(this);
		if(type==null){
			table.addError("Expressão invalida, inteiro esperado");
			return null;
		}
		if(!type.equals(new IntegerType()))
			table.addError("Expressão invalida, inteiro esperado");
			
		type = n.e2.accept(this);
		if(type==null){
			table.addError("Expressão invalida, inteiro esperado");
			return null;
		}		
		if(!type.equals( new IntegerType()))
			table.addError("Expressão invalida, inteiro esperado");
		return new IntegerType();
	}

	public Type visit(Times n) {
		Type type = n.e1.accept(this);
		if(type==null) {
			table.addError("Expressão invalida, inteiro esperado");
			return null;
		}
		if(!type.equals(new IntegerType()))
			table.addError("Expressão invalida, inteiro esperado");
			
		type = n.e2.accept(this);
		if(type==null){
			table.addError("Expressão invalida, inteiro esperado");	
			return null;
		}
		if(!type.equals( new IntegerType()))
			table.addError("Expressão invalida, inteiro esperado");
		return new IntegerType();
    }

	public Type visit(ArrayLookup n) {
		Type typeLeft,typeRight;
		typeLeft = n.e1.accept(this);
		if(typeLeft==null){
			table.addError("Expressão invalida, array esperado");
			return null;
		}			
		if(!typeLeft.equals(new IntArrayType()))
			table.addError("Tipo Array esperado");
		
		typeRight = n.e2.accept(this);
		if(!typeRight.equals(new IntegerType()))
			table.addError("Apenas inteiros podem indexar vetores");	
		return new IntegerType();
	}

	public Type visit(ArrayLength n) {
		Type type;
		type = n.e.accept(this);
		if(type==null){
			table.addError("Expressão invalida, array esperado");
			return null;
		}
		if(!type.equals(new IntArrayType()))
			table.addError("Tipo Array esperado");	
		return new IntegerType();
	}

	public Type visit(Call n) {
		Type type,t1;
		type = n.e.accept(this);
		if(type==null){
			table.addError("Expressão invalida, referência a objeto esperada");
			return null;
		}
		if(!type.equals(new IdentifierType("x"))&&!type.equals(new This())){
			table.addError("Expressão invalida, referência a objeto esperada");
			return null;
		}
	
		Method meth;
		
		if(type.equals(new This())) meth = table.getMethod(Symbol.symbol(table.getCurrentClass().toString()), Symbol.symbol(n.i.s));
		else meth = table.getMethod(Symbol.symbol(((IdentifierType)type).s), Symbol.symbol(n.i.s));
		
		if(meth == null){
			table.addError("Metodo não declarado : " + n.i.s);
			return null;
		}
		
		
		if(n.el.size()<meth.getPramsType().size()){
			table.addError("Faltam parametros para o metodo : " + n.i.s);
		}else if(n.el.size()>meth.getPramsType().size()) {
			table.addError("Excesso de parametros para o metodo : " + n.i.s);
		}
		
		for(int i=0; i<n.el.size(); i++){
			t1= n.el.elementAt(i).accept(this);
			if(n.el.size()==meth.getPramsType().size()&&t1!=null && !t1.equals(meth.getPramsType().elementAt(i).t)){
			table.addError("Paramentro com conflito de tipos, tipo esperado " + meth.getPramsType().elementAt(i).t.toString());
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
			table.addError("Variavel não declarada : " + n.s);
			return type;
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
			table.addError("Esperado tipo inteiro para essa expressão");
			return null;
		}
		if(!type.equals(new IntegerType()))
			table.addError("Esperado tipo inteiro para essa expressão");
		
		return new IntArrayType();
	}

	public Type visit(NewObject n) {
		Type type = n.i.accept(this);
		if(type==null){
			table.addError("Classe "+n.i.s+" nao declarada");
			return type;
		}		
		if(type.equals(new IntArrayType()) || type.equals(new IntegerType()) || type.equals(new BooleanType()) ){
			table.addError("Expressao invalida, identificador de classe esperado");
		}	
		return type;
	}

	public Type visit(Not n) {
		Type type = n.e.accept(this);
		if(type==null){
			table.addError("Expressão invalida, booleano esperado");
			return null;
		}
		if(!type.equals(new BooleanType()))
			table.addError("Expressão invalida, booleano esperado");
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
