package typeCheking;


import symbolTable.*;
import syntaxtree.*;
import java.util.Vector;
import visitor.TypeVisitor;


public class CheckType implements TypeVisitor {

	private SymbolTable table;
	
	public CheckType(SymbolTable t){
		table = t;
	}
	
	@Override
	public Type visit(Program n) {
		n.m.accept(this);
		for(int i = 0; i < n.cl.size(); i++){
			n.cl.elementAt(i).accept(this);
		}
		return null;
	}

	@Override
	public Type visit(MainClass n) {
		table.beginScopeClass(Symbol.symbol(n.i1.s));
		table.beginScopeMethod(table.getMethodMain(Symbol.symbol(n.i1.s),Symbol.symbol("main")));
		n.s.accept(this);
		table.endScopeMethod();
		table.endScopeClass();
		return null;
	}

	@Override
	public Type visit(ClassDeclSimple n) {
		table.beginScopeClass(Symbol.symbol(n.i.s));
		for(int i = 0; i < n.vl.size(); i++){
			n.vl.elementAt(i).accept(this);
		}
		for(int i = 0; i < n.ml.size(); i++){
			n.ml.elementAt(i).accept(this);
		}
		table.endScopeClass();
		return null;
	}

	@Override
	public Type visit(ClassDeclExtends n) {
		table.beginScopeClass(Symbol.symbol(n.i.s));
		for(int i = 0; i < n.vl.size(); i++){
			n.vl.elementAt(i).accept(this);
		}
		for(int i = 0; i < n.ml.size(); i++){
			n.ml.elementAt(i).accept(this);
		}
		table.endScopeClass();		
		return null;
	}

	@Override
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

	@Override
	public Type visit(MethodDecl n) {
		Method meth = table.getMethod(n);
		table.beginScopeMethod(meth);
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
		table.endScopeMethod();
		return null;
	}

	@Override
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

	@Override
	public Type visit(IntArrayType n) {
		return  n;
	}
	
	@Override
	public Type visit(NullType n) {
		return n;
	}
	
	@Override
	public Type visit(StringArrayType n) {
		return n;
	}

	@Override
	public Type visit(BooleanType n) {
		return n;
	}

	@Override
	public Type visit(IntegerType n) {
		return n;
	}

	@Override
	public Type visit(IdentifierType n) {
		Symbol s = Symbol.symbol(n.s);
		Type type = table.lookUp(s);
		return type;
	}

	@Override
	public Type visit(Block n) {
		for(int i = 0; i < n.sl.size(); i++){
			n.sl.elementAt(i).accept(this);
		}
		return null;
	}

	@Override
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

	@Override
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

	@Override
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

	@Override
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

	@Override
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

	@Override
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

	@Override
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

	@Override
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

	@Override
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

	@Override
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

	@Override
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

	@Override
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

	@Override
	public Type visit(Call n) {
		Type type,t1;
		type = n.e.accept(this);
		n.typeClass = (IdentifierType)type;
		if(type==null){
			table.addError("Expressão invalida, referência a objeto esperada");
			return null;
		}
		if(!type.equals(new IdentifierType("x"))&&!type.equals(new This())){
			table.addError("Expressão invalida, referência a objeto esperada");
			return null;
		}
	
		Method meth;
		Vector<Method> meths = new Vector<Method>();
		
		if(type.equals(new This())) meth = table.getCurrentMethod();
		else meths = table.getMethodVectorClass((IdentifierType)type,Symbol.symbol(n.i.s));

		boolean exists = true;
		for(int i=0;i<meths.size();i++){
			exists = true;
			meth = meths.elementAt(i);
			for(int j=0; j<n.el.size(); j++){
				t1= n.el.elementAt(j).accept(this);
				if((n.el.size()!=meth.getPramsType().size())||(t1==null)){ 
					exists = false;
					break;
				}
				if(!t1.equals(meth.getPramsType().elementAt(j).t)){
					exists = false;
					break;
				}
			}
			if(exists) return meth.getReturnType();
		}
		table.addError("Metodo "+n.i.toString()+" inexistente na classe "+ ((IdentifierType)type).s);
		return null;
	}

	@Override
	public Type visit(IntegerLiteral n) {
		return new IntegerType();
	}

	@Override
	public Type visit(True n) {
		return new BooleanType();
	}

	@Override
	public Type visit(False n) {
		return new BooleanType();
	}

	@Override
	public Type visit(IdentifierExp n) {
		Symbol s = Symbol.symbol(n.s);
		Type type = table.lookUp(s);
		if(type == null){
			table.addError("Variavel não declarada : " + n.s);
			return type;
		}
		return type;
	}

	@Override
	public Type visit(This n) {
		if (table.getCurrentClass() == null) return null;
		return table.getCurrentClass().getType();

	}

	@Override
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

	@Override
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

	@Override
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

	@Override
	public Type visit(Identifier n) {
		Symbol s = Symbol.symbol(n.s);
		Type type = table.lookUp(s);
		if(type == null)
			return new IdentifierType(n.s);
		else return type;
	}

}
