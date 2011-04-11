package symbolTable;

import java.util.HashMap;
import syntaxtree.Type;


public class Method {
	
	private HashMap<Symbol, Type> variables;

	private Type typeReturn;

	private Symbol name;

	private Type[] typeParam;
	
	public Method(Symbol m, Type returnType) {
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
	
	public Type[] getPramsType(){
		return typeParam;
	}

	public String toString(){
		return name.toString();
	}
}
