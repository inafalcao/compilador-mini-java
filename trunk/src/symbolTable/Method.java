package symbolTable;

import java.util.ArrayList;
import java.util.HashMap;
import syntaxtree.IdentifierType;
import syntaxtree.Type;


public class Method {
	
	
	private HashMap<Symbol, Type> variables;

	private Type typeReturn;

	private Symbol name;

	private Type[] typeParam;

	private Symbol[] param;
	
	public Method(Symbol m, Type returnType, ArrayList<String> arguments) {
		variables = new HashMap<Symbol, Type>();
		name = m;
		typeReturn = returnType;
		typeParam = new Type[arguments.size()/2];
		param = new Symbol[arguments.size()/2];
		for(int i = 0; i < arguments.size(); i=i+2){
			typeParam[i/2] = new IdentifierType(Symbol.symbol(arguments.get(i)).toString());
			param[i/2] = Symbol.symbol(arguments.get(i+1));
			variables.put(param[i/2], typeParam[i/2]);
		}
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
