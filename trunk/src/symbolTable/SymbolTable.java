package symbolTable;

import java.util.ArrayList;
import error.Error;

import java.util.HashMap;
import syntaxtree.Type;


public class SymbolTable {

	
	private HashMap<Symbol, Class> classes;

	private Class currentClass = null;

	private Method currentMethod = null;
	

	public SymbolTable(){
		classes = new HashMap<Symbol, Class>();
	}

	public void addClass(Symbol clas){
		if(classes.containsKey(clas))System.out.println("Classe:"+clas.toString()+" ja declarada");
		else{
			Class cl = new Class(clas);
			classes.put(clas, cl);
		}
	}
	
	
	public void addClass(Symbol clas, Symbol extenders){
		if(classes.containsKey(clas))System.out.println("Classe:"+clas.toString()+" ja declarada");
		else{
			Class cl = new Class(clas, extenders);
			classes.put(clas, cl);
		}
	}
	
	public void addMethod(Type returnType, Symbol method, ArrayList<String> arguments){
		if(currentClass.getMethods().containsKey(method))System.out.println("Metodo:"+method.toString()+" jï¿½ declarada");
		else
			currentClass.addMethod(method,returnType, arguments);
	}
	
	
	public void addVariable(Symbol variable , Type type){
		if(currentMethod == null){
			if(currentClass.getVariables().containsKey(variable))System.out.println("Atributo :"+variable.toString()+" jï¿½ declarada");
			else 
				currentClass.addVariable(variable, type);
		}else{
			if(currentMethod.getVariables().containsKey(variable))System.out.println("Variavel:"+variable.toString()+" jï¿½ declarada nesse escopo");
			else
				currentMethod.addVariable(variable, type);
		}
	}

	
	public void beginScope(Symbol elem){
		if(currentClass == null){
			currentClass = classes.get(elem);
			currentMethod = null;
		}else{
			currentMethod = classes.get(currentClass.getName()).getMethod(elem);
		}
	}
	
	
	public void endScope(){
		if(currentMethod == null)currentClass = null;
		else currentMethod = null;
	}
	
	
	public Type lookUp(Symbol symbol){
		if(currentMethod == null){
			if(currentClass != null){
				if(currentClass.getVariables().containsKey(symbol))
					return currentClass.getVariables().get(symbol);
				else
					return null;
			}else return null;
		}else{
			if(currentMethod.getVariables().containsKey(symbol)){
				return currentMethod.getVariables().get(symbol);
			}
			else{
				if(currentClass.getVariables().containsKey(symbol))
					return currentClass.getVariables().get(symbol);
				else return lookUpForExtender(currentClass.getExtender(), symbol);
			}
		}
	}
	
	
	private Type lookUpForExtender(Symbol extender, Symbol symbol) {
		if(extender==null) return null;
		if(!classes.containsKey(extender))return null;
		Class clas = classes.get(extender);
		if(clas.getVariables().containsKey(symbol))//caso simbolo de uma atributo da classe
			return clas.getVariables().get(symbol);
		if(clas.getMethods().containsKey(symbol)) //caso symbol de um metodo
			return clas.getMethod(symbol).getReturnType();
		return lookUpForExtender(clas.getExtender(), symbol);
	}

	
	public void print() {
		Symbol[] cls = classes.keySet().toArray(new Symbol[1]);
		for(int i = 0; i < classes.size(); i++){
			System.out.println("##################################");
			System.out.println("Classe: " + cls[i].toString());
			System.out.println("Atributos:");
			Class cl = classes.get(cls[i]);
			Symbol[] var = cl.getVariables().keySet().toArray(new Symbol[1]);
			for(int j = 0; j < var.length; j++){
				if(var[j]!= null)System.out.println( var[j].toString()+" "+ cl.getVariables().get(var[j]).toString());
			}
			System.out.println("Metodos:");
			Symbol[] m = cl.getMethods().keySet().toArray(new Symbol[1]);
			for(int j = 0; j < m.length; j++){
				if(m[j] != null){
					Method mt = cl.getMethod(m[j]);
					System.out.println("Nome: "+ m[j].toString());
					System.out.println("Retorno: "+ mt.getReturnType());
					System.out.println("Variaveis:");
					Symbol[] var2 = mt.getVariables().keySet().toArray(new Symbol[1]);
					for(int k = 0; k < var2.length; k++){
						if(var2[k]!= null)System.out.println( var2[k].toString()+" "+ mt.getVariables().get(var2[k]).toString());
					}
				}
			}
			System.out.println("##################################");
		}
	}

	
	public boolean lookUp(Symbol s, Type type) {
		return lookUp(s).equals(type);
	}

	
	public Class getCurrentClass() {
		return currentClass;
	}
	
	
	public Class getClass(Symbol s){
		return classes.get(s);
	}
	
	public Method getMethod(Symbol classSymbol, Symbol methSymbol){
		if(classSymbol==null){
			Error.getInstance().addErro("Simbolo dessa classe não existe");
			return null;
		}
		if(!classes.containsKey(classSymbol)){
			return null;
		}
		Class clas = classes.get(classSymbol);
		if(clas.getMethods().containsKey(methSymbol)){  //this is in case of symbol's method
			return clas.getMethod(methSymbol);
		}
		return getMethod(clas.getExtender(), methSymbol);
	}
	public boolean isClass(Symbol s) {
		return classes.containsKey(s);
	}

	public boolean hasType(Type type) {
		return classes.containsKey(type);
	}
	
}//end 
