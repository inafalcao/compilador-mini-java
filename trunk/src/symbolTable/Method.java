package symbolTable;

import java.util.HashMap;
import syntaxtree.*;
import java.util.Vector;


public class Method {
	
	private HashMap<Symbol, Type> variables;

	private Type typeReturn;

	private Symbol name;

	private Vector<Formal> typeParam;
	
	public Method(Symbol m, Vector<Formal> typeParamIn, Type returnType) {
		typeParam = typeParamIn;
		variables = new HashMap<Symbol, Type>();
		name = m;
		typeReturn = returnType;
	}

	public void addVariable(Symbol variable, Type type) {
		variables.put(variable, type);
	}

	public Type getReturnType() {
		return typeReturn;
	}

	public HashMap<Symbol, Type> getVariables() {
		return variables;
	}
	
	public Vector<Formal> getPramsType(){
		return typeParam;
	}

	public String toString(){
		return name.toString();
	}
}
