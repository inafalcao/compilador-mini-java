package symbolTable;

import java.util.HashMap;
import syntaxtree.*;
import activationRegister.Frame;
import activationRegister.Access;
import java.util.Vector;


public class Method {
	public Frame frame;
	
	public Access thisPtr;
	
	private HashMap<Symbol, Type> variables;
	private HashMap<Symbol, Access> accesses;

	private Type typeReturn;

	private Symbol name;

	private Vector<Formal> typeParam;
	
	public Method(Symbol m, Vector<Formal> typeParamIn, Type returnType) {
		typeParam = typeParamIn;
		variables = new HashMap<Symbol, Type>();
		accesses = new HashMap<Symbol, Access>();
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
	
	public HashMap<Symbol, Access> getAccesses() {
		return accesses;
	}
	
	public Vector<Formal> getPramsType(){
		return typeParam;
	}

	@Override
	public String toString(){
		return name.toString();
	}
}
