package symbolTable;

import java.util.HashMap;
import error.Error;
import syntaxtree.*;

public class Class {
	

	private HashMap<Symbol, Type> variables;

	private HashMap<Symbol, Method> methods;

	private Symbol extender;

	private Symbol name;

	private Type type;

	
	public Class(Symbol name){
		this.name = name;
		type = new IdentifierType(name.toString());
		variables = new HashMap<Symbol, Type>();
		methods = new HashMap<Symbol, Method>();
		extender = null;
	}
	
	public Class(Symbol name, Symbol ext){
		this.name = name;
		type = new IdentifierType(name.toString());
		variables = new HashMap<Symbol, Type>();
		methods = new HashMap<Symbol, Method>();
		extender =  null;
		if (name.equals(ext)) {
			extender = ext;
			Error.getInstance().addErro("Classe: "+ name.toString()+" esta estendendo ela mesma");
		}//end if
	}
	
	
	public boolean compare(Class s){
		return this.name.equals(s.name);
	}

	
	public void addMethod(Symbol m, Type returnType) {
		Method mt = new Method(m, returnType);
		methods.put(m, mt);
	}

	public void addVariable(Symbol variable, Type type2) {
		variables.put(variable, type2);
	}

	public Method getMethod(Symbol meth) {
		return methods.get(meth);
	}

	public Symbol getName() {
		return name;
	}
	
	public HashMap<Symbol, Type> getVariables(){
		return variables;
	}

	public HashMap<Symbol, Method> getMethods() {
		return methods;
	}

	public Symbol getExtender() {
		return extender;
	}
	
	public Type getType(){
		return type;
	}
	
	public int getNumberOfVariables(){
		return variables.size();
	}
	
}
