package symbolTable;

import java.util.HashMap;
import java.util.Vector;
import error.Error;
import syntaxtree.*;
import activationRegister.temp.Label;

public class Class {
	public Label vtable;

	private HashMap<Symbol, Type> variables;

	private Vector<Method> methods;
	private Vector<Symbol> methodsSymbols; 

	private Symbol extender;

	private Symbol name;

	private Type type;
	
	public Vector<Symbol> attributesOrder;

	
	public Class(Symbol name){
		this.name = name;
		type = new IdentifierType(name.toString());
		variables = new HashMap<Symbol, Type>();
		methods = new Vector<Method>();
		methodsSymbols = new Vector<Symbol>();
		attributesOrder = new Vector<Symbol>();
		extender = null;
	}
	
	public Class(Symbol name, Symbol ext){
		this.name = name;
		type = new IdentifierType(name.toString());
		variables = new HashMap<Symbol, Type>();
		methods = new Vector<Method>();
		methodsSymbols = new Vector<Symbol>();
		extender =  ext;
		if (name.equals(ext)) {
			extender = null;
			Error.getInstance().addErro("Classe: "+ name.toString()+" esta estendendo ela mesma");
		}//end if
	}
	
	
	public boolean compare(Class s){
		return this.name.equals(s.name);
	}

	
	public Method addMethod(Symbol m, Vector<Formal> listIn, Type returnType) {
		Method mt = new Method(m, listIn, returnType);
		methods.add(mt);
		methodsSymbols.add(m);
		mt.index = methodsSymbols.size()-1;
		return mt;
	}

	public void addVariable(Symbol variable, Type type2) {
		variables.put(variable, type2);
		attributesOrder.add(variable);
	}
	
	public Vector<Method> getMethodVector(Symbol meth) {
		Vector<Method> meths = new Vector<Method>();
		for(int i =0; i< methodsSymbols.size();i++){
			if(meth.equals(methodsSymbols.elementAt(i))) meths.add(methods.elementAt(i));
		}
		return meths;
	}

	public Method getMethodMain(Symbol meth) {
		for(int i =0; i< methodsSymbols.size();i++){
			if(meth.equals(methodsSymbols.elementAt(i))) return methods.elementAt(i);
		}
		return null;
	}

	public Symbol getName() {
		return name;
	}
	
	public HashMap<Symbol, Type> getVariables(){
		return variables;
	}

	public Vector<Method> getMethods() {
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
	
	public int getAttributeOffset(Symbol name)
    {
        // pega a ultima declaracao do simbolo.
        return attributesOrder.lastIndexOf(name) + 1; // adicionando 1 para considerar a vtable
    }
	
	public int getMethodOffset(Method m)
    {
        return m.index;
    }
}
