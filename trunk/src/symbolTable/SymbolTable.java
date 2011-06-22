package symbolTable;

import error.Error;

import java.util.HashMap;
import java.util.Vector;

import syntaxtree.Formal;
import syntaxtree.IdentifierType;
import syntaxtree.Type;
import syntaxtree.MethodDecl;
import treeIR.Exp;


public class SymbolTable {

	private static HashMap<Symbol, Class> classes;

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
		if(classes.containsKey(clas))System.out.println("Classe: "+clas.toString()+" ja declarada");
		else{
			Class cl = new Class(clas, extenders);
			classes.put(clas, cl);
		}
	}

	public Vector<Method> getMethodVectorClass(IdentifierType type,Symbol meth){
		Class iter = classes.get(Symbol.symbol(type.s));
		Vector<Method> aux;
		Vector<Method> meths = new Vector<Method>();
		while(iter!=null){
			aux = iter.getMethodVector(meth);
			for(int i=0;i<aux.size();i++){
				meths.add(aux.elementAt(i));
			}
			iter = classes.get(iter.getExtender());
		}
		return meths;
	}

	public Vector<Method> getMethodVector(Symbol meth){
		Class iter = currentClass;
		Vector<Method> aux;
		Vector<Method> meths = new Vector<Method>();
		while(iter!=null){
			aux = iter.getMethodVector(meth);
			for(int i=0;i<aux.size();i++){
				meths.add(aux.elementAt(i));
			}
			iter = classes.get(iter.getExtender());
		}
		return meths;
	}
	
	public Method getMethod(MethodDecl m){
		boolean exists = false;
		Vector <Method> meths = this.getMethodVector(Symbol.symbol(m.i.toString()));
		for(int j = 0; j<meths.size();j++){
			exists = true;
			Method meth = meths.elementAt(j);
			Type t1;
			if((m.fl.size()==meth.getPramsType().size())&&(meth.getReturnType().equals(m.t))){
				for(int i=0; i<meth.getPramsType().size(); i++){
					t1= m.fl.elementAt(i).t;
					if(!t1.equals(meth.getPramsType().elementAt(i).t)){
						exists = false;
						break;
					}
				}
			}else exists = false;
			if(exists) return meth;
		}
		return null;
	}
	
	public Method addMethod(Type returnType, Vector<Formal> listIn, Symbol method){
		boolean exists = false;
		Vector <Method> meths = this.getMethodVector(method);
		for(int j = 0; j<meths.size();j++){
			exists = true;
			Method meth = meths.elementAt(j);
			Type t1;
			if(listIn.size()==meth.getPramsType().size()){
				for(int i=0; i<meth.getPramsType().size(); i++){
					t1= listIn.elementAt(i).t;
					if(!t1.equals(meth.getPramsType().elementAt(i).t)){
						exists = false;
						break;
					}
				}
			}else exists = false;
			if(exists) break;
		}
		if(!exists){
			return currentClass.addMethod(method, listIn, returnType);
		}
		System.out.println("Metodo "+method.toString()+" ja declarado");
		return null;
	}
	
	
	public void addVariable(Symbol variable , Type type){
		if(currentMethod == null){
			if(currentClass.getVariables().containsKey(variable))System.out.println("Atributo :"+variable.toString()+" ja declarada");
			else 
				currentClass.addVariable(variable, type);
		}else{
			if(currentMethod.getVariables().containsKey(variable))System.out.println("Variavel:"+variable.toString()+" ja declarada nesse escopo");
			else
				currentMethod.addVariable(variable, type);
		}
	}
	
	public void addVariableAccess(Symbol variable , activationRegister.util.Exp exp){
		if(currentMethod == null){
			if(currentClass.getAccesses().containsKey(variable))System.out.println("Access de :"+variable.toString()+" ja declarada");
			else 
				currentClass.addVariableAccess(variable, exp);
		}else{
			if(currentMethod.getAccesses().containsKey(variable))System.out.println("Access de:"+variable.toString()+" ja declarada nesse escopo");
			else
				currentMethod.addVariableAccess(variable, exp);
		}
	}

	
	public void beginScopeClass(Symbol elem){
		if(currentClass == null){
			currentClass = classes.get(elem);
			currentMethod = null;
		}
	}
	
	
	public void endScopeClass(){
		if(currentMethod == null)currentClass = null;
	}
	
	public void beginScopeMethod(Method meth){
		if((currentClass != null)&&(currentMethod == null)){
			currentMethod = meth;
		}
	}
	
	public Method getCurrentMethod() {
		return currentMethod;
	}
	
	public void endScopeMethod(){
		if(currentClass != null) currentMethod = null;
	}
	
	public activationRegister.util.Exp lookUpAccess(Symbol symbol){
		if(currentMethod == null){
			if(currentClass != null){
				if(currentClass.getAccesses().containsKey(symbol))
					return currentClass.getAccesses().get(symbol);
				else return lookUpForExtenderAccess(currentClass.getExtender(), symbol);
			}else return null;
		}else{
			if(currentMethod.getAccesses().containsKey(symbol)){
				return currentMethod.getAccesses().get(symbol);
			}
			else{
				if(currentClass.getVariables().containsKey(symbol))
					return currentClass.getAccesses().get(symbol);
				else return lookUpForExtenderAccess(currentClass.getExtender(), symbol);
			}
		}
	}
	
	
	private activationRegister.util.Exp lookUpForExtenderAccess(Symbol extender, Symbol symbol) {
		if(extender==null) return null;
		if(!classes.containsKey(extender))return null;
		Class clas = classes.get(extender);
		if(clas.getAccesses().containsKey(symbol))//caso simbolo de uma atributo da classe
			return clas.getAccesses().get(symbol);
		return lookUpForExtenderAccess(clas.getExtender(), symbol);
	}
	
	public Type lookUp(Symbol symbol){
		if(currentMethod == null){
			if(currentClass != null){
				if(currentClass.getVariables().containsKey(symbol))
					return currentClass.getVariables().get(symbol);
				else return lookUpForExtender(currentClass.getExtender(), symbol);
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
		return lookUpForExtender(clas.getExtender(), symbol);
	}

	public static void print() {
		System.out.println("~~~~~~~~~~~~~TABELA DE SIMBOLOS~~~~~~~~~~~~~");
		Symbol[] cls = classes.keySet().toArray(new Symbol[1]);
		for(int i = 0; i < classes.size(); i++){
			System.out.println("Classe: " + cls[i].toString());
			System.out.println("Atributos:");
			Class cl = classes.get(cls[i]);
			Symbol[] var = cl.getVariables().keySet().toArray(new Symbol[1]);
			for(int j = 0; j < var.length; j++){
				if(var[j]!= null)System.out.println( var[j].toString()+" "+ cl.getVariables().get(var[j]).toString());
			}
			System.out.println("Metodos:");
			for(int j = 0; j < cl.getMethods().size(); j++){
					Method mt = cl.getMethods().elementAt(j);
					System.out.println("Nome: "+ mt.toString());
					System.out.println("Retorno: "+ mt.getReturnType());
					System.out.println("Variaveis:");
					Symbol[] var2 = mt.getVariables().keySet().toArray(new Symbol[1]);
					for(int k = 0; k < var2.length; k++){
						if(var2[k]!= null)System.out.println( var2[k].toString()+" "+ mt.getVariables().get(var2[k]).toString());
					}
			}
			System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
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
	
	public Method getMethodMain(Symbol classSymbol, Symbol methSymbol){
		if(classSymbol==null){
			Error.getInstance().addErro("Simbolo dessa classe não existe");
			return null;
		}
		if(!classes.containsKey(classSymbol)){
			return null;
		}
		Class clas = classes.get(classSymbol);
		if(clas.getMethodMain(methSymbol)!=null){  //this is in case of symbol's method
			return clas.getMethodMain(methSymbol);
		}
		return null;
	}
	public boolean isClass(Symbol s) {
		return classes.containsKey(s);
	}

	public boolean hasType(Symbol s) {
		return classes.containsKey(s);
	}
	
	public void addError(String s){
		String erro ="";
		if(currentClass!=null) erro = "[Classe:"+currentClass.getName()+"]";
		if(currentMethod!=null)erro = erro + "[Método:"+currentMethod.toString()+"]";
		erro = erro + " "+s;
		Error.getInstance().addErro(erro);
	}
	
}//end 

